

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleFinishedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.BattlePausedEvent;
import robocode.control.events.BattleResumedEvent;
import robocode.control.events.BattleStartedEvent;
import robocode.control.events.IBattleListener;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.events.TurnStartedEvent;
import ga.Config; 
import ga.GATree; 
import ga.fitness.FitnessMeasure;
  
  
public class Playback { 
	
	// Run with command line params:
	// -Xmx512M -Dsun.io.useCanonCaches=false -DNOSECURITY=true
	// -Ddebug=true -Djava.security.manager
	// -Djava.security.policy==/path/to/.java.policy
	
	// .java.policy must be:
	// grant {
	//		permission java.security.AllPermission;
	// };
	
	// dependencies include "robocode" libraries, and Google Guava 12.0.1
	
	public static final String fileToRead = "/home/atyndall/workspace/cits4404/GPRobot/output/sample/gen_1.ser";
	public static final boolean summary = true; // generate summary data; run with true once, then with false once
  
	 
	 
	 private static List<ScoreInfo> fitSort(List<ScoreInfo> map) {
			List<ScoreInfo> l = new ArrayList<ScoreInfo>(map.size());
			
			while(map.size() > 0) {
				boolean maxSet = false;
				ScoreInfo maxVal = null;
				for (ScoreInfo e : map) {
					if (!maxSet || e.fitness > maxVal.fitness) {
						maxVal = e;
						maxSet = true;
					}
				}
				l.add(maxVal);
				map.remove(maxVal);
			}
			
			return l;
		}
	
	
	 
    public static void main(String[] args) throws IOException { 
        Map<GATree, FitnessMeasure> trees; 
        List<ScoreInfo> sscores;
        
        RobocodeEngine feng;
        BattlefieldSpecification battlefield;
        RobotSpecification[] specs;

        BattleSpecification battle;
          
        if (summary) {
        	try { 
                FileInputStream fileIn = new FileInputStream(fileToRead); 
                ObjectInputStream in = new ObjectInputStream(fileIn); 
                trees = (Map<GATree, FitnessMeasure>) in.readObject(); 
                in.close(); 
                fileIn.close(); 
            } catch(IOException i) { 
                i.printStackTrace(); 
                return; 
            } catch(ClassNotFoundException c) { 
                c.printStackTrace(); 
                return; 
            } 
        	
	       feng = new RobocodeEngine (new File(Config.get().robocodeLoc + 1));;
	       
	        int i = 0;
	        List<ScoreInfo> scores = new ArrayList<ScoreInfo>(trees.size());
	        System.out.println("Calculating fitness");
	        for (Entry<GATree, FitnessMeasure> t : trees.entrySet()) {
	        	battlefield = new BattlefieldSpecification();
	        	feng.addBattleListener(new ScorePrinter(i, t.getKey(), scores));
	        	specs = feng.getLocalRepository("ga.botthreads.ThreadedGABot"+1+"*,sample.Walls");
	            Config.toFile(Config.get().serializedLoc + 1 + ".ser", t.getKey());
	            battle = new BattleSpecification(15, 450, 0.1, battlefield, specs);
	            feng.runBattle(battle);
	            feng.waitTillBattleOver();
	            
	            System.out.print(".");
	            i++;
	        }
	        feng.close();
	        
	        
	        sscores = fitSort(scores);
	        
	        System.out.println("Top 5 scores");
	        for (int j = 0; j < 5; j++) {
	        	ScoreInfo s = sscores.get(j);
	        	System.out.println(j + ": " + s.fitness + " (" + s.ours + " vs " + s.theirs + " )");
	        }
	        
	        System.out.println("Bottom 5 scores");
	        for (int j = trees.size() - 1; j > (trees.size() - 5); j--) {
	        	ScoreInfo s = sscores.get(j);
	        	System.out.println(j + ": " + s.fitness + " (" + s.ours + " vs " + s.theirs + " )");
	        }
	        
	        Config.toFile(Config.get().outDir + "playback.ser", sscores);
	        System.out.println("Written out playback file, run simulation again with summary=false");
	        
        } else {
        	
        	try { 
                FileInputStream fileIn = new FileInputStream(Config.get().outDir + "playback.ser"); 
                ObjectInputStream in = new ObjectInputStream(fileIn); 
                sscores = (List<ScoreInfo>) in.readObject(); 
                in.close(); 
                fileIn.close(); 
            } catch(IOException i) {
            	System.out.println("Run with summary=true to get a playback file to use");
                i.printStackTrace(); 
                return; 
            } catch(ClassNotFoundException c) { 
                c.printStackTrace(); 
                return; 
            } 

	        while (true) {
	        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        	System.out.print("Enter Integer:");
	            String s = br.readLine();
		        int view = Integer.parseInt(s);
		        
		        feng = new RobocodeEngine (new File(Config.get().slowRobocodeLoc));
	        	battlefield = new BattlefieldSpecification();
	        	specs = feng.getLocalRepository("ga.botthreads.ThreadedGABot"+1+"*,sample.Walls");
		        battle = new BattleSpecification(3, 450, 0.1, battlefield, specs);
		        feng.setVisible(true);
		        Config.toFile(Config.get().serializedLoc + 1 + ".ser", sscores.get(view).t);
		        feng.runBattle(battle);
		        feng.waitTillBattleOver();
		        feng.close();
	        }
	        
	        //GASystem sys = new GASystem(true, true); 
	          
	        //System.out.println("Applying fitness function"); 
	        //sys.setFitness(trees.subList(1, 2)); 
	        //Map<GATree, Integer> allfit = sys.getFitness(); 
	        //System.out.println(); 
	          
	        //sys.fitnessStats(allfit); 
        }
    } 
    
    public static class ScoreInfo implements Serializable {
    	public final GATree t;
    	public final int num;
    	public final int fitness;
    	public final int theirs;
    	public final int ours;
    	
    	public ScoreInfo(int num, GATree t, int fitness, int theirs, int ours) {
    		this.num = num;
    		this.t = t;
    		this.fitness = fitness;
    		this.theirs = theirs;
    		this.ours = ours;
    	}
    }
    
    public static class ScorePrinter implements IBattleListener {
    	GATree t;
		List<ScoreInfo> map;
		int num;
    	
    	public ScorePrinter(int num, GATree t, List<ScoreInfo> map) {
    		this.num = num;
    		this.t = t;
    		this.map = map;
    	}
    	
    	private int fitnessFromResult(BattleResults[] br) {			
			long ourScore = Math.round(br[0].getScore());
			long theirScore = Math.round(br[1].getScore());

			if (ourScore == 0 && theirScore == 0) {
				return 0;
			} else if (ourScore == 0 && theirScore > 0) {
				return (int) (-1 * theirScore);
			} else if (theirScore == 0 && ourScore > 0) {
				return (int) ourScore;
			} else {
				return (int) (ourScore - theirScore);
			}
		}
    	
		@Override
		public void onBattleCompleted(BattleCompletedEvent arg0) {
			BattleResults[] bre = arg0.getIndexedResults();
			map.add(new ScoreInfo(num, t, fitnessFromResult(bre), bre[1].getScore(), bre[0].getScore()));
		}

		@Override
		public void onBattleError(BattleErrorEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBattleFinished(BattleFinishedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBattleMessage(BattleMessageEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBattlePaused(BattlePausedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBattleResumed(BattleResumedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBattleStarted(BattleStartedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRoundEnded(RoundEndedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRoundStarted(RoundStartedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTurnEnded(TurnEndedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTurnStarted(TurnStartedEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
} 