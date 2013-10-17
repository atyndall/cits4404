package neuro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import robocode.AdvancedRobot;
import robocode.RobocodeFileOutputStream;
import robocode.RobotStatus;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.StatusEvent;
import robocode.util.Utils;

import neuroFuzzy.*;

public class NeuroBot extends AdvancedRobot {

	final static double maxAhead = 50;
	public final static int numInputs = 10;
	public final static int numOutputs = 4;
	
	NeuralNet nn;
	double[] input;
	
	public NeuroBot() {
		nn = null;
		input = new double[numInputs];
		
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
			nn = getControl();
		
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		scan();
		printDoubleArray(input);
		double[] output = nn.output(input);
		setFire(scale(output[0],Rules.MIN_BULLET_POWER,Rules.MAX_BULLET_POWER));
		setAhead(scale(output[1],-maxAhead,maxAhead)); //negative means back
		setTurnGunLeft(scale(output[2],-180,180)); //from -180 to 180 neg values means turn right
		setTurnLeft(scale(output[3],-180,180));
		printDoubleArray(output);
		
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		printBehaviour();
		while(getDistanceRemaining() > 0 && getTurnRemaining() > 0 && getGunTurnRemaining() > 0) {
			out.println("EXECUTING");
			execute();
		}
		//input = new double[numInputs];
		
		
	   // do {
	        // Check for new targets.
	        // Only necessary for Narrow Lock because sometimes our radar is already
	        // pointed at the enemy and our onScannedRobot code doesn't end up telling
	        // it to turn, so the system doesn't automatically call scan() for us
	        // [see the javadocs for scan()].
	        scan();
	    //} while (true);
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
	
	private double scale(double controlSignal, double min, double max) {
		if(controlSignal > 0)
			return min + controlSignal*(max - min);
		else
			return 0;
	}
	 
	public void onStatus(StatusEvent e) {
		out.println("Getting My Status");
		RobotStatus status = e.getStatus();
		input[0] = status.getEnergy();
		input[1] = status.getGunHeading();
		input[2] = status.getGunHeat();
		input[3] = status.getHeading();
		input[4] = status.getX();
		input[5] = status.getY();
		run();
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		out.println("Starting a scan");
		input[6] = e.getBearing();
		input[7] = e.getDistance();
		input[8] = e.getEnergy();
		input[9] = e.getHeading();
		
	    double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();
	 
	    setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
	    //scan();
	    // ...
	    out.println("Finished Scan");
	}
	
	public void onSkippedTurn(SkippedTurnEvent e) {
		out.println("Skipped turn " + e.getSkippedTurn());
	}
}
