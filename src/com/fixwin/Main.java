package com.fixwin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.fixwin.backend.Game;
import com.fixwin.backend.League;
import com.fixwin.backend.Season;
import com.fixwin.backend.Streak;
import com.fixwin.backend.Team;
import com.fixwin.strategies.BettingStrategy;
import com.fixwin.strategies.DrawAverageBettingStrategy;
import com.fixwin.strategies.DrawBettingStrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;

public class Main {
	static League lg = new League("Israel Bundsliga", "LEG");
	static Season s = new Season("2011/2012", 2011, lg);
	static DrawBettingStrategy bs;
	static ArrayList<String> csvs;
	static int currentFileIndex = 0;
	static double totalInvested;
	static double totalWon;
	static Timestamp currentTimestamp = new Timestamp(0); //current game date, in order not to invest in games during progress.
	static double maxProfit, maxInvested, maxWon, maxMultiplyStreaks, minNoDrawRatio = 1, maxWinningRatio = 0;
	static int maxDrawPercentage, maxNoDrawStreak, maxNextGamesGrade, maxStreakLength, maxMaxStreaks, maxBetIndexFrom, minNoDraws = 0, totalNoDraw, totalStreaks;
	static ArrayList<Double> profits;
	static ArrayList<Integer> noDraws;
	static ArrayList<ArrayList<Streak>> winningStreaks;
	
	
	//String fileUrl = "C:/Users/Ron/workspace/fixwin/src/com/fixwin/csvs/2014eng.csv";
	static String fileUrl = "csvs/2014eng.csv";
	private static int noDraw;
	private static int minMinGradeLimit;
	private static int totalWinStreaks;


