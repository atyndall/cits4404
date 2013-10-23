package neuro;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class ScanningBot extends AdvancedRobot {

	public ScanningBot() {
		
	}
	
	public void run() {
	    // ...
	 
	    turnGunRightRadians(Double.POSITIVE_INFINITY);
	    do {
	        // Check for new targets.
	        // Only necessary for Narrow Lock because sometimes our radar is already
	        // pointed at the enemy and our onScannedRobot code doesn't end up telling
	        // it to turn, so the system doesn't automatically call scan() for us
	        // [see the javadocs for scan()].
	        scan();
	    } while (true);
	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
	    double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();
	 
	    setTurnGunRightRadians(Utils.normalRelativeAngle(radarTurn)*2.0);
	 
	    // ...
	}
}
