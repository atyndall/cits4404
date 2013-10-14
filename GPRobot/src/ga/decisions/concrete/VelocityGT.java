package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;




public class VelocityGT extends DoubleThresholdDecision {

	public VelocityGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getVelocity() > this.getThreshold();
	}

}
