package games.flyingBlocks;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class GamePanel extends JPanel 
implements Constants, ActionListener, MouseListener, MouseMotionListener
{
	public static final Dimension DEFAULT_SIZE=new Dimension(500, 500);
	public static final int BORDER_WIDTH=20;
	
	private boolean started=false;
	private javax.swing.Timer timer=new javax.swing.Timer(TIMER_INTERVAL, this);

	private java.util.List<Block> blocks=new LinkedList<Block>();
	private MyBlock myBlock=new MyBlock();
	
	private boolean dragOn=false;  //标记用户是否正在托拽方块
	private int score=0;  //计时器
	private boolean paintScore=false;
	
	private int counter=0;
	
	public GamePanel()
	{
		setBorder(new LineBorder(Color.BLACK, BORDER_WIDTH));
		this.setPreferredSize(DEFAULT_SIZE);
		this.setOpaque(true);
		this.setBackground(Color.LIGHT_GRAY);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		refresh();
	}
	
	public void refresh()
	{
		blocks.clear();
		
		int size=60;
		
		blocks.add(new Block(BORDER_WIDTH,BORDER_WIDTH,size,size));
		blocks.add(new Block(DEFAULT_SIZE.width-size-BORDER_WIDTH,BORDER_WIDTH,size,size));
		blocks.add(new Block(BORDER_WIDTH,DEFAULT_SIZE.height-size-BORDER_WIDTH,size,size));
		blocks.add(new Block(DEFAULT_SIZE.width-size-BORDER_WIDTH,DEFAULT_SIZE.height-size-BORDER_WIDTH,size,size));
		
		
		myBlock.setX((GamePanel.DEFAULT_SIZE.width-MyBlock.SIZE.width)/2);
		myBlock.setY((GamePanel.DEFAULT_SIZE.height-MyBlock.SIZE.height)/2);
	    
		dragOn=false;
		
		score=0;
		counter=0;
		
		repaint();
	}
	
	public void start()
	{
		refresh();
		started=true;
		timer.start();
	}
	
	public void end()
	{
		timer.stop();
		started=false;
		paintScore=true;
		repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(started)
		{
			if(e.getSource()==timer)
			{
				score++;
				
				counter++;
				if(counter>1&&counter%100==0)
				{
					for(int i=0;i<blocks.size();i++)
					{
						Block b=blocks.get(i);
						blocks.get(i).setVx(b.getVx()*(1+50*1.0/Configure.averageV));
						blocks.get(i).setVy(b.getVy()*(1+50*1.0/Configure.averageV));
					}
					counter=0;
				}
				
				for(int i=0;i<blocks.size();i++)
				{
					blocks.get(i).selfProcess();
				}
				
				if(myBlock.hitBlock(blocks))
				{
					end();
				}
			}
		}
		
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		for(int i=0;i<blocks.size();i++)
		{
			blocks.get(i).paint(g, this);
		}
		
		myBlock.paint(g, this);
		
		//paint score
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times", Font.BOLD, 14));
		FontMetrics fm=g.getFontMetrics();
		String scoreStr=""+score+" *10ms";
		g.drawString(scoreStr, BORDER_WIDTH+5, getHeight()-5-BORDER_WIDTH);
		
		//paint final score
		if(paintScore)
		{
			g.setColor(Color.RED);
			g.setFont(new Font("Times", Font.BOLD, 28));
			fm=g.getFontMetrics();
			g.drawString(scoreStr, (getWidth()-fm.stringWidth(scoreStr))/2, (getHeight()-fm.getHeight())/2);
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) 
	{
		if(paintScore)
		{
			paintScore=false;
			refresh();
			return;
		}
		
		if(myBlock.contains(e.getPoint()))
		{	
			if(!started)
			{
				start();
				myBlock.setX(e.getX()-MyBlock.SIZE.width/2);
				myBlock.setY(e.getY()-MyBlock.SIZE.height/2);
			}
			
			dragOn=true;
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		dragOn=false;
	}
	
	public void mouseDragged(MouseEvent e) 
	{
		if (started && dragOn)
		{
			double x = myBlock.getX();
			double y = myBlock.getY();

			myBlock.setX(e.getX() - MyBlock.SIZE.width / 2);
			myBlock.setY(e.getY() - MyBlock.SIZE.height / 2);

			if (myBlock.hitBorder())
			{
				myBlock.setX(x);
				myBlock.setY(y);
			}
		}
	
		
	}

	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
