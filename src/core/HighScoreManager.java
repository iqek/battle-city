package core;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreManager {

    private static final String FILE_PATH = "resources/scores.csv";

    public static void save(HighScore hs){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            writer.write(hs.getName() + "," + hs.getScore() + "," + hs.getDate() + "," + hs.getTime() + "," + hs.getMap());
            writer.newLine();
        } catch(IOException e){
            System.err.println("could not save high score: " + e.getMessage());
        }
    }

    public static List<HighScore> getTopTen(){
        List<HighScore> list = new ArrayList<>();

        File file = new File(FILE_PATH);
        if(!file.exists()) return list;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 5){
                    list.add(new HighScore(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3], parts[4]));
                }
            }
        } catch(IOException e){
            System.err.println("could not read high scores: " + e.getMessage());
        }

        Collections.sort(list, new Comparator<HighScore>(){
            @Override
            public int compare(HighScore a, HighScore b){
                return b.getScore()-a.getScore();
            }
        });

        if(list.size() > 10) return list.subList(0,10);
        return list;
    }
}
