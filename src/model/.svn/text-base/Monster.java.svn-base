package model;

import java.util.HashMap;

import controller.Battlefield;

/**
 * The Monster class that extends the Character
 * It contain informations of the character class
 * @author Winson
 *
 */
public class Monster extends Character {
	//the y coordinate of the battle field
	private static final int Y_COORDINATE = 270;
	//attributes base are less than the hero with same level
	private static final int STRENGTH_BASE = 2;
	private static final int HEALTH_BASE = 40;
	private static final int DEFENSE_BASE = 1;	
	//gold and exp are to be given to player when player kill this monster
	private static final int GOLD_BASE = 50;
	private static final int EXP_BASE = 20;
	//currently the target of this monster
	public Hero target;

	/**
	 * Constructor of the monster class
	 * It will create the new monster with given name and level
	 * @param name The name of the Monster
	 * @param level The level of the monster
	 */
	public Monster(String name, int level) {
		super(name);
		this.level = level;
		target=null;
		buildCharacter();
	}

	/**
	 * The buildCharacter function that will build the monster
	 * with current level of the monster
	 */
	@Override
	protected void buildCharacter() {
		int strength = (STRENGTH_BASE + (int)(Math.random()*3))*level;
		int health = (HEALTH_BASE + (int)(Math.random()*15))*level;
		int defense = (DEFENSE_BASE + (int)(Math.random()*2))*level;
		int gold = (GOLD_BASE + (int)(Math.random()*20))*level;
		int experience = EXP_BASE * level;
		this.battleX = 600;
		this.battleY = Y_COORDINATE;
		this.direction = DIRECTION_LEFT;
		this.strength = strength;
		this.health = health;
		this.currHealth = health;
		this.defense = defense;
		this.gold = gold;
		this.experience = experience;
	}
	
	/**
	 * @return experience of monster
	 */
	public int getExperience() {
		return this.experience;
	}

	/**
	 * The function to get the attributes of the monster
	 * @return HashMap<String, Integer> The attributes of monster
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
		return status;
	}

	/**
	 * The attack function of the monster
	 * It will call the function helper from super class
	 * @param field The Battlefield it's currently in
	 */
	@Override
	public HashMap<Character, Boolean> attack(Battlefield field) {
		return attackHelper(field,"monster");
	}



}
