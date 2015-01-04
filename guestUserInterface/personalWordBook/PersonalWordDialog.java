package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import libraryManager.*;
import libraryInterface.*;

public class PersonalWordDialog extends JDialog
implements WindowListener
{
	private PersonalWordPanel wordPanel;
	
	public PersonalWordDialog(int index)
	{
		wordPanel=new PersonalWordPanel(index, this);
		PersonalLibrary libP=PersonalLibraryManager.getLibraryAccountor().get(index);
		if(libP!=null)
		{
			this.setTitle(libP.getName());
		}
		getContentPane().add(wordPanel);
		setSize(520,400);
		this.addWindowListener(this);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e)
	{
		PersonalLibraryManager.getLibraryAccountor().writeCurrentPersonalLibraries();
		
	}

	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
