package gp.nodes.absrct;

import java.util.Random;

import gp.GABot;

public abstract class DoubleThresholdDecision extends DecisionNode {
	private double threshold;
		
	public DoubleThresholdDecision(GABot r, double threshold) {
		super(r);
		this.threshold = threshold;
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(25));
	}
	
	public DoubleThresholdDecision(GABot r) {
		this(r, GenRandom());
	}
	
	public double getThreshold() {
		return this.threshold;
	}

	public String toString() {
		return this.getClass().getName() + " thresh=" + this.threshold;
	}
}
