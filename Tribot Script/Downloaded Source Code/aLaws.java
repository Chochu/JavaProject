package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Camera;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;



@ScriptManifest(authors={"Aegis"}, category="Money Making", name="aLaws")
public class aLaws extends Script implements MessageListening07, Painting {
	
	private RSTile atTrade = new RSTile(2858, 3376);
	int lawPrice = ItemPrice.lookup("Law rune").getPrice();
	int laws = 0; 
	String status = "";
	
	public int MouseSpeed = 150;
	
	
	
	 private final RSTile[] bankToMonks = { new RSTile(3092, 3243, 0), new RSTile(3084, 3249, 0), new RSTile(3078, 3257, 0), 
		new RSTile(3076, 3267, 0), new RSTile(3071, 3276, 0), new RSTile(3063, 3270, 0), 
		new RSTile(3062, 3260, 0), new RSTile(3057, 3251, 0), new RSTile(3047, 3246, 0), 
		new RSTile(3041, 3238, 0), new RSTile(3046, 3235, 0) };

	 private final RSTile[] monksToBank = { new RSTile(3046, 3235, 0), new RSTile(3040, 3243, 0), new RSTile(3048, 3249, 0), 
		new RSTile(3058, 3252, 0), new RSTile(3065, 3260, 0), new RSTile(3065, 3270, 0), 
		new RSTile(3069, 3277, 0), new RSTile(3071, 3272, 0), new RSTile(3076, 3263, 0), 
		new RSTile(3080, 3253, 0), new RSTile(3089, 3248, 0), new RSTile(3092, 3243, 0) };
	
	 private final RSTile[] toAltar = { new RSTile(2834, 3335, 0), new RSTile(2844, 3336, 0), new RSTile(2854, 3337, 0), 
		new RSTile(2859, 3346, 0), new RSTile(2858, 3356, 0), new RSTile(2856, 3366, 0), 
		new RSTile(2857, 3376, 0), new RSTile(2858, 3376, 0) };

	 private final RSTile[] toLeave = { new RSTile(2856, 3377, 0), new RSTile(2856, 3367, 0), new RSTile(2857, 3357, 0), 
		new RSTile(2857, 3347, 0), new RSTile(2853, 3339, 0), new RSTile(2843, 3335, 0), 
		new RSTile(2834, 3335, 0) };
	 
	

	
	private final int[] MONKS = { 657, 658, 2728, 2729, 2730, 2371 };
	private final int[] PLANK = { 2413, 2415 };
	public final int[] BANKERS = {494, 495};
	public final int[] ESS = {7936};
	
	public final int BANK = 2213;
	
	  private final RSTile ENTRANA_BOAT_TILE = new RSTile(2834, 3331, 1);
	  private final RSTile ENTRANA_PORT_TILE = new RSTile(2834, 3335, 0);
	  private final RSTile SARIM_BOAT_TILE = new RSTile(3048, 3231, 1);
	  
	  private final RSArea TRADE_AREA = new RSArea(new RSTile(2856, 3376), 
			    new RSTile(3060, 3380));

	  private final RSArea BANK_AREA = new RSArea(new RSTile(3092, 3240), 
			    new RSTile(3097, 3246));
	  
	  private final RSArea BOAT_AREA = new RSArea(new RSTile(3042, 3234), 
			    new RSTile(3051, 3237));

	  private final RSArea RUINS_AREA = new RSArea(new RSTile(2833, 3371), 
	    new RSTile(2869, 3388));

	  private final RSArea ENTRANA_AREA = new RSArea(new RSTile(2828, 3335), 
	    new RSTile(2865, 3380));

