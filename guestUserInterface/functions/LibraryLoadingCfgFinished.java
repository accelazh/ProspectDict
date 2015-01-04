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
public class LibraryLoadingCfgFinished extends LibraryLoadingCfgPanelSuper
{
	public static final String LABEL_INFO="Library Loading Configure Finished. ";
	
	private JTextArea label=new JTextArea(LABEL_INFO);
	
	private LibraryLoadingCfg libraryLoadingCfg;
	
	public LibraryLoadingCfgFinished()
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
		libraryLoadingCfg.getCardLayout().show(libraryLoadingCfg,"libraryLoadingCfgGeneral");
	}
	
	@Override
	public void cancel()
	{
		// TODO Auto-generated method stub
	}

	public LibraryLoadingCfg getLibraryLoadingCfg() {
		return libraryLoadingCfg;
	}

	public void setLibraryLoadingCfg(LibraryLoadingCfg libraryLoadingCfg) {
		this.libraryLoadingCfg = libraryLoadingCfg;
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new LibraryLoadingCfgFinished());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	
}
