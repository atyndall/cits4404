package NeuroFuzzy2;

import java.util.Random;

public class Util {

	public static double uniformDouble(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble()*(max-min) + min;
	}

	public static int uniformInt(double min, double max) {
		Random rand = new Random();
		return (int)min + rand.nextInt((int)(max - min)+1);
	}
	
	public static int choose(int[] a) {
		Random rand = new Random();
		return a[Util.uniformInt(0, a.length)];
	}
	
}
