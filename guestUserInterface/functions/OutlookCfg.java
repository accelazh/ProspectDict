package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import settings.*;
import java.io.*;
import javax.swing.event.*;


/**
 * 这个类用来产生用于设定外观的GUI，从属于Configure类
 * @author ZYL
 *
 */
public class OutlookCfg extends JPanel 
implements Constants, ListSelectionListener
{
	//for debug
	private static final boolean debug=true;
	
	private JList modeList;
	private SkinSettingPanel skinPanel;
	private BackgroundSettingPanel bkPanel;
    private JLabel listTitle=new JLabel();
	
	private JPanel contentP;
	private CardLayout cardLayout;
	
	public OutlookCfg()
	{
		//self initialization
		setLayout(new BorderLayout());
		
		//initialize components
		modeList=new JList(new String[]{
				"Background Setting",
				"Skin Setting",
		});
		
		skinPanel=new SkinSettingPanel();
		
		bkPanel=new BackgroundSettingPanel();
		
		listTitle.setIcon(new ImageIcon("GUISource\\selectListTitle.gif"));
		
		//add components
		JPanel p1=new InsetJPanel(new BorderLayout(0,5),6,10,6,0 );
		p1.add(listTitle,BorderLayout.NORTH);
		p1.add(new JScrollPane(modeList), BorderLayout.CENTER);		add(p1,BorderLayout.WEST);
		
		
		JPanel p2=new JPanel(cardLayout=new CardLayout());
	    p2.add(bkPanel,"bkPanel");
		p2.add(skinPanel,"skinPanel");
		add(p2,BorderLayout.CENTER);
		contentP=p2;
		
		//add listeners
		modeList.addListSelectionListener(this);
		
		//start
		initialization();
	}
	
	public void initialization()
	{
		
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getSource()==modeList)
		{
			if(modeList.getSelectedIndex()==0)
			{
				if(debug)
				{
					System.out.println("show bkPanel");
				}
				cardLayout.show(contentP, "bkPanel");
			}
			
			if(modeList.getSelectedIndex()==1)
			{
				if(debug)
				{
					System.out.println("show skinPanel");
				}
				skinPanel.initialize();
				cardLayout.show(contentP, "skinPanel");
			}
		}
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new OutlookCfg());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
}
