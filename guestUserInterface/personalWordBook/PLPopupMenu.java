package guestUserInterface.personalWordBook;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class PLPopupMenu extends JPopupMenu
implements ActionListener
{
	public static final Font FONT=new Font("Times", Font.BOLD, 14);
	
	private JMenuItem pause=new JMenuItem("Pause");
	private JMenuItem resume=new JMenuItem("Resume");
	private JMenuItem reset=new JMenuItem("Reset");
	private JCheckBoxMenuItem alwaysOnTop=new JCheckBoxMenuItem("Always On Top");
	private JMenuItem exit=new JMenuItem("Exit");
	
	//speed configure
	private JMenu speed=new JMenu("Speed");
    private JRadioButtonMenuItem[] speedLevels=new JRadioButtonMenuItem[5];
    
    private PLDialog plDialog;
    public PLPopupMenu(PLDialog plDialog)
    {
    	this.plDialog=plDialog;
    	
    	//initialize components
    	ButtonGroup group=new ButtonGroup();
    	for(int i=0;i<speedLevels.length;i++)
    	{
    		speedLevels[i]=new JRadioButtonMenuItem(i+1+"");
    		group.add(speedLevels[i]);
    		speed.add(speedLevels[i]);
    	}
    	
    	speedLevels[0].setSelected(true);
    	    	
    	//set font
    	pause.setFont(FONT);
    	resume.setFont(FONT);
    	reset.setFont(FONT);
    	alwaysOnTop.setFont(FONT);
    	exit.setFont(FONT);
    	
    	speed.setFont(FONT);
    	for(int i=0;i<speedLevels.length;i++)
    	{
    		speedLevels[i].setFont(FONT);
    	}
    	
    	alwaysOnTop.setSelected(true);
    	
    	//add components
    	add(pause);
    	add(resume);
    	add(reset);
    	add(speed);
    	add(new JSeparator());
    	add(alwaysOnTop);
    	add(exit);
    	
    	//add listeners
    	pause.addActionListener(this);
    	resume.addActionListener(this);
    	reset.addActionListener(this);
    	alwaysOnTop.addActionListener(this);
    	exit.addActionListener(this);
    	
    	speed.addActionListener(this);
    	for(int i=0;i<speedLevels.length;i++)
    	{
    		speedLevels[i].addActionListener(this);
    	}
    	
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	if(plDialog==null)
    	{
    		return;
    	}
    	
		if(e.getSource()==pause)
		{
			plDialog.pause();
		}
		
		if(e.getSource()==resume)
		{
			plDialog.resume();
		}
		
		if(e.getSource()==reset)
		{
			plDialog.restart();
		}
		
		if(e.getSource()==alwaysOnTop)
		{
			plDialog.setAlwaysOnTop(alwaysOnTop.isSelected());
		}
		
		if(e.getSource()==exit)
		{
			plDialog.exit();
		}
		
		for(int i=0;i<speedLevels.length;i++)
    	{
    		if(e.getSource()==speedLevels[i])
    		{
    			plDialog.setSpeed(i+1);
    		}
    	}
		
	}

	
}
