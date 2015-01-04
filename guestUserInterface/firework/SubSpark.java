package guestUserInterface.firework;

import java.awt.*;

/**
 * 
 * 用于创建单个spark
 */
public class SubSpark 
{
	//for debug
	private static final boolean debug=false;
	
	private MyVector3D velocity;
	private MyPoint3D headPoint;
    
	private double gravity=2300;
	private int timerInterval=10;  //计时器的时间间隔
	
	private long counter=0;   //用于计算这个火花已经存在多长时间了
	private boolean useable=true;   //用户来设定这个量，当为false的时候，火花不再显示
	
	private int sparkLineLength=25;
	private Color mainColor=Color.YELLOW;
	private Color subColor=Color.WHITE;
	
	public SubSpark(MyVector3D v, MyPoint3D startPoint, double gravity, int timerInterval)
	{
		this.velocity=new MyVector3D(v.getX(),v.getY(),v.getZ());
		this.headPoint=new MyPoint3D(startPoint.getX(),startPoint.getY(),startPoint.getZ());
		this.gravity=gravity;
		this.timerInterval=timerInterval;
				
	}
	public SubSpark(MyVector3D v, MyPoint3D startPoint)
	{
		this.velocity=new MyVector3D(v.getX(),v.getY(),v.getZ());
		this.headPoint=new MyPoint3D(startPoint.getX(),startPoint.getY(),startPoint.getZ());
				
	}
	
	public void selfProcess()
	{
		if(debug)
		{
			System.out.println("====in selfProcess of SubSpark====");
		}
		
		if(counter<Long.MAX_VALUE);
		{
			counter++;
		}
		headPoint.move(velocity.mutiply(timerInterval*1.0/1000));
		velocity.setZ(velocity.getZ()-gravity*timerInterval/1000);
	
		if(debug)
		{
			System.out.println("headPonit: "+headPoint);
			System.out.println("velocity: "+velocity);
		}
		
		if(debug)
		{
			System.out.println("====end of selfProcess of SubSpark====");
		}
		
	}
	
	//这以下,arc统一是在java坐标系中相对于x轴的到角
	private void paintLine(Graphics g, MyPoint start, double length, double arc, boolean stroked)
	{
		MyPoint startPoint=new MyPoint(start.x,start.y);
		MyPoint endPoint=new MyPoint(startPoint.x+length*Math.cos(arc),
				startPoint.y+length*Math.sin(arc));
		
		if(!stroked)
		{
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			return;
		}
		
		if(Math.abs(Math.sin(arc))<Math.abs(Math.cos(arc)))
		{
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
			startPoint.move(0,1);
			endPoint.move(0,1);
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
			startPoint.move(0,-2);
			endPoint.move(0,-2);
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
		}
		else
		{
            g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
            startPoint.move(1,0);
			endPoint.move(1,0);
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
			startPoint.move(-2,0);
			endPoint.move(-2,0);
			g.drawLine((int)startPoint.x,(int)startPoint.y, (int)endPoint.x,(int)endPoint.y);
			
		}
	}
	
	public void paint(Graphics g, PlaneCoordinateSystemIn3D plane)
	{			
		if(debug)
		{
			System.out.println("\n\n====in paint of SubSpark====");
		}
		
		if(!useable)
		{
			return;
		}
		
		double arc=velocity.getPlaneArc(plane)+Math.PI;
		int length=(int)(sparkLineLength*velocity.getPhotoLengthRate(plane));
		
		MyPoint start=headPoint.to2DMyPoint(plane);
		
		if(debug)
		{
			System.out.println("start: "+start);
			System.out.println("length: "+length);
			System.out.println("arc: "+arc);
		}
		
	    g.setColor(mainColor);
		paintLine(g, start, length, arc, true);
		g.setColor(subColor);
		paintLine(g, start, 2.0/3*length, arc,true);
		
		if(Math.abs(Math.sin(arc))<Math.abs(Math.cos(arc)))
		{
			start.move(0,2);
			g.setColor(mainColor);
			paintLine(g,start,2.0/3*length,arc,false);
			g.setColor(subColor);
			paintLine(g,start,1.0/3*2.0/3*length,arc,false);
			
			start.move(0,-4);
			g.setColor(mainColor);
			paintLine(g,start,2.0/3*length,arc,false);
			g.setColor(subColor);
			paintLine(g,start,1.0/3*2.0/3*length,arc,false);
		}
		else
		{
			start.move(2,0);
			g.setColor(mainColor);
			paintLine(g,start,2.0/3*length,arc,false);
			g.setColor(subColor);
			paintLine(g,start,1.0/3*2.0/3*length,arc,false);
			
			start.move(-4,0);
			g.setColor(mainColor);
			paintLine(g,start,2.0/3*length,arc,false);
			g.setColor(subColor);
			paintLine(g,start,1.0/3*2.0/3*length,arc,false);
		}
		
		if(debug)
		{
			System.out.println("====end of paint of SubSpark====\n\n");
		}
		
	}
	public double getGravity() {
		return gravity;
	}
	public void setGravity(double gravity) {
		this.gravity = gravity;
	}
	public int getTimerInterval() {
		return timerInterval;
	}
	public void setTimerInterval(int timerInterval) {
		this.timerInterval = timerInterval;
	}
	public boolean isUseable() {
		return useable;
	}
	public void setUseable(boolean useable) {
		this.useable = useable;
	}
	public MyVector3D getVector() {
		return velocity;
	}
	public MyPoint3D getHeadPoint() {
		return headPoint;
	}
	public long getCounter() {
		return counter;
	}
	public int getSparkLineLength() {
		return sparkLineLength;
	}
	public void setSparkLineLength(int sparkLineLength) {
		this.sparkLineLength = sparkLineLength;
	}
	public Color getMainColor() {
		return mainColor;
	}
	public void setMainColor(Color mainColor) {
		this.mainColor = mainColor;
	}
	public Color getSubColor() {
		return subColor;
	}
	public void setSubColor(Color subColor) {
		this.subColor = subColor;
	}

}
