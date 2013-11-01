package ga;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatchPlayer {
	boolean verbose;
	boolean visible;
	Queue<GATree> treesToTest;
	
	public static final int numThreads = 6;
	private ExecutorService pool;
	
	public MatchPlayer() {
		this(false, false);
	}
	
	public MatchPlayer(boolean visible, boolean verbose) {
		this.visible = visible;
		this.verbose = verbose;
		this.treesToTest = new LinkedList<GATree>();
		this.pool = Executors.newFixedThreadPool(numThreads);
	}
	
	public Map<GATree, Integer> run() {
		int numPer = treesToTest.size() / numThreads;
		List<GATree[]> queuedTrees = new LinkedList<GATree[]>();
		while (treesToTest.size() != 0) {
			List<GATree> nl = new LinkedList<GATree>();
			for (int i = 0; i < numPer; i++) {
				if (treesToTest.size() == 0) break;
				nl.add(treesToTest.remove());
			}
			if (nl.size() > 0)
				queuedTrees.add(nl.toArray(new GATree[nl.size()]));
		}
		
		List<CallableMatchPlayer> matches = new LinkedList<CallableMatchPlayer>();
		
		int num = 1;
		for (GATree[] trees : queuedTrees) {
			matches.add(new CallableMatchPlayer(true, verbose, trees, num));
			num++;
		}
		
		List<Future<Map<GATree, Integer>>> futures;
		try {
			futures = pool.invokeAll(matches);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
		Map<GATree, Integer> m = new HashMap<GATree, Integer>();
		
		boolean allDone = false;
		while (!allDone) {
			allDone = true;
			for(Future<Map<GATree, Integer>> f : futures) {
				if (f.isDone()) {
					try {
						m.putAll(f.get());
						System.out.println("Some tasks complete");
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					allDone = false;
				}
			}
		}
		
		return m;
	}
	
	public void enqueueTree(GATree tree) {
		treesToTest.add(tree);
	}
	
}
