package ga.decisions;



import java.util.Random;

public abstract class IntThresholdDecision extends DecisionNode {
	private int threshold;
		
	public IntThresholdDecision(int threshold) {
		this.threshold = threshold;
	}
	
	public static int GenRandom() {
		return (new Random().nextInt(25));
	}

	public IntThresholdDecision() {
		this(GenRandom());
	}
	
	public int getThreshold() {
		return this.threshold;
	}
	
	public String toString() {
		return this.getClass().getName() + " thresh=" + this.threshold;
	}
}
