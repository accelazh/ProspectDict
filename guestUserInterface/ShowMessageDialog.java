package guestUserInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class ShowMessageDialog extends MyOptionDialog
implements WindowListener
{
	private JLabel infoLabel=new JLabel();
	/**
	 * 如果为true，则按确定或cancel后，自动结束全部程式
	 */
	private boolean autoShutDown=false;
	
	public ShowMessageDialog(String info)
	{
		this.setTitle("Message");
		this.getContentPanel().setLayout(new BorderLayout());
		this.getContentPanel().add(infoLabel, BorderLayout.CENTER);
		this.addWindowListener(this);
		
		infoLabel.setText(info);
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		infoLabel.setBackground(getBackground());
		infoLabel.setFont(new Font("Times", Font.BOLD, 14));
		
		infoLabel.setBorder(null);
		infoLabel.setBackground(this.getBackground());
		infoLabel.setOpaque(false);
	
	}
	@Override
	public void cancelButtonPressed()
	{
		super.cancelButtonPressed();
		System.out.println("isAlwaysOnTop: "+isAlwaysOnTop());
		
		if(!autoShutDown)
		{
			this.dispose();
		}
		else
		{
			System.exit(0);
		}
	}
	@Override
	public void okButtonPressed()
	{
		super.okButtonPressed();
		if(!autoShutDown)
		{
			this.dispose();
		}
		else
		{
			System.exit(0);
		}
		
	}
	
	public void setAutoShutDown(boolean autoShutDown)
	{
		this.autoShutDown=autoShutDown;
	}
	
	public static ShowMessageDialog showDialog(Component topContainer, String info)
	{
		ShowMessageDialog dialog=new ShowMessageDialog(info);
		
	    dialog.pack();
	    dialog.setSize(dialog.getSize().width+200, dialog.getSize().height+80);
	    
	    if(topContainer!=null)
	    {
	    	dialog.setLocation(topContainer.getLocation().x+(topContainer.getWidth()-dialog.getWidth())/2, 
	    			topContainer.getLocation().y+(topContainer.getHeight()-dialog.getHeight())/2);
	    }
	    else
	    {
	    	dialog.setLocationRelativeTo(null);
	    }
	    
	    dialog.setVisible(true);
	    
	    return dialog;
				
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
		if(autoShutDown)
		{
			System.exit(0);
		}
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
