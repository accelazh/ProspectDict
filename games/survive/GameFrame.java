package games.survive;

import javax.swing.JFrame;

public class GameFrame extends JFrame
{
	public GameFrame()
	{
		GamePanel p=null;
		getContentPane().add(p=new GamePanel());
		this.setTitle("Survive");
		this.setFocusable(false);
		p.setFocusable(true);
		p.requestFocusInWindow();
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new GameFrame();
		
	}

}
