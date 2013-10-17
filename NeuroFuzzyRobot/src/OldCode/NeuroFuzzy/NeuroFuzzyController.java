package NeuroFuzzy;

import NeuroFuzzy.Genome.NodeGene;
import NeuroFuzzy.Genome.ConnectionGene;

public class NeuroFuzzyController {

	int numInputs;
	int numOutputs;
	Neuron[][] layers;
	int[][] inputs; 	//specifies which nodes input to each either
						//i.e. inputs[0][1] is the number of the second input to node 0
						//these are only for ante, rule and conseq nodes
						//node numbering is relative to the network not the genome
	double[][] weights;
	
	public NeuroFuzzyController(int numInputs, int numOutputs) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
	}
	
	public static NeuroFuzzyController buildFromGenome(Genome genome) {
		NeuroFuzzyController cont = new NeuroFuzzyController(genome.numInputs,genome.numOutputs);
		
		Neuron[][] layers = new Neuron[3][];
		layers[0] = new Neuron[genome.numAnte];
		layers[1] = new Neuron[genome.numRule];
		layers[2] = new Neuron[genome.numCons];
		int a = 0;
		int r = 0;
		int c = 0;
		for(NodeGene node : genome.nodes.values()) {
			if(node.type == Genome.ANTE) {
				layers[0][a] = node.neuron;
				a++;
			} else if(node.type == Genome.RULE) {
				layers[1][r] = node.neuron;
				r++;
			} else if(node.type == Genome.CONS) {
				layers[2][c] = node.neuron;
				c++;
			}
		}
		cont.layers = layers;
		cont.weights = new double[genome.nodes.values().size()][genome.nodes.values().size()];
		
		for(ConnectionGene gene : genome.geneList) {
			if(gene.enabled)
				cont.weights[gene.out][gene.in] = gene.weight;
		}
		
		return cont;
	}
	
	public double[] output(double[] input) {
		for(int i = 0; i < layers.length; i++) {
			double[] output = new double[layers[i].length];
			for(int j = 0; j < layers[i].length; j++) {
				//filter out the input we actually want
				double[] nodeInput = new double[inputs[j].length];
				for(int k = 0; k < inputs[j].length; k++)
					nodeInput[k] = input[inputs[j][k]];
				output[j] = layers[i][j].output(nodeInput);
			}
			input = output;
		}
		return input;
	}
	
}
