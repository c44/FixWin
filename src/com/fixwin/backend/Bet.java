package com.fixwin.backend;

public class Bet {
	private Game game;
	private double awayWin,drawWin,homeWin, winAmount, moneyPlaced;
	private boolean gamePlayed;
	public Bet(Game game) {
		this.game = game;
		gamePlayed = false;
		game.setAssociatedBet(this);
	}
	public void placeAway(double amount) {
		awayWin = amount*game.getAwayOdds();
		moneyPlaced += amount;
	}
	public void placeDraw(double amount) {
		drawWin = amount*game.getDrawOdds();	
		moneyPlaced += amount;
	}
	public void placeHome(double amount) {
		homeWin = amount*game.getHomeOdds();
		moneyPlaced += amount;
	}
	public void playBet() { 
		int result = game.getResult(); //0 if draw, 1 if home won, 2 if away won.
		switch(result) {
		case 0:
			winAmount = drawWin;
			break;
		case 1:
			winAmount = homeWin;
			break;
		case 2:
			winAmount = awayWin;
			break;
		}
		gamePlayed = true;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public double getAwayWin() {
		return awayWin;
	}
	public void setAwayWin(double awayWin) {
		this.awayWin = awayWin;
	}
	public double getDrawWin() {
		return drawWin;
	}
	public void setDrawWin(double drawWin) {
		this.drawWin = drawWin;
	}
	public double getHomeWin() {
		return homeWin;
	}
	public void setHomeWin(double homeWin) {
		this.homeWin = homeWin;
	}
	public double getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(double winAmount) {
		this.winAmount = winAmount;
	}
	public double getMoneyPlaced() {
		return moneyPlaced;
	}
	public void setMoneyPlaced(double moneyPlaced) {
		this.moneyPlaced = moneyPlaced;
	}
	public boolean isGamePlayed() {
		return gamePlayed;
	}
	public void setGamePlayed(boolean gamePlayed) {
		this.gamePlayed = gamePlayed;
	}
}
