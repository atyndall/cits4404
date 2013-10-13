package gp.nodes.absrct;

import java.util.Random;

import gp.GABot;

public abstract class IntThresholdDecision extends DecisionNode {
	private int threshold;
		
	public IntThresholdDecision(GABot r, int threshold) {
		super(r);
		this.threshold = threshold;
	}
	
	public static int GenRandom() {
		return (new Random().nextInt(25));
	}

	public IntThresholdDecision(GABot r) {
		this(r, GenRandom());
	}
	
	public int getThreshold() {
		return this.threshold;
	}
	
	public String toString() {
		return this.getClass().getName() + " thresh=" + this.threshold;
	}
}
