package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import settings.*;
import java.io.*;

/**
 * 
 * @author ZYL
 * 这个类从属于OutlookingCfg类，用于产生设定外观属性
 * 用的GUI
 * 
 */
public class SkinSettingPanel extends JPanel 
implements Constants, ActionListener, MouseListener
{	
	//用于说明的字符串
	public static final String ACTIVE_BK_INFO="    When selected, the background picture will act as if the dictionary is a window.";
	public static final String PAINT_BK_MASK_INFO="    When selected the background mask will be painted, which helps to make the text look clearer.";
	public static final String FREE_MOVE_INFO="    When selected, the windows can be separated and be moved freely.";
	public static final String FIREWORK_INFO="    Open the firework effect, you can see it when the window is maximized.";
	public static final String SPIN_INFO="    This will enable the firework to spin. The firework effect will appear when window is maximized.";
	
	private JCheckBox activeBk=new JCheckBox("Active Background");
	private JCheckBox paintBkMask=new JCheckBox("Paint Background Mask");
	private JCheckBox freeMove=new JCheckBox("Free Moving");
	private JCheckBox firework=new JCheckBox("Firework Effect");
	private JCheckBox spin=new JCheckBox("Firework Spining");
	private JLabel skinTitle=new JLabel();
	
	private JTextArea info=new JTextArea("Information Here: ");
	
	public SkinSettingPanel()
	{
		setLayout(new BorderLayout(5,5));
		
		//initialize components
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setEditable(false);
		info.setFont(new Font("Times",Font.BOLD,12));
	    info.setRows(5);
		
	    skinTitle.setIcon(new ImageIcon("GUISource\\skinSettingTitle.gif"));
		
	    //add components
	    JPanel p0=new JPanel(new BorderLayout());
		p0.add(skinTitle, BorderLayout.CENTER);
		add(p0,BorderLayout.NORTH);
			    
	    JPanel p1=new JPanel(new GridLayout(6,1,5,5));
		p1.add(activeBk);
		p1.add(paintBkMask);
		p1.add(freeMove);
		p1.add(firework);
		p1.add(spin);
		for(int i=0;i<1;i++)
		{
			p1.add(new JPanel());
		}
		add(p1,BorderLayout.CENTER);
		
		JPanel p2=new JPanel(new BorderLayout());
		p2.add(new JScrollPane(info),BorderLayout.CENTER);
		add(p2,BorderLayout.SOUTH);
	
		//add listeners
		activeBk.addActionListener(this);
		paintBkMask.addActionListener(this);
		freeMove.addActionListener(this);
		firework.addActionListener(this);
		spin.addActionListener(this);
		
		activeBk.addMouseListener(this);
		paintBkMask.addMouseListener(this);
		freeMove.addMouseListener(this);
		firework.addMouseListener(this);
		spin.addMouseListener(this);
		
		//setSelected
		paintBkMask.setSelected(true);
		freeMove.setSelected(true);
		
	}

	//这个方法时的一进入这个页面就会记录，即使用户什么也不选,这样避免出现选择与实际不匹配的问题
	
	protected void initialize()
	{
		try 
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					ACTIVE_BK_FILE));
			out.writeBoolean(activeBk.isSelected());
			out.close();
		}
			catch (IOException ex) 
		{
			System.out.println("Error when recoding activeBk, IOException");
		}
		
		try 
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					PAINT_BK_MASK_FILE));
			out.writeBoolean(paintBkMask.isSelected());
			out.close();
		}
			catch (IOException ex) 
		{
			System.out.println("Error when recoding paintBkMask, IOException");
		}	
			
		try 
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					FREE_MOVE_FILE));
			out.writeBoolean(freeMove.isSelected());
			out.close();
		}
			catch (IOException ex) 
		{
			System.out.println("Error when recoding freeMove, IOException");
		}		

		try 
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					FIREWORK_BK_FILE));
			out.writeBoolean(firework.isSelected());
			out.close();
		} 
		catch (IOException ex)
		{
			System.out.println("Error when recoding firework, IOException");
		}

		try
		{
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					SPIN_BK_FILE));
			out.writeBoolean(spin.isSelected());
			out.close();
		}
		catch (IOException ex) 
		{
			System.out.println("Error when recoding spin, IOException");
		}
		
	}
	
	@Override
	public Insets getInsets() 
	{
		return new Insets(6,10,6,10);
	}

	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==activeBk)
		{
			info.setText(ACTIVE_BK_INFO);
			try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(ACTIVE_BK_FILE)); 
	 	        out.writeBoolean(activeBk.isSelected());
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			System.out.println("Error when recoding activeBk, IOException");
	 		}
		}
		
		if(e.getSource()==paintBkMask)
		{
			info.setText(PAINT_BK_MASK_INFO);
			try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(PAINT_BK_MASK_FILE)); 
	 	        out.writeBoolean(paintBkMask.isSelected());
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			System.out.println("Error when recoding paintBkMask, IOException");
	 		}
		}
		
		if(e.getSource()==freeMove)
		{
			info.setText(FREE_MOVE_INFO);
			try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(FREE_MOVE_FILE)); 
	 	        out.writeBoolean(freeMove.isSelected());
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			System.out.println("Error when recoding freeMove, IOException");
	 		}
		}
		
		if(e.getSource()==firework)
		{
			info.setText(FIREWORK_INFO);
			try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(FIREWORK_BK_FILE)); 
	 	        out.writeBoolean(firework.isSelected());
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			System.out.println("Error when recoding firework, IOException");
	 		}
		}
		
		if(e.getSource()==spin)
		{
			info.setText(SPIN_INFO);
			try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(SPIN_BK_FILE)); 
	 	        out.writeBoolean(spin.isSelected());
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			System.out.println("Error when recoding spin, IOException");
	 		}
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) 
	{
		if(e.getSource()==activeBk)
		{
			info.setText(ACTIVE_BK_INFO);
		}
		
		if(e.getSource()==paintBkMask)
		{
			info.setText(PAINT_BK_MASK_INFO);
		}
		
		if(e.getSource()==freeMove)
		{
			info.setText(FREE_MOVE_INFO);
		}
		
		if(e.getSource()==firework)
		{
			info.setText(FIREWORK_INFO);
		}
		
		if(e.getSource()==spin)
		{
			info.setText(SPIN_INFO);
		}
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new SkinSettingPanel());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

}
