package neuroEvolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NEATRecombiner {

	static double enableChance = 0.75;
	List<ConnectionGene> newConns;
	List<NodeGene> nodes;
	NodeGene biasNode;
	
	
	public Genome recombine(Genome g1, Genome g2) {
		newConns = new ArrayList<ConnectionGene>();
		nodes = new ArrayList<NodeGene>();
		biasNode = NodeGene.newBiasNode();
		
		ConnectionGene[] c1 = g1.getSortedGeneList();
		ConnectionGene[] c2 = g2.getSortedGeneList();
		Arrays.sort(c1);
		Arrays.sort(c2);
	//	printConnectionArray(c1);
	//	printConnectionArray(c2);
		int i = 0;
		int j = 0;
		//TODO figure out some way of bringing the nodes across
		//Might be better to recreate all the nodes out of the connections
		
		while(i < c1.length && j < c2.length) {
			if(c1[i].innov == c2[j].innov) {
				selectRandom(c1[i],c2[j],g1,g2);
				i++;
				j++;
			//if c2 is missing some genes
			} else if(c1[i].innov < c2[j].innov) {
				if(g1.fit >= g2.fit) {
					addConnection(c1[i],g1);
				}
				i++;
			//if c1 is missing some genes
			} else {
				if(g2.fit >= g1.fit) {
					addConnection(c2[j],g2);
				}
				j++;
			}
		}
		//Do any excess genes
		while(i < c1.length && g1.fit >= g2.fit) {
			addConnection(c1[i],g1);
			i++;
		}
		while(j < c2.length && g2.fit >= g1.fit) {
			addConnection(c2[j],g2);
			j++;
		}
		//Make sure we add all the input and output nodes
		//Either genome should have all the correct nodes
		//TODO need to generate new inputs that know about ALL the hidden nodes
		List<NodeGene> inputs1 = g1.getInputNodes();
		List<NodeGene> outputs1 = g1.getOutputNodes();
		List<NodeGene> inputs2 = g2.getInputNodes();
		List<NodeGene> outputs2 = g2.getOutputNodes();
		for(NodeGene n : inputs1)
			addNode(n);
		for(NodeGene n : outputs1)
			addNode(n);
		for(NodeGene n : inputs2)
			addNode(n);
		for(NodeGene n : outputs2)
			addNode(n);
		
		addNode(biasNode);
		
		Genome newGene = new Genome(g1.numInputs,g2.numOutputs,nodes,newConns,0);
		//Determine the maximum node number
		int max = Integer.MIN_VALUE;
		for(NodeGene g : newGene.nodes) {
			if(g.id > max)
				max = g.id;
		}
		newGene.nodeNum = max+1;
		return newGene;
	}
	
	private void selectRandom(ConnectionGene c1, ConnectionGene c2, Genome g1, Genome g2) {
		Random rand = new Random();
		if(rand.nextBoolean()) {
			addConnection(c1,g1);
		}else {
			addConnection(c2,g2);
		}
	}
	
	//TODO Can attempt to add a a node that we don't have in the genome right now
	private void addNode(NodeGene g) {
		//if(g != null) 	//TODO worrying that I have to check this
						//shouldn't be passed null genes
			if(!nodes.contains(g)) {
				nodes.add(g);
				if(g.type != NodeGene.INPUT && g.type != NodeGene.BIAS)
					biasNode.addOutput(g.id);
			} else {
				//inherit every possible output
				NodeGene nodeInList = null;
				for(NodeGene n : nodes) {
					if(n.id == g.id)
						nodeInList = n;
				}
				for(Integer out : g.possibleOutputs)
					nodeInList.addOutput(out);
			}
	}
	
	/**
	 * 
	 * @param conns
	 * @param g
	 * @param gene the genome that this connection came from
	 */
	private void addConnection(ConnectionGene g, Genome gene) {
		if(!newConns.contains(g)) {
			Random rand = new Random();
			if(g.enabled == false && rand.nextDouble() < enableChance)
				g.enabled = true;
			newConns.add(g);
			addNode(gene.getNode(g.in)); //TODO These getNodes are sometimes returning null
			addNode(gene.getNode(g.out));
		}
	}
	
	private void printConnectionArray(ConnectionGene[] cgs) {
		for(ConnectionGene cg : cgs) {
			System.out.print(cg.toString() + " ");
		}
		System.out.println();
	}

}
