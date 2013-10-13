package gp.nodes.concrete.decisions;

import gp.nodes.absrct.IntThresholdDecision;
import gp.GABot;

public class BulletHitLT extends IntThresholdDecision {

	public BulletHitLT(GABot r, int threshold) {
		super(r, threshold);
	}
	
	public BulletHitLT(GABot r) {
		super(r);
	}

	@Override
	public boolean decision() {
		return this.getRobot().getEventsCounter().getBulletHit() < this.getThreshold();
	}

}
