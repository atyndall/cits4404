package gp.nodes.concrete.decisions;

import gp.nodes.absrct.InstantaneousDecision;
import gp.GABot;

public class BulletHitTR extends InstantaneousDecision {

	public BulletHitTR(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isBulletHitThisRound();
	}

}
