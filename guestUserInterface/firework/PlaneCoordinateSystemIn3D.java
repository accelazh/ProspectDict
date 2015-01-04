package guestUserInterface.firework;

import java.awt.*;
/**
 * 
 * @author ZYL
 * �����java��GUI�г��õ�ƽ��ֱ������ϵ��Ϊһ��ƽ��ŵ���ά�ռ��У�
 * ��ô�������Ǹ�����õġ�������ʵ������һ����ά�ռ��е�ƽ��ֱ��
 * ����ϵ������Ҫ�ƶ����ƽ��ķ��̺͹���ƽ��ֱ������ϵ�������������λ
 * ����
 */
public class PlaneCoordinateSystemIn3D 
{	
	//for debug
	private static final boolean debug=false;
	
	private MyPoint3D point;  //ԭ��
	private MyVector3D x; //x��ĵ�λ����
	private MyVector3D y; //y��ĵ�λ����
	
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
	
	//����һ��ƽ����z��ģ��ɽǶ�arcȷ����ƽ��
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
	
	//���ط�����
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
