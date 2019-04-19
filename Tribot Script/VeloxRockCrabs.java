package scripts;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

// Referenced classes of package scripts.VeloxRockCrabs:
//            VeloxCrabsGUI, VeloxCrabsEating, VeloxCrabsFailsafe, VeloxRandomHandler, 
//            VeloxIdleFix

public class VeloxRockCrabs extends Script
    implements Painting
{

    public static boolean stop = false;
    public static int foodId;
    public static int runeIds;
    public static int eatLevel = 9;
    private RSTile bankTile;
    private int crabIds[] = {
        1266, 1265, 1267, 1268
    };
    public static Boolean isWaitingForRockMove = Boolean.valueOf(false);
    private int startXpAtt;
    private int startXpStr;
    private int startXpDef;
    private int startXpRange;
    private int startXpMage;
    private int startXpHealth;
    private final Color color1 = new Color(255, 255, 255);
    private final Font font1 = new Font("Arial", 1, 10);
    private final Image img1 = getImage("http://i.imgur.com/sJQZLHl.png");
    private long startTime;
    public static Boolean foundTarget = Boolean.valueOf(false);
    public static RSNPC crab = null;
    public static Boolean isWest = Boolean.valueOf(false);
    public static Boolean isRandomHandler = Boolean.valueOf(false);
    private Boolean findInCombatCrab;
    public static String Version = "0.19";

    public VeloxRockCrabs()
    {
        bankTile = new RSTile(2725, 3491);
        findInCombatCrab = Boolean.valueOf(false);
    }

    public void run()
    {
        Thread GUIThread = new Thread(new VeloxCrabsGUI());
        GUIThread.start();
        while(VeloxCrabsGUI.isGui.booleanValue()) 
        {
            try
            {
                Thread.sleep(50L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        GUIThread.interrupt();
        startXpAtt = Skills.getXP("ATTACK");
        startXpStr = Skills.getXP("STRENGTH");
        startXpDef = Skills.getXP("DEFENCE");
        startXpRange = Skills.getXP("RANGE");
        startXpMage = Skills.getXP("MAGIC");
        startXpHealth = Skills.getXP("HITPOINTS");
        startTime = System.currentTimeMillis();
        Mouse.setSpeed(250);
        Thread eating = new Thread(new VeloxCrabsEating());
        Thread failsafe = new Thread(new VeloxCrabsFailsafe());
        Thread randomHandling = new Thread(new VeloxRandomHandler());
        Thread idleFix = new Thread(new VeloxIdleFix());
        failsafe.start();
        randomHandling.start();
        idleFix.start();
        println("Starting Velox Crabs! :)");
        while(!stop) 
        {
            if(Player.getPosition().getX() <= 2696 && !Player.getRSPlayer().isInCombat() && !isWest.booleanValue())
            {
                walkPath(Walking.generateStraightPath(whichSide()));
            }
            if(!isRandomHandler.booleanValue() && Skills.getCurrentLevel("Hitpoints") > eatLevel)
            {
                if(eating.isAlive())
                {
                    eating.stop();
                }
                loop();
            } else
            {
                eating.run();
            }
            if(!failsafe.isAlive())
            {
                failsafe.start();
            }
            try
            {
                Thread.sleep(50L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public int loop()
    {
        if(atCrabs())
        {
            if(!hasFood())
            {
                println("Walking to bank");
                walkPath(whichSideToBank());
            }
        } else
        if(!hasFood());
        if(atBank())
        {
            bank();
        }
        return 1;
    }

    public boolean hasFood()
    {
        return Inventory.getCount(new int[] {
            foodId
        }) > 0;
    }

    public void attackCrabs()
    {
        RSNPC crabs[] = null;
        if(!foundTarget.booleanValue())
        {
            crabs = NPCs.findNearest(crabIds);
            RSNPC arr$[] = crabs;
            int len$ = arr$.length;
            int i$ = 0;
            do
            {
                if(i$ >= len$)
                {
                    break;
                }
                RSNPC testCrab = arr$[i$];
                crabs = NPCs.sortByDistance(Player.getPosition(), crabs);
                if(!testCrab.isInCombat() && testCrab.getInteractingCharacter() == null && !findInCombatCrab.booleanValue())
                {
                    if(isWest.booleanValue())
                    {
                        if(testCrab.getPosition().getX() < 2689)
                        {
                            crab = testCrab;
                            foundTarget = Boolean.valueOf(true);
                        }
                    } else
                    if(testCrab.getPosition().getX() > 2689)
                    {
                        crab = testCrab;
                        foundTarget = Boolean.valueOf(true);
                    }
                    break;
                }
                i$++;
            } while(true);
        }
        if(crab != null && Player.getRSPlayer().getInteractingCharacter() == null && crab.isValid() && !crab.isInCombat() && !findInCombatCrab.booleanValue())
        {
            if(crab.isOnScreen() && !crab.isInCombat() && Player.getAnimation() == -1)
            {
                if(crab.getName().equals("Rock Crab") && Player.getAnimation() == -1 && crab.getInteractingCharacter() == null && crab.isValid())
                {
                    crab.click(new String[] {
                        "Attack"
                    });
                } else
                if(crab.getName().equals("Rocks"))
                {
                    crab.click(new String[] {
                        "Walk here"
                    });
                    int Timer = 0;
                    do
                    {
                        if(Player.getRSPlayer().getAnimation() != -1 || Player.isMoving())
                        {
                            break;
                        }
                        isWaitingForRockMove = Boolean.valueOf(true);
                        try
                        {
                            Thread.sleep(100L);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        Timer += 100;
                        println(Integer.valueOf(Timer));
                        if(Timer != 1000)
                        {
                            continue;
                        }
                        println("Walking south");
                        Walking.generateStraightPath(new RSTile(2673, 3712));
                        if(walkPath(whichSideSafe()))
                        {
                            println("Walking back to crabs");
                            walkPath(whichSideFromBank());
                        }
                        break;
                    } while(true);
                    if(Player.getPosition().distanceTo(crab.getPosition()) < 3)
                    {
                        crab.click(new String[] {
                            "Attack"
                        });
                    }
                    isWaitingForRockMove = Boolean.valueOf(false);
                }
            } else
            if(Player.getAnimation() == -1)
            {
                Walking.walkTo(crab.getPosition());
            }
        } else
        if(crab != null)
        {
            if(!Player.getRSPlayer().isInCombat())
            {
                foundTarget = Boolean.valueOf(false);
            } else
            {
                RSNPC attackingMeCrab = NPCs.sortByDistance(Player.getPosition(), NPCs.findNearest(new String[] {
                    "Rock Crab", "Hobgoblin", "Warrior"
                }))[0];
                if(attackingMeCrab.isInteractingWithMe())
                {
                    attackingMeCrab.click(new String[] {
                        "Attack"
                    });
                }
                if(!attackingMeCrab.isValid())
                {
                    foundTarget = Boolean.valueOf(false);
                }
            }
        }
    }

    public boolean atBank()
    {
        return Player.getPosition().distanceTo(bankTile) < 10;
    }

    public boolean atCrabs()
    {
        return Player.getPosition().distanceTo(whichSide()) < 18;
    }

    public boolean walkPath(RSTile path[])
    {
        for(int i = closestTile(path); i < path.length; i++)
        {
            Point click = Projection.tileToMinimap(path[i]);
            if(Projection.isInMinimap(click))
            {
                Mouse.click(click, 1);
                long t = System.currentTimeMillis();
                if(i != path.length - 1)
                {
                    for(; !Projection.isInMinimap(Projection.tileToMinimap(path[i + 1])) && Timing.timeFromMark(t) < 5000L; sleep(50L)) { }
                    if(!Projection.isInMinimap(Projection.tileToMinimap(path[i + 1])))
                    {
                        Walking.blindWalkTo(path[i + 1]);
                    }
                    continue;
                }
                for(; distance(Player.getPosition(), path[i]) > 1 && Timing.timeFromMark(t) < 5000L; sleep(50L)) { }
                for(; Player.isMoving(); sleep(50L)) { }
                continue;
            }
            println("Searching for path...");
            Walking.blindWalkTo(path[i]);
            for(long t = System.currentTimeMillis(); distance(Player.getPosition(), path[i]) > 1 && Timing.timeFromMark(t) < 5000L; sleep(50L)) { }
            for(; Player.isMoving(); sleep(50L)) { }
        }

        return true;
    }

    public int closestTile(RSTile path[])
    {
        for(int i = path.length - 1; i >= 0; i--)
        {
            if(Projection.tileToMinimap(path[i]).getX() != -1D)
            {
                println((new StringBuilder()).append("closest point: ").append(i).toString());
                return i;
            }
        }

        return 0;
    }

    public void bank()
    {
        if(Inventory.getAll().length > 0)
        {
            if(Banking.isBankScreenOpen())
            {
                Banking.depositAllExcept(new int[] {
                    foodId
                });
            } else
            {
                Banking.openBankBooth();
            }
        } else
        if(Banking.isBankScreenOpen())
        {
            if(Banking.find(new int[] {
    foodId
}).length > 0)
            {
                println("Finding Food");
                Banking.withdraw(General.random(30, 100), new int[] {
                    foodId
                });
                sleep(300, 500);
            } else
            {
                println("Out of food - Stopping VeloxCrabs");
                stop = true;
            }
        } else
        {
            Banking.openBankBooth();
        }
        if(Inventory.getCount(new int[] {
    foodId
}) > 0)
        {
            walkPath(whichSideFromBank());
        }
    }

    public boolean walkTo(RSTile destination)
    {
        return !PathFinding.aStarWalk(destination) ? true : true;
    }

    public int distance(RSTile p1, RSTile p2)
    {
        return (int)Math.round(Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2D) + Math.pow(p1.getY() - p2.getY(), 2D)));
    }

    private Image getImage(String url)
    {
        return ImageIO.read(new URL(url));
        IOException e;
        e;
        return null;
    }

    public static RSTile whichSide()
    {
        RSTile returnTiles = null;
        if(isWest.booleanValue())
        {
            returnTiles = new RSTile(2675, 3715);
        } else
        if(!isWest.booleanValue())
        {
            returnTiles = new RSTile(2709, 3716);
        }
        return returnTiles;
    }

    public static RSTile[] whichSideFromBank()
    {
        RSTile returnTiles[] = null;
        if(isWest.booleanValue())
        {
            returnTiles = (new RSTile[] {
                new RSTile(2725, 3486), new RSTile(2719, 3486), new RSTile(2713, 3500), new RSTile(2702, 3509), new RSTile(2689, 3513), new RSTile(2683, 3525), new RSTile(2681, 3541), new RSTile(2674, 3551), new RSTile(2659, 3572), new RSTile(2654, 3581), 
                new RSTile(2654, 3595), new RSTile(2653, 3607), new RSTile(2658, 3619), new RSTile(2659, 3633), new RSTile(2662, 3645), new RSTile(2662, 3657), new RSTile(2670, 3668), new RSTile(2671, 3681), new RSTile(2673, 3694), new RSTile(2674, 3708)
            });
        } else
        {
            returnTiles = (new RSTile[] {
                new RSTile(2725, 3491), new RSTile(2725, 3486), new RSTile(2719, 3486), new RSTile(2713, 3500), new RSTile(2702, 3509), new RSTile(2689, 3513), new RSTile(2683, 3525), new RSTile(2681, 3541), new RSTile(2674, 3551), new RSTile(2659, 3572), 
                new RSTile(2654, 3581), new RSTile(2654, 3595), new RSTile(2656, 3607), new RSTile(2671, 3613), new RSTile(2697, 3614), new RSTile(2703, 3633), new RSTile(2700, 3641), new RSTile(2703, 3663), new RSTile(2703, 3663), new RSTile(2703, 3663), 
                new RSTile(2706, 3684), new RSTile(2706, 3703), new RSTile(2706, 3703), new RSTile(2709, 3719)
            });
        }
        return returnTiles;
    }

    public static RSTile[] whichSideToBank()
    {
        RSTile returnTiles[] = null;
        if(isWest.booleanValue())
        {
            returnTiles = (new RSTile[] {
                new RSTile(2674, 3708), new RSTile(2673, 3694), new RSTile(2671, 3681), new RSTile(2670, 3668), new RSTile(2662, 3657), new RSTile(2662, 3645), new RSTile(2659, 3633), new RSTile(2658, 3619), new RSTile(2653, 3607), new RSTile(2654, 3595), 
                new RSTile(2654, 3581), new RSTile(2659, 3572), new RSTile(2674, 3551), new RSTile(2681, 3541), new RSTile(2683, 3525), new RSTile(2689, 3513), new RSTile(2702, 3509), new RSTile(2713, 3500), new RSTile(2719, 3486), new RSTile(2725, 3486), 
                new RSTile(2725, 3491)
            });
        } else
        {
            returnTiles = (new RSTile[] {
                new RSTile(2706, 3703), new RSTile(2706, 3684), new RSTile(2703, 3663), new RSTile(2700, 3641), new RSTile(2703, 3633), new RSTile(2697, 3614), new RSTile(2671, 3613), new RSTile(2656, 3607), new RSTile(2654, 3595), new RSTile(2654, 3581), 
                new RSTile(2659, 3572), new RSTile(2674, 3551), new RSTile(2681, 3541), new RSTile(2683, 3525), new RSTile(2689, 3513), new RSTile(2702, 3509), new RSTile(2713, 3500), new RSTile(2719, 3486), new RSTile(2725, 3486), new RSTile(2725, 3491)
            });
        }
        return returnTiles;
    }

    public static RSTile[] whichSideSafe()
    {
        RSTile returnTiles[] = null;
        if(isWest.booleanValue())
        {
            returnTiles = (new RSTile[] {
                new RSTile(2673, 3712), new RSTile(2674, 3698), new RSTile(2673, 3684)
            });
        } else
        {
            returnTiles = (new RSTile[] {
                new RSTile(2706, 3703), new RSTile(2706, 3694), new RSTile(2703, 3686)
            });
        }
        return returnTiles;
    }

    public void onPaint(Graphics g1)
    {
        if(!VeloxCrabsGUI.isGui.booleanValue())
        {
            Graphics2D g = (Graphics2D)g1;
            g.drawImage(img1, 6, 282, null);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("ATTACK") - startXpAtt).toString(), 231, 368);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("STRENGTH") - startXpStr).toString(), 231, 382);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("DEFENCE") - startXpDef).toString(), 231, 397);
            g.drawString((new StringBuilder()).append("").append(toString(Long.valueOf(System.currentTimeMillis() - startTime))).toString(), 180, 462);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("HITPOINTS") - startXpHealth).toString(), 231, 411);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("RANGE") - startXpRange).toString(), 231, 425);
            g.drawString((new StringBuilder()).append("").append(Skills.getXP("MAGIC") - startXpMage).toString(), 231, 440);
            g.drawString((new StringBuilder()).append("").append(VeloxCrabsFailsafe.Timer).toString(), 360, 462);
            if(crab != null && crab.isValid() && foundTarget.booleanValue())
            {
                try
                {
                    drawTileNpc(crab.getPosition(), new Color(0, 0, 255, 30), g);
                }
                catch(RuntimeException e)
                {
                    println("Error displaying painted tile.");
                }
            }
            if(Player.getRSPlayer().getInteractingCharacter() != null)
            {
                try
                {
                    drawTileNpc(Player.getRSPlayer().getInteractingCharacter().getPosition(), new Color(255, 0, 0, 30), g);
                }
                catch(RuntimeException e)
                {
                    println("Error displaying interacting NPC");
                }
            }
        }
    }

    private void drawTileNpc(RSTile loc, Color c, Graphics g)
    {
        if(loc != null)
        {
            g.setColor(c);
            g.fillPolygon(Projection.getTileBoundsPoly(loc, 1));
        }
    }

    public String toString(Long ms)
    {
        long s = ms.longValue() / 1000L;
        long sec = s % 60L;
        long min = (s % 3600L) / 60L;
        long hrs = s / 3600L;
        return (new StringBuilder()).append(hrs).append(":").append(min).append(":").append(sec).toString();
    }

}
