package model;

import java.awt.Image;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import javax.swing.ImageIcon;

import controller.Battlefield;


/**
 * Super class of all characters in the game (hero and monster)
 * It will contain the general methods and status of the characters
 * @author Winson
 *
 */
public abstract class Character {
	//define direction whether it's seeing right or left
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_LEFT = 0;
	//the position in battlefield
	public int battleX, battleY;
	//attributes of the characters
	protected int strength,health,level,defense, gold, direction, experience, currHealth;
	public Image currSprite;
	//boolean if the character is guarding or not, if it's alive or not in the battle
	private boolean guard, die;
	//name of this character
	protected String name;
	protected int currentDamage;

	/**
	 * The constructor will define the name and set die and guarding as false
	 * @param name - name of the character
	 */
	public Character(String name) {
		this.name = name;
		this.die = false;
		this.guardMode(false);
		currentDamage = 0;
	}

	/**
	 * The attackHelper that is the main function of attack
	 * Refactor of both attack from Hero and Monster
	 * @param field the battlefield
	 * @param type the caller -> hero / monster
	 * @return HashMap<Character,Boolean> new updated one
	 */
	public HashMap<Character, Boolean> attackHelper(Battlefield field, String type) {
		//change guardmode back to normal
		this.guardMode(false);
		HashMap<Character, Boolean> targetArea = field.getTarget(this.battleX, this.getDirection(), type);
		if(targetArea.size() == 0)
			return targetArea;
		try {
			for (Character enemy : targetArea.keySet()) {
				boolean isInrange = targetArea.get(enemy);
				//get enemy in range
				if(isInrange) {
					int damage = calculateDamage(type, enemy);
					enemy.currentDamage = damage;
					int currHealth = enemy.getCurrHealth() - damage;
					System.out.println("NOW : " + currHealth);
					enemy.setCurrHealth(currHealth);
					if(currHealth <= 0) {
						//update dead list
						HashMap<Character, Boolean> deadList = field.getDeadList();
						if(enemy.getClass()==Hero.class) {
							HashMap<Character, Point> effectCoor = field.getEffectCoor();
							HashMap<Character, Image> effectList = field.getEffectList();
							effectCoor.remove(enemy);
							effectList.remove(enemy);
						}
						deadList.put(enemy, true);
						targetArea.remove(enemy);
						enemy.currSprite = new ImageIcon("src/images/die.png").getImage();
					}
					else
						targetArea.put(enemy, false);
				}
			}
		}catch(ConcurrentModificationException e) {
			return targetArea;
		}

		//return the new updated enemy list
		return targetArea;
	}

	/**
	 * @return the currentDamage
	 */
	public int getCurrentDamage() {
		return currentDamage;
	}

	/**
	 * @param currentDamage the currentDamage to set
	 */
	public void setCurrentDamage(int currentDamage) {
		this.currentDamage = currentDamage;
	}

	/**
	 * Calculate the damage inflicted to the enemy
	 * @param type of attack caller
	 * @param enemy the enemy that's inflicted by the damage
	 * @return the damage inflicted
	 */
	private int calculateDamage(String type, Character enemy) {
		//calculation of damage
		int damage = this.getStrength();
		//reduction damage by enemy defense
		damage -= enemy.getDefense();
		Item[] item = new Item[2];
		if(type.equals("hero")) {
			item[0] = ((Hero)this).getArmor();
			item[1] = ((Hero)this).getWeapon();
		}
		else {
			item[0] = ((Hero)enemy).getArmor();
			item[1] = ((Hero)enemy).getWeapon();
		}
		if(type.equals("monster") && item[0]!=null)
			damage -= item[0].getDefense();
		else if(type.equals("hero") && item[1]!=null)
			damage += item[1].getStrength();
		//addition by weapon
		if(enemy.isGuarding())
			damage /= 4;
		System.out.println("attacker  : " + type);
		System.out.println("CURRHP : "+enemy.getCurrHealth());
		if(damage <= 0)
			damage = 1;
		System.out.println("DAmage : "+damage);
		return damage;
	}

	/**
	 * functions to be overridden by subclasses
	 */
	protected abstract void buildCharacter();
	public abstract HashMap<String,Integer> getStatus();
	public abstract HashMap<Character, Boolean> attack(Battlefield field);

	/**
	 * @return strength of the character
	 */
	public int getStrength() {
		return this.strength;
	}

	/**
	 * @return health of the character
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * @return direction of the character
	 */
	public int getDirection() {
		return this.direction;
	}


	/**
	 * Set direction, whether DIRECTION_LEFT or DIRECTION_RIGHT in the battlefield
	 * @param direction - 0 is left, and 1 is right
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return currHealth of the character
	 */
	public int getCurrHealth() {
		return this.currHealth;
	}

	/**
	 * Set current health in the battle field
	 * If curr is  <=0 then the character is currently dead
	 * @param curr The current health of the character
	 */
	public void setCurrHealth(int curr) {
		if(curr <= 0) {
			setDie(true);
			this.currHealth = 0;
		}
		else
			this.currHealth = curr;
	}

	/**
	 * @return guard status of the character
	 */
	public boolean isGuarding() {
		return this.guard;
	}

	/**
	 * @return strength of the character
	 */
	public void guardMode(boolean guard) {
		this.guard = guard;
	}

	/**
	 * @return name of the character
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return level of the character
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * @return defense of the character
	 */
	public int getDefense() {
		return this.defense;
	}

	/**
	 * @return gold of the character
	 */
	public int getGold() {
		return this.gold;
	}

	/**
	 * @return true if character is dead, false otherwise
	 */
	public boolean isDead() {
		return this.die;
	}

	/**
	 * Set character to be dead/alive
	 * @param dead - true->die and false->alive
	 */
	public void setDie(boolean dead) {
		this.die = dead;
	}


}
