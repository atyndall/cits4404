package ga.decisions.concrete;

import ga.decisions.InstantaneousDecision;




public class ScannedRobotTR extends InstantaneousDecision {
	
	public ScannedRobotTR() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isScannedRobotThisRound();
	}

}
