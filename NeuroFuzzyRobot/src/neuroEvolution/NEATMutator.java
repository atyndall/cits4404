package neuroEvolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import neuroFuzzy.Util;

public class NEATMutator {

	public static Population mutate(Vector<Genome> pop, double mutationRate) {
		Random rand = new Random();
		Vector<Genome> newGenes = new Vector<Genome>(pop.size());
		for(Genome g : pop) {
			if(rand.nextDouble() < mutationRate)
				if(rand.nextBoolean())
					newGenes.add(addNode(g));
				else
					newGenes.add(addConnection(g));
			else
				newGenes.add(g);
		}
		return new Population(newGenes);
	}
	
	public static Genome addNode(Genome g) {
		//split existing connection and insert a node between
		//disable old connection
		ConnectionGene c = select(g.connections);
		c.disable();
		NodeGene outNode = g.getNode(c.out);
		NodeGene inNode = g.getNode(c.in);
		Genome outGene = g.clone();
		//inherits the downstream nodes outputs
		NodeGene newNode = new NodeGene(outGene.nodeNum+1,NodeGene.HIDDEN,inNode.possibleOutputs);	
		//Anything that could output to the downstream node can now also output to new node
		for(NodeGene ng : g.nodes) {
			if(ng.possibleOutputs.contains(inNode))
				ng.addOutput(newNode);
		}
		//Add and connect node
		outGene.addNode(newNode);
		outGene.addConnection(outNode.id, newNode.id, neuroFuzzy.Util.uniformDouble(0, 1));
		outGene.addConnection(newNode.id, inNode.id, neuroFuzzy.Util.uniformDouble(0, 1));
		return outGene;
	}
	
	
	public static Genome addConnection(Genome g) {
		//find two unconnected nodes and connect them
		//select random node
		Genome outGene = g.clone();
		NodeGene n = select(g.nodes);
		List<Integer> outputs = getOutputIds(n,g.connections);
		List<NodeGene> possibleOutputs = scramble(n.possibleOutputs);
		for(NodeGene node : possibleOutputs) {
			if(!outputs.contains(node.id)) {
				outGene.addConnection(n.id, node.id, neuroFuzzy.Util.uniformDouble(0,1));
				return outGene;
			}	
		}
		return outGene;
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
