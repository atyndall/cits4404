package ga.actions.concrete;

import ga.Node;
import ga.actions.ActionNodeDoubleAmount;

import java.util.Random;

public class Fire extends ActionNodeDoubleAmount {

	public Fire() {
		this.amt = GenRandom();
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(3));
	}
	
	@Override
	public void action() {
		double gunHeat = this.getRobot().getGunHeat();
		if (gunHeat == 0) {
			this.getRobot().setFire(this.getAmount());
		}
	}

}
