package guestUserInterface.magneticWindows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * 磁性窗口互相间都能发生磁性作用，因此
 * 需要一个组来完成这项工作
 * @author ZYL
 *
 */
public class MagGroup 
{
	/**
	 * 用矩阵表示哪两个成员之间互相吸住了
	 */
	private boolean[][] isStuck=new boolean[0][0];
	
	private ArrayList<Magnetic> windows=new ArrayList<Magnetic>();
	
	public MagGroup()
	{
		
	}
	/**
	 * 这个更新矩阵，当元素数量有增减的时候会被使用
	 */
	private void initIsStuck()
	{
		boolean[][] newMatrix=new boolean[windows.size()][windows.size()];
	    
		//初始化矩阵
		for(int i=0;i<newMatrix.length;i++)
	    {
	    	for(int j=0;j<newMatrix.length;j++)
	    	{
	    		newMatrix[i][j]=false;	    
	    	}
	    }
		
		//拷贝原矩阵的内容
		int length=Math.min(windows.size(),isStuck.length);
		
	    for(int i=0;i<length;i++)
	    {
	    	for(int j=0;j<length;j++)
	    	{
	    		newMatrix[i][j]=isStuck[i][j];
	    	}
	    }

	   	//更换
	   	isStuck=newMatrix;
	}
	
	/**
	 * 加入一个窗口
	 */
	public void add(Magnetic w)
	{
		if(null==w)
		{
			return;
		}
		
		windows.add(w);
		
		//更新矩阵
		initIsStuck();
	}

	public void remove(Magnetic w)
	{
		if(windows.remove(w))
		{
			initIsStuck();
		}
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return a和b是否相互吸住
	 */
	public boolean isAStuckWithB(Magnetic a, Magnetic b)
	{
		int indexOfA=windows.indexOf(a);
		int indexOfB=windows.indexOf(b);
		
		if(indexOfA<0||indexOfB<0)
		{
			return false;
		}
		
		return isStuck[indexOfA][indexOfB]
		    ||isStuck[indexOfB][indexOfA];
		
	}

	/**
	 * 将a、b的状态设为吸住
	 * @param a
	 * @param b
	 * 
	 */
	public void setAStuckWithB(Magnetic a, Magnetic b)
	{
		int indexOfA=windows.indexOf(a);
		int indexOfB=windows.indexOf(b);
		
		if(indexOfA<0||indexOfB<0)
		{
			return;
		}
		
		isStuck[indexOfA][indexOfB]=true;
		isStuck[indexOfB][indexOfA]=true;
	}

	public Magnetic get(int index)
	{
		if(index<0||index>windows.size()-1)
		{
			return null;
		}
		
		return windows.get(index);
	}
	
	public int size()
	{
		return windows.size();
	}

	public String toString()
	{
		String output="";
		output+="==MagGroup=="+"\n";
		output+="size: "+size()+"\n";
		output+="isStuck: "+"\n";
		
		for(int i=0;i<isStuck.length;i++)
		{
			for(int j=0;j<isStuck[i].length;j++)
			{
				output+=isStuck[i][j]+" ";
			}
			output+="\n";
		}
		
		return output;
	}
}
