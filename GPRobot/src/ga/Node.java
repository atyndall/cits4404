package ga;



import java.io.Serializable;


public abstract class Node implements Serializable {
	private GABot r;
	private Node parent;
	
	public GABot getRobot() {
		return this.r;
	}
	
	public void setRobot(GABot r) {
		this.r = r;
		for (Node c : this.getChildren()) {
			if (c != null) c.setRobot(r);
		}
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
