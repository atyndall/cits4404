import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import ga.Config;
import ga.GASystem;
import ga.GATree;


public class EvolveOut {

	public static void main(String[] args) {
		GASystem sys = new GASystem(false, false);
		System.out.println("Evolving for 100 generations");
		List<GATree> res = sys.evolve(100);
		
		
		
	}
}