package scripts;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;


import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;


@ScriptManifest(authors={"Aegis"}, category="RuneCrafting", name="aLawsHost")
public class aLawsHost extends Script implements MessageListening07 {
	
	
	private final RSTile[] TO_ALTAR_PATH = { new RSTile(2464, 4830, 0) },
			LEAVE_ALTAR_PATH = { new RSTile(2464, 4819, 0) };
	
	private final int[] MONKS = { 657, 658, 2728, 2729, 2730, 2371 }, 
			PLANK = { 2413, 2415 }, 
			BANKERS = {494, 495}, 
			ESS = {7936},
			BANK = { 2213 };
	
	
	private final RSTile ENTRANA_BOAT_TILE = new RSTile(2834, 3331, 1), 
			ENTRANA_PORT_TILE = new RSTile(2834, 3335, 0), 
			SARIM_BOAT_TILE = new RSTile(3048, 3231, 1), 
			SARIM_PORT_TILE = new RSTile(3048, 3234, 0), 
			CRATE_TILE = new RSTile(2444, 3085);



	  private final RSArea ALTAR_AREA = new RSArea(new RSTile(2450, 4810), 
	    new RSTile(2475, 4851)), RUINS_AREA = new RSArea(new RSTile(2833, 3371), new RSTile(2869, 3388));
	  
		
	private String runner;

	
	@Override
	public void run() {
		while (!(Login.getLoginState() == (STATE.INGAME))) {
			sleep(500);
		}
		while (true) {
		Mouse.setSpeed((120 + General.random(0, 30)));
		
		if (haveEss()) {

			if (!(atAltarRoom())) {
				if (atRock()) {
					enterRock();
				}		
			} else {
				if (!(atAltar())) {
					pathRunTo(TO_ALTAR_PATH);
				} else {
					craftRunes();
				}
			}				
		} else {
			if (atAltarRoom()) {
				if (atAltar()) {
					pathRunTo(LEAVE_ALTAR_PATH);
				} else {
					if (atPortal()) {
						usePortal();
					}			
				}			
			}			
			if (atRuins()) {
				if (getWindowNum() == 0) {
					Keyboard.typeSend("Open");
					sleep(3000, 6000);					
				}
				if (getWindowNum() == 1) {
					if (hasSlot1()) {
						if (hasSlot2()) {
							if (hasOffered()) {
								accept();
							}
						} else {
							offerLaws();
						}
					} else {
						offerEss();
					}
				}
				if (getWindowNum() == 2) {
						accept();
				}				
			}
		}
		sleep(250);		
		}
	}
	
	private boolean atRuins() {
	    if (this.RUINS_AREA.contains(new RSTile[] { Player.getPosition() })) {
	      return true;
	    }
	    return false;
	  }



	  private boolean atAltarRoom() {
	    if (this.ALTAR_AREA.contains(new RSTile[] { Player.getPosition() })) {
	      return true;
	    }
	    return false;
	  }

	  private boolean atPortal() {
	    RSObject[] portal = Objects.findNearest(15, new int[] { 10054 });
	    RSTile myPos = Player.getPosition();
	    if ((portal != null) && (portal.length > 0)) {
	      for (RSObject port : portal)
	        if (myPos.distanceTo(port.getPosition()) < 3)
	          return true;
	    }
	    return false;
	  }
	  
	  public void waitToCross() {

		    long t = System.currentTimeMillis();

		    while (Timing.timeFromMark(t) < 10000L) {
		      sleep(500L);
		      if ((atSarimPort()) || (atEntranaPort()))
		      {
		        break;
		      }
		    }
		    RSObject[] plank = Objects.findNearest(10, PLANK);
		    if ((plank != null) && (plank.length > 0)) {
		      DynamicClicking.clickRSObject(plank[0], "Cross");
		      sleep(1000, 1500);
		    }
		  }

	  private boolean atAltar() {
	    RSObject[] altar = Objects.findNearest(15, new int[] { 10067 });
	    RSTile myPos = Player.getPosition();
	    if ((altar != null) && (altar.length > 0)) {
	      for (RSObject alt : altar)
	        if ((myPos.distanceTo(alt.getPosition()) < 5) || (alt.isOnScreen()))
	          return true;
	    }
	    return false;
	  }

	  private boolean atRock() {
	    RSObject[] altar = Objects.findNearest(15, new int[] { 12733 });
	    RSTile myPos = Player.getPosition();
	    if ((altar != null) && (altar.length > 0)) {
	      for (RSObject alt : altar)
	        if ((myPos.distanceTo(alt.getPosition()) < 5) || (alt.isOnScreen()))
	          return true;
	    }
	    return false;
	  }


	  private boolean atEntranaPort() {
	    RSTile myPos = Player.getPosition();
	    return myPos.distanceTo(this.ENTRANA_PORT_TILE) < 4;
	  }

