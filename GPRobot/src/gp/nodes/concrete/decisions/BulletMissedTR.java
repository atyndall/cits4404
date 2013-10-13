package gp.nodes.concrete.decisions;

import gp.nodes.absrct.InstantaneousDecision;
import gp.GABot;

public class BulletMissedTR extends InstantaneousDecision {

	public BulletMissedTR(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isBulletMissedThisRound();
	}

}
