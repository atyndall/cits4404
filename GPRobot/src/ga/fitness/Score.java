package ga.fitness;

import java.util.List;

import robowiki.runner.RobotScore;

public class Score extends FitnessMeasure {

	public Score(List<RobotScore> br) {
		super(br);
	}

	@Override
	public int getFitness() {
		long ourScore = Math.round(yourRobot.score);
		long theirScore = Math.round(enemyRobot.score);

		if (ourScore == 0 && theirScore == 0) {
			return 0;
		} else if (ourScore == 0 && theirScore > 0) {
			return (int) (-1 * theirScore);
		} else if (theirScore == 0 && ourScore > 0) {
			return (int) ourScore;
		} else {
			return (int) (ourScore - theirScore);
		}
	}

}
