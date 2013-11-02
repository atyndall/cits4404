import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import ga.Config;
import ga.GASystem;
import ga.GATree;


public class EvolveOut {
	
	public static final int n = Config.get().numGenerations;

	public static void main(String[] args) throws IOException {
		Date date = new Date();
		File outdir = new File(Config.get().outDir + date.getTime());
		if (!outdir.mkdir()) throw new IOException("Couldn't make dir");
		
		System.out.println("Outputting data to " + outdir.getAbsolutePath());
		
		GASystem sys = new GASystem(outdir, false, false);
		System.out.println("Evolving for "+n+" generations");
		List<GATree> res = sys.evolve(n);
		
		
		
	}
}