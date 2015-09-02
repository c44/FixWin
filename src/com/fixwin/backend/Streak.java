package com.fixwin.backend;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Streak {
	private Team team;
	private int streakLength, gamesPlayed, currentBetIndex;
	private double initialSum, invested, won, currentSum;
	private boolean active;
	private ArrayList<Game> games;
	private ArrayList<Bet> bets;
	private boolean isDefault;

	public Streak(int streakLength, double initialSum, Team team, Timestamp fromTimestamp) {
		this.streakLength = streakLength;
		this.initialSum = initialSum;
		this.currentSum = initialSum;
		this.team = team;
		setActive(true);
		games = team.getNextGames(streakLength, fromTimestamp);
		bets = new ArrayList<Bet>();
		currentBetIndex = 0;
		//System.out.println(games.size());
	}
	public Streak() {
		this.streakLength = 1;
		this.initialSum = 0;
		this.currentSum = 0;
		this.team = new Team();
		setActive(false);
		games = team.getNextGames(streakLength);
		bets = new ArrayList<Bet>();
		currentBetIndex = 0;
		isDefault = true;
		gamesPlayed = 0;
	}
	public void placeDraw(Game g, double amount) {
		Bet b = new Bet(g);
		bets.add(b);///CONTINUE
		b.placeDraw(amount);
		invested += amount;
		//System.out.println("Invested: " + amount +"$");
		gamesPlayed++;
		
	}
	public void playStreak() { 
		for(int k = currentBetIndex; k<bets.size(); k++) { //current bet index is needed in order not to duplicate winning amounts
			Bet b = bets.get(k);
			//System.out.println(currentBetIndex);
			if(b.getGame().isMatchPlayed() == false) continue;
			b.playBet();
			won+=b.getWinAmount();
			//System.out.println(won);
			currentBetIndex = k;
		}
	}
	public Game getNextGame() {
		for(Game g : games) {
			if(g.isMatchPlayed() == false) return g;
		}
		return new Game();
	}
	public boolean isNoDraw() {
		if(calcGamesBettedOn() == games.size()) { //if streak ended, and no games resulted in draw
			if(games.size() != 0) {
				if(games.get(games.size() - 1).getResult() != 0) {
					return true;
				}
			}
			else return true;
			
		}
		return false;
	}
	/*public Bet getLastBet() {
		Bet returnBet = null;
		for (Bet b : bets) {
			if(b.getGame().isMatchPlayed() == false) return returnBet;
			return Bet = b;
		}
		return returnBet;
	}*/
	public int calcBetsPlayed() {
		int t = 0;
		for(Bet b : bets) {
			if(b.getGame().isMatchPlayed()) t++;
		}
		return t;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getStreakLength() {
		return streakLength;
	}
	public void setStreakLength(int streakLength) {
		this.streakLength = streakLength;
	}
	public int calcGamesPlayed() {
		int t = 0;
		for(Game g : games) {
			if(g.isMatchPlayed()) t++;
		}
		return t;
	}
	public int calcGamesBettedOn() { // calc how many games have bets set on them TODO What if we want to have two different bets for a game?
		return bets.size();
	}
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	public double getInitialSum() {
		return initialSum;
	}
	public void setInitialSum(double initialSum) {
		this.initialSum = initialSum;
	}
	public double getInvested() {
		return invested;
	}
	public void setInvested(double invested) {
		this.invested = invested;
	}
	public double getWon() {
		return won;
	}
	public void setWon(double won) {
		this.won = won;
	}
	public ArrayList<Game> getGames() {
		return games;
	}
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	public ArrayList<Bet> getBets() {
		return bets;
	}
	public void setBets(ArrayList<Bet> bets) {
		this.bets = bets;
	}
	public double getCurrentSum() {
		return currentSum;
	}
	public void setCurrentSum(double currentSum) {
		this.currentSum = currentSum;
	}
	public int getCurrentBetIndex() {
		return currentBetIndex;
	}
	public void setCurrentBetIndex(int currentBetIndex) {
		this.currentBetIndex = currentBetIndex;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
}