	  private final RSArea SARIM_AND_DRAYNOR_AREA = new RSArea(new RSTile(3042, 3234), 
		new RSTile(3097, 3290));

	
	  private boolean atBoat() {
		    if (this.BOAT_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }
	  
	  private boolean atBank() {
		    if (this.BANK_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }

	  private boolean atRuins() {
		    if (this.RUINS_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }
	  private boolean atTrade() {
		    if (this.TRADE_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }


		  private boolean atEntrana() {
		    if (this.ENTRANA_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }


		  private boolean atSarimAndDraynor() {
		    if (this.SARIM_AND_DRAYNOR_AREA.contains(new RSTile[] { Player.getPosition() })) {
		      return true;
		    }
		    return false;
		  }


		  
			public void antiBan()
		    {
		        int options = General.random(1, 5); // Random action out of 5 possible actions
		        int rotation = General.random(-30, 30); // Random rotation that may be applied
		        int angle = General.random(-80, 80);  // Random angle that may be applied
		        int x = General.random(1,760); // Random X coordinate the mouse might move to
		        int y = General.random(1, 500); // Random Y coordinate the mouse might move to
		        
		        int speed = Mouse.getSpeed();    // Gets the mouse speed of the script to change it back
		        
		        if(options == 1)
		        {
		            //do nothing 
		            // This is a good option because moving the mouse once every time to click a tree and nowhere else is bot-like
		        }
		        else if(options == 2) // camera move option
		        {
		            int cameraOptions = General.random(1, 3); //options within options. The inception of options
		            
		            if(cameraOptions == 1)
		            {
		                Camera.setCameraAngle(angle); // Change the angle by a random, small amount.
		                sleep(1,10);
		            }
		            else if(cameraOptions == 2)
		            {
		                Camera.setCameraRotation(rotation); // Change the rotation by a random, small amount
		                sleep(1,10);
		                Camera.setCameraAngle(angle);
		                if(General.random(1, 10) > 7)
		                {
		                    Camera.setCameraRotation(General.random(-5, 5));
		                }
		            }
		            else if(cameraOptions == 3)
		            {
		                Camera.setCameraAngle(angle);          // Change both camera settings by a small amount
		                sleep(10,20);
		                Camera.setCameraRotation(rotation);
		                sleep(1,10);
		            }
		        }
		        else if(options == 3) // mouse move option
		        {
		            Mouse.setSpeed(General.random(160,200));
		            Mouse.move(x, y);
		            int r1 = General.random(1, 3);
		            if(r1 == 1)
		            {
		                Mouse.setSpeed(General.random(160,200));
		                Mouse.move(x, y);
		            }
		            Mouse.setSpeed(speed);
		        }
		        else if(options == 4) // Hybrid option
		        {
		            int hOptions = General.random(1, 3);
		            
		            if(hOptions == 1)
		            {
		                // Do nothing
		            }
		            else if(hOptions == 2)
		            {
		                int rotation2 = General.random(-20, 20); // Random rotation
		                int angle2 = General.random(-10, 15);  // Random angle
		                int rotation3 = General.random(-10, 10);
		                
		                Camera.setCameraRotation(rotation);
		                sleep(1,5);
		                Camera.setCameraAngle(angle2);                     // Simulates the user "playing" with the arrow keys
		                sleep(1,5);
		                Camera.setCameraRotation(rotation2);
		                sleep(1,5);
		                Camera.setCameraRotation(rotation3);
		                
		                if(rotation2 > 10) // Small chance
		                {
		                    int x2 = General.random(1,760); 
		                    int y2 = General.random(1, 500); 
		                    int x3 = General.random(1,760); 
		                    int y3 = General.random(1, 500); 
		                    Mouse.setSpeed(General.random(170,200));  
		                    Mouse.move(x, y);                           // move mouse randomly a couple times fastly
		                    Mouse.setSpeed(General.random(170,200));
		                    Mouse.move(x2, y2);
		                    if(Mouse.getSpeed() > 190) 
		                    {
		                        Mouse.setSpeed(General.random(150,165));
		                        Mouse.move(x3, y3);
		                    }
		                    Mouse.setSpeed(speed);
		                    
		                }
		                
		            }
		            else if(hOptions == 3)
		            {
		                Camera.setCameraAngle(angle);
		                sleep(1,10);
		                Camera.setCameraRotation(rotation);
		                if(rotation > 10)
		                {
		                    int angle2 = General.random(-10, 15);
		                    Camera.setCameraAngle(angle2);
		                    if(angle2 > 10)
		                    {
		                        int rotation2 = General.random(-20, 20); // Random rotation
		                        int angle3 = General.random(-10, 15);
		                        Camera.setCameraAngle(angle3);
		                        sleep(1,10);
		                        Camera.setCameraRotation(rotation2);
		                    }
		                }
		            }
		        }
		        else if(options == 5) // Another Hybrid option
		        {
		            Camera.setCameraAngle(angle);
		            sleep(1,10);
		            Mouse.setSpeed(General.random(170, 200));
		            Mouse.move(x, y);
		            if(angle > 5)
		            {
		                Camera.setCameraAngle(General.random(-20, 0));
		                sleep(1,10);
		                Camera.setCameraRotation(General.random(-10, 10));
		                if(General.random(-10, 10) > 0)
		                {
		                    Mouse.setSpeed(General.random(170, 200));
		                    Mouse.move(x, y);
		                    Mouse.setSpeed(speed);
		                }
		            }
		            
		        }
		        
		        Mouse.setSpeed(speed);  // Just in case I forgot to change it back in the previous code
		        sleep(10,200); 
		    }
			
		  
			public static class ItemPrice {
		        private static final String LINK = "http://forums.zybez.net/pages/2007-price-guide?id=";
		        private static final String LINK_NAME = "http://forums.zybez.net/index.php?app=priceguide&module=public&section=search&term=";
		        private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17";

		        private final static Pattern NAME = Pattern
		                .compile("Price Guide: (.+?)</h1>");
		        private final static Pattern PRICE = Pattern
		                .compile("Recent Trade Price: ~(.+?) GP");

		        private final String name;
		        private static int id;
		        private static int price;

		        private ItemPrice(final String name, final int id, final int price) {
		            this.name = name;
		            this.id = id;
		            this.price = price;
		        }

		        public String getName() {
		            return name;
		        }

		        public int getId() {
		            return id;
		        }

		        public int getPrice() {
		            return price;
		        }

		        public final static int getID(final String name) {
		            try {
		                final BufferedReader br = getReaderName(name
		                        .replace(" ", "%20"));

		                String inputLine;
		                inputLine = br.readLine();
		                br.close();
		                String[] broken = inputLine.split("}");
		                for (String cur : broken) {
		                    String[] current = cur.replace("[", "").replace("{", "")
		                            .replace("}", "").replace("]", "")
		                            .replace("\"", "").split(",");
		                    System.out.println("d: " + current[0]);
		                    if (current[0].replace("label:", "").equals(name)) {
		                        System.out.println("Try: "
		                                + current[1].replace("value:", ""));
		                        return Integer
		                                .valueOf(current[1].replace("value:", ""));
		                    }
		                }

		                return 0;
		            } catch (IOException e) {
		                return -1;
		            }
		        }

		        public final static ItemPrice lookup(final String name) {
		            return lookup(getID(name));
		        }

		        public final static ItemPrice lookup(final int id) {
		            try {
		                final BufferedReader br = getReader(id);
		                br.skip(18700);

		                Matcher m = NAME.matcher(br.readLine());
		                String name = "";
		                if (m.find()) {
		                    name = m.group(1);
		                }

		                String line = "";
		                while (!line.contains("Recent Trade Price")) {
		                    line = br.readLine();
		                }

		                m = PRICE.matcher(line);
		                int price = 0;
		                if (m.find()) {
		                    price = Integer.parseInt(m.group(1).replaceAll(",", ""));
		                }
		                return new ItemPrice(name, id, price);
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		            return null;
		        }

		        private static BufferedReader getReader(final int id) {
		            try {
		                final URL url = new URL(LINK + id);
		                final URLConnection con = url.openConnection();
		                con.setRequestProperty("User-Agent", USER_AGENT);
		                final Reader inr = new InputStreamReader(con.getInputStream());
		                return new BufferedReader(inr);
		            } catch (IOException e) {
		                System.out.println("Item not found.");
		            }
		            return null;
		        }

		        private static BufferedReader getReaderName(final String name) {
		            try {
		                final URL url = new URL(LINK_NAME + name);
		                final URLConnection con = url.openConnection();
		                con.setRequestProperty("User-Agent", USER_AGENT);
		                final Reader inr = new InputStreamReader(con.getInputStream());
		                return new BufferedReader(inr);
		            } catch (IOException e) {
		                System.out.println("Item not found.");
		            }
		            return null;
		        }
		    }

		  
		  public void waitToCross() {
			  if ((!(Player.isMoving())) && (Interfaces.get(299, 25) == null)  ) {
			    RSObject[] plank = Objects.findNearest(10, PLANK);
			    if ((plank != null) && (plank.length > 0)) {
			      DynamicClicking.clickRSObject(plank[0], "Cross");
			      sleep(1000, 1500);
			    }
			  }
			}

		  private boolean onEntranaBoat()
		  {
		    RSTile myPos = Player.getPosition();
		    return myPos.distanceTo(this.ENTRANA_BOAT_TILE) < 3;
		  }

		  private boolean onSarimBoat() {
		    RSTile myPos = Player.getPosition();
		    return myPos.distanceTo(this.SARIM_BOAT_TILE) < 3;
		  }

		  private boolean atEntranaPort() {
		    RSTile myPos = Player.getPosition();
		    return myPos.distanceTo(this.ENTRANA_PORT_TILE) < 4;
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
	  
	  private boolean takeBoat() {
		    RSNPC[] boat = NPCs.findNearest(this.MONKS);
		    if ((boat != null) && (boat.length > 0) && 
		      (DynamicClicking.clickRSNPC(boat[0], "Take-boat"))) {
		      sleep(200, 300);
		      return true;
		    }

		    return false;
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
	    

	    
	    private void handleRunTo(RSTile[] path)
	    {
	    	Camera.setCameraAngle(90);
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
	      
	      
	      private boolean hasSlot1() { 
	    	  RSInterfaceChild lp = Interfaces.get(335, 50);
	          if (lp != null) {
	              RSInterfaceComponent[] l = lp.getChildren();
	              if (l != null) {
	                  for (RSInterfaceComponent i : l) {
	                      if (i != null && i.getComponentItem() != -1) {
	                    	  if (i.getComponentStack() >= 26) {
	                    		  if (i.getIndex() == 0) {	                    			  
	                    			  if (i.getComponentItem() == (563) || i.getComponentItem() == (7937)) {
	                    				  //println(i.getComponentName() + " in slot 1.");
	                    				  return true;
	                    				  }	                    			  
	                    		  	}	                    			 
	                    	  }
	                      }
	                  }
	              }
	          }
			return false;
	      }
	      
	      private boolean hasSlot2() {
	    	  RSInterfaceChild lp = Interfaces.get(335, 50);
	          if (lp != null) {
	              RSInterfaceComponent[] l = lp.getChildren();
	              if (l != null) {
	                  for (RSInterfaceComponent i : l) {
	                      if (i != null && i.getComponentItem() != -1) {
	                    	  if (i.getComponentStack() >= 26) {
	                    		  if (i.getIndex() == 1) {	                    			  
	                    			  if (i.getComponentItem() == (563) || i.getComponentItem() == (7937)) {
	                    				  //println(i.getComponentName() + " in slot 2.");
	                    				  return true;
	                    				  }	                    			  
	                    		  	}	                    			 
	                    	  }
	                      }
	                  }
	              }
	          }
			return false;
	      }
	      
	      
	      private boolean checkScreen2() {
	    	  RSInterfaceChild lp = Interfaces.get(334, 40);
	          if (lp != null) {
	              String text = lp.getText();
	              if ((text.contains("Pure essence") && (text.contains("Law rune")))) {
	            	  //if (Integer.parseInt(text.substring(51, 53)) >= 26) {
	            		  //if (Integer.parseInt(text.substring(104, 106)) >= 26) {
	            			  accept();
	            			  return true;
	            		  //}
	            	  //}	            	  	
	              } else {
	            	  decline();
	              }
	          }
			return false;
	      }
	      
	      private boolean checkScreen1() {
				if ((hasSlot1() == true) && (hasSlot2() == true)) {
					status = ("Accepting trade");
					accept();
					return true;
				} else {
					status = ("Declining trade");
					decline();
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
	 	 
	 	   Random generator = new Random();
	 	   
	 		private final RSTile[] deathWalk = {
	 				new RSTile(3222, 3218),
	 				new RSTile(3227, 3218),
	 				new RSTile(3232, 3219),
	 				new RSTile(3234, 3224),
	 				new RSTile(3232, 3229),
	 				new RSTile(3228, 3232),
	 				new RSTile(3224, 3235),
	 				new RSTile(3220, 3238),
	 				new RSTile(3215, 3240),
	 				new RSTile(3210, 3241),
	 				new RSTile(3205, 3241),
	 				new RSTile(3200, 3241),
	 				new RSTile(3196, 3238),
	 				new RSTile(3191, 3237),
	 				new RSTile(3186, 3236),
	 				new RSTile(3181, 3235),
	 				new RSTile(3176, 3235),
	 				new RSTile(3171, 3235),
	 				new RSTile(3166, 3235),
	 				new RSTile(3161, 3234),
	 				new RSTile(3156, 3233),
	 				new RSTile(3151, 3232),
	 				new RSTile(3146, 3230),
	 				new RSTile(3142, 3227),
	 				new RSTile(3137, 3225),
	 				new RSTile(3132, 3223),
	 				new RSTile(3127, 3222),
	 				new RSTile(3122, 3222),
	 				new RSTile(3117, 3223),
	 				new RSTile(3112, 3224),
	 				new RSTile(3108, 3227),
	 				new RSTile(3104, 3230),
	 				new RSTile(3101, 3234),
	 				new RSTile(3099, 3239),
	 				new RSTile(3099, 3244),
	 				new RSTile(3097, 3249),
	 				new RSTile(3092, 3249),
	 				new RSTile(3092, 3244)
	 		};
	 	   
	 		private final RSArea Dead_Area = new RSArea(new RSTile(3189, 3203), 
	 			    new RSTile(3269, 3413));
	 	   
	 	  private boolean isDead() {
			    if (this.Dead_Area.contains(new RSTile[] { Player.getPosition() })) {
			      return true;
			    }
			    return false;
			  }


	
	@Override
	public void run() {
		while (!(Login.getLoginState() == (STATE.INGAME))) {
			Login.login();
		}
		while (true) {			
    		if (isDead()) {
    			status = ("Deathwalk");
    			handleRunTo(deathWalk);
    			
    		}
		int i = generator.nextInt(10);
		Mouse.setSpeed((MouseSpeed + i));
		if (getWindowNum() != 0) {
			status = ("In trade");
				if (Inventory.getCount(ESS) > 0) {
					//Mouse.click(620, 264, 3);
					Mouse.clickBox(614, 306, 627, 294, 3);
					sleep(550);
					ChooseOption.select("Offer-All");
					sleep(1000);
				} else {
					if (recipientHasAccepted()) {
						if (getWindowNum() == 1) {
							checkScreen1();
						}
						if (getWindowNum() == 2) {
							checkScreen2();
						}	
					}					
				}
			}
		
		if (onEntranaBoat() || onSarimBoat()) {
			status = ("Waiting to cross plank.");
			waitToCross();
		}
		
		if ((Inventory.getCount(ESS) == 26) && (getWindowNum() == 0)) {

			if (atSarimAndDraynor()) {
				if (!(atBoat())) {
					status = ("Walking for Entrana");
					handleRunTo(this.bankToMonks);
				} else {
					status = ("Taking boat");
					takeBoat();
					
					
				}			
			} else {
				
				if (atEntrana()) {					
					if (!(atRuins())) {
						status = ("Walking to altar");
						handleRunTo(this.toAltar);						
					} else {
						if (atRuins()) {
							while (!(atTrade())) {
								runToTile(atTrade);
							} 
								status = ("Waiting for open host");
								antiBan();
							
						
						}
					}				
				}			
			}
			
		} else {
			if (!(atSarimAndDraynor()) && (getWindowNum() == 0)) {
				if (!(atEntranaPort())) {
					status = ("Leaving for Draynor");
					handleRunTo(this.toLeave);
				} else {		
					status = ("Taking boat");
				takeBoat();
			}
				
			} else {
				if (!(atBank())&& (getWindowNum() == 0)) {
					status = ("Walking to bank");
					handleRunTo(this.monksToBank);
				} else {
					status = ("Banking");
					if (getWindowNum() == 0) {				
					GameTab.open(TABS.INVENTORY);									
					if (Banking.openBankBanker()) {
						laws = (laws + Inventory.getCount(563));	
						Banking.depositAll();
						if (Banking.withdraw(26, ESS)) {
							Banking.close();
						}
						}
					}
				}
			}
			
			
		}
		sleep(250);
		
		}
	}
	
    private void tradePlayer(String arg0) {
		for (RSPlayer i : Players.findNearest(arg0)) {
			if (i.hover()) {
				Mouse.click(3);
				sleep(300);
				if (ChooseOption.select("Trade with " + arg0)) {
					sleep(500);
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
	if ((atRuins()) && (Inventory.getCount(ESS) == 26) && (getWindowNum() == 0)) {
		if (arg1.contains("Op")) {
			tradePlayer(arg0);
			
		} else {
		if (arg1.contains("/")) {
			tradePlayer(arg0);
		} else {
			if (arg1.contains("26")) {
				tradePlayer(arg0);
		} else {
			if (arg1.contains("27")) {
				tradePlayer(arg0);
			} else {
				if (arg1.contains("28")) {
					tradePlayer(arg0);
				} else {
					if (arg1.contains("29")) {
						tradePlayer(arg0);
					} else {
						if (arg1.contains("30")) {
							tradePlayer(arg0);
						}
					}
				}
				
			}
		}
		}
		}
	}
 }
	
	

	@Override
    public void serverMessageRecieved(String message) {
    }
	
    private String insertCommas(String str) {
        if (str.length() < 4) {
            return str;
        }
        return insertCommas(str.substring(0, str.length() - 3)) + ","
                + str.substring(str.length() - 3, str.length());
    }

	@Override
	public void onPaint(Graphics g1) {
        g1.setColor(Color.white);
        g1.setFont(new Font("Calibri light", Font.PLAIN, 10));
        int y = 40;
        g1.drawString("aLaws: by Aegis", 20, y);
        y = y + 15;
        g1.drawString("Status: " + status, 20, y);
        y = y + 15;
        g1.drawString("Time running: " + Timing.msToString(getRunningTime()),
                20, y);
        y = y + 15;
        g1.drawString("Laws: " + laws, 20, y);
        y = y + 15;
        g1.drawString("Money Made: " + (laws * lawPrice), 20, y);
        y = y + 15;
        double rt = getRunningTime();
        int totalValue = laws * lawPrice;
        int lootPerHour = (int) (totalValue / (rt / 1000 / 60 / 60));
        int runitePerHour = (int) (laws / (rt / 1000 / 60 / 60));
        g1.drawString(
                "Per Hour: " + insertCommas(Integer.toString(runitePerHour))
                        + " laws ("
                        + insertCommas(Integer.toString(lootPerHour)) + " gp)",
                20, y);
        y = y + 15;
    }
	
}