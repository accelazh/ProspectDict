package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

/**
 * 这个类用于制作安装界面，是负责生成选择界面的类
 * @author ZYL
 *
 */
public class InstallPanelChoose extends InstallerPanelSuper
{
	public static final String INFO="You can choose what to do next here: ";
	
	private JLabel infoLabel=new JLabel(INFO);
	private JRadioButton installLib=new JRadioButton("Install a new dict library");
	private JRadioButton restoreLib=new JRadioButton("Restore the dict library system to the latest");
    private JRadioButton backupLib=new JRadioButton("Backup the dict library files");
	private JRadioButton removeLib=new JRadioButton("Remove a selected dict library");
	
	private Installer installer;
	
	public InstallPanelChoose()
	{
		getContentPanel().setLayout(new BorderLayout(5,5));
		
		//初始化组件
		ButtonGroup group=new ButtonGroup();
		group.add(installLib);
		group.add(restoreLib);
		group.add(backupLib);
		group.add(removeLib);
		
		getCancelButton().setVisible(true);
		
		//加入组件
		JPanel p1=new JPanel(new GridLayout(6,1));
		p1.add(installLib);
		p1.add(restoreLib);
		p1.add(backupLib);
		p1.add(removeLib);
		for(int i=0;i<2;i++)
		{
			p1.add(new JPanel());
		}
		getContentPanel().add(p1,BorderLayout.CENTER);
		
		JPanel p2=new JPanel(new BorderLayout());
		p2.add(infoLabel,BorderLayout.CENTER);
		getContentPanel().add(p2,BorderLayout.NORTH);
	}

	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer,"installPanelGeneral");
	}

	@Override
	public void nextButtonPressed() 
	{
		if(restoreLib.isSelected())
		{
			installer.getCardLayout().show(installer, "installPanelInfoForRestore");
		}
		
		if(installLib.isSelected())
		{
			installer.getCardLayout().show(installer, "installPanelInput");
		}
		
		if(backupLib.isSelected())
		{
			installer.getCardLayout().show(installer, "installPanelInfoForBackup");
		}
		
		if(removeLib.isSelected())
		{
			installer.getCardLayout().show(installer, "installPanelRemoveInfo");
		}
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
		frame.getContentPane().add(new InstallPanelChoose());
		
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
