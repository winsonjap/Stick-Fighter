package model;

import java.awt.Color;
import java.util.HashMap;

/**
 * The shop class that have items generated
 * It will get the players in the game and generates
 * items from level 1 to the highest level of player in game
 * @author Winson
 *
 */
public class Shop {
	private HashMap<Item, String> items;

	/**
	 * Contructor that will generate items with given players
	 * @param players The player in game right now
	 */
	public Shop(Hero[] players) {
		items = new HashMap<Item, String>();
		int level;
		if(players[1]!=null && players[1].getLevel() > players[0].getLevel())
			level = players[1].getLevel();
		else
			level = players[0].getLevel();
		for(int lvl=1;lvl<=level;lvl++)
			generateItem(lvl);

	}
	
	/**
	 * Function to generate item with given highest level of player in game
	 * @param level the level of item should be made
	 */
	private void generateItem(int level) {
		Item weapon;
		Item armor;
		Color color;
		if(level <= 5)
			color = Color.green;
		else if(level <= 10)
			color = Color.blue;
		else if(level <= 15)
			color = Color.red;
		else
			color = Color.black;
		if(level%2==0) {
			weapon = new Item("Dagger "+level, level, "weapon", color);
			armor = new Item("Plate "+level, level, "armor", color);
		}
		else {
			weapon = new Item("Saber "+level, level, "weapon", color);
			armor = new Item("Aegis "+level, level, "armor", color);
		}
		items.put(weapon, "weapon");
		items.put(armor, "armor");
	}
	
	/**
	 * Sell item to the Hero 
	 * @param hero - hero that buying the item
	 * @param item - item that is sold
	 * @return true if success, fail if no money
	 */
	public boolean sell(Hero hero, Item item) {
		if(hero.getGold() >= item.getPrice()) {
			hero.addGold(-(item.getPrice()));
			hero.setItem(item, item.getType());
			return true;
		}
		else
			return false;
	}
	
	/**
	 * @return the HashMap<Item,String> items in shop
	 */
	public HashMap<Item, String> getItem() {
		return this.items;
	}

}
