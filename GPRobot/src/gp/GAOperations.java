package gp;

import gp.nodes.absrct.ActionNode;
import gp.nodes.absrct.Node;

import java.util.Random;

public class GAOperations {

	public static void Mutate(GATree t) {
		Random rnd = new Random();
		Node[] nlist = t.getNodeList();
		int index = rnd.nextInt(nlist.length);
		
		Node n = nlist[index];
		// replace with random
		Node newn = n.getClass() == ActionNode.class ? t.getRandomAction() : t.getRandomDecision();
		for (int i = 0; i < n.getParent().numChildren(); i++) {
			if (n.getParent().getChildren()[i] == n) {
				n.getParent().setChild(i, newn);
				for (int j = 0; i < n.numChildren(); i++) {
					newn.setChild(j, n.getChildren()[j]);
				}
			}
		}
	}

	public static void Crossover(GATree t1, GATree t2) {
		Random rnd = new Random();
		Node[] nlist1 = t1.getNodeList();
		Node[] nlist2 = t2.getNodeList();
		int i1 = rnd.nextInt(nlist1.length);
		int i2 = rnd.nextInt(nlist2.length);
		
		Node n1 = nlist1[i1];
		Node n2 = nlist2[i2];
		
		int n1swap = -1;
		int n2swap = -1;
		
		for (int i = 0; i < n1.getParent().numChildren(); i++) {
			if (n1.getParent().getChildren()[i] == n1) {
				n1swap = i;
			}
		}
		
		for (int i = 0; i < n2.getParent().numChildren(); i++) {
			if (n2.getParent().getChildren()[i] == n2) {
				n2swap = i;
			}
		}
		
		assert (n1swap != -1 && n2swap != -1);
		
		n1.getParent().setChild(n1swap, n2);
		n2.getParent().setChild(n2swap, n1);
	
		
	}
	
}
