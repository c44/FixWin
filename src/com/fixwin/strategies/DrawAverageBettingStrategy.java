package com.fixwin.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.fixwin.backend.*;

public class DrawAverageBettingStrategy extends BettingStrategy {

	protected Team runningTeam;
	protected int streakLength = 7;
	protected int initialSum = 10;

	public DrawAverageBettingStrategy(Season s) {
		super(s);
		this.runningTeam = null;
		// TODO Auto-generated constructor stub
	}

	public boolean ifToPlace(Game g) {
		if(g.getHome().equals(runningTeam) || g.getAway().equals(runningTeam)) return true;
		return false;
	}
	
	public void betPlaced() {
		betsPlayed += 1;
		Streak lastStreak = streaks.get(streaks.size() - 1);
		lastStreak.placeDraw(lastStreak.getNextGame(), lastStreak.getCurrentSum());
		lastStreak.setCurrentSum(lastStreak.getCurrentSum()*2);
		if(betsPlayed == streakLength) {
			disableStreak();
		}
	}
	
	public void disableStreak() {
		this.setRunning(false);
		betsPlayed = 0;
		streaks.get(streaks.size() - 1).setActive(false);
	}
	
	public boolean activeStreakExists() {
		return streaks.get(streaks.size() - 1).isActive();
	}

	public void pickTeam() {
		ArrayList<Team> teams = new ArrayList<Team>(season.getTeams()); //copy teams into new arraylist
		Collections.sort(teams, new Comparator<Team>() {

			@Override // sort teams by draw percentage
			public int compare(Team o1, Team o2) {
				return Double.compare(o2.getDrawPercentage(),o1.getDrawPercentage());
			}
		});
		for(Team t : teams) {
			if(t.getDrawPercentage() >=0.25) {
				ArrayList<Game> nextGames = t.getNextGames(streakLength);
				double sumDrawPercentage = 0; 
				for(Game g : nextGames) {
					if(g.getOpponent(t) != null) sumDrawPercentage+= g.getOpponent(t).getDrawPercentage();
				}
				if(sumDrawPercentage/streakLength >= 0.25) {
					runningTeam = t;
					this.setRunning(true);
					break;
				}
			}
		}
	}
	
	public Streak generateStreak() {
		Streak st = new Streak(streakLength, initialSum, runningTeam);
		streaks.add(st);
		return st;
	}

	public Team getRunningTeam() {
		return runningTeam;
	}

	public void setRunningTeam(Team runningTeam) {
		this.runningTeam = runningTeam;
	}

	@Override
	public void calcGrades() {
		// TODO Auto-generated method stub
		
	}

}
