package kolokviumski.kol1.zad51;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */

class TeamStatistic implements Comparable<TeamStatistic>{
    private String name;
    private int goals;
    private int games_played;
    private int wins;
    private int draws;
    private int points;
    private int loses;
    private int takenGoals;

    public String getName() {
        return name;
    }

    public int getGoals() {
        return goals;
    }

    public int getTakenGoals() {
        return takenGoals;
    }
    public int getGames_played() {
        return games_played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getPoints() {
        return points;
    }

    public TeamStatistic(String name) {
        this.name = name;
        this.goals=0;
        this.games_played=0;
        this.wins=0;
        this.draws=0;
        this.points=0;
        this.takenGoals=0;
        this.loses=0;
    }

    public void update(int homeGoals, int awayGoals) {
        goals+=homeGoals;
        takenGoals+=awayGoals;
        games_played++;
        if(homeGoals>awayGoals){
            wins++;
            points =wins * 3 + draws;
        }
        else if(homeGoals==awayGoals){
            draws++;
            points =wins * 3 + draws;
        }
        else{
            loses++;
        }

    }

    //Тимовите се подредени според бројот на освоени поени во опаѓачки редослед,
    // ако имаат ист број на освоени поени според гол разликата (разлика од постигнатите голови и примените голови)
    // во опаѓачки редослед,
    // а ако имаат иста гол разлика, според името.
    @Override
    public int compareTo(TeamStatistic o) {
        int comparePoints = Integer.compare(o.points,this.points);
        int compareGoals = Integer.compare(o.goals-o.takenGoals,this.goals-this.takenGoals);
        int compareName = this.name.compareTo(o.name);

        if(comparePoints==0){
            if(compareGoals==0){
                return compareName;
            }
            else return compareGoals;
        }
        else return comparePoints;
    }

    public int getLoses() {
        return loses;
    }
}
class FootballTable{
    private Map<String, TeamStatistic> teams;

    public FootballTable() {
        this.teams = new TreeMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        teams.computeIfAbsent(homeTeam, k -> new TeamStatistic(homeTeam)).update(homeGoals,awayGoals);
        teams.computeIfAbsent(awayTeam, k -> new TeamStatistic(awayTeam)).update(awayGoals,homeGoals);
    }

    public void printTable(){
        List<TeamStatistic> table = new ArrayList<>(teams.values()).stream().sorted().collect(Collectors.toList());


        for (int i = 0; i < table.size(); i++) {
            TeamStatistic team = table.get(i);
            int rank = i + 1;
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n",
                    rank,
                    team.getName(),
                    team.getGames_played(),
                    team.getWins(),
                    team.getDraws(),
                    team.getLoses(),
                    team.getPoints()
            );
        }

    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

