package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import model.Hero;
import model.Item;
import model.Shop;

import org.junit.Test;

public class ItemShopTest {

	/**
	 * Testing the weapon construction
	 */
	@Test
	public void WeaponConstructorTest() {
		Item testItem = new Item("Saber",3,"weapon",Color.BLUE);
		assertTrue(testItem.getStrength()==5);
		assertTrue(testItem.getDefense()==0);
		assertTrue(testItem.getPrice()==450);
	}

	/**
	 * Testing the armor construction
	 */
	@Test
	public void ArmorConstructorTest() {
		Item testItem = new Item("Plate",4,"armor",Color.RED);
		assertTrue(testItem.getStrength()==0);
		assertTrue(testItem.getDefense()==4);
		assertTrue(testItem.getPrice()==1200);
	}
	
	/**
	 * Testing the shop construction
	 * it will auto generate items
	 */
	@Test
	public void ShopConstructorTest() {
		Hero hero1 = new Hero("hero1");
		Hero hero2 = new Hero("hero2");
		Hero[] heroes = new Hero[2];
		heroes[0] = hero1;
		heroes[1] = hero2;
		Shop shopTest = new Shop(heroes);
		HashMap<Item, String> items = shopTest.getItem();
		for(Item item : items.keySet()) {
			assertTrue(item.getColor() == Color.GREEN);
			assertTrue((item.getName()).equals("Saber 1") || (item.getName()).equals("Aegis 1"));
			assertTrue(item.getLevel() == 1);
		}
		hero1.increaseExperience(2430);
		hero2.increaseExperience(2430);
		heroes[0] = hero1;
		heroes[1] = hero2;
		shopTest = new Shop(heroes);
		items = shopTest.getItem();
		for(Item item : items.keySet()) {
			if(item.getLevel() == 6) {
				assertTrue(item.getColor() == Color.BLUE);
				assertTrue((item.getName()).equals("Dagger 6") || (item.getName()).equals("Plate 6"));
			}
		}
	}
}
