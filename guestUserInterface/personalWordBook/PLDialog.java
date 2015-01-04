package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import guestUserInterface.magneticWindows.*;

/**
 * ��������ɴʿⲥ�ŵĽ���
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
	 * ���ʿⲥ����һ�˺󣬲��ŵڶ���֮ǰ�ļ��
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
	 * @param �ʿⲥ����Ҫ���ŵ��ַ���,
	 * text���ܳ��ȱ������textField����ʾ���ȣ�
	 * ��Ȼû�й���Ч��
	 */
	public PLDialog(String text)
	{
		//��ʼ���Լ�
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
		
		//��ʼ��blank
		StringBuffer blankBuffer=new StringBuffer();
		while(fm.stringWidth(blankBuffer.toString())<=DEFAULT_SIZE.width)
		{
			blankBuffer.append("    ");
		}
		blank=blankBuffer.toString();
		blankLength=fm.stringWidth(blank);
		
		//��ʼ�����
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
		
		//�������
		JPanel p=new MyJPanel(new BorderLayout());
		p.setBorder(textArea.getBorder());
		textArea.setBorder(null);
		p.add(viewport, BorderLayout.CENTER);
		this.getContentPane().add(p, BorderLayout.CENTER);
			
		//��ʼ
		timer.start();
		this.setVisible(true);
	}
	
	/**
	 * ���������visibleRect�����ʼλ��,����Ӧ����textField
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
	 * �����ǰΪ��ͣ״̬�����л�Ϊ����״̬��
	 * ��֮��Ȼ
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
			//�ʵ��������εĴ�С
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
