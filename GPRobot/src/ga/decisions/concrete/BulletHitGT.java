package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class BulletHitGT extends IntThresholdDecision {
	
	public BulletHitGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getBulletHit() > this.getThreshold();
	}

}
