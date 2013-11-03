

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import ga.Config;
import ga.GASystem;
import ga.GATree;


public class EvolveOut {
	
	public static final int n = Config.get().numGenerations;

	// Run with command line params:
	// -Xmx512M -Dsun.io.useCanonCaches=false -DNOSECURITY=true
	// -Ddebug=true -Djava.security.manager
	// -Djava.security.policy==/path/to/.java.policy
	
	// .java.policy must be:
	// grant {
	//		permission java.security.AllPermission;
	// };
	
	// dependencies include "robocode" libraries, and Google Guava 12.0.1
	
	public static void main(String[] args) throws IOException {
		Date date = new Date();
		File outdir = new File(Config.get().outDir + date.getTime());
		if (!outdir.mkdir()) throw new IOException("Couldn't make dir");
		
		System.out.println("Outputting data to " + outdir.getAbsolutePath());
		
		GASystem sys = new GASystem(outdir, false, false);
		System.out.println("Evolving for "+n+" generations");
		sys.evolve(n);
		
		
		
	}
}