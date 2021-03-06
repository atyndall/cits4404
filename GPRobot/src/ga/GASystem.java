package ga;



import ga.actions.ActionNode;
import ga.fitness.FitnessMeasure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import robowiki.runner.RobotScore;

public class GASystem {

	private GATreeGenerator gen;
	private MatchPlayer mp;
	private Random rnd;
	private FileWriter csv;
	private File outdir;
	
	private void mutateSwap(GATree t) {
		Random rnd = new Random();
		List<Node> nlist = t.getNodeList();
		int index = rnd.nextInt(nlist.size());
		
		Node n = nlist.get(index);
		Node np = n.getParent();
		// replace with random
		Node newn = n.getClass() == ActionNode.class ? gen.getRandomAction() : gen.getRandomDecision();
		for (int i = 0; i < np.numChildren(); i++) {
			if (np.getChildren()[i] == n) {
				np.setChild(i, newn);
				newn.setParent(np);
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
		
		Node n1p = n1.getParent();
		Node n2p = n2.getParent();
		
		//System.out.println(Integer.toHexString(System.identityHashCode(n1)));
		//System.out.println(Integer.toHexString(System.identityHashCode(n2)));
		
		
		for (int i = 0; i < n1p.numChildren(); i++) {
			Node[] children = n1p.getChildren();
			//System.out.println(Integer.toHexString(System.identityHashCode(children[0])));
			if (children[i] == n1) {
				n1swap = i;
			}
		}
		
		for (int i = 0; i < n2p.numChildren(); i++) {
			Node[] children = n2p.getChildren();
			if (children[i] == n2) {
				n2swap = i;
			}
		}
		
		if (!(n1swap != -1 && n2swap != -1)) {
			System.out.println("A weird and bad thing happened when crossing over");
			return;
			//assert(false);
		}
		
		n1p.setChild(n1swap, n2);
		n2.setParent(n1p);
		n2p.setChild(n2swap, n1);
		n1.setParent(n2p);
	}
	
	public void setFitness(List<GATree> trees) {
		for (GATree t : trees) {
			mp.enqueueTree(t);
		}
	}
	
	public Map<GATree, FitnessMeasure> getFitness() {
		return mp.run();
	}
	
	public static class TreeFitness {
		public final GATree t;
		public final FitnessMeasure fitness;
		
		public TreeFitness(GATree t, FitnessMeasure fitness) {
			this.t = t;
			this.fitness = fitness;
		}

	}
	
	public static List<TreeFitness> fitSort(Map<GATree, FitnessMeasure> map) {
		List<TreeFitness> l = new ArrayList<TreeFitness>(map.size());
		
		while(map.size() > 0) {
			boolean maxSet = false;
			FitnessMeasure maxVal = null;
			GATree maxTree = null;
			for (Entry<GATree, FitnessMeasure> e : map.entrySet()) {
				if (!maxSet || e.getValue().getFitness() > maxVal.getFitness()) {
					maxVal = e.getValue();
					maxTree = e.getKey();
					maxSet = true;
				}
			}
			l.add(new TreeFitness(maxTree, maxVal));
			map.remove(maxTree);
		}
		
		return l;
	}
	
	private static double translate(int v, int lMin, int lMax, int rMin, int rMax) {
		double leftSpan = lMax - lMin;
		double rightSpan = rMax - rMin;
		
		double valueScaled = (double)(v - lMin) / (double)leftSpan;
		return rMin + (valueScaled * rightSpan);
	}
	
	public static TreeFitness[] rouletteWheel(Map<GATree, FitnessMeasure> fitnessMap) {
		List<TreeFitness> s = GASystem.fitSort(fitnessMap);
		
		Map<TreeFitness, Double> prob = new HashMap<TreeFitness, Double>();
		for (int i = 0; i < s.size(); i++) {
			prob.put(s.get(i), translate(i, 0, s.size(), 0, 1));
		}
		
		List<TreeFitness> l = new ArrayList<TreeFitness>();
		for (Map.Entry<TreeFitness, Double> e : prob.entrySet()) {
			if (new Random().nextDouble() < e.getValue()) {
				l.add(e.getKey());
			}
		}
		
		return l.toArray(new TreeFitness[l.size()]);
	}
	
	public static TreeFitness[] topX(Map<GATree, FitnessMeasure> fitnessMap, int x) {
		List<TreeFitness> s = GASystem.fitSort(fitnessMap);
		return s.subList(0,  x).toArray(new TreeFitness[x]);
	}
	
	private List<GATree> makeRandomTrees(int amt) {
		List<GATree> trees = new ArrayList<GATree>(amt);
		for (int i = 0; i < amt; i++)
			trees.add(gen.makeRandomTree());
		return trees;
	}
	
	// http://stackoverflow.com/questions/4191687/how-to-calculate-mean-median-mode-and-range-from-a-set-of-numbers
	private  static double mean(int[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	// the array double[] m MUST BE SORTED
	private static double median(int[] m) {
	    int middle = m.length/2;
	    if (m.length%2 == 1) {
	        return m[middle];
	    } else {
	        return (m[middle-1] + m[middle]) / 2.0;
	    }
	}
	
	private static int mode(int a[]) {
	    int maxValue = a[0], maxCount = a[0];

	    for (int i = 0; i < a.length; ++i) {
	        int count = 0;
	        for (int j = 0; j < a.length; ++j) {
	            if (a[j] == a[i]) ++count;
	        }
	        if (count > maxCount) {
	            maxCount = count;
	            maxValue = a[i];
	        }
	    }

	    return maxValue;
	}
	
	public List<GATree> evolve(int generations) {
		System.out.println("Generating random treesttt");
		
		setFitness(makeRandomTrees(Config.get().numGenomes));
		Map<GATree, FitnessMeasure> oldgen = getFitness();	
		List<GATree> nextgen = new ArrayList<GATree>(Config.get().numGenomes);
		
		for (int x = 0; x <= generations; x++) {
			System.out.println("GENERATION " + x);
			
			nextgen.clear();
			for (GATree t : oldgen.keySet()) nextgen.add(GATree.CopyTree(t));
			Collections.shuffle(nextgen);
			
			// TODO: Uncomment
			System.out.println("Applying mutations");
			for (GATree t : nextgen) {
				assert(t != null);
				if (rnd.nextDouble() > Config.get().mutationRate) {
					mutateSwap(t);
				}
			}
	
			System.out.println("Applying crossover");
			int max = nextgen.size();
			if (nextgen.size() % 2 != 0) {
				max -= 1;
			}
			for (int i = 0; i < max; i += 2) {
				crossover(nextgen.get(i), nextgen.get(i+1));
			}
			
			System.out.println("Applying fitness function");
			
			setFitness(nextgen);
			int r = Math.abs(Config.get().numGenomes-nextgen.size());
			if (nextgen.size() > Config.get().numGenomes) {
				System.out.println("Too many trees, removing "+r+" randomly");
				for (int i = 0; i < r; i++) nextgen.remove(0);
			} else if (nextgen.size() < Config.get().numGenomes) {
				System.out.println("Some trees short, generating "+r+" random ones");
				setFitness(makeRandomTrees(r));
			}
			
			Map<GATree, FitnessMeasure> newfit = getFitness();
			Map<GATree, FitnessMeasure> allfit = new HashMap<GATree, FitnessMeasure>(newfit);
			allfit.putAll(oldgen);
			
			System.out.println();
			fitnessStats(x, allfit);
			
			try {
				String outp = outdir.getAbsolutePath() + File.separator + "gen_" + x + ".ser";
				FileOutputStream fileOut = new FileOutputStream(outp);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(allfit);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved in " + outp);
		    } catch(IOException i) {
		        i.printStackTrace();
		        //System.exit(1);
		    }
			
			
			oldgen.clear();
			
			TreeFitness[] fit;
			switch (Config.get().fitnessMeasure) {
			case "rouletteWheel":
				fit = rouletteWheel(allfit);
				break;
				
			case "topX":
				fit = topX(allfit, Config.get().numGenomes);
				break;
				
			default:
				throw new RuntimeException("Unknown fitness measure");
			}
			
			for (TreeFitness t : fit) {
				oldgen.put(t.t, t.fitness);
			}
			System.out.println("Oldgen size; " + oldgen.size());
			
			//assert(trees.size() >= 10); // we don't have enough diversity, or the algo is borked
		}
		
		return nextgen;
	}
	
	public void fitnessStats(int generation, Map<GATree, FitnessMeasure> allfit) {
		int[] values = new int[allfit.size()];
		int i = 0;
		for (FitnessMeasure e : allfit.values()) {
			values[i] = e.getFitness();
			i++;
		}
		Arrays.sort(values);
		
		csvWrite(generation, mean(values), median(values), mode(values));
		System.out.println("Mean: " + mean(values) + ", Median: " + median(values) + ", Mode: " + mode(values));
	}
	
	private void csvWrite(Object ... lines) {
		try {
			for (Object l : lines) {
				
				if (l instanceof String) {
					String s = (String)l;
					this.csv.append("\"" + s.replaceAll("\"", "\"\"") + "\"");
				} else {
					this.csv.append("\"" + l + "\"");
				}
				this.csv.append(",");
				
			}
			this.csv.append(System.lineSeparator());
			this.csv.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GASystem(File outdir, boolean display, boolean verbose) throws IOException {
		this.outdir = outdir;
		File csv = new File(outdir.getAbsolutePath() + File.separator + "summary.csv");
		csv.createNewFile();
		
		this.csv = new FileWriter(csv);
		csvWrite("Generation", "Mean", "Median", "Mode");
		
		this.gen = new GATreeGenerator();
		
		Config.toFile(outdir.getAbsolutePath() + File.separator + "cfg.ser", Config.get());
		this.mp = new MatchPlayer(display, verbose);
		this.rnd = new Random();
	}
	
}
