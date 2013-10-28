package neuro;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class ScanningBot extends AdvancedRobot {

	public ScanningBot() {
		
	}
	
	
	
	public void run() {
	    //do stuff that you want to happen at the start of the round
		//also a good place for initialisation that can't happen outside the constructor
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		do{
			
			//set everything else you want the robot to do this turn
			//you need to use the "set" methods, everything will be called
			//in execute
			
			if(getRadarTurnRemaining() == 0.0)
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			execute();
		} while(true);
	}
	 
	public void onScannedRobot(ScannedRobotEvent e) {
	    double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();
	 
	    setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
	 
	    //do other things you want to do per scan
	    //you can do them before or after the above scanning code
	}
}
