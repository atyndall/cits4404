package ga.actions.concrete;

import ga.actions.ActionNodeDoubleAmount;

import java.util.Random;

public class Fire extends ActionNodeDoubleAmount {

	public Fire() {
		this.amt = GenRandom();
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(100));
	}
	
	@Override
	public void action() {
		this.getRobot().fire(this.getAmount());
	}

}
