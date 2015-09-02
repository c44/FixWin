package com.fixwin.backend;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Team {
	
	private String name;;
	private int mp, w, l, d, pts, rank;
	//calculation categories
	private double drawPercentage;
	private int noDrawStreak, maxNoDrawStreak;
	private ArrayList<Game> games = new ArrayList<Game>();
	private double grade = 0;
	public Team(String name) {
		this.name = name;
	}
	public Team() {
		this("defaultTeam");
	}
	public void playDraw() {
		mp += 1;
		pts += Season.DRAW_PTS;
		d += 1;
		
		noDrawStreak = 0;
		
		calcCategories();
	}
	public void playWin() {
		mp += 1;
		pts += Season.WIN_PTS;
		w += 1;
		
		noDrawStreak += 1;
		
		calcCategories();
	}
	public void playLose() {
		mp += 1;
		l += 1;
		
		noDrawStreak += 1;
		
		calcCategories();
	}
	public void calcCategories() {
		
		maxNoDrawStreak = Math.max(maxNoDrawStreak, noDrawStreak);
		drawPercentage = (double) d/mp;
		
	}
	
	public ArrayList<Game> getNextGames(int numOfGames) {
		ArrayList<Game> returnArr = new ArrayList<Game>();
		for(Game g : games) {

			if(g.isMatchPlayed()) continue;
			if(numOfGames > 0) returnArr.add(g);
			else break; // break in order to not continue iterating over unnecessary games
			numOfGames--;
		}
		return returnArr;
	}
	public ArrayList<Game> getNextGames(int numOfGames, Timestamp largerThanTimestamp) {
		ArrayList<Game> returnArr = new ArrayList<Game>();
		for(Game g : games) {
			if(g.isMatchPlayed()) continue;
			//System.out.println(g.getTimestamp().getTime() + " " + largerThanTimestamp.getTime());
			if(numOfGames > 0 /*&& g.getTimestamp().getTime() > largerThanTimestamp.getTime()*/) {
				//System.out.println("gameadded");
				returnArr.add(g);
				numOfGames--;
			}
			else break; // break in order to not continue iterating over unnecessary games
			
		}
		return returnArr;
	}
	
	public void addGame(Game g) {
		games.add(g);
	}
	
	// getters setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public int getPts() {
		return pts;
	}
	public void setPts(int pts) {
		this.pts = pts;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public ArrayList<Game> getGames() {
		return games;
	}
	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	public double getDrawPercentage() {
		return drawPercentage;
	}
	public void setDrawPercentage(double drawPercentage) {
		this.drawPercentage = drawPercentage;
	}
	public int getNoDrawStreak() {
		return noDrawStreak;
	}
	public void setNoDrawStreak(int noDrawStreak) {
		this.noDrawStreak = noDrawStreak;
	}
	public int getMaxNoDrawStreak() {
		return maxNoDrawStreak;
	}
	public void setMaxNoDrawStreak(int maxNoDrawStreak) {
		this.maxNoDrawStreak = maxNoDrawStreak;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
}
