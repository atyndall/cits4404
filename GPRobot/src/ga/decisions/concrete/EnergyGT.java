package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;




public class EnergyGT extends DoubleThresholdDecision {

	public EnergyGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEnergy() > this.getThreshold();
	}

}
