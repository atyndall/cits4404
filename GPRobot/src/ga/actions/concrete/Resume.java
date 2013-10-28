package ga.actions.concrete;

import ga.actions.ActionNode;





public class Resume extends ActionNode {

	public Resume() { super(); }
	
	@Override
	public void action() {
		this.getRobot().setResume();
	}

}
