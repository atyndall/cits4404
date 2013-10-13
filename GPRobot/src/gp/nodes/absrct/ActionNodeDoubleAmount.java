package gp.nodes.absrct;

import gp.GABot;

public abstract class ActionNodeDoubleAmount extends ActionNode {
	protected double amt;
	
	public ActionNodeDoubleAmount(GABot r) {
		super(r);
	}
	
	public double getAmount() {
		return this.amt;
	}
	
	public String toString() {
		return this.getClass().getName() + " amt=" + this.amt;
	}

}
