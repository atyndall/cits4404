package ga;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
		try {
			FileInputStream door = new FileInputStream(Config.serializedLoc);
			ObjectInputStream reader = new ObjectInputStream(door);
			this.actionTree = (GATree) reader.readObject();
			assert(this.actionTree != null);
			reader.close();
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
			return;
		}
		
		this.actionTree.setRobot(this);
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

}
