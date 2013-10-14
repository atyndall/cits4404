package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;




public class GunHeatGT extends DoubleThresholdDecision {

	public GunHeatGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getGunHeat() > this.getThreshold();
	}

}
