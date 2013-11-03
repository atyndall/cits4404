package neuroEvolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import neuroFuzzy.GaussianActivation;
import neuroFuzzy.Util;

public class NEATMutator {

	double mutationRate;
	double weightMutationChance;
	
	public NEATMutator(double mutationRate, double weightMutationChance) {
		super();
		this.mutationRate = mutationRate;
		this.weightMutationChance = weightMutationChance;
	}

	double nodeChance = 0.3;
	double linkChance = 0.5;
	
	public List<Genome> mutate(List<Genome> pop) {
		Random rand = new Random();
		ArrayList<Genome> newGenes = new ArrayList<Genome>(pop.size());
		for(Genome g : pop) {
			Genome toAdd = g;
			if(rand.nextDouble() < linkChance*mutationRate){
				toAdd = addConnection(toAdd);
			}
			if(rand.nextDouble() < nodeChance*mutationRate) {
				toAdd = addNode(toAdd);
			} 
			
			if(rand.nextDouble() < weightMutationChance) {
				toAdd = mutateWeights(toAdd);
			}
			
			
			newGenes.add(toAdd);
		}
		return newGenes;
	}
	
	public Genome mutateWeights(Genome g) {
		Random rand = new Random();
		for(int i = 0; i < g.connections.size(); i++) {
			ConnectionGene c = g.connections.get(i);
			if(rand.nextDouble() < 0.9) {
				//uniformly perturb weight
				c.weight = c.weight - (c.weight*Util.uniformDouble(-0.5, 0.5));
				if(c.weight < -1)
					c.weight = -1;
				if(c.weight > 1)
					c.weight = 1;
			} else {
				//assign new weight
				c.weight = Util.newWeight();
			}
		}
		return g;
	}
	
	public Genome addNode(Genome g) {
		//split existing connection and insert a node between
		//disable old connection
		ConnectionGene c = null;
		do{
			c = select(g.connections);
		} while(g.getBiasNode().id == c.out);
		//we don't want to make nodes inbetween bias nodes
		
		NodeGene outNode = g.getNode(c.out);
		NodeGene inNode = g.getNode(c.in);
		c.disable();
		Genome outGene = g.clone();
		
		//inherits the upstream node's outputs
		NodeGene newNode = outGene.addNode(NodeGene.HIDDEN,outNode.possibleOutputs);	
		
		//Anything that could output to the upstream node can now also output to new node
		for(NodeGene ng : outGene.nodes) {
			if(ng.possibleOutputs.contains(inNode.id))
				ng.addOutput(newNode.id);
		}
		//connect node
		//System.out.println("Adding node " + newNode.id + " between " + outNode.id + " and " + inNode.id);
		outGene.addConnection(outNode.id, newNode.id, Util.newWeight());
		outGene.addConnection(newNode.id, inNode.id, Util.newWeight());
		return outGene;
	}
	
	
	public Genome addConnection(Genome g) {
		//find two unconnected nodes and connect them
		//select random node
		Genome outGene = g.clone();
		NodeGene n = selectNonOutput(g.nodes);
		List<Integer> outputs = getOutputIds(n,g.connections);
		List<Integer> possibleOutputs = scramble(n.possibleOutputs);
		for(Integer out : possibleOutputs) {
			//TODO this second check is because sometimes some nodes (usually input nodes)
			//will have nodes they don't know about in their possible outputs
			if(!outputs.contains(out) && g.hasNode(out)) {
				//System.out.println("Adding connection from " + n.id + " to " + out.id);
				outGene.addConnection(n.id, out, neuroFuzzy.Util.newWeight());
				return outGene;
			}
		}
		return outGene;
	}
	
	public Genome disableConnection(Genome g) {
		Genome out = new Genome(g);
		NodeGene n = selectNonOutput(out.nodes);
		//just remove the first one or some random other one
		ConnectionGene cgToRemove = null;
		Random rand = new Random();
		for(ConnectionGene cg : out.connections) {
			if(cg.out == n.id) {
				if(cgToRemove == null)
					cgToRemove = cg;
				else if(rand.nextBoolean())
					cgToRemove = cg;
			}
		}
		if(cgToRemove != null)
			cgToRemove.disable();
		return out;
	}

	
	public NodeGene selectNonOutput(List<NodeGene> genes) {
		NodeGene n = null;
		do {
			n = select(genes);
		} while(n.type == NodeGene.OUTPUT);
		return n;
	}
	
	public static <E> List<E> scramble(List<E> list) {
		List<E> out = new ArrayList<E>();
		List<E> listClone = new ArrayList<E>(list);
		while(out.size() < list.size()) {
			E next = selectAndRemove(listClone);
			out.add(next);
		}
		return out;
	}
	
	public static <E> E selectAndRemove(List<E> list) {
		E next = select(list);
		list.remove(next);
		return next;
	}
	
	public static <E> E select(List<E> list) {
		return list.get(Util.uniformInt(0, list.size()-1));
	}
	
	public static List<ConnectionGene> getConnectionGenes(NodeGene g, List<ConnectionGene> conns) {
		List<ConnectionGene> genes = new ArrayList<ConnectionGene>();
		for(ConnectionGene c : conns) {
			if(g.id == c.out)
				genes.add(c);
		}
		return genes;
	}
	
	public static List<Integer> getOutputIds(NodeGene g, List<ConnectionGene> conns) {
		List<Integer> ids = new ArrayList<Integer>();
		for(ConnectionGene c : conns) {
			if(c.out == g.id)
				ids.add(c.in);
		}
		return ids;
	}
}
