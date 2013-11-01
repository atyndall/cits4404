package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;


public class VelocityLT extends DoubleThresholdDecision {

	public VelocityLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getVelocity() < this.getThreshold();
	}

}
