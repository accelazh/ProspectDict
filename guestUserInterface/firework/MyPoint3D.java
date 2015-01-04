package guestUserInterface.firework;

import java.awt.*;

public class MyPoint3D 
{
	private double x;
	private double y;
	private double z;
	
	public MyPoint3D()
	{
		
	}
	
	public MyPoint3D(double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * ���������귽�̽�����
	 * @param thita
	 * @param fai
	 * @param û�����ã�ֻ��Ϊ�����ֲ�ͬ�Ĺ��췽��
	 */
	public MyPoint3D(double r,double thita,double fai, boolean b)
	{
		this.x=r*Math.sin(fai)*Math.cos(thita);
		this.y=r*Math.sin(fai)*Math.sin(thita);
		this.z=r*Math.cos(fai);
	}
	public MyPoint3D(MyPoint3D myPoint3D)
	{
		this.x=myPoint3D.x;
		this.y=myPoint3D.y;
		this.z=myPoint3D.z;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	//�����������ά����ϵ�еĵ�ͶӰ�������Ķ�ά����ϵ��,���ض�ά����
	
	public Point to2DPoint(PlaneCoordinateSystemIn3D plane)
	{	
		//�������
		double A=plane.getN().getX();
		double B=plane.getN().getY();
		double C=plane.getN().getZ();
	    double D=-(A*plane.getPoint().getX()+B*plane.getPoint().getY()+C*plane.getPoint().getZ());
		   
		double t=-(A*getX()+B*getY()+C*getZ()+D)/(A*A+B*B+C*C);
			
		MyPoint3D meetPoint=new MyPoint3D(getX()+A*t,getY()+B*t,getZ()+C*t);
	
		//������������planeԭ�������
		MyVector3D pointVector=new MyVector3D(plane.getPoint(),meetPoint);
		
    	//��������plane����ϵ��x��y����
		MyVector3D i=plane.getX();
		MyVector3D j=plane.getY();
		
		
		MyPoint3D x1=new MyPoint3D(i.getX(),j.getX(),pointVector.getX()); //����x1,x2,x3���ǵ㣬ֻ��������¼�������̵Ĳ���
		MyPoint3D x2=new MyPoint3D(i.getY(),j.getY(),pointVector.getY());
		MyPoint3D x3=new MyPoint3D(i.getZ(),j.getZ(),pointVector.getZ());
		
		MyPoint mp1=solveEquations(x1.getX(),x1.getY(),x1.getZ(),x2.getX(),x2.getY(),x2.getZ());
		MyPoint mp2=solveEquations(x1.getX(),x1.getY(),x1.getZ(),x3.getX(),x3.getY(),x3.getZ());
		MyPoint mp3=solveEquations(x3.getX(),x3.getY(),x3.getZ(),x2.getX(),x2.getY(),x2.getZ());
		
    	if(mp1!=null)
		{
			return mp1.getPoint();
		}
		else
		{
			if(mp2!=null)
			{
				return mp2.getPoint();
			}
			else
			{
				return mp3.getPoint();
			}
		}
	}
	
	public MyPoint to2DMyPoint(PlaneCoordinateSystemIn3D plane)
	{
		//�������
		double A=plane.getN().getX();
		double B=plane.getN().getY();
		double C=plane.getN().getZ();
	    double D=-(A*plane.getPoint().getX()+B*plane.getPoint().getY()+C*plane.getPoint().getZ());
		
	   	double t=-(A*getX()+B*getY()+C*getZ()+D)/(A*A+B*B+C*C);
		
		MyPoint3D meetPoint=new MyPoint3D(getX()+A*t,getY()+B*t,getZ()+C*t);
		
			
		//������������planeԭ�������
		MyVector3D pointVector=new MyVector3D(plane.getPoint(),meetPoint);
		
		//��������plane����ϵ��x��y����
		MyVector3D i=plane.getX();
		MyVector3D j=plane.getY();
		
		
		MyPoint3D x1=new MyPoint3D(i.getX(),j.getX(),pointVector.getX()); //����x1,x2,x3���ǵ㣬ֻ��������¼�������̵Ĳ���
		MyPoint3D x2=new MyPoint3D(i.getY(),j.getY(),pointVector.getY());
		MyPoint3D x3=new MyPoint3D(i.getZ(),j.getZ(),pointVector.getZ());
		
		MyPoint mp1=solveEquations(x1.getX(),x1.getY(),x1.getZ(),x2.getX(),x2.getY(),x2.getZ());
		MyPoint mp2=solveEquations(x1.getX(),x1.getY(),x1.getZ(),x3.getX(),x3.getY(),x3.getZ());
		MyPoint mp3=solveEquations(x3.getX(),x3.getY(),x3.getZ(),x2.getX(),x2.getY(),x2.getZ());
		
		if(mp1!=null)
		{
			return mp1;
		}
		else
		{
			if(mp2!=null)
			{
				return mp2;
			}
			else
			{
				return mp3;
			}
		}
	}
	
	private MyPoint solveEquations(double A1, double B1, double C1, double A2,double B2,double C2)
	{
		if(Math.abs(A1*B2-A2*B1)<1e-6)
		{
			return null;
		}
		else
		{
			double divider=A1*B2-A2*B1;
			
			return new MyPoint((C1*B2-C2*B1)/divider,-(C1*A2-C2*A1)/divider);
		}
	}
	
	public void move(MyVector3D v)
	{
		if(null==v)
		{
			return;
		}
		
		this.x+=v.getX();
		this.y+=v.getY();
		this.z+=v.getZ();
	}
	
	public String toString()
	{
		return "MyPoint3D["+"x="+getX()+",y="+getY()+",z="+getZ()+"]";
	}
}
