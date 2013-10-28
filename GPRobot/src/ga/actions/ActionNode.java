package ga.actions;

import ga.Node;

public abstract class ActionNode extends Node {
	private Node childNode;
	
	public abstract void action();

	public void evaluate() {
		assert(getRobot() != null);
		action();
		if (childNode != null) childNode.evaluate();
	}

	public Node getChildNode() {
		return childNode;
	}

	public void setChildNode(Node childNode) {
		setChild(0, childNode);
	}

	public Node[] getChildren() {
		Node[] ret = new Node[1];
		ret[0] = childNode;
		return ret;
	}
	
	public void setChild(int index, Node n) {
		if (index == 0) {
			this.childNode = n;
		} else {
			throw new IllegalArgumentException("Only 1 child");
		}
	}
	
	public int numChildren() { return 1; }

}
