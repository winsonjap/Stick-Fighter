package controller;


import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.Hero;
import model.Item;
import model.Shop;

public class City extends SuperController{
	public String[] map;
	public HashMap<String,Point> mapCoor;
	public int chosenMap;
	private GameState game;
	private Shop shop;

	/**
	 * Constructor of city Class, it will generate the shop depending levels
	 * @param game
	 */
	public City(GameState game) {
		mapCoor = new HashMap<String,Point>();
		this.game = game;
		chosenMap = 0;
		map = new String[2];
		this.setUpMap();
		Hero[] players = game.getPlayerList();
		shop = new Shop(players);
		background = new ImageIcon("src/images/city.png").getImage();
		choser = new ImageIcon("src/images/chosen2.png").getImage();
	}

	/**
	 * This will setup the map, to put positions of each locations
	 * and also the name for that place
	 */
	public void setUpMap() {
		map[0] = "heal";
		map[1] = "shop";
		mapCoor.put("heal", new Point(20,170));
		mapCoor.put("shop", new Point(20,290));
	}

	/**
	 * Function to heal the player with 10 gold
	 * @param player the player to be healed
	 * @return true if heal is successful, false otherwise
	 */
	public boolean heal(Hero player) {
		if(player.getGold() <= 10 || player.getCurrHealth()==player.getHealth()) {
			return false;
		}
		else {
			int health = player.getHealth();
			player.setCurrHealth(health);
			player.addGold(-10);
			return true;
		}
	}

	/**
	 * Key Press action, keeping track of the location of chosen
	 * And when user press enter, it will change the state of the game to the chosen one
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_UP) {
			chosenMap-=1;
			if(chosenMap < 0)
				chosenMap = map.length-1;
		}
		if(e.getKeyCode() == e.VK_DOWN) {
			chosenMap +=1;
			chosenMap = chosenMap % map.length;
		}
		if(e.getKeyCode() == e.VK_ENTER) {
			this.chooseAction();
		}
		if(e.getKeyCode() == e.VK_ESCAPE) {
			game.view.changeController(game);
			game.setState(GameState.MAP_STATE) ;
		}
	}

	/**
	 * Choose the chosen place in city
	 * It's either heal/shop
	 */
	private void chooseAction() {
		String chosen = map[chosenMap];
		if(chosen.equals("heal"))
			this.healAct();
		else
			this.shopAct();
	}

	/**
	 * What to do when healing action is chosen
	 * IT will ask which user to heal
	 */
	private void healAct() {
		Hero[] players = game.getPlayerList();
		String[] names;
		if(players[1]==null) {
			names = new String[1];
			names[0] = players[0].getName();
		}
		else {
			names = new String[2];
			names[0] = players[0].getName();
			names[1] = players[1].getName();
		}
		String heal = (String)JOptionPane.showInputDialog(
				game.view.frame,"Heal for 10 gold ?","Heal",JOptionPane.PLAIN_MESSAGE,null,names,null);
		if(heal==null)
			return;
		boolean moneyCheck;
		if(heal.equals(players[0].getName()))
			moneyCheck = this.heal(players[0]);
		else
			moneyCheck = this.heal(players[1]);
		this.moneyChecker(moneyCheck,"Successfully Healing","Fail to heal, health is full or not enough money");
	}

	/**
	 * The shop action when shop is chosen
	 * IT will ask which player want to shop
	 * And then ask which item to buy
	 */
	private void shopAct() {
		Hero[] players = game.getPlayerList();
		String[] names;
		if(players[1]==null) {
			names = new String[1];
			names[0] = players[0].getName();
		}
		else {
			names = new String[2];
			names[0] = players[0].getName();
			names[1] = players[1].getName();
		}
		HashMap<Item, String> items = shop.getItem();
		String[] itemNames = new String[items.size()];
		HashMap<String, Item> itemList = new HashMap<String, Item>();
		int i = 0;
		for(Item item : items.keySet()) {
			String name = item.getName() + " - " + items.get(item);
			name += " : STR+"+item.getStrength()+", DEF+" + item.getDefense();
			name += " -> "+item.getPrice()+"G";
			itemNames[i] = name;
			itemList.put(name, item);
			i++;
		}
		sellPrompt(players, names, itemNames, itemList);
	}

	/**
	 * Ask for user inputs for which player is buying
	 * and which item to buy
	 * @param players the list of players
	 * @param names list of names of players
	 * @param itemNames the list of item names
	 * @param itemList the list of items
	 */
	private void sellPrompt(Hero[] players, String[] names, String[] itemNames,
		HashMap<String, Item> itemList) {
		String buyer = (String)JOptionPane.showInputDialog(
				game.view.frame,"Player that is buying item is :","Shop",JOptionPane.PLAIN_MESSAGE,null,names,null);
		if(buyer==null)
			return;
		String sell = (String)JOptionPane.showInputDialog(
				game.view.frame,"Selling","Shop",JOptionPane.PLAIN_MESSAGE,null,itemNames,null);
		boolean moneyCheck;
		if(sell==null)
			return;
		if(players[0].getName().equals(buyer))
			moneyCheck = shop.sell(players[0], itemList.get(sell));
		else
			moneyCheck = shop.sell(players[1], itemList.get(sell));
		this.moneyChecker(moneyCheck, "Buying success for "+buyer, "Buying failed for "+buyer+" (not enough money!)");
	}
	
	private void moneyChecker(boolean moneyCheck, String succMsg, String failMsg) {
		if(moneyCheck)
			JOptionPane.showMessageDialog(game.view.frame, succMsg);
		else
			JOptionPane.showMessageDialog(game.view.frame, failMsg);
	}


	/**
	 * @return shop
	 */
	public Shop getShop() {
		return this.shop;
	}

}
