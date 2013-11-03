package neuro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BattleRules;
import robocode.BulletHitEvent;
import robocode.Event;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobocodeFileOutputStream;
import robocode.Robot;
import robocode.RobotStatus;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.StatusEvent;
import robocode.util.Utils;

import neuroEvolution.Genome;
import neuroFuzzy.*;

public class NeuroStrafingBot extends AdvancedRobot {

	final static double maxAhead = 20;
	public final static int numInputs = 2;
	public final static int numOutputs = 1;
	public static int numHits;
	public int direction = 1;
	public int collideDir = 1;
	
	
	NeuralNet nn;
	double[] input;
	
	public NeuroStrafingBot() {
		nn = null;
		input = new double[numInputs];
		numHits = 0;
	}
	
	/**
	 * an initialiser full of stuff that we have to do after the robot has been constructed
	 */
	public void init() {
		
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setEventPriority("ScannedRobotEvent",99);
		setEventPriority("StatusEvent",98);
		nn = getControl();
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
			
			if(output[0] > 0)
				direction = 1;
			else
				direction = -1;
			setAhead(maxAhead*direction*collideDir);
			
			
		
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
		input[0] = scaleDown(e.getDistance(),0,Rules.RADAR_SCAN_RADIUS);
		input[1] = scaleDown(e.getEnergy(),0,Rules.MAX_VELOCITY);
		
		setTurnRight((e.getBearing() + 90)*direction);
	    
		//run();
	    //scan();
	    // ...
	   // out.println("Finished Scan");
	}
	
	@Override
	public void onBulletHit(BulletHitEvent e) {
		System.out.println("HIT");
		numHits++;
	}
	
	@Override
	public void onBattleEnded(BattleEndedEvent e) {
		
	}
	
	public void onSkippedTurn(SkippedTurnEvent e) {
		out.println("Skipped turn " + e.getSkippedTurn());
	}
	
	public void onHitWall(HitWallEvent e) {
		collideDir *= -1;
	}
	
	public void onHitRobot(HitRobotEvent e) {
		collideDir *= -1;
	}
	
}


