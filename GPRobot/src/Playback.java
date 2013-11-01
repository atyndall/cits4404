import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
  
import ga.Config; 
import ga.GASystem; 
import ga.GATree; 
import ga.MatchPlayer; 
  
  
public class Playback { 
  
    public static void main(String[] args) { 
        List<GATree> trees; 
  
        try { 
            FileInputStream fileIn = new FileInputStream(Config.finalOut); 
            ObjectInputStream in = new ObjectInputStream(fileIn); 
            trees = (List<GATree>) in.readObject(); 
            in.close(); 
            fileIn.close(); 
        } catch(IOException i) { 
            i.printStackTrace(); 
            return; 
        } catch(ClassNotFoundException c) { 
            System.out.println("Employee class not found"); 
            c.printStackTrace(); 
            return; 
        } 
          
        GASystem sys = new GASystem(true, true); 
          
        System.out.println("Applying fitness function"); 
        Map<GATree, Integer> allfit = sys.getFitnesses(trees.subList(1, 2)); 
        System.out.println(); 
          
        sys.fitnessStats(allfit); 
    } 
} 
