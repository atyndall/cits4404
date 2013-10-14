package ga.decisions;



import java.util.Random;

public abstract class DoubleThresholdDecision extends DecisionNode {
	private double threshold;
		
	public DoubleThresholdDecision(double threshold) {
		this.threshold = threshold;
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(25));
	}
	
	public DoubleThresholdDecision() {
		this(GenRandom());
	}
	
	public double getThreshold() {
		return this.threshold;
	}

	public String toString() {
		return this.getClass().getName() + " thresh=" + this.threshold;
	}
}
