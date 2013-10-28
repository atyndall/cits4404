package ga.actions;



import java.util.Random;

public abstract class ActionDegreeDoubleAmount extends ActionNodeDoubleAmount {

	public ActionDegreeDoubleAmount(double amt) {
		this.amt = amt % 360; // restruct movement
	}
	
	public ActionDegreeDoubleAmount() {
		this(GenRandom());
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(720) - 360);
	}

	
}
