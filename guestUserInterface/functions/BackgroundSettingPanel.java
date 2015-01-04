package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import settings.*;
import java.io.*;

import javax.swing.border.*;

/**
 * 
 * @author ZYL
 * 这个类从属于OutlookingCfg类，用于产生设定背景属性
 * 用的GUI
 * 
 */
public class BackgroundSettingPanel extends JPanel 
implements Constants, ActionListener, AdjustmentListener
{
	//for debug
	private static final boolean debug=true;
		
	//data filed
	public static final String HELP_INFO="    In this part you can choose an image and press apply to change the dictionary's background."+
	    " If you want to use your own image, you can press the \"Add\" button.";
	
	private ImageViewer imageViewer=new ImageViewer();
	private JScrollBar scrollBar=new JScrollBar(JScrollBar.HORIZONTAL);  //用来控制图像切换
    private JLabel titleLabel=new JLabel();
	private JTextArea info=new JTextArea(HELP_INFO);
    
	private JButton addButton=new JButton("Add");
	private JButton okButton=new JButton("Apply");  //当点击时纪录选取的图片
	
	private int numOfPics; //图片文件名从零开始:0.jpg,1.jpg,2.jpg,3.jpg,...
	private int currentPic=0;
	
	private JFileChooser fc=new JFileChooser(); //addButton选择文件
	public BackgroundSettingPanel()
	{
		setLayout(new BorderLayout(5,5));
		
		//get numOfPics;
		File picFile;
		int i=0;
		do
		{
			picFile=new File("GUISource\\backgrounds\\"+i+".jpg");
		    i++;
		}while(picFile.exists());
		numOfPics=i-1;
		
		if(debug)
		{
			System.out.println("numOfPics initialized is: "+numOfPics);
		}
		
		//initialize components
		if(numOfPics>0)
		{
			imageViewer.setImage(new ImageIcon("GUISource\\backgrounds\\"+currentPic+".jpg"));
		}
		imageViewer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		if(numOfPics>0)
		{
			scrollBar.setMaximum(numOfPics);
		}
		else
		{
			scrollBar.setMaximum(0);
		}
		scrollBar.setMinimum(0);
		scrollBar.setValue(0);
		scrollBar.setBlockIncrement(1);
		scrollBar.setUnitIncrement(1);
		scrollBar.setVisibleAmount(1);
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new JpgeFileFilter());
		
		titleLabel.setIcon(new ImageIcon("GUISource\\bkSettingTitle.gif"));
		
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		info.setFont(new Font("Times",Font.BOLD,12));
	    info.setRows(3);
	    info.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//加入组件
		JPanel p0=new JPanel(new BorderLayout());
		p0.add(titleLabel, BorderLayout.CENTER);
		add(p0,BorderLayout.NORTH);
		
		JPanel p1=new JPanel(new BorderLayout());
		p1.add(imageViewer,BorderLayout.CENTER);
		p1.add(scrollBar,BorderLayout.SOUTH);
		add(p1,BorderLayout.CENTER);
		
		JPanel p2=new JPanel(new BorderLayout(5,5));
		
		JPanel p22=new JPanel(new BorderLayout());
		p22.add(info,BorderLayout.CENTER);
		p2.add(info, BorderLayout.SOUTH);
		
		JPanel p21=new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
		p21.add(okButton);
		p21.add(addButton);
		p2.add(p21,BorderLayout.CENTER);
		add(p2,BorderLayout.SOUTH);
		
		//加入监听器
		addButton.addActionListener(this);
		okButton.addActionListener(this);
		scrollBar.addAdjustmentListener(this);
			
	}
	
	@Override
	public Insets getInsets() 
	{
		return new Insets(6,10,6,10);
	}

	public void adjustmentValueChanged(AdjustmentEvent e) 
	{
		if(debug)
		{
	    	System.out.println("adjustmentValueChanged");
		}
		
	    if(e.getSource()==scrollBar)
	    {
	    	if(debug)
	    	{
	    		System.out.println("scrollBar value changed");
	    		System.out.println("scrollBar value: "+scrollBar.getValue());
	    	}
	    	imageViewer.setImage(new ImageIcon("GUISource\\backgrounds\\"+scrollBar.getValue()+".jpg"));
	        currentPic=scrollBar.getValue();
	    }
	}
	
	public void actionPerformed(ActionEvent e) 
	{
	 	if(e.getSource()==addButton)
	 	{
	 		File newPicFile=fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION?fc.getSelectedFile():null;
	 		if(newPicFile!=null)
	 		{
	 		    if(UsefulTools.copyFile(newPicFile,new File("GUISource\\backgrounds\\"+numOfPics+".jpg")))
	 		    {
	 			    numOfPics++;
	 			    currentPic=numOfPics-1;
	 			    scrollBar.setMaximum(numOfPics);
	 			    scrollBar.setValue(scrollBar.getMaximum()-1);
	 			    repaint();
	 			    
	 			    if(debug)
	 			    {
	 			    	System.out.println("add pic: ");
	 			    	System.out.println("numOfPics: "+numOfPics);
	 			    	System.out.println("scrollBar value: "+scrollBar.getValue());
	 			    	
	 			    }
	 		    }
	 		    else
	 		    {
	 		    	JOptionPane.showMessageDialog(this,"Sorry, failed to add the new picture.", "Error", JOptionPane.ERROR_MESSAGE);
	 		    }
	 		}
	 	}
	 	
	 	if(e.getSource()==okButton)
	 	{
	 		try
	 		{
	 		    DataOutputStream out=new DataOutputStream(new FileOutputStream(SELECTED_BK_FILE)); 
	 	        out.writeInt(scrollBar.getValue());
	 	        
	 	        if(debug)
	 	        {
	 	        	System.out.println("integer written: "+scrollBar.getValue());
	 	        }
	 	        JOptionPane.showMessageDialog(this,"Background selected!");
	 	        out.close();
	 		}
	 		catch(IOException ex)
	 		{
	 			JOptionPane.showMessageDialog(this,"Sorry, failed to select the picture.", "Error", JOptionPane.ERROR_MESSAGE);
	 			System.out.println("Error when recoding selected background, IOException");
	 		}
	 	}
		
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new BackgroundSettingPanel());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
}
