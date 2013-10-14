package ga.decisions.concrete;

import ga.decisions.InstantaneousDecision;




public class HitByBulletTR extends InstantaneousDecision {

	public HitByBulletTR() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isHitByBulletThisRound();
	}

}
