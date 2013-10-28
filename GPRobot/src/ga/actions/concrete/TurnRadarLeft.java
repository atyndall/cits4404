package ga.actions.concrete;

import ga.actions.ActionDegreeDoubleAmount;





public class TurnRadarLeft extends ActionDegreeDoubleAmount {

	public TurnRadarLeft() { super(); }
	
	@Override
	public void action() {
		this.getRobot().setTurnRadarLeft(this.getAmount());
	}

}
