package view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import controller.Battlefield;
import controller.GameState;
import controller.SuperController;
import controller.City;

import model.Character;
import model.Hero;
import model.Item;
import model.Monster;


/**
 * The main View Class that will paint everything to the JFrame
 * @author Winson
 *
 */
public class MainStateView {
	private Image background;
	private GameState game;
	private KeyAdapter currKeyListener;
	public JFrame frame;
	public ArrayList<JMenuItem> buttonList;
	private JMenuBar menubar;
	private JMenu file;
	private DrawPanel draw;
	private int xChosen, yChosen;
	private int battleCount;
	private Image choser;
	private AudioStream audiostream;
	private long lastAiMovement;


	/**
	 * Constructor of the View
	 * @param game The game it's currently in
	 */
	public MainStateView(GameState game) {
		this.game = game;
		battleCount =0;
		buttonList = new ArrayList<JMenuItem>();
		xChosen = 230;
		yChosen = 80;
		lastAiMovement = 0;
	}

	/**
	 * The function to play the animation, it will forever loops until the game stops
	 * Keep repainting every 25ms
	 */
	public void play() {
		//set up the jframe
		frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw =new DrawPanel();
		frame.getContentPane().add(draw);
		frame.setSize(800,550);
		frame.setVisible(true);
		setupMenuBar();
		frame.setJMenuBar(menubar);
		this.changeController(game);
		while(true)
		{
			if(game.getState() != GameState.BATTLE_STATE) {
				this.checkMapState();
			}
			else {
				if(((Battlefield)currKeyListener).attack1)
					((Battlefield)currKeyListener).attack1 = false;
				if(((Battlefield)currKeyListener).attack2)
					((Battlefield)currKeyListener).attack2 = false;
				if(((Battlefield)currKeyListener).skill1)
					((Battlefield)currKeyListener).checkSkillLocation(1);
				if(((Battlefield)currKeyListener).skill2)
					((Battlefield)currKeyListener).checkSkillLocation(2);
				aiAction();
			}
			draw.repaint();
			try{
				Thread.sleep(25);
			}catch(Exception e)
			{}
		}
	}


	/**
	 * The action taken by AI, it will keep moving after 800ms
	 */
	private void aiAction() {
		if(System.currentTimeMillis()-lastAiMovement>=800) {
			((Battlefield)currKeyListener).aiBattle();
			lastAiMovement = System.currentTimeMillis();
		}

	}

	/**
	 * The setupfor the menu bar
	 */
	private void setupMenuBar() {
		//set up the menu bar
		menubar = new JMenuBar();
		file = new JMenu("File");
		JMenuItem newP = new JMenuItem("New Player");
		newP.addActionListener(game);
		file.add(newP);
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(game);
		file.add(save);
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(game);
		file.add(load);
		buttonList.add(newP);
		buttonList.add(save);
		buttonList.add(load);
	}

	/**
	 * The function to get all the items in maps (except in dungeon)
	 */
	public void checkMapState() {
		int chosen;
		String map;
		HashMap<String, Point> coordinateList;
		if(currKeyListener.getClass()==GameState.class) {
			chosen = ((GameState)currKeyListener).chosenMap;
			map = ((GameState)currKeyListener).map[chosen];
			coordinateList = ((GameState)currKeyListener).mapCoor;
		}
		else {
			chosen = ((City)currKeyListener).chosenMap;
			map = ((City)currKeyListener).map[chosen];
			coordinateList = ((City)currKeyListener).mapCoor;
		}

		Point coordinate = coordinateList.get(map);
		xChosen = coordinate.x;
		yChosen = coordinate.y;
	}

	/**
	 * Function to change controller when changing scene
	 * MAP - CITY - BATTLE
	 * @param controller the current controller of the scene
	 */
	public void changeController(SuperController controller) {
		if(currKeyListener != null)
			frame.removeKeyListener(currKeyListener);
		//change the current controller
		currKeyListener = controller;
		frame.addKeyListener(currKeyListener);
		background = controller.background;
		choser = controller.choser;
		if(controller.getClass() == GameState.class)
			menubar.add(file);
		else
			menubar.remove(file);
	}


