package ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import robocode.BattleResults;
import robowiki.runner.BattleRunner;
import robowiki.runner.BattleRunner.BattleResultHandler;
import robowiki.runner.BotList;
import robowiki.runner.RobotScore;

public class MatchPlayer {
	boolean verbose;
	boolean visible;
	Queue<GATree> treesToTest;
	BattleRunner br;
	
	public static final String[] robocodePaths = {
		Config.robocodeLoc + 1,
		Config.robocodeLoc + 2,
		Config.robocodeLoc + 3,
		Config.robocodeLoc + 4,
		Config.robocodeLoc + 5,
		Config.robocodeLoc + 6
	};
	
	public MatchPlayer() {
		this(false, false);
	}
	
	public MatchPlayer(boolean visible, boolean verbose) {
		this.visible = visible;
		this.verbose = verbose;
		this.treesToTest = new LinkedList<GATree>();
		Set<String> robots = new HashSet<String>(Arrays.asList(robocodePaths));
		this.br = new BattleRunner(robots, "-Xmx512M -Djava.security.manager -Djava.security.policy==/home/atyndall/.java.policy -Dsun.io.useCanonCaches=false -DNOSECURITY=true -Ddebug=true", 5, 800, 600);
	}
	
	public Map<GATree, Integer> run() {
		Map<GATree, Integer> m = new HashMap<GATree, Integer>();
		System.out.println(treesToTest.size());
		br.runBattles((List<GATree>)treesToTest, new BotList("sample.Walls"), new resultsHandler(m));
		
		return m;
	}
	
	public void enqueueTree(GATree tree) {
		treesToTest.add(tree);
	}
	
	private class resultsHandler implements BattleResultHandler {
		private Map<GATree, Integer> res;
		
		public resultsHandler(Map<GATree, Integer> res) {
			this.res = res;
		}
		
		public void processResults(GATree t, List<RobotScore> robotScores,
				long elapsedTime) {
			int score = fitnessFromResult(robotScores);
			//System.out.println("Score: " + score);
			res.put(t, score);
		}
		
		private int fitnessFromResult(List<RobotScore> br) {
			int theirs = -1;
			int ours = -1;
			
			if (br.get(0).botName.equals("sample.Walls")) {
				theirs = 0;
				ours = 1;
			} else if (br.get(1).botName.equals("sample.Walls")) {
				theirs = 1;
				ours = 0;
			} else {
				throw new RuntimeException("Don't know whats going on");
			}
			
			long ourScore = Math.round(br.get(ours).score);
			long theirScore = Math.round(br.get(theirs).score);

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
		
	}
	
}
