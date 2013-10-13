package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class HitRobotGT extends IntThresholdDecision {

	public HitRobotGT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public HitRobotGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitRobot() > this.getThreshold();
	}

}
