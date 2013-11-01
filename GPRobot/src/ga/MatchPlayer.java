package ga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
		for (int j = 1; j <= numThreads; j++) {
			List<GATree> nl = new LinkedList<GATree>();
			for (int i = 0; i < numPer; i++) {
				if (treesToTest.size() == 0) break;
				nl.add(treesToTest.remove());
			}
			
			if (j == numThreads && treesToTest.size() != 0) {
				for (GATree t : treesToTest) nl.add(t);
				treesToTest.clear();
			}
			
			if (nl.size() > 0) {
				queuedTrees.add(nl.toArray(new GATree[nl.size()]));
			}
		}
		
		List<CallableMatchPlayer> matches = new LinkedList<CallableMatchPlayer>();
		
		List<Future<Map<GATree, Integer>>> futures = new ArrayList(numThreads);
		int num = 1;
		for (GATree[] trees : queuedTrees) {
			futures.add(pool.submit(new CallableMatchPlayer(visible, verbose, trees, num)));
		}
		
		
//		try {
//			futures = pool.invokeAll(matches);
//			//pool.awaitTermination(10, TimeUnit.MINUTES);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			System.exit(1);
//			return null;
//		}
//		
		Map<GATree, Integer> m = new HashMap<GATree, Integer>();
		
		
		for(Future<Map<GATree, Integer>> f : futures) {
			try {
				Map<GATree, Integer> ret = f.get();
				m.putAll(ret);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		//boolean allDone = false;
		//while (!allDone) {
			//allDone = true;
			//for(Future<Map<GATree, Integer>> f : futures) {
				//if (f.isDone()) {
				//	try {
				//		m.putAll(f.get());
				//		System.out.println("Thread complete");
				//	} catch (InterruptedException | ExecutionException e) {
				//		e.printStackTrace();
				//		return null;
				//	}
				//} else {
				//	allDone = false;
				//}
		//	}
		//}
		
		return m;
	}
	
	public void enqueueTree(GATree tree) {
		treesToTest.add(tree);
	}
	
}
