package libraryInterface;

import java.io.*;

/**
 * 功能和RandomAccessFile相同，新增readUnicodeLine()方法
 * 和writeUnicodeLine()方法
 * @author ZYL
 *
 */
public class MyRandomAccessFile extends RandomAccessFile
{
	public MyRandomAccessFile(File name, String mode) throws FileNotFoundException
	{
		super(name,mode);
	}
	
	public MyRandomAccessFile(String name, String mode) throws FileNotFoundException
	{
		super(name,mode);
	}
	
	/**
	 * 这个方法假设将要读取的一行都是Unicode格式的字符，即每个字符用
	 * 两个字节来写，当读到'\n'的时候停止,'\n'被自动舍弃。
	 * 
	 * 如果已经到流尾，则返回null
	 * 
	 */
	public String readUnicodeLine()
	{
	
   	    StringBuffer buffer = new StringBuffer();
		char tempChar = 0;
		boolean readSomething = false; //这个量用来考察是不是已经到了流尾

		try 
		{
			while (true) 
			{
				tempChar = readChar();
				readSomething = true;
				if (tempChar != '\n') 
				{
					buffer.append(tempChar);
				} 
				else 
				{
					break;
				}
			}

			return buffer.toString();
		} 
		catch (IOException ex) 
		{
			return readSomething ? buffer.toString() : null;
		}
	}
	
	/**
	 * 这个方法以Unicode格式写出字符串，并自动在末尾加上'\n'
	 * 
	 * @param s
	 * @throws IOException
	 */
	public void writeUnicodeLine(String s) throws IOException
	{
		if(null==s)
		{
			return;
		}
		
		for(int i=0;i<s.length();i++)
		{
			writeChar(s.charAt(i));
		}
		
		writeChar('\n');
	}
	
	/**
	 * 这个方法以Unicode格式写出字符串，不换行
	 * 
	 * @param s
	 * @throws IOException
	 */
	public void writeUnicodeString(String s) throws IOException
	{
		if(null==s)
		{
			return;
		}
		
		for(int i=0;i<s.length();i++)
		{
			writeChar(s.charAt(i));
		}
	}
	
	/**
	 * 这个方法读取一行长度，假定一行以'\n'结尾，并且
	 * 用Unicode书写,如果出现错误，则返回-1,如果到达
	 * 文件结尾还没有找到'\n'，会返回-1
	 * @return
	 */
	public int calculateUnicodeLineLength()
	{
		try
		{
			int length=0;
	    	while(true)
	    	{
	    		if(getFilePointer()>=length())
		    	{
		    		return -1;
		    	}
	    		
		    	int ch1=read();
		    	int ch2=read();
		    	if(0==ch1&&'\n'==ch2)
		    	{
		    		break;
		    	}
		    	length++;
		    	    	
		    }
	    	
	    	return length;
		}
		catch(IOException ex)
		{
			return -1;
		}
	}
		
	

}
