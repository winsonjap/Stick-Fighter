package animationtest;

import javax.swing.*;

import java.awt.*;

public class Animation
{
	int xim=0;
	int yim=0;
	public EventController anime;
	public Image test2,test3;
	
	public Animation(EventController anime) {
		this.anime = anime;
		test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();
		test3 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman.png").getImage();
	}
	
	public static void main(String[] args)
	{
		System.out.println("TESTS");
		//Animation gui=new Animation();
		//gui.play();
	}
	public void play()
	{
		JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DrawPanel draw=new DrawPanel();
		frame.getContentPane().add(draw);
		frame.setSize(500,500);
		frame.setVisible(true);
		frame.addKeyListener(anime);
		while(true)
		{
			if(anime.pd1) {
				yim+=5;
				anime.pd1 = false;
			}
			if(anime.pd2) {
				anime.yim+=5;
				anime.pd2 = false;
			}
			if(anime.pl1) {
				xim-=5;
				anime.pl1 = false;
				test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman-run2.png").getImage();
			}
			if(anime.pl2) {
				anime.xim-=5;
				anime.pl2 = false;
			}
			if(anime.pr1) {
				xim+=5;
				anime.pr1 = false;
				test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman-run.png").getImage();
			}
			if(anime.pr2) {
				anime.xim+=5;
				anime.pr2 = false;
			}
			if(anime.attack) {
				if(anime.direction == 1)
					test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman-atk.png").getImage();
				else
					test2 = new ImageIcon("D:\\Users\\Winson\\Desktop\\stickman-atkl.png").getImage();
			}
			
			draw.repaint();  //tells the panel to redraw itself so we can see the circle in new location
			try{
				Thread.sleep(25);
			}catch(Exception e)
			{}

			
		}
	}

	class DrawPanel extends JPanel{
		public void paintComponent(Graphics g)
		{ 
			super.paintComponent(g);
			Image image = createImage(800, 500);
	        Graphics offG = image.getGraphics();
			Image testbg = new ImageIcon("D:\\Users\\Winson\\Desktop\\bg.jpg").getImage();
	        offG.drawImage(testbg, 0,0, null);
			offG.drawImage(test2, xim,yim, null);
	        offG.drawImage(test3, anime.xim,anime.yim, null);
	        g.drawImage(image, 0,0, null);
		}
	}
}
