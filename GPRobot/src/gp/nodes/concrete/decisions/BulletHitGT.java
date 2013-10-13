package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class BulletHitGT extends IntThresholdDecision {

	public BulletHitGT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public BulletHitGT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getBulletHit() > this.getThreshold();
	}

}
