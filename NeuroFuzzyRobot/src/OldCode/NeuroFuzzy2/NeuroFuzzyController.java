package NeuroFuzzy2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import NeuroFuzzy2.Genome;
import NeuroFuzzy2.Genome.NodeGene;
import NeuroFuzzy2.Genome.ConnectionGene;

public class NeuroFuzzyController {

	HashMap<Integer,Input> inputs;
	HashMap<Integer,Antecedent> antes;
	HashMap<Integer,Consequent> cons;
	HashMap<Integer,Output> outputs;
	
	public NeuroFuzzyController(Genome genome) {
		inputs = new HashMap<Integer,Input>(genome.numInputs);
		outputs = new HashMap<Integer,Output>(genome.numOutputs);
		antes = new HashMap<Integer,Antecedent>(genome.numAnte);
		cons = new HashMap<Integer,Consequent>(genome.numCons);
		
		for(int i = 0; i < genome.numInputs; i++) {
			inputs.put(i, new Input(0));
		}
		
		
		//double[][] weights = new double[numNodes][numNodes];
		for(NodeGene node : genome.nodes.values()) {
			if(node.type == Genome.ANTE) {
				List<Input> nodeInput = new ArrayList<Input>();
				List<ConnectionGene> conns = genome.getInputs(node.id);
				for(ConnectionGene gene : conns) {
					nodeInput.add(inputs.get(gene.out)); 
				}
				antes.put(node.id, new Antecedent(node.f,nodeInput.get(0)));
			} else if(node.type == Genome.CONS) {
				List<Antecedent> nodeInput = new ArrayList<Antecedent>();
				List<ConnectionGene> conns = genome.getInputs(node.id);
				for(ConnectionGene gene : conns) {
					nodeInput.add(antes.get(gene.out)); 
				}
				cons.put(node.id, new Consequent(node.f,nodeInput));
			}
		}
		
		for(int i = 0; i < genome.numOutputs; i++) {
			NodeGene node = genome.outputs.get(i);
			List<Consequent> nodeInput = new ArrayList<Consequent>();
			List<ConnectionGene> conns = genome.getInputs(node.id);
			for(ConnectionGene gene : conns) {
				nodeInput.add(cons.get(gene.out)); 
			}
			outputs.put(node.id, new Output(0,nodeInput));
		}
	}
	
	
	public double[] output(double[] input) {
		//prime input
		int i = 0;
		for(Input in : inputs.values()) {
			in.setValue(input[i]);
			i++;
		}
		//call layers
		for(Antecedent ante : antes.values()) {
			ante.output();
		}
		for(Consequent con : cons.values()) {
			con.output();
		}
		double[] output = new double[outputs.size()];
		i = 0;
		for(Output out : outputs.values()) {
			output[i] = out.output();
			i++;
		}
		return output;
	}
	
}
