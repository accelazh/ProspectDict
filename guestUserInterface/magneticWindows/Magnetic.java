package guestUserInterface.magneticWindows;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * 这个接口描述实现了磁性效果的窗口,
 * 实现这个接口的应该是Window的子类
 * @author ZYL
 *
 */
public interface Magnetic 
{
	/**
	 * 相距多少像素的时候发生磁性作用
	 */
	public final static int MAG_BOUND=15;
	
	public final static int NONE=-1;
	public final static int UP=0;
	public final static int DOWN=1;
	public final static int LEFT=2;
	public final static int RIGHT=3;
	
	public final static int[] DIRECTIONS={
		UP,
		DOWN,
		LEFT,
		RIGHT,
	};
	/**
	 * refreshCounter的循环周期
	 */
	public static final int REFRESH_COUNT_SPAN=2;
	
	public final static Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	
	//=======================
	
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
	public Rectangle getSensitiveBound(int direction);
	
	/**
	 * 得到这个窗体的顶点的方法，传入参数是
	 * 0,1,2,3,4,5,6,7分别表示从左上角（inclusive）
	 * 沿顺时针方向的四个顶点,以及从上边开始严顺时针方向
	 * 的四条边的中点
	 * 
	 */
	public Point getCornerPoint(int value);

	/**
	 * 这个方法传入一个相对于屏幕的点的坐标，
	 * 判断该点进入了这个方法的所有者的哪一
	 * 个边界区域，并返回UP,DOWN,LEFT
	 * RIGHT之一。如果一个都没有进入，则返回
	 * NONE;
	 * 
	 */
	public int findEnterWhichBound(Point point);
	
	/**
	  这个方法
	  判断传入参数进入了这个方法的所有者的哪一
	  个边界区域，并返回UP,DOWN,LEFT
	  RIGHT之一。如果一个都没有进入，则返回
	  NONE;
	 */
	public int findEnterWhichBound(Magnetic w);
	
	/**
	 * 这个方法根据传入的direction，在认为w进入了这个方法
	 * 的所有者的sensitiveBound的情况下，对w的位置做出调
	 * 整，使其贴近自己，即发生了磁铁效应
	 * 
	 * direction表示w在这个方法的所有者的哪一个边界区域
	 * 
	 */
	public void adjustLocationWhenEnterBound(int direction, Magnetic w);

	/**
	 * 这个方法判断w进入了这个方法的所有者的哪个区域，
	 * 并自动调整w的位置以发生磁铁效应
	 * 
	 * 如果调整了，即触发了磁铁效应，那么
	 * 就返回true，否则返回false
	 * 
	 */
	public boolean findAndAdjustWithWindow(Magnetic w);

	/**
	 * 这个方法判断这个方法的所有者与group中其他成员间
	 * 是否发生磁性效应，如果发生，则自动调整这个方法的
	 * 所有者的位置，并将其moveLock设为true,再将group
	 * 中相应状态改变，最后返回true
	 */
	public boolean magDetectInGroup();

	/**
	 * 这个方法根据direction返回屏幕的磁性敏感区，
	 * 合法的direction的值为UP,DOWN,LEFT,RIGHT
	 * 
	 * 注意一个窗体的顶点不可能同时在两个敏感区域中
	 * 
	 */
	public Rectangle getScreenSensitiveBound(int direction);
	
	/**
	 * 这个方法判断point进入了屏幕的那一个边缘区域，
	 * 相应地返回NONE,UP,DOWN,LEFT,RIGHT
	 */
	public int[] findEnterWhichScreenBound(Point point);
	
	/**
	 * 这个方法判断自己进入了屏幕的哪一个区域
	 * 可能的返回值:
	 * NONE,UO,DOWN,LEFT,RIGHT
	 * 
	 */
	public int[] findEnterWhichScreenBound();
	
	/**
	 * 这个方法根据传入的direction，在认为自己进入了屏幕
	 * 的相应磁性敏感区域的情况下，对自己的位置做出调整,
	 * 以实现磁铁效应
	 * 
	 * direction表示进入了屏幕的哪一个方位的敏感区
	 * 
	 */
	public void adjustLocationWhenEnterScreenBound(int[] direction);

	/**
	 * 这个方判断与screen的磁性作用是否发生，并作出位置调整
	 * 
	 */
	public boolean findAndAdjustWithScreen();

	/**
	 * 这个方法判断这个方法的所有者（即自己）与屏幕的边缘
	 * 是否发生磁性效应，如果发生了，则自动调整自己的位置，
	 * 并将自己的moveLock设为true，然后返回true 
	 * 
	 */
	public boolean magDetectWithScreen();
	
	/**
	 * 这个方法处理用鼠标托拽移动的事件,
	 * 因该是由mouseDragged(MouseEvent e)
	 * 方法调用，并传入当前鼠标位置(这个鼠标位置
	 * 是相对于屏幕的)
	 * 
	 * 这个方法自动更新movingDirection和mousePoint,
	 * 
	 */
	public void dragMoving(Point newMousePoint);

	/**
	 * 得到窗体当前的移动方向，用一个长度为二的
	 * 数组表示，第一位表示水平方向，第二位表示
	 * 竖直方向
	 * 
	 */
	public int[] getMovingDirection(); 

	
	//=======================
	
	/**
	 * 共同发生磁性作用的分组
	 * 
	 */
	public MagGroup getGroup();
	
	/**
	 * 共同发生磁性作用的分组
	 * 
	 */
	public void setGroup(MagGroup group);
	
	//下面几个方法其实是窗体公有的特性
	public Dimension getSize();
	
	public void setSize(Dimension size);
	
	public void setSize(int width, int height);
	
	public Point getLocation();
	
	public void setLocation(Point location);
	
	public void setLocation(int x, int y);
	
	//下面是磁铁效果的开关
	public boolean isMagEnable();

	public void setMagEnable(boolean magEnable);
}
