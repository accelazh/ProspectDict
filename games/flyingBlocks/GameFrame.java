package games.flyingBlocks;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
	public GameFrame()
	{
		this.setTitle("Flying Blocks");
		GamePanel p=null;
        this.getContentPane().add(p=new GamePanel());
        p.requestFocusInWindow();
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        
        this.setVisible(true);
        
	}
	
	//test
	public static void main(String[] args)
	{
		new GameFrame();
	}

}
