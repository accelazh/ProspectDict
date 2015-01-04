package guestUserInterface.firework;

public class MyVector3D extends MyPoint3D 
{
	public MyVector3D()
	{
		super();
	}

	public MyVector3D(double x,double y,double z)
	{
		super(x,y,z);
	}
	
	public MyVector3D(MyVector3D v)
	{
		this.setX(v.getX());
		this.setY(v.getY());
		this.setZ(v.getZ());
	}
	
	//建立由a指向b的向量
	public MyVector3D(MyPoint3D a, MyPoint3D b)
	{
		this(b.getX()-a.getX(),b.getY()-a.getY(),b.getZ()-a.getZ());
	}
	
	public double getLength()
	{
		return Math.pow(getX()*getX()+getY()*getY()+getZ()*getZ(), 0.5);
	}

	public double innerProduct(MyVector3D v)
	{
		return getX()*v.getX()+getY()*v.getY()+getZ()*v.getZ();
	}
	
	public MyVector3D outerProduct(MyVector3D v)
	{
		double x1=getX();
		double y1=getY();
		double z1=getZ();
		
		double x2=v.getX();
		double y2=v.getY();
		double z2=v.getZ();
		
		return new MyVector3D(y1*z2-y2*z1,x2*z1-x1*z2,x1*y2-x2*y1);
	}
	
	//是否垂直
	public boolean isPerpendicular(MyVector3D v)
	{
		return Math.abs(innerProduct(v))<1e-6;
	}
	
	//是否平行
	public boolean isParallel(MyVector3D v)
	{
		double check1=getX()*v.getY()-getY()*v.getX();
		double check2=getX()*v.getZ()-getZ()*v.getX();
		double check3=getZ()*v.getY()-getY()*v.getZ();
	
		if((Math.abs(check1)<1e-6)
				&&(Math.abs(check2)<1e-6)
				&&(Math.abs(check3)<1e-6))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public MyVector3D toUnitVector()
	{	
		return new MyVector3D(getX()/getLength(),getY()/getLength(),getZ()/getLength());
	}
	
	public MyVector3D mutiply(double x)
	{
		return new MyVector3D(getX()*x,getY()*x,getZ()*x);
		
	}
	
	/**
	 * 
	 * 这个方法返回相对于给定平面的x轴的到角
	 */
	public double getPlaneArc(PlaneCoordinateSystemIn3D plane)
	{
		//计算沿x、y轴的分量
		double amountOfX=innerProduct(plane.getX());
		double amountOfY=innerProduct(plane.getY());
		
		double amount=Math.pow(amountOfX*amountOfX+amountOfY*amountOfY,0.5);
		double sin=amountOfY/amount;
		
		double arc=0;
		if(amountOfX>=0)
		{
			arc=Math.asin(sin);
		}
		else
		{
			arc=Math.PI-Math.asin(sin);
		}
		
		return arc;
	}
	
	/**
	 * 这个方法返回向量在指定平面的投影长度与自己的长度的比值
	 */
	public double getPhotoLengthRate(PlaneCoordinateSystemIn3D plane)
	{
		double amountOfX=innerProduct(plane.getX());
		double amountOfY=innerProduct(plane.getY());
		
		double amount=Math.pow(amountOfX*amountOfX+amountOfY*amountOfY,0.5);
	
	    return amount/getLength();
	}
	
	public MyVector3D addition(MyVector3D v)
	{
		return new MyVector3D(getX()+v.getX(),getY()+v.getY(),getZ()+v.getZ());
	}
	
	public MyVector3D subtract(MyVector3D v)
	{
		return new MyVector3D(getX()-v.getX(),getY()-v.getY(),getZ()-v.getZ());
	}
	
	public String toString()
	{
		return "MyVector3D["+"x="+getX()+",y="+getY()+",z="+getZ()+"]";
	}
	
	/*public static void main(String[] args)
	{
		MyVector3D v=new MyVector3D(10,10,10);
		System.out.println("v.getLength(): "+v.getLength());
		System.out.println("v.toUnitVector(): "+v.toUnitVector());
	}*/
}
