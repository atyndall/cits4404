package ga.decisions.concrete;

import ga.decisions.InstantaneousDecision;




public class HitRobotTR extends InstantaneousDecision {

	public HitRobotTR() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isHitRobotThisRound();
	}

}
