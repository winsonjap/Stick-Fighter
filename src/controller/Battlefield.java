package controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;


import model.Hero;
import model.Character;
import model.Item;
import model.Monster;

/**
 * The class that handles the battlefield state
 * It can see all players and monsters and the controller in the battlefield
 * @author Winson
 *
 */
public class Battlefield extends SuperController{
	//contain all monsters and heroes in the battlefield currently
	private HashMap<Character, Boolean> heroList;
	private HashMap<Character, Boolean> monsList;
	private HashMap<Character, Boolean> deadList;
	private HashMap<Character, Point> coorList;
	private HashMap<Character, Point> effectCoor, skillCoor;
	private HashMap<Character, Image> effectList, skillList;
	private String[] skillEffectList;
	private String[] skillShoutList;
	private GameState game;
	private static final String path = "src/images/";
	public Hero hero0, hero1;
	public boolean attack1,attack2,skill1,skill2;
	public int x1,y1,x2,y2;
	private long prev1,prev2;

	/**
	 * The constructor of the battlefield, it will need the current gamestate
	 * It also need level of the dungeon which will affect the monster inside
	 * @param game - the gamestate of the game
	 * @param dungeonLevel - the level for this battlefield
	 */
	public Battlefield(GameState game, int dungeonLevel) {
		initialization(game);
		initializeSkillEffect();
		initializeSkillShout();
		int size = playerInitialization(game);
		for(int i=0;i<(size*2);i++) {
			int j = i+1;
			Monster monster = new Monster("Monster "+j,dungeonLevel);
			monster.currSprite = new ImageIcon(path+"mons1l.png").getImage();
			monster.battleX -= i*20;
			monster.battleY -= i*10;
			monsList.put(monster, false);
			coorList.put(monster, new Point(monster.battleX, monster.battleY));
		}
	}

	private void initializeSkillShout() {
		skillShoutList = new String[5];
		skillShoutList[0] = path+"skillshout0.png";
		skillShoutList[1] = path+"skillshout1.png";
		skillShoutList[2] = path+"skillshout2.png";
		skillShoutList[3] = path+"skillshout3.png";
		skillShoutList[4] = path+"skillshout4.png";
	}

	private void initializeSkillEffect() {
		skillEffectList = new String[5];
		skillEffectList[0] = path+"skilleffect0";
		skillEffectList[1] = path+"skilleffect1";
		skillEffectList[2] = path+"skilleffect2";
		skillEffectList[3] = path+"skilleffect3";
		skillEffectList[4] = path+"skilleffect4";
	}

	/**
	 * Initialize players to the battlefield
	 * @param game the current game it's in
	 * @return the number of player playing
	 */
	private int playerInitialization(GameState game) {
		//get players from gamestate
		Hero[] players = game.getPlayerList();
		int size = 1;
		hero0 = players[0];
		hero1 = players[1];
		this.changeSprite(hero0, "");
		if(players[1] != null) {
			size++;
			this.changeSprite(hero1,"");
		}
		//put hero into hero list
		for(int i=0;i<size;i++) {
			players[i].battleX -= i*5;
			players[i].battleY -= i*10;
			heroList.put(players[i],false);
			coorList.put(players[i],new Point(players[i].battleX, players[i].battleY));
		}
		return size;
	}

	/**
	 * Initialize all variables
	 * @param game the game it's currently in
	 */
	private void initialization(GameState game) {
		//initialization
		heroList = new HashMap<Character, Boolean>();
		monsList = new HashMap<Character, Boolean>();
		deadList = new HashMap<Character, Boolean>();
		this.game = game;
		//coordinates of players
		coorList = new HashMap<Character, Point>();
		//list of coordinates of the effect and the effect
		effectCoor = new HashMap<Character, Point>();
		effectList = new HashMap<Character, Image>();
		//list of coordinates of the skill and the effect
		skillCoor = new HashMap<Character, Point>();
		skillList = new HashMap<Character, Image>();
		//set the background
		background = new ImageIcon(path+"dungeon.png").getImage();
		//play music
		game.view.playSound("battle.wav");
		//for attacking system
		prev1 = System.currentTimeMillis();
		prev2 = System.currentTimeMillis();
		attack1 = false;
		attack2 = false;
		skill1 = false;
		skill2 = false;
	}

