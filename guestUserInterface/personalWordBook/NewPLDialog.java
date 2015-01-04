package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import settings.*;

import guestUserInterface.*;
import libraryManager.*;

import javax.swing.border.*;

/**
 * 这个类用来创建建立新的个人词库的界面
 * @author ZYL
 *
 */
public class NewPLDialog extends MyOptionDialog
{
	private JLabel info=new JLabel("Name: ");
	private JTextField nameField=new JTextFieldOfMind();
	
	public NewPLDialog()
	{
		this.setTitle("Create a User Library");
		
		info.setBackground(getBackground());
		info.setFont(new Font("Times", Font.BOLD, 14));
		
		nameField.setFont(new Font("Times", Font.BOLD, 14));
		nameField.setSelectionColor(new Color(128,255,255));
		nameField.setSelectedTextColor(Color.BLACK);
		nameField.setOpaque(false);
		nameField.setPreferredSize(new Dimension(200,40));
		nameField.setBorder(null);
		nameField.setMargin(new Insets(1,1,1,1));
		nameField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,new Color(80,118,175), Color.BLACK));
		
		this.getContentPanel().setLayout(new BorderLayout(5,5));
		this.getContentPanel().add(info, BorderLayout.NORTH);
		
		JPanel p1=new JPanel(new BorderLayout());
		p1.add(new JLabel("\t"), BorderLayout.NORTH);
		p1.add(new JLabel("\t"), BorderLayout.SOUTH);
		p1.add(nameField, BorderLayout.CENTER);
		this.getContentPanel().add(p1, BorderLayout.CENTER);
		
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
		
		if(PersonalLibraryManager.createPersonalLibrary(nameField.getText()))
		{
			new PersonalWordDialog(PersonalLibraryManager.getLibraryAccountor().size()-1);
			PersonalLibraryManager.getLibraryAccountor().writeCurrentPersonalLibraries();
			this.dispose();
		}
		else
		{
			ShowMessageDialog.showDialog(this, "Sorry, invalid or repeated name,\n please change and try.");
		}
	}
	
	public static void showDialog(Component topContainer)
	{
		JDialog dialog=new NewPLDialog();
		
	    dialog.pack();
	    //dialog.setSize(dialog.getSize().width+200, dialog.getSize().height+80);
	    
	    if(topContainer!=null)
	    {
	    	dialog.setLocation(topContainer.getLocation().x+(topContainer.getWidth()-dialog.getWidth())/2, 
	    			topContainer.getLocation().y+(topContainer.getHeight()-dialog.getHeight())/2);
	    }
	    
	    dialog.setVisible(true);
				
	}
	
	private class JTextFieldOfMind extends JTextField
	{
		public Insets getInsets()
		{
			return new Insets(2,2,2,2);
		}
		
		public Insets getInsets(Insets insets)
		{
			return new Insets(2,2,2,2);
		}
	}

}
