package ga.fitness;

import ga.Config;

import java.io.Serializable;
import java.util.List;

import robowiki.runner.RobotScore;

public abstract class FitnessMeasure implements Serializable {
	RobotScore yourRobot;
	RobotScore enemyRobot;
	String opponent;
	
	public FitnessMeasure(List<RobotScore> br) {
		opponent = Config.get().opponent;
		int theirs = -1;
		int ours = -1;

		if (br.get(0).botName.equals(opponent)) {
			theirs = 0;
			ours = 1;
		} else if (br.get(1).botName.equals(opponent)) {
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
