package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class HitRobotGT extends IntThresholdDecision {
	
	public HitRobotGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitRobot() > this.getThreshold();
	}

}
