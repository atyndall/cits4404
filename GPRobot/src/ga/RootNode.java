package ga;

public class RootNode extends Node {
	Node n;
	
	public RootNode(Node firstNode) {
		this.n = firstNode;
	}
	
	@Override
	public void evaluate() {
		if (this.n != null)	this.n.evaluate();
	}

	@Override
	public Node[] getChildren() {
		Node[] ret = {this.n};
		return ret;
	}

	@Override
	public void setChild(int index, Node n) {
		if (index == 0) {
			this.n = n;
		} else {
			throw new IllegalArgumentException("Only 1 child");
		}
	}

	@Override
	public int numChildren() {
		return 1;
	}
}
