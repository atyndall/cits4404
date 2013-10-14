package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;




public class GunHeatLT extends DoubleThresholdDecision {

	public GunHeatLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getGunHeat() < this.getThreshold();
	}

}
