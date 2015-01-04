package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

/**
 * 这个类用于制作安装界面，负责restore的面板之一
 * @author ZYL
 *
 */
public class InstallPanelInfoForRestore extends InstallerPanelSuper
{
	public static final String LABEL_INFO="Restore will be processed if you press next, sure?";
	public static final String TEXT_INFO="Caution: \n"+
	"    After restore, you dict library system will change to the latest, and the current dict library will be lost. ";
	
	private JTextArea info=new JTextArea(TEXT_INFO);
	private JTextArea label=new JTextArea(LABEL_INFO);
	
	private Installer installer;
	
	public InstallPanelInfoForRestore()
	{
		super();
		
		//初始化组件
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		info.setFont(new Font("Times",Font.BOLD,14));
		
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		label.setFont(new Font("Times",Font.BOLD,14));
		label.setBackground(getContentPanel().getBackground());
		
		//加入组件
		getContentPanel().setLayout(new BorderLayout(5,10));
		getContentPanel().add(new JScrollPane(info),BorderLayout.CENTER);
		getContentPanel().add(label,BorderLayout.SOUTH);
	    
	}

	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer,"installPanelChoose");
	}

	@Override
	public void nextButtonPressed()
	{
		installer.getInstallPanelForRestoreProcessing().getBackButton().setEnabled(false);
		installer.getCardLayout().show(installer, "installPanelForRestoreProcessing");
		installer.getInstallPanelForRestoreProcessing().startThreads();
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
		frame.getContentPane().add(new InstallPanelInfoForRestore());
		
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
