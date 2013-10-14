package ga.decisions.concrete;

import ga.decisions.IntThresholdDecision;




public class HitByBulletGT extends IntThresholdDecision {

	public HitByBulletGT() { super(); }
	
	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitByBullet() > this.getThreshold();
	}

}
