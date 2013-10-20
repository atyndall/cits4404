package neuro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import robocode.AdvancedRobot;
import robocode.BattleRules;
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

public class NeuroBot extends AdvancedRobot {

	final static double maxAhead = 50;
	final static double radarTurnRate = Rules.RADAR_TURN_RATE_RADIANS;
	public final static int numInputs = 10;
	public final static int numOutputs = 3;
	
	NeuralNet nn;
	double[] input;
	
	public NeuroBot() {
		nn = null;
		input = new double[numInputs];
		
	}
	
	/**
	 * an initialiser full of stuff that we have to do after the robot has been constructed
	 */
	public void init() {
		//setAdjustGunForRobotTurn(true);
		setEventPriority("ScannedRobotEvent",99);
		setEventPriority("StatusEvent",98);
		nn = getControl();
		//Put the gun slightly ahead of the radar
		//turnRadarRight(-10);
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
		out.println("Starting a run");
		if(nn == null)
			init();
		do {
		
			//scan();
			printDoubleArray(input);
			double[] output = nn.output(input);
			printDoubleArray(output);
			setFire(scaleUp(output[0],Rules.MIN_BULLET_POWER,Rules.MAX_BULLET_POWER));
			setAhead(scaleUp(output[1],-maxAhead,maxAhead)); //negative means back
			//double gunTurn = scaleUp(output[2],-45,45);
			double roboTurn = scaleUp(output[2],-180,180);
			//setTurnGunLeft(gunTurn); //from -180 to 180 neg values means turn right
			setTurnLeft(roboTurn);
			//printDoubleArray(output);
			
			
			printBehaviour();
			//execute();
			
			
			//input = new double[numInputs];
		
		
			if(getGunTurnRemaining() == 0.0)
				setTurnGunRightRadians(Double.POSITIVE_INFINITY);
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
		out.println("Turn Remaining " + getTurnRemaining());
	}
	
	private double scaleUp(double controlSignal, double min, double max) {
		if(controlSignal > 0)
			return min + controlSignal*(max - min);
		else
			return 0;
	}
	
	private double scaleDown(double input, double min, double max) {
		return input/(max - min);
	}
	 
	public void onStatus(StatusEvent e) {
		out.println("Getting My Status");
		RobotStatus status = e.getStatus();
		input[0] = scaleDown(status.getEnergy(),0,100);
		input[1] = scaleDown(status.getGunHeading(),0,360);
		if(status.getGunHeat() > 0)
			input[2] = 1;
		input[3] = scaleDown(status.getHeading(),0,360);
		input[4] = scaleDown(status.getX(),0,getBattleFieldWidth());
		input[5] = scaleDown(status.getY(),0,getBattleFieldHeight());
		//run();
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		
		double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getGunHeadingRadians();
	 
	    setTurnGunRightRadians(Utils.normalRelativeAngle(radarTurn));
		
		out.println("Starting a scan");
		input[6] = scaleDown(e.getBearing(),0,360);
		input[7] = scaleDown(e.getDistance(),0,Rules.RADAR_SCAN_RADIUS);
		input[8] = scaleDown(e.getEnergy(),0,100);
		input[9] = scaleDown(e.getHeading(),0,360);
		
	    
		//run();
	    //scan();
	    // ...
	   // out.println("Finished Scan");
	}
	
	public void onSkippedTurn(SkippedTurnEvent e) {
		out.println("Skipped turn " + e.getSkippedTurn());
	}
}
