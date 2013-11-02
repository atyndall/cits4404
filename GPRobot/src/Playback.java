import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  





import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import ga.Config; 
import ga.GASystem; 
import ga.GATree; 
import ga.MatchPlayer; 
  
  
public class Playback { 
  
	 public static void toFile(String path, Object obj) {
         try {
                 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
                 out.writeObject(obj);
                 out.close();
         } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
                 System.exit(1);
         }
	 }
	
    public static void main(String[] args) { 
        List<GATree> trees; 
  
        try { 
            FileInputStream fileIn = null;//new FileInputStream(Config.finalOut + 11 + ".ser"); 
            ObjectInputStream in = new ObjectInputStream(fileIn); 
            trees = (List<GATree>) in.readObject(); 
            in.close(); 
            fileIn.close(); 
        } catch(IOException i) { 
            i.printStackTrace(); 
            return; 
        } catch(ClassNotFoundException c) { 
            System.out.println("Employee class not found"); 
            c.printStackTrace(); 
            return; 
        } 
          
        RobocodeEngine engine = new RobocodeEngine (new File(Config.slowRobocodeLoc));
        BattlefieldSpecification battlefield = new BattlefieldSpecification();
        try {
                Thread.sleep(100);
        } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
        }
        RobotSpecification[] specs = engine.getLocalRepository("ga.botthreads.ThreadedGABot"+1+"*,sample.Walls");

        BattleSpecification battle = new BattleSpecification(1, 450, 0.1, battlefield, specs);
        

        engine.setVisible(true);
        
        toFile(Config.serializedLoc + 1 + ".ser", trees.get(4));
        engine.runBattle(battle);
        
        engine.waitTillBattleOver();
        
        //GASystem sys = new GASystem(true, true); 
          
        //System.out.println("Applying fitness function"); 
        //sys.setFitness(trees.subList(1, 2)); 
        //Map<GATree, Integer> allfit = sys.getFitness(); 
        //System.out.println(); 
          
        //sys.fitnessStats(allfit); 
    } 
} 