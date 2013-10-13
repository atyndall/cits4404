package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class EnergyLT extends DoubleThresholdDecision {

	public EnergyLT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public EnergyLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEnergy() < this.getThreshold();
	}


}
