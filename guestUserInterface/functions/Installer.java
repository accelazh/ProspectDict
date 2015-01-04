package guestUserInterface.functions;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * 这个类生成安装词库的界面
 * @author ZYL 
 * 
 */
public class Installer extends JPanel
{
	private InstallPanelGeneral installPanelGeneral=new InstallPanelGeneral();
	private InstallPanelChoose installPanelChoose=new InstallPanelChoose();
	
	private InstallPanelInfoForRestore installPanelInfoForRestore=new InstallPanelInfoForRestore();
	private InstallPanelForRestoreProcessing installPanelForRestoreProcessing=new InstallPanelForRestoreProcessing();
	private InstallPanelForRestoreFinished installPanelForRestoreFinished=new InstallPanelForRestoreFinished();
	
	private InstallPanelInput installPanelInput=new InstallPanelInput();
	private InstallPanelInputConfirm installPanelInputConfirm=new InstallPanelInputConfirm();
	private InstallPanelForInstallLibProcessing installPanelForInstallLibProcessing=new InstallPanelForInstallLibProcessing(null);
	private InstallPanelForInstallLibFinished installPanelForInstallLibFinished=new InstallPanelForInstallLibFinished();
	
	private InstallPanelInfoForBackup installPanelInfoForBackup=new InstallPanelInfoForBackup();
	private InstallPanelForBackupProcessing installPanelForBackupProcessing=new InstallPanelForBackupProcessing();
	private InstallPanelForBackupFinished installPanelForBackupFinished=new InstallPanelForBackupFinished();
		
	private InstallPanelRemoveInfo installPanelRemoveInfo=new InstallPanelRemoveInfo();
	private InstallPanelChooseToRemove installPanelChooseToRemove=new InstallPanelChooseToRemove();
	private InstallPanelForRemoveFinished installPanelForRemoveFinished=new InstallPanelForRemoveFinished();
	
	private CardLayout cardLayout=new CardLayout();
	
	public Installer()
	{
		setLayout(cardLayout);
		
		installPanelGeneral.setInstaller(this);
		installPanelChoose.setInstaller(this);
		
		installPanelInfoForRestore.setInstaller(this);
		installPanelForRestoreProcessing.setInstaller(this);
		installPanelForRestoreFinished.setInstaller(this);
		
		installPanelInput.setInstaller(this);
		installPanelInputConfirm.setInstaller(this);
		installPanelForInstallLibProcessing.setInstaller(this);
		installPanelForInstallLibFinished.setInstaller(this);
		
		installPanelInfoForBackup.setInstaller(this);
		installPanelForBackupProcessing.setInstaller(this);
		installPanelForBackupFinished.setInstaller(this);	
		
		installPanelRemoveInfo.setInstaller(this);
		installPanelChooseToRemove.setInstaller(this);
		installPanelForRemoveFinished.setInstaller(this);
		
		add(installPanelGeneral,"installPanelGeneral");
		add(installPanelChoose,"installPanelChoose");
		
		add(installPanelInfoForRestore,"installPanelInfoForRestore");
		add(installPanelForRestoreProcessing,"installPanelForRestoreProcessing");
		add(installPanelForRestoreFinished,"installPanelForRestoreFinished");
		
		add(installPanelInput,"installPanelInput");
		add(installPanelInputConfirm,"installPanelInputConfirm");
		add(installPanelForInstallLibProcessing,"installPanelForInstallLibProcessing");
		add(installPanelForInstallLibFinished,"installPanelForInstallLibFinished");
	
		add(installPanelInfoForBackup,"installPanelInfoForBackup");
		add(installPanelForBackupProcessing,"installPanelForBackupProcessing");
		add(installPanelForBackupFinished,"installPanelForBackupFinished");
		
		add(installPanelRemoveInfo,"installPanelRemoveInfo");
		add(installPanelChooseToRemove,"installPanelChooseToRemove");
		add(installPanelForRemoveFinished,"installPanelForRemoveFinished");
		
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public InstallPanelGeneral getInstallPanelGeneral() {
		return installPanelGeneral;
	}

	public InstallPanelChoose getInstallPanelChoose() {
		return installPanelChoose;
	}

	public InstallPanelInfoForRestore getInstallPanelInfoForRestore() {
		return installPanelInfoForRestore;
	}

	public InstallPanelForRestoreProcessing getInstallPanelForRestoreProcessing() {
		return installPanelForRestoreProcessing;
	}

	public InstallPanelForRestoreFinished getInstallPanelForRestoreFinished() {
		return installPanelForRestoreFinished;
	}

	public InstallPanelInput getInstallPanelInput() {
		return installPanelInput;
	}

	public InstallPanelInputConfirm getInstallPanelInputConfirm() {
		return installPanelInputConfirm;
	}

	public InstallPanelForInstallLibProcessing getInstallPanelForInstallLibProcessing() {
		return installPanelForInstallLibProcessing;
	}

	public InstallPanelForInstallLibFinished getInstallPanelForInstallLibFinished() {
		return installPanelForInstallLibFinished;
	}

	public InstallPanelRemoveInfo getInstallPanelRemoveInfo() {
		return installPanelRemoveInfo;
	}

	public InstallPanelChooseToRemove getInstallPanelChooseToRemove() {
		return installPanelChooseToRemove;
	}

	public InstallPanelForRemoveFinished getInstallPanelForRemoveFinished() {
		return installPanelForRemoveFinished;
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new Installer());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	public InstallPanelInfoForBackup getInstallPanelInfoForBackup() {
		return installPanelInfoForBackup;
	}

	public InstallPanelForBackupProcessing getInstallPanelForBackupProcessing() {
		return installPanelForBackupProcessing;
	}

	public InstallPanelForBackupFinished getInstallPanelForBackupFinished() {
		return installPanelForBackupFinished;
	}

}
