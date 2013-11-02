package ga.fitness;

import java.util.List;

import robowiki.runner.RobotScore;

public abstract class FitnessMeasure {
	RobotScore yourRobot;
	RobotScore enemyRobot;
	
	public FitnessMeasure(List<RobotScore> br) {
		int theirs = -1;
		int ours = -1;

		if (br.get(0).botName.equals("sample.Walls")) {
			theirs = 0;
			ours = 1;
		} else if (br.get(1).botName.equals("sample.Walls")) {
			theirs = 1;
			ours = 0;
		} else {
			throw new RuntimeException("Don't know whats going on");
		}
		
		yourRobot = br.get(ours);
		enemyRobot = br.get(theirs);
	}
	
	public abstract int getFitness();
}
