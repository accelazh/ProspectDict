package games.moonLanding;

import javax.swing.JFrame;

public class GameFrame extends JFrame
{
	public GameFrame()
	{
		GamePanel p=null;
		getContentPane().add(p=new GamePanel());
		this.setTitle("Moon Landing");
		pack();
		setLocationRelativeTo(null);
		p.requestFocusInWindow();
		
		setAlwaysOnTop(true);
		setVisible(true);
		
	}
	
	public static void main(String[] args)
	{
		new GameFrame();
		
	}

}