	/**
	 * The setup when the state is in battle
	 * @param g Graphics to paint on
	 * @return true when the battle is done and false otherwise
	 * @throws InterruptedException 
	 */
	private boolean battleSetup(Graphics g) {
		HashMap<Character, Boolean> monsList = ((Battlefield)currKeyListener).getMonsterList();
		boolean gameFinish = ((Battlefield)currKeyListener).isGameDone();
		//these 2 conditions are when there're no more monsters/players, then game is done
		if(gameFinish) {
			try {
				Thread.sleep(2000);
				if(monsList.size()==0 && battleCount == 1) {
					((Battlefield)currKeyListener).winSetup(g);
					((Battlefield)currKeyListener).resetHeroes(false);
				}
				else if(battleCount == 1){
					((Battlefield)currKeyListener).lostSetup(g);
					((Battlefield)currKeyListener).resetHeroes(true);
				}
				else if(battleCount < 1)
					drawBattlefield(g);
				battleCount++;
			} catch (InterruptedException e) {}
			return true;
		}
		else {
			drawBattlefield(g);
			return false;
		}

	}

	/**
	 * Drawing the whole battlefield
	 * Draw hero health bar
	 * Draw all the sprites in the battlefield
	 * Draw all the effect in the battlefield
	 * @param g
	 */
	private void drawBattlefield(Graphics g) {
		g.drawImage(background, 0,0, null);
		heroHealthShow(g);
		drawCharacters(g);
		drawEffects(g);
		drawSkill(g);
	}

	/**
	 * draw the effects of walking/attacking
	 * @param g
	 */
	private void drawEffects(Graphics g) {
		HashMap<Character, Point> effectCoor = ((Battlefield)currKeyListener).getEffectCoor();
		HashMap<Character, Image> effectList = ((Battlefield)currKeyListener).getEffectList();
		try { 
			for(Character character : effectCoor.keySet()) {
				Point pos = effectCoor.get(character);
				g.drawImage(effectList.get(character), pos.x, pos.y, null);
			}
		} catch(Exception e){}
	}

	/**
	 * Draw the skills of heroes on battlefield
	 */
	private void drawSkill(Graphics g) {
		HashMap<Character, Point> skillCoor = ((Battlefield)currKeyListener).getSkillCoor();
		HashMap<Character, Image> skillList = ((Battlefield)currKeyListener).getSkillList();
		for(Character character : skillCoor.keySet()) {
			Point pos = skillCoor.get(character);
			g.drawImage(skillList.get(character), pos.x, pos.y, null);
		}
	}

	/**
	 * draw all characters and the monster health bar and also the damage
	 * @param g
	 */
	private void drawCharacters(Graphics g) {
		HashMap<Character, Point> coor = ((Battlefield)currKeyListener).getCoordList();
		int ct = 0;
		for(Character character : coor.keySet()) {
			if(character.getClass() == Monster.class) {
				String ht = "  HP : " + character.getCurrHealth() +" / " + character.getHealth();
				String lv = "  Lvl : " + character.getLevel();
				if(ct<2) {
					g.drawString(character.getName(), 600, 30+30*ct);
					g.drawString(ht, 600, 30+30*ct+10);
					g.drawString(lv, 600, 30+30*ct+20);
				}
				else {
					g.drawString(character.getName(), 700, 30+30*(ct-2));
					g.drawString(ht, 700, 30+30*(ct-2)+10);
					g.drawString(lv, 700, 30+30*(ct-2)+20);
				}
				ct++;
			}
			Image sprite = character.currSprite;
			Point pos = coor.get(character);
			//every character has its current sprite to show its status right now
			g.drawImage(sprite, pos.x, pos.y, null);
			if(character.getCurrentDamage()!=0)
				g.drawString(""+character.getCurrentDamage(),pos.x+20,pos.y-20);
			character.setCurrentDamage(0);
		}
	}

	/**
	 * Showing health of the hero in the battlefield
	 * @param g
	 */
	private void heroHealthShow(Graphics g) {
		Hero hero0 = ((Battlefield)currKeyListener).hero0;
		Hero hero1 = ((Battlefield)currKeyListener).hero1;
		String health0 = "  HP : " + hero0.getCurrHealth() +" / " + hero0.getHealth();
		String lv0 = "  Lvl : "+ hero0.getLevel();
		if(hero1!=null) {
			String health1 = "  HP : " + hero1.getCurrHealth() +" / " + hero1.getHealth();
			String lv1 = "  Lvl : " + hero1.getLevel();
			g.drawString(hero1.getName(),30,90);
			g.drawString(health1, 30, 100);
			g.drawString(lv1, 30, 110);
		}
		g.drawString(hero0.getName(),30,40);
		g.drawString(health0, 30, 50);
		g.drawString(lv0, 30, 60);
	}

	public void drawLostStatement(Graphics g) {
		g.drawString("All Players are dead!! Lost 10% of Gold", 250, 150);
	}

