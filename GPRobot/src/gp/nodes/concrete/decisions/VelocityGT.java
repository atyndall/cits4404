package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class VelocityGT extends DoubleThresholdDecision {

	public VelocityGT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public VelocityGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getVelocity() > this.getThreshold();
	}

}
