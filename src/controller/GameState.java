package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import view.MainStateView;

import model.Hero;

/**
 * Main Controller of the whole system
 * It has state of current game is it in battle, city, or map
 * @author Winson
 *
 */
public class GameState extends SuperController implements ActionListener{
	public static final int CITY_STATE = 0;
	public static final int BATTLE_STATE = 1;
	public static final int MAP_STATE = 2;
	//players currently playing in the game
	private Hero[] players;
	private int currentState;
	public int chosenMap;
	public MainStateView view;
	public String[] map;
	public HashMap<String,Point> mapCoor;

	/**
	 * Constructor will make the new hero list
	 * and set the state to 0
	 */
	public GameState() {
		players = new Hero[2];
		currentState = MAP_STATE;
		mapCoor = new HashMap<String,Point>();
		chosenMap = 1;
		map = new String[3];
		view = new MainStateView(this);
		this.setUpMap();
		//background of this state (MAP) and the sprite1 is the choser
		background = new ImageIcon("src/images/mapWorld.png").getImage();
		choser = new ImageIcon("src/images/chosenMap.png").getImage();
		view.playSound("normal.wav");
		view.play();
	}

	/**
	 * This will setup the map, to put positions of each locations
	 * and also the name for that place
	 */
	public void setUpMap() {
		map[0] = "dungeon 1";
		map[1] = "city 1";
		map[2] = "dungeon 5";
		mapCoor.put("dungeon 1", new Point(60,280));
		mapCoor.put("city 1", new Point(230,80));
		mapCoor.put("dungeon 5", new Point(450,250));
	}

	/**
	 * Make new player to the Game
	 * @param name - name of the new hero
	 * @return boolean - true if successfully make new hero, false if party already full
	 */
	public boolean newPlayer(String name) {
		Hero newHero = new Hero(name);
		boolean ret = true;
		if(players[0] == null)
			players[0] = newHero;
		else if(players[1] == null)
			players[1] = newHero;
		else {
			//false when there're already 2 players playing
			ret = false;
		}
		return ret;
	}

	/**
	 * Get list of players currently playing
	 * @return array of hero that're playing
	 */
	public Hero[] getPlayerList() {
		return players;
	}

	/**
	 * Set the player that are plying
	 * @param players array of players that're playing
	 */
	public void setPlayerList(Hero[] players) {
		this.players = players;
	}

	/**
	 * Key Press action, keeping track of the location of chosen
	 * And when user press enter, it will change the state of the game to the chosen one
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(players[0]==null) {
			JOptionPane.showMessageDialog(view.frame, "No Player found!! Create new character or load one");
			return;
		}
		if(e.getKeyCode() == e.VK_LEFT) {
			chosenMap-=1;
			if(chosenMap < 0)
				chosenMap = map.length-1;
		}
		if(e.getKeyCode() == e.VK_RIGHT) {
			chosenMap +=1;
			chosenMap = chosenMap % map.length;
		}
		if(e.getKeyCode() == e.VK_ENTER) {
			String chosen = map[chosenMap];
			String[] tokens = chosen.split(" ");
			if(tokens[0].equals("dungeon")) {
				view.stopSound();
				int lv1 = players[0].getLevel();
				int lv = lv1+3;
				if(players[1]!=null) {
					int lv2 = players[1].getLevel();
					if(lv2 > lv1)
						lv = lv2+3;
				}
				if(Integer.parseInt(tokens[1])==1)
					lv=lv-3;
				view.changeController(new Battlefield(this, lv));
				this.currentState = BATTLE_STATE;
			}
			else {
				view.changeController(new City(this));
				this.currentState = CITY_STATE;
			}
		}
	}

	/**
	 * @return currentState of the game 
	 */
	public int getState() {
		return this.currentState;
	}

	/**
	 * Set the state of the game
	 * @param state
	 */
	public void setState(int state) {
		this.currentState = state;
	}

	/**
	 * This is for the jmenu bar action, to add new player to the game
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("New Player"))
			this.newPlayerAction();
		else if(e.getActionCommand().equals("Save")) {
			String player = this.playerInput();
			if(player==null)
				return;
			String filename = (String)JOptionPane.showInputDialog(view.frame, "Input Save Name you wish");
			if(filename==null)
				return;
			if(filename.trim().equals(""))
				JOptionPane.showMessageDialog(view.frame, "Invalid naming");
			else
				this.saveAction(player,filename);
		}
		else if(e.getActionCommand().equals("Load")) {
			File folder = new File("src/save");
			File[] listOfFiles = folder.listFiles();
			String[] fileNames = new String[listOfFiles.length];
			int j=0;
			for(int i=0;i<listOfFiles.length;i++) {
				if (listOfFiles[i].isFile()) {
					String nm = listOfFiles[i].getName();
					String ext = nm.substring(nm.length() - 4);
					if(ext.equals(".xml"))
						fileNames[j] = nm.substring(0, nm.length() - 4);
					j++;
				}
			}
			String[] files = new String[j];
			for(int k=0;k<j;k++)
				files[k] = fileNames[k];
			this.loadAction(files);
		}
	}

	/**
	 * Loading character
	 * It will show list of save files and ask user to choose which file to load from
	 * It will load from xml files
	 * @param listofFiles is the list of xml files from save folder
	 */
	private void loadAction(String[] listOfFiles) {
		String file = (String)JOptionPane.showInputDialog(
				view.frame,"Which character to load?","Load",JOptionPane.PLAIN_MESSAGE,null,listOfFiles,null);
		if(file==null)
			return;
		XStream xstream = new XStream(new DomDriver());
		Hero hero = (Hero)xstream.fromXML(new File("src/save/"+file+".xml"));
		if(players[0]==null)
			players[0]=hero;
		else if(players[1]==null) {
			if(hero.equals(players[0]))
				JOptionPane.showMessageDialog(view.frame, "Already in Game!!");
			else
				players[1]=hero;
		}
		else
			JOptionPane.showMessageDialog(view.frame, "Full Players!");

	}

	/**
	 * It will ask for player input for which player to save
	 * @return the name of the player
	 */
	private String playerInput() {
		if(players[0] == null) {
			JOptionPane.showMessageDialog(view.frame, "No Player to Save");
			return null;
		}
		String[] names;
		if(players[1]==null) {
			names = new String[1];
			names[0] = players[0].getName();
		}
		else {
			names = new String[2];
			names[0] = players[0].getName();
			names[1] = players[1].getName();
		}
		String player = (String)JOptionPane.showInputDialog(
				view.frame,"Which character to save?","Save",JOptionPane.PLAIN_MESSAGE,null,names,null);
		return player;
	}

	/**
	 * add new player action
	 */
	private void newPlayerAction() {
		String name = (String)JOptionPane.showInputDialog(
				view.frame,
				"Input your character name :",
				"New Character",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"");
		if ((name != null) && (name.length() > 0) && (name.length() <=12)) {
			boolean create = this.newPlayer(name);
			if(!create)
				JOptionPane.showMessageDialog(view.frame, "Full Players!");
		}
	}

	/**
	 * SAve player action
	 * @param playerName the name of player to save
	 * @param filename the name of the file to save
	 */
	private void saveAction(String playerName, String filename) {
		XStream xstream = new XStream(new DomDriver());
		Hero hero;
		if(playerName.equals(players[0].getName()))
			hero = players[0];
		else
			hero = players[1];
		String xml = xstream.toXML(hero);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("src/save/"+filename+".xml"));
			out.write(xml);
			out.close();
		} catch (IOException e) {
			System.out.println("Fail to export file");
		}
	}


	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		new GameState();
	}


}
