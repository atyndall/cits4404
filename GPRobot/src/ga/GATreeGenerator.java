package ga;



import ga.actions.ActionNode;
import ga.decisions.DecisionNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GATreeGenerator {
	
	private static String[] decisions = {
			"BulletHitGT",
			"BulletHitLT",
			"BulletHitTR",
			"BulletMissedTR",
			"EnergyGT",
			"EnergyLT",
			"GunHeatGT",
			"GunHeatLT",
			"HitByBulletGT",
			"HitByBulletLT",
			"HitByBulletTR",
			"HitRobotGT",
			"HitRobotLT",
			"HitRobotTR",
			"ScannedRobotTR",
			"VelocityGT",
			"VelocityLT",
	};
	
	private static String[] actions = {
			"Ahead",
			"Fire",
			//"Resume",
			//"Stop",
			"TurnGunLeft",
			"TurnLeft",
			"TurnRadarLeft",
	};
	
	private List<Constructor<DecisionNode>> decisionConstructors;
	private List<Constructor<ActionNode>> actionConstructors;
	private Random rnd;
	
	protected Node getRandomNode() {
		if (rnd.nextDouble() < 0.8) {
			return getRandomAction();
		} else {
			return getRandomDecision();
		}
	}
	
	protected ActionNode getRandomAction() {
		int index = rnd.nextInt(actions.length);
		Constructor<ActionNode> c = actionConstructors.get(index);
		
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
		int index = rnd.nextInt(decisions.length);
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
			decisionConstructors = new ArrayList<Constructor<DecisionNode>>(decisions.length);
			for (String c : decisions) {
				Class<DecisionNode> cls = (Class<DecisionNode>) Class.forName("ga.decisions.concrete." + c);
				decisionConstructors.add(cls.getConstructor());
			}

			actionConstructors = new ArrayList<Constructor<ActionNode>>(actions.length);
			for (String c : actions) {
				Class<ActionNode> cls = (Class<ActionNode>) Class.forName("ga.actions.concrete." + c);
				actionConstructors.add(cls.getConstructor());
			}		
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public GATree makeRandomTree() {
		GATree t = new GATree(makeRandomTree(getRandomNode(), 0, 3));
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
