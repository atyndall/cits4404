package ga.decisions.concrete;

import ga.decisions.DecisionNode;

public class GunHeatAllowFire extends DecisionNode {

	public GunHeatAllowFire() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getGunHeat() == 0;
	}

}
