package NeuroFuzzy2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Genome {

	public static final int ANTE = 0;
	public static final int OUTPUT = 1;
	public static final int CONS = 2;
	public static final int INPUT = 4;
	
	public List<NodeGene> inputs;
	public List<NodeGene> outputs;
	public HashMap<Integer,NodeGene> nodes;
	public List<ConnectionGene> geneList;
	
	int numInputs;
	int numAnte;
	int numOutputs;
	int numCons;
	
	public Genome(int numInputs, int numOutputs, double[] inLowerBounds, 
			double[] inUpperBounds, double[] outLowerBounds, double[] outUpperBounds) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		numAnte = Util.uniformInt(numInputs, numInputs*2);
		numCons = Util.uniformInt(numAnte, numAnte*2);
		nodes = new HashMap<Integer,NodeGene>();
		inputs = new ArrayList<NodeGene>();
		outputs = new ArrayList<NodeGene>();
		List<NodeGene> antes = new ArrayList<NodeGene>();
		List<NodeGene> cons = new ArrayList<NodeGene>();
		
		geneList = new ArrayList<ConnectionGene>();
		
		//Generate all the nodes
		
		for(int i = 0; i < numInputs; i++) {
			NodeGene gene = new NodeGene(i,Genome.INPUT,null,null,antes);
			inputs.add(gene);
		}
		
		//We make the outputs before the consequents because they need to refer to them
		for(int i = 0; i < numOutputs; i++) {
			NodeGene gene = new NodeGene(i,Genome.OUTPUT,null,cons,null);
			outputs.add(gene);
		}
		
		int nodeNum = 0;
		for(int i = 0; i < numAnte; i++) {
			NodeGene input = select(inputs);
			
			NodeGene gene = new NodeGene(nodeNum,Genome.ANTE, 
					new MembershipFunction(inLowerBounds[input.id], inUpperBounds[input.id], 1.0), inputs,cons);
			antes.add(gene);
			nodes.put(nodeNum,gene);
			
			geneList.add(new ConnectionGene(input.id, gene.id,
					Util.uniformDouble(0, 1),true,1));
			
			nodeNum++;
		}
			
		for(int	i = 0; i < numCons; i++) {
			NodeGene output = select(outputs);
			
			NodeGene gene = new NodeGene(nodeNum,Genome.CONS, 
					new MembershipFunction(outLowerBounds[output.id], outUpperBounds[output.id], 1.0), antes,outputs);
			
			geneList.add(new ConnectionGene(gene.id, output.id,Util.uniformDouble(0,1),true,1));
				
			cons.add(gene);
			nodes.put(nodeNum,gene);
			nodeNum++;
		}
		
		
		
		//Finish making the connections
		
		//For each node select its inputs
		for(NodeGene node : nodes.values()) {
			int num = 0;
			if(node.type == Genome.OUTPUT)
				num = Util.uniformInt(1, numCons);
			else if(node.type == Genome.CONS)
				num = Util.uniformInt(1, numAnte);
			
			for(int i = 0; i < num; i++) {
				geneList.add(new ConnectionGene(select(node.possibleInputs).id, node.id,
						Util.uniformDouble(0, 1),true,1));
			}
		}
		
	}
	
	public List<ConnectionGene> getInputs(int id) {
		List<ConnectionGene> ins = new ArrayList<ConnectionGene>();
		for(ConnectionGene gene : geneList) {
			if(gene.in == id)
				ins.add(gene);
		}
		return ins;
	}
	
	private static NodeGene select(List<NodeGene> genes) {
		return genes.get(Util.uniformInt(0, genes.size()-1));
	}
	
	public class ConnectionGene {
		
		int out;
		int in;
		double weight;
		boolean enabled;
		int innov;
		
		
		public ConnectionGene(int out, int in, double weight, boolean enabled,
				int innov) {
			this.out = out;
			this.in = in;
			this.weight = weight;
			this.enabled = enabled;
			this.innov = innov;
		}
	
		public String toString() {
			return out + "->" + in;
		}
		
	}
	
	public class NodeGene {
		
		int id;
		int type;
		MembershipFunction f;
		List<NodeGene> possibleInputs;
		List<NodeGene> possibleOutputs;
		
		public NodeGene(int id, int type, MembershipFunction f, List<NodeGene> possibleInputs,
				List<NodeGene> possibleOutputs) {
			this.id = id;
			this.type = type;
			this.f = f;
			this.possibleInputs = possibleInputs;
			this.possibleOutputs = possibleOutputs;
		}
		
		public String toString() {
			String typeString = "";
			if(type == Genome.ANTE)
				typeString = "ANTE";
			else if(type == Genome.CONS)
				typeString = "CONS";
			else if(type == Genome.INPUT)
				typeString = "INPUT";
			else if(type == Genome.OUTPUT)
				typeString = "OUTPUT";
			return id + " " + typeString;
		}
		
			
	}
	
}
