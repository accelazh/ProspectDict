package guestUserInterface.firework;

/**
 * 
 * @author ZYL
 * 这个类模拟空间直角坐标系中的平面
 */
public class PlaneIn3D 
{
	private MyVector3D n; //法向量
	private MyPoint3D point; //点法式的点
	
    //点法式构造平面
	public PlaneIn3D(MyPoint3D point, MyVector3D n)
	{
		this.n=n;
		this.point=point;
	}
	
	//用一个点两个向量构造平面
	public PlaneIn3D(MyPoint3D p, MyVector3D v1, MyVector3D v2)
	{
		this(p,v1.outerProduct(v2));
	}
	
	public MyVector3D getN() {
		return n;
	}

	public void setN(MyVector3D n) {
		this.n = n;
	}

	public MyPoint3D getPoint() {
		return point;
	}

	public void setPoint(MyPoint3D point) {
		this.point = point;
	}

	public boolean isPointOnMe(MyPoint3D p)
	{
		MyVector3D v=new MyVector3D(point,p);
		return n.isPerpendicular(v);
	}
	
	public boolean isVectorOnMe(MyVector3D v)
	{
		return n.isPerpendicular(v);
	}

}
