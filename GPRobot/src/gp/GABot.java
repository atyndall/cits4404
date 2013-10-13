package gp;

import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 * A simple robot that uses genetic programming to determine its moveset.
 * 
 * @author Ash Tyndall
 */
public class GABot extends Robot {
	private EventsCounter eventsCounter;
	private GATree actionTree;
	
	public GABot() {
		this.eventsCounter = new EventsCounter();
	}
	
	public void run() {
		actionTree.evaluate();
		eventsCounter.newRound();
	}
	
	public EventsCounter getEventsCounter() {
		return eventsCounter;
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		eventsCounter.haveScannedRobotThisRound();
	}
	
	public void onBulletHit(BulletHitEvent e) {
		eventsCounter.incBulletHit();
	}
	
	public void onBulletMissed(BulletMissedEvent e) {
		eventsCounter.incBulletMissed();
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		eventsCounter.incHitByBullet();
	}
	
	public void onHitRobot(HitRobotEvent e) {
		eventsCounter.incHitRobot();
	}

	public GATree getActionTree() {
		return actionTree;
	}

	public void setActionTree(GATree actionTree) {
		this.actionTree = actionTree;
	}
	
}
