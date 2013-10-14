package ga.decisions.concrete;

import ga.decisions.InstantaneousDecision;




public class BulletMissedTR extends InstantaneousDecision {

	public BulletMissedTR() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().isBulletMissedThisRound();
	}

}