	/**
	 * private function to change the given hero sprite
	 * @param hero hero to be changed
	 * @param state state of the hero
	 */
	private void changeSprite(Hero hero, String state) {
		String stick = "stickman";
		if(hero==hero1)
			stick+="2";
		String imagePath;
		if(hero.getArmor()!=null && hero.getWeapon()!=null)
			imagePath = path+stick+"-armwep"+state+".png";
		else if(hero.getArmor()!=null)
			imagePath = path+stick+"-arm"+state+".png";
		else if(hero.getWeapon()!=null)
			imagePath = path+stick+"-wep"+state+".png";
		else {
			if(!state.equals(""))
				imagePath = path+stick+"-"+state+".png";
			else
				imagePath = path+stick+state+".png";
		}
		hero.currSprite = new ImageIcon(imagePath).getImage();
	}

	/**
	 * This function will return the target within 35 pixel in front
	 * of the caller with given direction and type of the caller
	 * @param battleX is the x-coordinate of the caller
	 * @param direction is the direction of the caller
	 * @param type is the type of the caller whether hero or monster
	 * @return HashMap<Character,Boolean> the list of enemies, boolean is true if it's in range
	 */
	public HashMap<Character, Boolean> getTarget(int battleX, int direction, String type) {
		HashMap<Character, Boolean> doList;
		if(type.equals("hero"))
			doList = monsList;
		else
			doList = heroList;

		for(Character enemy : doList.keySet()) {
			int dist = 35;
			if(direction == Character.DIRECTION_LEFT)
				dist *= -1;
			int distanceBetween = enemy.battleX - battleX;
			//condition when direction is to the right
			boolean cond1 = distanceBetween <= dist && dist>=0 && distanceBetween > 0;
			//condition when direction is to the left
			boolean cond2 = distanceBetween >= dist && dist<=-1 && distanceBetween < 0;
			if(cond1 || cond2)
				doList.put(enemy, true);
		}
		if(type.equals("hero"))
			monsList = doList;
		else
			heroList = doList;
		return doList;
	}

	/**
	 * The action when user pressed button, right-left arrow for movement for player 1
	 * a-d for movement player 2, L-player 1 attack, G-player 2 attack
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_D && hero1!=null && (!hero1.isDead()))
			movementAction(hero1,Character.DIRECTION_RIGHT,"run");
		if(e.getKeyCode() == e.VK_A && hero1!=null && (!hero1.isDead()))
			movementAction(hero1,Character.DIRECTION_LEFT,"runl");
		if((!hero0.isDead()) && e.getKeyCode() == e.VK_LEFT)
			movementAction(hero0,Character.DIRECTION_LEFT,"runl");
		if((!hero0.isDead()) && e.getKeyCode() == e.VK_RIGHT)
			movementAction(hero0,Character.DIRECTION_RIGHT,"run");
		if((!hero0.isDead()) && e.getKeyCode() == e.VK_K)
			guardAction(hero0);
		if(e.getKeyCode() == e.VK_F && hero1!=null && (!hero1.isDead()))
			guardAction(hero1);
		if((!hero0.isDead()) && e.getKeyCode() == e.VK_L)
			attackAction(hero0, prev1, attack1, 1);
		if(e.getKeyCode() == e.VK_G && hero1 != null && (!hero1.isDead()))
			attackAction(hero1, prev2, attack2, 2);
		if(e.getKeyCode() == e.VK_O && (!hero0.isDead()))
			skillAction(hero0);
		if(e.getKeyCode() == e.VK_R && hero1 != null && (!hero1.isDead()))
			skillAction(hero1);
	}

	/**
	 * The aaction done when user press skill button (O or R)
	 * @param hero the hero calling the skill
	 */
	private void skillAction(Hero hero) {
		if(hero.getWeapon()!=null && System.currentTimeMillis()-hero.cooldown >= 5000) {
			if(hero == hero0)
				skill1 = true;
			else
				skill2 = true;
			int addition = 10;
			int level = hero.getLevel();
			int index = level/5;
			if(index>4)
				index = 4;
			String paths = skillEffectList[index]+".png";
			int yChecker = 20;
			if(index == 4)
				yChecker = 120;
			this.changeSprite(hero,"atk");
			if(hero.getDirection()==Character.DIRECTION_LEFT) {
				this.changeSprite(hero,"atkl");
				addition = -10;
				paths =  skillEffectList[index]+"l.png";;
			}
			damageEffect(hero,skillShoutList[index], hero.battleX, hero.battleY-70);
			skillList.put(hero, new ImageIcon(paths).getImage());
			skillCoor.put(hero, new Point(hero.battleX+addition,hero.battleY-yChecker));
			hero.cooldown = System.currentTimeMillis();
		}
	}

