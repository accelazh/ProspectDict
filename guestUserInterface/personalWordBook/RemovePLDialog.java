package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import settings.*;

import guestUserInterface.*;
import libraryManager.*;
import libraryInterface.*;

import javax.swing.border.*;

public class RemovePLDialog extends MyOptionDialog
{
	public static final Font FONT=new Font("Times", Font.BOLD, 14);
	
	private JLabel info=new JLabel("Choose To Remove: ");
	private JRadioButton[] pls;
	
	public RemovePLDialog()
	{
		this.setTitle("Remove a User Library");
		this.getContentPanel().setLayout(new BorderLayout(5,5));
		
		info.setBackground(getBackground());
		info.setFont(new Font("Times", Font.BOLD, 14));
		this.getContentPanel().add(info, BorderLayout.NORTH);
		
        initializePls();
	}
	
	private void initializePls()
	{
		JPanel panel=new JPanel(new GridLayout(6,1,5,5));
		ButtonGroup group=new ButtonGroup();
		
		java.util.List<PersonalLibrary> libraries=PersonalLibrary.getLibraryAccountor().getLibraries();
		pls=new JRadioButton[libraries.size()];
		for(int i=0;i<libraries.size();i++)
		{
			PersonalLibrary pl=libraries.get(i);
			if(pl!=null)
			{
				pls[i]=new JRadioButton(" "+pl.getName()+" ");
				
				pls[i].addActionListener(this);
				pls[i].setFont(FONT);
				panel.add(pls[i]);
				group.add(pls[i]);
			}
			else
			{
				pls[i]=null;
				System.out.println("Error in initializePls, null pl["+i+"]");
			}
		}
		
		for(int i=0;i<6-pls.length;i++)
		{
			panel.add(new JPanel());
		}
		
		getContentPanel().add(panel, BorderLayout.CENTER);
	}

	
	@Override
	public void cancelButtonPressed()
	{
		super.cancelButtonPressed();
		this.dispose();
	}

	
	@Override
	public void okButtonPressed()
	{
		super.okButtonPressed();
		
		int index=-1;
		for(int i=0;i<pls.length;i++)
		{
			if(pls[i]!=null&&pls[i].isSelected())
			{
				if(pls[i].getText().trim().equals(
						PersonalLibraryManager.getLibraryAccountor().get(i).
						getName().trim()))
				
				{
					System.out.println(pls[i].getText()+" is removed");	
					index=i;
					break;
				}
			}
		}
		
		if(index!=-1)
		{
			
			PersonalLibraryManager.getLibraryAccountor().remove(index);
			PersonalLibraryManager.getLibraryAccountor().writeCurrentPersonalLibraries();
			this.dispose();
		}
	}

	public static void showDialog(Component topContainer)
	{
		JDialog dialog=new RemovePLDialog();
		
	    dialog.pack();
	    dialog.setSize(dialog.getSize().width+200, dialog.getSize().height+80);
	    
	    if(topContainer!=null)
	    {
	    	dialog.setLocation(topContainer.getLocation().x+(topContainer.getWidth()-dialog.getWidth())/2, 
	    			topContainer.getLocation().y+(topContainer.getHeight()-dialog.getHeight())/2);
	    }
	    
	    dialog.setVisible(true);
				
	}
	
}
