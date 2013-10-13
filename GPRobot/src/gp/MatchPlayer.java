package gp;

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
	
	public MatchPlayer() {
		this.engine = new RobocodeEngine(this);
		this.battlefield = new BattlefieldSpecification();
		this.battle = new BattleSpecification(1, this.battlefield, this.engine.getLocalRepository("sample.SittingDuck"));

	}

	@Override
	public void onBattleCompleted(BattleCompletedEvent event) {
		// TODO Auto-generated method stub
		
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
