package ga.actions.concrete;

import ga.actions.ActionDegreeDoubleAmount;





public class TurnGunLeft extends ActionDegreeDoubleAmount {

	public TurnGunLeft() { super(); }
	
	@Override
	public void action() {
		this.getRobot().turnGunLeft(this.getAmount());
	}

}