	/**
	 * Function to be called when hero is guarding
	 * set the guard to true and receive 1/4 damage
	 * @param hero
	 */
	public void guardAction(Hero hero) {
		hero.guardMode(true);
		int direction = hero.getDirection();
		if(direction == Hero.DIRECTION_RIGHT)
			this.changeSprite(hero,"guard");
		else
			this.changeSprite(hero,"guardl");
	}

	/**
	 * The attack action -> it will have the chosen hero to attack andhave the effect
	 * Sound and graphic effect
	 * @param hero the hero to attack
	 * @param prev previous attack Attempt
	 * @param attack change boolean attack
	 */
	private void attackAction(Hero hero, long prev, boolean attack, int player) {
		if(System.currentTimeMillis()-prev>=400 && !attack) {
			game.view.playEffect("attack.wav");
			hero.attack(this);
			if(hero.getDirection()==Character.DIRECTION_RIGHT) {
				this.changeSprite(hero,"atk");
				effectCoor.put(hero, new Point(hero.battleX+30,hero.battleY));
				effectList.put(hero, new ImageIcon(path+"atkeffect.png").getImage());
			}
			else {
				this.changeSprite(hero,"atkl");
				effectCoor.put(hero, new Point(hero.battleX-30,hero.battleY));
				effectList.put(hero, new ImageIcon(path+"atkeffect.png").getImage());
			}
			if(player==1) {
				attack1 = true;
				prev1 = System.currentTimeMillis();
			}
			else {
				attack2 = true;
				prev2 = System.currentTimeMillis();
			}
		}
	}

	/**
	 * action of movement of the chosen hero to the given direction
	 * @param hero The hero to move
	 * @param direction direction it's going
	 * @param spriteType the type for the sprite change
	 */
	private void movementAction(Hero hero, int direction, String spriteType) {
		hero.guardMode(false);
		game.view.playEffect("walk.wav");
		if(direction == Character.DIRECTION_RIGHT) {
			effectCoor.put(hero, new Point(hero.battleX-20, hero.battleY));
			effectList.put(hero, new ImageIcon(path+"walkeffect.png").getImage());
			if(hero.battleX+10 <= 750)
				hero.battleX+=10;
			else
				hero.battleX=750;
		}
		else {
			effectCoor.put(hero, new Point(hero.battleX+20, hero.battleY));
			effectList.put(hero, new ImageIcon(path+"walkeffectl.png").getImage());
			if(hero.battleX-10 >= 0)
				hero.battleX-=10;
			else
				hero.battleX=0;
		}
		coorList.put(hero, new Point(hero.battleX,hero.battleY));
		hero.setDirection(direction);
		this.changeSprite(hero,spriteType);
	}

