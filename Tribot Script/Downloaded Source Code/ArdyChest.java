package scripts;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

public class ArdyChest extends Script
    implements Painting
{

    int initialLevel;
    int initialExp;
    int natureStolen;
    int ladderdown;
    int ladderup;
    String status;
    long t;
    int chestID;
    int natureBegin;
    int natureid;
    Image img;
    private int foodID;
    private int percentToEat;

    public ArdyChest()
    {
        ladderdown = 1740;
        ladderup = 1738;
        chestID = 2567;
        natureid = 561;
        percentToEat = 50;
    }

    public void onPaint(Graphics g)
    {
        int perHour = (int)(((double)(Inventory.getCount(new int[] {
            natureid
        }) - natureBegin) / (double)((getRunningTime() + 1000L) / 1000L)) * 3600D);
        int xpPerHour = (int)((long)(Skills.getXP("THIEVING") - initialExp) / ((getRunningTime() + 1000L) / 1000L)) * 3600;
        g.setColor(Color.BLACK);
        g.drawImage(img, 261, 302, null);
        g.setColor(Color.WHITE);
        g.drawString((new StringBuilder(String.valueOf(Skills.getXP("THIEVING") - initialExp))).append("  [").append(xpPerHour).append("/hr]").toString(), 376, 394);
        g.drawString((new StringBuilder(String.valueOf(Inventory.getCount(new int[] {
            natureid
        }) - natureBegin))).append("  (").append(perHour).append("/hr)").toString(), 376, 374);
        g.drawString(Timing.msToString(getRunningTime()), 376, 414);
    }



    public void run()
    {
        onStart();
        foodID = Integer.parseInt(JOptionPane.showInputDialog("Enter food ID: "));
        percentToEat = Integer.parseInt(JOptionPane.showInputDialog("Enter HP % to eat at: "));
        do
        {
            do
            {
                openchest();
            } while(!Player.getRSPlayer().isInCombat());
            escapeladder();
            sleep(400, 500);
            downladder();
            sleep(400, 500);
        } while(true);
    }

    private void downladder()
    {
        status = "Escaped combat, doc!";
        RSObject ladderups[] = Objects.findNearest(15, new int[] {
            ladderup
        });
        RSTile objecttwo = ladderups[0].getPosition();
        Camera.turnToTile(objecttwo);
        sleep(8000, 10000);
        restorehealth();
        if(ladderups.length > 0 && ladderups[0].isOnScreen())
        {
            println("found object 2");
            ladderups[0].click(new String[] {
                "Climb-up"
            });
            sleep(1000, 2000);
        }
        println("Scanning for ladder!!");
    }

    public static double hpPercent()
    {
        double currentHP = Skills.getCurrentLevel("Hitpoints");
        double totalHP = Skills.getActualLevel("Hitpoints");
        return (currentHP / totalHP) * 100D;
    }
    
    private void restorehealth()
    {
        if(hpPercent() < (double)percentToEat)
        {
            GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
            General.sleep(100, 110);
            RSItem food[] = Inventory.find(new int[] {
                foodID
            });
            if(food.length > 0)
            {
                food[0].click(new String[] {
                    "Eat"
                });
                General.sleep(800, 1300);
            }
        }
    }

    private void escapeladder()
    {
        status = "Abort!Abort! Escaping with the ladder!";
        RSObject ladderdowns[] = Objects.findNearest(15, new int[] {
            ladderdown
        });
        RSTile object = ladderdowns[0].getPosition();
        Camera.turnToTile(object);
        if(ladderdowns.length > 0 && ladderdowns[0].isOnScreen())
        {
            println("found object");
            ladderdowns[0].click(new String[] {
                "Climb-down"
            });
            sleep(5000, 8000);
        }
        println("Scanning for ladder!!");
    }

    private void onStart()
    {
        t = System.currentTimeMillis();
        natureBegin = Inventory.getCount(new int[] {
            natureid
        });
        Mouse.setSpeed(199);
        status = "Starting up!";
        initialExp = Skills.getXP("thieving");
        initialLevel = Skills.getActualLevel("thieving");
    }

    private void openchest()
    {
        status = "Stealing natures!";
        if(Objects.findNearest(4, new int[] {
    chestID
}).length >= 1)
        {
            sleep(30, 180);
            stealnature();
        } else
        {
            for(; Objects.findNearest(6, new int[] {
    chestID
}).length < 1; General.sleep(100, 160)) { }
        }
    }

    private void stealnature()
    {
        RSObject chests[] = Objects.findNearest(4, new int[] {
            chestID
        });
        if(chests[0].isOnScreen())
        {
            chests[0].click(new String[] {
                "Search for traps"
            });
            natureStolen++;
            
            for(; Player.getAnimation() != -1; sleep(50, 80)) { }
        }
    }

    public int randomRange(int aFrom, int aTo)
    {
        return aTo - (new Random()).nextInt(Math.abs(aTo - aFrom) + 1);
    }
}
