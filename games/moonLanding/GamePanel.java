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
	public static final Dimension FRAME_SIZE=new Dimension(25,6);  //����Ĵ�С
	public static final double LAND_HEIGHT=1.0/4;  //½��ռ�ܽ���߶ȵı���
	public static final double START_HEIGHT=7.0/8;  //��ʼʱ�ɴ��ĸ߶�
	public static final Dimension MOUNTAIN_SIZE=new Dimension(133, 150);  //�����Ĭ�ϵĴ�С���ڴ��ڵĴ�С�仯�󣬴�С���ܲ�������ˣ���˺���Ҫ�ñ�������ʾ��С
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
	
	private boolean started=false;  //��ʶ��Ϸ�Ƿ��Ѿ���ʼ
	private Dimension currentSize;  //��¼��ǰ����Ļ��С 
	
	private int heighScore=-1;   //��С��������ʱ��,��λ������
	
	private Timer timer=new Timer(TIMER_INTERVAL,this);
	
	//�ɴ������趨
	private double vLimit=ConfigWrap.default_vLimit;   //�Ӵ������ʱ��ֻ���ٶ�����һ�²���ɹ�
	private double gravity=ConfigWrap.default_gravity;  //����/s^2
	private double leftAcc=ConfigWrap.default_leftAcc;  //�ɴ�������ڵ�������ٶ�
	private double rightAcc=ConfigWrap.default_rightAcc;  //�ɴ���������ڵ�������ٶ�
	private double downAcc=ConfigWrap.default_downAcc;
	private double upAcc=ConfigWrap.default_upAcc;
	private int totalFuel=ConfigWrap.default_totalFuel;
	
	//�ɴ�״̬�趨
	private ImageIcon craft_img=CRAFT_ICON;
	private double vx=0;  //��λ������/s^2
	private double vy=0;
	private double x=0;
	private double y=0;
	private int score=0;   //��ǰ���ĵ�ʱ��
	private boolean left=false;  //��������Ƿ�������ͬ
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
	 * ɽ�ļ��δ�С�ö���α�ʾ
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
	 * �÷�������Ϸ״̬���ã���������ʼ״̬
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
				
				//����λ��
				x+=vx*TIMER_INTERVAL*1.0/1000;
				y+=vy*TIMER_INTERVAL*1.0/1000;
				
				//�����ٶ�
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
				
				//����ʱ��
				score+=1;
				
				//����Ƿ�ײɽ
				Rectangle craftRect=new Rectangle((int)x, (int)y, CRAFT_SIZE.width, CRAFT_SIZE.height);
				Polygon mp=getMountainPolygon();
				
				if(mp.intersects(craftRect))
				{
					failed();
				}
				
				//�����Ϸ�Ƿ����
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
				
				//��������
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
	 * ����ʧ��
	 */
	public void failed()
	{
		endGame();
		craft_img=CRAFT_DESTORYED_ICON;		
		repaint();
	}
	
	/**
	 * ���³ɹ�
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
		
		//���ļ��м�¼�ɼ�
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
	 * �õ��ɼ�
	 * @return ��һϵ�гɼ���ɵĳɼ�����
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
	 * ��ConfigWrap��װ�Ĳ���ֵ�������趨������
	 * ֻ�е�start==false,����wrap.checkValid()
	 * ==true��ʱ��Ż���Ч
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
	 * ��ʾ�������
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
		
		// ��ʼ��Ϸ�Ĳ���
		if(e.getKeyCode()==KeyEvent.VK_ENTER&&!started)
		{
			startGame();
		}
		
		//��ʾ�������Ĳ���
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
		
		//У���ɴ�����
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
