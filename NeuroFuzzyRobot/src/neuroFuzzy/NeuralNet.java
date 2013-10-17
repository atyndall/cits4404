package neuroFuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class NeuralNet {

	List<Neuron> inputNeurons;
	HashMap<Integer,Neuron> hiddenNeurons;
	List<OutputNeuron> outputNeurons;
	
	
	public NeuralNet(Genome genome) {
		outputNeurons = new ArrayList<OutputNeuron>();
		inputNeurons = new ArrayList<Neuron>();
		hiddenNeurons = new HashMap<Integer,Neuron>();
		//init output collectors
		for(int i = 0; i < genome.numOutputs; i++) {
			outputNeurons.add(new OutputNeuron(0));
		}
		int outCount = 0;
		for(Genome.NodeGene n : genome.nodes) {
			//link up the output collectors to the bottom most hidden nodes
			if(n.type == Genome.NodeGene.OUTPUT) {
				HiddenNeuron neu = new HiddenNeuron(n.id, new LogisticFunction());
				neu.addChild(outputNeurons.get(outCount),1.0);
				outCount++;
				hiddenNeurons.put(n.id, neu);
			//store away the input neurons
			} else if(n.type == Genome.NodeGene.INPUT) {
				InputNeuron neu = new InputNeuron(n.id);
				inputNeurons.add(neu);
			} else if(n.type == Genome.NodeGene.HIDDEN) {
				HiddenNeuron neu = new HiddenNeuron(n.id, new LogisticFunction());
				hiddenNeurons.put(n.id, neu);
			}
		}
		
		//Connect everything
		for(Genome.ConnectionGene cg : genome.connections) {
			if(cg.enabled) {
				Neuron out = null;
				if(hiddenNeurons.containsKey(cg.out))
					out = hiddenNeurons.get(cg.out);
				else
					out = getNeuron(cg.out,inputNeurons);
				if(out == null) {
					System.out.println("OUT: GOD DAMN IT");
					System.exit(0);
				}
				Neuron in = null;
				if(hiddenNeurons.containsKey(cg.in))
					in = hiddenNeurons.get(cg.in);
				else
					in = getOutputNeuron(cg.in,outputNeurons);
				if(in == null) {
					System.out.println("IN: GOD DAMN IT");
					System.exit(0);
				}
				
				out.addChild(in, cg.weight);
			}
		}
	}
	
	private Neuron getNeuron(int id, List<Neuron> neurons) {
		for(Neuron n : neurons)
			if(n.id == id)
				return n;
		return null;
	}
	
	private Neuron getOutputNeuron(int id, List<OutputNeuron> neurons) {
		for(Neuron n : neurons)
			if(n.id == id)
				return n;
		return null;
	}
	
	public double[] output(double input[]) {
		loadInput(input);
		PriorityQueue<Neuron> queue = new PriorityQueue<Neuron>(11, new ProcessOrderingComparator());
		queue.addAll(inputNeurons);
		while(!queue.isEmpty()) {
			Neuron n = queue.poll();
			n.output();
			queue.addAll(n.children);
		}
		double[] out = new double[outputNeurons.size()];
		for(int i = 0; i < out.length; i++) {
			//"output" neurons are set up to capture the results of the NN
			//so they just have one output
			out[i] = outputNeurons.get(i).output;
		}
		return out;
	}
	
	public void loadInput(double[] input) {
		for(int i = 0; i < inputNeurons.size(); i++) {
			inputNeurons.get(i).receiveInput(input[i]);
		}
	}
	
}
