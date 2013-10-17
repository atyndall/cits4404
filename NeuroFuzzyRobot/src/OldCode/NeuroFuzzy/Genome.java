package NeuroFuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Genome {

	public static final int ANTE = 0;
	public static final int CONS = 1;
	public static final int RULE = 2;
	public static final int INPUT = 4;
	
	HashMap<Integer,NodeGene> nodes;
	List<ConnectionGene> geneList;
	
	int numInputs;
	int numOutputs;
	int numAnte;
	int numCons;
	int numRule;
	
	public Genome(int numInputs, int numOutputs, double lowerBound, double upperBound, double cap) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		numAnte = Util.uniformInt(numInputs, numInputs*2);
		numCons = Util.uniformInt(numAnte, numAnte*2);
		numRule = numOutputs;
		nodes = new HashMap<Integer,NodeGene>(numAnte + numRule + numCons);
		List<NodeGene> inputs = new ArrayList<NodeGene>();
		List<NodeGene> antes = new ArrayList<NodeGene>();
		List<NodeGene> rules = new ArrayList<NodeGene>();
		List<NodeGene> cons = new ArrayList<NodeGene>();
		
		
		//Generate all the nodes
		int nodeNum = 0;
		for(int i = 0; i < numInputs; i++) {
			NodeGene gene = new NodeGene(nodeNum,Genome.INPUT, new Input(i),null,antes);
			nodes.put(nodeNum,gene);
			inputs.add(gene);
			nodeNum++;
		}
		
		for(int i = 0; i < numAnte; i++) {
			NodeGene gene = new NodeGene(nodeNum,Genome.ANTE, 
					new Antecedent(new MembershipFunction(lowerBound, upperBound, cap)),inputs,rules);
			antes.add(gene);
			nodes.put(nodeNum,gene);
			nodeNum++;
		}	
		for(int	i = 0; i < numRule; i++) {
			NodeGene gene = new NodeGene(nodeNum,Genome.RULE,new Rule(),antes,cons);
			rules.add(gene);
			nodes.put(nodeNum,gene);
			nodeNum++;
		}
		
		for(int i = 0; i < numCons; i++) {
			NodeGene gene = new NodeGene(nodeNum,Genome.CONS,
					new Consequent(new MembershipFunction(lowerBound, upperBound, cap)),rules,null);
			cons.add(gene);
			nodes.put(nodeNum,gene);
			nodeNum++;
		}
		
		//Randomise connections
		geneList = new ArrayList<ConnectionGene>();
		//For each node select its inputs
		for(NodeGene node : nodes.values()) {
			int num = 0;
			if(node.type == Genome.CONS)
				num = Util.uniformInt(1, numRule);
			else if(node.type == Genome.ANTE) 
				num = 1;
			else if(node.type == Genome.RULE)
				num = Util.uniformInt(1, numAnte);
			
			for(int i = 0; i < num; i++) {
				geneList.add(new ConnectionGene(select(node.possibleInputs).id, node.id,
						Util.uniformDouble(0, 1),true,1));
			}
		}
		
	}
	
	private static NodeGene select(List<NodeGene> genes) {
		return genes.get(Util.uniformInt(0, genes.size()));
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
		
	}
	
	public class NodeGene {
		
		int id;
		int type;
		Neuron neuron;
		List<NodeGene> possibleInputs;
		List<NodeGene> possibleOutputs;
		
		public NodeGene(int id, int type, Neuron neuron, List<NodeGene> possibleInputs,
				List<NodeGene> possibleOutputs) {
			this.id = id;
			this.type = type;
			this.neuron = neuron;
			this.possibleInputs = possibleInputs;
			this.possibleOutputs = possibleOutputs;
		}
		
		
			
	}
	
}
