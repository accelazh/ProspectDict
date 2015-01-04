package games.survive;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ConfigDialog extends JDialog
{
	private GamePanel gamePanel;
	private ConfigPanel configPanel;
	
	public ConfigDialog(GamePanel gamePanel)
	{
		this.gamePanel=gamePanel;
		this.configPanel=new ConfigPanel(gamePanel, this);
		
		this.getContentPane().add(configPanel);
		
		this.setTitle("Configure Dialog");
		this.setSize(350,200);
		this.setLocationRelativeTo(gamePanel);
	    this.setResizable(false);
	    this.setAlwaysOnTop(true);
		
		this.setVisible(true);
		
	}

}
