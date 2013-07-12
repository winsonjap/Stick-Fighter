package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import model.Hero;
import model.Character;
import model.Monster;

import org.junit.BeforeClass;
import org.junit.Test;

import controller.Battlefield;
import controller.GameState;

/**
 * Test unit that test the Character class, Hero Class, and Monster class
 * @author Winson
 *
 */
public class CharacterTest {
	public static Battlefield field;
	public static GameState test ;
	public static Hero testHero;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		test = new GameState();
		test.newPlayer("hero1");
		field = new Battlefield(test,4);
	}

	/**
	 * Testing the constructor of Hero Constructor
	 */
	@Test
	public void constructorTest() {
		testHero = new Hero("TestName");
		HashMap<String,Integer> status = testHero.getStatus();
		assertTrue(status.get("strength") >=5 && status.get("strength") <=10);
		assertTrue(status.get("defense") >=2 && status.get("defense") <=4);
		assertTrue(status.get("health") >=15 && status.get("health") <=25);
		assertTrue(status.get("experience")==0);
		assertTrue(status.get("expLimit")==30);
		assertTrue(status.get("expLimit")==30);
		assertTrue(status.get("gold")==200);
		assertTrue(status.get("level")==1);
		assertTrue(testHero.getCurrHealth() == status.get("health"));
		assertTrue(testHero.getDirection() == Hero.DIRECTION_RIGHT);
		assertTrue(!testHero.isGuarding());
	}
	
	/**
	 * Test of increasing experience at hero
	 * If it will get correct attributes
	 */
	@Test
	public void increaseExperienceTest() {
		int health = testHero.getHealth();
		testHero.setCurrHealth(health/2);
		testHero.increaseExperience(30);
		assertTrue(testHero.getLevel()==2);
		HashMap<String,Integer> status = testHero.getStatus();
		assertTrue(status.get("experience") == 30);
		assertTrue(status.get("expLimit") == 90);
		assertTrue(status.get("strength") >=10 && status.get("strength") <=20);
		assertTrue(status.get("defense") >=4 && status.get("defense") <=8);
		assertTrue(status.get("health") >=30 && status.get("health") <=50);
		assertTrue(testHero.getCurrHealth() == status.get("health"));
	}

	/**
	 * Testing the attack function in hero Class
	 * IF the damage dealt is correct, etc
	 */
	@Test
	public void attackTest() {
		
		Hero[] players = test.getPlayerList();
		Hero hero1 = players[0];
		hero1.increaseExperience(510);
		HashMap<String,Integer> status = hero1.getStatus();
		System.out.println("Level : " + status.get("level"));
		System.out.println("Health : " + hero1.getHealth());
		System.out.println("Attack : " + status.get("strength"));
		HashMap<Character, Boolean> monsList = field.getMonsterList();
		for(Character monster : monsList.keySet()) {
			monster.battleX = 30;
			System.out.println("Monster " + monster.getName()+" max hp = " + monster.getCurrHealth());
			System.out.println("Defense : " + monster.getDefense());
		}
		System.out.println();
		field.updateMonsterList(hero1.attack(field));
		Monster randMons = new Monster("random",3);
		for(Character monster : monsList.keySet()) {
			randMons = (Monster)monster;
			assertTrue(monster.getHealth()-(hero1.getStrength()-monster.getDefense()) == monster.getCurrHealth());
			System.out.println(monster.getName() + " curr hp = " + monster.getCurrHealth());
			System.out.println("Attack : " + monster.getStrength());
		}
		field.updateHeroList(randMons.attack(field));
		int damage = randMons.getStrength() - hero1.getDefense();
		int eq = hero1.getArmor().getDefense();
		damage -= eq;
		assertTrue(hero1.getCurrHealth() == hero1.getHealth()-damage);
		System.out.println("Hero curr Health : " + hero1.getCurrHealth());
		System.out.println("Defense : " + hero1.getDefense());
	}
	
	/**
	 * Test to check the dead status of the hero
	 */
	@Test
	public void DieTest() {
		Hero hero1 = new Hero("HeroTest");
		hero1.setCurrHealth(-1);
		assertTrue(hero1.isDead());
		hero1.setDie(false);
		hero1.setCurrHealth(1);
		assertTrue(!hero1.isDead());
		hero1.setCurrHealth(0);
		assertTrue(hero1.isDead());		
	}

}
