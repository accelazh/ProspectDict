package guestUserInterface.firework;

import java.awt.*;
public class MyPoint 
{
	public double x;
	public double y;
	
	
	public MyPoint()
	{
		this(0,0);
	}
	public MyPoint(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
	public MyPoint(Point point)
	{
		this.x=point.x;
		this.y=point.y;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public Point getPoint()
	{
		return new Point((int)x,(int)y);
	}
	
	public String toString()
	{
		return "MyPoint[x="+x+",y="+y+"]";
	}
	public double distance(MyPoint point)
	{
		return Math.pow((point.x-x)*(point.x-x)+
				(point.y-y)*(point.y-y),0.5);
        		
	}
	public double distance(Point point)
	{
		return Math.pow((point.x-x)*(point.x-x)+
				(point.y-y)*(point.y-y),0.5);
        
	}
	
	public void setLocation(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
	
	public static double getDistance(MyPoint a,MyPoint b)
	{
		double distance;
		distance=Math.pow((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y), 0.5);
		return distance;
	}
	public static double getDistance(Point a,Point b)
	{
		double distance;
		distance=Math.pow((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y), 0.5);
		return distance;
	}
	
	public void move(double dertX, double dertY)
	{
	    this.x+=dertX;
	    this.y+=dertY;
	}
}
