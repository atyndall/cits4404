
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
		System.out.println("Evolving for 5 generations");
		List<GATree> res = sys.evolve(5);
		
		try {
			FileOutputStream fileOut = new FileOutputStream(Config.finalOut);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(res);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + Config.finalOut);
	    } catch(IOException i) {
	        i.printStackTrace();
	    }
	}
}
