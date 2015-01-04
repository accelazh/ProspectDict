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
 * �������Ϊ�˽�DictFrame��Ϊ���к�������������
 * �����໥��������ЧӦ�Ĵ��ڣ�����Ƶġ�������Ҫ
 * ��IFrame�໥��ϣ��������಻�ʺ������������
 * ����
 * @author ZYL
 *
 */
class MagIFrameForSpecialUse extends IFrame
implements Magnetic, MouseListener, MouseMotionListener
{
	//for debug
	private static final boolean debug=false;
	
	/**
	 * ����ЧӦ�Ŀ���
	 */
	private boolean magEnable=true; 
	/**
	 * ���෢���������õĴ������
	 */
	private MagGroup group;
	/**
	 * �����ʶ�Ƿ������ƶ�����Ϊtrueʱ���岻���ƶ�,
	 * ��mousePressʱ��ֵfalse����mouseRelease
	 * ��ֵtrue,����ק�ƶ������з�������ЧӦ�����
	 * ����Ϊtrue;
	 * 
	 */
	private boolean moveLock=true;
	/**
	 * refreshCounter��ѭ������
	 */
	private static final int REFRESH_COUNT_SPAN=15;
	/**
	 * ����ÿ�η���mouseDragged�¼���ˢ��mousePoint��
	 * movingDirection���Ȳ��ߣ����ÿ��REFRESH_COUNT_SPAN
	 * ˢ��һ�Σ����õ����������
	 */
	private int refreshCounter=0;
	/**
	 * �����Ļ���������,��mousePressʱ��ֵ����mouseReleaseʱ��Ϊnull
	 * ����ק�ƶ������в��ϱ�ˢ��
	 */
	private Point mousePoint=null;
	/**
	 * ������Լ���������꣬��mousePressʱ��ֵ����mouseReleaseʱ��Ϊnull
	 * ����ק�ƶ������б��ֲ���
	 */
	private Point relativeMousePoint=null;
	/**
	 * ���嵱ǰ���ƶ�������һ������Ϊ����
	 * �����ʾ����һλ��ʾˮƽ���򣬵ڶ�λ��ʾ
	 * ��ֱ����
	 * ��ѡֵΪNONE,UP,DOWN,LEFT,RIGHT
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
	 * �����������Ч������MagWindow.UP
	 * MagWindow.DOWN,MagWindow.LEFT
	 * MagWindow.RIGHT��
	 * 
	 * �������������Χ�ı߽����򣬵����������
	 * ������ĳ���߽�������ʱ����ᷢ���������á�
	 * 
	 * ע��ͬһ������Ķ��㲻����ͬʱ�������߽�
	 * ������
	 * 
	 * ���е��������������Ļ��
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
	 * �õ��������Ķ���ķ��������������
	 * 0,1,2,3,4,5,6,7�ֱ��ʾ�����Ͻǣ�inclusive��
	 * ��˳ʱ�뷽����ĸ�����,�Լ����ϱ߿�ʼ��˳ʱ�뷽��
	 * �������ߵ��е�
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
	 * ���������Rectangle.contains������ͬ���ǣ�
	 * ������䵽�߽��ϣ��᷵��false
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
	 * �����������һ���������Ļ�ĵ�����꣬
	 * �жϸõ��������������������ߵ���һ
	 * ���߽����򣬲�����UP,DOWN,LEFT
	 * RIGHT֮һ�����һ����û�н��룬�򷵻�
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
	  �������
	  �жϴ��������������������������ߵ���һ
	  ���߽����򣬲�����UP,DOWN,LEFT
	  RIGHT֮һ�����һ����û�н��룬�򷵻�
	  NONE;
	 */
	public int findEnterWhichBound(Magnetic w)
	{
		if(null==w)
		{
			return NONE;
		}
		
		//�����Լ���UP�߽������Ƿ�w����
		Rectangle upBound=getSensitiveBound(UP);
		if(containWithBound(upBound, w.getCornerPoint(2))
				||containWithBound(upBound, w.getCornerPoint(3))
				||containWithBound(upBound, w.getCornerPoint(6)))
		        
		{
			return UP;
		}
		
		//�����Լ���DOWN�߽������Ƿ�w����
		Rectangle downBound=getSensitiveBound(DOWN);
		if(containWithBound(downBound, w.getCornerPoint(0))
				||containWithBound(downBound, w.getCornerPoint(1))
				||containWithBound(downBound, w.getCornerPoint(4)))
		{
			return DOWN;
		}
		
		//�����Լ���LEFT�߽������Ƿ�w����
		Rectangle leftBound=getSensitiveBound(LEFT);
		if(containWithBound(leftBound, w.getCornerPoint(1))
				||containWithBound(leftBound, w.getCornerPoint(2))
				||containWithBound(leftBound, w.getCornerPoint(5)))
		{
			return LEFT;
		}
		
		//�����Լ���RIGHT�߽������Ƿ�w����
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
	 * ����������ݴ����direction������Ϊw�������������
	 * �������ߵ�sensitiveBound������£���w��λ��������
	 * ����ʹ�������Լ����������˴���ЧӦ
	 * 
	 * direction��ʾw����������������ߵ���һ���߽�����
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
	 * ��������ж�w��������������������ߵ��ĸ�����
	 * �Լ��Լ�������w��ʲô����
	 * ���Զ�����w��λ���Է�������ЧӦ
	 * 
	 * ��������ˣ��������˴���ЧӦ����ô
	 * �ͷ���true�����򷵻�false
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
		
		//��w�ĽǶ��ڽ����ж�
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
	 * ��������ж������������������group��������Ա��
	 * �Ƿ�������ЧӦ��������������Զ��������������
	 * �����ߵ�λ�ã�������moveLock��Ϊtrue,�ٽ�group
	 * ����Ӧ״̬�ı䣬��󷵻�true
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
				if(group.get(i).findAndAdjustWithWindow(this))  //�ҵ���һ�����Լ���������ЧӦ�Ĵ���
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
	 * �����������direction������Ļ�Ĵ�����������
	 * �Ϸ���direction��ֵΪUP,DOWN,LEFT,RIGHT
	 * 
	 * ע��һ������Ķ��㲻����ͬʱ����������������
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
	 * ��������ж�point��������Ļ����һ����Ե����
	 * ��Ӧ�ط���NONE,UP,DOWN,LEFT,RIGHT,
	 * ���ص����鳤��Ϊ������һλ��ʾ��ˮƽ����������ĸ�����
	 * �ڶ�λ��ʾ����ֱ�����������Щ����
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
	 * ��������ж��Լ���������Ļ����һ������
	 * ���ܵķ���ֵ:
	 * NONE,UP,DOWN,LEFT,RIGHT
	 * ���ص����鳤��Ϊ������һλ��ʾ��ˮƽ����������ĸ�����
	 * �ڶ�λ��ʾ����ֱ�����������Щ����
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
	 * ����������ݴ����direction������Ϊ�Լ���������Ļ
	 * ����Ӧ�����������������£����Լ���λ����������,
	 * ��ʵ�ִ���ЧӦ
	 * 
	 * direction��ʾ��������Ļ����һ����λ��������
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
	 * ������ж���screen�Ĵ��������Ƿ�����������λ�õ���
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
				//��������·��Ĵ���ЧӦ��Ϊ��ʹ�û����Խ������Ϸŵ���Ļ֮���ʡ�ռ�
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
	 * �����������˵��ר��ΪDictFrame��������̬����󻯶���ƣ�
	 * �������ֻ���ʹ���ˮƽ���������Ļ��Ե�Ĵ���ЧӦ
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
	 * �����������˵��ר��ΪDictFrame��������̬����󻯶���ƣ�
	 * �������ֻ���ʹ���ˮƽ���������Ļ��Ե�Ĵ���ЧӦ.
	 * ��������ˣ����Զ������Լ���λ�ã������Լ���moveLock��Ϊ
	 * true��Ȼ�󷵻�true 
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
	 * ��������ж���������������ߣ����Լ�������Ļ�ı�Ե
	 * �Ƿ�������ЧӦ����������ˣ����Զ������Լ���λ�ã�
	 * �����Լ���moveLock��Ϊtrue��Ȼ�󷵻�true 
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
	 * ������������������ק�ƶ����¼�,
	 * �������mouseDragged(MouseEvent e)
	 * �������ã������뵱ǰ���λ��(������λ��
	 * ���������Ļ��)����������������϶��ʹ���
	 * Ч��������Ļ��ͬ��Ĵ��嶼�ᷢ������
	 * 
	 * ��������Զ�����movingDirection��mousePoint,
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
		
		//У������
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		if(debug)
		{
			System.out.println("==refresh movingDirection==");
		}
		
		//����movingDirection
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
			//����mousePoint
			if (debug) {
				System.out.println("==refresh mousePoint==");
			}
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//�����ƶ�
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==process moving==");
			}
			
			setLocation(newMousePoint.x-relativeMousePoint.x, newMousePoint.y-relativeMousePoint.y);
		}
		
				
		//�������ЧӦ
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
	 * ������������������ק�ƶ����¼�,
	 * �������mouseDragged(MouseEvent e)
	 * �������ã������뵱ǰ���λ��(������λ��
	 * ���������Ļ��)����������������϶��ʹ���
	 * Ч����ֻ����Ļ��������
	 * 
	 * ��������Զ�����movingDirection��mousePoint
	 * 
	 */
	public void dragMovingOnlyScreenSensitive(Point newMousePoint)
	{
		if(null==mousePoint||null==relativeMousePoint)
		{
			return;
		}
		
		//У������
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		if(debug)
		{
			System.out.println("==refresh movingDirection==");
		}
		
		//����movingDirection
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
			//����mousePoint
			if (debug) {
				System.out.println("==refresh mousePoint==");
			}
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//�����ƶ�
		if(!moveLock)
		{
			if(debug)
			{
				System.out.println("==process moving==");
			}
			
			setLocation(newMousePoint.x-relativeMousePoint.x, newMousePoint.y-relativeMousePoint.y);
		}
		
				
		//�������ЧӦ
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
	 * �����������˵��ר��ΪDictFrame��������̬����󻯶���ƣ�
	 * ������������������ק��ֻ����ˮƽ�ƶ���������ֱ�ƶ�����
	 * ˮƽ�������ܹ�����Ļ��Ե��������ЧӦ 
	 * 
	 * ��������Զ�����movingDirection��mousePoint
	 */
	public void dragMovingWhenMaxified(Point newMousePoint)
	{
		if(null==mousePoint||null==relativeMousePoint)
		{
			return;
		}
		
		//У������
		newMousePoint=new Point(getLocation().x+newMousePoint.x, getLocation().y+newMousePoint.y);
		
		//����movingDirection
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
			//����mousePoint
			mousePoint = new Point(newMousePoint);
		}
		else
		{
			refreshCounter++;
		}
		
		//�����ƶ�
		if(!moveLock)
		{
			setLocation(newMousePoint.x-relativeMousePoint.x, 0);
		}
		
				
		//�������ЧӦ
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
