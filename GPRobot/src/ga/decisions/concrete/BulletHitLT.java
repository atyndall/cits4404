package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class BulletHitLT extends IntThresholdDecision {

	public BulletHitLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getBulletHit() < this.getThreshold();
	}

}
