package ga;



import ga.actions.ActionNode;
import ga.decisions.DecisionNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class GATreeGenerator {
	
	private static Map<String, Double> decisions = new HashMap<String, Double>() {
		{
		  //put("BulletHitGT", 		1.0);
		  //put("BulletHitLT", 		1.0);
			put("BulletHitTR", 		1.0);
			put("BulletMissedTR",	1.0);
		  //put("EnergyGT", 		1.0);
			put("EnergyLT", 		1.0);
		  //put("GunHeatGT",		0.0);
		  //put("GunHeatLT", 		0.0);
			put("GunHeatAllowFire",	1.0);
		  //put("HitByBulletGT", 	1.0);
		  //put("HitByBulletLT", 	1.0);
			put("HitByBulletTR", 	1.0);
		  //put("HitRobotGT", 		1.0);
			put("HitRobotLT", 		1.0);
			put("HitRobotTR", 		1.0);
			put("ScannedRobotTR", 	1.0);
		  //put("VelocityGT", 		1.0);
			put("VelocityLT", 		1.0);
		}
	};
	
	private static Map<String, Double> actions = new HashMap<String, Double>() {
		{
		    put("Ahead", 			1.0);
			put("Fire", 			1.0);
		    put("Resume", 			0.2);
		    put("Stop", 			0.2);
		    put("VaryGun", 			0.4);
		    put("TurnLeft", 		1.0);
		  //put("TurnRadarLeft", 	1.0);
		}
	};
	
	private List<Constructor<DecisionNode>> decisionConstructors;
	private List<Constructor<ActionNode>> actionConstructors;
	private List<Double> decisionProbs;
	private List<Double> actionProbs;
	
	private Random rnd;
	
	protected Node getRandomNode() {
		if (rnd.nextDouble() < 0.8) {
			return getRandomAction();
		} else {
			return getRandomDecision();
		}
	}
	
	protected ActionNode getRandomAction() {
		Constructor<ActionNode> c;
		double prob;
		double selectprob;
		
		do {
			int index = rnd.nextInt(actions.size());
			c = actionConstructors.get(index);
			prob = actionProbs.get(index);
			selectprob = rnd.nextDouble();
		} while (selectprob >= prob); // TODO verify accuracy
		
		
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	protected DecisionNode getRandomDecision() {
		int index = rnd.nextInt(decisions.size());
		Constructor<DecisionNode> c = decisionConstructors.get(index);
		
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void populateConstructorLists() {
		
		try {			
			decisionConstructors = new ArrayList<Constructor<DecisionNode>>(decisions.size());
			decisionProbs = new ArrayList<Double>(decisions.size());
			for (Entry<String, Double> e : decisions.entrySet()) {
				Class<DecisionNode> cls = (Class<DecisionNode>) Class.forName("ga.decisions.concrete." + e.getKey());
				decisionConstructors.add(cls.getConstructor());
				decisionProbs.add(e.getValue());
			}

			actionConstructors = new ArrayList<Constructor<ActionNode>>(actions.size());
			actionProbs = new ArrayList<Double>(actions.size());
			for (Entry<String, Double> e : actions.entrySet()) {
				Class<ActionNode> cls = (Class<ActionNode>) Class.forName("ga.actions.concrete." + e.getKey());
				actionConstructors.add(cls.getConstructor());
				actionProbs.add(e.getValue());
			}		
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public boolean treeHasCycles(GATree t) {
		return treeHasCycles(new HashSet<Node>(), t.getRoot());
	}
	
	private boolean treeHasCycles(Set<Node> seenNodes, Node t) {
		if (t != null) {
			if (seenNodes.contains(t)) {
				return true;
			} else {
				seenNodes.add(t);
				
				boolean hasCycles = false;
				for (Node c : t.getChildren()) {
					if (c != null) {
						if (treeHasCycles(seenNodes, c)) {
							hasCycles = true;
						}
					}
				}
				return hasCycles;
			}
		} else {
			return false;
		}
	}
	
	public GATree makeRandomTree() {
		GATree t;
		boolean hasCycles;
		do {
			t = new GATree(makeRandomTree(getRandomNode(), 0, 3));
			hasCycles = treeHasCycles(t);
			if (hasCycles) System.out.println("Found cycles, regen");
		} while (hasCycles);
		
		assert(t.getRoot() != null);
		return t;
	}
	
	private Node makeRandomTree(Node n, int depth, int maxdepth) {
		if (depth >= maxdepth || (depth > 1 && rnd.nextDouble() > 0.95) ) {
			return null;
		}
		
		for (int i = 0; i < n.numChildren(); i++) {
			Node rnode = getRandomNode();
			n.setChild(i, makeRandomTree(rnode, depth + 1, maxdepth));
			rnode.setParent(n);
		}
		
		return n;
	}
	
	public GATreeGenerator() {
		this.rnd = new Random();
		populateConstructorLists();
	}
	
}
