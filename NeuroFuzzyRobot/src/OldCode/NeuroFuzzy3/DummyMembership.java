package NeuroFuzzy3;

public class DummyMembership extends MembershipFunction {

	public DummyMembership() {
		
	}
	
	@Override
	double getMembership(double x) {
		return x;
	}

}
