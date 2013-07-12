package animationtest;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class EventController extends KeyAdapter implements ActionListener{
	Thread runner;
	public int xim,yim,direction;
	public long lastPressProcessed;
	static Animation g;
	public boolean pl1,pl2,pr1,pr2,pu1,pu2,pd1,pd2,attack;
	public long prev;



	public EventController() {
		g = new Animation(this);
		prev =0;
		direction = 1;
		xim = 0;
		yim = 0;
		pl1 = false;
		pl2 = false;
		pr1 = false;
		pr2 = false;
		pu1 = false;
		pu2 = false;
		pd1 = false;
		pd2 = false;
		attack = false;
		lastPressProcessed = 0;
		
	}

	public static void main(String[] args){
		new EventController();
		g.play();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_DOWN) {
			pd1 = true;
		}
		if(e.getKeyCode() == e.VK_S) {
			pd2 = true;
		}
		if(e.getKeyCode() == e.VK_D) {
			pr2 = true;
			direction = 1;
		}
		if(e.getKeyCode() == e.VK_A) {
			pl2 = true;
			direction = 0;
		}
		if(e.getKeyCode() == e.VK_LEFT) {
			pl1 = true;
			direction = 0;
		}
		if(e.getKeyCode() == e.VK_RIGHT) {
			pr1 = true;
			direction = 1;
		}
		if(e.getKeyCode() == e.VK_L) {
			if(System.currentTimeMillis() - prev > 500) {
				attack = true;
				prev = System.currentTimeMillis();
			}
			//if(System.currentTimeMillis() - lastPressProcessed > 1000) {
			//	g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman-atk.png").getImage();
			//    lastPressProcessed = System.currentTimeMillis();
			// }
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == e.VK_DOWN) {
			pd1 = false;
			g.yim+=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_S) {
			pd2 = false;
			yim+=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_D) {
			pr2 = false;
			xim+=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_A) {
			pl2 = false;
			xim-=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_LEFT) {
			pl1 = false;
			g.xim-=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_RIGHT) {
			pr1 = false;
			g.xim+=1;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();

		}
		if(e.getKeyCode() == e.VK_L) {
			attack = false;
			g.test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();
		}

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}



}
