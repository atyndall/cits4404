package ga.actions.concrete;

import java.util.Random;

import ga.actions.ActionDegreeDoubleAmount;





public class TurnLeft extends ActionDegreeDoubleAmount {

	public TurnLeft() {
		super(GenRandom());
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(20) - 10);
	}
	
	@Override
	public void action() {
		this.getRobot().setTurnLeft(this.getAmount());
	}

}
