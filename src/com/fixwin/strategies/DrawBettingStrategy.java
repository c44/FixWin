package com.fixwin.strategies;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.fixwin.backend.*;

public class DrawBettingStrategy extends BettingStrategy {

	protected ArrayList<Team> runningTeams;
	protected int streakLength = 6;
	protected double initialSum = 10;
	protected double minDrawPercentage = 0.25;
	private double drawPercentagePercent;
	private double noDrawStreakPercent;
	private double maxNoDrawStreakPercent;
	private double nextGamesGradePercent;
	private double gradeMinLimit;
	private int maxStreaks;


	public DrawBettingStrategy(Season s) {
		super(s);
		this.runningTeams = new ArrayList<Team>();
		// TODO Auto-generated constructor stub
	}

	public boolean ifToPlace(Game g) {
		if(runningTeams.contains(g.getHome()) || runningTeams.contains(g.getAway())) return true;
		return false;
	}
	
	public void betPlaced(Team t) {
		//betsPlayed += 1;
		//System.out.println();
		Streak lastStreak = getAssociatedStreak(t);
		if(!lastStreak.isDefault()) {
			lastStreak.placeDraw(lastStreak.getNextGame(), lastStreak.getCurrentSum());
			lastStreak.setCurrentSum(lastStreak.getCurrentSum()*2);
			//System.out.println("BETS PLAYED " + lastStreak.calcGamesBettedOn());
			/*if(lastStreak.calcGamesBettedOn() >= streakLength) { //
				disableStreak(lastStreak);
			}*/
		}
	}
	
	public void disableStreak(Streak st) {
		st.setActive(false);
		runningTeams.remove(st.getTeam());

	}
	
	public Streak getAssociatedStreak(Team t) {
		if(runningTeams.contains(t)) {
			for(Streak st : streaks) {
				if(st.getTeam().equals(t) && st.isActive()) return st;
			}
		}
		return new Streak();
	}

	public boolean activeStreakExists() {
		for(Streak st : streaks) {
			if(st.isActive()) return true;
		}
		return false;
	}
	
	public int calcActiveStreaks() {
		int i = 0;
		for(Streak st : streaks) {
			if(st.isActive()) i++;
		}
		return i;
	}
	
	public boolean canOpenStreak() {
		if(calcActiveStreaks() >= maxStreaks) return false;
		return true;
	}

	public void calcGrades() {
		
		for(Team t : season.getTeams()) {
			double grade;
//			if t.grt
//			grade = t.getDrawPercentage()*drawPercentagePercent + t.getNoDrawStreak()*noDrawStreakPercent + t.getMaxNoDrawStreak()*maxNoDrawStreakPercent;
			grade = t.getDrawPercentage()*drawPercentagePercent + ((t.getMaxNoDrawStreak() != 0) ? t.getNoDrawStreak()/t.getMaxNoDrawStreak() : 0) * noDrawStreakPercent;
			
			double nextGamesGrade = 0;
			//System.out.println("Next games: " + t.getNextGames(streakLength).size());
			for(Game g : t.getNextGames(streakLength)) {
				//nextGamesGrade += (double) (season.getTeams().size() - Math.abs(t.getRank() - g.getOpponent(t).getRank())) / season.getTeams().size();
				nextGamesGrade += (double) g.getOpponent(t).getDrawPercentage();
				//if(g.getOpponent(t).getDrawPercentage() >= 0.25) {
					//nextGamesGrade += 0.1;
				//}
			}
			
			grade += nextGamesGrade / streakLength * nextGamesGradePercent;
			t.setGrade(grade);
			/*if(t.getDrawPercentage() >= minDrawPercentage && t.getMaxNoDrawStreak()-t.getNoDrawStreak() <= streakLength) {
				ArrayList<Game> nextGames = t.getNextGames(streakLength);
				double sumDrawPercentage = 0; 
				for(Game g : nextGames) {
					if(g.getOpponent(t) != null) sumDrawPercentage+= g.getOpponent(t).getDrawPercentage();
				}
				if(sumDrawPercentage/streakLength >= 0.25) {
					runningTeam = t;
					//this.setRunning(true);
					break;
				}
			}*/
		}
	}
	
