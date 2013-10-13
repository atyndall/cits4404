package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class GunHeatLT extends DoubleThresholdDecision {

	public GunHeatLT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public GunHeatLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getGunHeat() < this.getThreshold();
	}

}
