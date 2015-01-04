package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

/**
 * 这个类用于制作安装界面，是负责概述的面板
 * @author ZYL
 *
 */
public class InstallPanelGeneral extends InstallerPanelSuper
{
	public static final String INFO=
	"    This is the guide of dict library installer. "+
	"You can choose either to install a new dict library, "+
	"or restore the dict library system to latest. \n"+
	"    "+
	"To install a dict library, you need to pay special caution to that you have inputed the right arguments. "+
	"If not, the analy system may not work properly, which may damage the dict library system. "+
	"If so, you can choose to restore it. But limited to current function, you can only restore to the latest state. \n"+
	"    "+
	"You can backup the current library system by chooseing the back-up option. But note that this will overwrite the older back-up. \n"+
	"    "+
	"If you are bored with a library, you can choose to remove it. But we recommend you to backup first. \n";
	private JTextArea info=new JTextArea(INFO);
	private JLabel title=new JLabel("Library Installer: ");
	
	private Installer installer;
	
	public InstallPanelGeneral()
	{
		getContentPanel().setLayout(new BorderLayout(5,10));
		
		//初始化组件
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		info.setFont(new Font("Times",Font.BOLD,14));
		
		title.setFont(new Font("Times",Font.BOLD,14));
				
		getBackButton().setEnabled(false);
		
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
		installer.getCardLayout().show(installer, "installPanelChoose");
	}
	
	@Override
	public void cancel()
	{
		// TODO Auto-generated method stub
	}

	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelGeneral());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}
	

}
