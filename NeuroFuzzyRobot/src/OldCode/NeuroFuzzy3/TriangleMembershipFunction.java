package NeuroFuzzy3;

import java.util.Random;

public class TriangleMembershipFunction {

	double c;
	double sr;
	double sl;
	double cap;
	
	public TriangleMembershipFunction(double c, double sr, double sl, double cap) {
		this.c = c;
		this.sr = sr;
		this.sl = sl;
		this.cap = cap;
	}
	
	public TriangleMembershipFunction(double lowerBound, double upperBound, double cap) {
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
	



}
