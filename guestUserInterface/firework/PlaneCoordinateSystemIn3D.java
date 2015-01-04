package guestUserInterface.firework;

import java.awt.*;
/**
 * 
 * @author ZYL
 * 如果把java的GUI中常用的平面直角坐标系作为一个平面放到三维空间中，
 * 那么这个类就是干这个用的。这个类的实例代表一个三维空间中的平面直角
 * 坐标系，你需要制定这个平面的方程和构造平面直角坐标系所必需的两个单位
 * 向量
 */
public class PlaneCoordinateSystemIn3D 
{	
	//for debug
	private static final boolean debug=false;
	
	private MyPoint3D point;  //原点
	private MyVector3D x; //x轴的单位向量
	private MyVector3D y; //y轴的单位向量
	
	private void construct(MyPoint3D point, MyVector3D xAix, MyVector3D yAix)
	{
		if(debug)
		{
			System.out.println("====in method construct====");
			System.out.println("xAix: "+xAix);
			System.out.println("yAix: "+yAix);
		}
		
		this.point=point;
		this.x=xAix.toUnitVector();
		this.y=yAix.toUnitVector();
		
		if(debug)
		{
			System.out.println("xAix.toUnitVector: "+xAix.toUnitVector());
			System.out.println("yAix.toUnitVector: "+yAix.toUnitVector());
			System.out.println("====end of method construct====");
		}
	}
	
	public PlaneCoordinateSystemIn3D(MyPoint3D point, MyVector3D xAix, MyVector3D yAix)
	{
		construct(point,xAix,yAix);
	}
	
	//构造一个平行于z轴的，由角度arc确定的平面
	public PlaneCoordinateSystemIn3D(double arc,double distance, Dimension size)
	{
		if(debug)
		{
			System.out.println("====Constucting PlaneCoordinateSystemIn3D====");
		    System.out.println("arc: "+arc);
		}
		
		MyVector3D yAix=new MyVector3D(0,0,-1);
		MyVector3D xAix=new MyVector3D(-Math.sin(arc),Math.cos(arc),0);
		
		MyPoint3D origPoint=new MyPoint3D(distance*Math.cos(arc)+Math.sin(arc)*size.width/2.0,distance*Math.sin(arc)-Math.cos(arc)*size.width/2.0,size.height); 		
	
		if(debug)
		{
			System.out.println("xAix: "+xAix);
			System.out.println("yAix: "+yAix);
		}
		
		construct(origPoint,xAix,yAix);
		
		if(debug)
		{
			System.out.println("====end of constucting PlaneCoordinateSystemIn3D====");
			System.out.println("xAix: "+xAix);
			System.out.println("yAix: "+yAix);
		}
	}

	public MyPoint3D getPoint() {
		return point;
	}

	public void setPoint(MyPoint3D point) {
		this.point = point;
	}

	public MyVector3D getX() {
		return x.toUnitVector();
	}

	public void setX(MyVector3D x) {
		this.x = x.toUnitVector();
	}

	public MyVector3D getY() {
		return y.toUnitVector();
	}

	public void setY(MyVector3D y) {
		this.y = y.toUnitVector();
	}
	
	//返回法向量
	public MyVector3D getN()
	{
		return x.outerProduct(y);
		
	}

	public String toString()
	{
		String output="";
		output+="PlaneCoordinateSystemIn3D[point="+point+",x="+x+",y="+y+",getN()="+getN()+"]";
		return output;
	}

	/*public static void main(String[] args)
	{
		PlaneCoordinateSystemIn3D plane=new PlaneCoordinateSystemIn3D(0,600,new Dimension(800,600));
		System.out.println(plane);
	}*/
}
