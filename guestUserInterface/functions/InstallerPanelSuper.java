package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;
import settings.*;

/**
 * 
 * ���������������װ���棬����Ϊ���࣬�ṩģ��
 * @author ZYL
 *
 */
public abstract class InstallerPanelSuper extends JPanel 
implements Constants, ActionListener
{
	//����
	
	
	//���
	private JLabel leftLabel=new JLabel();
	private InsetJPanel contentPanel=new InsetJPanel(6,10,6,10);
	private JLabel upLabel=new JLabel();
	private JButton nextButton=new JButton("Next");
	private JButton backButton=new JButton("Back");
	private JButton cancelButton=new JButton("Cancel");
	
	public InstallerPanelSuper()
	{
		//self initialization
		setLayout(new BorderLayout());
        	
		//initialize component
		leftLabel.setIcon(new ImageIcon("GUISource\\installLeftLabel.gif"));
		upLabel.setIcon(new ImageIcon("GUISource\\installTopLabel.gif"));
		
		contentPanel.setLayout(new BorderLayout());
		
		cancelButton.setVisible(false);
		
		//add components
		JPanel p1=new InsetJPanel(new BorderLayout(),6,10,6,0);
		p1.add(leftLabel,BorderLayout.CENTER);
		add(p1,BorderLayout.WEST);
		
		JPanel p2=new InsetJPanel(new BorderLayout(),6,10,6,10);
		p2.add(upLabel,BorderLayout.NORTH);
		p2.add(contentPanel,BorderLayout.CENTER);
		
		JPanel p21=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,5));
		p21.add(nextButton);
		p21.add(backButton);
		p21.add(cancelButton);
		p2.add(p21,BorderLayout.SOUTH);
		
		add(p2,BorderLayout.CENTER);
		
		//add listeners
		nextButton.addActionListener(this);
		backButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==nextButton)
		{
			nextButtonPressed();
		}
		
		if(e.getSource()==backButton)
		{
			backButtonPressed();
		}
		
		if(e.getSource()==cancelButton)
		{
			if(0==JOptionPane.showConfirmDialog(this,"Are you sure to cancel?", "Sure to Cancel?", JOptionPane.YES_NO_OPTION))
			{
				cancel();
			}
		}
	}

	/**
	 * ����ͨ�������������ʵ���书��
	 */
	public abstract void nextButtonPressed();
	
	/**
	 * ����ͨ�������������ʵ���书��
	 */
	public abstract void backButtonPressed();
	
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * ����ͨ�������������ʵ���书��
	 */
	public abstract void cancel();
	
	public InsetJPanel getContentPanel() {
		return contentPanel;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public JButton getBackButton() {
		return backButton;
	}
	
}
