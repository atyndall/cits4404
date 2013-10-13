package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionDegreeDoubleAmount;

public class TurnRadarLeft extends ActionDegreeDoubleAmount {
	
	public TurnRadarLeft(GABot r, double amt) {
		super(r, amt);
	}
	
	public TurnRadarLeft(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().turnRadarLeft(this.getAmount());
	}

}
