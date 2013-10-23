package neuroEvolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NEATRecombiner implements Recombination {

	static double enableChance = 0.1;

	@Override
	public Genome recombine(Genome p1, Genome p2) {
		ConnectionGene[] c1 = new ConnectionGene[p1.connections.size()];
		ConnectionGene[] c2 = new ConnectionGene[p2.connections.size()];
		c1 = p1.connections.toArray(c1);
		c2 = p2.connections.toArray(c2);
		Arrays.sort(c1);
		Arrays.sort(c2);
		int i = 0;
		int j = 0;
		Random rand = new Random();
		List<ConnectionGene> newConns = new ArrayList<ConnectionGene>();
		//TODO figure out some way of bringing the nodes across
		//Might be better to recreate all the nodes out of the connections
		List<NodeGene> nodes = new ArrayList<NodeGene>();
		while(i < c1.length && j < c2.length) {
			if(c1[i].innov == c2[j].innov) {
				if(rand.nextBoolean()) {
					addConnection(newConns,c1[i]);
					//We get the nodes from that parent too
					addNode(nodes,p1.getNode(c1[i].in));
					addNode(nodes,p1.getNode(c1[i].out));
				}else {
					addConnection(newConns,c2[j]);
					addNode(nodes,p2.getNode(c2[j].in));
					addNode(nodes,p2.getNode(c2[j].out));
				}
				i++;
				j++;
			//if c2 is missing some genes
			} else if(c1[i].innov < c2[j].innov) {
				if(p1.fit >= p2.fit) {
					addConnection(newConns,c1[i]);
					addNode(nodes,p1.getNode(c1[i].in));
					addNode(nodes,p1.getNode(c1[i].out));
				}
				i++;
			//if c1 is missing some genes
			} else {
				if(p2.fit >= p1.fit) {
					addConnection(newConns,c2[j]);
					addNode(nodes,p2.getNode(c2[j].in));
					addNode(nodes,p2.getNode(c2[j].out));
				}
				j++;
			}
		}
		//Do any excess genes
		while(i < c1.length && p1.fit >= p2.fit) {
			addConnection(newConns,c1[i]);
			addNode(nodes,p1.getNode(c1[i].in));
			addNode(nodes,p1.getNode(c1[i].out));
			i++;
		}
		while(j < c2.length && p2.fit >= p1.fit) {
			addConnection(newConns,c2[j]);
			addNode(nodes,p2.getNode(c2[j].in));
			addNode(nodes,p2.getNode(c2[j].out));
			j++;
		}
		//Make sure we add all the input and output nodes
		//Either genome should have all the correct nodes
		List<NodeGene> inputs = p1.getInputNodes();
		List<NodeGene> outputs = p1.getOutputNodes();
		for(NodeGene n : inputs)
			addNode(nodes,n);
		for(NodeGene n : outputs)
			addNode(nodes,n);
		
		//Don't forget to add in the bias neuron again!
		//Just to make sure although they should be
		//picked up by the previous checks
		if(p1.fit > p2.fit) {
			addNode(nodes,p1.getBiasNode());
		} else {
			addNode(nodes,p2.getBiasNode());
		}
		
		return new Genome(p1.numInputs,p2.numOutputs,nodes,newConns,0);
	}
	
	private void addNode(List<NodeGene> nodes, NodeGene g) {
		if(!nodes.contains(g))
			nodes.add(g);
	}
	
	private void addConnection(List<ConnectionGene> conns, ConnectionGene g) {
		if(!conns.contains(g))
			conns.add(g);
	}

}
