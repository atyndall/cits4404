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
	public int nodeNum;
	public static int globalInnov = 1;
	private static final List<ConnectionGene> structuralMutations = new ArrayList<ConnectionGene>();
	
	public double getFit() {
		return fit;
	}

	public void setFit(double fit) {
		this.fit = fit;
	}

	
	public Genome(Genome g) {
		this(g.numInputs,g.numOutputs,g.nodes,g.connections,g.fit);
	}
	
	public Genome(int numInputs, int numOutputs, List<NodeGene> nodes,
			List<ConnectionGene> connections, double fit) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.nodes = new ArrayList<NodeGene>();
		for(NodeGene n : nodes) {
			this.nodes.add(n.clone());
		}
		this.connections = new ArrayList<ConnectionGene>();
		for(ConnectionGene c : connections) {
			this.connections.add(c.clone());
		}
		this.fit = fit;
	}
	
	public Genome(int numInputs, int numOutputs) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		nodes = new ArrayList<NodeGene>();
		connections = new ArrayList<ConnectionGene>();
		
		//Gen nodes from output to input
		
		List<Integer> outputLayer = new ArrayList<Integer>();
		//Make the output layer
		//We reserve the lower numbers for the input and output nodes
		nodeNum = 1;
		nodeNum += numInputs;
		for(int i = 0; i < numOutputs; i++) {
			NodeGene n =  new NodeGene(nodeNum, NodeGene.OUTPUT, new ArrayList<Integer>());
			nodes.add(n);
			outputLayer.add(n.id);
			nodeNum++;
		}
		
		//Make the input layer
		//We use the iteration number here as we have already reserved these ids
		for(int i = 1; i <= numInputs; i++) {
			NodeGene n = new NodeGene(i, NodeGene.INPUT, outputLayer);
			nodes.add(n);
		}
		
		//Make the bias neuron
		NodeGene bias = new NodeGene(0, NodeGene.BIAS, new ArrayList<Integer>(outputLayer));
		nodes.add(bias);
		
		//connect up the layers
		//just completely connect everything
		for(NodeGene out : nodes) {
			for(Integer in : out.possibleOutputs) {
				addConnection(out.id,in,Util.newWeight());
			}
		}
	}
	
	public Genome(int numInputs, int numOutputs, int[] hiddenLayers) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		
		nodes = new ArrayList<NodeGene>();
		connections = new ArrayList<ConnectionGene>();
		
		//Gen nodes from output to input
		
		List<Integer> lastLayer = new ArrayList<Integer>();
		List<Integer> nonInputs = new ArrayList<Integer>();
		//Make the output layer
		//We reserve the lower numbers for the input and output nodes
		nodeNum = 1;
		nodeNum += numInputs;
		for(int i = 0; i < numOutputs; i++) {
			NodeGene n =  new NodeGene(nodeNum, NodeGene.OUTPUT, new ArrayList<Integer>());
			nodes.add(n);
			lastLayer.add(n.id);
			nodeNum++;
			nonInputs.add(n.id);
		}
		
		List<Integer> nextLayer;
		for(int i = 0; i < hiddenLayers.length; i++) {
			nextLayer = new ArrayList<Integer>();
			for(int j = 0; j < hiddenLayers[i]; j++) {
				NodeGene n = new NodeGene(nodeNum, NodeGene.HIDDEN, lastLayer);
				nodes.add(n);
				nextLayer.add(n.id);
				nodeNum++;
				nonInputs.add(n.id);
			}
			lastLayer = nextLayer;
		}
		
		//Make the input layer
		//We use the iteration number here as we have already reserved these ids
		for(int i = 1; i <= numInputs; i++) {
			NodeGene n = new NodeGene(i, NodeGene.INPUT, lastLayer);
			nodes.add(n);
		}
		
		//Make the bias neuron
		
		NodeGene bias = new NodeGene(0, NodeGene.BIAS, new ArrayList<Integer>(nonInputs));
		nodes.add(bias);
		
		//connect up the layers
		//just completely connect everything
		for(NodeGene out : nodes) {
			for(Integer in : out.possibleOutputs) {
				addConnection(out.id,in,Util.newWeight());
			}
		}
	}
	
	
	@Override
	public Genome clone() {
		Genome g = new Genome(numInputs,numOutputs,nodes,connections,fit);
		g.nodeNum = nodeNum;
		return g;
	}
	
	public void addConnection(int out, int in, double weight) {
		ConnectionGene g = new ConnectionGene(out,in,weight,true,0);
		for(int i = 0; i < structuralMutations.size(); i++) {
			if(structuralMutations.get(i).equals(g))
				g.innov = structuralMutations.get(i).innov;
		}
		if(g.innov == 0) {
			g.innov = globalInnov;
			structuralMutations.add(g);
			globalInnov++;
		}
		//System.out.println(g);
		connections.add(g);
	}
	
	public NodeGene addNode(int type, List<Integer> possibleOutputs) {
		NodeGene n = new NodeGene(nodeNum, type, possibleOutputs);
		nodes.add(n);
		nodeNum++;
		return n;
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
		System.out.println("Could not find node " + id);
		System.out.print(this.toString());
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
		out += "Fitness: " + fit + "\n";
		return out;
	}
	
	public ConnectionGene[] getSortedGeneList() {
		ConnectionGene[] c1 = new ConnectionGene[connections.size()];
		c1 = connections.toArray(c1);
		return c1;
	}
	
	public boolean hasNode(int id) {
		for(NodeGene n : nodes) {
			if(n.id == id) {
				return true;
			}
		}
		return false;
	}
	
}
