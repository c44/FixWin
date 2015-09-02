package com.fixwin.strategies;

import java.util.ArrayList;

import com.fixwin.*;
import com.fixwin.backend.*;
/// every betting strategy extends this abstract class
public abstract class BettingStrategy {
	protected Season season;
	protected boolean running;
	protected int betsPlayed;
	protected ArrayList<Streak> streaks;
	public BettingStrategy(Season s) {
		this.season = s;
		this.running = false;
		streaks = new ArrayList<Streak>();
	}
	
	//public abstract Streak generateStreak();
	
	public abstract boolean activeStreakExists();

	public abstract boolean ifToPlace(Game g);
	
	public abstract void pickTeam();
	
	public abstract void calcGrades();
	
	//public abstract void betPlaced();

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public int getBetsPlayed() {
		return betsPlayed;
	}

	public void setBetsPlayed(int betsPlayed) {
		this.betsPlayed = betsPlayed;
	}

	public ArrayList<Streak> getStreaks() {
		return streaks;
	}

	public void setStreaks(ArrayList<Streak> streaks) {
		this.streaks = streaks;
	}
	
}
