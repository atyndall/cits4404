package neuroEvolution;

import java.util.ArrayList;
import java.util.List;


public class NodeGene implements java.io.Serializable {
	
	public final static int INPUT = 0;
	public final static int OUTPUT = 1;
	public final static int HIDDEN = 2;
	public final static int BIAS = 3;
	
	public int id;
	public int type;
	public List<NodeGene> possibleOutputs;
	
	public NodeGene(int id, int type, List<NodeGene> possibleOutputs) {
		this.id = id;
		this.type = type;
		this.possibleOutputs = new ArrayList<NodeGene>(possibleOutputs);
	}
	
	@Override
	public boolean equals(Object o) {
		NodeGene n = (NodeGene)o;
		return this.id == n.id && this.type == n.type;
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
	
	public void addOutput(NodeGene g) {
		possibleOutputs.add(g);
	}
	
	
			
}
