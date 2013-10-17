package NeuroFuzzy3;

public abstract class MembershipFunction {

	abstract double getMembership(double x);
	
	public double or(MembershipFunction f, double x){
		return or(this,f,x);
	}
	
	public static double or(MembershipFunction f, MembershipFunction g, double x) {
		return Math.max(f.getMembership(x), g.getMembership(x));
	}
	
	public static double or(MembershipFunction[] fs, double x) {
		double max = Double.MIN_VALUE;
		for(int i = 1; i < fs.length; i++) {
			max = or(fs[i-1],fs[i],x);
		}
		return max;
	}
	
}
