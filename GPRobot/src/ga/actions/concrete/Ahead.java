package ga.actions.concrete;

import ga.actions.ActionNodeDoubleAmount;

public class Ahead extends ActionNodeDoubleAmount {

	public Ahead() { super(); }
	
	@Override
	public void action() {
		this.getRobot().turnGunLeft(this.getAmount());
	}

}
