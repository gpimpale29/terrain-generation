package tester;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JFrame;

import fastnoise.FastNoise;
import worldUtils.ShortMap;
import worldUtils.WorldUtils;

@SuppressWarnings("serial")
public class Main
{
	
	public static void main(String[] args) throws InterruptedException 
	{
		FastNoise n = new FastNoise();
		
		
		ShortMap sh = WorldUtils.perlinNoise(n, 300, 300);
		
	//	WorldUtils.scale(sh, 10, 0, 0, sh.getXSize(), sh.getYSize());
		
		JFrame frame = new JFrame("test");
		Canvas canvas = new Canvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,300);
		frame.add(canvas);
		frame.setVisible(true);
		
		Graphics2D g2d = (Graphics2D) canvas.getGraphics();
		while(true)
		{
			g2d.drawImage(sh.getImage(), 0, 0, null);
			Thread.sleep(100);
		}
	}

}

