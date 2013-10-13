package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionDegreeDoubleAmount;

public class TurnGunLeft extends ActionDegreeDoubleAmount {

	public TurnGunLeft(GABot r, double amt) {
		super(r, amt);
	}
	
	public TurnGunLeft(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().turnGunLeft(this.getAmount());
	}

}
