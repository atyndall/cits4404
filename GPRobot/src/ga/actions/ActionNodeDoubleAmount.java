package ga.actions;




public abstract class ActionNodeDoubleAmount extends ActionNode {
	protected double amt;
	
	public double getAmount() {
		return this.amt;
	}
	
	public String toString() {
		return this.getClass().getName() + " amt=" + this.amt;
	}

}
