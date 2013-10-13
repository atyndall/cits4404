package gp.nodes.concrete.decisions;

import gp.nodes.absrct.InstantaneousDecision;
import gp.GABot;

public class HitRobotTR extends InstantaneousDecision {

	public HitRobotTR(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isHitRobotThisRound();
	}

}
