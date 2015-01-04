package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

import libraryInterface.*;

/**
 * 这个类用于制作安装界面，是负责生成remove操作的选择界面的类
 * @author ZYL
 *
 */
public class InstallPanelChooseToRemove extends InstallerPanelSuper
{
	public static final String INFO="Choose the library to be removed below: ";
	public static final String WARN="Press next and then the library selected will be removed. ";
	
	private JLabel infoLabel=new JLabel(INFO);
	private JRadioButton[] libraryButtons;	
	private JLabel warnLabel=new JLabel(WARN);
	
	private Installer installer;
	
	public InstallPanelChooseToRemove()
	{
		getContentPanel().setLayout(new BorderLayout(5,5));
	    warnLabel.setForeground(Color.RED);
		
	}
	
	/**
	 * 每次开启这个界面都需要运行这个方法
	 */
	public void initialize()
	{
		getContentPanel().removeAll();
		
		ButtonGroup group=new ButtonGroup();
		JPanel p1=new JPanel(new GridLayout(Math.max(Library.getLibraryAccountor().getLibraryAccounts().size(),6),1));
		
		libraryButtons=new JRadioButton[Library.getLibraryAccountor().getLibraryAccounts().size()];
		for(int i=0;i<libraryButtons.length;i++)
		{
			LibraryAccount libAccount=Library.getLibraryAccountor().getLibraryAccounts().get(i);
			libraryButtons[i]=new JRadioButton("Library "+libAccount.getName()+" translating "+libAccount.getFrom()+" to "+libAccount.getTo()+" ");
		    group.add(libraryButtons[i]);
		    p1.add(libraryButtons[i]);
		}
		for(int i=0;i<6-libraryButtons.length;i++)
		{
			p1.add(new JPanel());
		}
		
        getContentPanel().add(p1,BorderLayout.CENTER);
		
        JPanel p2=new JPanel(new BorderLayout());
		p2.add(infoLabel,BorderLayout.CENTER);
		getContentPanel().add(p2,BorderLayout.NORTH);
		
		JPanel p3=new JPanel(new BorderLayout());
		p3.add(warnLabel,BorderLayout.CENTER);
		getContentPanel().add(p3, BorderLayout.SOUTH);
        
		revalidate();
	}

	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer,"installPanelRemoveInfo");
	}

	@Override
	public void nextButtonPressed() 
	{
		int selected=-1;
		for(int i=0;i<libraryButtons.length;i++)
		{
			if(libraryButtons[i].isSelected())
			{
				selected=i;
				break;
			}
		}
		
		if(selected!=-1)
		{
			if(!Library.deleteLibraryFile(Library.getLibraryAccountor().getLibraryAccounts().get(selected)))
			{
				JOptionPane.showMessageDialog(this,"Error occurs when remove! ","Error",JOptionPane.ERROR_MESSAGE);
			}
			installer.getCardLayout().show(installer, "installPanelForRemoveFinished");
		    System.out.println("library["+selected+"] is removed");
	    }
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelChooseToRemove());
		
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
