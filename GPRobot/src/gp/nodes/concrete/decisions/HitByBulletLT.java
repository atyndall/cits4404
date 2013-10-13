package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class HitByBulletLT extends IntThresholdDecision {

	public HitByBulletLT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public HitByBulletLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getHitByBullet() < this.getThreshold();
	}

}
