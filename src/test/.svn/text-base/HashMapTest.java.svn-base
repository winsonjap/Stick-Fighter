package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import javax.swing.ImageIcon;

import model.Hero;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HashMapTest {

	private Hero hero1 = new Hero("TES");
	private HashMap<Hero, Boolean> testList = new HashMap<Hero,Boolean>();
	
	public HashMapTest() {
		hero1.currSprite = new ImageIcon("C:\\Users\\Winson\\workspace\\FinalProject\\images\\stickman.png").getImage();
		testList.put(hero1,true);
	}

	@Test
	public void test() {
		hero1.battleX = 5;
		hero1.battleY = 10;
		hero1.currSprite = new ImageIcon("C:\\Users\\Winson\\workspace\\FinalProject\\images\\stickman2.png").getImage();
		assertTrue(testList.get(hero1));
	}

}
