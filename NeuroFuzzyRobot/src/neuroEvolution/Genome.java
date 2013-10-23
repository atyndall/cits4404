package neuroEvolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import neuroFuzzy.Util;


public class Genome implements java.io.Serializable {
	
	public int numInputs;
	public int numOutputs;
	public List<NodeGene> nodes;
	public List<ConnectionGene> connections;
	public double fit;
	public int nodeNum = 1;
	public static int globalInnov = 1;
	
	public double getFit() {
		return fit;
	}

	public void setFit(double fit) {
		this.fit = fit;
	}

	
	public Genome(int numInputs, int numOutputs, List<NodeGene> nodes,
			List<ConnectionGene> connections, double fit) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.nodes = new ArrayList<NodeGene>(nodes);
		this.connections = new ArrayList<ConnectionGene>(connections);
		this.fit = fit;
	}

	public Genome(int numInputs, int numOutputs, int maxHiddenLayers, int minHiddenNodes, int maxHiddenNodes, double connectionChance) {
		
		/*
		 * InputNeuron ids and connections are important but our "OutputNeuron"
		 * is just a dummy neuron to capture output
		 */
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		nodes = new ArrayList<NodeGene>();

		
		//Gen nodes from output to input
		
		List<NodeGene> lastLayer = new ArrayList<NodeGene>();
		//Make the output layer
		//We reserve the lower numbers for the input and output nodes
		nodeNum += numInputs;
		for(int i = 0; i < numOutputs; i++) {
			NodeGene n =  new NodeGene(nodeNum, NodeGene.OUTPUT, new ArrayList<NodeGene>());
			nodes.add(n);
			lastLayer.add(n);
			nodeNum++;
		}
		
		//Make the hidden layers
		//For now we won't initialise layer skipping neurons
		int numHiddenLayers = Util.uniformInt(1, maxHiddenLayers);
		int numHiddenNodes = Util.uniformInt(minHiddenNodes, maxHiddenNodes);
		System.out.println("# Hidden Layers = " + numHiddenLayers);
		System.out.println("# Hidden Nodes = " + numHiddenNodes);
		int nodeCount = 0;
		List<NodeGene> hiddenNodes = new ArrayList<NodeGene>();
		for(int i = 0; i < numHiddenLayers; i++) {
			List<NodeGene> newLastLayer = new ArrayList<NodeGene>();
			int nodesThisLayer = 0;
			if(i == numHiddenLayers-1)
				nodesThisLayer = numHiddenNodes-nodeCount;
			else if(numHiddenNodes-nodeCount <= 0)
				nodesThisLayer = 0;
			else
				nodesThisLayer = Util.uniformInt(1, numHiddenNodes-nodeCount);
			for(int j = 0; j < nodesThisLayer; j++) {
				NodeGene n = new NodeGene(nodeNum, NodeGene.HIDDEN, lastLayer);
				nodes.add(n);
				newLastLayer.add(n);
				nodeNum++;
				hiddenNodes.add(n);
			}
			nodeCount += nodesThisLayer;
			lastLayer.addAll(newLastLayer);
		}
		
		//Make the input layer
		//We use the iteration number here as we have already reserved these ids
		for(int i = 1; i <= numInputs; i++) {
			NodeGene n = new NodeGene(i, NodeGene.INPUT, hiddenNodes);
			nodes.add(n);
		}
		
		//Make the bias neuron
		NodeGene bias = new NodeGene(0, NodeGene.BIAS, new ArrayList<NodeGene>(nodes));
		
		
		//Make the connections
		connections = new ArrayList<ConnectionGene>();
		//Every neuron needs at least one output, except the output layer
		for(NodeGene n : nodes) {
			//System.out.println("DOING CONNECTIONS FOR " + n.toString());
			//always connect the bias unless an input
			if(n.type != NodeGene.INPUT)
				connections.add(new ConnectionGene(bias.id,n.id,Util.uniformDouble(-1, 1),true,1));
			for(int i = 0; i < n.possibleOutputs.size(); i++) {
				if(Util.uniformDouble(0, 1) < connectionChance) {
					NodeGene in = n.possibleOutputs.get(i);
					addConnection(n.id,in.id,Util.uniformDouble(-1,1));
				}
			}
		}
		nodes.add(bias);
	}
	
	public Genome clone() {
		Genome g = new Genome(numInputs,numOutputs,nodes,connections,0);
		g.nodeNum = this.nodeNum;
		return g;
	}
	
	public void addConnection(int out, int in, double weight) {
		ConnectionGene g = new ConnectionGene(out,in,weight,true,globalInnov);
		//System.out.println(g);
		globalInnov++;
		connections.add(g);
	}
	
	public void addNode(int type, List<NodeGene> possibleOutputs) {
		nodes.add(new NodeGene(nodeNum+1, type, possibleOutputs));
		nodeNum++;
	}
	
	public void addNode(NodeGene n) {
		nodes.add(n);
		nodeNum = n.id+1;
	}
	
	public NodeGene getNode(int id) {
		for(NodeGene n : nodes) {
			if(id == n.id)
				return n;
		}
		return null;
	}
	
	public List<NodeGene> getInputNodes() {
		List<NodeGene> inputs = new ArrayList<NodeGene>();
		for(NodeGene n : nodes)
			if(n.type == NodeGene.INPUT)
				inputs.add(n);
		return inputs;
	}

	public List<NodeGene> getOutputNodes() {
		List<NodeGene> inputs = new ArrayList<NodeGene>();
		for(NodeGene n : nodes)
			if(n.type == NodeGene.OUTPUT)
				inputs.add(n);
		return inputs;
	}
	
	public NodeGene getBiasNode() {
		for(NodeGene n : nodes)
			if(n.type == NodeGene.BIAS)
				return n;
		return null;
	}
	
	public String toString() {
		String out = "";
		out += "Nodes:\n";
		for(NodeGene n : nodes)
			out += n.toString() + "\n";
		for(ConnectionGene g : connections)
			out += g.toString() + "\n";
		return out;
	}
	
}
