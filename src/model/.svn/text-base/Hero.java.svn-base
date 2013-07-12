package model;

import java.util.HashMap;

import controller.Battlefield;

/**
 * Class of the Hero
 * It extends the Character class and contain informations
 * from character class
 * @author Winson
 *
 */
public class Hero extends Character{
	//the y coordinate of the battle field
	private static final int Y_COORDINATE = 295;
	//the attributes base are different from monsters
	private static final int STRENGTH_BASE = 5;
	private static final int HEALTH_BASE = 45;
	private static final int DEFENSE_BASE = 2;
	//there's attribute explimit, which it will increase when increasing level
	private int expLimit;
	public long cooldown;
	//Items that this user hold
	private Item weapon;
	private Item armor;

	/**
	 * Constructor of the Hero class
	 * @param name - name of the hero
	 */
	public Hero(String name) {
		super(name);
		cooldown = 0;
		//build character lv 1 and random attributes
		buildCharacter();
	}
	
	/**
	 * The function to increase experience
	 * It will automatically check if the experience
	 * is enough to level up, and level up when it is enough
	 * @param exp - the experience added
	 * @return 1 if it increase level, 0 otherwise
	 */
	public int increaseExperience(int exp) {
		this.experience = this.experience + exp;
		int ret = 0;
		while(this.experience >= this.expLimit) {
			increaseLevel();
			setDie(false);
			ret = 1;
		}
		return ret;
	}
	
	/**
	 * Function to increase level of the player, it will add the attributes
	 * also updating the new experience limit
	 */
	private void increaseLevel() {
		this.level++;
		int addStrength = STRENGTH_BASE + (int)(Math.random()*5);
		int addHealth = HEALTH_BASE + (int)(Math.random()*15);
		int addDefense = DEFENSE_BASE + (int)(Math.random()*2);
		this.strength += addStrength;
		this.health += addHealth;
		this.currHealth = this.health;
		this.defense += addDefense;	
		this.expLimit = this.expLimit * 3;
	}
	
	/**
	 * the function to get the weapon of this hero
	 * @return weapon
	 */
	public Item getWeapon() {
		return this.weapon;
	}
	
	/**
	 * function to get the armor of the hero
	 * @return armor
	 */
	public Item getArmor() {
		return this.armor;
	}
	
	/**
	 * the function to set the item of this hero
	 * @param the new set of items of this hero
	 */
	public void setItem(Item item, String type) {
		if(type.equals("weapon"))
			weapon = item;
		else if(type.equals("armor"))
			armor = item;
	}
	
	/**
	 * Function to build Character at level 1
	 * It will set random attributes to hero
	 * Set the battle position to default and direction is right
	 * Set all to the base of lv 1
	 */
	@Override
	protected void buildCharacter() {
		int strength = STRENGTH_BASE + (int)(Math.random()*5);
		int health = HEALTH_BASE + (int)(Math.random()*10);
		int defense = DEFENSE_BASE + (int)(Math.random()*2);
		this.battleX = 10;
		this.battleY = Y_COORDINATE;
		this.direction = DIRECTION_RIGHT;
		this.experience = 0;
		this.level = 1;
		this.strength = strength;
		this.health = health;
		this.currHealth = health;
		this.defense = defense;
		this.expLimit = 30;
		this.gold = 200;
	}
	
	/**
	 * The function that will return set of status
	 * @return HashMap<String, Integer> - key is attributes, and integer is the value held
	 */
	@Override
	public HashMap<String,Integer> getStatus() {
		HashMap<String,Integer> status = new HashMap<String,Integer>();
		status.put("strength", this.strength);
		status.put("health", this.health);
		status.put("level", this.level);
		status.put("defense", this.defense);
		status.put("gold", this.gold);
		status.put("experience", this.experience);
		status.put("expLimit", this.expLimit);
		return status;
	}

	/**
	 * The function attack in battlefield
	 * It will check if there's enemy in range, and attack them
	 * @param btlfild is the Battlefield the hero is currently in
	 */
	@Override
	public HashMap<Character, Boolean> attack(Battlefield field) {
		return attackHelper(field,"hero");
	}
	
	
	/**
	 * function to add gold from current gold
	 * @param gold - the gold addition
	 */
	public void addGold(int gold) {
		this.gold += gold;
	}
	
	public boolean equals(Hero hero) {
		boolean ret = false;
		ret = hero.getName().equals(this.name) && hero.getStatus().equals(getStatus());
		return ret;
	}
}
