package com.fixwin.backend;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Game {

	private String gameType;// TODO: add game types
	private Season season;
	private Team home, away;
	private Bet associatedBet;
	private int homeScore, awayScore, result;
	private double drawOdds, homeOdds, awayOdds;
	private boolean matchPlayed; // if the match was played -> true.
	private Timestamp timestamp;

	public Game(Team home, Team away, Season season, double drawOdds,
			double homeOdds, double awayOdds, String gameType, Timestamp timestamp) {
		matchPlayed = false;
		this.home = home;
		this.away = away;
		this.season = season;
		this.drawOdds = drawOdds;
		this.homeOdds = homeOdds;
		this.awayOdds = awayOdds;
		this.gameType = gameType;
		this.timestamp = timestamp;
		calcResult();
	}
	public Game() { //default constructor
		this(new Team(),new Team(),new Season(),0,0,0,"DEF",new Timestamp(0));
	}

	// TODO matchType
	public void playMatch(int homeScore, int awayScore) {
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		matchPlayed = true;
		calcResult(); // update result
		if(result == 0) { // if draw
			home.playDraw();
			away.playDraw();
		}
		else if(result == 1) { //if home won
			home.playWin();
			away.playLose();
		}
		else { // if away won
			away.playWin();
			home.playLose();
		}
		season.rankTeams();
	}

	private void calcResult() { // sets -1 if game not played, 0 if draw, 1 if
								// home won, 2 if away won.
		if (matchPlayed == false)
			result = -1;
		int diff = homeScore - awayScore;
		if (diff == 0)
			result = 0;
		else if (diff > 0)
			result = 1;
		else
			result = 2;
	}

	// // getters setters
	public String getGameType() {
		return gameType;
	}
	public Team getOpponent(Team t) {
		if(t.equals(this.home)) return this.away;
		else if (t.equals(this.away)) return this.home;
		else return null;
	}
	public boolean isTeamPlaying(Team t) {
		if(this.home.equals(t) || this.away.equals(t)) return true;
		return false;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public Team getHome() {
		return home;
	}

	public void setHome(Team home) {
		this.home = home;
	}

	public Team getAway() {
		return away;
	}

	public void setAway(Team away) {
		this.away = away;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public double getDrawOdds() {
		return drawOdds;
	}

	public void setDrawOdds(double drawOdds) {
		this.drawOdds = drawOdds;
	}

	public double getHomeOdds() {
		return homeOdds;
	}

	public void setHomeOdds(double homeOdds) {
		this.homeOdds = homeOdds;
	}

	public double getAwayOdds() {
		return awayOdds;
	}

	public void setAwayOdds(double awayOdds) {
		this.awayOdds = awayOdds;
	}

	public boolean isMatchPlayed() {
		return matchPlayed;
	}

	public void setMatchPlayed(boolean matchPlayed) {
		this.matchPlayed = matchPlayed;
	}

	public Bet getAssociatedBet() {
		return associatedBet;
	}

	public void setAssociatedBet(Bet associatedBet) {
		this.associatedBet = associatedBet;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
