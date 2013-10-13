package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class HitByBulletGT extends IntThresholdDecision {

	public HitByBulletGT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public HitByBulletGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitByBullet() > this.getThreshold();
	}

}
