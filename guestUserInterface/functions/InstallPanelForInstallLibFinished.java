package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

/**
 * ���������������װ���棬����restore�����֮һ
 * @author ZYL
 *
 */
public class InstallPanelForInstallLibFinished extends InstallerPanelSuper
{
	public static final String LABEL_INFO="Install Library File Finished. ";
	
	private JTextArea label=new JTextArea(LABEL_INFO);
	
	private Installer installer;
	
	public InstallPanelForInstallLibFinished()
	{
		super();
		
		//��ʼ�����
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		label.setFont(new Font("Times",Font.BOLD,14));
		label.setBackground(getContentPanel().getBackground());
		
		getBackButton().setEnabled(false);
		getNextButton().setText("Finish");
		
		//�������
		getContentPanel().setLayout(new BorderLayout(5,10));
		getContentPanel().add(label,BorderLayout.CENTER);
	    
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
		frame.getContentPane().add(new InstallPanelForInstallLibFinished());
		
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
