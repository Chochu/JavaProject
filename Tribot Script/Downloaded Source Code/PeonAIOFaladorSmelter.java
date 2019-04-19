package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors={"Exxos"}, category="Smithing", name="Peon Falador Smelter", description="<html> \t<body> \t\t<h1 style=\"text-align: center; \">\t\t\tPeon AIO Smelter</h1>\t\t<p style=\"text-align: center; \">\t\t\tBar:&nbsp;<select name=\"Bar\"><option selected=\"selectedv\" value=\"\">Bronze</option><option selected=\"selectedv\" value=\"\">Iron</option><option selected=\"selectedv\" value=\"\">IronROF</option><option selected=\"selectedv\" value=\"\">Steel</option><option selected=\"selectedv\" value=\"\">Silver</option><option selected=\"selectedv\" value=\"\">Gold</option><option selected=\"selectedv\" value=\"\">Mithril</option><option selected=\"selectedv\" value=\"\">Adamant</option><option selected=\"selectedv\" value=\"\">Rune</option></select></p>\t\t<h2 style=\"text-align: right; \">\t\t\tTested by: Elytius</h2>\t</body> </html> ")
public class PeonAIOFaladorSmelter extends Script
  implements Painting, Arguments
{
  private final RSTile inBank = new RSTile(3013, 3355, 0);
  private final RSTile inFurnace = new RSTile(2975, 3369, 0);
  Random r = new Random();
  int i1 = this.r.nextInt(2000) + 3000;
  Random rand = new Random();
  int i2 = this.rand.nextInt(15) + 65;
  public static int COUNTER = 0;
  public int PLACEHOLDER = 0;
  private long startTime;
  private final RSTile tileOne = new RSTile(3012, 3359, 0);
  private final RSTile tileTwo = new RSTile(2999, 3365, 0);
  private final RSTile tileThree = new RSTile(2987, 3372, 0);
  private final RSTile tileFour = new RSTile(2971, 3375, 0);
  private final RSTile tileFive = new RSTile(2969, 3379, 0);
  public int initialXP;
  private int oreID1 = 0;
  private int oreID2 = 0;
  private int oreID1Count = 0;
  private int ore2Count = 0;
  public int copperID = 436;
  public int tinID = 438;
  public int ironID = 440;
  public int coalID = 453;
  public int silverID = 442;
  public int goldID = 444;
  public int mithID = 447;
  public int addyID = 449;
  public int runeID = 451;
  public int bronzeBarID = 2349;
  public int ironBarID = 2351;
  public int steelBarID = 2353;
  public int silverBarID = 2355;
  public int goldBarID = 2357;
  public int mithBarID = 2359;
  public int addyBarID = 2361;
  public int runeBarID = 2363;
  private String barType;
  public String currentStatus;
  private double divider;
  private final RSTile[] tileOneBANK = { 
    new RSTile(3012, 3359, 0), new RSTile(3013, 3356, 0) };

  private final RSTile[] tileOneFURNACE = { 
    new RSTile(3012, 3359, 0) };

  private final RSTile[] tileTwoBANK = { 
    new RSTile(2999, 3365, 0), new RSTile(3009, 3362, 0), 
    new RSTile(3013, 3358, 0), new RSTile(3013, 3356, 0) };

  private final RSTile[] tileTwoFURNACE = { 
    new RSTile(2999, 3365, 0), new RSTile(2988, 3371, 0), 
    new RSTile(2980, 3372, 0), new RSTile(2974, 3369, 0) };

  private final RSTile[] tileThreeBANK = { 
    new RSTile(2987, 3372, 0), new RSTile(2997, 3366, 0), 
    new RSTile(3006, 3362, 0), new RSTile(3013, 3356, 0) };

  private final RSTile[] tileThreeFURNACE = { 
    new RSTile(2987, 3372, 0), new RSTile(2979, 3373, 0), 
    new RSTile(2974, 3369, 0) };

  private final RSTile[] tileFourBANK = { 
    new RSTile(2969, 3379, 0), new RSTile(2985, 3373, 0), 
    new RSTile(2997, 3365, 0), new RSTile(3009, 3362, 0), 
    new RSTile(3013, 3358, 0), new RSTile(3013, 3356, 0) };

  private final RSTile[] tileFourFURNACE = { 
    new RSTile(2969, 3379, 0), new RSTile(2974, 3369, 0) };

  private final RSTile[] tileFiveBANK = { 
    new RSTile(3011, 3347, 0), new RSTile(3013, 3358, 0), 
    new RSTile(3013, 3356, 0) };

  private final RSTile[] tileFiveFURNACE = { 
    new RSTile(3011, 3347, 0), new RSTile(3011, 3358, 0), 
    new RSTile(3005, 3364, 0), new RSTile(2992, 3369, 0), 
    new RSTile(2980, 3373, 0), new RSTile(2974, 3369, 0) };

  private final RSTile[] bankWalk = { 
    new RSTile(2989, 3370, 0), new RSTile(3000, 3363, 0), 
    new RSTile(3013, 3358, 0), new RSTile(3013, 3355, 0) };

  private final RSTile[] furnaceWalk = { 
    new RSTile(3005, 3364, 0), new RSTile(2992, 3369, 0), 
    new RSTile(2980, 3373, 0), new RSTile(2974, 3369, 0) };

  public void passArguments(HashMap<String, String> m)
  {
    this.barType = ((String)m.get("Bar"));
  }

  public boolean inBank()
  {
    if ((this.inBank.distanceTo(Player.getPosition()) < 3) || (new RSTile(3013, 3355, 0) == Player.getPosition())) {
      return true;
    }
    return false;
  }
  public boolean inFurnace() {
    if ((this.inFurnace.distanceTo(Player.getPosition()) < 5) || (new RSTile(2974, 3369, 0) == Player.getPosition())) {
      return true;
    }
    return false;
  }

  private void loop()
  {
    while (true)
    {
      if (inBank()) {
        doBank();
      }
      if (inFurnace()) {
        smeltOre();
      }

      if (Player.getAnimation() == -1) {
        if (this.tileOne.isOnScreen()) {
          if (Inventory.getCount(new int[] { this.oreID1 }) == 0) {
            Walking.walkPath(this.tileOneBANK);
            doBank();
          }
          if (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
            Walking.walkPath(this.tileOneFURNACE);
            smeltOre();
          }
        }
        if (this.tileTwo.isOnScreen()) {
          if (Inventory.getCount(new int[] { this.oreID1 }) == 0) {
            Walking.walkPath(this.tileTwoBANK);
            doBank();
          }
          if (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
            Walking.walkPath(this.tileTwoFURNACE);
            smeltOre();
          }
        }
        if (this.tileThree.isOnScreen()) {
          if (Inventory.getCount(new int[] { this.oreID1 }) == 0) {
            Walking.walkPath(this.tileThreeBANK);
            doBank();
          }
          if (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
            Walking.walkPath(this.tileThreeFURNACE);
            smeltOre();
          }
        }
        if (this.tileFour.isOnScreen()) {
          if (Inventory.getCount(new int[] { this.oreID1 }) == 0) {
            Walking.walkPath(this.tileFourBANK);
            doBank();
          }
          if (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
            Walking.walkPath(this.tileFourFURNACE);
            smeltOre();
          }
        }
        if (this.tileFive.isOnScreen()) {
          if (Inventory.getCount(new int[] { this.oreID1 }) == 0) {
            Walking.walkPath(this.tileFiveBANK);
            doBank();
          }
          if (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
            Walking.walkPath(this.tileFiveFURNACE);
            smeltOre();
          }
        }
      }
    }
  }

  private void smeltOre()
  {
    RSObject FURNACE = getNearest(11666);
    sleep(2000L);
    if (!FURNACE.isOnScreen()) {
      Walking.walkTo(new RSTile(2974, 3370, 0));
      sleep(1500L);
    }
    sleep(this.i2);
    this.currentStatus = "Smelting ore";
    sleep(6500L);
    Camera.turnToTile(FURNACE.getPosition());
    if (!FURNACE.isOnScreen()) {
      Walking.walkTo(new RSTile(2974, 3370, 0));
      sleep(1500L);
    }
    RSObject FURNACE2 = getNearest(11666);
    FURNACE2.click(new String[] { "Smelt" });
    sleep(3000L);
    getBarsSmelted();
    sleep(650L);
    if (Player.getAnimation() != 899) {
      while (Inventory.getCount(new int[] { this.oreID1 }) == this.oreID1Count) {
        RSObject FURNACE4 = getNearest(11666);
        FURNACE4.click(new String[] { "Smelt" });
        sleep(3000L);
        getBarsSmelted();
        sleep(1000L);
      }
    }
    sleep(6500L);
    if (Inventory.getCount(new int[] { this.oreID1 }) != this.oreID1Count) {
      sleep(Inventory.getCount(new int[] { this.oreID1 }) * 3100);
    }
    if (smeltCheck()) {
      while (Inventory.getCount(new int[] { this.oreID1 }) == this.oreID1Count) {
        RSObject FURNACE1 = getNearest(11666);
        FURNACE1.click(new String[] { "Smelt" });
        sleep(3000L);
        getBarsSmelted();
        sleep(1000L);
      }
      while (Inventory.getCount(new int[] { this.oreID1 }) != 0) {
        sleep(1000L);
      }
    }
    totalSmelt();
    this.currentStatus = "Walking to Bank";
    Camera.setCameraAngle(100);
    Camera.setCameraRotation(0);
    Walking.walkPath(this.bankWalk);
    sleep(6000L);
    Walking.walkTo(new RSTile(3013, 3356, 0));
  }

  private void totalSmelt()
  {
    if (Inventory.getCount(new int[] { this.oreID1 }) > 0) {
      this.currentStatus = "Smelting interrupted, smelting again";
      RSObject FURNACE1 = getNearest(11666);
      FURNACE1.click(new String[] { "Smelt" });
      sleep(3000L);
      getBarsSmelted();
      sleep(Inventory.getCount(new int[] { this.oreID1 }) * 3100);
    }
  }

  public boolean smeltCheck()
  {
    if (Inventory.getCount(new int[] { this.oreID1 }) == this.oreID1Count) {
      return true;
    }
    return false;
  }

  private void doBank() {
    RSObject BANK = getNearest(11758);
    this.currentStatus = "Using Bank";
    if (!BANK.isOnScreen()) {
      Walking.walkTo(new RSTile(3013, 3356, 0));
      sleep(4000L);
    }
    while (!Banking.isBankScreenOpen()) {
      Banking.openBankBooth();
    }
    Banking.depositAll();
    if (Inventory.getAll().length != 0) {
      while (Inventory.getAll().length != 0) {
        Banking.depositAll();
      }
    }
    if (Banking.find(new int[] { this.oreID1 }).length == 0) {
      this.currentStatus = "Out of ore, shutting down...";
      System.exit(0);
    }
    if ((Banking.find(new int[] { this.oreID2 }).length == 0) && (this.oreID2 != 99999)) {
      this.currentStatus = "Out of coal ore, shutting down...";
      System.exit(0);
    }
    if ((Banking.find(new int[] { 2568 }).length == 0) && (this.barType.equals("IronROF"))) {
      this.currentStatus = "No rings, shutting down...";
      System.exit(0);
    }
    sleep(this.i2);
    if (this.barType.equals("IronROF")) {
      withdrawRof();
    }
    getOres();
    sleep(1000L);
    if ((Inventory.getCount(new int[] { this.oreID1 }) != this.oreID1Count) && (this.oreID2 != 99999)) if (Inventory.getCount(new int[] { this.oreID2 }) != this.ore2Count) {
        do {
          Banking.depositAll();
          getOres();
          sleep(1000L);

          if (Inventory.getCount(new int[] { this.oreID1 }) == this.oreID1Count) break;  } while (Inventory.getCount(new int[] { this.oreID2 }) != this.ore2Count);
      }


    if ((Inventory.getCount(new int[] { this.oreID1 }) != this.oreID1Count) && (this.oreID2 == 99999) && (!this.barType.equals("IronROF"))) {
      while (Inventory.getCount(new int[] { this.oreID1 }) != this.oreID1Count) {
        Banking.depositAll();
        getOres();
        sleep(1000L);
      }
    }
    if ((Inventory.getCount(new int[] { this.oreID1 }) != this.oreID1Count) && (this.oreID2 == 99999) && (this.barType.equals("IronROF"))) {
      while (Inventory.getCount(
        new int[] { this.oreID1 }) < 27) {
        Banking.depositAll();
        if (Banking.find(new int[] { 2568 }).length != 0) {
          withdrawRof();
        }
        getOres();
        sleep(1000L);
      }
    }
    Banking.close();
    sleep(this.i2);
    if (this.barType.equals("IronROF")) {
      equipRof();
    }
    equipRof();
    sleep(this.i2);
    this.currentStatus = "Walking to Furnace";
    Walking.walkPath(this.furnaceWalk);
    sleep(this.i2);
    Walking.walkTo(new RSTile(2973, 3371, 0));
  }

  private void getOres()
  {
    Banking.withdraw(this.oreID1Count, new int[] { this.oreID1 });
    sleep(this.i2);
    if (this.oreID2 != 99999) {
      Banking.withdraw(this.ore2Count, new int[] { this.oreID2 });
      sleep(this.i2);
    }
  }

  private void getIDs()
  {
    if (this.barType.equals("Bronze")) {
      this.oreID1 = this.tinID;
      this.oreID2 = this.copperID;
      this.oreID1Count = 14;
      this.ore2Count = 14;
      this.divider = 6.5D;
    }

    if ((this.barType.equals("Iron")) || (this.barType.equals("IronROF"))) {
      this.oreID1 = this.ironID;
      this.oreID2 = 99999;
      this.oreID1Count = 28;
      this.ore2Count = 99999;
      this.divider = 12.5D;
    }
    if (this.barType.equals("Steel")) {
      this.oreID1 = this.ironID;
      this.oreID2 = this.coalID;
      this.oreID1Count = 9;
      this.ore2Count = 18;
      this.divider = 17.5D;
    }
    if (this.barType.equals("Silver")) {
      this.oreID1 = this.silverID;
      this.oreID2 = 99999;
      this.oreID1Count = 28;
      this.ore2Count = 99999;
      this.divider = 13.5D;
    }
    if (this.barType.equals("Gold")) {
      this.oreID1 = this.goldID;
      this.oreID2 = 99999;
      this.oreID1Count = 28;
      this.ore2Count = 99999;
      this.divider = 22.5D;
    }
    if (this.barType.equals("Mithril")) {
      this.oreID1 = this.mithID;
      this.oreID2 = this.coalID;
      this.oreID1Count = 5;
      this.ore2Count = 20;
      this.divider = 30.0D;
    }
    if (this.barType.equals("Adamant")) {
      this.oreID1 = this.addyID;
      this.oreID2 = this.coalID;
      this.oreID1Count = 4;
      this.ore2Count = 24;
      this.divider = 37.5D;
    }
    if (this.barType.equals("Rune")) {
      this.oreID1 = this.runeID;
      this.oreID2 = this.coalID;
      this.oreID1Count = 3;
      this.ore2Count = 24;
      this.divider = 50.0D;
    }
  }

  public void run()
  {
    getIDs();
    onStart();
    loop();
  }

  public void onStart() {
    this.currentStatus = "Starting Up";
    Mouse.setSpeed(140);
    Walking.control_click = true;
    this.initialXP = Skills.getXP("SMITHING");
    this.startTime = System.currentTimeMillis();
    Camera.setCameraAngle(100);
    Camera.setCameraRotation(0);
    Walking.walkTo(new RSTile(3013, 3356, 0));
  }

  public RSObject getNearest(int ID)
  {
    RSObject[] obj = Objects.find(104, new int[] { ID });
    if (obj == null)
      return null;
    if (obj.length < 1)
      return null;
    return obj[0];
  }

  private boolean equipRof()
  {
    RSItem[] pess = Inventory.find(new int[] { 2568 });
    if ((pess != null) && (pess.length > 0)) {
      return pess[0].click(new String[0]);
    }
    return false;
  }

  private boolean withdrawRof() {
    RSItem[] ROF = Banking.find(new int[] { 2568 });
    if ((ROF != null) && (ROF.length > 0)) {
      return ROF[0].click(new String[] { "withdraw 1" });
    }
    return false;
  }

  private void getBarsSmelted()
  {
    if (this.barType.equals("Bronze")) {
      Mouse.clickBox(37, 399, 67, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt X" });
      sleep(650L);
      Keyboard.typeSend("14");
    }
    else if ((this.barType.equals("Iron")) || (this.barType.equals("IronROF"))) {
      Mouse.clickBox(140, 399, 170, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt X" });
      sleep(650L);
      Keyboard.typeSend("28");
    }
    else if (this.barType.equals("Steel")) {
      Mouse.clickBox(240, 399, 270, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt 10" });
    }
    else if (this.barType.equals("Silver")) {
      Mouse.clickBox(190, 399, 220, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt X" });
      sleep(650L);
      Keyboard.typeSend("28");
    }
    else if (this.barType.equals("Gold")) {
      Mouse.clickBox(295, 399, 325, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt X" });
      sleep(650L);
      Keyboard.typeSend("28");
    }
    else if (this.barType.equals("Mithril")) {
      Mouse.clickBox(350, 399, 375, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt 5" });
    }
    else if (this.barType.equals("Adamant")) {
      Mouse.clickBox(400, 399, 430, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt 5" });
    }
    else if (this.barType.equals("Rune")) {
      Mouse.clickBox(450, 399, 480, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt 5" });
    }
    else if (this.barType.equals("IronROF")) {
      Mouse.clickBox(140, 399, 170, 418, 3);
      ChooseOption.select(new String[] { 
        "Smelt X" });
      sleep(650L);
      Keyboard.typeSend("28");
    }
  }

  public void onPaint(Graphics g)
  {
    long timeRan = System.currentTimeMillis() - this.startTime;
    int xpGained = Skills.getXP("SMITHING") - this.initialXP;
    double multiplier = timeRan / 3600000.0D;
    int xpPerHour = (int)(xpGained / multiplier);
    int bsMade = (int)(xpGained / this.divider);
    int bsPerHour = (int)(bsMade / multiplier);

    g.setColor(Color.cyan);
    g.drawString("Peon Falador Smelter", 550, 285);
    g.drawString("Smelting: " + this.barType, 550, 305);
    g.drawString("Status: " + this.currentStatus, 550, 325);
    g.drawString("Running for: " + Timing.msToString(timeRan), 550, 345);

    g.drawString("Xp gained: " + xpGained + " (" + xpPerHour + "/h)", 550, 365);
    g.drawString("Bars made: " + bsMade + " (" + bsPerHour + "/h)", 550, 385);
    g.drawString("Current level: " + Skills.getCurrentLevel("SMITHING"), 550, 405);
    g.drawString("Percent to next level: " + Skills.getPercentToNextLevel("SMITHING") + "%", 550, 425);
    g.setColor(Color.orange);
    g.drawString("-Exxos", 670, 445);
  }

  public String runTime(long i)
  {
    DecimalFormat nf = new DecimalFormat("00");
    long millis = System.currentTimeMillis() - i;
    long hours = millis / 3600000L;
    millis -= hours * 3600000L;
    long minutes = millis / 60000L;
    millis -= minutes * 60000L;
    long seconds = millis / 1000L;
    return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
  }
}