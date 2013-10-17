package neuroFuzzy;

import java.util.List;


public class NodeGene implements java.io.Serializable {
	
	final static int INPUT = 0;
	final static int OUTPUT = 1;
	final static int HIDDEN = 2;
	
	int id;
	int type;
	List<NodeGene> possibleOutputs;
	
	public NodeGene(int id, int type, List<NodeGene> possibleOutputs) {
		this.id = id;
		this.type = type;
		this.possibleOutputs = possibleOutputs;
	}
	
	public String toString() {
		String out = "";
		out += id + " ";
		if(type == INPUT) {
			out += "INPUT ";
		} else if(type == OUTPUT) {
			out += "OUTPUT ";
		} else if(type == HIDDEN) {
			out += "HIDDEN ";
		}
		for(int i = 0; i < possibleOutputs.size(); i++) {
			NodeGene n = possibleOutputs.get(i);
			out += n.id;
			if(i < possibleOutputs.size()-1)
				out += ",";
		}
		return out;
	}
			
}
