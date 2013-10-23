package neuroFuzzy;

public class BiasNeuron extends Neuron {

	public BiasNeuron() {
		super(0, null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void output() {
		for(Neuron n : children) {
			//We weight the outputs
			n.receiveInput(1.0*weights.get(n.id));
		}
	}

}
