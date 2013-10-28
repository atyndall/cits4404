package ga.actions.concrete;

import ga.actions.ActionDegreeDoubleAmount;





public class TurnLeft extends ActionDegreeDoubleAmount {

	public TurnLeft() { super(); }
	
	@Override
	public void action() {
		this.getRobot().setTurnLeft(this.getAmount());
	}

}