	/**
	 * The action when key is released is basically set the stickman back to normal
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(hero1!=null && e.getKeyCode() == e.VK_D && (!hero1.isDead()))
			this.changeSprite(hero1,"");
		if(hero1!=null && e.getKeyCode() == e.VK_A && (!hero1.isDead()))
			this.changeSprite(hero1,"");
		if(e.getKeyCode() == e.VK_LEFT && (!hero0.isDead()))
			this.changeSprite(hero0,"");
		if(e.getKeyCode() == e.VK_RIGHT && (!hero0.isDead()))
			this.changeSprite(hero0,"");
		if(e.getKeyCode() == e.VK_L && (!hero0.isDead())) {
			attack1 = false;
			this.changeSprite(hero0,"");
		}
		if(hero1!=null && e.getKeyCode() == e.VK_G && (!hero1.isDead())) {
			attack2 = false;
			this.changeSprite(hero1,"");
		}
		if(e.getKeyCode() == e.VK_O && (!hero0.isDead()))
			this.changeSprite(hero0, "");
		if(hero1!=null && e.getKeyCode() == e.VK_R && (!hero1.isDead()))
			this.changeSprite(hero1, "");
		if((e.getKeyCode()==e.VK_L || e.getKeyCode()==e.VK_LEFT || e.getKeyCode()==e.VK_RIGHT)&&(!hero0.isDead())) {
			effectCoor.remove(hero0);
			effectList.remove(hero0);
		}
		if(hero1!=null &&(e.getKeyCode()==e.VK_A || e.getKeyCode()==e.VK_D || e.getKeyCode()==e.VK_G)&&(!hero1.isDead())) {
			effectCoor.remove(hero1);
			effectList.remove(hero1);
		}
	}

	/**
	 * The movement of AI, it will move when user press buttons as well
	 * IT will randomly follow player 1 or player 2
	 * and it will keep attacking if there're players in front of the monster
	 */
	public synchronized void aiBattle() {
		try {
			for(Character monster : monsList.keySet()) {
				//check if there's player in attack area, if there's found is true and it will now move
				//else it will moving towards the player randomly
				HashMap<Character, Boolean> list = this.getTarget(monster.battleX,monster.getDirection(),"monster");
				boolean found = false;
				for(Character hero : list.keySet()) {
					if(list.get(hero)) {
						this.updateHeroList(monster.attack(this));
						if(monster.currSprite.equals(new ImageIcon(path+"mons1.png").getImage()))
							monster.currSprite = new ImageIcon(path+"mons1-atk.png").getImage();
						else
							monster.currSprite= new ImageIcon(path+"mons1l-atk.png").getImage();
						found = true;
					}
				}
				if(!found)
					moveAI(monster);
			}
		}catch(Exception e) {}
	}

	/**
	 * Moving AI to the hero direction
	 * @param monster the current monster attacking
	 */
	private void moveAI(Character monster) {
		int dist;
		int direction = 0;
		int chosen = 0;
		Hero target = ((Monster)monster).target;
		if(target!=null) {
			if(target.isDead())
				((Monster)monster).target = null;
			else {
				if(target.battleX > monster.battleX)
					direction = 1;
				else
					direction = 0;
			}
		}
		if(((Monster)monster).target==null) {
			//random choosing between player 1 and 2 every time
			if(hero1!=null && (!hero1.isDead()))
				chosen = (int)(Math.random()*2);
			if(!hero0.isDead() && chosen ==0) {
				((Monster)monster).target = hero0;
				dist = Math.abs(hero0.battleX-monster.battleX);
				if(hero0.battleX > monster.battleX && dist > 35)
					direction = 1;
				else if(hero0.battleX < monster.battleX && dist > 35)
					direction = 0;
			}
			else {
				if(hero1==null)
					direction = 0;
				else if(!(hero0.isDead() && hero1.isDead())) {
					((Monster)monster).target = hero1;
					dist = Math.abs(hero1.battleX-monster.battleX);
					if(hero1.battleX > monster.battleX && dist > 35)
						direction = 1;
					else
						direction = 0;
				}
				else
					direction = 0;//default direction if all players are dead
			}
		}
		moveMonster(monster, direction);
	}

