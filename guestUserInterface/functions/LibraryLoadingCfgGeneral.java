package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*; 

import guestUserInterface.functions.*;

/**
 * 这个类用来生成词库加载配置界面的向导GUI
 * 
 * @author ZYL
 *
 */
public class LibraryLoadingCfgGeneral extends LibraryLoadingCfgPanelSuper 
{
	public static final String INFO=
	"    You can choose libraries to be loaded into the memory when the dictionary is launched. \n" +
	"    The major libraries refer to the libraries the dictionary will be in ues when launched. \n"+
	"    The secondary libraries " +
	"refer to the libraries loaded to the memory when launching, but not in use immediately. \n"+
	"Note that although more libraries " +
	"selected will add to the speed when switch libraries in the dictionary, it may increase the time to launch. \n" +
	"    Tips: Choosing the most frequently used libraries as the major and the secondary libraries. ";
	private JTextArea info=new JTextArea(INFO);
	private JLabel title=new JLabel("Library Loading Configure: ");
	private LibraryLoadingCfg libraryLoadingCfg;
	
	public LibraryLoadingCfgGeneral()
	{
		getContentPanel().setLayout(new BorderLayout(5,10));
		
		//初始化组件
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		info.setFont(new Font("Times",Font.BOLD,14));
		
		title.setFont(new Font("Times",Font.BOLD,14));
		
		getBackButton().setEnabled(false);
		getCancelButton().setVisible(true);
		
		//加入组件
		getContentPanel().add(new JScrollPane(info),BorderLayout.CENTER);
	    getContentPanel().add(title,BorderLayout.NORTH);
	}

	@Override
	public void backButtonPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextButtonPressed()
	{
		libraryLoadingCfg.getLibraryLoadingCfgChooseMajor().initialize();
		libraryLoadingCfg.getCardLayout().show(libraryLoadingCfg, "libraryLoadingCfgChooseMajor");
	}
	
	@Override
	public void cancel()
	{
		System.exit(0);
	}

	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new LibraryLoadingCfgGeneral());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	public LibraryLoadingCfg getLibraryLoadingCfg() {
		return libraryLoadingCfg;
	}

	public void setLibraryLoadingCfg(LibraryLoadingCfg libraryLoadingCfg) {
		this.libraryLoadingCfg = libraryLoadingCfg;
	}

}
