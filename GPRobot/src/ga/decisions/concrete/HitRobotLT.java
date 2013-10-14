package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class HitRobotLT extends IntThresholdDecision {
	
	public HitRobotLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitRobot() < this.getThreshold();
	}

}
