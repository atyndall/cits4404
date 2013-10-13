import gp.*;


public class GenerateGA {

	public static void main(String[] args) {
		GABot bot = new GABot();
		GATree g = new GATree(bot);
		bot.setActionTree(g);
		g.makeRandomTree();
		GATree.PrintGATree(g);
	}

}
