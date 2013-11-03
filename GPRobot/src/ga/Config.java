package ga;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class Config implements Serializable {
	public final String comment = "First version using the dynamic chance depth method";
	
	public final double mutationRate = 0.95;
	public final double randomNodeChance = 0.8;
	
	public final int genMinNodes = 3;
	public final int genMaxNodes = 10;
	
	public final String fitnessMeasure = "topX";
	
	public final int numGenerations = 100;
	public final int numGenomes = 100;
	
	public final String opponent = "sample.Walls";
	
	public final Map<String, Double> decisions = new HashMap<String, Double>() {
		{
		    put("BulletHitGT", 		1.0);
		    put("BulletHitLT", 		1.0);
			put("BulletHitTR", 		1.0);
			put("BulletMissedTR",	1.0);
		    put("EnergyGT", 		1.0);
			put("EnergyLT", 		1.0);
		  //put("GunHeatGT",		0.0);
		  //put("GunHeatLT", 		0.0);
	        put("GunHeatAllowFire",	1.0);
		    put("HitByBulletGT", 	1.0);
		    put("HitByBulletLT", 	1.0);
			put("HitByBulletTR", 	1.0);
		    put("HitRobotGT", 		1.0);
			put("HitRobotLT", 		1.0);
			put("HitRobotTR", 		1.0);
		    put("ScannedRobotTR", 	1.0);
		    put("VelocityGT", 		1.0);
			put("VelocityLT", 		1.0);
		}
	};
	
	public final Map<String, Double> actions = new HashMap<String, Double>() {
		{
		    put("Ahead", 			1.0);
			put("Fire", 			1.0);
		    put("Resume", 			0.2);
		    put("Stop", 			0.2);
		    put("VaryGun", 			0.6);
		    put("TurnLeft", 		1.0);
		  //put("TurnRadarLeft", 	1.0);
		}
	};
	
	public final String serializedLoc = "/home/atyndall/workspace/cits4404/actiontree";
	public final String logLoc = "/home/atyndall/workspace/cits4404/gabot";
	public final String outDir = "/home/atyndall/workspace/cits4404/output/";
	public final String robocodeLoc = "/home/atyndall/robocode-bin/robocodes/r";
	public final String slowRobocodeLoc = "/home/atyndall/robocode-bin/robocode";
	
	
	
	
	private static Config cfgclass;
	
	public static Config get() {
		if (cfgclass == null) cfgclass = new Config();
		return cfgclass;
	}
	
	
	public static void toFile(String path, Object obj) {
        try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
                out.writeObject(obj);
                out.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(1);
        }
	 }
}
