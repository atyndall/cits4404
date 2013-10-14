package ga.decisions;

import ga.Node;




public abstract class DecisionNode extends Node {

	private Node trueNode;
	private Node falseNode;
	
	public abstract boolean decision();
	
	public void evaluate() {
		assert(getRobot() != null);
		if (this.decision()) {
			if (trueNode != null) trueNode.evaluate();
		} else {
			if (trueNode != null) falseNode.evaluate();
		}
	}
	
	public Node getTrueNode() {
		return this.trueNode;
	}
	
	public Node getFalseNode() {
		return this.trueNode;
	}
	
	public void setTrueNode(Node newN) {
		setChild(0, newN);
	}
	
	public void setFalseNode(Node newN) {
		setChild(1, newN);
	}
	
	public Node[] getChildren() {
		Node[] ret = {trueNode, falseNode};
		return ret;
	}
	
	public void setChild(int index, Node n) {
		if (index == 0) {
			this.trueNode = n;
		} else if (index == 1) {
			this.falseNode = n;
		} else {
			throw new IllegalArgumentException("Only 2 children");
		}
	}
	
	public int numChildren() { return 2; }
}
