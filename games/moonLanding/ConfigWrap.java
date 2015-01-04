package games.moonLanding;

/**
 * 
 * �����������װ���ò���
 * @author ZYL
 *
 */
public class ConfigWrap
{
	//��Щ�ǲ�����Ĭ��ֵ
	public static final double default_vLimit=80;   
	public static final double default_gravity=800;  
	public static final double default_leftAcc=0.8*default_gravity;  
	public static final double default_rightAcc=0.8*default_gravity;  
	public static final double default_downAcc=1.5*default_gravity;
	public static final double default_upAcc=0.8*default_gravity;
	public static final int default_totalFuel=400;
	
	private double vLimit=default_vLimit;   //�Ӵ������ʱ��ֻ���ٶ�����һ�²���ɹ�
	private double gravity=default_gravity;  //����/s^2
	private double leftAcc=default_leftAcc;  //�ɴ�������ڵ�������ٶ�
	private double rightAcc=default_rightAcc;  //�ɴ���������ڵ�������ٶ�
	private double downAcc=default_downAcc;
	private double upAcc=default_upAcc;
	private int totalFuel=default_totalFuel;

	public ConfigWrap()
	{
		
	}
	
	public void resetToDefault()
	{
		vLimit=default_vLimit;   
		gravity=default_gravity;  
		leftAcc=default_leftAcc;  
		rightAcc=default_rightAcc;  
		downAcc=default_downAcc;
		upAcc=default_upAcc;
		totalFuel=default_totalFuel;
		
	}
	
	public boolean checkValid()
	{
		if(gravity<=0)
		{
			return false;
		}
		
		if(vLimit<=0)
		{
			return false;
		}
		
		if(leftAcc<=0)
		{
			return false;
		}
		
		if(rightAcc<=0)
		{
			return false;
		}
		
		if(upAcc<=0)
		{
			return false;
		}
		
		if(downAcc<=0)
		{
			return false;
		}
		
		if(totalFuel<=0)
		{
			return false;
		}
		
		return true;
	}

	public double getVLimit()
	{
		return vLimit;
	}

	public void setVLimit(double limit)
	{
		vLimit = limit;
	}

	public double getGravity()
	{
		return gravity;
	}

	public void setGravity(double gravity)
	{
		this.gravity = gravity;
	}

	public double getLeftAcc()
	{
		return leftAcc;
	}

	public void setLeftAcc(double leftAcc)
	{
		this.leftAcc = leftAcc;
	}

	public double getRightAcc()
	{
		return rightAcc;
	}

	public void setRightAcc(double rightAcc)
	{
		this.rightAcc = rightAcc;
	}

	public double getDownAcc()
	{
		return downAcc;
	}

	public void setDownAcc(double downAcc)
	{
		this.downAcc = downAcc;
	}

	public double getUpAcc()
	{
		return upAcc;
	}

	public void setUpAcc(double upAcc)
	{
		this.upAcc = upAcc;
	}

	public int getTotalFuel()
	{
		return totalFuel;
	}

	public void setTotalFuel(int totalFuel)
	{
		this.totalFuel = totalFuel;
	}
	
	
}
