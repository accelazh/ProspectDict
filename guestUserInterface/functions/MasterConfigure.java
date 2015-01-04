package guestUserInterface.functions;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * 所有管理设置的GUI面板在这里汇总
 * @author ZYL
 *
 */
public class MasterConfigure extends JPanel 
{
	public static final Dimension PANEL_SIZE=new Dimension(500,400-37);
	
	private OutlookCfg outlookCfg=new OutlookCfg();
	private LibraryLoadingCfg libraryLoadingCfg=new LibraryLoadingCfg();
	private Installer installer=new Installer();
	
	private JTabbedPane tab=new JTabbedPane(JTabbedPane.TOP);
	
	public MasterConfigure()
	{
		this.add(tab);
		
		outlookCfg.setPreferredSize(PANEL_SIZE);
		libraryLoadingCfg.setPreferredSize(PANEL_SIZE);
		installer.setPreferredSize(PANEL_SIZE);
		
		tab.addTab("Outlook", outlookCfg);
		tab.addTab("Loading", libraryLoadingCfg);
		tab.addTab("Installer", installer);
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(new MasterConfigure());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

}
