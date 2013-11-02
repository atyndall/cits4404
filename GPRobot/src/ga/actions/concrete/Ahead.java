package ga.actions.concrete;

import java.util.Random;

import ga.actions.ActionNodeDoubleAmount;
import ga.Node;

public class Ahead extends ActionNodeDoubleAmount {

	public Ahead() {
		this.amt = GenRandom();
	}
	
	public static double GenRandom() {
		double maxX = 50;
		double minX = 0;
		return (double)(new Random().nextFloat() * (maxX - minX) + minX);
	}
	
	@Override
	public void action() {
		this.getRobot().setTurnGunLeft(this.getAmount());
	}

}
