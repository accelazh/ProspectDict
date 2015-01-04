package guestUserInterface.functions;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class LibraryLoadingCfg extends JPanel 
{
	private LibraryLoadingCfgGeneral libraryLoadingCfgGeneral=new LibraryLoadingCfgGeneral();
	private LibraryLoadingCfgChooseMajor libraryLoadingChooseCfgMajor=new LibraryLoadingCfgChooseMajor();
	private LibraryLoadingCfgChooseSecondary libraryLoadingCfgChooseSecondary=new LibraryLoadingCfgChooseSecondary();
    private LibraryLoadingCfgFinished libraryLoadingCfgFinished=new LibraryLoadingCfgFinished();
    
    private CardLayout cardLayout=new CardLayout();
    
    public LibraryLoadingCfg()
    {
    	setLayout(cardLayout);
    	
    	libraryLoadingCfgGeneral.setLibraryLoadingCfg(this);
    	libraryLoadingChooseCfgMajor.setLibraryLoadingCfg(this);
    	libraryLoadingCfgChooseSecondary.setLibraryLoadingCfg(this);
    	libraryLoadingCfgFinished.setLibraryLoadingCfg(this);
    	
    	add(libraryLoadingCfgGeneral,"libraryLoadingCfgGeneral");
    	add(libraryLoadingChooseCfgMajor,"libraryLoadingCfgChooseMajor");
    	add(libraryLoadingCfgChooseSecondary,"libraryLoadingCfgChooseSecondary");
    	add(libraryLoadingCfgFinished,"libraryLoadingCfgFinished");
    }

	public LibraryLoadingCfgGeneral getLibraryLoadingCfgGeneral() {
		return libraryLoadingCfgGeneral;
	}

	public LibraryLoadingCfgChooseMajor getLibraryLoadingCfgChooseMajor() {
		return libraryLoadingChooseCfgMajor;
	}

	public LibraryLoadingCfgChooseSecondary getLibraryLoadingCfgChooseSecondary() {
		return libraryLoadingCfgChooseSecondary;
	}

	public LibraryLoadingCfgFinished getLibraryLoadingCfgFinished() {
		return libraryLoadingCfgFinished;
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}
    
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new LibraryLoadingCfg());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
    
}
