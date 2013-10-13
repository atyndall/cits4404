package gp.nodes.concrete.decisions;

import gp.nodes.absrct.InstantaneousDecision;
import gp.GABot;

public class ScannedRobotTR extends InstantaneousDecision {

	public ScannedRobotTR(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isScannedRobotThisRound();
	}

}