	public void pickTeam() {
		ArrayList<Team> teams = season.getTeams(); //new ArrayList<Team>(season.getTeams()); //copy teams into new arraylist
		Collections.sort(teams, new Comparator<Team>() {

			@Override // sort teams by draw percentage
			public int compare(Team o1, Team o2) {
				return Double.compare(o2.getGrade(),o1.getGrade());
			}
		});
		//System.out.println(teams.get(0).getName() + " " + teams.get(1).getName());
		for(Team t : teams) {
			//System.out.println();
			if(t.getGrade() >= gradeMinLimit && !runningTeams.contains(t) && calcActiveStreaks() < maxStreaks) {
				runningTeams.add(t);
				generateStreaks();
				
			}
				
		}
		if(runningTeams.size() > 0) this.setRunning(true);
	}
	public Game getLastGamePlayed() {
		Game returnGame = new Game();
		for (Game g : season.getGames()) {
			if(g.isMatchPlayed()) returnGame = g;
		}
		return returnGame;
	}
	public void generateStreaks() {
		for(Team t : runningTeams) {
			boolean toCreate = true;
			for(Streak st : streaks) {
				
				if(st.getTeam().equals(t) && st.isActive()) {
					toCreate = false;
				}
			}
			if(toCreate) {
				Timestamp time = getLastGamePlayed().getTimestamp();
				if(streaks.size() == 0) {// if there are no active streaks and season begins
					time = new Timestamp(0);
				}
				Streak s = new Streak(streakLength, initialSum, t, time);
				streaks.add(s);
				
			}
		}
		for(Streak st : streaks) {
			if(st.calcGamesBettedOn() >= streakLength && st.isActive()) {
				disableStreak(st);
			}
		}
	}

	public double getMinDrawPercentage() {
		return minDrawPercentage;
	}

	public void setMinDrawPercentage(double minDrawPercentage) {
		this.minDrawPercentage = minDrawPercentage;
	}

	public int getStreakLength() {
		return streakLength;
	}

	public void setStreakLength(int streakLength) {
		this.streakLength = streakLength;
	}

	public double getInitialSum() {
		return initialSum;
	}

	public void setInitialSum(int initialSum) {
		this.initialSum = initialSum;
	}

	public double getDrawPercentagePercent() {
		return drawPercentagePercent;
	}

	public void setDrawPercentagePercent(double drawPercentagePercent) {
		this.drawPercentagePercent = drawPercentagePercent;
	}

	public double getNoDrawStreakPercent() {
		return noDrawStreakPercent;
	}

	public void setNoDrawStreakPercent(double noDrawStreakPercent) {
		this.noDrawStreakPercent = noDrawStreakPercent;
	}

	public double getMaxNoDrawStreakPercent() {
		return maxNoDrawStreakPercent;
	}

	public void setMaxNoDrawStreakPercent(double d) {
		this.maxNoDrawStreakPercent = d;
	}

	public double getNextGamesGradePercent() {
		return nextGamesGradePercent;
	}

	public void setNextGamesGradePercent(double nextGamesGradePercent) {
		this.nextGamesGradePercent = nextGamesGradePercent;
	}

	public ArrayList<Team> getRunningTeams() {
		return runningTeams;
	}

	public void setRunningTeams(ArrayList<Team> runningTeams) {
		this.runningTeams = runningTeams;
	}

	public double getGradeMinLimit() {
		return gradeMinLimit;
	}

	public void setGradeMinLimit(double gradeMinLimit) {
		this.gradeMinLimit = gradeMinLimit;
	}

	public int getMaxStreaks() {
		return maxStreaks;
	}

	public void setMaxStreaks(int maxStreaks) {
		this.maxStreaks = maxStreaks;
	}

	public void setInitialSum(double initialSum) {
		this.initialSum = initialSum;
	}

}
