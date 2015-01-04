package guestUserInterface.firework;

import java.awt.*;
import java.net.*;


/**
 * 
 * 这个类生成模拟一枚烟花弹
 */
public class Firework 
{
	private boolean boomed=false; //标明当前状态是正在飞行，还是已经炸开

	private double gravity=2300;
	private int timerInterval=10;  //计时器的时间间隔
	
	private long counter=0;   //用于计算这个火花已经存在多长时间了
	private boolean useable=true;   //用户来设定这个量，当为false的时候，火花不再显示
	
	private long flyingTimeCount=40;  //描述飞行多少个timerInterval后爆炸
	private long BoomingDisplayTimeCount=40;  //描述爆炸后多少个timerInterval后消失
	
	private SubSpark boom;
	private Sparks sparkBoomed;
	private double startVelocity=1450;	
	
	private double boomVelocity=1300;
	private int sparkLineNumPerLayer=20;
	private int sparkLineLayerNum=3;
	private boolean isRegular=true; 

	private SoundPlayer boomClip; 
	
	public Firework(MyPoint3D startPoint, MyVector3D arrow, boolean isRegular)
	{
		this.isRegular=isRegular;
		boom=new SubSpark(arrow.toUnitVector().mutiply(startVelocity), startPoint,
				this.gravity, this.timerInterval);
	}
	
	public Firework(MyPoint3D startPoint, MyVector3D arrow, boolean isRegular, boolean randomStartVelocity)
	{
		this.isRegular=isRegular;
		if(randomStartVelocity)
		{
			this.startVelocity=0.8*startVelocity+0.3*Math.random()*startVelocity;
		}
		
		boom=new SubSpark(arrow.toUnitVector().mutiply(startVelocity), startPoint,
				this.gravity, this.timerInterval);
	}
	
	public Firework(MyPoint3D startPoint, MyVector3D arrow, double startVelocity, boolean isRegular, boolean randomStartVelocity)
	{
		this.isRegular=isRegular;
		if(!randomStartVelocity)
		{
			this.startVelocity=startVelocity;
		}
		else
		{
			this.startVelocity=0.8*startVelocity+0.3*Math.random()*startVelocity;
		}
		boom=new SubSpark(arrow.toUnitVector().mutiply(startVelocity), startPoint,
				this.gravity, this.timerInterval);
	}
	
	public void selfProcess()
	{
		if(counter<Long.MAX_VALUE);
		{
			counter++;
		}
		if(!boomed)
		{
			boom.selfProcess();
		}
		else
		{
			sparkBoomed.selfProcess();
		}
		
		if(counter==flyingTimeCount)
		{
			boomed=true;
			
			if(boomClip!=null)
			{
				boomClip.play();
			}

			//计算火花的中心角度
			MyVector3D arrow=boom.getVector();
			
			//计算三个坐标单位向量
			MyVector3D roughI=new MyVector3D(arrow.getZ(),0,-arrow.getX());
			MyVector3D roughJ=roughI.outerProduct(arrow);
		
			MyVector3D I=roughI.toUnitVector().mutiply(2*Math.random()-1);
			MyVector3D J=roughJ.toUnitVector().mutiply(2*Math.random()-1);
			MyVector3D K=arrow.toUnitVector();
			
			sparkBoomed=new Sparks(boom.getHeadPoint(),boomVelocity, 
					sparkLineNumPerLayer, sparkLineLayerNum,K.addition(I.addition(J)),isRegular);

			boom=null;
		}
		
		if(counter==flyingTimeCount+BoomingDisplayTimeCount)
		{
			sparkBoomed=null;
			this.useable=false;
		}
	}
	
	public void paint(Graphics g, PlaneCoordinateSystemIn3D plane)
	{
		if(!useable)
		{
			return;
		}
		
		if(boom!=null)
		{
			boom.paint(g, plane);
		}
		
		if(sparkBoomed!=null)
		{
			sparkBoomed.paint(g, plane);
		}
	}
	
	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
		if(boom!=null)
		{
			boom.setGravity(gravity);
		}
		if(sparkBoomed!=null)
		{
			sparkBoomed.setGravity(gravity);
		}
	}

	public int getTimerInterval() {
		return timerInterval;
	}

	public void setTimerInterval(int timerInterval) {
		this.timerInterval = timerInterval;
	    if(boom!=null)
	    {
	    	boom.setTimerInterval(timerInterval);
	    }
	    if(sparkBoomed!=null)
	    {
	    	sparkBoomed.setTimerInterval(timerInterval);
	    }
	}

	public long getFlyingTimeCount() {
		return flyingTimeCount;
	}

	public void setFlyingTimeCount(long flyingTimeCount) {
		this.flyingTimeCount = flyingTimeCount;
	}

	public long getBoomingDisplayTimeCount() {
		return BoomingDisplayTimeCount;
	}

	public void setBoomingDisplayTimeCount(long boomingDisplayTimeCount) {
		BoomingDisplayTimeCount = boomingDisplayTimeCount;
	}

	public int getSparkLineNumPerLayer() {
		return sparkLineNumPerLayer;
	}

	public void setSparkLineNumPerLayer(int sparkLineNumPerLayer) {
		this.sparkLineNumPerLayer = sparkLineNumPerLayer;
	}

	public int getSparkLineLayerNum() {
		return sparkLineLayerNum;
	}

	public void setSparkLineLayerNum(int sparkLineLayerNum) {
		this.sparkLineLayerNum = sparkLineLayerNum;
	}

	public boolean isRegular() {
		return isRegular;
	}

	public void setRegular(boolean isRegular) {
		this.isRegular = isRegular;
	}

	public boolean isBoomed() {
		return boomed;
	}

	public long getCounter() {
		return counter;
	}

	public boolean isUseable() {
		return useable;
	}

	public double getStartVelocity() {
		return startVelocity;
	}
	
	public void setBoomClip(URL url)
	{
		if(url!=null)
		{
			boomClip=new SoundPlayer(url,1);
		}
	}
}