	  private boolean atSarimPort() {
	    RSTile myPos = Player.getPosition();
	    return myPos.distanceTo(this.SARIM_PORT_TILE) < 3;
	  }

  public class RSArea
  {
    private final Polygon area;
    private final int plane;

    public RSArea(RSTile[] tiles, int plane)
    {
      this.area = tilesToPolygon(tiles);
      this.plane = plane;
    }

    public RSArea(RSTile[] tiles) {
      this(tiles, 0);
    }

    public RSArea(RSTile southwest, RSTile northeast) {
      this(southwest, northeast, 0);
    }

    public RSArea(int swX, int swY, int neX, int neY) {
      this(new RSTile(swX, swY), new RSTile(neX, neY), 0);
    }

    public RSArea(int swX, int swY, int neX, int neY, int plane)
    {
      this(new RSTile(swX, swY), new RSTile(neX, neY), plane);
    }

    public RSArea(RSTile southwest, RSTile northeast, int plane)
    {
      this(new RSTile[] { southwest, 
        new RSTile(northeast.getX() + 1, southwest.getY()), 
        new RSTile(northeast.getX() + 1, northeast.getY() + 1), 
        new RSTile(southwest.getX(), northeast.getY() + 1) }, plane);
    }

    public boolean contains(RSTile[] tiles) {
      RSTile[] areaTiles = getTiles();
      for (RSTile check : tiles) {
        for (RSTile space : areaTiles) {
          if (check.equals(space)) {
            return true;
          }
        }
      }
      return false;
    }

    public boolean contains(int x, int y) {
      return contains(new RSTile[] { new RSTile(x, y) });
    }

    public boolean contains(int plane, RSTile[] tiles) {
      return (this.plane == plane) && (contains(tiles));
    }

    public Rectangle getDimensions() {
      return new Rectangle(this.area.getBounds().x + 1, 
        this.area.getBounds().y + 1, getWidth(), getHeight());
    }

    public RSTile getNearestTile(RSTile base) {
      RSTile tempTile = null;
      for (RSTile tile : getTiles()) {
        if ((tempTile == null) || 
          (distanceBetween(base, tile) < distanceBetween(
          tempTile, tile))) {
          tempTile = tile;
        }
      }
      return tempTile;
    }

    public int getPlane() {
      return this.plane;
    }

    public Polygon getPolygon() {
      return this.area;
    }

    public RSTile[] getTiles() {
      ArrayList tiles = new ArrayList();
      for (int x = getX(); x <= getX() + getWidth(); x++) {
        for (int y = getY(); y <= getY() + getHeight(); y++) {
          if (this.area.contains(x, y)) {
            tiles.add(new RSTile(x, y));
          }
        }
      }
      return (RSTile[])tiles.toArray(new RSTile[tiles.size()]);
    }

    public int getWidth() {
      return this.area.getBounds().width;
    }

    public int getHeight() {
      return this.area.getBounds().height;
    }

    public int getX() {
      return this.area.getBounds().x;
    }

    public int getY() {
      return this.area.getBounds().y;
    }

    public Polygon tilesToPolygon(RSTile[] tiles) {
      Polygon polygon = new Polygon();
      for (RSTile t : tiles) {
        polygon.addPoint(t.getX(), t.getY());
      }
      return polygon;
    }

    public double distanceBetween(RSTile curr, RSTile dest) {
      return Math.sqrt((curr.getX() - dest.getX()) * (
        curr.getX() - dest.getX()) + (curr.getY() - dest.getY()) * (
        curr.getY() - dest.getY()));
    }
  }
  
  private String getChat() {
       for (int y = 11; y < 103; y++){
           RSInterfaceChild chat = Interfaces.get(137, y);
           if(chat != null && !chat.isHidden()){
               if(chat.getText() == ""){
                   RSInterfaceChild message = Interfaces.get(137, (y - 1));
                   return message.getText();
               } else if(y == 103){
                   if(chat.getText() != ""){
                       return chat.getText();
                   }
               }
           }
       }
       return null;
  }
  
    
    
    public void waitUntilIdle()
    {
      long t = System.currentTimeMillis();

      while (Timing.timeFromMark(t) < General.random(1500, 2000)) {
        if ((!Player.isMoving()) && (Player.getAnimation() == -1)) break;
        t = System.currentTimeMillis();

        sleep(50, 150);
      }
    }
    
    private void pathRunTo(RSTile[] path)
    {
      if (!Player.isMoving()) {
        runTo(path);
      }
      else
        waitUntilIdle();
    }
    
    private void runTo(RSTile[] path) {
        Keyboard.pressKey('\021');
        Walking.walkPath(path);
        Keyboard.releaseKey('\021');
      }

