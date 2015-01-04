package games.moonLanding;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;

public class GamePanel extends JPanel 
implements ActionListener, KeyListener, ComponentListener
{
	//for debug
	private static final boolean debug=false;	

	public static final File GRADES_FILE=new File("games/moonLanding/grades.txt");
	
	public static final int TIMER_INTERVAL=10;
	public static final Dimension DEFAULT_SIZE=new Dimension(500,400);
	public static final Dimension CRAFT_SIZE=new Dimension(20,20);
	public static final Dimension FRAME_SIZE=new Dimension(25,6);  //火焰的大小
	public static final double LAND_HEIGHT=1.0/4;  //陆地占总界面高度的比例
	public static final double START_HEIGHT=7.0/8;  //起始时飞船的高度
	public static final Dimension MOUNTAIN_SIZE=new Dimension(133, 150);  //这个是默认的大小，在窗口的大小变化后，大小可能不是这个了，因此后面要用比例来表示大小
	public static final double MOUNTAIN_X=(DEFAULT_SIZE.width-MOUNTAIN_SIZE.width)/2.0/DEFAULT_SIZE.width;
	public static final double MOUNTAIN_Y=(DEFAULT_SIZE.height-DEFAULT_SIZE.height*LAND_HEIGHT-MOUNTAIN_SIZE.height)/1.0/DEFAULT_SIZE.height;
	public static final double MOUNTAIN_W=MOUNTAIN_SIZE.width*1.0/DEFAULT_SIZE.width;
	public static final double MOUNTAIN_H=MOUNTAIN_SIZE.height*1.0/DEFAULT_SIZE.height;
	
	public final ImageIcon CRAFT_ICON=new ImageIcon("GUISource/games/moonLanding/craft.gif");
	public final ImageIcon CRAFT_DESTORYED_ICON=new ImageIcon("GUISource/games/moonLanding/craft2.gif");
	public final ImageIcon BK_ICON=new ImageIcon("GUISource/games/moonLanding/bk.gif");
	public final ImageIcon LAND_ICON=new ImageIcon("GUISource/games/moonLanding/land.gif");
	public final ImageIcon FRAME_LEFT_ICON=new ImageIcon("GUISource/games/moonLanding/frameLeft.gif");
	public final ImageIcon FRAME_RIGHT_ICON=new ImageIcon("GUISource/games/moonLanding/frameRight.gif");
	public final ImageIcon FRAME_UP_ICON=new ImageIcon("GUISource/games/moonLanding/frameUp.gif");
	public final ImageIcon FRAME_DOWN_ICON=new ImageIcon("GUISource/games/moonLanding/frameDown.gif");
	public final ImageIcon MOUNTAIN_ICON=new ImageIcon("GUISource/games/moonLanding/mountain.gif");
	
	private boolean started=false;  //标识游戏是否已经开始
	private Dimension currentSize;  //记录当前的屏幕大小 
	
	private int heighScore=-1;   //最小完成任务的时间,单位：毫秒
	
	private Timer timer=new Timer(TIMER_INTERVAL,this);
	
	//飞船属性设定
	private double vLimit=ConfigWrap.default_vLimit;   //接触月球的时候，只有速度在这一下才算成功
	private double gravity=ConfigWrap.default_gravity;  //像素/s^2
	private double leftAcc=ConfigWrap.default_leftAcc;  //飞船左喷射口的喷射加速度
	private double rightAcc=ConfigWrap.default_rightAcc;  //飞船的右喷射口的喷射加速度
	private double downAcc=ConfigWrap.default_downAcc;
	private double upAcc=ConfigWrap.default_upAcc;
	private int totalFuel=ConfigWrap.default_totalFuel;
	
	//飞船状态设定
	private ImageIcon craft_img=CRAFT_ICON;
	private double vx=0;  //单位：像素/s^2
	private double vy=0;
	private double x=0;
	private double y=0;
	private int score=0;   //当前消耗的时间
	private boolean left=false;  //左喷射口是否工作，下同
	private boolean right=false;
	private boolean down=false;
	private boolean up=false;
	private int fuel=totalFuel;
	
	public GamePanel()
	{
		this.setPreferredSize(DEFAULT_SIZE);
		this.setSize(DEFAULT_SIZE);
		currentSize=DEFAULT_SIZE;
		
		this.addKeyListener(this);
		this.addComponentListener(this);
		
		this.requestFocusInWindow();
		
		refresh();
	}
	
	/**
	 * 山的几何大小用多边形表示
	 */
	public Polygon getMountainPolygon()
	{
		Polygon mp=new Polygon();
		mp.addPoint((int)(currentSize.width*(MOUNTAIN_X+0.5*MOUNTAIN_W)), (int)(currentSize.height*MOUNTAIN_Y));
		mp.addPoint((int)(currentSize.width*MOUNTAIN_X), (int)(currentSize.height*(MOUNTAIN_Y+MOUNTAIN_H)));
		mp.addPoint((int)(currentSize.width*(MOUNTAIN_X+MOUNTAIN_W)), (int)(currentSize.height*(MOUNTAIN_Y+MOUNTAIN_H)));
		
		return mp;
	}
	
	/**
	 * 该方法将游戏状态重置，调整到开始状态
	 */
	public void refresh()
	{
		craft_img=CRAFT_ICON;
		vx = 0;
		vy = 0;
		x=(currentSize.width-CRAFT_SIZE.width)/2;
		y = currentSize.height * (1 - START_HEIGHT);
		score = 0;
		left = false;
		right = false;
		down = false;
		up = false;
		fuel=totalFuel;
	}
	
	public void startGame()
	{
		refresh();
		started=true;
		timer.start();
	}
	
	public void endGame()
	{
		started=false;
		timer.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==timer)
		{
			if(started)
			{
				if(fuel<=0)
				{
					left = false;
					right = false;
					down = false;
					up = false;
					
					fuel=0;
				}
				
				//调整位移
				x+=vx*TIMER_INTERVAL*1.0/1000;
				y+=vy*TIMER_INTERVAL*1.0/1000;
				
				//调整速度
			    vx+=left?(leftAcc*TIMER_INTERVAL*1.0/1000):0;
				vx-=right?(rightAcc*TIMER_INTERVAL*1.0/1000):0;
				
				vy+=up?(upAcc*TIMER_INTERVAL*1.0/1000):0;
				vy-=down?(downAcc*TIMER_INTERVAL*1.0/1000):0;
				vy+=gravity*TIMER_INTERVAL*1.0/1000;
				
				if(up)
				{
					fuel--;
				}
				
				if(down)
				{
					fuel--;
				}
				
				if(left)
				{
					fuel--;
				}
				
				if(right)
				{
					fuel--;
				}
				
				//调整时间
				score+=1;
				
				//检查是否撞山
				Rectangle craftRect=new Rectangle((int)x, (int)y, CRAFT_SIZE.width, CRAFT_SIZE.height);
				Polygon mp=getMountainPolygon();
				
				if(mp.intersects(craftRect))
				{
					failed();
				}
				
				//检查游戏是否结束
				if(y+CRAFT_SIZE.height>=currentSize.height*(1-LAND_HEIGHT))
				{
					double v=Math.pow(vx*vx+vy*vy , 0.5);
					if(v>=vLimit)
					{
						failed();
					}
					else
					{
						succeeded();
					}
				}
				
				//后续处理
				repaint();
				
			}
		}
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
	 * 登月失败
	 */
	public void failed()
	{
		endGame();
		craft_img=CRAFT_DESTORYED_ICON;		
		repaint();
	}
	
	/**
	 * 登月成功
	 */
	public void succeeded()
	{
		endGame();
		if(heighScore>0)
		{
			heighScore=Math.min(heighScore, score);
		}
		else
		{
			heighScore=score;
		}
		repaint();
		
		//在文件中记录成绩
		try
		{
			PrintWriter out=new PrintWriter(new FileWriter(GRADES_FILE, true));
			
			if(GRADES_FILE.exists())
			{
				if(debug)
				{
					System.out.println("append grades");
				}
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
		
		output+="Time: "+score*10+" ms"+"\n";
		output+="Velocity: "+(int)Math.pow(vx*vx+vy*vy,0.5)+" pts/s"+"\n";
		output+="Fuel: "+this.fuel+" unit"+"\n";
		output+="\n";
		
		output+="====Current Settings "+"====\n";
		output+="Velocity Limit: "+vLimit+" pts/s"+"\n";
		output+="Gravity: "+(int)gravity+" pts/s^2"+"\n";
		output+="Down Accelation: "+(int)downAcc+" pts/s^2"+"\n";
		output+="Left Accelation: "+(int)leftAcc+" pts/s^2"+"\n";
		output+="Right Accelation: "+(int)rightAcc+" pts/s^2"+"\n";
		output+="Up Accelation: "+(int)upAcc+" pts/s^2"+"\n";
		output+="Total Fuel: "+totalFuel+" unit"+"\n";
		output+="=======================================================";
		
		output+="\n";
		
		return output;
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
			this.gravity=wrap.getGravity();
			this.vLimit=wrap.getVLimit();
			this.downAcc=wrap.getDownAcc();
			this.leftAcc=wrap.getLeftAcc();
			this.rightAcc=wrap.getRightAcc();
			this.upAcc=wrap.getUpAcc();
			this.totalFuel=wrap.getTotalFuel();
			
			this.fuel=totalFuel;
			
			repaint();
		}
	}
	
	/**
	 * 显示配置面板
	 */
	public void showConfigDialog()
	{
		new ConfigDialog(this);
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (fuel > 0)
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
			startGame();
		}
		
		//显示控制面板的操作
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE&&!started)
		{
			showConfigDialog();
		}
		
	}

	 
	public void keyTyped(KeyEvent e)
	{
	}
	
	 
	public void componentHidden(ComponentEvent e)
	{
		if(started)
		{
			timer.stop();
		}
	}

	 
	public void componentMoved(ComponentEvent e)
	{
		
	}

	 
	public void componentResized(ComponentEvent e)
	{
		if(debug)
		{
			System.out.println("x: "+x);
			System.out.println("y: "+y);
			System.out.println("currentSize: "+currentSize);
			System.out.println("getSize(): "+getSize()+"\n");
		}
		
		//校正飞船坐标
		x=x/currentSize.width*getSize().width;
		y=y/currentSize.height*getSize().height;
		
		currentSize=this.getSize();
		
		repaint();
	}

	 
	public void componentShown(ComponentEvent e)
	{
		if(started)
		{
			timer.start();
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw background
	    g.drawImage(BK_ICON.getImage(), 0, 0, getWidth(), getHeight(), this);
	
	    //draw mountain
	    g.drawImage(MOUNTAIN_ICON.getImage(), (int)(getWidth()*MOUNTAIN_X), (int)(getHeight()*MOUNTAIN_Y)+1, (int)(getWidth()*MOUNTAIN_W), (int)(getHeight()*MOUNTAIN_H), this);
	    	    
	    //draw land
	    g.drawImage(LAND_ICON.getImage(), 0, (int)(getHeight()*(1-LAND_HEIGHT)), getWidth(), (int)(getHeight()*LAND_HEIGHT), this);
		   
	    //draw craft
	    g.drawImage(craft_img.getImage(), (int)x, (int)y, CRAFT_SIZE.width, CRAFT_SIZE.height, this);
	    
	    //draw frames
	   
	    if (started)
		{
			if (left)
			{
				g.drawImage(FRAME_LEFT_ICON.getImage(),
						(int) (x - FRAME_SIZE.width), (int) (y
								+ CRAFT_SIZE.height - FRAME_SIZE.height),
						FRAME_SIZE.width, FRAME_SIZE.height, this);
			}
			if (right)
			{
				g.drawImage(FRAME_RIGHT_ICON.getImage(),
						(int) (x + CRAFT_SIZE.width), (int) (y
								+ CRAFT_SIZE.height - FRAME_SIZE.height),
						FRAME_SIZE.width, FRAME_SIZE.height, this);
			}
			if (down)
			{
				g.drawImage(FRAME_DOWN_ICON.getImage(),
						(int) (x + (CRAFT_SIZE.width - FRAME_SIZE.height) / 2),
						(int) (y + CRAFT_SIZE.height), FRAME_SIZE.height,
						FRAME_SIZE.width, this);
			}
			if (up)
			{
				g.drawImage(FRAME_UP_ICON.getImage(),
						(int) (x + (CRAFT_SIZE.width - FRAME_SIZE.height) / 2),
						(int) (y - FRAME_SIZE.width), FRAME_SIZE.height,
						FRAME_SIZE.width, this);
			}
		}
		//draw info
	    String info=null;
	    if(!started)
	    {
	    	info="ENTER to start, ESC to config";
	    }
	    else
	    {
	    	info="Land safely as fast as possible";
	    }
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Times", Font.BOLD, 10));
	    FontMetrics fm=g.getFontMetrics();
	    g.drawString(info, (getWidth()-fm.stringWidth(info))/2, getHeight()-5);
	    
	    //draw velocity, limitVelcity and fuel
	    String velocity="Velocity: "+(int)Math.pow(vx*vx+vy*vy,0.5)+" pts/s";
	    String limitV="Dangerous: "+(int)vLimit+" pts/s";
	    String fuel="Fuel: "+this.fuel+" unit";
	    int maxWidth=Math.max(fm.stringWidth(velocity), fm.stringWidth(limitV));
	    maxWidth=Math.max(fm.stringWidth(fuel), maxWidth);
	    
	    g.drawString(limitV, getWidth()-5-maxWidth, getHeight()-5);
	    g.drawString(velocity, getWidth()-5-maxWidth, getHeight()-5-fm.getHeight());
	    g.drawString(fuel, getWidth()-5-maxWidth, getHeight()-5-2*fm.getHeight());
	    
	    //draw score
	    String time="Time: "+score*10+" ms";
	    String lowestTime=null;
	    if(heighScore>0)
	    {
	    	lowestTime="Record: "+heighScore*10+" ms";
	    }
	    else
	    {
	    	lowestTime="Record: "+"empty"+" ms";
	    }
	    g.drawString(time, 5, getHeight()-5-fm.getHeight());
	    g.drawString(lowestTime, 5, getHeight()-5);
	}
	

}
