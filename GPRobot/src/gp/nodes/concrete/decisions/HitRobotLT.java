package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class HitRobotLT extends IntThresholdDecision {

	public HitRobotLT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public HitRobotLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitRobot() < this.getThreshold();
	}

}
