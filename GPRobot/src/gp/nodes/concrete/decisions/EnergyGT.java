package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class EnergyGT extends DoubleThresholdDecision {

	public EnergyGT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public EnergyGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEnergy() > this.getThreshold();
	}

}
