package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionNode;

public class Stop extends ActionNode {

	public Stop(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().stop();
	}

}