	public static void main(String[] args) {
		profits = new ArrayList<Double>();
		noDraws = new ArrayList<>();
		winningStreaks = new ArrayList<>();
		
		
		csvs = new ArrayList<String>();
		
		/// load all CSV's
		String dir = System.getProperty("user.dir");
		fileUrl = dir + "/src/com/fixwin/csvs/fr";
        File folder = new File(fileUrl);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                csvs.add(file.getPath());
            }
        }
        ///
		
        
        	for(int drawPercentage = 50/*0*/; drawPercentage <= /*100*/50; drawPercentage = drawPercentage + 10) {
        		for(int noDrawStreak = 0; noDrawStreak <= /*100-drawPercentage*/0; noDrawStreak = noDrawStreak + 10) {
        			for(int nextGamesGrade = /*100-drawPercentage-noDrawStreak*/50; nextGamesGrade >= /*100-drawPercentage-noDrawStreak*/50; nextGamesGrade = nextGamesGrade - 10) {
        				for(int streakLength = 6; streakLength <= 6; streakLength ++) {
        					for(int maxStreaks = 1; maxStreaks <= 3; maxStreaks++) {
        						for(int betFromIndex = 100; betFromIndex <= 100; betFromIndex = betFromIndex + 10) {
	        							for(int minGradeLimit = /*0*/34; minGradeLimit<=/*34*/34; minGradeLimit = minGradeLimit + 1) {
	        								for(double multipleStreaks = 2; multipleStreaks <= 2; multipleStreaks += 1) {
			        							boolean skip = false;
							        			for(String csv : csvs) {	
									        	
											        	fileUrl = csv; // set Current file url;
											        	//System.out.println(csv);
											        	lg = new League(csv, "LEG");
											        	s = new Season("2011/2012", 2011, lg); // currently season doesn't matter
											        	lg.addSeason(s);
											    		bs = new DrawBettingStrategy(s);
											    		
											    		bs.setDrawPercentagePercent(drawPercentage);
											//    		bs.setMaxNoDrawStreakPercent(30);
											    		bs.setInitialSum(10);
											    		bs.setNoDrawStreakPercent(noDrawStreak);
											    		bs.setNextGamesGradePercent(nextGamesGrade);
											    		bs.setStreakLength(streakLength);
											    		bs.setGradeMinLimit(minGradeLimit);
											    		bs.setMaxStreaks(maxStreaks);
											    		
											    		Main obj = new Main();
											    		obj.addGames();
											    		obj.run(/*381*/381,betFromIndex);
											    		
											    		ArrayList<Team> teams = s.getTeams();
											    		
											    		int invested=0,won=0,noDraw=0;
											    		for(Streak st : bs.getStreaks()) {
											    			st.playStreak();
											    			invested += st.getInvested();
											    			won += st.getWon();
											    			totalStreaks++;
											    			if(st.isNoDraw()) {
											    				noDraw++;
											    			}
											    		}
											    		profits.add(new Double(won - invested));
											    		noDraws.add(new Integer(noDraw));
											    		winningStreaks.add(bs.getStreaks());
											    		/// add invested and won to sum
											    		totalInvested += invested;
											    		totalWon += won;
											    		totalNoDraw += noDraw;
											    		totalWinStreaks += bs.getStreaks().size() - noDraw;
											    		
											    		if(((double) totalWinStreaks/totalStreaks ) < maxWinningRatio && csvs.indexOf(csv) == csvs.size()-1 && totalWon-totalInvested < maxProfit) {
											    			profits.clear();
											    			winningStreaks.clear();
											    		}
											    		//if(/*noDraw > multipleStreaks*maxStreaks  || */won - invested < 0) {
											    			//skip = true;
											    			//profits.clear();
											    			//winningStreaks.clear();
											    			//break;
											    		//}
											    		//System.out.println(csv);
											    		//System.out.println("Invested : " + invested + "$. Won " + won + "$");	
											    		//System.out.println();
											    		
											    		/*if(noDraw > minNoDraws && csvs.indexOf(csv) == csvs.size()-1) {
											    			noDraws.clear();
											    		}*/
								        			} // end of csvs
								        			if(/*totalWon - totalInvested > maxProfit*/ ((double) totalWinStreaks/totalStreaks ) >= maxWinningRatio && totalWon-totalInvested >= maxProfit && !skip /*&& totalNoDraw <= minNoDraws*/) {
								        				maxProfit = totalWon - totalInvested;
								        				maxInvested = totalInvested;
								        				maxWon = totalWon;
								        				minNoDraws = noDraw;
								        				minNoDrawRatio = (double) totalNoDraw/totalStreaks;
								        				maxWinningRatio = (double) totalWinStreaks/totalStreaks;
								        				
								        				maxDrawPercentage = drawPercentage;
								        				maxNoDrawStreak = noDrawStreak;
								        				maxNextGamesGrade = nextGamesGrade;
								        				maxStreakLength = streakLength;
								        				maxMaxStreaks = maxStreaks;
								        				maxBetIndexFrom = betFromIndex;
								        				minMinGradeLimit = minGradeLimit;
								        				maxMultiplyStreaks = multipleStreaks;
								        				//System.out.println("max streak found");
								        				for(Double p : profits) {
								        					System.out.println(p.toString());
								        					for(Streak st : winningStreaks.get(profits.indexOf(p))) {
								        						System.out.println("Bets Played: " + st.calcGamesBettedOn() +". Invested: " + st.getInvested() + ". Won: " + st.getWon() + ". DidntWin?: " + st.isNoDraw() +". Team: " + st.getTeam().getName());
								        					}
								        				}
								        				System.out.println();
								        			}
								        			
								        			totalWon = 0; totalInvested = 0; totalNoDraw = 0; totalStreaks = 0; totalWinStreaks = 0;
								        			//System.out.println("checking finished");
								        			skip = false;
						        				}
	        							}
        							}
        						}
        					}	
        			}
        		}
        	}
        	
        	System.out.println(/*"Max Profit: " + maxProfit + */". Invested: "+maxInvested+". Won: " + maxWon +". DrawPercentage: "+ maxDrawPercentage + ". NoDrawStreak: " + maxNoDrawStreak +"." +
        			" NextGamesGrade: " + maxNextGamesGrade +". StreakLength: " + maxStreakLength +". MaxStreaks" + maxMaxStreaks +". BettedFrom game no: " + maxBetIndexFrom + ". minNoDraw: " + minNoDraws
        			+ ". MinGradeLimit: " + minMinGradeLimit + ". MultiplyStreaks: " + maxMultiplyStreaks + ". MinNoDrawRatio: " + minNoDrawRatio + ". maxWinRation: " + maxWinningRatio);
        
        //System.out.println("TOTAL : INVESTED "+totalInvested+"$. WON "+totalWon+"$.");
        //System.out.println("PROFIT : " + (totalWon - totalInvested));
		
		
	}
	public void run(int playAmount, int betFromIndex) {
		Calendar c = Calendar.getInstance();
		ArrayList<Game> games = s.getGames();
		
		String csvFile = fileUrl;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine(); //read first line
			int i=0;
			Game currentGame = new Game();
			while ((line = br.readLine()) != null) {
				String[] row = line.split(cvsSplitBy);

				
				
				if(i < playAmount) { // subtract is needed in order not to open a streak in last week of games
					if(i>=betFromIndex && i<=playAmount - 60) {
						
						if(bs.canOpenStreak() && !currentGame.getTimestamp().equals(currentTimestamp)) {// if to pick a new team
							//System.out.println("open streak");
							bs.calcGrades();
							bs.pickTeam();
							bs.generateStreaks();
							bs.setRunning(true);
							//System.out.println(bs.getStreaks().size());
							//System.out.println(i + " " + bs.getRunningTeam().getName());
						}
					}
					currentGame = games.get(i);
					boolean ifToPlaceBet = false;
					for(Streak st : bs.getStreaks()) { // iterate over streaks and check if there is one open streak associated to this game
						if(st.isActive()) ifToPlaceBet = ifToPlaceBet || (currentGame.isTeamPlaying(st.getTeam()) && bs.activeStreakExists() /* &&(!currentTimestamp.equals(currentGame.getTimestamp()))*/);	
					}
					
					/*for(Streak st : bs.getStreaks()) {
						System.out.println(st.isDefault());
					}*/
					/*System.out.println(bs.calcActiveStreaks());
					for(Team t : bs.getRunningTeams()) {
						System.out.println(t.getName());
					}*/
					//if(bs.getStreaks().size() > 0) System.out.println(bs.getStreaks().get(bs.getStreaks().size() - 1).getTeam().getName());
					if(ifToPlaceBet) { //if to place a bet
						//System.out.println("GAME : " + currentGame.getHome().getName() + " VS " + currentGame.getAway().getName());
						for(Streak st : bs.getStreaks()) {
							//System.out.println(st.getGames().contains(currentGame));
							/*System.out.println(st.getGames().contains(currentGame));
							System.out.println(st.getGames().size());*/
							for(Game g1 : st.getGames()) {
							//	if(st.isActive()) System.out.println("HOME " + g1.getHome().getName() +". AWAY " + g1.getAway().getName());
							}
							if(st.isActive() && st.getGames().contains(currentGame)) {
								
								bs.betPlaced(st.getTeam());
								//System.out.println("TEAM : " + st.getTeam().getName() +". Game Number " + st.calcGamesBettedOn());
								currentTimestamp = currentGame.getTimestamp();
								//System.out.println(currentTimestamp.toLocaleString() + " bet Placed "/* + st.getTeam().getName() + " Against " + currentGame.getOpponent(st.getTeam()).getName()*/);
							}
						}
					}
					
					currentGame.playMatch(Integer.parseInt(row[4]),
							Integer.parseInt(row[5]));
					
					if(ifToPlaceBet) { //if  bet placed
						//System.out.println("RESULT : " + currentGame.getResult());
						//System.out.println();	
							//System.out.println("DRAW! home:"+ currentGame.getHomeScore() + " away:"+currentGame.getAwayScore());
						for(Streak st : bs.getStreaks()) {
							
								if(st.getGames().contains(currentGame)) { //if game associated with streak
									if(currentGame.getResult() == 0 || st.calcGamesBettedOn() >= bs.getStreakLength()) {
										//System.out.println(bs.getStreakLength());
										if(st.isActive()) bs.disableStreak(st);
								}
							}
						}
						
						//System.out.println(bs.calcActiveStreaks());
						
					}
					bs.generateStreaks();
					
				}
				
				i++;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//System.out.println("Done");
	}
	public void addGames() {
		Calendar c = Calendar.getInstance();

		String csvFile = fileUrl;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] row = line.split(cvsSplitBy);

				if (s.findTeambyName(row[2]) == null)
					s.addTeam(new Team(row[2]));
				if (s.findTeambyName(row[3]) == null)
					s.addTeam(new Team(row[3]));

				/// game time
				Timestamp timestamp = null;
				try{
				    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
				    Date parsedDate = dateFormat.parse(row[1]);
				    timestamp = new java.sql.Timestamp(parsedDate.getTime());
				}catch(Exception e){//this generic but you can control another types of exception
					e.printStackTrace();
				}
				
				///
				
				Game g = new Game(s.findTeambyName(row[2]),
						s.findTeambyName(row[3]), s,
						Double.parseDouble(row[24]),
						Double.parseDouble(row[23]),
						Double.parseDouble(row[25]), "", timestamp);

				s.addGame(g);
				/*if (i < playAmount) {
					g.playMatch(Integer.parseInt(row[4]),
							Integer.parseInt(row[5]));
					i++;
				}*/

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//System.out.println("Done");
	}
}
