package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import neuro.NeuroTargetingBot;
import robocode.BattleResults;
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

public class BattleListener implements IBattleListener {

	public BattleResults[] result;
	
	public int numTurnsAlive;
	public int totalTurns;
	
	public String accuracyFileName;
	public int numHits;
	public int numFired;
	public double accuracy;
	
	public BattleListener() {
		numTurnsAlive = 0;
	}
	
	@Override
	public void onBattleCompleted(BattleCompletedEvent arg0) {
		result = arg0.getIndexedResults();
		try {
			FileReader file = new FileReader(accuracyFileName);
			BufferedReader reader = new BufferedReader(file);
			String sbuf = reader.readLine();
			String[] tokens = sbuf.split(",");
			numHits = Integer.parseInt(tokens[0]);
			numFired = Integer.parseInt(tokens[1]);
			accuracy = numHits/(double)numFired;
			reader.close();
			//file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onBattleError(BattleErrorEvent arg0) {
		System.out.println(arg0.getError());

	}

	@Override
	public void onBattleFinished(BattleFinishedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBattleMessage(BattleMessageEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBattlePaused(BattlePausedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBattleResumed(BattleResumedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBattleStarted(BattleStartedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoundEnded(RoundEndedEvent arg0) {
		numTurnsAlive += arg0.getTurns();
		//System.out.println(numRoundsAlive);
		totalTurns = arg0.getTotalTurns();
	}

	@Override
	public void onRoundStarted(RoundStartedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTurnEnded(TurnEndedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTurnStarted(TurnStartedEvent arg0) {
		// TODO Auto-generated method stub

	}

}
