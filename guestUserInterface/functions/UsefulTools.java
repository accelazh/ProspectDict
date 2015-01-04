package guestUserInterface.functions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * ������ṩһЩ���õĹ���
 * @author ZYL
 *
 */
public class UsefulTools 
{
	/**
	 * �����ļ�,�ɹ��򷵻�true
	 */
    public static boolean copyFile(File from, File to)
    {
    	if(null==from||!from.exists())
    	{
    		System.out.println("Error in copyFile, null argument from");
    		return false;
    	}
    	
    	try
    	{
    	    BufferedInputStream in=new BufferedInputStream(new FileInputStream(from));
    	    BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(to));
    	   
    	    int r=-1;
    	    while((r=in.read())!=-1)
    	    {
    	    	out.write((byte)r);
    	    }
    	
    	    in.close();
    	    out.close();
    	    
    	    return true;
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in copyFile, IOException");
    		ex.printStackTrace();
    		return false;
    	}
    	
    }

}
