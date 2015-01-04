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
public class InstallPanelInfoForBackup extends InstallerPanelSuper
{
	public static final String LABEL_INFO="Back will begin if you press next, sure?";
	public static final String TEXT_INFO="Caution: \n"+
	"    After back, you will have chance to bring old library system back. But note that " +
	"the original back-up will be overwriten. \n"+
	"    Also note that operations such as Install a Library and Restore can auto-maticaly backup, " +
	"which will overwrite the back-up files. ";
	
	private JTextArea info=new JTextArea(TEXT_INFO);
	private JTextArea label=new JTextArea(LABEL_INFO);
	
	private Installer installer;
	
	public InstallPanelInfoForBackup()
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
		installer.getInstallPanelForBackupProcessing().getBackButton().setEnabled(false);
		installer.getCardLayout().show(installer, "installPanelForBackupProcessing");
		installer.getInstallPanelForBackupProcessing().startThreads();
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
		frame.getContentPane().add(new InstallPanelInfoForBackup());
		
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
