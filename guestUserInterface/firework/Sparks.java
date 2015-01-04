package guestUserInterface.firework;

import java.awt.*;
import java.util.*;


/**
 * �������������һȦ��
 * �����̻�Ч��
 * 
 */
public class Sparks 
{
	private double gravity=2300;
	private int timerInterval=10;  //��ʱ����ʱ����
	
	private long counter=0;   //���ڼ���������Ѿ����ڶ೤ʱ����
	private boolean useable=true;   //�û����趨���������Ϊfalse��ʱ�򣬻𻨲�����ʾ
	
	private int sparkLineLength=25;
	private Color mainColor=Color.YELLOW;
	private Color subColor=Color.WHITE;
	private double sparkLineVelocity=1000;
	
	private boolean isRegular=true;   //���ɵĻ������λ�û��ǹ���λ��
	private int sparkLineNumPerLayer=20;
	private int layerNum=3;   //�𻨵Ĳ���
	
	private ArrayList<SubSpark> sparkLines=new ArrayList<SubSpark>();
	
	//arrow Ϊ�𻨷ɳ��ķ���
	public Sparks(MyPoint3D startPoint, double sparkLineVelocity, 
			int sparkLineNumPerLayer, int layerNum, MyVector3D arrow, boolean isRegular)
	{
		this.isRegular=isRegular;
		this.sparkLineVelocity=sparkLineVelocity;
		this.sparkLineNumPerLayer=sparkLineNumPerLayer;
		this.layerNum=layerNum;
		
		construct(new MyPoint3D(startPoint),new MyVector3D(arrow));
	}
	
	public Sparks(MyPoint3D startPoint, MyVector3D arrow, boolean isRegular)
	{
		this.isRegular=isRegular;
		
		construct(new MyPoint3D(startPoint),new MyVector3D(arrow));
	}
	
	//������ɹ��췽��
	private void construct(MyPoint3D startPoint, MyVector3D arrow)
	{
		
		//�����������굥λ����
		MyVector3D roughI=new MyVector3D(arrow.getZ(),0,-arrow.getX());
		MyVector3D roughJ=roughI.outerProduct(arrow);
	
		MyVector3D I=roughI.toUnitVector();
		MyVector3D J=roughJ.toUnitVector();
		MyVector3D K=arrow.toUnitVector();
		
		double topArcUnit=Math.PI/2/(layerNum+1);
		double turnArcUnit=Math.PI*2/sparkLineNumPerLayer;
		for(int i=1;i<=layerNum;i++)
		{
			double topArc=topArcUnit*i;
			
			for(int j=0;j<sparkLineNumPerLayer;j++)
			{
				
				double vRate=isRegular?1:(0.5+1*Math.random());
				
				double turnArc=turnArcUnit*j;
				MyVector3D vk=K.mutiply(sparkLineVelocity*Math.cos(topArc));
			    MyVector3D vi=I.mutiply(sparkLineVelocity*Math.sin(topArc)*Math.cos(turnArc)); 
			    MyVector3D vj=J.mutiply(sparkLineVelocity*Math.sin(topArc)*Math.sin(turnArc)); 
            
			    SubSpark newSubSpark=null;
			    sparkLines.add(newSubSpark=new SubSpark((vk.addition(vi.addition(vj))).mutiply(vRate), startPoint,gravity,timerInterval));
			    newSubSpark.setSparkLineLength(this.sparkLineLength);
			    newSubSpark.setMainColor(this.mainColor);
			    newSubSpark.setSubColor(this.subColor);
    
			}
		}
		
	}
	
	public void selfProcess()
	{
		if(counter<Long.MAX_VALUE);
		{
			counter++;
		}
		
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.selfProcess();
			}
		}
	}
	
	public void paint(Graphics g, PlaneCoordinateSystemIn3D plane)
	{
		if(!useable)
		{
			return;
		}
		
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.paint(g,plane);
			}
		}
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) 
	{
		this.gravity = gravity;
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.setGravity(gravity);
			}
		}
	}

	public int getTimerInterval() {
		return timerInterval;
	}

	public void setTimerInterval(int timerInterval) {
		this.timerInterval = timerInterval;
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.setTimerInterval(timerInterval);
			}
		}
	}

	public boolean isUseable() {
		return useable;
	}

	public void setUseable(boolean useable) {
		this.useable = useable;
	}

	public int getSparkLineLength() {
		return sparkLineLength;
	}

	public void setSparkLineLength(int sparkLineLength) {
		this.sparkLineLength = sparkLineLength;
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.setSparkLineLength(sparkLineLength);
			}
		}
	}

	public Color getMainColor() {
		return mainColor;
	}

	public void setMainColor(Color mainColor) {
		this.mainColor = mainColor;
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.setMainColor(mainColor);
			}
		}
	}

	public Color getSubColor() {
		return subColor;
	}

	public void setSubColor(Color subColor) {
		this.subColor = subColor;
		for(int i=0;i<sparkLines.size();i++)
		{
			SubSpark subSpark=sparkLines.get(i);
			if(subSpark!=null)
			{
				subSpark.setSubColor(subColor);
			}
		}
	}

	public long getCounter() {
		return counter;
	}

	public double getSparkLineVelocity() {
		return sparkLineVelocity;
	}

	public boolean isRegular() {
		return isRegular;
	}

	public int getSparkLineNumPerLayer() {
		return sparkLineNumPerLayer;
	}

	public int getLayerNum() {
		return layerNum;
	}

	public SubSpark getSparkLineAt(int index)
	{
		if(index<0||index>=sparkLines.size())
		{
			return null;
		}
		
		return sparkLines.get(index);
	}
	
	
	
}
