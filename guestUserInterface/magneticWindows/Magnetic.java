package guestUserInterface.magneticWindows;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * ����ӿ�����ʵ���˴���Ч���Ĵ���,
 * ʵ������ӿڵ�Ӧ����Window������
 * @author ZYL
 *
 */
public interface Magnetic 
{
	/**
	 * ���������ص�ʱ������������
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
	 * refreshCounter��ѭ������
	 */
	public static final int REFRESH_COUNT_SPAN=2;
	
	public final static Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	
	//=======================
	
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
	public Rectangle getSensitiveBound(int direction);
	
	/**
	 * �õ��������Ķ���ķ��������������
	 * 0,1,2,3,4,5,6,7�ֱ��ʾ�����Ͻǣ�inclusive��
	 * ��˳ʱ�뷽����ĸ�����,�Լ����ϱ߿�ʼ��˳ʱ�뷽��
	 * �������ߵ��е�
	 * 
	 */
	public Point getCornerPoint(int value);

	/**
	 * �����������һ���������Ļ�ĵ�����꣬
	 * �жϸõ��������������������ߵ���һ
	 * ���߽����򣬲�����UP,DOWN,LEFT
	 * RIGHT֮һ�����һ����û�н��룬�򷵻�
	 * NONE;
	 * 
	 */
	public int findEnterWhichBound(Point point);
	
	/**
	  �������
	  �жϴ��������������������������ߵ���һ
	  ���߽����򣬲�����UP,DOWN,LEFT
	  RIGHT֮һ�����һ����û�н��룬�򷵻�
	  NONE;
	 */
	public int findEnterWhichBound(Magnetic w);
	
	/**
	 * ����������ݴ����direction������Ϊw�������������
	 * �������ߵ�sensitiveBound������£���w��λ��������
	 * ����ʹ�������Լ����������˴���ЧӦ
	 * 
	 * direction��ʾw����������������ߵ���һ���߽�����
	 * 
	 */
	public void adjustLocationWhenEnterBound(int direction, Magnetic w);

	/**
	 * ��������ж�w��������������������ߵ��ĸ�����
	 * ���Զ�����w��λ���Է�������ЧӦ
	 * 
	 * ��������ˣ��������˴���ЧӦ����ô
	 * �ͷ���true�����򷵻�false
	 * 
	 */
	public boolean findAndAdjustWithWindow(Magnetic w);

	/**
	 * ��������ж������������������group��������Ա��
	 * �Ƿ�������ЧӦ��������������Զ��������������
	 * �����ߵ�λ�ã�������moveLock��Ϊtrue,�ٽ�group
	 * ����Ӧ״̬�ı䣬��󷵻�true
	 */
	public boolean magDetectInGroup();

	/**
	 * �����������direction������Ļ�Ĵ�����������
	 * �Ϸ���direction��ֵΪUP,DOWN,LEFT,RIGHT
	 * 
	 * ע��һ������Ķ��㲻����ͬʱ����������������
	 * 
	 */
	public Rectangle getScreenSensitiveBound(int direction);
	
	/**
	 * ��������ж�point��������Ļ����һ����Ե����
	 * ��Ӧ�ط���NONE,UP,DOWN,LEFT,RIGHT
	 */
	public int[] findEnterWhichScreenBound(Point point);
	
	/**
	 * ��������ж��Լ���������Ļ����һ������
	 * ���ܵķ���ֵ:
	 * NONE,UO,DOWN,LEFT,RIGHT
	 * 
	 */
	public int[] findEnterWhichScreenBound();
	
	/**
	 * ����������ݴ����direction������Ϊ�Լ���������Ļ
	 * ����Ӧ�����������������£����Լ���λ����������,
	 * ��ʵ�ִ���ЧӦ
	 * 
	 * direction��ʾ��������Ļ����һ����λ��������
	 * 
	 */
	public void adjustLocationWhenEnterScreenBound(int[] direction);

	/**
	 * ������ж���screen�Ĵ��������Ƿ�����������λ�õ���
	 * 
	 */
	public boolean findAndAdjustWithScreen();

	/**
	 * ��������ж���������������ߣ����Լ�������Ļ�ı�Ե
	 * �Ƿ�������ЧӦ����������ˣ����Զ������Լ���λ�ã�
	 * �����Լ���moveLock��Ϊtrue��Ȼ�󷵻�true 
	 * 
	 */
	public boolean magDetectWithScreen();
	
	/**
	 * ������������������ק�ƶ����¼�,
	 * �������mouseDragged(MouseEvent e)
	 * �������ã������뵱ǰ���λ��(������λ��
	 * ���������Ļ��)
	 * 
	 * ��������Զ�����movingDirection��mousePoint,
	 * 
	 */
	public void dragMoving(Point newMousePoint);

	/**
	 * �õ����嵱ǰ���ƶ�������һ������Ϊ����
	 * �����ʾ����һλ��ʾˮƽ���򣬵ڶ�λ��ʾ
	 * ��ֱ����
	 * 
	 */
	public int[] getMovingDirection(); 

	
	//=======================
	
	/**
	 * ��ͬ�����������õķ���
	 * 
	 */
	public MagGroup getGroup();
	
	/**
	 * ��ͬ�����������õķ���
	 * 
	 */
	public void setGroup(MagGroup group);
	
	//���漸��������ʵ�Ǵ��幫�е�����
	public Dimension getSize();
	
	public void setSize(Dimension size);
	
	public void setSize(int width, int height);
	
	public Point getLocation();
	
	public void setLocation(Point location);
	
	public void setLocation(int x, int y);
	
	//�����Ǵ���Ч���Ŀ���
	public boolean isMagEnable();

	public void setMagEnable(boolean magEnable);
}
