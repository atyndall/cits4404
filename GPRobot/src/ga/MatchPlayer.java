package ga;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import robocode.BattleResults;
import robocode.control.*;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleFinishedEvent;
import robocode.control.events.BattleMessageEvent;
import robocode.control.events.BattlePausedEvent;
import robocode.control.events.BattleResumedEvent;
import robocode.control.events.BattleStartedEvent;
import robocode.control.events.IBattleListener;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.events.TurnStartedEvent;

public class MatchPlayer implements IBattleListener {
	BattlefieldSpecification battlefield;
	BattleSpecification battle;
	RobocodeEngine engine;
	BattleResults curResults;
	
	
	public static void toFile(String path, Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MatchPlayer() {
		this.engine = new RobocodeEngine (new File(Config.robocodeLoc));
		engine.addBattleListener(this);
		this.battlefield = new BattlefieldSpecification();
		this.battle = new BattleSpecification(1, 10, 10, this.battlefield, this.engine.getLocalRepository("ga.GABot*,sample.SittingDuck"));
	}
	
	private void play() {
		engine.setVisible(false);
		engine.runBattle(battle, true);
	}
	
	private void playWith(GATree tree) {
		toFile(Config.serializedLoc, tree);
		play();
	}
	
	public int fitness(GATree tree) {
		playWith(tree);
		assert(curResults != null);
		return curResults.getScore();
	}

	@Override
	public void onBattleCompleted(BattleCompletedEvent event) {
		BattleResults[] br = event.getIndexedResults();
		assert(br.length == 2);
		curResults = br[0];
	}

	@Override
	public void onBattleError(BattleErrorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBattleFinished(BattleFinishedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBattleMessage(BattleMessageEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBattlePaused(BattlePausedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBattleResumed(BattleResumedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBattleStarted(BattleStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoundEnded(RoundEndedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoundStarted(RoundStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnStarted(TurnStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}
