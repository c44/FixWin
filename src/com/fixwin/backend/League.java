package com.fixwin.backend;

import java.util.ArrayList;

public class League {
	
	private String name;
	private String gameType; 
	private ArrayList<Season> seasons = new ArrayList<>();

	public League(String name, String gameType) {
		this.name = name;
		this.gameType = gameType;
	}

	
	public League() {
		this("defaultLeague","DEF");
	}


	// getters & setters
	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public ArrayList<Season> getSeasons() {
		return seasons;
	}

	public void addSeason(Season season) {
		seasons.add(season);
	}

}
