package scripts;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;

public class MooseEssence extends Script

implements ActionListener, Painting, Ending
{

	private boolean running;
	private final boolean debug = false;
	private boolean paintEnabled;
	private final int ESSENCE_ID = 23929;
	private int PORTAL_ID = 23930;
	private final int AUBURY_ID = 3359;
	private final int RUNE_ESSENCE = 1436;
	private final int PURE_ESSENCE = 7936;
	private final int CLOSED_DOOR_ID = 24381;
	private final int BROKEN_PICK_ID = 466;
	private final RSTile AT_ESSENCE = new RSTile(12472, 10487);
	private final RSTile IN_RUNE_SHOP = new RSTile(3253, 3401);
	private final RSTile IN_BANK = new RSTile(3253, 3420);
	private final RSTile IN_ABYSS = new RSTile(2338, 4747);
	private final int MINING_ANIMATIONS[] = {
			624, 628, 629, 627, 626, 625
	};
	private final int PICK_HEADS[] = {
			480, 482, 484, 486, 488, 490
	};
	private final int PICKAXE_IDS[] = {
			1265, 1267, 1269, 1271, 1273, 1275
	};
	private int essenceMined;
	private Image proggyImage;
	private long startTime;
	private int antiBanFreq;
	private final RSTile IN_THE_MIDDLE = new RSTile(3256, 3408);
	private String emailAddress;
	private JFrame jf;
	private int uploadAttempts;
	char ctrl;
	private Object lastText;
	private final RSTile randomAubury[] = {
			new RSTile(3255, 3401), new RSTile(3254, 3401), new RSTile(3253, 3401), new RSTile(3252, 3401), new RSTile(3252, 3400), new RSTile(3253, 3400), new RSTile(3252, 3399), new RSTile(3253, 3399), new RSTile(3254, 3402), new RSTile(3253, 3402), 
			new RSTile(3252, 3402), new RSTile(3252, 3403), new RSTile(3253, 3403)
	};
	private JLabel label3;
	private JLabel label4;
	private JLabel label1;
	private JSlider slider1;
	private JLabel label2;
	private JTextField textField1;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JButton button1;

	public MooseEssence()
	{
		running = false;
		paintEnabled = true;
		essenceMined = 0;
		startTime = 0L;
		antiBanFreq = 40;
		emailAddress = null;
		uploadAttempts = 0;
		ctrl = '\021';
	}


	public void run()
	{
		running = true;
		onStart();
		while(running) 
		{
			Walking.control_click = true;
			Camera.setCameraAngle(100);
			Camera.setCameraRotation(0);
			try
			{
				loop();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean inMine()
	{
		if(AT_ESSENCE.distanceTo(Player.getPosition()) < 150 || Objects.find(20 ,ESSENCE_ID).length > 0)
			return true;
		return false;
	}

	public boolean inAbyss()
	{
		return IN_ABYSS.distanceTo(Player.getPosition()) < 2;
	}

	public boolean isLost()
	{
		return IN_THE_MIDDLE.distanceTo(Player.getPosition()) > 30 && !inMine() && !inAbyss();
	}

	public void doDoor()
	{
		RSObject fagDoor = getNearest(24381);
		if(fagDoor != null && !isMoving() && fagDoor.getPosition().getX() == 3253 && fagDoor.getPosition().getY() == 3398 && fagDoor.getPosition().distanceTo(Player.getPosition()) < 6)
		{
			if(fagDoor.isOnScreen())
			{
				customLog("Opening door cuz kids r mad");
				fagDoor.click("Open", new Point(-10, 10), new Point(0, -40));
				sleep(300, 700);
			} else
			{
				customWalkTo(fagDoor.getPosition());
			}
		}
	}

	public void homeTeleport()
	{
	}

	public boolean hasPickaxeHead()
	{
		return Inventory.getCount(PICK_HEADS) > 0;
	}

	public boolean hasPickaxe()
	{
		if(Inventory.getCount(PICKAXE_IDS) > 0)
		{
			return true;
		}
		RSItem equipment[] = getEquipment();
		for(int j = 0; j < PICKAXE_IDS.length; j++)
		{
			for(int i = 0; i < equipment.length; i++)
			{
				if(equipment[i].getID() == PICKAXE_IDS[j])
				{
					return true;
				}
			}

		}

		return false;
	}

	public void putPickaxeTogether()
	{
		if(Inventory.getCount(new int[] {
				466
		}) < 1)
		{
			unequipItem(466);
		} else
		{
			RSItem brokenPicks[] = Inventory.find(new int[] {
					466
			});
			RSItem brokenPick = null;
			RSItem pickaxes[] = Inventory.find(PICK_HEADS);
			RSItem pickaxe = null;
			if(brokenPicks.length < 1 || pickaxes.length < 1)
			{
				return;
			}
			brokenPick = brokenPicks[0];
			pickaxe = pickaxes[0];
			brokenPick.click(null);
			pickaxe.click(null);
		}
	}

	public boolean pickupPickHead()
	{
		customLog("Finding pickaxe head");
		RSGroundItem pickHeads[] = GroundItems.findNearest(PICK_HEADS);
		RSGroundItem pickHead = null;
		if(pickHeads.length < 1)
		{
			return false;
		}
		pickHead = pickHeads[0];
		if(pickHead != null)
		{
			if(pickHead.isOnScreen())
			{
				pickHead.click(new String[] {
						"Take"
				});
			} else
			{
				customWalkTo(pickHead.getPosition());
			}
			return true;
		} else
		{
			return false;
		}
	}

	public void attemptAbyssSolve()
	{
		customLog("In the abyss..");
		int random = General.random(12722, 12725);
		RSObject appendage = getNearest(random);
		if(appendage == null)
		{
			return;
		}
		if(appendage.isOnScreen())
		{
			if(appendage.click(new String[] {
					"Operate"
			}))
			{
				sleep(2000, 3000);
				customLog("Guessing on appendage..");
				return;
			}
			Camera.turnToTile(appendage.getPosition());
			sleep(300, 750);
			customLog("Turning camera to see appendage");
		}
	}

	public void goDownstairs()
	{
		if(IN_BANK.distanceTo(Player.getPosition()) < 6)
		{
			return;
		}
		RSObject stairs = getNearest(24353);
		if(stairs == null)
		{
			return;
		}
		if(stairs.isOnScreen())
		{
			if(stairs.click(new String[] {
					"Climb-down"
			}))
			{
				sleep(2000, 4000);
				customLog("Attempting to go back downstairs");
				return;
			}
			Camera.turnToTile(stairs.getPosition());
			sleep(300, 750);
			customLog("Finding stairs..");
		}
	}

	public boolean isMining()
	{
		for(int i = 0; i < MINING_ANIMATIONS.length; i++)
		{
			if(Player.getAnimation() == MINING_ANIMATIONS[i])
			{
				return true;
			}
		}

		return false;
	}

	public void onEnd()
	{
		println("Moose's essence miner is shutting down- Generating proggy!");
		println((new StringBuilder("Ran for ")).append(runTime(startTime)).toString());
		println((new StringBuilder("Mined ")).append(essenceMined).append(" essences").toString());
	}

	public void uploadProggy(String s, long l, int i)
	{
	}

	public RSObject getNearest(int ID)
	{
		RSObject obj[] = Objects.findNearest(104, new int[] {
				ID
		});
		if(obj.length < 1)
		{
			return null;
		} else
		{
			return obj[0];
		}
	}

	public boolean mineEssence()
	{
		RSObject ess = getNearest(ESSENCE_ID);
		if(ess == null)
		{
			return false;
		}
		if(ess.isOnScreen())
		{
			if(isMining())
			{
				sleep(2000L);
				return true;
			}
			if(!isMining())
			{
				sleep(1500L);
				if(isMining())
				{
					return true;
				}
			}
			int fails = 0;
			while(!DynamicClicking.clickRSObject(ess, "Mine Rune Essence") && !isMining()) 
			{
				if(++fails == 5)
				{
					Walking.walkTo(ess.getPosition());
					Walking.walking_timeout = 500;
					if(General.random(1, 3) == 3)
					{
						Camera.setCameraRotation(General.random(10, 360));
					}
					fails = 0;
				}
			}
			customLog("Mining");
			sleep(600L);
			for(; isMoving(); sleep(1000L)) { }
			return true;
		}
		if(!inMine())
		{
			customLog("We're not in Kansas anymore todo~!");
		}
		walkToEssence();
		return false;
	}

	public void walkToEssence()
	{
		RSObject ess = getNearest(ESSENCE_ID);
		if(ess != null && ess.getPosition().distanceTo(Player.getPosition()) < 30 && !isMoving() && !isMining())
		{
			customWalkTo(ess.getPosition());
			return;
		}
		RSTile t[] = new RSTile[4];
		t[0] = new RSTile(2919, 4840);
		t[1] = new RSTile(2902, 4841);
		t[2] = new RSTile(2902, 4822);
		t[3] = new RSTile(2919, 4822);
		RSTile nearest = getNearest(t);
		if(nearest.distanceTo(Player.getPosition()) < 30 && !isMoving())
		{
			customWalkTo(nearest);
			sleep(500, 1000);
		}
	}

	public static RSTile getNearest(RSTile t[])
	{
		RSTile nearest = null;
		double distance = 9999999D;
		RSTile arstile[];
		int j = (arstile = t).length;
		for(int i = 0; i < j; i++)
		{
			RSTile object = arstile[i];
			if((double)object.distanceTo(Player.getPosition()) < distance)
			{
				nearest = object;
				distance = object.distanceTo(Player.getPosition());
			}
		}

		return nearest;
	}

	public boolean isMoving()
	{
		return Player.isMoving();
	}
	private void camera(RSObject cam){
		RSObject target = cam;	
		Camera.turnToTile(target.getPosition());
	}
	public boolean usePortal()
	{

		RSObject portal = getNearest(26254);
		
		if(portal == null)
		{
			customLog("Portal not found");
			return false;
		}

		println("Portal Found");
		camera(portal);

		Walking.walkTo(portal.getPosition());
		if(portal.isOnScreen())
		{
			println("Portal is on screen");
			if(DynamicClicking.clickRSObject(portal, "Use"))
			{
				customLog("Teleported.");
				return true;
			}
			if(portal.click("Use"))
			{
				customLog("Teleported.");
				return true;
			}

		} else
			if(portal.getPosition().distanceTo(Player.getPosition()) < 20 && !isMoving())
			{
				customWalkTo(portal.getPosition());
			}
		portal = getNearest(23930);
		
		if(portal == null)
		{
			customLog("Portal not found");
			return false;
		}

		println("Portal Found");
		camera(portal);

		Walking.walkTo(portal.getPosition());
		if(portal.isOnScreen())
		{
			println("Portal is on screen");
			if(DynamicClicking.clickRSObject(portal, "Use"))
			{
				customLog("Teleported.");
				return true;
			}
			if(portal.click("Use"))
			{
				customLog("Teleported.");
				return true;
			}

		} else
			if(portal.getPosition().distanceTo(Player.getPosition()) < 20 && !isMoving())
			{
				customWalkTo(portal.getPosition());
			}
		return false;
	}

	public boolean inRuneShop()
	{
		for(int i = 0; i < randomAubury.length; i++)
		{
			if(Player.getPosition().equals(randomAubury[i]))
			{
				return true;
			}
		}

		return false;
	}

	public boolean inBank()
	{
		return IN_BANK.distanceTo(Player.getPosition()) < 5;
	}

	public static Image getImage(String url)
	{
		Image img = null;
		try
		{
			URL link = new URL(url);
			img = ImageIO.read(link);
		}
		catch(Exception exception) { }
		return img;
	}

	public boolean openBank()
	{
		RSObject [] banks = Objects.find(10, 23965);
		if(banks != null)
			try{banks[0].click("Bank");
			}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
		return true;
	}

	public boolean doBank()
	{
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		if(!GameTab.getOpen().equals(org.tribot.api2007.GameTab.TABS.INVENTORY))
		{
			return false;
		}
		if(openBank())
		{
			Banking.depositAllExcept(PICKAXE_IDS);
			return true;
		} else
		{
			return false;
		}
	}

	public boolean teleportIntoMine()
	{
		RSNPC jim[] = NPCs.find(new int[] {
				AUBURY_ID
		});
		Mouse.setSpeed(2000);
		if(!jim[0].isOnScreen())
		{
			RSNPC target = jim[0];
			Camera.turnToTile(target.getPosition());
			Walking.walkTo(jim[0].getPosition());
			sleep(500L);
		}
		jim[0].click(new String[] {
				"Teleport"
		});
		return true;
	}

	public void loop()
			throws Exception
			{
		sleep(100L);
		if(isLost() && PathFinding.canReach(IN_BANK, false))
		{
			PathFinding.aStarWalk(IN_BANK);
		}
		if(inAbyss())
		{
			attemptAbyssSolve();
		}
		goDownstairs();
		if(inMine() && Inventory.isFull())
		{
			println("Searching for portal..");
			sleep(5000);
			usePortal();
			customLog("Searching Done");
		}
		if(!inMine() && !inBank() && Inventory.isFull())
		{
			if(IN_BANK.distanceTo(Player.getPosition()) < 60 && !isMoving())
			{
				doDoor();
				customWalkTo(IN_BANK);
				sleep(400, 600);
			}
			customLog("Walking to the bank");
		} else
			if(!inMine() && !inRuneShop() && !Inventory.isFull())
			{
				if(IN_RUNE_SHOP.distanceTo(Player.getPosition()) < 60 && !isMoving())
				{
					doDoor();
					Walking.blindWalkTo(randomAubury[General.random(0, randomAubury.length - 1)]);
					sleep(400, 700);
				}
				customLog("Walking to the rune shop");
			}
		if(inBank() && Inventory.isFull())
		{
			int essThisRun = Inventory.getCount(new int[] {
					7936
			}) + Inventory.getCount(new int[] {
					1436
			});
			if(doBank())
			{
				essenceMined += essThisRun;
			}
			customLog("Banking.");
		}
		if(inRuneShop() && !Inventory.isFull() && !teleportIntoMine())
		{
			customLog("Teleporting via Aubury..");
		}
		if(isMining())
		{
			antiban();
		}
		if(inMine() && !Inventory.isFull())
		{
			if(!hasPickaxe())
			{
				if(!hasPickaxeHead())
				{
					pickupPickHead();
					sleep(700, 1700);
				} else
					if(hasPickaxeHead())
					{
						putPickaxeTogether();
						sleep(700, 1700);
					} else
					{
						println("We completely lost our pickaxe.. Sorry.");
					}
				return;
			}
			if(Player.getRSPlayer().isInCombat())
			{
				usePortal();
				return;
			}
			if(!mineEssence())
			{
				customLog("Searching for essence deposit..");
			}
		}
		if(Login.getLoginState() != org.tribot.api2007.Login.STATE.INGAME || jf != null)
		{
			customLog("Not logged in");
		}
			}

	public boolean onStart()
	{
		startTime = System.currentTimeMillis();
		proggyImage = getImage("http://i.imgur.com/NBRkT7v.png");
		Mouse.setSpeed(140);
		customLog("MooseEssence starting up.. Report bugs on the thread!!");
		return true;
	}

	public void customLog(Object text)
	{
		lastText = text;
	}

	public void customWalkTo(RSTile t)
	{
		Walking.control_click = true;
		t = new RSTile(t.getX() + General.random(-1, 1), t.getY() + General.random(-1, 1));
		if(t.distanceTo(Player.getPosition()) < 6 && General.random(1, 2) == 1)
		{
			Walking.walkScreenPath(Walking.generateStraightPath(t));
		} else
		{
			Walking.walkPath(Walking.generateStraightPath(t));
		}
		sleep(50, 200);
		if(Objects.getAt(t) != null && General.random(1, 2) == 1)
		{
			Camera.setCameraRotation(General.random(0, 360));
		}
		sleep(200, 400);
	}

	public void antiban()
	{
		int random = General.random(1, antiBanFreq * 2);
		String antibanString = null;
		switch(random)
		{
		case 1: // '\001'
			antibanString = "Antiban - Camera Pitch";
			Camera.setCameraRotation(General.random(200, 300));
			break;

		case 2: // '\002'
			antibanString = "Antiban - Camera Angle";
			Camera.setCameraAngle(General.random(10, 360));
			break;

		case 3: // '\003'
			antibanString = "Antiban - Mouse movement";
			Mouse.move(General.random(1, 200), General.random(1, 200));
			break;

		case 4: // '\004'
			antibanString = "Antiban - Stat Check";
			Point p = new Point(562 + General.random(0, 162), 216 + General.random(0, 200));
			GameTab.open(org.tribot.api2007.GameTab.TABS.STATS);
			sleep(200, 500);
			Mouse.move(p);
			sleep(1000, 6000);
			GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
			break;

		case 5: // '\005'
			antibanString = "Antiban - View player";
			Camera.turnToTile(Player.getPosition());
			break;

		case 6: // '\006'
			RSPlayer players[] = Players.getAll();
			if(players.length >= 1)
			{
				players[General.random(0, players.length - 1)].hover();
				antibanString = "Viewing another player";
			}
			break;

		case 7: // '\007'
			GameTab.open(org.tribot.api2007.GameTab.TABS.values()[General.random(0, org.tribot.api2007.GameTab.TABS.values().length - 1)]);
			antibanString = "Opening a random tab";
			break;

		default:
			antibanString = null;
			break;
		}
		if(antibanString != null)
		{
			customLog(antibanString);
		}
	}

	public void onPaint(Graphics g1)
	{
		if(!paintEnabled)
		{
			return;
		}
		Graphics2D g = (Graphics2D)g1;
		if(proggyImage != null)
		{
			g.drawImage(proggyImage, 6, 344, null);
			if(lastText != null)
			{
				g.drawString(lastText.toString(), 182, 402);
			}
			g.drawString((new StringBuilder()).append(essenceMined).toString(), 251, 446);
			g.drawString((new StringBuilder()).append(runTime(startTime)).toString(), 195, 424);
		} else
		{
			g.drawString("Moose's essence miner", 10, 295);
			if(lastText != null)
			{
				g.drawString((new StringBuilder("Status: ")).append(lastText.toString()).toString(), 10, 310);
			}
			g.drawString((new StringBuilder("Essences mined: ")).append(essenceMined).toString(), 10, 320);
			g.drawString((new StringBuilder("Time running: ")).append(runTime(startTime)).toString(), 10, 330);
		}
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

	private void initComponents()
	{
		jf = new JFrame();
		jf.setVisible(true);
		label3 = new JLabel();
		label4 = new JLabel();
		label1 = new JLabel();
		slider1 = new JSlider(1, 10);
		slider1.setInverted(true);
		label2 = new JLabel();
		textField1 = new JTextField();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		button1 = new JButton();
		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new GridLayout(10, 1));
		label3.setText("---------- Moose's essence miner settings ----------");
		contentPane.add(label3);
		contentPane.add(label4);
		label1.setText("Antiban Frequency(1-10)");
		contentPane.add(label1);
		contentPane.add(slider1);
		label2.setText("E-mail address(Can be blank):");
		contentPane.add(label2);
		contentPane.add(textField1);
		radioButton1.setText("Send e-mail on script terminate");
		contentPane.add(radioButton1);
		radioButton2.setText("Hide paint");
		contentPane.add(radioButton2);
		button1.setText("Start");
		contentPane.add(button1);
		jf.pack();
		jf.setLocationRelativeTo(jf.getOwner());
		button1.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(radioButton1.isSelected())
		{
			emailAddress = textField1.getText();
		}
		paintEnabled = !radioButton2.isSelected();
		antiBanFreq = slider1.getValue();
		jf.dispose();
		jf = null;
	}

	private RSItem[] getEquipment()
	{
		RSInterfaceChild equip = Interfaces.get(387, 28);
		RSItem items[] = null;
		if(equip != null)
		{
			items = equip.getItems();
		}
		return items;
	}

	public boolean unequipItem(int itemID)
	{
		GameTab.open(org.tribot.api2007.GameTab.TABS.EQUIPMENT);
		RSItem items[] = getEquipment();
		RSItem arsitem[];
		int j = (arsitem = items).length;
		for(int i = 0; i < j; i++)
		{
			RSItem item = arsitem[i];
			if(item.getID() == itemID)
			{
				item.click(new String[] {
						"Remove"
				});
				return true;
			}
		}

		return false;
	}
}
