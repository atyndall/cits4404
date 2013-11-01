package ga.botthreads;



import ga.Config;
import ga.EventsCounter;
import ga.GATree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.robotinterfaces.IBasicEvents;
import robocode.util.Utils;

/**
 * A simple robot that uses genetic programming to determine its moveset.
 * 
 * @author Ash Tyndall
 */
public class GABot extends AdvancedRobot implements IBasicEvents {
	private EventsCounter eventsCounter;
	private GATree actionTree;
	private FileOutputStream fout;
	
	public GABot(int num) {
		try {
			this.fout = new FileOutputStream(Config.logLoc + num + ".log");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		try {
			FileInputStream door = new FileInputStream(Config.serializedLoc + num + ".ser");
			ObjectInputStream reader = new ObjectInputStream(door);
			this.actionTree = (GATree) reader.readObject();
			assert(this.actionTree != null);
			reader.close();
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		this.actionTree.setRobot(this);
		this.eventsCounter = new EventsCounter();
	}
	
	public void run() {	
		//do stuff that you want to happen at the start of the round
		//also a good place for initialisation that can't happen outside the constructor
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAhead(Double.POSITIVE_INFINITY);
		do{
			
			eventsCounter.newRound();
			actionTree.evaluate();
			
			if(getRadarTurnRemaining() == 0.0)
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			
			//if(getGunTurnRemainingRadians() != 0)
			//	setTurnGunRightRadians(getGunTurnRemainingRadians());
			
			execute();
		} while(true);
	}
 

	
	public EventsCounter getEventsCounter() {
		return eventsCounter;
	}
	

 
	public void onScannedRobot(ScannedRobotEvent e) {
	    double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();
	    
	    double gunTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getGunHeadingRadians();
	 
	    setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
	    setTurnGunRightRadians(Utils.normalRelativeAngle(gunTurn));
	 
	    //do other things you want to do per scan
	    //you can do them before or after the above scanning code
	}
	
	public void onBulletHit(BulletHitEvent e) {
		eventsCounter.incBulletHit();
	}
	
	public void onBulletMissed(BulletMissedEvent e) {
		eventsCounter.incBulletMissed();
	}
	
	/**
	 * We do a little set of "safety" moves if something bad happens, to prevent complete system failure
	 */
	private void backoff() {
		int half = (int)Math.min(getBattleFieldHeight(), getBattleFieldWidth()) / 2;
		setStop();
		setBack(Utils.getRandom().nextInt(half) + 36);
		setTurnRight(Utils.getRandom().nextInt(360));
		setResume();
		setAhead(Double.POSITIVE_INFINITY);
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		eventsCounter.incHitByBullet();
	}
	
	public void onHitWall(HitWallEvent e) {
		backoff();
	}
	
	public void onHitRobot(HitRobotEvent e) {
		eventsCounter.incHitRobot();
		if (e.isMyFault()) {
			backoff();
		}
	}

}
