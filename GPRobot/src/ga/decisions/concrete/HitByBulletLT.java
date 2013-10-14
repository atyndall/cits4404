package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class HitByBulletLT extends IntThresholdDecision {

	public HitByBulletLT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitByBullet() < this.getThreshold();
	}

}
