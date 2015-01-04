package libraryInterface;

import java.io.*;

/**
 * ���ܺ�RandomAccessFile��ͬ������readUnicodeLine()����
 * ��writeUnicodeLine()����
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
	 * ����������轫Ҫ��ȡ��һ�ж���Unicode��ʽ���ַ�����ÿ���ַ���
	 * �����ֽ���д��������'\n'��ʱ��ֹͣ,'\n'���Զ�������
	 * 
	 * ����Ѿ�����β���򷵻�null
	 * 
	 */
	public String readUnicodeLine()
	{
	
   	    StringBuffer buffer = new StringBuffer();
		char tempChar = 0;
		boolean readSomething = false; //��������������ǲ����Ѿ�������β

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
	 * ���������Unicode��ʽд���ַ��������Զ���ĩβ����'\n'
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
	 * ���������Unicode��ʽд���ַ�����������
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
	 * ���������ȡһ�г��ȣ��ٶ�һ����'\n'��β������
	 * ��Unicode��д,������ִ����򷵻�-1,�������
	 * �ļ���β��û���ҵ�'\n'���᷵��-1
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
