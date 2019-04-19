package scripts;

import java.io.PrintStream;

public class Sudoku
{

    static int recurCalls = 0;

    public Sudoku()
    {
    }

    public static int[] solve(int info[])
    {
        int matrix[][] = parseArray(info);
        if(solve(0, matrix))
        {
            return parseArray(matrix);
        } else
        {
            System.out.println("NONE");
            return (new int[] {
                99
            });
        }
    }

    static int nextPosition(int p)
    {
        int j = p & 0xf;
        if(j < 8)
        {
            return p + 1;
        }
        int i = p >> 4;
        if(i == 8)
        {
            return -1;
        } else
        {
            return i + 1 << 4;
        }
    }

    static boolean solve(int p, int cells[][])
    {
        recurCalls++;
        if(p == -1)
        {
            return true;
        }
        if(p == -2)
        {
            return false;
        }
        int j = p & 0xf;
        int i = p >> 4;
        if(cells[i][j] != 0)
        {
            return solve(nextPosition(p), cells);
        }
        for(int val = 1; val <= 9; val++)
        {
            if(legal(i, j, val, cells))
            {
                cells[i][j] = val;
                if(solve(nextPosition(p), cells))
                {
                    return true;
                }
            }
        }

        cells[i][j] = 0;
        return false;
    }

    static boolean legal(int i, int j, int val, int cells[][])
    {
        for(int k = 0; k < 9; k++)
        {
            if(val == cells[k][j])
            {
                return false;
            }
        }

        for(int k = 0; k < 9; k++)
        {
            if(val == cells[i][k])
            {
                return false;
            }
        }

        int boxRowOffset = (i / 3) * 3;
        int boxColOffset = (j / 3) * 3;
        for(int k = 0; k < 3; k++)
        {
            for(int m = 0; m < 3; m++)
            {
                if(val == cells[boxRowOffset + k][boxColOffset + m])
                {
                    return false;
                }
            }

        }

        return true;
    }

    static int[][] parseArray(int array[])
    {
        int puzzle[][] = new int[9][9];
        int index = 0;
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                puzzle[i][j] = array[index];
                index++;
            }

        }

        return puzzle;
    }

    static int[] parseArray(int array[][])
    {
        int puzzle[] = new int[82];
        int index = 0;
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                puzzle[index] = array[i][j];
                index++;
            }

        }

        return puzzle;
    }

    static void writeMatrix(int solution[][])
    {
        for(int i = 0; i < 9; i++)
        {
            if(i % 3 == 0)
            {
                System.out.println(" -----------------------");
            }
            for(int j = 0; j < 9; j++)
            {
                if(j % 3 == 0)
                {
                    System.out.print("| ");
                }
                System.out.print(solution[i][j] != 0 ? Integer.toString(solution[i][j]) : " ");
                System.out.print(' ');
            }

            System.out.println("|");
        }

        System.out.println(" -----------------------");
    }

}
