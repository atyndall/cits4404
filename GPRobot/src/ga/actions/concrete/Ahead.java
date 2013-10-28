package ga.actions.concrete;

import ga.actions.ActionNodeDoubleAmount;
import ga.Node;

public class Ahead extends ActionNodeDoubleAmount {

	public Ahead() { super(); }
	
	@Override
	public void action() {
		this.getRobot().setTurnGunLeft(this.getAmount());
	}

}
