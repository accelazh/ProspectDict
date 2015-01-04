package guestUserInterface.magneticWindows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * ���Դ��ڻ���䶼�ܷ����������ã����
 * ��Ҫһ��������������
 * @author ZYL
 *
 */
public class MagGroup 
{
	/**
	 * �þ����ʾ��������Ա֮�以����ס��
	 */
	private boolean[][] isStuck=new boolean[0][0];
	
	private ArrayList<Magnetic> windows=new ArrayList<Magnetic>();
	
	public MagGroup()
	{
		
	}
	/**
	 * ������¾��󣬵�Ԫ��������������ʱ��ᱻʹ��
	 */
	private void initIsStuck()
	{
		boolean[][] newMatrix=new boolean[windows.size()][windows.size()];
	    
		//��ʼ������
		for(int i=0;i<newMatrix.length;i++)
	    {
	    	for(int j=0;j<newMatrix.length;j++)
	    	{
	    		newMatrix[i][j]=false;	    
	    	}
	    }
		
		//����ԭ���������
		int length=Math.min(windows.size(),isStuck.length);
		
	    for(int i=0;i<length;i++)
	    {
	    	for(int j=0;j<length;j++)
	    	{
	    		newMatrix[i][j]=isStuck[i][j];
	    	}
	    }

	   	//����
	   	isStuck=newMatrix;
	}
	
	/**
	 * ����һ������
	 */
	public void add(Magnetic w)
	{
		if(null==w)
		{
			return;
		}
		
		windows.add(w);
		
		//���¾���
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
	 * @return a��b�Ƿ��໥��ס
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
	 * ��a��b��״̬��Ϊ��ס
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
