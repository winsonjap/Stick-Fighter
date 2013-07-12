package model;

import java.awt.Color;

/**
 * The class that holds item that can be used by hero
 * @author Winson
 *
 */
public class Item {
	//base price for items
	private static final int PRICE_BASE = 150;
	private int strength, defense, level, price;
	private String name, type;
	private Color color;
	
	/**
	 * The Item constructor which will make new item base on given parameters
	 * @param name - name of the new item
	 * @param level - level of the item, status of item will depend on the level
	 * @param type - type of the item is weapon or armor
	 * @param color - color will define the item level
	 */
	public Item(String name, int level, String type, Color color) {
		this.name = name;
		this.level = level;
		this.type = type;
		this.color = color;
		generateItem(level, type);
	}
	
	/**
	 * Private helper that will generate depends on the level and type
	 * @param level - level of the item
	 * @param type - type of the item, whether weapon or armor
	 */
	private void generateItem(int level, String type) {
		// if it's armor, then just add the defense, price is higher than weapon
		if(type=="armor") {
			int armorAdded = 1 + (level - 1);
			this.defense = armorAdded;
			this.strength = 0;
			this.price = 2*PRICE_BASE * level;
		}//if it's weapon then just add strength
		else if(type=="weapon") {
			int strengthAdded = 1 + 2*(level - 1);
			this.strength = strengthAdded;
			this.defense = 0;
			this.price = PRICE_BASE * level;
		}
	}
	
	/**
	 * @return name of the item
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return level of the item
	 */
	public int getLevel() {
		return this.level;
	}
	
	/**
	 * @return type of the item
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * @return color of the item
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * @return price of the item
	 */
	public int getPrice() {
		return this.price;
	}
	
	/**
	 * @return strength of the item
	 */
	public int getStrength() {
		return this.strength;
	}
	
	/**
	 * @return defense of the item
	 */
	public int getDefense() {
		return this.defense;
	}
}
