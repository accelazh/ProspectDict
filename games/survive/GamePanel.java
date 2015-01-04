package games.survive;

import java.awt.*;

import javax.swing.*;

import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 游戏的主界面
 * @author ZYL
 *
 */
public class GamePanel extends JPanel
implements Constants, ActionListener, KeyListener
{
	public static final Dimension DEFAULT_SIZE=new Dimension(200,300);
	public static final File GRADES_FILE=new File("games/survive/grades.txt");
	
	private boolean started=false;  //标识游戏是否已经开始
	private int heighScore=-1;   //坚持的最高的时间,单位：毫秒
	private javax.swing.Timer timer=new javax.swing.Timer(TIMER_INTERVAL,this);
	
	//游戏过程中变化的量
	private int score=0;   //当前坚持的时间
	private Craft craft=new Craft();
	private java.util.List<Ball> balls=new LinkedList<Ball>();
	
	private boolean left=false;  //指示移动方向，下同
	private boolean right=false;
	private boolean down=false;
	private boolean up=false;
	
	private int counter=0;  //控制增加子弹的counter
	
	public GamePanel()
	{
		this.setPreferredSize(DEFAULT_SIZE);
		this.setBackground(Color.BLACK);
		
		this.setOpaque(true);
		this.addKeyListener(this);
		
		refresh();
	}
	
	/**
	 * 游戏开始的时候初始化
	 */
	public void refresh()
	{
		craft.initialize();
		balls.clear();
		score=0;
		
		left=false;
		right=false;
		down=false;
		up=false;
	}
	
	public void start()
	{
		refresh();
		started=true;
		timer.start();
	}
	
	public void stop(boolean succ)
	{
		started=false;
		timer.stop();
		
		if(succ)
		{
			if(heighScore>0)
			{
				heighScore=Math.max(heighScore, score);
			}
			else
			{
				heighScore=score;
			}
		}
		else
		{
			if(heighScore>0)
			{
				heighScore=Math.max(heighScore, score);
			}
			else
			{
				heighScore=score;
			}
		}
		
		//纪录分数
		//在文件中记录成绩
		try
		{
			PrintWriter out=new PrintWriter(new FileWriter(GRADES_FILE, true));
			
			if(GRADES_FILE.exists())
			{
				out.append(getGrades());
			}
			else
			{
				out.print(getGrades());
			}
			
			out.close();
			
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		repaint();
	}
	
	/**
	 * 得到成绩
	 * @return 有一系列成绩组成的成绩报告
	 */
	public String getGrades()
	{
		String output="";
		output+="\n";
		
		output+="=======================================================\n";
		output+="Achieved When: "+(new java.util.Date().toString())+"\n\n";
		
		output+="Time: "+score+"\n";
		output+="\n";
		
		output+="====Current Settings "+"====\n";
		output+="Ball Average Velocity: "+ConfigValues.getBallAverageV()+" pts/s"+"\n";
		output+="Craft Velocity: "+ConfigValues.getCraftAverageV()+" pts/s^2"+"\n";
		output+="Ball Generating Cycle: "+ConfigValues.getBallGenerageCycle()+" 10ms"+"\n";
		output+="=======================================================";
		
		output+="\n";
		
		return output;
	}
	
	public void pause()
	{
		timer.stop();
	}
	
	public void resume()
	{
		if(started)
		{
			timer.start();
		}
	}
	
	/**
	 * 用ConfigWrap包装的参数值来重新设定参数，
	 * 只有当start==false,并且wrap.checkValid()
	 * ==true的时候才会生效
	 * 
	 * @param wrap
	 */
	public void setValues(ConfigWrap wrap)
	{
		if(!started&&wrap!=null&&wrap.checkValid())
		{
			ConfigValues.setBallAverageV(wrap.getBallAV());
			ConfigValues.setCraftAverageV(wrap.getCraftV());
			ConfigValues.setBallGenerageCycle(wrap.getBallGT());
			
			repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(started)
		{
			if(e.getSource()==timer)
			{
				//调整时间
				score++;
				
				//刷新子弹
				for(int i=0;i<balls.size();i++)
				{
					Ball ball=balls.get(i);
					ball.selfProcess();
					if(!ball.isValid())
					{
						balls.remove(ball);
					}
				}
				
				//刷新craft
				int hDir=NONE;
				if(left&&!right)
				{
					hDir=LEFT;
				}
				else
				{
					if(!left&&right)
					{
						hDir=RIGHT;
					}
				}
				
				int vDir=NONE;
				if(up&&!down)
				{
					vDir=UP;
				}
				else
				{
					if(!up&&down)
					{
						vDir=DOWN;
					}
				}
				
				craft.selfProcess(hDir, vDir, balls);
				
				//新建子弹
				if(counter>=ConfigValues.getBallGenerageCycle())
				{
					balls.add(Ball.createBall());
					counter=0;
				}
				else
				{
					counter++;
				}
				
				//检测是否结束游戏
				if(craft.isDestroyed())
				{
					stop(false);
				}
				
			}
		}
		
		//后续处理
		repaint();
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == 'W')
		{
			up = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == 'S')
		{
			down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == 'A')
		{
			left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 'D')
		{
			right = true;
		}
		
	}

	 
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == 'W')
		{
			up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == 'S')
		{
			down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == 'A')
		{
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 'D')
		{
			right = false;
		}
		
		// 开始游戏的操作
		if(e.getKeyCode()==KeyEvent.VK_ENTER&&!started)
		{
			start();
		}
		
		//显示控制面板的操作
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE&&!started)
		{
			showConfigDialog();
		}
		
	}
	
	/**
	 * 显示配置面板
	 */
	public void showConfigDialog()
	{
		new ConfigDialog(this);
	}

	public void keyTyped(KeyEvent e)
	{
		
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw balls
		Iterator<Ball> iterator=balls.iterator();
		while(iterator.hasNext())
		{
			Ball ball=iterator.next();
			ball.paint(g);
		}
		
		//draw craft
		craft.paint(g, this);
		
		//draw info
	    String info=null;
	    if(!started)
	    {
	    	info="ENTER to start, ESC to config";
	    }
	    else
	    {
	    	info="Hold on!!!";
	    }
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Times", Font.BOLD, 10));
	    FontMetrics fm=g.getFontMetrics();
	    g.drawString(info, (getWidth()-fm.stringWidth(info))/2, fm.getHeight());
	    
	    //draw score
	    String time="Time: "+score;
	    String lowestTime=null;
	    if(heighScore>0)
	    {
	    	lowestTime="Record: "+heighScore;
	    }
	    else
	    {
	    	lowestTime="Record: "+"empty";
	    }
	    g.drawString(time, 5, getHeight()-5-fm.getHeight());
	    g.drawString(lowestTime, 5, getHeight()-5);
	}
	
}