	/**
	 * Moving monster location in the battlefield
	 * Updating the sprite as well
	 * @param monster the monster to move
	 * @param direction direction it's going
	 */
	private void moveMonster(Character monster, int direction) {
		//determine the direction of monster path to the player
		if(direction == 1) {
			coorList.put(monster, new Point(monster.battleX+10,monster.battleY));
			monster.battleX+=5;
			monster.currSprite = new ImageIcon(path+"mons1.png").getImage();
			monster.setDirection(Character.DIRECTION_RIGHT);
		}
		else if(direction == 0) {
			coorList.put(monster, new Point(monster.battleX-10,monster.battleY));
			monster.battleX-=5;
			monster.currSprite = new ImageIcon(path+"mons1l.png").getImage();
			monster.setDirection(Character.DIRECTION_LEFT);
		}
	}

	/**
	 * Resetting heroes for every battle
	 * Not healing when players are not dead
	 * @param heal true if all players are dead and need to be healed
	 */
	public void resetHeroes(boolean heal) {
		hero0.battleX = 10;
		hero0.battleY = 295;
		hero0.guardMode(false);
		hero0.cooldown = 0;
		if(hero1!=null) {
			hero1.battleX = 10;
			hero1.battleY = 295;
			hero1.guardMode(false);
			hero0.cooldown = 0;
			if(hero1.isDead() && !heal) {
				int h1 = hero1.getHealth();
				hero1.setCurrHealth(h1/2);
				hero1.setDie(false);
			}
		}
		//win the game but is dead, healed 1/2
		if(hero0.isDead() && !heal) {
			int h0 = hero0.getHealth();
			hero0.setCurrHealth(h0/2);
			hero0.setDie(false);
		}
		if(heal) {
			int health = hero0.getHealth();
			hero0.setCurrHealth(health);
			hero0.setDie(false);
			if(hero1!=null) {
				health = hero1.getHealth();
				hero1.setCurrHealth(health);
				hero1.setDie(false);
			}
		}
	}

	/**
	 * The page to show when player is winning in the battle field
	 * @param g The graphics to show the String on the page
	 */
	public void winSetup(Graphics g) {
		int exp = 0;
		int gold = 0;
		//iterating all dead character, if it's monster then take the gold and exp and add them to players
		for(Character dead : deadList.keySet()) {
			if(dead.getClass() == Monster.class) {
				exp+=((Monster)dead).getExperience();
				gold+= dead.getGold();
			}
		}
		Hero[] hero = game.getPlayerList();
		if(hero[1]!=null) {
			exp/=2;
			gold/=2;
		}
		//it will update the experience and gold of players
		//and also show the string on screen
		game.view.updateExperience(hero[0],1,exp,gold,100,g);
		if(hero[1]!=null)
			game.view.updateExperience(hero[1],2,exp,gold,180,g);
	}

	/**
	 * The setup to be called when all players are dead in the battlefield
	 * @param g The canvas to draw on
	 */
	public void lostSetup(Graphics g) {
		//reducing gold from players and show them on the screen
		game.view.drawLostStatement(g);
		for(Character dead : deadList.keySet()) {
			if(dead.getClass() == Hero.class) {
				int minus = dead.getGold()/10;
				((Hero)dead).addGold(-minus);
			}
		}
	}

	/**
	 * Check where is the skill right now
	 * @param pl indicates player(1 is hero0(player1), 2 is hero1(player2))
	 */
	public void checkSkillLocation(int pl) {
		Hero hero;
		if(pl==1)
			hero = hero0;
		else
			hero = hero1;
		Point coor = skillCoor.get(hero);
		try {
			for(Character monster : monsList.keySet()) {
				if(!monster.isDead()) {
					if(Math.abs(coor.x-monster.battleX)<=10) {
						skillDamage(hero, monster);
						if(hero.getLevel()<=20)
							return;
					}
					else {
						int addition = 10;
						Image img = skillList.get(hero);
						int level = hero.getLevel();
						int index = level/5;
						if(index > 4)
							index = 4;
						if(img.equals(new ImageIcon(skillEffectList[index]+"l.png").getImage()))
							addition = -10;
						skillCoor.put(hero, new Point(coor.x+addition,coor.y));
					}
				}
			}
		} catch(Exception e) {
			return;
		}
	}

