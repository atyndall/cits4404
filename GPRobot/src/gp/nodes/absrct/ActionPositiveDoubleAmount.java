package gp.nodes.absrct;

import java.util.Random;

import gp.GABot;

public abstract class ActionPositiveDoubleAmount extends ActionNodeDoubleAmount {
	
	public ActionPositiveDoubleAmount(GABot r, double amt) {
		super(r);
		this.amt = Math.abs(amt);
	}
	
	public ActionPositiveDoubleAmount(GABot r) {
		this(r, GenRandom());
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(100));
	}
	
}
