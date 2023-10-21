import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static String[] readFile(String path) {
        try {
            int i = 0;
            int length = Files.readAllLines(Paths.get(path)).size();
            String[] results = new String[length];
            for (String line : Files.readAllLines(Paths.get(path))) {
                results[i++] = line;
            }
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToOutputFile(String value) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true));
            bw.write(value);
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printOutput(String[][] InitialBoard, ArrayList<String> movement, String[][] LastBoard, int score) {
        writeToOutputFile("Game board: \n");
        for (String[] rows : InitialBoard) {
            for (String columns : rows) {
                writeToOutputFile(columns + " ");
            }
            writeToOutputFile("\n");
        }
        writeToOutputFile("\nYour movement is: \n");
        for (String mv : movement) {
            writeToOutputFile(mv + " ");
        }
        writeToOutputFile("\n\n");

        writeToOutputFile("Your output is: \n");
        for (String[] rows2 : LastBoard) {
            for (String columns2 : rows2) {
                writeToOutputFile(columns2 + " ");
            }
            writeToOutputFile("\n");
        }
        writeToOutputFile("\nGame over!\n");
        writeToOutputFile("Score:  " + score);
    }

    public static void main(String[] args) {
         // read input files and create 2 dimensional array.
        String[] boards = readFile(args[1]);
        String[] move = boards[0].split(" ");
        String[] lines = readFile(args[0]);

        // calculate the size for creating 2 dimensional array.
        int i = lines != null ? lines.length : 0;
        int i2 = (lines[0].split(" ")).length;
        String[][] boardValues = new String[i][i2];

        // split every lıne to get every value char by char. And add them to array.
        int a = 0;
        for (String line : lines) {
            String[] parts = line.split(" ");
            int b = 0;
            for (String part : parts) {
                boardValues[a][b] = part;
                b++;
            }
            a++;
        }
        // use copy of real board to manıpulate data
        String[][] copyBoard = Arrays.stream(boardValues).map(String[]::clone).toArray(String[][]::new);
        String[][] findBalls =  Arrays.stream(boardValues).map(String[]::clone).toArray(String[][]::new);
        int row = (copyBoard.length) - 1;
        int column = (copyBoard[0].length) - 1;
        ArrayList<String> playedMoves = new ArrayList<>();
        ArrayList <String> rybBalls = new ArrayList<>();
        String[][] lastStituation = copyBoard;

        for (String mv : move) {
            int []location =  playTheGame.findLocationOfStar(lastStituation);
            if ( location [0] == -1){ // -1 means star fall to hole and disappear.
                break;
            }
            playedMoves.add(mv);
            switch (mv) {
                case "L":
                    int[] through = {0, -1};
                    rybBalls.add(playTheGame.getBalls(findBalls, through, location, row, column));
                    lastStituation = playTheGame.change(lastStituation, through, location, row, column);
                    break;
                case "R":
                    int[] through1 = {0, 1};
                    rybBalls.add(playTheGame.getBalls(findBalls, through1, location, row, column));
                    lastStituation = playTheGame.change(lastStituation, through1, location, row, column);
                    break;
                case "U":
                    int[] through2 = {-1, 0};
                    rybBalls.add(playTheGame.getBalls(findBalls, through2, location, row, column));
                    lastStituation =  playTheGame.change(lastStituation, through2, location, row, column);
                    break;
                case "D":
                    int[] through3 = {1, 0};
                    rybBalls.add(playTheGame.getBalls(findBalls, through3, location, row, column));
                    lastStituation =  playTheGame.change(lastStituation, through3, location, row, column);
                default:
                    break;
            }
        }
        int reds = Collections.frequency(rybBalls, "R");
        int yellows = Collections.frequency(rybBalls, "Y");
        int blacks = Collections.frequency(rybBalls, "B");
        int totalPoint = (reds * 10) + (yellows * 5) + (blacks * -5);
        copyBoard = lastStituation;
        printOutput(boardValues, playedMoves, copyBoard, totalPoint);
    }
}
