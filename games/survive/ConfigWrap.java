package games.survive;

/**
 * 
 * 这个类用来包装配置参数
 * @author ZYL
 *
 */
public class ConfigWrap
{
	//这些是参数的默认值
	public static final double default_ballAV=280;   
	public static final double default_craftV=400;  
	public static final int default_ballGT=5;  
	
	private double ballAV=default_ballAV;
	private double craftV=default_craftV;
	private int ballGT=default_ballGT;

	public ConfigWrap()
	{
		
	}
	
	public boolean checkValid()
	{
		if(ballAV<=0)
		{
			return false;
		}
		
		if(craftV<=0)
		{
			return false;
		}
		
		if(ballGT<0)
		{
			return false;
		}
		
		return true;
	}
	
	public void resetToDefault()
	{
		ballAV=default_ballAV;
		craftV=default_craftV;
		ballGT=default_ballGT;
	}

	public double getBallAV()
	{
		return ballAV;
	}

	public void setBallAV(double ballAV)
	{
		this.ballAV = ballAV;
	}

	public double getCraftV()
	{
		return craftV;
	}

	public void setCraftV(double craftV)
	{
		this.craftV = craftV;
	}

	public int getBallGT()
	{
		return ballGT;
	}

	public void setBallGT(int ballGT)
	{
		this.ballGT = ballGT;
	}
	
	
	
}
