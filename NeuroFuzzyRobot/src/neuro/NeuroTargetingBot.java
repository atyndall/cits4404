package neuro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BattleRules;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.Event;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobocodeFileOutputStream;
import robocode.RobocodeFileWriter;
import robocode.Robot;
import robocode.RobotStatus;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.StatusEvent;
import robocode.util.Utils;
import test.BattleListener;

import neuroEvolution.Genome;
import neuroFuzzy.*;

public class NeuroTargetingBot extends AdvancedRobot {

	final static double maxAhead = 100;
	final static double radarTurnRate = Rules.RADAR_TURN_RATE_RADIANS;
	final static double gunTurnRate = Rules.GUN_TURN_RATE_RADIANS;
	final static double bulletPower = 1;
	public final static int numInputs = 3;
	public final static int numOutputs = 1;
	public static int numHits = 0;
	public static int numFired = 0;
	
	NeuralNet nn;
	double[] input;
	
	public NeuroTargetingBot() {
		nn = null;
		input = new double[numInputs];
		
	}
	
	/**
	 * an initialiser full of stuff that we have to do after the robot has been constructed
	 */
	public void init() {
		
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		//setEventPriority("ScannedRobotEvent",99);
		//setEventPriority("StatusEvent",98);
		nn = getControl();
		//Put the gun slightly ahead of the radar
		//turnRadarRight(-10);
		numHits = 0;
		numFired = 0;
	}
	
	private NeuralNet getControl() {
		
		Genome g = null;
		try {
			FileInputStream fileIn = new FileInputStream(getDataFile("genome.ser"));
			ObjectInputStream in = new ObjectInputStream(fileIn);
			g = (Genome) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new NeuralNet(g);
	}
	
	@Override
	public void run() {
		if(nn == null)
			init();
		do {
			printDoubleArray(input);
			double[] output = nn.output(input);
			printDoubleArray(output);
			
			double gunTurn = scaleUp(output[0],-Math.PI,Math.PI);
			System.out.println("Gun Turn: " + gunTurn);
			setTurnGunLeftRadians(gunTurn); //from -180 to 180 neg values means turn right
			setFire(bulletPower);
			//printBehaviour();
			//execute();
			
			
			
		
			//radar control
			if(getRadarTurnRemaining() == 0.0)
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			execute();
			
		} while(true);
	}
	
	private void printDoubleArray(double input[]) {
		for(int i = 0; i < input.length; i++) {
			out.print(input[i]);
			if(i < input.length-1)
				out.print(",");
		}
		out.println();
	}
	
	private void printBehaviour() {
		out.println("Gun Turn Remaining " + getGunTurnRemaining());
	}
	
	//controlSignal between 0 and 1
	private double scaleUp(double controlSignal, double min, double max) {
		return min + controlSignal*(max-min);
	}
	
	private double scaleDown(double input, double min, double max) {
		return input/(max - min);
	}
	
	public void onStatus(StatusEvent e) {
		out.println("Getting My Status");
		RobotStatus status = e.getStatus();
		//input[0] = scaleDown(status.getEnergy(),0,100);
		input[0] = scaleDown(status.getGunHeadingRadians(),0,2*Math.PI);
		input[1] = scaleDown(status.getHeadingRadians(),0,2*Math.PI);
		/*
		if(status.getGunHeat() > 0)
			input[1] = 1;
		else
			input[1] = 0;
			*/
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		//input = new double[numInputs];
		double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();
	 
	    setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
		
		out.println("Starting a scan");
		input[2] = scaleDown(e.getBearingRadians(),-Math.PI,Math.PI);
		//input[3] = scaleDown(e.getDistance(),0,Rules.RADAR_SCAN_RADIUS);
		//input[4] = scaleDown(e.getVelocity(),0,Rules.MAX_VELOCITY);
		//input[5] = scaleDown(e.getHeadingRadians(),0,2*Math.PI);
		
	    
		//run();
	    //scan();
	    // ...
	   // out.println("Finished Scan");
	}
	
	@Override
	public void onBulletHit(BulletHitEvent e) {
		System.out.println("HIT");
		numHits++;
		numFired++;
	}
	
	@Override
	public void onBulletMissed(BulletMissedEvent e) {
		numFired++;
	}
	
	@Override
	public void onBulletHitBullet(BulletHitBulletEvent e) {
		numFired++;
	}
	
	@Override
	public void onBattleEnded(BattleEndedEvent e) {
		try {
			RobocodeFileWriter fileIn = new RobocodeFileWriter(getDataFile("accuracy.dat"));
			fileIn.write(numHits + ",");
			fileIn.write(numFired + "\n");
			fileIn.close();
		} catch (FileNotFoundException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
			
		} catch (IOException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
			System.out.println(x.getLocalizedMessage());
		}
	}
	
	public void onSkippedTurn(SkippedTurnEvent e) {
		out.println("Skipped turn " + e.getSkippedTurn());
	}
}
