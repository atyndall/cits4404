package ga.actions.concrete;

import java.util.Random;

import ga.actions.ActionDegreeDoubleAmount;





public class VaryGun extends ActionDegreeDoubleAmount {

	public VaryGun() {
		this.amt = GenRandom();
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(10) - 5);
	}
	
	@Override
	public void action() {
		this.getRobot().setGunTurnVariance(this.getAmount());
	}

}
