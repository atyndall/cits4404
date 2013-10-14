package ga.decisions.concrete;

import ga.decisions.InstantaneousDecision;




public class BulletHitTR extends InstantaneousDecision {

	public BulletHitTR() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isBulletHitThisRound();
	}

}
