package guestUserInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import settings.*;

/**
 * 这个类用来生成各式各样的短生命期dialog，
 * 用于简单的交互
 * 
 * @author ZYL
 *
 */
public class MyOptionDialog extends JDialog 
implements Constants, ActionListener
{
	private JPanel contentPanel=new JPanel();
	private JButton okButton=new JButton("OK");
	private JButton cancelButton=new JButton("Cancel");
	
	public MyOptionDialog()
	{
		this.setLayout(new BorderLayout(5,5));
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel p1=new JPanel(new BorderLayout(5,5));
		p1.add(new JSeparator(), BorderLayout.NORTH);
		
		JPanel p11=new JPanel(new FlowLayout(FlowLayout.CENTER, 20,5));
		p11.add(okButton);
		okButton.setPreferredSize(new Dimension(60,30));
		cancelButton.setPreferredSize(new Dimension(60,30));
		p11.add(cancelButton);
		p1.add(p11, BorderLayout.CENTER);
		
		this.getContentPane().add(p1, BorderLayout.SOUTH);
		
		this.setBackground(SHARED_BACKGROUND);
		contentPanel.setBackground(SHARED_BACKGROUND);
	
		this.setAlwaysOnTop(true);
		
	    //add listeners
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==okButton)
		{
			okButtonPressed();
		}
		
		if(e.getSource()==cancelButton)
		{
			cancelButtonPressed();
    	}
	}
	
	public void okButtonPressed()
	{
		
	}
	
	public void cancelButtonPressed()
	{
		
	}

	public JPanel getContentPanel()
	{
		return contentPanel;
	}

	public void setContentPanel(JPanel contentPanel)
	{
		this.contentPanel = contentPanel;
	}

	

}
