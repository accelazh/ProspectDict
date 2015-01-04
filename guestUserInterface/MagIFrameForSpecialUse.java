package guestUserInterface;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.*;

import com.ibm.iwt.*;
import com.ibm.iwt.util.*;
import com.ibm.iwt.window.*;

import java.awt.event.*;
import guestUserInterface.magneticWindows.*;

/**
 * 这个类是为了将DictFrame变为具有和其他几个窗口
 * 可以相互发生磁性效应的窗口，而设计的。由于需要
 * 与IFrame相互配合，因此这个类不适合用来制作别的
 * 东西
 * @author ZYL
 *
 */
class MagIFrameForSpecialUse extends IFrame
implements Magnetic, MouseListener, MouseMotionListener
{
	//for debug
	private static final boolean debug=false;
	
	/**
	 * 磁铁效应的开关
	 */
	private boolean magEnable=true; 
	/**
	 * 互相发生磁性作用的窗体的组
	 */
	private MagGroup group;
	/**
	 * 这个标识是否锁定移动，即为true时窗体不能移动,
	 * 在mousePress时赋值false，在mouseRelease
	 * 赋值true,在拖拽移动过程中发生磁性效应，则会
	 * 被设为true;
	 * 
	 */
	private boolean moveLock=true;
	/**
	 * refreshCounter的循环周期
	 */
	private static final int REFRESH_COUNT_SPAN=15;
	/**
	 * 由于每次发起mouseDragged事件都刷新mousePoint和
	 * movingDirection精度不高，因此每隔REFRESH_COUNT_SPAN
	 * 刷新一次，故用到这个计数器
	 */
	private int refreshCounter=0;
	/**
	 * 相对屏幕的鼠标坐标,在mousePress时赋值，在mouseRelease时清为null
	 * 在拖拽移动过程中不断被刷新
	 */
	private Point mousePoint=null;
	/**
	 * 相对于自己的鼠标坐标，在mousePress时赋值，在mouseRelease时清为null
	 * 在拖拽移动过程中保持不变
	 */
	private Point relativeMousePoint=null;
	/**
	 * 窗体当前的移动方向，用一个长度为二的
	 * 数组表示，第一位表示水平方向，第二位表示
	 * 竖直方向
	 * 可选值为NONE,UP,DOWN,LEFT,RIGHT
	 */
	private int[] movingDirection=new int[]{
		NONE,
		NONE,
	};

	
	public MagIFrameForSpecialUse()
	{
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		setUndecorated(true);
		setAlwaysOnTop(true);
	}
	
	public MagIFrameForSpecialUse(MagGroup group)
	{
		if(group!=null)
		{
			group.add(this);
			this.group=group;
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		setUndecorated(true);
		setAlwaysOnTop(true);
	}
	
	/**
	 * 这个方法的有效参数是MagWindow.UP
	 * MagWindow.DOWN,MagWindow.LEFT
	 * MagWindow.RIGHT。
	 * 
	 * 返回这个窗体周围的边界区域，当其它窗体的
	 * 顶点在某个边界区域中时，则会发生磁性作用。
	 * 
	 * 注意同一个窗体的顶点不可能同时在两个边界
	 * 区域中
	 * 
	 * 所有的区域都是相对于屏幕的
	 */
	public Rectangle getSensitiveBound(int direction)
	{
		Rectangle rect=new Rectangle();
		
		if(UP==direction)
		{
			rect.setLocation(getLocation().x+2,getLocation().y-MAG_BOUND);
			rect.setSize(getSize().width-4,2*MAG_BOUND);
			
			return rect;
		}
		
		if(DOWN==direction)
		{
			rect.setLocation(getLocation().x+2,getLocation().y+getSize().height-MAG_BOUND);
			rect.setSize(getSize().width-4,2*MAG_BOUND);
			
			return rect;
		}
		
		if(LEFT==direction)
		{
			rect.setLocation(getLocation().x-MAG_BOUND,getLocation().y);
			rect.setSize(2*MAG_BOUND,getSize().height);
			
			return rect;
		}
		
		if(RIGHT==direction)
		{
			rect.setLocation(getLocation().x+getSize().width-MAG_BOUND,getLocation().y);
			rect.setSize(2*MAG_BOUND,getSize().height);
			
			return rect;
		}
		
		return null;	
	}
	
	/**
	 * 得到这个窗体的顶点的方法，传入参数是
	 * 0,1,2,3,4,5,6,7分别表示从左上角（inclusive）
	 * 沿顺时针方向的四个顶点,以及从上边开始严顺时针方向
	 * 的四条边的中点
	 * 
	 */
	public Point getCornerPoint(int value)
	{
		if(0==value)
		{
			return getLocation();
		}
		
		if(1==value)
		{
			return new Point(getLocation().x+getSize().width,getLocation().y);
		}
		
		if(2==value)
		{
			return new Point(getLocation().x+getSize().width,getLocation().y+getSize().height);
		}
		
		if(3==value)
		{
			return new Point(getLocation().x,getLocation().y+getSize().height);
		}
		
		if(4==value)
		{
			return new Point(getLocation().x+getSize().width/2, getLocation().y);
		}
		
		if(5==value)
		{
			return new Point(getLocation().x+getSize().width, getLocation().y+getSize().height/2);
		}
		
		if(6==value)
		{
			return new Point(getLocation().x+getSize().width/2, getLocation().y+getSize().height);
		}
		
		if(7==value)
		{
			return new Point(getLocation().x, getLocation().y+getHeight()/2);
		}
		
		return null;
	}

	/**
	 * 这个方法与Rectangle.contains方法不同的是，
	 * 如果点落到边界上，会返回false
	 * 
	 */
	private static boolean containWithBound(Rectangle rect, Point p)
	{
		if(null==p||null==rect)
		{
			return false;
		}
		
		/*if ((p.x > rect.getLocation().x)
				&& (p.x < rect.getLocation().x + rect.getSize().width))
		{
			if ((p.y > rect.getLocation().y)
					&& (p.y < rect.getLocation().y + rect.getSize().height)) 
			{
				return true;
			}
		}
	
		return false;*/
		
		return rect.contains(p);
	}
	
	/**
	 * 这个方法传入一个相对于屏幕的点的坐标，
	 * 判断该点进入了这个方法的所有者的哪一
	 * 个边界区域，并返回UP,DOWN,LEFT
	 * RIGHT之一。如果一个都没有进入，则返回
	 * NONE;
	 * 
	 */
	public int findEnterWhichBound(Point point)
	{
		if(null==point)
		{
			return NONE;
		}
		
		if(containWithBound(getSensitiveBound(LEFT),point))
		{
			return LEFT;
		}
		
		if(containWithBound(getSensitiveBound(RIGHT),point))
		{
			return RIGHT;
		}
		
		if(containWithBound(getSensitiveBound(UP),point))
		{
			return UP;
		}
		
		if(containWithBound(getSensitiveBound(DOWN),point))
		{
			return DOWN;
		}
		
		return NONE;
	}
	
	/**
	  这个方法
	  判断传入参数进入了这个方法的所有者的哪一
	  个边界区域，并返回UP,DOWN,LEFT
	  RIGHT之一。如果一个都没有进入，则返回
	  NONE;
	 */
	public int findEnterWhichBound(Magnetic w)
	{
		if(null==w)
		{
			return NONE;
		}
		
		//检验自己的UP边界区域是否被w进入
		Rectangle upBound=getSensitiveBound(UP);
		if(containWithBound(upBound, w.getCornerPoint(2))
				||containWithBound(upBound, w.getCornerPoint(3))
				||containWithBound(upBound, w.getCornerPoint(6)))
		        
		{
			return UP;
		}
		
		//检验自己的DOWN边界区域是否被w进入
		Rectangle downBound=getSensitiveBound(DOWN);
		if(containWithBound(downBound, w.getCornerPoint(0))
				||containWithBound(downBound, w.getCornerPoint(1))
				||containWithBound(downBound, w.getCornerPoint(4)))
		{
			return DOWN;
		}
		
		//检验自己的LEFT边界区域是否被w进入
		Rectangle leftBound=getSensitiveBound(LEFT);
		if(containWithBound(leftBound, w.getCornerPoint(1))
				||containWithBound(leftBound, w.getCornerPoint(2))
				||containWithBound(leftBound, w.getCornerPoint(5)))
		{
			return LEFT;
		}
		
		//检验自己的RIGHT边界区域是否被w进入
		Rectangle rightBound=getSensitiveBound(RIGHT);
		if(containWithBound(rightBound, w.getCornerPoint(0))
				||containWithBound(rightBound, w.getCornerPoint(3))
				||containWithBound(rightBound, w.getCornerPoint(7)))
		{
			return RIGHT;
		}
		
		return NONE;
	}
	
	
	/**
	 * 这个方法根据传入的direction，在认为w进入了这个方法
	 * 的所有者的sensitiveBound的情况下，对w的位置做出调
	 * 整，使其贴近自己，即发生了磁铁效应
	 * 
	 * direction表示w在这个方法的所有者的哪一个边界区域
	 * 
	 */
	public void adjustLocationWhenEnterBound(int direction, Magnetic w)
	{
		if(null==w)
		{
			return;
		}
		
		if(UP==direction)
		{
			int gap=getLocation().y-w.getLocation().y-w.getSize().height;
			w.setLocation(w.getLocation().x, w.getLocation().y+gap);
			return;
		}
		
		if(DOWN==direction)
		{
			int gap=w.getLocation().y-getLocation().y-getSize().height;
			w.setLocation(w.getLocation().x, w.getLocation().y-gap);
			return;
		}
		
		if(LEFT==direction)
		{
			int gap=getLocation().x-w.getLocation().x-w.getSize().width;
			w.setLocation(w.getLocation().x+gap, w.getLocation().y);
			return;
		}
		
		if(RIGHT==direction)
		{
			int gap=w.getLocation().x-getLocation().x-getSize().width;
			w.setLocation(w.getLocation().x-gap, w.getLocation().y);
			return;
		}
		
	}

	/**
	 * 这个方法判断w进入了这个方法的所有者的哪个区域，
	 * 以及自己进入了w的什么区域，
	 * 并自动调整w的位置以发生磁铁效应
	 * 
	 * 如果调整了，即触发了磁铁效应，那么
	 * 就返回true，否则返回false
	 * 
	 */
	public boolean findAndAdjustWithWindow(Magnetic w)
	{
		if(null==w)
		{
			return false;
		}
		
		int direction=findEnterWhichBound(w);
		
		if(direction!=NONE)
		{
			if((UP==direction&&DOWN==w.getMovingDirection()[1])
					||(LEFT==direction&&RIGHT==w.getMovingDirection()[0])
					||(DOWN==direction&&UP==w.getMovingDirection()[1])
					||(RIGHT==direction&&LEFT==w.getMovingDirection()[0]))
					
			{
				adjustLocationWhenEnterBound(direction,w);
				return true;
			}
		}
		
		//从w的角度在进行判断
		direction=w.findEnterWhichBound(this);
		if(direction!=NONE)
		{
			if((UP==direction&&UP==w.getMovingDirection()[1])
				||(DOWN==direction&&DOWN==w.getMovingDirection()[1])
				||(LEFT==direction&&LEFT==w.getMovingDirection()[0])
				||(RIGHT==direction&&RIGHT==w.getMovingDirection()[0]))
			{
				adjustLocationWhenEnterBound(reverse(direction),w);
				return true;
			}
		}
		
		return false;
	}
	
	private int reverse(int direction)
	{
		switch(direction)
		{
		case UP:
		{
			return DOWN;
		}
		case DOWN:
		{
			return UP;
		}
		case LEFT:
		{
			return RIGHT;
		}
		case RIGHT:
		{
			return LEFT;
		}
		default:
		{
			return NONE;
		}
		}
	}

	/**
	 * 这个方法判断这个方法的所有者与group中其他成员间
	 * 是否发生磁性效应，如果发生，则自动调整这个方法的
	 * 所有者的位置，并将其moveLock设为true,再将group
	 * 中相应状态改变，最后返回true
	 */
	public boolean magDetectInGroup()
	{
		if(null==group)
		{
			return false;
		}
		
		for(int i=0;i<group.size();i++)
		{
			if((!group.get(i).equals(this))&&(group.get(i).isMagEnable()))
			{
				if(group.get(i).findAndAdjustWithWindow(this))  //找到了一个和自己发生磁铁效应的窗体
				{
					moveLock=true;
					group.setAStuckWithB(this,group.get(i));
					
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * 这个方法根据direction返回屏幕的磁性敏感区，
	 * 合法的direction的值为UP,DOWN,LEFT,RIGHT
	 * 
	 * 注意一个窗体的顶点不可能同时在两个敏感区域中
	 * 
	 */
	public Rectangle getScreenSensitiveBound(int direction)
	{
        Rectangle rect=new Rectangle();
		
		if(UP==direction)
		{
			rect.setLocation(0,-MAG_BOUND);
			rect.setSize(screenSize.width,2*MAG_BOUND);
			
			return rect;
		}
		
		if(DOWN==direction)
		{
			rect.setLocation(0,screenSize.height-MAG_BOUND);
			rect.setSize(screenSize.width,2*MAG_BOUND);
			
			return rect;
		}
		
		if(LEFT==direction)
		{
			rect.setLocation(-MAG_BOUND,0);
			rect.setSize(2*MAG_BOUND,screenSize.height);
			
			return rect;
		}
		
		if(RIGHT==direction)
		{
			rect.setLocation(screenSize.width-MAG_BOUND,0);
			rect.setSize(2*MAG_BOUND,screenSize.height);
			
			return rect;
		}
		
		return null;	
	}
	
	/**
	 * 这个方法判断point进入了屏幕的那一个边缘区域，
	 * 相应地返回NONE,UP,DOWN,LEFT,RIGHT,
	 * 返回的数组长度为二，第一位表示在水平方向进入了哪个区域
	 * 第二位表示在竖直方向进入了哪些区域
	 */
	public int[] findEnterWhichScreenBound(Point point)
	{
		if(null==point)
		{
			return new int[]{NONE,NONE};
		}
		
		int[] returnVal=new int[2];
		if(containWithBound(getScreenSensitiveBound(UP),point))
		{
			returnVal[1]=UP;
		}
		else
		{
			if(containWithBound(getScreenSensitiveBound(DOWN),point))
			{
				returnVal[1]=DOWN;
			}
			else
			{
				returnVal[1]=NONE;
			}
		}
		
		if(containWithBound(getScreenSensitiveBound(LEFT),point))
		{
			returnVal[0]=LEFT;
		}
		else
		{
		    if(containWithBound(getScreenSensitiveBound(RIGHT),point))
	    	{
	    		returnVal[0]=RIGHT;
	    	}
	    	else
	    	{
	    		returnVal[0]=NONE;
	    	}
	    }
		
		return returnVal;
	}
	
	/**
	 * 这个方法判断自己进入了屏幕的哪一个区域
	 * 可能的返回值:
	 * NONE,UP,DOWN,LEFT,RIGHT
	 * 返回的数组长度为二，第一位表示在水平方向进入了哪个区域
	 * 第二位表示在竖直方向进入了哪些区域
	 */
	public int[] findEnterWhichScreenBound()
	{
		int[] returnVal=new int[]{NONE,NONE};
		int[] temp=null;
		for (int i = 0; i < 4; i++) 
		{
			temp = findEnterWhichScreenBound(getCornerPoint(i)) ;
		    
			if(temp[0]!=NONE)
		    {
		    	returnVal[0]=temp[0];
		    }
			if(temp[1]!=NONE)
			{
				returnVal[1]=temp[1];
			}
			
		}
		
		return returnVal;
	}
	
	/**
	 * 这个方法根据传入的direction，在认为自己进入了屏幕
	 * 的相应磁性敏感区域的情况下，对自己的位置做出调整,
	 * 以实现磁铁效应
	 * 
	 * direction表示进入了屏幕的哪一个方位的敏感区
	 * 
	 */
	public void adjustLocationWhenEnterScreenBound(int[] direction)
	{
		if(UP==direction[1])
		{
			setLocation(getLocation().x,0);
    	}
		
		if(DOWN==direction[1])
		{
			setLocation(getLocation().x, screenSize.height-getSize().height);
		}
		
		if(LEFT==direction[0])
		{
			setLocation(0, getLocation().y);
		}
		
		if(RIGHT==direction[0])
		{
			setLocation(screenSize.width-getSize().width, getLocation().y);
		}
		
	}

	/**
	 * 这个方判断与screen的磁性作用是否发生，并作出位置调整
	 * 
	 */
	public boolean findAndAdjustWithScreen()
	{
		int[] direction=findEnterWhichScreenBound();
		boolean adjusted=false;
		
		if(direction[0]!=NONE)
		{
			if((LEFT==direction[0]&&LEFT==getMovingDirection()[0])
					||(RIGHT==direction[0]&&RIGHT==getMovingDirection()[0]))
					
			{
				adjustLocationWhenEnterScreenBound(new int[]{direction[0], NONE});
				adjusted=true;
			}
		}
		
		if(direction[1]!=NONE)
		{
			if((UP==direction[1]&&UP==getMovingDirection()[1]))  	
			{
				adjustLocationWhenEnterScreenBound(new int[]{NONE, direction[1]});
				adjusted=true;
			}
			else
			{
				//这样检测下方的磁铁效应是为了使用户可以将常口拖放到屏幕之外节省空间
			    if((DOWN==direction[1]&&DOWN==getMovingDirection()[1])) 
			    {
				    int x=getLocation().x;
				    int bound=(screenSize.width-getSize().width)/4;
				    
				    if(x<bound||x+getSize().width>screenSize.width-bound)
				    {
				    	adjustLocationWhenEnterScreenBound(new int[]{NONE, direction[1]});
						adjusted=true;
				    }
				    
		    	}
			}
		}
		
		return adjusted;
		
	}

	/**
	 * 这个方法可以说是专门为DictFrame的特殊形态的最大化而设计，
	 * 这个方法只检测和处理水平方向的与屏幕边缘的磁铁效应
	 */
	public boolean findAndAdjustWithScreenWhenMaxified()
	{
		int[] direction=findEnterWhichScreenBound();
		boolean adjusted=false;
		
		if(direction[0]!=NONE)
		{
			if((LEFT==direction[0]&&LEFT==getMovingDirection()[0])
					||(RIGHT==direction[0]&&RIGHT==getMovingDirection()[0]))
					
			{
				adjustLocationWhenEnterScreenBound(new int[]{direction[0], NONE});
				adjusted=true;
			}
		}
		
		return adjusted;
	}
	
	/**
	 * 这个方法可以说是专门为DictFrame的特殊形态的最大化而设计，
	 * 这个方法只检测和处理水平方向的与屏幕边缘的磁铁效应.
	 * 如果发生了，则自动调整自己的位置，并将自己的moveLock设为
	 * true，然后返回true 
	 * 
	 */
	public boolean magDetectWithScreenWhenMaxified()
	{
		if (findAndAdjustWithScreenWhenMaxified()) 
		{
			moveLock = true;
			return true;
		}
		else
		{
			return false;	
		}
	}
	
	/**
	 * 这个方法判断这个方法的所有者（即自己）与屏幕的边缘
	 * 是否发生磁性效应，如果发生了，则自动调整自己的位置，
	 * 并将自己的moveLock设为true，然后返回true 
	 * 
	 */
	public boolean magDetectWithScreen()
	{
		if (findAndAdjustWithScreen()) 
		{
			moveLock = true;
			return true;
		}
		else
		{
			return false;	
		}
		
	}
	
	/**
	 * 这个方法处理用鼠标托拽移动的事件,
	 * 因该是由mouseDragged(MouseEvent e)
	 * 方法调用，并传入当前鼠标位置(这个鼠标位置
	 * 是相对于屏幕的)。这个方法完成鼠标拖动和磁铁
	 * 效果，对屏幕和同组的窗体都会发生作用
	 * 
	 * 这个方法自动更新movingDirection和mousePoint,
	 * 
	 */
	public void dragMoving(Point newMousePoint)
	{
		if(debug)
		{
			System.out.println("====in method dragMoving====");
		}
		if(null==mousePoint||null==relativeMousePoint)
		{
			return;
		}
		
		//校正坐标
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		if(debug)
		{
			System.out.println("==refresh movingDirection==");
		}
		
		//更新movingDirection
		if(newMousePoint.x>mousePoint.x)
		{
			movingDirection[0]=RIGHT;
		}
		else
		{
			if(newMousePoint.x<mousePoint.x)
			{
				movingDirection[0]=LEFT;
		    }
			else
			{
				movingDirection[0]=NONE;
			}
		}
		
		if(newMousePoint.y>mousePoint.y)
		{
			movingDirection[1]=DOWN;
		}
		else
		{
			if(newMousePoint.y<mousePoint.y)
			{
				movingDirection[1]=UP;
		    }
			else
			{
				movingDirection[1]=NONE;
			}
		}
				
		
		if (refreshCounter>=REFRESH_COUNT_SPAN)
		{
			//更新mousePoint
			if (debug) {
				System.out.println("==refresh mousePoint==");
			}
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//处理移动
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==process moving==");
			}
			
			setLocation(newMousePoint.x-relativeMousePoint.x, newMousePoint.y-relativeMousePoint.y);
		}
		
				
		//处理磁性效应
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==mag detect of screen==");
			}
			magDetectWithScreen();
		}
		
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==mag detect of group==");
			}
			magDetectInGroup();
		}
		
		if(debug)
		{
			System.out.println("====end of method dragMoving====\n\n");
		}
		
		repaint();
	}
	
	/**
	 * 这个方法处理用鼠标托拽移动的事件,
	 * 因该是由mouseDragged(MouseEvent e)
	 * 方法调用，并传入当前鼠标位置(这个鼠标位置
	 * 是相对于屏幕的)。这个方法完成鼠标拖动和磁铁
	 * 效果，只对屏幕发生作用
	 * 
	 * 这个方法自动更新movingDirection和mousePoint
	 * 
	 */
	public void dragMovingOnlyScreenSensitive(Point newMousePoint)
	{
		if(null==mousePoint||null==relativeMousePoint)
		{
			return;
		}
		
		//校正坐标
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		if(debug)
		{
			System.out.println("==refresh movingDirection==");
		}
		
		//更新movingDirection
		if(newMousePoint.x>mousePoint.x)
		{
			movingDirection[0]=RIGHT;
		}
		else
		{
			if(newMousePoint.x<mousePoint.x)
			{
				movingDirection[0]=LEFT;
		    }
			else
			{
				movingDirection[0]=NONE;
			}
		}
		
		if(newMousePoint.y>mousePoint.y)
		{
			movingDirection[1]=DOWN;
		}
		else
		{
			if(newMousePoint.y<mousePoint.y)
			{
				movingDirection[1]=UP;
		    }
			else
			{
				movingDirection[1]=NONE;
			}
		}
				
		
		if (refreshCounter>=REFRESH_COUNT_SPAN)
		{
			//更新mousePoint
			if (debug) {
				System.out.println("==refresh mousePoint==");
			}
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//处理移动
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==process moving==");
			}
			
			setLocation(newMousePoint.x-relativeMousePoint.x, newMousePoint.y-relativeMousePoint.y);
		}
		
				
		//处理磁性效应
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==mag detect of screen==");
			}
			magDetectWithScreen();
		}
		
		repaint();
	}

	/**
	 * 这个方法可以说是专门为DictFrame的特殊形态的最大化而设计，
	 * 用这个方法处理鼠标拖拽，只允许水平移动，不允许垂直移动，在
	 * 水平方向上能够在屏幕边缘产生磁铁效应 
	 * 
	 * 这个方法自动更新movingDirection和mousePoint
	 */
	public void dragMovingWhenMaxified(Point newMousePoint)
	{
		if(null==mousePoint||null==relativeMousePoint)
		{
			return;
		}
		
		//校正坐标
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		//更新movingDirection
		if(newMousePoint.x>mousePoint.x)
		{
			movingDirection[0]=RIGHT;
		}
		else
		{
			if(newMousePoint.x<mousePoint.x)
			{
				movingDirection[0]=LEFT;
		    }
			else
			{
				movingDirection[0]=NONE;
			}
		}
		
		if(newMousePoint.y>mousePoint.y)
		{
			movingDirection[1]=DOWN;
		}
		else
		{
			if(newMousePoint.y<mousePoint.y)
			{
				movingDirection[1]=UP;
		    }
			else
			{
				movingDirection[1]=NONE;
			}
		}
				
		
		if (refreshCounter>=REFRESH_COUNT_SPAN)
		{
			//更新mousePoint
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//处理移动
		if(!moveLock)
		{
			setLocation(newMousePoint.x-relativeMousePoint.x, 0);
		}
		
				
		//处理磁性效应
		if(!moveLock)
		{
			magDetectWithScreenWhenMaxified();
		}
		
		repaint();
	}
	
	public int[] getMovingDirection() {
		return movingDirection;
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		super.mouseClicked(e);
		
	}

	public void mouseEntered(MouseEvent e) 
	{
		super.mouseEntered(e);		
	}

	public void mouseExited(MouseEvent e)
	{
		super.mouseExited(e);
		
	}

	public void mousePressedProcess(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) 
		{
			if(debug)
			{
				System.out.println("mousePressed and init values");
			}
			moveLock = false;
			mousePoint = new Point(getLocation().x + e.getX(), getLocation().y
					+ e.getY());
			relativeMousePoint = e.getPoint();
			refreshCounter=0;
		}
	}
	
	public void mousePressed(MouseEvent e) 
	{
		super.mousePressed(e);
	}

	public void mouseReleasedProcess(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) 
		{
			if(debug)
			{
				System.out.println("mouseReleased and clear values");
			}
			moveLock = true;
			mousePoint = null;
			relativeMousePoint = null;
			movingDirection=new int[]{
					NONE,
					NONE
			};
			refreshCounter=0;
		}
		
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		super.mouseReleased(e);			
	}

	public void mouseDragged(MouseEvent e) 
	{
		super.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) 
	{
		super.mouseMoved(e);
	}
	

	public MagGroup getGroup() {
		return group;
	}

	public void setGroup(MagGroup group) {
		this.group = group;
	}
	
	public boolean isMagEnable() {
		return magEnable;
	}

	public void setMagEnable(boolean magEnable) {
		this.magEnable = magEnable;
	}
}
