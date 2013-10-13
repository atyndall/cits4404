package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionDegreeDoubleAmount;

public class TurnLeft extends ActionDegreeDoubleAmount {

	public TurnLeft(GABot r, double amt) {
		super(r, amt);
	}
	
	public TurnLeft(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().turnLeft(this.getAmount());
	}

}
