package gp.nodes.concrete.decisions;

import gp.nodes.absrct.InstantaneousDecision;
import gp.GABot;

public class HitByBulletTR extends InstantaneousDecision {

	public HitByBulletTR(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isHitByBulletThisRound();
	}

}
