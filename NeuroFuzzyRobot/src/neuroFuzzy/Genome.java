package neuroFuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Genome implements java.io.Serializable {
	
	int numInputs;
	int numOutputs;
	List<NodeGene> nodes;
	List<ConnectionGene> connections;
	
	public Genome(int numInputs, int numOutputs, int maxHiddenLayers, int minHiddenNodes, int maxHiddenNodes, double connectionChance) {
		
		/*
		 * InputNeuron ids and connections are important but our "OutputNeuron"
		 * is just a dummy neuron to capture output
		 */
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		nodes = new ArrayList<NodeGene>();
		int nodeNum = 1;
		
		//Gen nodes from output to input
		int numHiddenLayers = Util.uniformInt(1, maxHiddenLayers);
		int numLayers = numHiddenLayers + 2;
		List<NodeGene> lastLayer = new ArrayList<NodeGene>();
		//Make the output layer
		for(int i = 0; i < numOutputs; i++) {
			NodeGene n =  new NodeGene(nodeNum, NodeGene.OUTPUT, new ArrayList<NodeGene>());
			nodes.add(n);
			lastLayer.add(n);
			nodeNum++;
		}
		
		//Make the hidden layers
		//For now we won't initialise layer skipping neurons
		int numHiddenNodes = Util.uniformInt(minHiddenNodes, maxHiddenNodes);
		int nodeCount = 0;
		for(int i = 0; i < numLayers; i++) {
			List<NodeGene> newLastLayer = new ArrayList<NodeGene>();
			int nodesThisLayer = 0;
			if(i == numLayers-1)
				nodesThisLayer = numHiddenNodes-nodeCount;
			else
				nodesThisLayer = Math.min(numHiddenNodes-nodeCount, Util.uniformInt(2, numHiddenNodes-nodeCount));
			for(int j = 0; j < nodesThisLayer; j++) {
				NodeGene n = new NodeGene(nodeNum, NodeGene.HIDDEN, lastLayer);
				nodes.add(n);
				newLastLayer.add(n);
				nodeNum++;
			}
			lastLayer = newLastLayer;
		}
		
		//Make the input layer
		for(int i = 0; i < numInputs; i++) {
			NodeGene n = new NodeGene(nodeNum, NodeGene.INPUT, lastLayer);
			nodes.add(n);
			nodeNum++;
		}
		
		//Make the connections
		connections = new ArrayList<ConnectionGene>();
		//Every neuron needs at least one output, except the output layer
		for(NodeGene n : nodes) {
			System.out.println("DOING CONNECTIONS FOR " + n.toString());
			for(int i = 0; i < n.possibleOutputs.size(); i++) {
				if(Util.uniformDouble(0, 1) < connectionChance) {
					NodeGene in = n.possibleOutputs.get(i);
					ConnectionGene g = new ConnectionGene(n.id,in.id,Util.uniformDouble(0,1),true,1);
					connections.add(g);
				}
			}
		}
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
	
}
