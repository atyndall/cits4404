package gp.nodes.absrct;

import gp.GABot;


public abstract class Node {
	private GABot r;
	private Node parent;
	
	public Node(GABot r) {
		this.r = r;
	}
	
	public GABot getRobot() {
		return this.r;
	}
	
	public abstract void evaluate();
	
	public String toString() {
		return this.getClass().getName();
	}
	
	public abstract Node[] getChildren();
	
	public abstract void setChild(int index, Node n);
	
	public abstract int numChildren();
	
	public Node getParent() {
		return this.parent;
	}
	
	public void setParent(Node n) {
		this.parent = n;
	}
}
