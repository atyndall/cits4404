package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionPositiveDoubleAmount;

public class Fire extends ActionPositiveDoubleAmount {

	public Fire(GABot r, double amt) {
		super(r, amt);
	}
	
	public Fire(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().fire(this.getAmount());
	}

}
