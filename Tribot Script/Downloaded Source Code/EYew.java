package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Constants;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

public class EYew extends Script
    implements Painting
{

    int rando;
    int logsChopped;
    int lastTreeIndex;
    int totallogs;
    RSTile LastTree;
    final RSTile escape = new RSTile(3093, 3475);
    final RSTile Door = new RSTile(3094, 3472);
    final int yewstree[] = {
        1309
    };
    final int ent = 778;
    final RSTile middle[] = {
        new RSTile(3086, 3478), new RSTile(3086, 3472)
    };
    final RSTile bankpath[] = {
        new RSTile(3093, 3485), new RSTile(3094, 3489)
    };
    final RSTile treepath[];
    final RSTile tree1[] = {
        new RSTile(3085, 3479), new RSTile(3086, 3471)
    };
    public long startTime;
    public int startexp;
    public int exp;
    public int expGained;
    public int currentEXP;
    public int currentLVL;
    public int nextLVL;
    final int nest = 5073;
    final int logs = 1515;
    final int booth = 7126;
    Point best;
    final Point offset = new Point(-20, 20);
    final Point random = new Point(-5, 5);
    int count;
    private final long START_TIME = System.currentTimeMillis();
    public int current_level;
    String status;
    public int starting_level;
    public int starting_exp;
    public int exp_gained;
    public int total_cut;
    public int yew_cost;
    int trips;
    int waiting;
    int stats;
    int cutting;

    public EYew()
    {
        logsChopped = 0;
        LastTree = new RSTile(3086, 3472);
        treepath = (new RSTile[] {
            new RSTile(3094, 3482), new RSTile(3093, 3471), new RSTile(3088, 3477), middle[0]
        });
        startTime = System.currentTimeMillis();
        count = 0;
        yew_cost = 450;
        starting_exp = 0;
        exp_gained = 0;
        total_cut = 0;
        waiting = 0;
        stats = 0;
        cutting = 0;
    }

    private boolean selectOptionsTab()
    {
        GameTab.getOpen();
        if(org.tribot.api2007.GameTab.TABS.OPTIONS != null)
        {
            GameTab.open(org.tribot.api2007.GameTab.TABS.OPTIONS);
            for(long l = System.currentTimeMillis(); Timing.timeFromMark(l) < (long)General.random(1500, 2000);)
            {
                if(GameTab.getOpen().equals(org.tribot.api2007.GameTab.TABS.OPTIONS))
                {
                    return true;
                }
            }

            sleep(50, 150);
        }
        return false;
    }

    public void run()
    {
    	
        starting_level = Skills.getActualLevel("Woodcutting");
        current_level = starting_level;
        starting_exp = Skills.getXP("Woodcutting");
        println("Thank You for using EYew.");
        if(!isRunOn())
        {
            toggleRun();
        }
        GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
        Camera.setCameraRotation(180);
        Mouse.setSpeed(General.random(125, 126));
        for(int i = 1; i != 0;)
        {
            while(!Ent() || !Spirit()) 
            {
                println("EYew Started");
                if(Tree() && Player.getPosition().distanceToDouble(bankpath[1]) > 4D)
                {
                    Nests();
                    println("Trees within range");
                    sleep(100, 200);
                    for(; !Inventory.isFull(); sleep(500, 800))
                    {
                        if(Player.getRSPlayer().isInCombat())
                        {
                            println("Running from combat");
                            WalkToBank();
                            sleep(100, 200);
                            WalkToTree();
                        }
                        Nests();
                        sleep(100, 250);
                        Chop();
                    }

                    sleep(100, 200);
                } else
                if(Player.getPosition().distanceTo(bankpath[1]) <= 4 || Ismiddle())
                {
                    WalkToTree();
                    performAntiban();
                }
                if(Inventory.isFull() && !BankIsOnScreen())
                {
                    WalkToBank();
                    sleep(1000L);
                }
                for(; Inventory.isFull() && !isAnimating() && (!Inventory.isFull() || !BankIsOnScreen()); WalkToBank())
                {
                    sleep(100, 200);
                }

                while(BankIsOnScreen() && Inventory.isFull()) 
                {
                    sleep(100, 200);
                    Bank();
                    if(!LogsInvent())
                    {
                        break;
                    }
                }
                for(; !LogsInvent() && !Ismiddle(); sleep(100, 200))
                {
                    println("Walking to tree's");
                    WalkToTree();
                }

            }
            if(Ent())
            {
                EscapeFromEnt();
                sleep(100, 200);
            }
            if(Spirit())
            {
                Door();
                Walking.walkTo(escape);
                sleep(100, 200);
                WalkToTree();
            }
            sleep(100, 200);
        }

    }

    private void Chop()
    {
        exp_gained = Skills.getXP("Woodcutting") - starting_exp;
        sleep(50L);
        total_cut = exp_gained / 175;
        current_level = Skills.getActualLevel("Woodcutting");
        if(genieNear())
        {
            try_genie();
        }
        plant();
        oldMan();
        Nests();
        if(!isAnimating())
        {
            RSObject arrayOfRSObject[] = Objects.find(50, yewstree);
            if(!Ent() && arrayOfRSObject.length > 0)
            {
                if(!arrayOfRSObject[0].click("Chop down", random, offset) && arrayOfRSObject.length != 0)
                {
                    println("chop");
                    Camera.setCameraRotation(180);
                    if(arrayOfRSObject[0].getPosition().distanceTo(middle[0]) <= 5 && !Player.isMoving())
                    {
                        Walking.walkTo(middle[0]);
                        println("mid0");
                        lastTreeIndex = 0;
                        LastTree = arrayOfRSObject[0].getPosition();
                        sleep(100, 300);
                    } else
                    if(arrayOfRSObject[0].getPosition().distanceTo(middle[1]) <= 5 && !Player.isMoving())
                    {
                        Walking.walkTo(middle[1]);
                        lastTreeIndex = 1;
                        LastTree = arrayOfRSObject[0].getPosition();
                        sleep(100, 300);
                    }
                    performAntiban();
                    sleep(700, 900);
                }
            } else
            if(!Ismiddle() || !BankIsOnScreen())
            {
                WalkToTree();
                Mouse.move(random);
                sleep(100, 200);
            }
            Nests();
            sleep(400, 700);
            if(Player.getRSPlayer().isInCombat())
            {
                Walking.walkTo(escape);
                sleep(100, 200);
            }
        }
        sleep(300, 400);
        if(Ent())
        {
            EscapeFromEnt();
            sleep(100, 200);
        }
        Nests();
        performAntiban();
        if(Player.getRSPlayer().isInCombat())
        {
            Walking.walkTo(escape);
            sleep(100, 200);
        }
        sleep(500, 800);
    }

    private boolean plant()
    {
        RSNPC arrayOfRSNPC[] = NPCs.findNearest(new String[] {
            "Strange plant"
        });
        if(arrayOfRSNPC.length > 0)
        {
            sleep(1000, 1500);
            arrayOfRSNPC[0].click(new String[] {
                "Pick"
            });
            if(arrayOfRSNPC.length > 0 && !Player.getRSPlayer().isInCombat() && Player.getAnimation() == -1)
            {
                sleep(3000, 8000);
                println("Waiting a bit after clicking the old man, so its not to bottisch.");
                if(arrayOfRSNPC.length > 0 && !Player.getRSPlayer().isInCombat() && Player.getAnimation() != -1)
                {
                    sleep(1000, 1500);
                    arrayOfRSNPC[0].click(new String[] {
                        "Pick"
                    });
                    println("Waiting a bit after clicking the old man, so its not to bottisch.");
                    return true;
                } else
                {
                    return true;
                }
            } else
            {
                return true;
            }
        } else
        {
            return false;
        }
    }

    private boolean oldMan()
    {
        RSNPC arrayOfRSNPC[] = NPCs.findNearest(new String[] {
            "Mysterious Old Man"
        });
        if(arrayOfRSNPC.length > 0)
        {
            sleep(1000, 1500);
            arrayOfRSNPC[0].click(new String[] {
                "Talk-to"
            });
            if(arrayOfRSNPC.length > 0 && !Player.getRSPlayer().isInCombat() && Player.getAnimation() == -1)
            {
                sleep(5000, 8000);
                println("Waiting a bit after clicking the old man, so its not to bottisch.");
                if(arrayOfRSNPC.length > 0 && !Player.getRSPlayer().isInCombat() && Player.getAnimation() != -1)
                {
                    sleep(1000, 1500);
                    arrayOfRSNPC[0].click(new String[] {
                        "Talk-to"
                    });
                    println("Waiting a bit after clicking the old man, so its not to bottisch.");
                    return true;
                } else
                {
                    return true;
                }
            } else
            {
                return true;
            }
        } else
        {
            return false;
        }
    }

    public boolean genieNear()
    {
        RSItem arrayOfRSItem[] = Inventory.find(new int[] {
            2528
        });
        return arrayOfRSItem.length > 0;
    }

    public void try_genie()
    {
        RSItem arrayOfRSItem[] = Inventory.find(new int[] {
            2528
        });
        if(arrayOfRSItem.length > 0)
        {
            arrayOfRSItem[0].click(new String[] {
                "Rub"
            });
            sleep(3000, 4000);
            Mouse.click(new Point(248, 228), 0);
            sleep(3000, 4000);
            Mouse.click(new Point(255, 272), 0);
            sleep(500, 1000);
        }
    }

    private void Nests()
    {
        RSGroundItem arrayOfRSGroundItem[] = GroundItems.find(new int[] {
            5073
        });
        if(arrayOfRSGroundItem.length > 0)
        {
            arrayOfRSGroundItem[0].click(new String[] {
                "Take"
            });
            sleep(450, 700);
        }
    }

    private boolean Ent()
    {
        RSObject arrayOfRSObject[] = Objects.findNearest(4, new int[] {
            778
        });
        if(arrayOfRSObject.length > 0)
        {
            println("Ent close to me");
            return true;
        } else
        {
            return false;
        }
    }

    private void WalkToBank()
    {
        Door();
        Walking.walkPath(bankpath);
        sleep(300, 500);
        Camera.setCameraRotation(30);
        for(; Player.isMoving(); sleep(200, 500)) { }
    }

    private boolean Spirit()
    {
        return Player.getRSPlayer().isInCombat();
    }

    private void reversePath(RSTile paramArrayOfRSTile[])
    {
        int i = 0;
        for(int j = paramArrayOfRSTile.length - 1; i < j; j--)
        {
            RSTile localRSTile = paramArrayOfRSTile[i];
            paramArrayOfRSTile[i] = paramArrayOfRSTile[j];
            paramArrayOfRSTile[j] = localRSTile;
            i++;
        }

    }

    private void WalkToTree()
    {
        if((BankIsOnScreen() || !Ismiddle()) && !Player.isMoving() && !Inventory.isFull())
        {
            println("walk");
            Door();
            Walking.walkPath(treepath);
            sleep(1200, 1500);
            Camera.setCameraRotation(180);
        }
        for(; Player.isMoving(); sleep(700, 1000)) { }
        if(!Player.isMoving())
        {
            ChooseTree();
        }
    }

    private void ChooseTree()
    {
        if(!Inventory.isFull() && !Ent())
        {
            if(middle[1].distanceTo(LastTree) <= 4 && Player.getPosition().distanceToDouble(middle[0]) > 3D)
            {
                println("mid03");
                Camera.setCameraRotation(180);
                Walking.walkTo(middle[0]);
                sleep(700, 1000);
            } else
            if(middle[0].distanceTo(LastTree) <= 4 && Player.getPosition().distanceToDouble(middle[1]) > 3D)
            {
                println("mid1");
                Camera.setCameraRotation(180);
                Walking.walkTo(middle[1]);
                sleep(700, 1000);
            }
        }
    }

    private boolean Ismiddle()
    {
        if(Player.getPosition().distanceToDouble(middle[0]) <= 4D || Player.getPosition().distanceToDouble(middle[1]) <= 4D)
        {
            return true;
        } else
        {
            println("not middle");
            return false;
        }
    }

    private boolean isRunOn()
    {
        selectOptionsTab();
        return org.tribot.api.Screen.coloursMatch(new Color(123, 29, 27), Screen.getColorAt(630, 423), new Tolerance(10));
    }

    private void toggleRun()
    {
        selectOptionsTab();
        if(!isRunOn())
        {
            RSInterfaceChild localRSInterfaceChild = Interfaces.get(261, 40);
            localRSInterfaceChild.click(new String[0]);
            sleep(200, 500);
        }
    }

    private boolean BankIsOnScreen()
    {
        int tmp5_3[] = new int[1];
        getClass();
        tmp5_3[0] = 7126;
        RSObject arrayOfRSObject[] = Objects.find(10, tmp5_3);
        if(arrayOfRSObject.length > 0 && arrayOfRSObject[0].isOnScreen() && Inventory.isFull())
        {
            println("I see the banker");
            sleep(100, 200);
            return true;
        } else
        {
            return false;
        }
    }

    private boolean LogsInvent()
    {
        RSItem arrayOfRSItem[] = Inventory.find(new int[] {
            1515
        });
        return arrayOfRSItem.length > 0 && Inventory.isFull();
    }

    private boolean Tree()
    {
        RSObject arrayOfRSObject[] = Objects.find(30, yewstree);
        return arrayOfRSObject.length > 0;
    }

    private void EscapeFromEnt()
    {
        if(Ent() && isAnimating())
        {
            println("Escaping....");
            if(Player.getPosition().distanceTo(middle[1]) < 4)
            {
                Walking.walkTo(middle[1]);
                sleep(500, 750);
            } else
            {
                Walking.walkTo(middle[0]);
                sleep(500, 750);
            }
        }
    }

    private void Bank()
    {
        GameTab.getOpen();
        if(!BankIsOnScreen() && Inventory.isFull())
        {
            WalkToBank();
            sleep(1000, 1400);
        }
        if(BankIsOnScreen() && (Inventory.isFull()))
        {
        	RSObject [] bank = Objects.findNearest(10, 7126);
        	if(bank != null)
        		bank[0].click("Bank");
        	sleep(1000);
            if(Banking.isBankScreenOpen())
            {
                Banking.depositAllExcept(org.tribot.api2007.Constants.IDs.Items.hatchets);
                int tmp57_55[] = new int[1];
                getClass();
                tmp57_55[0] = 1515;
                RSItem arrayOfRSItem[] = Banking.find(tmp57_55);
                totallogs = arrayOfRSItem[0].getStack();
            } else
            {
                return;
            }
            sleep(800, 1000);
            Banking.close();
        }
        logsChopped += 27;
        exp += 1836;
        Camera.setCameraRotation(180);
        WalkToTree();
        sleep(500, 700);
    }

    private boolean isAnimating()
    {
        return Player.getAnimation() != -1;
    }

    public void moveMouseSlightly()
    {
        Point localPoint = Mouse.getPos();
        int i = General.random(-20, 20);
        int j = General.random(-20, 20);
        Mouse.move(localPoint.x + i, localPoint.y + j);
    }

    public void moveMouseFar()
    {
        Point localPoint = Mouse.getPos();
        int i = General.random(-200, 200);
        int j = General.random(-200, 200);
        Mouse.move(localPoint.x + i, localPoint.y + j);
    }

    public void performAntiban()
    {
        int i = General.random(1, 100);
        int j = General.random(0, 100);
        int k = General.random(0, 10);
        int m = General.random(0, 1);
        int n = General.random(0, 1);
        switch(i)
        {
        case 1: // '\001'
        case 3: // '\003'
        case 4: // '\004'
        case 6: // '\006'
        case 9: // '\t'
        case 10: // '\n'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        default:
            break;

        case 2: // '\002'
            if(Player.getAnimation() != -1 && rando < 5)
            {
                GameTab.open(org.tribot.api2007.GameTab.TABS.STATS);
                sleep(800, 1000);
                Mouse.move(712 + General.random(0, 4), 377 + General.random(0, 4));
                sleep(3000, 5000);
                GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
            }
            break;

        case 5: // '\005'
            moveMouseSlightly();
            break;

        case 7: // '\007'
            moveMouseSlightly();
            break;

        case 8: // '\b'
            moveMouseFar();
            break;

        case 11: // '\013'
            moveMouseSlightly();
            break;

        case 12: // '\f'
            moveMouseFar();
            break;
        }
    }

    private void Door()
    {
        RSObject arrayOfRSObject[] = Objects.findNearest(30, new int[] {
            1536
        });
        if(arrayOfRSObject.length > 0)
        {
            Walking.blindWalkTo(Door);
            DynamicClicking.clickRSObject(arrayOfRSObject[0], "open");
        }
    }

    private void drawMouse(Graphics paramGraphics)
    {
        Point localPoint = Mouse.getPos();
        paramGraphics.setColor(new Color(0, 0, 0, 50));
        paramGraphics.fillOval(localPoint.x - 5, localPoint.y - 5, 10, 10);
        paramGraphics.setColor(Color.BLACK);
        paramGraphics.drawLine(0, localPoint.y, 766, localPoint.y);
        paramGraphics.drawLine(localPoint.x, 0, localPoint.x, 505);
    }

    public void onPaint(Graphics paramGraphics)
    {
        Graphics localGraphics = paramGraphics;
        long l = System.currentTimeMillis() - START_TIME;
        localGraphics.drawString((new StringBuilder("Status: ")).append(status).toString(), 10, 300);
        localGraphics.drawString("EYew Private", 10, 270);
        localGraphics.drawString((new StringBuilder("Total Cut: ")).append(total_cut).toString(), 10, 315);
        localGraphics.drawString((new StringBuilder("Running for: ")).append(Timing.msToString(l)).toString(), 10, 285);
        localGraphics.drawString((new StringBuilder("Cash Made:")).append(total_cut * yew_cost).toString(), 10, 330);
    }
}
