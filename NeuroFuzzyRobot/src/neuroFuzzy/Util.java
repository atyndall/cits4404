package neuroFuzzy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	public static double uniformDouble(double min, double max) {
		Random rand = new Random();
		return rand.nextDouble()*(max-min) + min;
	}

	public static int uniformInt(int min, int max) {
		Random rand = new Random();
		return min + rand.nextInt(max - min +1);
	}
	
	public static int choose(int[] a) {
		return a[Util.uniformInt(0, a.length)];
	}
	
	public static List<Double> zip(List<Double> a, List<Double> b) {
		List<Double> out = new ArrayList<Double>();
		for(int i = 0; i < Math.min(a.size(), b.size()); i++)
			out.add(a.get(i)*b.get(i));
		return out;
	}
	
	public static double newWeight() {
		return uniformDouble(-1,1);
	}
	
}
