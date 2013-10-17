package NeuroFuzzy;

import java.util.Random;

public class MembershipFunction {

	double c;
	double sr;
	double sl;
	double cap;
	
	public MembershipFunction(double c, double sr, double sl, double cap) {
		this.c = c;
		this.sr = sr;
		this.sl = sl;
		this.cap = cap;
	}
	
	public MembershipFunction(double lowerBound, double upperBound, double cap) {
		this(Util.uniformDouble(lowerBound,upperBound),
				Util.uniformDouble(lowerBound,upperBound),Util.uniformDouble(lowerBound,upperBound),cap);	
	}
	
	public double getMembership(double x) {
		if(x >= c && x <= c + sr)
			return cap - Math.abs(x-c)/sr;
		else if(x <= c && x >= c - sl)
			return cap - Math.abs(x-c)/sl;
		else
			return 0;
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
