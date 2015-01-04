package games.survive;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * 
 * @author 描述飞船的类
 *
 */
public class Craft implements Constants
{
	public static final Dimension SIZE=new Dimension(20,20);
	public static final ImageIcon ICON=new ImageIcon("GUISource/games/survive/craft.gif");
	public static final ImageIcon ICON_DESTORY=new ImageIcon("GUISource/games/survive/craft_destory.gif");
	
	//描述在游戏过程中不断变化的属性
	private double x=(GamePanel.DEFAULT_SIZE.width-SIZE.width)/2;
	private double y=GamePanel.DEFAULT_SIZE.height-SIZE.height-10;
	
	private boolean destroyed=false;
	
	
	public Craft()
	{
		
	}
	
	/**
	 * 游戏开始时的初始化
	 */
	public void initialize()
	{
		x=(GamePanel.DEFAULT_SIZE.width-SIZE.width)/2;
		y=GamePanel.DEFAULT_SIZE.height-SIZE.height-10;
		
		destroyed=false;
	}
	
	/**
	 * 按照direction发生一次位移，
	 * 
	 * hDir的合法值为：
	 * Constants.LEFT,
	 * Constants.RIGHT,
	 * Constants.NONE;
	 * 
	 * vDir的合法值为：
	 * Constants.UP,
	 * Constants.DOWN,
	 * Constants.NONE;
	 * 
	 * 
	 * 移动的长度是velocity*Constants.TIMER_INTERVAL*1.0/1000
	 * 
	 * @param hDir 水平移动的方向
	 * @param vDir 竖直移动的方向
	 */
	public void move(int hDir, int vDir)
	{
		int hd=0;
		int vd=0;
		
		switch(hDir)
		{
		case LEFT:
		{
			hd = -1;
			break;
		}
		
		case RIGHT:
		{
			hd=1;
			break;
		}
		
		default:
		{
			break;
		}
		}
		
		switch(vDir)
		{
		case UP:
		{
			vd = -1;
			break;
		}
		
		case DOWN:
		{
			vd=1;
			break;
		}
		
		default:
		{
			break;
		}
		}
		
		//判断是否在游戏界面内
		if(x<0)
		{
			x=0;
			return;
		}
		
		if(x+SIZE.width>GamePanel.DEFAULT_SIZE.width)
		{
			x=GamePanel.DEFAULT_SIZE.width-SIZE.width;
			return;
		}
		
		if(y<0)
		{
			y=0;
			return;
		}

		if(y+SIZE.height>GamePanel.DEFAULT_SIZE.height)
		{
			y=GamePanel.DEFAULT_SIZE.height-SIZE.height;
			return;
		}		
		
		//调整位置
		double length=Math.pow(hd*hd+vd*vd, 0.5);
		
		if(length<1e-6)
		{
			return;
		}
		
		double vx=ConfigValues.getCraftAverageV()*hd*1.0/length;
		double vy=ConfigValues.getCraftAverageV()*vd*1.0/length;
		
		x+=vx*TIMER_INTERVAL*1.0/1000;
		y+=vy*TIMER_INTERVAL*1.0/1000;
		
	}
	
	public void paint(Graphics g, ImageObserver o)
	{
		if(!destroyed)
		{
			g.drawImage(ICON.getImage(), (int)x, (int)y, SIZE.width, SIZE.height, o);
		}
		else
		{
			g.drawImage(ICON_DESTORY.getImage(), (int)x, (int)y, SIZE.width, SIZE.height, o);
		}
	}
	
	
	/**
	 * 判断是否和子弹相撞
	 * @param ball
	 * @return
	 */
	public boolean isCollided(Ball ball)
	{
		if(null==ball)
		{
			return false;
		}
		
		Polygon bound=new Polygon();
		bound.addPoint((int)(x+SIZE.width/2), (int)y);
		bound.addPoint((int)x, (int)(y+SIZE.height));
		bound.addPoint((int)(x+SIZE.width), (int)(y+SIZE.height));
		
		Area area=new Area(bound);
		
		return area.contains(new Point((int)ball.getX(), (int)ball.getY()));
		
	}

	/**
	 * 当飞船被击毁时调用
	 */
	public void beDestory()
	{
		this.destroyed=true;
	}
	
	/** hDir的合法值为：
	 * Constants.LEFT,
	 * Constants.RIGHT,
	 * Constants.NONE;
	 * 
	 * vDir的合法值为：
	 * Constants.UP,
	 * Constants.DOWN,
	 * Constants.NONE;
	 */
	public void selfProcess(int hDir, int vDir, List<Ball> balls)
	{
		if(destroyed)
		{
			return;
		}
		
		this.move(hDir, vDir);
		
		Iterator<Ball> iterator=balls.iterator();
		while(iterator.hasNext())
		{
			if(isCollided(iterator.next()))
			{
				beDestory();
				break;
			}
		}
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public boolean isDestroyed()
	{
		return destroyed;
	}
	
	
	

}
