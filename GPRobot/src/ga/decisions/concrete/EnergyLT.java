package ga.decisions.concrete;

import ga.decisions.DoubleThresholdDecision;




public class EnergyLT extends DoubleThresholdDecision {

	public EnergyLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEnergy() < this.getThreshold();
	}


}
