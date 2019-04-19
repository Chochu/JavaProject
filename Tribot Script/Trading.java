package scripts;

import java.awt.Color;
import java.util.ArrayList;

import org.tribot.api.General;
import org.tribot.api.Screen;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;

public class Trading {
	public static String getName(){
		if(getFirstTradeInterface() != null){
			String[] s = getFirstTradeInterface().getChild(16).getText().split(" ");
			String ret = "";
			for(int i = 2; i < s.length; i++){
				ret = ret + s[i];
				if(i != s.length - 1){
					ret = ret + " ";
				}
			}
			return ret;
		}else if(getSecondTradeInterface() != null){
			String[] s = getSecondTradeInterface().getChild(44).getText().split(">");
			return s[1];
		}
		return "";
	}
	public static RSInterfaceMaster getFirstTradeInterface(){
		return Interfaces.get(335);
	}
	public static RSInterfaceMaster getSecondTradeInterface(){
		return Interfaces.get(334);
	}
	public static int getFreeSpace(){
		if(getFirstTradeInterface() != null){
			try{
				return Integer.parseInt(getFirstTradeInterface().getChild(20).getText().replaceAll("[^0-9]",""));
			}catch(NumberFormatException e){}
		}
		return 0;
	}
	public static void withdraw(int itemID, int count){
		if(getFirstTradeInterface() != null &&
				findMyOffer(itemID).length > 0){
			int i = Inventory.getCount(itemID);
			getFirstTradeInterface().getChild(48).getChild(findMyOffer(itemID)[0].getIndex()).hover();
			Mouse.click(3);
			//Offer
			switch(count){
			case 0:
				ChooseOption.select("Remove-All");
				break;
			case 1:
				ChooseOption.select("Remove");
				break;
			case 5:
				ChooseOption.select("Remove-5");
				break;
			case 10:
				ChooseOption.select("Remove-10");
				break;
			default:
				ChooseOption.select("Remove-X");
				long time = System.currentTimeMillis() + General.random(750, 1500);
				while(System.currentTimeMillis() < time &&
						Screen.getColourAt(137, 459).equals(new Color(128,118,96))){
					General.sleep(100);
				}
				if(!Screen.getColourAt(137, 459).equals(new Color(128,118,96))){
					Keyboard.typeSend("" + count);
				}
			}
			//Sleep until you have less in your invevntory.
			long time = System.currentTimeMillis() + General.random(750, 1500);
			while(System.currentTimeMillis() < time &&
					Inventory.getCount(itemID) == i){
				General.sleep(100);
			}
		}
	}
	public static void offer(int itemID, int count){
		if(getFirstTradeInterface() != null){
			if(Inventory.getCount(itemID) != 0){
				//Set number of the item in your inventory
				int i = Inventory.getCount(itemID);
				Inventory.find(itemID)[0].hover();
				Mouse.click(3);
				//Offer
				switch(count){
				case 0:
					ChooseOption.select("Offer-All");
					break;
				case 1:
					ChooseOption.select("Offer");
					break;
				case 5:
					ChooseOption.select("Offer-5");
					break;
				case 10:
					ChooseOption.select("Offer-10");
					break;
				default:
					ChooseOption.select("Offer-X");
					long time = System.currentTimeMillis() + General.random(750, 1500);
					while(System.currentTimeMillis() < time &&
							Screen.getColourAt(137, 459).equals(new Color(128,118,96))){
						General.sleep(100);
					}
					if(!Screen.getColourAt(137, 459).equals(new Color(128,118,96))){
						Keyboard.typeSend("" + count);
					}
				}
				//Sleep until you have less in your invevntory.
				long time = System.currentTimeMillis() + General.random(750, 1500);
				while(System.currentTimeMillis() < time &&
						Inventory.getCount(itemID) == i){
					General.sleep(100);
				}
			}
		}
	}
	public static void accept(){
		if(getFirstTradeInterface() != null &&
				!getFirstTradeInterface().getChild(56).getText().equals("Waiting for other player...")){
			getFirstTradeInterface().getChild(17).click();
			long time = System.currentTimeMillis() + General.random(750, 1500);
			while(System.currentTimeMillis() < time &&
					getFirstTradeInterface() != null &&
					!getFirstTradeInterface().getChild(56).getText().equals("Waiting for other player...")){
				General.sleep(100);
			}
		}else if(getSecondTradeInterface() != null &&
				!getSecondTradeInterface().getChild(33).getText().equals("Waiting for other player...")){
			getSecondTradeInterface().getChild(20).click();
			long time = System.currentTimeMillis() + General.random(750, 1500);
			while(System.currentTimeMillis() < time &&
					getSecondTradeInterface() != null &&
					!getSecondTradeInterface().getChild(33).getText().equals("Waiting for other player...")){
				General.sleep(100);
			}
		}
	}
	public static void decline(){
		if(getFirstTradeInterface() != null){
			getFirstTradeInterface().getChild(55).click();
			long time = System.currentTimeMillis() + General.random(750, 1500);
			while(System.currentTimeMillis() < time &&
					getFirstTradeInterface() != null &&
					!getFirstTradeInterface().getChild(56).getText().equals("Waiting for other player...")){
				General.sleep(100);
			}
		}else if(getSecondTradeInterface() != null){
			getSecondTradeInterface().getChild(20).click();
			long time = System.currentTimeMillis() + General.random(750, 1500);
			while(System.currentTimeMillis() < time &&
					getSecondTradeInterface() != null){
				General.sleep(100);
			}
		}
	}
	public static RSItem[] findMyOffer(int itemID){
		int size = 0;
		RSItem[] ret = new RSItem[0];
		for(RSItem i :getMyOffer()){
			if(i.getID() == itemID){
				size++;
			}
		}
		try{
			ret = new RSItem[size];
		}catch(ArrayIndexOutOfBoundsException e){}
		for(int i = 0; i < ret.length; i++){
			try{
				if(getMyOffer()[i].getID() == itemID){
					ret[i] = getMyOffer()[i];
				}
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		return ret;
	}
	public static RSItem[] findOffer(int itemID){
		int size = 0;
		for(RSItem i :getOffer()){
			if(i.getID() == itemID){
				size++;
			}
		}
		RSItem[] ret = new RSItem[size];
		for(int i = 0; i < ret.length; i++){
			try{
				if(getOffer()[i].getID() == itemID){
					ret[i] = getOffer()[i];
				}
			}catch(ArrayIndexOutOfBoundsException e){}
		}
		return ret;
	}
	public static int getCount(int itemID){
		int i = 0;
		for(RSItem item : getOffer()){
			if(item.getID() == itemID){
				i++;
			}
		}
		return i;
	}
	private static RSItem[] getMyOffer(){
		RSItem[] ret = new RSItem[0];
		if(Interfaces.get(335, 48) != null){
			ArrayList<RSItem> list = new ArrayList <RSItem>();
			for(RSInterfaceComponent i : Interfaces.get(335, 48).getChildren()){
				if(i.getComponentItem() != -1){
					list.add(new RSItem(i.getIndex(),i.getComponentItem(),i.getComponentStack(), RSItem.TYPE.BANK));
				}
			}
			ret = list.toArray(new RSItem[list.size()]);
		}
		return ret;
	}
	private static RSItem[] getOffer(){
		RSItem[] ret = new RSItem[0];
		if(Interfaces.get(335, 50) != null){
			ArrayList<RSItem> list = new ArrayList <RSItem>();
			for(RSInterfaceComponent i : Interfaces.get(335, 50).getChildren()){
				if(i.getComponentItem() != -1){
					list.add(new RSItem(i.getIndex(),i.getComponentItem(),i.getComponentStack(), RSItem.TYPE.BANK));
				}
			}
			ret = list.toArray(new RSItem[list.size()]);
		}
		return ret;
	}
}