package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
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


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "Clue Scroll Solver", description = "Making Money", version = 1.0)


public class ClueScroll extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	char ctrl = KeyEvent.VK_CONTROL;
	private final int [] ITEMSNOT = {555,556,554,2566,557,563,7331,995,1627,952,559,10230,10200,10212,10198,10214,10323,10182,10220,10222,3497,3516,2690,2711,2703,2713,2689,7236,10186}; // a array of ID
	final int Va = 15, Lu = 18, Fa = 21 , Ca = 26;
	private final int HAM = 1645, DEACON = 1643,JIMMY = 5524; // a single ID
	private final long startTime = System.currentTimeMillis(); //timer
	private final RSTile [] Walkroute1 = {
			new RSTile(3148,3216),
			new RSTile(3152,3220),
			new RSTile(3154,3227),
			new RSTile(3160,3233),
			new RSTile(3163,3242),
			new RSTile(3164,3247)
	}, 
	WalkRouteLumbi = {
			new RSTile(3221, 3218, 0),
			new RSTile(3232, 3218, 0), 
			new RSTile(3233, 3223, 0), 
			new RSTile(3230, 3230, 0),
			new RSTile(3227, 3234, 0), 
			new RSTile(3224, 3236, 0), 
			new RSTile(3217, 3237, 0), 
			new RSTile(3210, 3239, 0), 
			new RSTile(3201, 3238, 0),
			new RSTile(3192, 3237, 0), 
			new RSTile(3185, 3244, 0), 
			new RSTile(3176, 3243, 0),
			new RSTile(3167, 3247, 0),
			new RSTile(3165, 3248, 0)
	},
	Walkroute2 = {
			new RSTile(3139, 3260, 0),
			new RSTile(3149, 3257, 0), 
			new RSTile(3150, 3254, 0),
			new RSTile(3155, 3251, 0),
			new RSTile(3156, 3249, 0),
			new RSTile(3165, 3248, 0) 
	},
	Walkroute3 = {
			new RSTile(3186, 3211, 0),
			new RSTile(3181, 3216, 0), 
			new RSTile(3179, 3225, 0), 
			new RSTile(3172, 3232, 0),
			new RSTile(3170, 3239, 0), 
			new RSTile(3167, 3247, 0) 
	},
	Walkroute4 = {
			new RSTile(3139, 3228, 0),
			new RSTile(3145, 3231, 0),
			new RSTile(3150, 3233, 0), 
			new RSTile(3152, 3234, 0), 
			new RSTile(3158, 3237, 0),
			new RSTile(3164, 3243, 0), 
			new RSTile(3167, 3247, 0) 
	},
	WalkToCamBank = {
			new RSTile(2757, 3478, 0), 
			new RSTile(2754, 3478, 0),
			new RSTile(2744, 3478, 0),
			new RSTile(2738, 3479, 0), 
			new RSTile(2730, 3485, 0),
			new RSTile(2724, 3492, 0)
	},
	CamTo3516 = { 
			new RSTile(2724, 3493, 0),
			new RSTile(2725, 3487, 0), 
			new RSTile(2721, 3486, 0),
			new RSTile(2712, 3484, 0), 
			new RSTile(2708, 3483, 0),
			new RSTile(2699, 3483, 0),
			new RSTile(2694, 3483, 0),
			new RSTile(2689, 3483, 0), 
			new RSTile(2681, 3481, 0), 
			new RSTile(2679, 3475, 0), 
			new RSTile(2675, 3467, 0), 
			new RSTile(2672, 3463, 0), 
			new RSTile(2666, 3463, 0), 
			new RSTile(2661, 3462, 0), 
			new RSTile(2652, 3465, 0), 
			new RSTile(2649, 3461, 0), 
			new RSTile(2641, 3458, 0), 
			new RSTile(2631, 3457, 0),
			new RSTile(2629, 3460, 0), 
			new RSTile(2623, 3464, 0), 
			new RSTile(2622, 3472, 0),
			new RSTile(2620, 3476, 0), 
			new RSTile(2614, 3481, 0), 
			new RSTile(2612, 3482, 0)
	},
	CBankTo10222 = {
			new RSTile(2725, 3491, 0), 
			new RSTile(2727, 3484, 0),
			new RSTile(2728, 3477, 0), 
			new RSTile(2729, 3468, 0), 
			new RSTile(2732, 3457, 0),
			new RSTile(2733, 3443, 0), 
			new RSTile(2735, 3431, 0), 
			new RSTile(2741, 3428, 0),
			new RSTile(2748, 3422, 0), 
			new RSTile(2755, 3415, 0), 
			new RSTile(2755, 3402, 0), 
			new RSTile(2760, 3401, 0)
	},
	VaTo2689 = {
			new RSTile(3214, 3424, 0), new RSTile(3206, 3427, 0), new RSTile(3194, 3429, 0), new RSTile(3182, 3429, 0), new RSTile(3173, 3427, 0), new RSTile(3164, 3422, 0), new RSTile(3154, 3416, 0), new RSTile(3151, 3413, 0)
	},
	Vato10186 = { 
			new RSTile(3217, 3425, 0), new RSTile(3221, 3429, 0), new RSTile(3227, 3428, 0), new RSTile(3238, 3429, 0), new RSTile(3245, 3429, 0), new RSTile(3253, 3421, 0) 
	}
	;


	String sa = "null"; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE); //set color
		g.drawString(sa , 10, 70); //Explain of using the sa variable from before
		//Any other paint stuff goes here, such as Code generated using Enfilade's Easel
	}

	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			Walking.control_click = true;
			Mouse.setSpeed(180); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable
			sa = "Starting";
			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				sa = "Checking";

				break;
			case PickPocket:
				sa = "PickPocketing for Clue Scroll";
				PickPocketing();
				break;
			case Drop:
				sa = "Inventory is full";
				Drop();
				break;
			case TRAP:
				sa = "Trap in Cage";
				try{
					Trap();
				}
				catch(ArrayIndexOutOfBoundsException e){
					break;
				}
				break;
			case OpeningCamp:
				sa = "Opening the Trap door";
				OpeningCamp();
				break;
			case ToWalk:
				sa = "Walking to Camp";
				WheretoWalk();
				break;
			case HAVESCROLL:
				sa = "Scroll Found!";
				GettingClueScroll();
				break;
			default:
				break;

			}
		}
	}
	//STATE--------------------------------------------------------
	private STATE getState(){
		sa = "Getting What to do";
		if(Inventory.find("Clue scroll").length == 0){
			sa = "Clue Scroll not Found";
			UriNear();
			if(Inventory.find("Clue scroll").length != 1 && NearDeacon() && !BehindDoor2()){ //if dont have clue scroll, near deacon and not behinddoor2 
				if(Inventory.isFull())
					return STATE.Drop; //dropping stuff
				else
					return STATE.PickPocket; //pick pocketing
			}
			else if(JIMMY() || BehindDoor2()){
				return STATE.TRAP; //trap with jimmy
			}
			else if(EntofCamp() || Ladder()){
				return STATE.OpeningCamp; //Open the trapdoor of Camp
			}
			else if(HavetoWalk()){
				return STATE.ToWalk;
			}
			else if(Inventory.find("Casket").length > 0){
				println("Casket");
				RSItem [] cask = Inventory.find("Casket");
				if(cask != null){
					cask[0].click("Open");
				}
				return STATE.CHECK;
			}
			else
			{
				Tele(Lu);
			}
		}
		else if(Inventory.find("Clue scroll").length > 0){
			sa = "Clue Scroll Found!";
			return STATE.HAVESCROLL;
		}
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, Drop, PickPocket, TRAP, OpeningCamp,ToWalk,HAVESCROLL
	}	
	//-------------------------------------------------------------



	//---------------------
	public boolean UriNear(){
		RSNPC [] Uri = NPCs.findNearest("Uri");
		if(Uri != null && Uri.length > 0){
			Uri[0].click("Talk");
			return true;
		}
		return false;
	}
	public boolean GettingClueScroll(){
		int ScrID = 0;
		RSItem [] Scr = Inventory.find("Clue scroll");
		if(Scr != null){
			ScrID = Scr[0].getID(); 
			CheckingScroll(ScrID);
			return true;
		}
		else
			return false;
	}
	public boolean OpenDoor(RSTile x){
		RSObject [] Door = Objects.getAt(x);
		if(Door != null){
			if(Door[0].click("Open")){
				sleep(250,500);
				return true;
			}
		}
		return false;
	}
	boolean OpenBank(){
		while(!Banking.isBankScreenOpen()){
			sa = "Opening Bank";
			Banking.openBankBooth();
			if(Banking.isPinScreenOpen()){
				Banking.inPin();
			}
		}
		return true;
	}
	public boolean CheckingScroll(int SCID){
		if(SCID == 10186){
			Tele(Va);
			Walkto(Vato10186,3253,3421);
			OpenBank();
			if(Banking.isBankScreenOpen()){
				sa = "Depositing Everything";
				Banking.depositAllExcept((ITEMSNOT));
				sa = "Withdrawing Item";
				Banking.withdraw(1, "Steel med helm");
				sleep(250,500);
				Banking.withdraw(1, "Steel pickaxe");
				sleep(250,500);
				Banking.withdraw(1, "Bronze platelegs");
				sleep(250,500);
			}
			sa = "Withdraw Done";
			sa = "Equiping Item";
			Equip("Steel med helm",EQUIPMENT.HELM);
			Equip("Steel pickaxe",EQUIPMENT.WEAPON);
			Equip("Bronze platelegs",EQUIPMENT.LEGS);
			OpenBank();
			if(Banking.isBankScreenOpen()){
				Banking.depositAllExcept(ITEMSNOT);
			}
		}
		if(SCID == 2689){
			Tele(Va);
			Walkto(VaTo2689,3151,3413);
			IsWalking();
			OpenDoor(new RSTile(3151,3413));
			while(Player.getPosition().distanceToDouble(new RSTile(3153,3405)) > 0)
				Walking.walkTo(new RSTile(3153,3405));
			OpenDoor(new RSTile(3153,3405));
			RSObject [] Drawer = Objects.getAt(new RSTile(3156,3406));
			if(Drawer != null){
				if(Drawer[0].click("Open")){
					IsWalking();
					sleep(250,500);
					Drawer[0].click("Search");
					sleeps();
					sleep(250,500);
					return true;
				}

			}
		}
		if(SCID == 10222){
			while(Player.getPosition().distanceTo(new RSTile(2757,3478)) > 10 && Player.getPosition().distanceTo(new RSTile(2724,3492)) > 10 ){
				sa = "Teleporting to Cam";
				Tele(Ca);
			}
			sa = "Walking to Bank";
			Walkto(WalkToCamBank, 2724 , 3492);
			OpenBank();
			if(Banking.isBankScreenOpen()){
				sa = "Depositing Everything";
				Banking.depositAllExcept((ITEMSNOT));
				sa = "Withdrawing Item";
				Banking.withdraw(1, "Coif");
				sleep(250,500);
				Banking.withdraw(1, "Iron platebody");
				sleep(250,500);
				Banking.withdraw(1, "Leather gloves");
				sleep(250,500);
			}
			sa = "Withdraw Done";

			while(Banking.isBankScreenOpen()){
				Banking.close();
			}
			sa = "Equiping Stuff";
			Equip("Coif", EQUIPMENT.helm);
			Equip("Iron platebody",EQUIPMENT.BODY);
			Equip("Leather gloves",EQUIPMENT.GLOVES);

			sa = "Walking to Keep Le Faye";
			Walkto(CBankTo10222, 2760, 3401);
			Walking.walkTo(new RSTile(2761,3402));
			IsWalking();
			sa = "Doing Emote";
			Emote(20, "Raspberry");
			sa = "Done";
			sleeps();
			sleep(1000);
			return true;
		}
		if(SCID == 10218){

		}
		if(SCID == 10230){

		}
		if(SCID == 10200){

		}
		if(SCID == 10212){

		}
		if(SCID == 10198){

		}
		if(SCID == 10214){

		}
		if(SCID == 10232){

		}
		if(SCID == 10182){

		}
		if(SCID == 10220){

		}
		if(SCID == 3497){

		}
		if(SCID == 3516){
			while(Player.getPosition().distanceTo(new RSTile(2757,3478)) > 10 && Player.getPosition().distanceTo(new RSTile(2724,3492)) > 10 ){
				sa = "Teleporting to Cam";
				Tele(Ca);
			}
			sa = "Walking to Bank";
			Walkto(WalkToCamBank, 2724 , 3492);
			while(!Banking.isBankScreenOpen()){
				sa = "Opening Bank";
				Banking.openBankBooth();
				if(Banking.isPinScreenOpen()){
					Banking.inPin();
				}
			}
			if(Banking.isBankScreenOpen()){
				sa = "Depositing Everything";
				Banking.depositAllExcept((ITEMSNOT));
				while(Inventory.find("Scroll clue").length == 0){
					sa = "Withdrawing Scroll";
					if(Banking.withdraw(1, 3516))
						break;
				}
				sa = "Withdraw Done";
			}
			sa = "Walking to Brother Galahad's house";
			Walkto(CamTo3516, 2612, 3482);
			sa = "Digging";
			DigSpade();
			sa = "Done";
			sleeps();
			sleep(1000);
			return true;
		}
		if(SCID == 2690){

		}
		if(SCID == 2711){

		}
		if(SCID == 2703){

		}
		if(SCID == 2713){

		}
		if(SCID == 7236){

		}

		return false;
	}


	//---------------------
	public enum EQUIPMENT {
		helm(0), HELM(0), cape(1), CAPE(1)
		, neck(2), NECK(2), weapon(3), WEAPON(3)
		, body(4), BODY(4), shield(5), SHIELD(5)
		, legs(7), LEGS(7), gloves(9), GLOVES(9)
		, boots(10), BOOTS(10), ring(12), RING(12)
		, arrow(13), ARROW(13);
		private int value;

		private EQUIPMENT(int value) {
			this.value = value;
		}
	}; 
	public static int getEquipment(EQUIPMENT equip){
		if(Interfaces.get(387,28) != null){
			for(RSItem i : Interfaces.get(387, 28).getItems()){
				if(i.getIndex() == equip.value){
					return i.getID();
				}
			}
		}
		return -1;
	}
	public static int getArrowStack(){
		if(Interfaces.get(387,28) != null){
			for(RSItem i : Interfaces.get(387, 28).getItems()){
				if(i.getIndex() == 13){
					return i.getStack();
				}
			}
		}
		return -1;
	}

	public boolean Emote(int x, String y){
		/*
		 *1 = Yes   2 = no    3 = bow   4 = Angry 5 = think
		 *6 = wave  7 = shrug   8 =  cheer   9 = beckon  10 = laugh
		 *11 = jump for joy 12 = yawn  13 = dance  14 = jig    15 = spin
		 *16 = headbang  17 = cry   18 = blow kiss   19 = panic 20 = raspberry
		 * */
		GameTab.open(GameTab.TABS.EMOTES);
		RSInterfaceChild Emo = Interfaces.get(464, x);
		if(Emo != null){
			while(Inventory.find("Clue scroll").length > 0){
				Emo.click(y);
				sleeps();
				UriNear();
			}
			GameTab.open(GameTab.TABS.INVENTORY);
			return true;
		}
		return false;

	}
	public boolean Equip(String na,EQUIPMENT x){ //Check if equipment is already on
		RSItem [] Item = Inventory.find(na);
		if(Item != null){
			while(Inventory.find(na).length > 0 && getEquipment(x) != Item[0].getID()){
				Item[0].click("Wear");
				sleep(250,500);

			}
			return true;
		}
		return false;
	}
	public boolean DigSpade(){
		RSItem [] Spade = Inventory.find("Spade");
		if(Spade != null)
			Spade[0].click("dig");
		return true;
	}
	public boolean Tele(int x){
		GameTab.open(GameTab.TABS.MAGIC);
		RSInterfaceChild Teleport = Interfaces.get(192, x);
		if(Teleport != null){
			Teleport.click("Cast");
			sleep(250,500);
			sleeps();
			GameTab.open(GameTab.TABS.INVENTORY);
			return true;
		}
		else 
			return false;
	}
	public boolean WheretoWalk(){
		if(Player.getPosition().distanceTo(new RSTile(3148,3216)) < 5){
			Walkto(Walkroute1,3164,3247);
			return true;
		}
		else if(Player.getPosition().distanceTo(new RSTile(3221,3218)) < 5){
			Walkto(WalkRouteLumbi,3164,3247);
			return true;
		}
		else if(Player.getPosition().distanceTo(new RSTile(3139,3260)) < 5){
			Walkto(Walkroute2,3164,3247);
			return true;
		}
		else if(Player.getPosition().distanceTo(new RSTile(3186,3211)) < 5){
			Walkto(Walkroute3,3164,3247);
			return true;
		}
		else if(Player.getPosition().distanceTo(new RSTile(3139,3228)) < 5){
			Walkto(Walkroute4,3164,3247);
			return true;
		}


		return false;
	}
	public boolean Walkto(RSTile [] walkingpath, int x, int y){
		while(Player.getPosition().distanceTo(new RSTile(x,y)) > 5){
			Walking.walkPath(walkingpath);
			Walking.walking_timeout = 1;

		}
		return true;
	}
	public boolean OpeningCamp(){
		if(EntofCamp()){
			RSObject [] trap = Objects.getAt(new RSTile(3166,3252));
			if(!trap[0].isOnScreen()){
				Walking.walkTo(new RSTile(3166,3252));
				IsWalking();
			}
			if(trap != null && trap.length != 0){
				if(trap[0].isOnScreen()){
					if(trap[0].click("Climb-down")){
						sa = "Climing down";
						sleeps();
						sleep(1000);
					}
					else{
						sa = "Pick Locketing";
						trap[0].click("Pick-lock");
						sleeps();
					}
				}
			}
		}
		if(Ladder()){
			sa = "At ladder";
			Walking.walkTo(new RSTile(3153,9646));
			IsWalking();
			while( Player.getPosition().distanceTo(new RSTile(3158,9641)) > 1 ){
				sa = "walking to Door";
				Camera.turnToTile(new RSTile(3158,9641));
				Walking.walkTo(new RSTile(3158,9641));
				IsWalking();

			}

			sa = "Opening Door [Ladder]";
			RSObject [] door = Objects.find(2, "Door");
			if(door != null){
				sa = "Door Found";
				if(door[0].click("Open")){
					sleeps();
				}
				Walking.walkTo(new RSTile(3165,9631));
				IsWalking();
			}
			else {
				Walking.walkTo(new RSTile(3165,9631));
				IsWalking();
				return true;
			}
		}
		else{
			Walking.walkTo(new RSTile(3165,9631));
			IsWalking();
		}
		return true;

	}
	public boolean Trap(){
		while(!BehindDoor1() && JIMMY()){
			sa = "Openning Door";
			RSObject [] door = Objects.getAt(new RSTile(3183,9611));
			if(door != null){
				door[0].click("Pick-lock");
				sleeps();
			}
		}
		while(!BehindDoor2()){
			sa = "Walking to Door 2";
			Walking.walkTo(new RSTile(3176,9616));
			while(Player.getPosition().distanceTo(new RSTile(3171,9621)) != 0){
				Walking.walkTo(new RSTile(3171,9621));
				IsWalking();
			}
			sa = "Opening Door 2";
			RSObject [] door = Objects.find(2, "Door");
			if(door != null)
				sa = "Door Found";
			if(door[0].click("Open")){
				sleeps();
				break;
			}
			else{
				Walking.walkTo(new RSTile(3165,9631));
				IsWalking();
				return true;
			}

		}
		Walking.walkTo(new RSTile(3165,9631));
		IsWalking();
		return true;
	}
	public boolean PickPocketing(){
		sa = "Looking for Ham";
		while(!Inventory.isFull() && !JIMMY()){
			RSNPC[] ham = NPCs.findNearest(HAM);
			if(ham != null)
				if(!ham[0].isOnScreen()){
					Walking.walkTo(ham[0].getPosition());
				}
			if(ham[0].isOnScreen()){
				sa = "Pick Pocketing";
				if(ham[0].click("Pickpocket"))
					sleeps();
				else
					ham[0].click("Cancel");
				if(JIMMY())
					return true;
				break;
			}
		}
		return false;
	}
	public boolean Drop(){
		if(Inventory.isFull()){
			Inventory.dropAllExcept(ITEMSNOT);
		}
		return true;
	}


	//Check Method---------------------------------------------------
	public boolean HavetoWalk(){
		if(Player.getPosition().distanceTo(new RSTile(3139,3228)) < 5 || Player.getPosition().distanceTo(new RSTile(3186,3211)) < 5 || Player.getPosition().distanceTo(new RSTile(3139,3260)) < 5 || Player.getPosition().distanceTo(new RSTile(3148,3216)) < 5 || Player.getPosition().distanceTo(new RSTile(3221,3218)) < 5)
			return true;
		return false;
	}
	public boolean Ladder(){
		if(Objects.find(3, 5493).length > 0)
			return true;
		return false;
	}
	public boolean EntofCamp(){
		if((Player.getPosition().distanceTo(new RSTile(3166,3252)) < 15 )){
			return true;
		}
		return false;
	}
	public boolean BehindDoor1(){
		if(Player.getPosition().distanceTo(new RSTile(3182,9611)) == 0)
			return true;
		return false;
	}
	public boolean BehindDoor2(){
		if(Player.getPosition().distanceTo(new RSTile(3171,9623)) == 0)
			return true;
		return false;
	}
	public boolean NearDeacon(){
		if(NPCs.find(10,DEACON).length > 0){
			return true;
		}
		return false;
	}

	public boolean JIMMY(){
		if(NPCs.find(5, JIMMY).length > 0){
			return true;
		}
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//Other Method-------------------------------------------------
	//Other method such as...
	private void sleeps() {
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
	}
	private void IsWalking(){
		sleep(500);
		while(Player.isMoving()){
			sleep(50);
		}
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(0);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		EX.click("Open");
		sleep(50);
	}
	//-------------------------------------------------------------


} // END OF SCRIPT