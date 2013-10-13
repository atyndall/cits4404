package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class VelocityLT extends DoubleThresholdDecision {

	public VelocityLT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public VelocityLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getVelocity() < this.getThreshold();
	}

}
