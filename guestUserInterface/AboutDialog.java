package guestUserInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import settings.*;
import javax.swing.border.*;

/**
 * 用于生成About按钮的按后出现的介绍界面
 * @author ZYL
 *
 */
public class AboutDialog extends MyOptionDialog
{
	private JTextField info=new JTextField("Author: ZYL");
	public AboutDialog()
	{
		this.setTitle("About");
		this.getContentPanel().setLayout(new BorderLayout());
		this.getContentPanel().add(info, BorderLayout.CENTER);
		info.setEditable(false);
		info.setHorizontalAlignment(SwingConstants.CENTER);
	    info.setBorder(null);
	    info.setBackground(this.getBackground());
	    info.setOpaque(false);
	    info.setFont(new Font("Times", Font.BOLD, 16));
	    
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
		this.dispose();
	}
	
	public static void showDialog(Component c)
	{
		AboutDialog d=new AboutDialog();
		d.setSize(new Dimension(350,160));
		if(c!=null)
		{
			d.setLocation(c.getLocation().x+(c.getWidth()-d.getWidth())/2, 
				c.getLocation().y+(c.getHeight()-d.getHeight())/2);
		}
		d.setVisible(true);
		
	}
	

}