	/**
	 * doing Damage from skill from hero to monster
	 * @param hero
	 * @param monster
	 */
	private void skillDamage(Hero hero, Character monster) {
		if(hero==hero0 && hero.getLevel()<=20)
			skill1 = false;
		else if(hero == hero1 && hero.getLevel()<=20)
			skill2 = false;
		if(hero.getLevel() > 20)
			damageEffect(monster,path+"atkeffect2.png",monster.battleX,monster.battleY);
		else
			damageEffect(monster,path+"atkeffect.png",monster.battleX,monster.battleY);
		doDamage(hero, monster);
		if(hero.getLevel() <= 20) {
			skillList.remove(hero);
			skillCoor.remove(hero);
		}
	}

	/**
	 * function to do damage to monster using skills
	 * @param hero
	 * @param monster
	 */
	private void doDamage(Hero hero, Character monster) {
		int damage = hero.getStrength();
		Item weapon = hero.getWeapon();
		int weaponDamage = weapon.getStrength();
		damage = (damage+weaponDamage)*2;
		damage+= hero.getLevel()*5;
		monster.setCurrentDamage(damage);
		int currHealth = monster.getCurrHealth() - damage;
		monster.setCurrHealth(currHealth);
		if(currHealth <= 0) {
			deadList.put(monster, true);
			monsList.remove(monster);
			monster.currSprite = new ImageIcon("src/images/die.png").getImage();
		}
	}

	/**
	 * This is the skill effect shown
	 * @param character the hero calling the effect
	 * @param effect the effect shown
	 * @param x the x-coordinate where it want to be shown
	 * @param y the y-coordinate where it want to be shown
	 */
	private void damageEffect(final Character character, final String effect, final int x, final int y) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				effectList.put(character, new ImageIcon(effect).getImage());
				effectCoor.put(character, new Point(x,y));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {}
				effectList.remove(character);
				effectCoor.remove(character);
			}
		}).start();
	}


	/**
	 * @return hashmap<character, boolean> list of heroes in battlefield
	 */
	public HashMap<Character, Boolean> getHeroList() {
		return heroList;
	}

	/**
	 * @return list of dead characters in this battlefield
	 */
	public HashMap<Character, Boolean> getDeadList() {
		return deadList;
	}

	/**
	 * Update the hero List
	 * @param heroList - the new HashMap that contain new information
	 */
	public void updateHeroList(HashMap<Character, Boolean> heroList) {
		this.heroList = heroList;
	}

	/**
	 * @return hashmap<character, boolean> list of monsters in battlefield
	 */
	public HashMap<Character, Boolean> getMonsterList() {
		return monsList;
	}

	/**
	 * @return the List of coordinates of all characters in the battlefield
	 */
	public HashMap<Character, Point> getCoordList() {
		return this.coorList;
	}

	/**
	 * @return the effect coordinates
	 */
	public HashMap<Character, Point> getEffectCoor() {
		return this.effectCoor;
	}

	/**
	 * @return the effect list
	 */
	public HashMap<Character, Image> getEffectList() {
		return this.effectList;
	}

	/**
	 * @return the skill coordinates
	 */
	public HashMap<Character, Point> getSkillCoor() {
		return this.skillCoor;
	}

	/**
	 * @return the skill list
	 */
	public HashMap<Character, Image> getSkillList() {
		return this.skillList;
	}

	/**
	 * Update the monster List
	 * @param monsList - the new HashMap that contain new information
	 */
	public void updateMonsterList(HashMap<Character, Boolean> monsList) {
		this.monsList = monsList;
	}

	public boolean isGameDone() {
		return monsList.size()==0 || heroList.size()==0;
	}


}
