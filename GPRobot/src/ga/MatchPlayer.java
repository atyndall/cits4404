package ga;

import ga.fitness.FitnessMeasure;
import ga.fitness.Score;

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
	
	public static final String[] robocodePaths = {
		Config.get().robocodeLoc + 1,
		Config.get().robocodeLoc + 2,
		Config.get().robocodeLoc + 3,
		Config.get().robocodeLoc + 4,
		Config.get().robocodeLoc + 5,
		Config.get().robocodeLoc + 6
	};
	
	Set<String> robots;
	
	public MatchPlayer() {
		this(false, false);
	}
	
	public MatchPlayer(boolean visible, boolean verbose) {
		this.visible = visible;
		this.verbose = verbose;
		this.treesToTest = new LinkedList<GATree>();
		this.robots = new HashSet<String>(Arrays.asList(robocodePaths));
	}
	
	public Map<GATree, FitnessMeasure> run() {
		BattleRunner br = new BattleRunner(robots, "-Xmx512M -Djava.security.manager -Djava.security.policy==/home/atyndall/.java.policy -Dsun.io.useCanonCaches=false -DNOSECURITY=true -Ddebug=true", 5, 800, 600);
		System.out.println();
		System.out.println("System initialized");
		Map<GATree, FitnessMeasure> m = new HashMap<GATree, FitnessMeasure>();
		List<GATree> testing = new LinkedList<GATree>(treesToTest);
		treesToTest.clear();
		br.runBattles(testing, new BotList("sample.Walls"), new ResultsHandler(m));
		br.shutdown();
		return m;
	}
	
	public void enqueueTree(GATree tree) {
		treesToTest.add(tree);
	}
	
	public class ResultsHandler implements BattleResultHandler {
		private Map<GATree, FitnessMeasure> res;
		
		public ResultsHandler(Map<GATree, FitnessMeasure> res) {
			this.res = res;
		}
		
		public void processResults(GATree t, List<RobotScore> robotScores,
				long elapsedTime) {
			res.put(t, new Score(robotScores)); // using the "Score" fitness measure
		}
		
	}
	
}
