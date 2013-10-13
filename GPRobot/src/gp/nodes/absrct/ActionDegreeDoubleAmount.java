package gp.nodes.absrct;

import java.util.Random;

import gp.GABot;

public abstract class ActionDegreeDoubleAmount extends ActionNodeDoubleAmount {

	public ActionDegreeDoubleAmount(GABot r, double amt) {
		super(r);
		this.amt = amt % 360; // restruct movement
	}
	
	public ActionDegreeDoubleAmount(GABot r) {
		this(r, GenRandom());
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(720) - 360);
	}
	
}
