package ga;



import ga.decisions.DecisionNode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GATree implements Serializable {	
	private Node root;

	public static void PrintGATree(GATree t) {
		PrintNode(t.getRoot());
	}
	
	private static void PrintNode(Node n) {
		System.out.println(n);
		for (Node c : n.getChildren()) {
			if (c != null) PrintNode(c);
		}
	}
	
	public GATree(Node root) {
		this.root = root;
		//this.root.setParent(new RootNode(this.root));
	}
	
	public void setRoot(DecisionNode n) {
		this.root = n;
	}
	
	public void setRobot(GABot r) {
		this.root.setRobot(r);
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public void evaluate() {
		root.evaluate();
	}
	
	public List<Node> getNodeList() {
		LinkedList<Node> l = new LinkedList<Node>();		
		getNodeList(l, this.root);
		return l;
	}
	
	private void getNodeList(List<Node> l, Node n) {
		if (n != null) {
			for (Node c : n.getChildren()) {
				if (c != null) {
					l.add(c);
					getNodeList(l, c);
				}
			}
		}
	}

	
}
