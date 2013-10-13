package gp.nodes.concrete.decisions;

import gp.GABot;
import gp.nodes.absrct.DoubleThresholdDecision;

public class GunHeatGT extends DoubleThresholdDecision {

	public GunHeatGT(GABot r, double threshold) {
		super(r, threshold);
	}
	
	public GunHeatGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getGunHeat() > this.getThreshold();
	}

}
