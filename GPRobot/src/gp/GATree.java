package gp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import gp.nodes.absrct.ActionNode;
import gp.nodes.absrct.DecisionNode;
import gp.nodes.absrct.Node;

public class GATree {
	private GABot robot;
	private Node root;
	
	private static String[] decisions = {
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
			"Fire",
			"Resume",
			"Stop",
			"TurnGunLeft",
			"TurnLeft",
			"TurnRadarLeft",
	};
	
	public static void PrintGATree(GATree t) {
		PrintNode(t.getRoot());
	}
	
	private static void PrintNode(Node n) {
		System.out.println(n);
		for (Node c : n.getChildren()) {
			if (c != null) PrintNode(c);
		}
	}
	
	private List<Constructor<DecisionNode>> decisionConstructors;
	private List<Constructor<ActionNode>> actionConstructors;
	private Random rnd;
	
	protected Node getRandomNode() {
		if (rnd.nextBoolean()) {
			return getRandomAction();
		} else {
			return getRandomDecision();
		}
	}
	
	protected ActionNode getRandomAction() {
		int index = rnd.nextInt(actions.length);
		Constructor<ActionNode> c = actionConstructors.get(index);
		
		try {
			return c.newInstance(this.robot);
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
			return c.newInstance(this.robot);
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
			Class<?>[] ptypes = {Class.forName("gp.GPBot")};
			
			decisionConstructors = new ArrayList<Constructor<DecisionNode>>(decisions.length);
			for (String c : decisions) {
				Class<DecisionNode> cls = (Class<DecisionNode>) Class.forName("gp.nodes.concrete.decisions." + c);
				decisionConstructors.add(cls.getConstructor(ptypes));
			}

			actionConstructors = new ArrayList<Constructor<ActionNode>>(actions.length);
			for (String c : actions) {
				Class<ActionNode> cls = (Class<ActionNode>) Class.forName("gp.nodes.concrete.actions." + c);
				actionConstructors.add(cls.getConstructor(ptypes));
			}		
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void makeRandomTree() {
		this.root = makeRandomTree(getRandomNode(), 0, 10);
	}
	
	private Node makeRandomTree(Node n, int depth, int maxdepth) {
		if (depth >= maxdepth || rnd.nextDouble() > 0.95) {
			return null;
		}
		
		for (int i = 0; i < n.numChildren(); i++) {
			Node rnode = getRandomNode();
			n.setChild(i, makeRandomTree(rnode, depth + 1, maxdepth));
			rnode.setParent(n);
		}
		
		return n;
	}
	
	public GATree(GABot robot) {
		this.robot = robot;
		this.rnd = new Random();
		populateConstructorLists();
	}
	
	public void setRoot(DecisionNode n) {
		this.root = n;
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public void evaluate() {
		root.evaluate();
	}
	
	public Node[] getNodeList() {
		LinkedList<Node> l = new LinkedList<Node>();
		getNodeList(l, this.root);
		return (Node[]) l.toArray();
	}
	
	private void getNodeList(List<Node> l, Node n) {
		for (Node c : n.getChildren()) {
			if (c != null) {
				l.add(c);
				getNodeList(l, c);
			}
		}
	}
	
}
