package ga;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import robocode.BattleResults;
import robocode.control.*;
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

public class CallableMatchPlayer implements Callable<Map<GATree, Integer>> {
	BattlefieldSpecification battlefield;
	BattleSpecification battle;
	RobocodeEngine engine;
	BattleResults[] curResults;
	GATree[] trees;
	int[] allResults;
	boolean verbose;
	boolean visible;
	int num;
	
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
	
	public CallableMatchPlayer(GATree[] trees, int num) {
		this(false, false, trees, num);
	}
	
	public CallableMatchPlayer(boolean visible, boolean verbose, GATree[] trees, int num) {
		this.trees = trees;
		this.verbose = verbose;
		this.visible = visible;
		this.num = num;
	}
	
	@Override
	public Map<GATree, Integer> call() {
		//System.out.println(num + ":Happened once");
		this.engine = new RobocodeEngine (new File(Config.robocodeLoc + num));
		this.battlefield = new BattlefieldSpecification();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		RobotSpecification[] specs = this.engine.getLocalRepository("ga.botthreads.ThreadedGABot"+num+"*,sample.Walls");
		boolean badness = this.engine == null || this.battlefield == null || specs == null;
		this.battle = new BattleSpecification(1, 450, 0.1, this.battlefield, specs);
		
		//System.out.println(num + "fff");
		MatchListener ml = new MatchListener();
		
		engine.addBattleListener(ml);
		engine.setVisible(visible);
		
		Map<GATree, Integer> m = new HashMap<GATree, Integer>();
		
		//System.out.println(num + "ggg");
		for (int i = 0; i < trees.length; i++) {
			toFile(Config.serializedLoc + num + ".ser", trees[i]);
			engine.runBattle(battle);
			
			engine.waitTillBattleOver();
			//System.out.println(num + "fff1");

			int res = ml.getFitness();
			m.put(trees[i], res);
		}
		
		//System.out.println(num + "ff324f");
		
		this.engine.close();
		
		//System.out.println(num + "ff324f44444");
		return m;
	}

	


	private class MatchListener implements IBattleListener {
		private BattleResults[] curResults;
		
		public void onBattleCompleted(BattleCompletedEvent event) {
			BattleResults[] br = event.getIndexedResults();
			assert(br.length == 2);
			if (verbose) {
				System.out.println("Battle complete; " + br[0].getScore() + " (us) vs " + br[1].getScore() + " (them) - fitness: " + fitnessFromResult(br));
			} else {
				System.out.print(".");
			}
			curResults = br;
		}

		private int fitnessFromResult(BattleResults[] br) {
			int ourScore = br[0].getScore();
			int theirScore = br[1].getScore();
			if (ourScore == 0 && theirScore == 0) {
				return 0;
			} else if (ourScore == 0 && theirScore > 0) {
				return -1 * theirScore;
			} else if (theirScore == 0 && ourScore > 0) {
				return ourScore;
			} else {
				return ourScore - theirScore;
			}
		}
		
		public int getFitness() {
			if (curResults != null) {
				return fitnessFromResult(curResults);
			} else {
				return -1;
			}
		}

		public void onBattleError(BattleErrorEvent event) {
			System.out.println("!!!!! Robocode battle ended abruptly;\n" + event.getError());
			curResults = null;
		}
		
		public void onBattleFinished(BattleFinishedEvent arg0) { }
		public void onBattleMessage(BattleMessageEvent arg0) { }
		public void onBattlePaused(BattlePausedEvent arg0) { }
		public void onBattleResumed(BattleResumedEvent arg0) { }
		public void onBattleStarted(BattleStartedEvent arg0) { }
		public void onRoundEnded(RoundEndedEvent arg0) { }
		public void onRoundStarted(RoundStartedEvent arg0) { }
		public void onTurnEnded(TurnEndedEvent arg0) { }
		public void onTurnStarted(TurnStartedEvent arg0) { }
	}
	
	
}
