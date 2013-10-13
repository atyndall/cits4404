package gp.nodes.concrete.actions;

import gp.GABot;
import gp.nodes.absrct.ActionNode;

public class Resume extends ActionNode {

	public Resume(GABot r) {
		super(r);
	}

	@Override
	public void action() {
		this.getRobot().resume();
	}

}
