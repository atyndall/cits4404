package ga;



import ga.decisions.DecisionNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	
	public static GATree CopyTree(GATree orig) {
		GATree obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = (GATree) in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
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
