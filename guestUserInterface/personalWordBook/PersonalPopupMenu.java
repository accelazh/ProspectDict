package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PersonalPopupMenu extends JPopupMenu
implements ActionListener
{
	public static final Font FONT=new Font("Times", Font.BOLD, 14);
	
	private JMenuItem insert=new JMenuItem("Insert New Entry");
	private JMenuItem remove=new JMenuItem("Remove This Entry");
	private JMenuItem topMove=new JMenuItem("Move Top");
	private JMenuItem bottomMove=new JMenuItem("Move Bottom");
	private JCheckBoxMenuItem alwaysOnTop=new JCheckBoxMenuItem("Always On Top"); 
	private JMenuItem exit=new JMenuItem("Exit");
	
	private PersonalWordPanel wordPanel;
	
	public PersonalPopupMenu(PersonalWordPanel wordPanel)
	{
		this.wordPanel=wordPanel;
		
		add(insert);
		add(remove);
		add(topMove);
		add(bottomMove);
		add(new JSeparator());
		add(alwaysOnTop);
		add(exit);
		
		insert.setFont(FONT);
		remove.setFont(FONT);
		topMove.setFont(FONT);
		bottomMove.setFont(FONT);
		alwaysOnTop.setFont(FONT);
		exit.setFont(FONT);
		
		insert.addActionListener(this);
		remove.addActionListener(this);
		topMove.addActionListener(this);
		bottomMove.addActionListener(this);
		alwaysOnTop.addActionListener(this);
		exit.addActionListener(this);

		alwaysOnTop.setSelected(true);
		
		if(wordPanel.getWordDialog()!=null)
		{
			System.out.println("wordDialog always on top set true");
			wordPanel.getWordDialog().setAlwaysOnTop(alwaysOnTop.isSelected());
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(null==wordPanel)
		{
			return;
		}
		
		if(e.getSource()==insert)
		{
			wordPanel.insertEntry();
		}
		
		if(e.getSource()==remove)
		{
			wordPanel.removeEntry();
		}
		
		if(e.getSource()==topMove)
		{
			wordPanel.topMoveEntry();
		}
		
		if(e.getSource()==bottomMove)
		{
			wordPanel.bottomMoveEntry();
		}
		
		if(e.getSource()==alwaysOnTop)
		{
			if(wordPanel.getWordDialog()!=null)
			{
				wordPanel.getWordDialog().setAlwaysOnTop(alwaysOnTop.isSelected());
			}
		}
		
		if(e.getSource()==exit)
		{
			if(wordPanel.getWordDialog()!=null)
			{
				wordPanel.getWordDialog().dispose();
			}
		}
	}

}