      private void runToTile(RSTile tile) {
        Keyboard.pressKey('\021');
        Walking.walkTo(tile);
        Keyboard.releaseKey('\021');
      }
      
      private boolean hasESS() {
    	  RSInterfaceChild lp = Interfaces.get(335, 50);
          if (lp != null) {
              RSInterfaceComponent[] l = lp.getChildren();
              if (l != null) {
                  for (RSInterfaceComponent i : l) {
                      if (i != null && i.getComponentItem() != -1) {
                    	  if (i.getComponentStack() >= 26) {
                    		  return true;

                    	  }
                      }
                  }
              }
          }
		return false;
      }
      
      private boolean hasSlot1() {
    	  RSInterfaceChild lp = Interfaces.get(335, 48);
          if (lp != null) {
        	  if (lp.getChild(0).getComponentItem() == 7937) {
        		  if (lp.getChild(0).getComponentStack() == 26) {
        			  return true;
        		  } else {
  					Mouse.clickBox(33, 104, 46, 89, 3);
					sleep(550);
					ChooseOption.select("Remove-All");
					sleep(1000);
        		  }
        		  } else {
        			  if (lp.getChild(0).getComponentItem() != -1) {
  	  					Mouse.clickBox(33, 104, 46, 89, 3);
						sleep(550);
						ChooseOption.select("Remove-All");
						sleep(1000);
        				  
        			  }
        		  }
        	  }
          return false;

          }
     
      
      private boolean hasSlot2() {
    	  RSInterfaceChild lp = Interfaces.get(335, 48);
          if (lp != null) {
        	  if (lp.getChild(1).getComponentItem() == 563) {
        		  if (lp.getChild(1).getComponentStack() == 26) {
        			  return true;
        		  } else {
  					Mouse.clickBox(83, 103, 95, 87, 3);
					sleep(550);
					ChooseOption.select("Remove-All");
					sleep(1000);
        		  }
        		  } else {
        			  if (lp.getChild(1).getComponentItem() != -1) {
        				  Mouse.clickBox(83, 103, 95, 87, 3);
						sleep(550);
						ChooseOption.select("Remove-All");
						sleep(1000);
        				  
        			  }
        		  }
        	  }
          return false;

          }
      
      private boolean hasOffered() {
    	  RSInterfaceChild lp = Interfaces.get(335, 50);
          if (lp != null) {
        	  if ((lp.getChild(25).getComponentItem() == 7936) && (lp.getChild(0).getComponentItem() == 7936)) {
        			  return true;
        	  }
          }
  					
          return false;
        		  

          }

      

 	    public static void accept(){
 	        if(getWindowNum() == 1){
 	            if(Interfaces.get(335, 17) != null && !Interfaces.get(335, 17).isHidden()){
 	                Interfaces.get(335, 17).click("Accept Trade");
 	            }
 	        } else if(getWindowNum() == 2){
 	            if(Interfaces.get(334, 20) != null && !Interfaces.get(334, 20).isHidden()){
 	                Interfaces.get(334, 20).click("Accept");
 	            }
 	        }
 	    }
 	    
 	    public static void decline(){
 	        if(getWindowNum() == 1){
 	            if(Interfaces.get(335, 18) != null && !Interfaces.get(335, 18).isHidden()){
 	                Interfaces.get(335, 18).click("Decline Trade");
 	            }
 	        } else if(getWindowNum() == 2){
 	            if(Interfaces.get(334, 21) != null && !Interfaces.get(334, 21).isHidden()){
 	                Interfaces.get(334, 21).click("Decline");
 	            }
 	        }
 	    }
 	    
 	    public static String getRecipientName(){
 	        String[] splitstring = null;
 	        if(getWindowNum() == 1){
 	            if(Interfaces.get(335,16) != null && !Interfaces.get(335,16).isHidden()){
 	                splitstring = Interfaces.get(335, 16).getText().split(": ");
 	                return splitstring[1];
 	            }
 	        } else if (getWindowNum() == 2){
 	            if(Interfaces.get(334,44) != null && !Interfaces.get(334,44).isHidden()){
 	                splitstring = Interfaces.get(334, 44).getText().split(">");
 	                return splitstring[1];
 	            }
 	        }
 	        return null;
 	    }
 	    
 	    public static int getWindowNum(){
 	        if(Interfaces.get(335) != null && !Interfaces.get(335).isHidden()){
 	            return 1;
 	        } else if (Interfaces.get(334) != null && !Interfaces.get(334).isHidden()){
 	            return 2;
 	        }
 	        return 0;
 	    }
 	    
