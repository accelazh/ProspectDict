package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

import libraryInterface.*;
import java.io.*;


/**
 * 这个类用来生成词库加载配置界面的向导GUI，具体分工是选择主词库的界面
 * @author ZYL
 *
 */
public class LibraryLoadingCfgChooseMajor extends LibraryLoadingCfgPanelSuper
{
	public static final String INFO="Choose the major library to be loaded when launching: ";

	private JLabel infoLabel=new JLabel(INFO);
	private JCheckBox[] libraryButtons;	
	
	private LibraryLoadingCfg libraryLoadingCfg;
	
	public LibraryLoadingCfgChooseMajor()
	{
		getContentPanel().setLayout(new BorderLayout(5,5));
	}
	
	/**
	 * 每次开启这个界面都需要运行这个方法
	 */
	public void initialize()
	{
		getContentPanel().removeAll();
		
		JPanel p1=new JPanel(new GridLayout(Math.max(Library.getLibraryAccountor().getLibraryAccounts().size(),6),1));
		
		libraryButtons=new JCheckBox[Library.getLibraryAccountor().getLibraryAccounts().size()];
		for(int i=0;i<libraryButtons.length;i++)
		{
			LibraryAccount libAccount=Library.getLibraryAccountor().getLibraryAccounts().get(i);
			libraryButtons[i]=new JCheckBox("Library "+libAccount.getName()+" translating "+libAccount.getFrom()+" to "+libAccount.getTo()+" ");
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
		    
		revalidate();
	}

	@Override
	public void backButtonPressed() 
	{
		libraryLoadingCfg.getCardLayout().show(libraryLoadingCfg,"libraryLoadingCfgGeneral");
	}

	@Override
	public void nextButtonPressed() 
	{
		boolean[] selecteds=new boolean[libraryButtons.length];
		for(int i=0;i<libraryButtons.length;i++)
		{
			selecteds[i]=false;
			if(libraryButtons[i]!=null)
			{
				if(libraryButtons[i].isSelected())
				{
					selecteds[i]=true;
				}
			}
		}
		
		//记录数据
		try
		{
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(MAJOR_LIBRARY_FILE));
			out.writeObject(selecteds);
			System.out.print("write booleans in major selecteds: ");
			for(int i=0;i<selecteds.length;i++)
			{
				System.out.print(selecteds[i]+" ");
			}
			System.out.println();
			out.close();
			
			//开启下一个页面
			libraryLoadingCfg.getLibraryLoadingCfgChooseSecondary().initialize(selecteds);
			libraryLoadingCfg.getCardLayout().show(libraryLoadingCfg,"libraryLoadingCfgChooseSecondary");
			
		}
		catch(IOException ex)
		{
			System.out.println("Error in LibraryLoadingCfgChooseSecondary's nextButtonPressed(), IOException");
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
		frame.getContentPane().add(new LibraryLoadingCfgChooseMajor());
		
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
