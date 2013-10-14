package ga.actions;



import java.util.Random;

public abstract class ActionPositiveDoubleAmount extends ActionNodeDoubleAmount {
	
	public ActionPositiveDoubleAmount(double amt) {
		this.amt = Math.abs(amt);
	}
	
	public ActionPositiveDoubleAmount() {
		this(GenRandom());
	}
	
	public static double GenRandom() {
		return (double)(new Random().nextInt(100));
	}
	
}
