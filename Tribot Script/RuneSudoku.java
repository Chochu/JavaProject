package scripts;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.JCheckBox;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

// Referenced classes of package scripts:
//            RuneSudokuGUI, Sudoku

public class RuneSudoku extends Script
    implements Painting
{

    int runeGrid[];
    int chests;
    long startTime;
    int runeGridFillIn[];
    private Object lastText;
    private int currentlySelected;
    public int runesToBuy[];

    public RuneSudoku()
    {
        runeGrid = new int[82];
        chests = 0;
        runeGridFillIn = new int[82];
        currentlySelected = -1;
        runesToBuy = new int[14];
    }

    public void run()
    {
        Mouse.setSpeed(150);
        startTime = System.currentTimeMillis();
        do
        {
            try
            {
                loop();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } while(true);
    }

    public int getValuesOfRunes()
    {
        int total = 0;
        total += Inventory.getCount(new int[] {
            566
        }) * 300;
        total += Inventory.getCount(new int[] {
            565
        }) * 300;
        total += Inventory.getCount(new int[] {
            558
        }) * 17;
        total += Inventory.getCount(new int[] {
            563
        }) * 280;
        total += Inventory.getCount(new int[] {
            561
        }) * 240;
        total += Inventory.getCount(new int[] {
            559
        }) * 14;
        total += Inventory.getCount(new int[] {
            562
        }) * 110;
        total += Inventory.getCount(new int[] {
            560
        }) * 230;
        total += Inventory.getCount(new int[] {
            564
        }) * 100;
        return total;
    }

    public void printRuneGrid()
    {
        for(int i = 0; i < 81; i++)
        {
            println(Integer.valueOf(runeGrid[i]));
        }

    }

    public int getSudokuIDForRune(int ID)
    {
        switch(ID)
        {
        case 558: 
        case 6436: 
            return 1;

        case 554: 
        case 6428: 
            return 2;

        case 559: 
        case 6438: 
            return 3;

        case 556: 
        case 6422: 
            return 4;

        case 560: 
        case 6432: 
            return 5;

        case 555: 
        case 6424: 
            return 6;

        case 562: 
        case 6430: 
            return 7;

        case 557: 
        case 6426: 
            return 8;

        case 563: 
        case 6434: 
            return 9;
        }
        return 0;
    }

    public int getRuneForSudokuID(int ID)
    {
        switch(ID)
        {
        case 1: // '\001'
            return 558;

        case 2: // '\002'
            return 554;

        case 3: // '\003'
            return 559;

        case 4: // '\004'
            return 556;

        case 5: // '\005'
            return 560;

        case 6: // '\006'
            return 555;

        case 7: // '\007'
            return 562;

        case 8: // '\b'
            return 557;

        case 9: // '\t'
            return 563;
        }
        return 0;
    }

    public void loop()
    {
        sleep(100L);
        if(Inventory.getCount(new int[] {
    995
}) < 50000)
        {
            customLog("Start the script with at least 50k");
            return;
        }
        if(Interfaces.get(288, 10) != null)
        {
            if(runeGrid[0] == 0 && runeGridFillIn[0] == 0)
            {
                populateRuneList();
                runeGridFillIn = Sudoku.solve(runeGrid);
                if(runeGridFillIn[0] == 99)
                {
                    closeInterface();
                }
                return;
            }
            customLog("Solving the puzzle");
            if(!isGridCorrect())
            {
                if(!enterRuneValues() && !fixRuneValues())
                {
                    closeInterface();
                }
            } else
            if(Interfaces.get(288, 131).getItems().length == 81)
            {
                customLog("Clicking to complete puzzle");
                completePuzzle();
            }
        } else
        if(continueDialogue())
        {
            customLog("Talking to the dude");
        } else
        if(shopInterfaceOpen())
        {
            customLog("Buying runes");
            if(!buyProfitableRunes())
            {
                closeShopInterface();
            }
        } else
        {
            customLog("Finding dude");
            resetPuzzle();
            getFaggot().click(new String[] {
                "Talk-to"
            });
            sleep(500L);
        }
    }

    public void resetPuzzle()
    {
        for(int i = 0; i < 81; i++)
        {
            runeGridFillIn[i] = 0;
            runeGrid[i] = 0;
        }

    }

    public boolean continueDialogue()
    {
        if(NPCChat.getMessage() != null && NPCChat.getMessage().contains("Hang on a second"))
        {
            int fails = 0;
            NPCChat.clickContinue(true);
            while(!NPCChat.selectOption("large casket of runes", true)) 
            {
                sleep(100L);
                if(++fails == 50)
                {
                    break;
                }
            }
            return true;
        }
        if(NPCChat.selectOption("Find out what the runes are", true))
        {
            int fails = 0;
            while(!shopInterfaceOpen()) 
            {
                sleep(100L);
                if(++fails == 15)
                {
                    break;
                }
            }
            chests++;
            return true;
        }
        if(NPCChat.selectOption("large casket of runes", true))
        {
            return true;
        }
        if(NPCChat.selectOption("Examine lock", true))
        {
            return true;
        }
        if(NPCChat.clickContinue(true))
        {
            return true;
        }
        return NPCChat.selectOption("I would like to have a look at your selection of runes", true);
    }

    public boolean buyProfitableRunes()
    {
        Interfaces.getAll();
        RSItem items[] = Interfaces.get(300, 75).getItems();
        for(int i = 0; i < items.length; i++)
        {
            if(isProfitable(items[i]))
            {
                items[i].changeType(org.tribot.api2007.types.RSItem.TYPE.BANK);
                for(; items[i].click("Buy 10", null, new Point(0, 10)); sleep(50, 200))
                {
                    Interfaces.getAll();
                    items[i] = Interfaces.get(300, 75).getItems()[i];
                    if(items[i].getStack() >= 11)
                    {
                        continue;
                    }
                    sleep(600, 800);
                    break;
                }

                return true;
            }
        }

        return false;
    }

    public boolean isProfitable(RSItem i)
    {
        if(i == null)
        {
            return false;
        }
        for(int j = 1; j < 14; j++)
        {
                return true;
        }

        return false;
    }

    public void closeShopInterface()
    {
        for(; Interfaces.get(300, 91) != null; sleep(500, 800))
        {
            Interfaces.get(300, 91).click(new String[] {
                "Close"
            });
        }

    }

    public boolean shopInterfaceOpen()
    {
        return Interfaces.get(300, 91) != null;
    }

    public RSNPC getFaggot()
    {
        RSNPC fagboi[] = NPCs.find(new int[] {
            1862
        });
        if(fagboi.length < 1)
        {
            return null;
        } else
        {
            return fagboi[0];
        }
    }

    public void completePuzzle()
    {
        int fails = 0;
        while(Interfaces.get(288, 8) != null) 
        {
            Interfaces.get(288, 8).click(new String[] {
                "Ok"
            });
            sleep(900, 1200);
            if(++fails == 5)
            {
                closeInterface();
            }
        }
    }

    public void closeInterface()
    {
        customLog("Closing interface");
        for(; Interfaces.get(288, 132) != null; sleep(500, 800))
        {
            Interfaces.get(288, 132).click(new String[] {
                "Close"
            });
        }

    }

    public void enterRuneIntoGrid(int i)
    {
        try
        {
            RSInterfaceChild highalch = Interfaces.get(288, 10 + i);
            if(currentlySelected != getInterfaceForRuneValue(runeGridFillIn[i]))
            {
                Point p = Interfaces.get(288, getInterfaceForRuneValue(runeGridFillIn[i])).getAbsolutePosition();
                p.x += General.random(10, 35);
                p.y += General.random(10, 35);
                Mouse.click(p, 1);
            }
            currentlySelected = getInterfaceForRuneValue(runeGridFillIn[i]);
            Mouse.click(new Point((int)highalch.getAbsolutePosition().getX() + General.random(0, 30), (int)highalch.getAbsolutePosition().getY() + General.random(0, 30)), 1);
            sleep(150, 400);
        }
        catch(Exception exception) { }
    }

    public boolean enterRuneValues()
    {
        boolean success = false;
        Interfaces.getAll();
        for(int j = 0; j < 10; j++)
        {
            for(int i = 0; i < 81; i++)
            {
                if(runeGrid[i] == 0 && runeGridFillIn[i] == j)
                {
                    try
                    {
                        if(isCellEmpty(Interfaces.get(288, 10 + i).getAbsolutePosition()))
                        {
                            success = true;
                            enterRuneIntoGrid(i);
                        }
                    }
                    catch(Exception e)
                    {
                        enterRuneIntoGrid(i);
                    }
                }
            }

        }

        return success;
    }

    public boolean fixRuneValues()
    {
        boolean success = false;
        Interfaces.getAll();
        for(int i = 0; i < 81; i++)
        {
            if(runeGrid[i] == 0)
            {
                try
                {
                    if(Interfaces.get(288, 131).getItems()[i].getID() != getRuneForSudokuID(runeGridFillIn[i]))
                    {
                        success = true;
                        enterRuneIntoGrid(i);
                    }
                }
                catch(Exception e)
                {
                    enterRuneIntoGrid(i);
                }
            }
        }

        return success;
    }

    public boolean isGridCorrect()
    {
        Interfaces.getAll();
        RSItem items[] = Interfaces.get(288, 131).getItems();
        if(items.length < 81)
        {
            return false;
        }
        for(int i = 0; i < 81; i++)
        {
            if(items[i].getID() != getRuneForSudokuID(runeGridFillIn[i]) && items[i].getID() < 600)
            {
                return false;
            }
        }

        return true;
    }

    public int getInterfaceForRuneValue(int ID)
    {
        switch(ID)
        {
        case 8: // '\b'
            return 91;

        case 6: // '\006'
            return 92;

        case 4: // '\004'
            return 93;

        case 2: // '\002'
            return 94;

        case 1: // '\001'
            return 95;

        case 3: // '\003'
            return 96;

        case 5: // '\005'
            return 97;

        case 7: // '\007'
            return 98;

        case 9: // '\t'
            return 99;

        case 0: // '\0'
            return 100;
        }
        return 0;
    }

    public void populateRuneList()
    {
        Interfaces.getAll();
        RSInterfaceChild highalch = null;
        for(int i = 0; i < 81; i++)
        {
            highalch = Interfaces.get(288, 10 + i);
            if(highalch == null)
            {
                i--;
            } else
            {
                Point p = highalch.getAbsolutePosition();
                if(isCellEmpty(p))
                {
                    runeGrid[i] = 0;
                } else
                {
                    runeGrid[i] = -1;
                }
            }
        }

        highalch = Interfaces.get(288, 131);
        RSItem runeItems[] = highalch.getItems();
        int index = 0;
        for(int i = 0; i < 81; i++)
        {
            if(runeGrid[i] == -1)
            {
                runeGrid[i] = getSudokuIDForRune(runeItems[index].getID());
                index++;
            }
        }

    }

    public boolean isCellEmpty(Point p)
    {
        p.x += 10;
        p.y += 10;
        Color color1 = new Color(233, 192, 1);
        Color color2 = new Color(250, 153, 7);
        return Screen.getColorAt(p).equals(color1) || Screen.getColorAt(p).equals(color2);
    }

    public void onPaint(Graphics g1)
    {
        Graphics2D g = (Graphics2D)g1;
        g.drawString("Moose's Sudoku Solver - BETA", 10, 285);
        if(lastText != null)
        {
            g.drawString((new StringBuilder("Status: ")).append(lastText.toString()).toString(), 10, 300);
        }
        if((new StringBuilder()).append(chests).toString() != null)
        {
            g.drawString((new StringBuilder("Chests done: ")).append(chests).toString(), 10, 310);
        }
        if((new StringBuilder()).append(runTime(startTime)).toString() != null)
        {
            g.drawString((new StringBuilder("Time running: ")).append(runTime(startTime)).toString(), 10, 320);
        }
        g.drawString((new StringBuilder("Value of runes: ")).append(getValuesOfRunes()).toString(), 10, 330);
    }

    public void customLog(Object text)
    {
        lastText = text;
    }

    public String runTime(long i)
    {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = System.currentTimeMillis() - i;
        long hours = millis / 0x36ee80L;
        millis -= hours * 0x36ee80L;
        long minutes = millis / 60000L;
        millis -= minutes * 60000L;
        long seconds = millis / 1000L;
        return (new StringBuilder(String.valueOf(nf.format(hours)))).append(":").append(nf.format(minutes)).append(":").append(nf.format(seconds)).toString();
    }
}
