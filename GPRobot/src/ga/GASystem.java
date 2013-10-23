package ga;



import ga.actions.ActionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class GASystem {

	private GATreeGenerator gen;
	private MatchPlayer mp;
	private Random rnd;
	
	private void mutateSwap(GATree t) {
		Random rnd = new Random();
		List<Node> nlist = t.getNodeList();
		int index = rnd.nextInt(nlist.size());
		
		Node n = nlist.get(index);
		// replace with random
		Node newn = n.getClass() == ActionNode.class ? gen.getRandomAction() : gen.getRandomDecision();
		for (int i = 0; i < n.getParent().numChildren(); i++) {
			if (n.getParent().getChildren()[i] == n) {
				n.getParent().setChild(i, newn);
				for (int j = 0; i < n.numChildren(); i++) {
					newn.setChild(j, n.getChildren()[j]);
				}
			}
		}
	}

	private void crossover(GATree t1, GATree t2) {
		Random rnd = new Random();
		List<Node> nlist1 = t1.getNodeList();
		List<Node> nlist2 = t2.getNodeList();
		
		int i1, i2;
		Node n1, n2;
		do {
			i1 = rnd.nextInt(nlist1.size());
			i2 = rnd.nextInt(nlist2.size());
			
			n1 = nlist1.get(i1);
			n2 = nlist2.get(i2);
		} while (n1.getParent() == null || n2.getParent() == null);
		
		int n1swap = -1;
		int n2swap = -1;
		
		for (int i = 0; i < n1.getParent().numChildren(); i++) {
			if (n1.getParent().getChildren()[i] == n1) {
				n1swap = i;
			}
		}
		
		for (int i = 0; i < n2.getParent().numChildren(); i++) {
			if (n2.getParent().getChildren()[i] == n2) {
				n2swap = i;
			}
		}
		
		assert (n1swap != -1 && n2swap != -1);
		
		n1.getParent().setChild(n1swap, n2);
		n2.getParent().setChild(n2swap, n1);
	
		
	}
	
	private Map<GATree, Integer> getFitnesses(List<GATree> trees) {
		Map<GATree, Integer> m = new HashMap<GATree, Integer>();
		for (GATree t : trees) {
			m.put(t, mp.fitness(t));
		}
		return m;
	}
	
	private static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
	private static double translate(int v, int lMin, int lMax, int rMin, int rMax) {
		double leftSpan = lMax - lMin;
		double rightSpan = rMax - rMin;
		
		double valueScaled = (double)(v - lMin) / (double)leftSpan;
		return rMin + (valueScaled * rightSpan);
	}
	
	private List<GATree> rouletteWheel(Map<GATree, Integer> fitnessMap) {
		SortedSet<Entry<GATree, Integer>> s = GASystem.entriesSortedByValues(fitnessMap);
		
		Map<GATree, Double> prob = new HashMap<GATree, Double>();
		for (int i = 0; i < s.size(); i++) {
			prob.put(s.last().getKey(), translate(i, 0, s.size(), 0, 1));
		}
		
		List<GATree> l = new ArrayList<GATree>();
		for (Map.Entry<GATree, Double> e : prob.entrySet()) {
			if (rnd.nextDouble() < e.getValue()) {
				l.add(e.getKey());
			}
		}
		
		return l;
	}
	
	private List<GATree> makeRandomTrees(int amt) {
		List<GATree> trees = new ArrayList<GATree>(amt);
		for (int i = 0; i < amt; i++)
			trees.add(gen.makeRandomTree());
		return trees;
	}
	
	public List<GATree> evolve(int generations) {
		System.out.println("Generating random trees");
		List<GATree> trees = makeRandomTrees(100);
		
		for (int x = 0; x < generations; x++) {
			System.out.println("GENERATION " + x);
			Collections.shuffle(trees);
			
			System.out.println("Applying mutations");
			for (GATree t : trees) {
				assert(t != null);
				if (rnd.nextDouble() > 0.95) {
					mutateSwap(t);
				}
			}
			
			System.out.println("Applying crossover");
			int max = trees.size();
			if (trees.size() % 2 != 0) {
				max -= 1;
			}
			for (int i = 0; i < max; i += 2) {
				crossover(trees.get(i), trees.get(i+1));
			}
			
			System.out.println("Applying fitness function");
			trees = rouletteWheel(getFitnesses(trees));
			
			assert(trees.size() >= 10);
		}
		
		return trees;
	}
	
	public GASystem() {
		this.gen = new GATreeGenerator();
		this.mp = new MatchPlayer();
		this.rnd = new Random();
	}
	
}