 	    public static boolean recipientHasAccepted(){
 	        if(getWindowNum() == 1){
 	            if(Interfaces.get(335, 56) != null && !Interfaces.get(335, 56).isHidden() && Interfaces.get(335, 56).getText().contains("Other player has accepted.")){
 	                return true;
 	            }
 	        } else if (getWindowNum() == 2){
 	            if(Interfaces.get(334, 33) != null && !Interfaces.get(334, 33).isHidden() && Interfaces.get(334, 33).getText().contains("Other player has accepted.")){
 	                return true;
 	            }
 	        }
 	        return false;
 	    }
 	    
 	    public static boolean localHasAccepted(){
 	        if(getWindowNum() == 1){
 	            if(Interfaces.get(335, 56) != null && !Interfaces.get(335, 56).isHidden() && Interfaces.get(335, 56).getText().contains("Waiting for other player")){
 	                return true;
 	            }
 	        } else if (getWindowNum() == 2){
 	            if(Interfaces.get(334, 33) != null && !Interfaces.get(334, 33).isHidden() && Interfaces.get(334, 33).getText().contains("Waiting for other player")){
 	                return true;
 	            }
 	        }
 	        return false;
 	    }
 	    
 	   private void enterRock() {
 		    RSObject[] ruins = Objects.findNearest(15, new int[] { 12733 });
 		    if ((ruins != null) && (ruins.length > 0)) {
 		      RSTile ClickTile = new RSTile(ruins[0].getPosition().getX() + 1, ruins[0].getPosition().getY() + 1);
 		      if (!ClickTile.isOnScreen()) {
 		        Camera.turnToTile(ClickTile);
 		      }
 		      if (DynamicClicking.clickRSTile(ClickTile, "Enter"))
 		        waitUntilIdle();
 		    }
 		  }

 		  private boolean usePortal()
 		  {
 		    RSObject[] portal = Objects.findNearest(10, new int[] { 10054 });
 		    if ((portal != null) && (portal.length > 0) && (haveLaw())) {
 		      DynamicClicking.clickRSObject(portal[0], "Use");
 		      sleep(500, 700);
 		      return true;
 		    }
 		    return false;
 		  }
 		  
 		  private boolean haveEss()
 		  {
 		    RSItem[] string = Inventory.find(new int[] { 7936 });
 		    return (string != null) && (string.length > 0);
 		  }

 		  private boolean haveLaw() {
 		    RSItem[] string = Inventory.find(new int[] { 563 });
 		    return (string != null) && (string.length > 0);
 		  }
 		  

 		  private boolean craftRunes() {
 		    RSObject[] altar = Objects.findNearest(15, new int[] { 10067 });
 		    if ((altar != null) && (altar.length > 0)) {
 		      RSTile ClickTile = new RSTile(altar[0].getPosition().getX() + 1, altar[0].getPosition().getY() + 2);
 		      if (!ClickTile.isOnScreen()) {
 		        Camera.turnToTile(ClickTile);
 		      }
 		      if (DynamicClicking.clickRSTile(ClickTile, "Craft")) {
 		        long t = System.currentTimeMillis();
 		        while (Timing.timeFromMark(t) < General.random(1500, 2000)) {
 		          if ((Inventory.find(new int[] { 7936 }).length == 0) && (Player.getAnimation() == -1)) {
 		            return true;
 		          }
 		          sleep(50, 150);
 		          if ((Player.getAnimation() != -1) || (Player.isMoving())) {
 		            t = System.currentTimeMillis();
 		          }
 		        }
 		      }
 		    }
 		    return false;
 		  }
 		  
 		  public void offerEss() {
				Mouse.clickBox(570, 234, 584, 221, 3);
				sleep(550);
				ChooseOption.select("Offer-X");
				sleep(1000);
				Keyboard.typeSend("26");
				sleep(1500);
 		  }
 		  
 		  public void offerLaws() {
				Mouse.clickBox(613, 235, 625, 222, 3);
				sleep(550);
				ChooseOption.select("Offer-X");
				sleep(1000);
				Keyboard.typeSend("26");
				sleep(1500);
 		  }

 		  private void tradePlayer(String runner) {
 		    	for (RSPlayer i : Players.findNearest(runner)) {
 					if ((getWindowNum() == 0) && (!(haveEss()))) {
 						if (i.hover()) {
 							Mouse.click(3);
 							sleep(300);
 							if (ChooseOption.select("Trade with " + runner)) {
 								sleep(500);
 					}
 				}						
 						break;
 					}
 				}
 		    }

	
	@Override
    public void clanMessageRecieved(String arg0, String arg1) {
    }

	@Override
    public void playerMessageRecieved(String arg0, String arg1) {	
		runner = getChat().toString(); 
		if (runner.contains("wishes to trade")) {
			runner = getChat().substring(12, (getChat().length() - 26));
			tradePlayer(runner);
		}
 }

	@Override
    public void serverMessageRecieved(String message) {
    }
	
}