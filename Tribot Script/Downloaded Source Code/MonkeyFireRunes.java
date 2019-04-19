package scripts;

import org.tribot.api.DynamicClicking;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.*;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Skarner
 * Date: 20.4.2013
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public class MonkeyFireRunes extends Script implements Painting, MessageListening07 {

    String state = "Starting";

    double version = 0.01;

    int ringsUsed;

    final RSTile[] toRuins = {
            new RSTile(3313, 3235),
            new RSTile(3307, 3241),
            new RSTile(3307, 3244),
            new RSTile(3308, 3247),
            new RSTile(3309, 3250),
            new RSTile(3312, 3253)};

    @Override
    public void playerMessageRecieved(String s, String s2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clanMessageRecieved(String s, String s2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void serverMessageRecieved(String s) {
        if(s.contains("crumble")){
            ringsUsed++;
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    enum State {
        BANK_OPEN,
        BANK_DEPOSIT,
        BANK_WITHDRAW,
        BANK_CLOSE,
        TELEPORT_TO_ALTAR,
        TELEPORT_TO_BANK,
        RUINS_ENTER,
        RUINS_WALK,
        CRAFT_RUNES,
        UNKNOWN
    }

    int[] duelRings = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};

    int essId = 7936;

    int fireRune = 554;

    int startXp;

    long startTime;

    int startLvl;

    State getState() {
        RSObject[] bankChest = Objects.findNearest(100, 4483);

        RSNPC[] duelArena = NPCs.find(957);
        if (Banking.isBankScreenOpen()) {
            if (Inventory.find(fireRune).length != 0) {
                return State.BANK_DEPOSIT;
            }
            if (Inventory.find(duelRings).length == 0 || Inventory.find(essId).length != 27) {
                return State.BANK_WITHDRAW;
            }
        }


        if (Inventory.find(essId).length != 27) {
            if (!Banking.isBankScreenOpen()) {
                if (bankChest != null && bankChest.length != 0) {
                    return State.BANK_OPEN;
                }else if( bankChest.length==0 && Player.getAnimation() == -1){
                    return State.TELEPORT_TO_BANK;
                }
            }
        } else {
            if (Banking.isBankScreenOpen()) {
                return State.BANK_CLOSE;
            }
            RSObject[] altar = Objects.findNearest(100, 10064);
            if(altar != null && altar.length != 0){
                return State.CRAFT_RUNES;
            }
            if (duelArena != null && duelArena.length != 0) {

                RSObject[] ruins = Objects.findNearest(100, 12730);
                if (ruins != null && ruins.length != 0 && ruins[0].isOnScreen()) {
                    return State.RUINS_ENTER;
                } else {
                    return State.RUINS_WALK;
                }
            }

            if(Player.getAnimation() == -1 && duelArena.length==0){
            return State.TELEPORT_TO_ALTAR;
            }
        }
        return State.UNKNOWN;
    }

    public String runTime(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = System.currentTimeMillis() - i;
        long hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    public int perHour(int gained) {
        return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
    }

    boolean running = true;

    @Override
    public void onPaint(Graphics graphics) {
        Graphics g = graphics;
        g.drawString("Monkey Fire Crafter v. " + version, 10, 100);
        g.drawString("Ran for " + runTime(startTime), 10, 120);
        g.drawString("State: " + state, 10, 140);
        g.drawString("XP gained:" + (Skills.getXP("Runecrafting") - startXp), 10, 160);
        g.drawString("XP/HR: " + perHour(Skills.getXP("Runecrafting") - startXp), 10, 180);


        int curLvl = Skills.getActualLevel("Runecrafting");

        int lvlsGained = curLvl - startLvl;

        g.drawString("Level: " + curLvl + " (+" + lvlsGained + ")", 10, 200);

        ///g.drawString("Rings used " + ringsUsed, 10, 220);         this is bugged because of message listener.

    }

    void onStart() {
        Mouse.setSpeed(randomInt(170, 190));
        startLvl = Skills.getActualLevel("Runecrafting");
        startXp = Skills.getXP("Runecrafting");
        startTime = System.currentTimeMillis();

    }

    @Override
    public void run() {
        onStart();
        while (running) {
            sleep(loop());

        }
    }


    void duelRingClick(int option) {

        RSInterfaceChild click = Interfaces.get(230).getChild(1 + option);
        click.click("Continue");
        Timing.waitCondition(new Condition() {

            @Override
            public boolean active() {
                return Player.getAnimation() == -1 && Interfaces.get(230) == null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, randomLong(2000, 2500));
    }

    void setRunOn() {

        sleep(new Random().nextInt(100));
        Keyboard.pressKey((char) KeyEvent.VK_CONTROL);
        sleep(new Random().nextInt(100));
    }

    void setRunOff() {
        sleep(new Random().nextInt(100));
        Keyboard.releaseKey((char) KeyEvent.VK_CONTROL);
        sleep(new Random().nextInt(100));
    }

    int loop() {
        State cur = getState();
        state = cur.name();
        switch (cur) {
            case CRAFT_RUNES:
                RSObject[] altar = Objects.findNearest(100, 10064);
                if(altar.length != 0){
                    if(altar[0].getPosition().distanceTo(Player.getPosition()) < 5){
                        if(Player.getAnimation() == -1)
                        DynamicClicking.clickRSObject(altar[0], "Craft");
                       // altar[0].click("Craft");
                        Timing.waitCondition(new Condition() {

                            @Override
                            public boolean active() {
                                return Inventory.getCount(essId) == 0;  //To change body of implemented methods use File | Settings | File Templates.
                            }
                        }, randomLong(500, 700));

                        return randomInt(500, 800);
                    }      else{
                        Walking.walkTo(altar[0].getPosition());
                        return randomInt(500, 700);
                    }
                }

            case RUINS_ENTER:
                final RSObject[] ruins = Objects.findNearest(100, 12730);
                if(ruins != null && ruins.length != 0 && ruins[0].isOnScreen()){
                    ruins[0].click("Enter");
                    Timing.waitCondition(new Condition() {

                        @Override
                        public boolean active() {
                            return !ruins[0].isOnScreen();  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }, randomLong(1000, 1500));

                    return randomInt(500, 800);
                }
            case RUINS_WALK:

                if(PathFinding.canReach(toRuins[toRuins.length-1], false)){
                    setRunOn();
                    Walking.walkPath(toRuins);
                    setRunOff();
                    return randomInt(500, 700);
                }

                RSObject[] gate = Objects.find(200, 3197);
                if(gate != null && gate.length != 0){
                     if(gate[0].isOnScreen()){
                         Camera.setCameraRotation(randomInt(85, 95));
                         RSTile a = new RSTile(3312, 3234);
                         Point p = Projection.tileToScreen(a, 100);
                         Mouse.move(p);
                         sleep(randomInt(100, 200));
                         Mouse.click(p, 0);
                        // DynamicClicking.clickRSObject(gate[0], "Open");
                         ///gate[0].click("Open");
                         return randomInt(2000, 3000);
                     }else{
                         Walking.walkTo(gate[0].getPosition());
                         return randomInt(500, 1000);
                     }
                }

            case TELEPORT_TO_BANK:
                if (Interfaces.get(230) != null) {
                    duelRingClick(1);
                    return randomInt(1000, 1500);
                }
                if(Player.getAnimation() != -1){
                    return randomInt(500, 1000);
                }
                RSItem[] duelRing2 = Inventory.find(duelRings);//[0];
                if (duelRing2 != null && duelRing2.length != 0 && Player.getAnimation() == -1 && Objects.findNearest(100, 4483).length == 0  ) {
                    duelRing2[0].click("Rub");
                    Timing.waitCondition(new Condition() {

                        @Override
                        public boolean active() {
                            return Interfaces.get(230) != null || Player.getAnimation() != -1;  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }, randomLong(2500, 3500));

                    return randomInt(500, 1500);
                }
                return 500;


            case TELEPORT_TO_ALTAR:
                if(Player.getAnimation() != -1){
                    return randomInt(300, 500);
                }
                if (Interfaces.get(230) != null) {
                    duelRingClick(0);
                    return randomInt(100, 400);
                }
                RSItem duelRing = Inventory.find(duelRings)[0];
                if (duelRing != null && NPCs.find(957).length ==0) {
                    duelRing.click("Rub");
                    Timing.waitCondition(new Condition() {

                        @Override
                        public boolean active() {
                            return Interfaces.get(230) != null;  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }, randomLong(2000, 2500));

                    return randomInt(100, 400);
                }
                return 500;
            case BANK_CLOSE:
                Banking.close();
                Timing.waitCondition(new Condition() {

                    @Override
                    public boolean active() {
                        return !Banking.isBankScreenOpen();  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, randomLong(1000, 1500));

                return randomInt(100, 400);
            case BANK_DEPOSIT:
                Banking.deposit(0, fireRune);
                Timing.waitCondition(new Condition() {

                    @Override
                    public boolean active() {
                        return Inventory.getCount(fireRune) == 0;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, randomLong(1000, 1500));

                return randomInt(100, 400);
            case BANK_WITHDRAW:
                if (Inventory.find(duelRings).length == 0) {
                    Banking.withdraw(1, duelRings);
                    Timing.waitCondition(new Condition() {

                        @Override
                        public boolean active() {
                            return Inventory.getCount(duelRings) != 0;  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }, randomLong(1000, 1500));

                    return randomInt(100, 400);
                }

                Banking.withdraw(0, essId);
                Timing.waitCondition(new Condition() {

                    @Override
                    public boolean active() {
                        return Inventory.getCount(essId) != 0;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, randomLong(1000, 1500));

                return randomInt(100, 400);

            case BANK_OPEN:
                if(Player.isMoving()){
                    return randomInt(100, 150);
                }
                RSObject[] bankChest = Objects.findNearest(100, 4483);
                if(bankChest[0].isOnScreen()){
                    bankChest[0].click("Use");
                    Timing.waitCondition(new org.tribot.api.types.generic.Condition() {

                        @Override
                        public boolean active() {
                            return Banking.isBankScreenOpen();  //To change body of implemented methods use File | Settings | File Templates.
                        }
                    }, randomLong(1000, 1500));

                    return randomInt(100, 500);
                }
                Walking.walkTo(bankChest[0].getPosition());
                return randomInt(500, 700);


        }
        return 100;
    }


    long randomLong(int start, int end) {

        Random r = new Random();

        return (long) start + r.nextInt(end - start);

    }

    int randomInt(int start, int end) {

        Random r = new Random();

        return start + r.nextInt(end - start);

    }
}