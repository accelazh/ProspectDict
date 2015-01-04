package guestUserInterface.firework;

/**
 * 
 * @author ZYL
 * �����ģ��ռ�ֱ������ϵ�е�ƽ��
 */
public class PlaneIn3D 
{
	private MyVector3D n; //������
	private MyPoint3D point; //�㷨ʽ�ĵ�
	
    //�㷨ʽ����ƽ��
	public PlaneIn3D(MyPoint3D point, MyVector3D n)
	{
		this.n=n;
		this.point=point;
	}
	
	//��һ����������������ƽ��
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
