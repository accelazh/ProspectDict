package games.survive;

/**
 * 这个类定义所有可以进行配置的参数
 * 
 * @author ZYL
 *
 */
public class ConfigValues
{
	private static double ballAverageV=ConfigWrap.default_ballAV;
	private static double craftAverageV=ConfigWrap.default_craftV;
	
	private static int ballGenerageCycle=ConfigWrap.default_ballGT;

	public static double getBallAverageV()
	{
		return ballAverageV;
	}

	public static void setBallAverageV(double ballAverageV)
	{
		ConfigValues.ballAverageV = ballAverageV;
	}

	public static double getCraftAverageV()
	{
		return craftAverageV;
	}

	public static void setCraftAverageV(double craftAverageV)
	{
		ConfigValues.craftAverageV = craftAverageV;
	}

	public static int getBallGenerageCycle()
	{
		return ballGenerageCycle;
	}

	public static void setBallGenerageCycle(int ballGenerageCycle)
	{
		ConfigValues.ballGenerageCycle = ballGenerageCycle;
	}

}