	/**
	 * The function to update the experience and gold and show it to the screen
	 * @param hero The hero to be updated
	 * @param player The player number (either 1 or 2)
	 * @param exp The experience gained
	 * @param gold The gold gained
	 * @param baseY the position of the string on the screen
	 * @param g The image to work on
	 */
	public void updateExperience(Hero hero, int player, int exp, int gold, int baseY, Graphics g) {
		HashMap<String, Integer> status = hero.getStatus();
		int toNextLevel = status.get("expLimit") - status.get("experience");
		g.drawString("Player " + player +" current Exp : " + status.get("experience") 
				+ ". To next level : " + toNextLevel, 250, baseY);
		g.drawString("Player "+ player +" get "+exp+" experience and "+gold+" Gold", 250, baseY+20);
		hero.increaseExperience(exp);
		hero.addGold(gold);
		if(exp >= toNextLevel) {
			g.drawString("Congratulations, player "+player+" has leveled up to level : " + hero.getLevel(), 250, baseY+40);
			this.playEffect("levelup.wav");
		}
	}

	/**
	 * Playing background sound
	 * all songs are not mine -> it's taken from internet from different sources
	 * From ragnarok online bfx
	 * @param song
	 */
	public void playSound(final String song) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					String path = "src/sounds/";
					InputStream in = new FileInputStream(path+song);
					// Create an AudioStream object from the input stream.
					audiostream = new AudioStream(in);         
					AudioPlayer.player.start(audiostream);             
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * stop the music
	 */
	public void stopSound() {
		AudioPlayer.player.stop(audiostream);
	}

	/**
	 * Playing effect sounds in battle, like hitting/running
	 * @param effect
	 */
	public void playEffect(final String effect) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					String path = "src/sounds/";
					InputStream in = new FileInputStream(path+effect);
					// Create an AudioStream object from the input stream.
					AudioStream as = new AudioStream(in);         
					AudioPlayer.player.start(as);             
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	/**
	 * The canvas to draw on
	 * @author Winson
	 *
	 */
	class DrawPanel extends JPanel{
		private long prev = System.currentTimeMillis();
		private int fps = 0;

		public void paintComponent(Graphics g)
		{ 
			super.paintComponent(g);
			Image image = createImage(800, 500);
			Graphics offG = image.getGraphics();

			//true if battle is finish, false otherwise
			boolean battleStat = false;
			//if current redraw in in battleState, then we have to call battleSetup
			if(game.getState() == GameState.BATTLE_STATE)
				battleStat = battleSetup(offG);
			else {
				//else just draw the background and have choser around and heroes statistic
				offG.drawImage(background, 0,0, null);
				offG.drawImage(choser, xChosen,yChosen, null);
				this.drawStatistic(offG);
			}
			//draw FPS
			offG.drawString("FPS : ", 0, 10);
			//just to count on the FPS rate/second
			fps++;
			if(System.currentTimeMillis()- prev >= 1000) {
				prev = System.currentTimeMillis();
				offG.drawString(""+fps, 40,10);
				fps = 0;
			}
			g.drawImage(image, 0,0, null);
			if(battleStat && battleCount == 3)
				AudioPlayer.player.stop(audiostream);
			if(battleStat && battleCount == 3) {
				//this is to show when battle ends             
				battleCount =0;
				game.setState(GameState.MAP_STATE);
				playSound("normal.wav");
				changeController(game);
			}
		}

		private void drawStatistic(Graphics offG) {
			Hero[] heroes = game.getPlayerList();
			int t=0;
			int x= 30;
			while(t<=1 && heroes[t]!=null) {
				int y=50;
				if(t==1)
					x = 600;
				HashMap<String,Integer> stats = heroes[t].getStatus();
				offG.drawString("Player " +(t+1)+ " : ", x, y-10);
				offG.drawString(" " + heroes[t].getName(), x, y);
				offG.drawString(" HP : " + heroes[t].getCurrHealth()+" / "+heroes[t].getHealth(),x,y+10);
				offG.drawString(" Lvl : " + stats.get("level"),x,y+20);
				offG.drawString(" Str : " + stats.get("strength"),x,y+30);
				offG.drawString(" Def : " + stats.get("defense"),x,y+40);
				offG.drawString(" Exp : " + stats.get("experience")+" / " + stats.get("expLimit"),x,y+50);
				offG.drawString(" Gold : " + stats.get("gold"),x,y+60);
				Item weapon = heroes[t].getWeapon();
				if(weapon!=null)
					offG.drawString(" Weapon : STR+"+weapon.getStrength(), x, y+70);
				Item armor = heroes[t].getArmor();
				if(armor!=null)
					offG.drawString(" Armor : DEF+"+armor.getDefense(), x, y+80);
				t++;
			}
		}
	}
}
