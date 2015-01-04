package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import guestUserInterface.magneticWindows.*;

/**
 * 这个类生成词库播放的界面
 * @author ZYL
 *
 */
public class PLDialog extends MagDialog 
implements ActionListener, WindowListener, ComponentListener 
{	
	public static final Font FONT=new Font("Times", Font.BOLD, 16); 
	public static final Dimension DEFAULT_SIZE=new Dimension(400,40);
	
	private JTextArea textArea=new JTextArea();
	private String text;
	private int textLength;
	
	/**
	 * 当词库播放完一趟后，播放第二趟之前的间隔
	 */
	private String blank="";
	private int blankLength;
	
	private Timer timer=new Timer(40, this);
	private int step=2;
	
	private Rectangle visibleRect=new Rectangle();
	
	private boolean unAdjusted=true;
	
	//pop up menu
	private PLPopupMenu pop=new PLPopupMenu(this);
	
	/**
	 * 
	 * @param 词库播放器要播放的字符串,
	 * text的总长度必须大于textField的显示长度，
	 * 不然没有滚动效果
	 */
	public PLDialog(String text)
	{
		//初始化自己
		if(text!=null)
		{
			this.text=text;
		}
		else
		{
			this.text="none";
		}
		
		FontMetrics fm=getFontMetrics(FONT);
		textLength=fm.stringWidth(text);
		
		this.setLayout(new BorderLayout());
		this.setSize(DEFAULT_SIZE);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.addWindowListener(this);
		this.setUndecorated(true);
		
		//初始化blank
		StringBuffer blankBuffer=new StringBuffer();
		while(fm.stringWidth(blankBuffer.toString())<=DEFAULT_SIZE.width)
		{
			blankBuffer.append("    ");
		}
		blank=blankBuffer.toString();
		blankLength=fm.stringWidth(blank);
		
		//初始化组件
		textArea.setFont(FONT);
		textArea.setText(blank+text+blank);
		textArea.setEditable(false);
		textArea.scrollRectToVisible(visibleRect);
	    textArea.addMouseListener(this);
		
		JViewport viewport=new JViewport();
		viewport.setView(textArea);
		viewport.addComponentListener(this);
		viewport.addMouseListener(this);
		viewport.addMouseMotionListener(this);
		
		//加入组件
		JPanel p=new MyJPanel(new BorderLayout());
		p.setBorder(textArea.getBorder());
		textArea.setBorder(null);
		p.add(viewport, BorderLayout.CENTER);
		this.getContentPane().add(p, BorderLayout.CENTER);
			
		//开始
		timer.start();
		this.setVisible(true);
	}
	
	/**
	 * 这个方法将visibleRect调会初始位置,并相应调整textField
	 */
	public void restart()
	{
		visibleRect.setLocation(0,0);
        textArea.scrollRectToVisible(visibleRect);
	}

	public void pause()
	{
		timer.stop();
	}
	
	public void resume()
	{
		timer.start();
	}
	
	/**
	 * 如果当前为暂停状态，则切换为运行状态，
	 * 反之亦然
	 */
	public void switchPauseAndResume()
	{
		if(timer.isRunning())
		{
			timer.stop();
		}
		else
		{
			timer.start();
		}
	}
	
	public void setSpeed(int speed)
	{
		if(speed<0)
		{
			return;
		}
		
		this.step=speed;
	}
	
	public void exit()
	{
		timer.stop();
		this.dispose();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==timer)
		{
			//适当调整矩形的大小
			if(unAdjusted)
			{
				Point location=visibleRect.getLocation();
				visibleRect=textArea.getVisibleRect();
				visibleRect.setLocation(location);
				
				unAdjusted=false;
			}
			
			if(visibleRect.getLocation().x<blankLength+textLength+blankLength)
			{
				visibleRect.setLocation(visibleRect.getLocation().x+step, 0);
			    textArea.scrollRectToVisible(visibleRect);
			}
			else
			{
				restart();
			}
			
			repaint();
		}
	}
	
	private class MyJPanel extends JPanel
	{
		
		public MyJPanel(LayoutManager l)
		{
			super(l);
		}
		
		public Insets getInsets()
		{
			return new Insets(8,9,8,9);
		}
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent e) 
	{
		System.out.println("resized");
		unAdjusted=true;
	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) 
	{
		timer.stop();
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		
		if(e.getSource()==textArea&&SwingUtilities.isLeftMouseButton(e))
		{
     		switchPauseAndResume();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		
		if(e.getSource()==textArea)
		{
			if(e.isPopupTrigger())
			{
				pop.show((Component)e.getSource(), e.getX(), e.getY());
			}
		}
	}

}
