
package scripts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.DGNatureRunner.NewJFrame;
import scripts.DGNatureRunner.STATE;


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Nature Crafter 2.0", description = "Buy and Sell Ess and make Nat Rune \n 2.0 Rework the whole scrip /n" +
		"2.1 Fixed Getting Stuck at Atlar\n" +
		"2.2 fixed Walking path " +
		"2.3 Fix ID and Walkpath " +
		"2.4 Fix Exiting Portal " +
		"Drop everything Except Gold,Note ess, Ess, and nature rune" +
		"2.5 Add AntiLure" +
		"2.6 Hop World When a spider is near" +
		"2.7 Fix Walk Path" +
		"2.8 Fix buying method" +
		"2.9 Update to use Name instead of ID",version = 2.6)


public class DGNatureCrafter extends Script implements Painting {


	//------------Object----------------
	NewJFrame g1 = new NewJFrame();

	private static int MOUSESPEED = 0;
	private static boolean USESHORTCUT = false;
	private  int ID_ROCK = 19851; //o
	private  int ID_ALTAR = 13583;
	private  int ID_PORTAL = 12661;
	private final int ID_NESS = 7937;
	private final int ID_ESS = 7936;
	private  int NPC = 2255;
	int x1,y1,x2,y2;
	private int [] keep = {995,7937,561,7936};
	char ctrl = KeyEvent.VK_CONTROL;
	private STATE SS = getState();
	private int amount = Inventory.getCount(561);
	String sa;
	Random randomGenerator = new Random();
	private final long startTime = System.currentTimeMillis();
	private int START_XP = Skills.getXP("Runecrafting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
	private int lvl = Skills.getCurrentLevel("Runecrafting");
	//------------location--------------
	final RSTile ATOS[] ={
			new RSTile(2865, 3024, 0), new RSTile(2861, 3028, 0), new RSTile(2858, 3034, 0), new RSTile(2854, 3041, 0), new RSTile(2845, 3048, 0), new RSTile(2833, 3056, 0),
			new RSTile(2828, 3063, 0), new RSTile(2830, 3066, 0), new RSTile(2831, 3074, 0), new RSTile(2830, 3081, 0), new RSTile(2830, 3087, 0), new RSTile(2826, 3098, 0),
			new RSTile(2822, 3107, 0), new RSTile(2819, 3114, 0), new RSTile(2813, 3123, 0), new RSTile(2803, 3128, 0), new RSTile(2793, 3127, 0), new RSTile(2785, 3133, 0),
			new RSTile(2775, 3132, 0) };
	final RSTile STOA[] = {
			new RSTile(2769, 3124, 0), new RSTile(2772, 3128, 0), new RSTile(2775, 3133, 0), new RSTile(2780, 3133, 0), new RSTile(2785, 3133, 0), new RSTile(2789, 3130, 0), new RSTile(2800, 3130, 0), new RSTile(2809, 3122, 0), new RSTile(2815, 3117, 0), new RSTile(2823, 3111, 0), 
			new RSTile(2827, 3102, 0), new RSTile(2832, 3096, 0), new RSTile(2832, 3088, 0), new RSTile(2830, 3080, 0), new RSTile(2831, 3075, 0), new RSTile(2830, 3072, 0), new RSTile(2829, 3063, 0), new RSTile(2836, 3054, 0), new RSTile(2839, 3051, 0), new RSTile(2847, 3045, 0), 
			new RSTile(2854, 3040, 0), new RSTile(2856, 3035, 0), new RSTile(2858, 3027, 0), new RSTile(2866, 3020, 0)};
	final RSTile STOAs[] = {
			new RSTile(2766, 3120), new RSTile(2772, 3113), new RSTile(2777, 3106), new RSTile(2783, 3101), new RSTile(2788, 3096), new RSTile(2789, 3090), new RSTile(2797, 3087),
			new RSTile(2804, 3082), new RSTile(2812, 3077), new RSTile(2819, 3070), 
			new RSTile(2825, 3064), new RSTile(2832, 3058), new RSTile(2836, 3050), new RSTile(2842, 3047), new RSTile(2849, 3037), new RSTile(2856, 3033), new RSTile(2863, 3027),
			new RSTile(2866, 3021)
	};
	final RSTile ATOSs[] = {
			new RSTile(2865, 3022), new RSTile(2858, 3028), new RSTile(2852, 3034), new RSTile(2845, 3034), new RSTile(2835, 3043), new RSTile(2832, 3052), new RSTile(2829, 3059),
			new RSTile(2825, 3064), new RSTile(2820, 3069), new RSTile(2816, 3072), 
			new RSTile(2810, 3075), new RSTile(2805, 3077), new RSTile(2800, 3082), new RSTile(2796, 3085), new RSTile(2791, 3089), new RSTile(2786, 3097), new RSTile(2778, 3106),
			new RSTile(2772, 3113), new RSTile(2768, 3120)
	};
	//---------------------------------------------------------------
	//START: Code generated using Enfilade's Easel
	private final Color color1 = new Color(255, 255, 255, 90);
	private final Color color2 = new Color(0, 0, 0);
	private final Color color3 = new Color(0, 200, 0);

	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Arial", 0, 9);
	private final Font font2 = new Font("Arial", 2, 15);

	@Override
	public void onPaint(Graphics g) {
		g.setColor(color1);
		g.fillRoundRect(245, 205, 188, 113, 16, 16);
		g.setColor(color2);
		((Graphics2D) g).setStroke(stroke1);
		g.drawRoundRect(245, 205, 188, 113, 16, 16);
		g.setFont(font2);
		g.drawRect(x1, y1, x2, y2);
		g.drawString("DG Nature Runner", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Rune Craft", 254, 275);
		g.drawString("Time", 254, 295);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString(""+ (Inventory.getCount(561)-amount), 310, 275);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 295);
		g.drawString("Nat Per Hour: " + perHour((Inventory.getCount(561)-amount)), 254, 315);
	}
	//END: Code generated using Enfilade's Easel
	//--------------------------------------------------------------------------------------------------------------------------


	boolean checkDate(){
		Date current = new Date();
		//create a date one day before current date
		//create date object
		//compare both dates
		Date prev = new Date(2013,5,29);

		if(prev.before(current)){
			return true;

		} else {
			return false;
		}
	}

	@Override
	public void run() {



		g1.setVisible(true);

		while(g1.isVisible())
			sleep(500);
		if(checkDate())
			stopScript();
		while(true){
			Inventory.dropAllExcept(keep);
			START_XP = Skills.getXP("Runecrafting");
			XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
			lvl = Skills.getCurrentLevel("Runecrafting");
			Mouse.setSpeed(MOUSESPEED);	
			Walking.control_click = true;
			int randomInt = randomGenerator.nextInt(360);

			SS = getState();	
			switch(SS){
			case CHECK:
				break;
			case enterrock:
				sa = "Entering Rock";
				enterrock();
				sleeps();
				break;
			case runes:
				sa = "Crafting Rune";
				runes();
				sleeps();
				break;
			case enterportal:
				sleeps();
				sa = "Entering Portal";
				enterportal();
				sleeps();
				break;
			case trade:
				sa = "Trading NPC";
				store();
				sleep(20,150);
				break;
			case buyandsell:
				while(!haveess() && shopInterfaceOpen()){
					if(storeess()){
						sa = "Buying";
						buy(10);
					}
					else if (Inventory.find(ID_NESS).length== 0)
					{
						println("No Note Ess Found");
						stopScript();
					}
					else{
						if(!Inventory.isFull() && ((!in() && !storeess()) && Inventory.find(ID_NESS).length != 0)) {
							sa = "Selling";
							if(Inventory.find(ID_ESS).length < 25 && Inventory.find(ID_ESS).length >= 20){
								println("Selling 5");
								sell(5);
							}
							else{
								println("Selling 10");
								sell(10);
							}

						}
						if(Inventory.isFull()){
							walktorock();
							break;
						}
						if(in()){
							SS = getState();
							break;
						}

						buy(10);
					}
				}
				sleep(100);

			case walktorock:
				sa = "walking to rock";
				walktorock();
				break;
			case walktostore:
				sa = "walking to store";
				walktostore();
				break;
			case danger:
				sa = "Low on HP, Teleporting";
				danger();
			default:
				break;
			}
		}

	}

	public enum STATE {
		enterrock, CHECK, walktorock, makerune, walktostore, trade, runes, enterportal, buyandsell, danger

	}
	//--------------------GET STATE---------------------------------
	private STATE getState(){
		if(haveess()){
			if(!ataltar() && !atrock()){
				return STATE.walktorock;

			}
			if(atrock()){
				return STATE.enterrock;
			}
			if(ataltar()){
				return STATE.runes;
			}	
		}
		if(!haveess()){
			if(ataltar()){
				return STATE.enterportal;
			}
			if(!atstore()){
				return STATE.walktostore;
			}
			if(atstore()){
				if(shopInterfaceOpen()){
					buy(10);
					return STATE.buyandsell;
				}
				else{
					return STATE.trade;
				}
			}
		}
		if(Skills.getCurrentLevel("CONSTITUTION") < 6){
			return STATE.danger;
		}
		println("CHECK");
		return STATE.CHECK;

	}



	//---------------------AT LOCATION--------------------------------
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
	}
	private boolean in(){
		if(Inventory.find(ID_ESS).length > 25){
			return true;
		}
		return false;
	}
	private boolean storeess(){
		RSItem[] items = Interfaces.get(300 , 75).getItems();
		for(int i = 0; i < items.length; i++)
			if(items[i].getID() == ID_ESS){
				return true;
			}
		return false;
	}
	private boolean haveess(){
		if(Inventory.find(ID_ESS).length < 25){
			return false;
		}
		return true;
	}
	private boolean atrock(){
		return Player.getPosition().distanceTo((new RSTile(2867,3020))) < 5;
	}
	private boolean atstore()
	{
		return NPCs.find("Jiminua").length > 0;
	}
	private boolean ataltar() {
		return Objects.find(40, "Altar").length > 0;
	}
	//-----------------------To Do Method-------------------------
	private boolean danger(){
		RSItem[] dan = Inventory.find(8009);
		if(Skills.getCurrentLevel("CONSTITUTION") < 10){
			dan[0].click("Break");
			return true;
		}
		return false;

	}	
	private boolean enterportal(){
		try{
			RSObject[] portal = Objects.find(15, "Portal");
			if(portal.length > 0)
				if(portal[0] != null)
					if(!portal[0].isOnScreen()){
						camera(portal[0]);
						if(!portal[0].isOnScreen())
							Walking.walkTo(portal[0].getPosition());
					}
			if(DynamicClicking.clickRSObject(portal[0], "Use")){
				sleep(500);
				while(Player.isMoving())
					sleep(50);
			}
			sleeps();
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	private boolean runes(){
		RSObject[] altar = Objects.find(15, "Altar");
		camera(altar[0]);
		if(!altar[0].isOnScreen())
			Walking.walkTo(altar[0].getPosition());
		if(DynamicClicking.clickRSObject(altar[0], "Craft-rune")){
			sleep(500);
			return true;
		}
		return false;
	}
	private void store(){
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		RSNPC[] jim = NPCs.findNearest("Jiminua");
		if(!jim[0].isOnScreen()){
			RSNPC[] spider = NPCs.findNearest("Jungle spider");
			if(spider.length > 0){
				println("Checking Spider");
				if(spider[0].getPosition().distanceTo(Player.getPosition()) < 3)
					RUNAWAY();
			}
			RSNPC target = jim[0];
			Camera.turnToTile(target.getPosition());
			Walking.walkTo(jim[0].getPosition());
			sleeps();
		}
		DynamicClicking.clickRSNPC(jim[0], "Trade");
	}
	public boolean RUNAWAY(){
		Walking.walkTo(new RSTile(2766,3120));
		worldHop();
		return true;
	}
	private void worldHop() {

		final int[] WORLD_COLUMN_1 = {301,302,303,304,305,306,308,309,310,311,312,313,314,316,317,318},
				WORLD_COLUMN_2 = {319,320,321,322,325,326,327,328,329,330,333,334,335,336,337,338},
				WORLD_COLUMN_3 = {341,342,343,344,345,346,349,350,351,352,353,354,357,358,359,360},
				WORLD_COLUMN_4 = {361,362,365,366,367,368,369,370,373,374,375,376,377,378};

		final int[][] WORLD_LIST = {WORLD_COLUMN_1, WORLD_COLUMN_2, WORLD_COLUMN_3, WORLD_COLUMN_4};

		int world = General.random(305, 378);
		final int topX = 240,
				topY = 80,
				X_MULTIPLIER = 95,
				Y_MULTIPLIER = 24;
		int wNum = 0,
				xPos = 0,
				yPos = 0;

		println("Worldhoping");
		super.setLoginBotState(false);
		if (Login.getLoginState() == Login.STATE.INGAME) {
			Login.logout();
			sleep(2000, 3000);
		}
		if (Login.getLoginState() == Login.STATE.LOGINSCREEN) {
			Mouse.clickBox(15, 465, 95, 490, 1); // Clicks Box to Change world
			sleep(3000);
			Mouse.clickBox(394, 9, 400, 12, 1); //clicks players ^
			Mouse.clickBox(628, 9, 635, 12, 1); //clicks type v

			for(int x = 0; x < WORLD_LIST.length; x++){
				for(int y = 0; y < WORLD_LIST[x].length; y++){
					wNum = WORLD_LIST[x][y];

					if (wNum == world) {
						println(world);
						xPos = x * X_MULTIPLIER + topX;
						yPos = y * Y_MULTIPLIER + topY;

						Mouse.click(xPos, yPos, 1);
						sleep(3000);
					}
				}
			}
		}


		super.setLoginBotState(true);
		sleep(1500, 2500);
	}
	public boolean shopInterfaceOpen()
	{
		if(Interfaces.get(300, 91) != null){
			return true;
		}
		return false;
	}


	private boolean sell(int x){
		Mouse.setSpeed(180);	
		RSItem[] ess = Inventory.find(ID_NESS);

		if(storeess())
			buy(10);
		else if(!storeess()){
			if(x == 10){

				ess[0].click("Sell 10");
				sleep(750);
				return true;
			}
			else if(x == 5){
				ess[0].click("Sell 5");
				sleep(1000);
				return true;
			}
		}
		return false;
	}

	private boolean buy(int x){
		while(storeess() && !Inventory.isFull()){
			RSItem[] items = Interfaces.get(300, 75).getItems(); // GET ALL THE ITEM IN THE SHOP
			for (int i = 0; i < items.length; i++) { // LOOP THROUGHT THE ITEM
				if (items[i].getID() == ID_ESS) { // IF THE ITEM IS SAME AS THE ITEM WANT
					println(items[i].getID()+ " "+ items[i].getStack());
					items[i].changeType(org.tribot.api2007.types.RSItem.TYPE.BANK); // TURN TO A BANK ITEM
					Mouse.click((int)items[i].getArea().getCenterX(), (int)items[i].getArea().getCenterY()+40,3); //right click the item;
					if(x == 10){
						if(ChooseOption.isOptionValid("Buy 10")) { // IF BUY 10 IS AVI
							ChooseOption.select("Buy 10"); // CLICK BUY TEN
						} 
						sleep(500);
					}
					if(x == 5){
						if(ChooseOption.isOptionValid("Buy 10")) { // IF BUY 5 IS AVI
							ChooseOption.select("Buy 10"); // CLICK buy 5 
						} 
						sleep(500);
					}
					if(x == 1){
						if(ChooseOption.isOptionValid("Buy 1")) { // IF BUY 1 IS AVI
							ChooseOption.select("Buy 1"); // CLICK BUY one
						} 
						sleep(500);
					}
					if(ChooseOption.isOptionValid("Cancel"))
						ChooseOption.select("Cancel"); // CLICK BUY one
				} 
			}
		}
		return false;
	}
	private boolean walktostore(){
		Cameraset();
		if(USESHORTCUT == true)
			while(!atstore()){
				println("Walking");
				Walking.walkPath(ATOSs);
				Walking.walking_timeout = 1;

			}
		if(USESHORTCUT == false)
			while(!atstore()){
				Walking.walkPath(ATOS);
				Walking.walking_timeout = 1;
			}
		sleepW();
		return false;
	}

	private boolean walktorock(){
		if(USESHORTCUT == true)
			while(!atrock() && !ataltar()){
				println("Walking");
				Walking.walkPath(STOAs);
				Walking.walking_timeout = 1;
			}
		if(USESHORTCUT == false)
			while(!atrock() && !ataltar()){
				println("Walkingx");
				Walking.walkPath(STOA);
				Walking.walking_timeout = 1;
			}
		sleepW();
		return false;
	}


	boolean walkPath(RSTile [] x){
		for(int z = 0; z < x.length; z++){
			Walking.walkTo(x[z]);
			while(Player.isMoving()){
				sleep(1);
			}
		}
		return true;

	}
	private boolean enterrock(){
		RSObject[] rock = Objects.getAt(new RSTile(2868,3018));
		println(rock.length);
		if(rock.length > 0)
			if(rock != null){
				camera(rock[0]);
				//	if(DynamicClicking.clickRSObject(rock[0], "Enter")){
				Walking.walkTo(rock[0].getPosition());
				if(rock[1].click("Enter")){
					sleeps();
					return true;
				}
					
			}
		sleeps();
		return false;
	}
	//-----------------------------------------------------------
	private void sleepW(){
		while(Player.isMoving())
			sleep(50);
	}
	private void Cameraset(){
		Camera.setCameraAngle(100);
		Camera.setCameraRotation(0);
	}
	private void sleeps(){
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
		sleep(200,30);
	}
	private void camera(RSObject cam){
		RSObject target = cam;	
		Camera.turnToTile(target.getPosition());
	}
	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.

	/**
	 *
	 * @author John
	 */
	 class NewJFrame extends javax.swing.JFrame {

	    /**
	     * Creates new form NewJFrame
	     */
	    public NewJFrame() {
	        initComponents();
	    }

	    /**
	     * This method is called from within the constructor to initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is always
	     * regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {

	        jLabel1 = new javax.swing.JLabel();
	        jPanel1 = new javax.swing.JPanel();
	        jLabel2 = new javax.swing.JLabel();
	        SPEED = new javax.swing.JTextField();
	        shortcut = new javax.swing.JCheckBox();
	        jButton1 = new javax.swing.JButton();
	        NPCs = new javax.swing.JTextField();
	        ROCKO = new javax.swing.JTextField();
	        ROCKI = new javax.swing.JTextField();
	        POR = new javax.swing.JTextField();
	        jLabel3 = new javax.swing.JLabel();
	        jLabel4 = new javax.swing.JLabel();
	        jLabel5 = new javax.swing.JLabel();
	        jLabel6 = new javax.swing.JLabel();

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel1.setText("DG Nature Crafter");

	        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel2.setText("Mouse Speed");

	        SPEED.setText("180");

	        shortcut.setText("Use ShortCut");

	        jButton1.setText("Start");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        NPCs.setText("523");

	        ROCKO.setText("20792");

	        ROCKI.setText("12640");

	        POR.setText("12627");


	        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel3.setText("NPC");

	        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel4.setText("Rock Outside");

	        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel5.setText("Rock Inside");

	        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	        jLabel6.setText("Portal");

	        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addComponent(shortcut, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
	                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addComponent(jButton1)
	                        .addGap(136, 136, 136))
	                    .addGroup(jPanel1Layout.createSequentialGroup()
	                        .addGap(26, 26, 26)
	                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(jPanel1Layout.createSequentialGroup()
	                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(POR, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(jPanel1Layout.createSequentialGroup()
	                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(ROCKI, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(jPanel1Layout.createSequentialGroup()
	                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(ROCKO, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(jPanel1Layout.createSequentialGroup()
	                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(NPCs, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addGroup(jPanel1Layout.createSequentialGroup()
	                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                                .addComponent(SPEED, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))))
	                .addContainerGap())
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGap(22, 22, 22)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(SPEED, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(1, 1, 1)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(NPCs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(ROCKO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(ROCKI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(POR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
	                .addComponent(shortcut)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addComponent(jButton1)
	                .addContainerGap())
	        );

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(layout.createSequentialGroup()
	                        .addGap(82, 82, 82)
	                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
	                    .addGroup(layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap(45, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGap(0, 18, Short.MAX_VALUE))
	        );

	        pack();
	    }// </editor-fold>                        


		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			MOUSESPEED = Integer.parseInt(SPEED.getText());
			 ID_ROCK =  Integer.parseInt(ROCKO.getText()); //o
			 ID_ALTAR =  Integer.parseInt(ROCKI.getText());;
			 ID_PORTAL =  Integer.parseInt(POR.getText());
			 NPC = Integer.parseInt(NPCs.getText());
			println("Mouse Speed Set to: " + MOUSESPEED);
			if(shortcut.isSelected()){
				USESHORTCUT = true;
				println("Using ShortCut");
			}
			g1.setVisible(false);
		}                                        

	    /**
	     * @param args the command line arguments
	     */

	    // Variables declaration - do not modify                     
	    private javax.swing.JTextField NPCs;
	    private javax.swing.JTextField POR;
	    private javax.swing.JTextField ROCKI;
	    private javax.swing.JTextField ROCKO;
	    private javax.swing.JTextField SPEED;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JLabel jLabel5;
	    private javax.swing.JLabel jLabel6;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JCheckBox shortcut;
	    // End of variables declaration                   
	}


}