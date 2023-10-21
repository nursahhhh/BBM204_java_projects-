import java.util.Arrays;

public class playTheGame {
        public static String[][] change(String[][] Boards, int[] value, int[] loc, int row, int column) {
            // Summarise: row and column are the size of array.
            int out = loc[0];
            int in = loc[1];
            String[][] checkBoard = Arrays.stream(Boards).map(String[]::clone).toArray(String[][]::new);
            try {
                checkBoard[out][in] = checkBoard[out + value[0]][in + value[1]];
            } catch (ArrayIndexOutOfBoundsException o) {
                checkBoard = starOutOfBoard(checkBoard, value, out, in, row, column);
            }
            switch (checkBoard[out][in]) {
                case "W":
                    try {
                        String next = Boards[out - value[0]][in - value[1]];
                        Boards[out][in] = (next.equals("R") || next.equals("Y") || next.equals("B")) ? "X" : (Boards[out - value[0]][in - value[1]]);
                        Boards[out - value[0]][in - value[1]] = "*";
                    } catch (ArrayIndexOutOfBoundsException o) {
                        int[] valOpposite = new int[2];
                        valOpposite[0] = (-1) * value[0];
                        valOpposite[1] = (-1) * value[1];
                        Boards = starOutOfBoard(Boards, valOpposite, out, in, row, column);
                    }
                    break;
                case "H":
                    Boards[out][in] = " ";
                    break;
                case "R":
                case "Y":
                case "B":// save the r,y,b balls ın a list.
                    try {
                        Boards[out][in] = "X";
                        Boards[out + value[0]][in + value[1]] = "*";
                    } catch (ArrayIndexOutOfBoundsException o) {
                        Boards = starOutOfBoard(Boards, value, out, in, row, column);
                        break;
                    }
                    break;
                default:
                    try {
                        Boards[out][in] = Boards[out + value[0]][in + value[1]];
                        Boards[out + value[0]][in + value[1]] = "*";
                    } catch (ArrayIndexOutOfBoundsException o) {
                        Boards = starOutOfBoard(Boards, value, out, in, row, column);
                        break;
                    }
                    break;
            }
            return Boards;
        }
    public static String getBalls (String [][] Boards, int []value ,int[] loc ,int row, int column) {
        // Summarise: row and column are the size of array.
        int out = loc[0];
        int in = loc[1];
        String[][] checkBoard = Arrays.stream(Boards).map(String[]::clone).toArray(String[][]::new);
        String passing =" ";
        try {
            checkBoard[out][in] = checkBoard[out + value[0]][in + value[1]];
        } catch (ArrayIndexOutOfBoundsException o) {
            checkBoard = starOutOfBoard(checkBoard, value, out, in, row, column);
        }
        switch (checkBoard[out][in]) {
            case "W":
                try {String next =  Boards[out - value[0]][in - value[1]];
                    passing = ((next.equals("R")||next.equals("Y") || next.equals("B")) ? next : " " );
                } catch (ArrayIndexOutOfBoundsException o) {
                    break;
                }
                break;
            case "R": case "Y": case "B":// save the r,y,b balls ın a list.
                try {
                    passing = (Boards[out + value[0]][in + value[1]]);
                    Boards[out][in] = "X";
                    Boards[out + value[0]][in + value[1]] = "*";
                } catch (ArrayIndexOutOfBoundsException o) {
                    break;
                }
                break;
        }
        return passing;
    }
        public static String[][] starOutOfBoard(String[][] Boards, int[] value, int out, int in, int row, int column) {
            if ((out + value[0]) > row) {
                // items shifted  downward and  star comes to top.
                for (int i = row; i > 0; i--) {
                    Boards[i][in] = Boards[i - 1][in];
                }
                Boards[0][in] = "*";
            } else if ((out + value[0]) < 0) {
                // items shifted upward and  star comes to the end.
                for (int i = 0; i < row; i++) {
                    Boards[i][in] = Boards[i + 1][in];
                }
                Boards[row][in] = "*";
            }
            if ((in + value[1]) > column) {
                for (int k = column; k > 0; k--) {
                    Boards[out][k] = Boards[out][k - 1];
                }
                Boards[out][0] = "*";
            } else if ((in + value[1]) < 0) {
                for (int k1 = 0; k1 < column; k1++) {
                    Boards[out][k1] = Boards[out][k1 + 1];
                }
                Boards[out][column] = "*";
            }
            return Boards;
        }
        public static int[] findLocationOfStar(String[][] Board) {
            int outerIndex = 0;
            boolean isEnterFindStars = false;
            int[] loc = new int[2];
            for (String[] var : Board) {
                int innerIndex = 0;
                for (String var2 : var) {
                    if (var2.equals("*")) {
                        loc[0] = outerIndex;
                        loc[1] = innerIndex;
                        isEnterFindStars = true;
                    }
                    innerIndex++;
                }
                outerIndex++;
            }
           if (!isEnterFindStars){
               // -1 is the sign for determine if stars fall to hole and disappear.
               loc[0] = -1;
           }
            return loc;
        }
    }



