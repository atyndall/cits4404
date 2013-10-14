package ga.actions.concrete;

import ga.actions.ActionNode;





public class Stop extends ActionNode {

	public Stop() { super(); }
	
	@Override
	public void action() {
		this.getRobot().stop();
	}

}
