package core;

public class HighScore {
    private String name;
    private int score;
    private String date;
    private String time;
    private String map;

    public HighScore(String name, int score, String date, String time, String map){
        this.name = name;
        this.score = score;
        this.date = date;
        this.time = time;
        this.map = map;
    }

    public String getName(){ return name;}
    public int getScore(){ return score; }
    public String getDate(){ return date;}
    public String getTime(){ return time;}
    public String getMap() { return map; }
}
