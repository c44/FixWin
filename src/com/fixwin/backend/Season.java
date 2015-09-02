package com.fixwin.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Season {
	
	static int WIN_PTS = 3,
			DRAW_PTS = 1;
	
	
	
	private String name;
	private int Id, year;
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<Game> games = new ArrayList<Game>();
	private League league;

	public Season(String name, int year, League league) {
		this.name = name;
		this.year = year;
		this.league = league;
	}
	
	public Season() {
		this("defaultSeason",1970,new League());
	}

	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public void addGame(Game e) {
		games.add(e);
		e.getHome().addGame(e);
		e.getAway().addGame(e);
	}
	
	public void rankTeams() {
		Collections.sort(teams, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				return Integer.compare(o2.getPts(),o1.getPts());
			}
		});
		
		for (Team t : teams) {
			t.setRank(teams.indexOf(t) + 1);
		}
	}

	
	//getters setters
	public ArrayList<Team> getTeams() {
		return teams;
	}	
	
	public Team findTeambyName(String name) {
		for (Team team : teams) {
			if (team.getName().equals(name)){
				return team;
			}
		}
		return null;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	
	
}
