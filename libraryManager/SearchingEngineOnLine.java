package libraryManager;

import java.io.*;
import java.net.*;
import settings.*;

public class SearchingEngineOnLine implements Constants
{
	//debug
	private static final boolean debug=false;
	
	// ������볣�� 
	public static final String CHARSET = "GBK";

	// google���߷�������url 
	static final String engineUrl = "http://translate.google.com/translate_t";

	//skip��������¼���������ֽ��ٶ�ȡ
	private static final long SKIP=8160;
	
	//�������ǽ��Ͳ��ֵ�λ�õı��
	private static final String END_STR="</div>";
	private static final String START_STR="<div id=result_box dir=\"ltr\">";
	
	/** 
	 * ����google���߷�������ʵ�ַ��룬����ȡ��������
	 * �����Ϸ��룬�ƺ�ֻ��Ӣ�ĺ���������֮��ķ����ܹ��ɹ����ʲ�ȡ���η��룬����Ӣ�����
	 * ����ֵ�Ƿ��Ļ������쳣���������null
	 *  @param  translateText Ҫ������ı�����
	 *  @param  index  ���Constants.DEFAULT_LIBRARY_FROM��Constants.DEFAULT_LIBRARY_TO���ԣ�������ȷ�����ַ���
	 */
	public static String translate(String translateText, int index)
	{
		//�Ϸ��Լ��
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		try 
		{
			// ��һ�η���
			translateText = translateToEnglish(translateText, index);

			System.out.println("firstTranslate: " + translateText);

			// �ڶ��η���
			translateText = englishTranslateTo(translateText, index);

			System.out.println("secondTranslate: " + translateText);

			return translateText;

		}
		catch(Exception ex)
		{
			System.out.println("Error in translate");
			return null;
		}
	}
	
	/**
	 * ���������DEFAULT_LIBRARY_FROM[index]ָ�������Է����Ӣ��
	 * 
	 */
	private static String translateToEnglish(String translateText, int index)
	throws MalformedURLException, IOException, UnsupportedEncodingException,UnknownHostException
	{
		//�Ϸ��Լ��
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		if(0==index)
		{
			return translateText;
		}
		
		// text��google����ҳ���ύʱ�������������ֵı�����
		// langpair��google����ҳ���ύʱ���ڲ��ú��ֻ������Եı����� 
		URL url = new URL(engineUrl);

		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-agent", "IE/6.0");
		connection.setDoOutput(true);
		PrintWriter out=new PrintWriter(connection.getOutputStream());
		
		out.print("text="+myEncodeText(translateText)+"&");
		
		//���ɾ�����������Ĳ���
		String sl="sl=";
		switch(index)
		{
		case 0:{
			return translateText;
		}
		case 1:{
			sl+="ja";
			break;
		}
		case 2:{
			sl+="fr";
			break;
		}
		case 3:{
			sl+="de";
			break;
		}
		
		case 4:{
			sl+="zh-CN";
			break;
		}
		case 5:{
			sl+="zh-CN";
			break;
		}
		case 6:{
			sl+="zh-CN";
			break;
		}
		case 7:{
			sl+="zh-CN";
			break;
		}
		
		default :{
			sl+="zh-CN";
			break;
		}
			
		}
		out.print(sl+"&"+"tl=en");
				
		if(debug)
		{
			System.out.println("encodeText(translateText): "+myEncodeText(translateText));
		}
		
		out.close();
		
		BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),CHARSET));
		StringBuffer buffer=new StringBuffer();
		int temp=-1;

		in.skip(SKIP+translateText.length());  //����Ϊ��ֱ�Ӱ�ָ���ƶ�����ȡ���͵�λ��
		int counter=-1; //���������Ѱ�ҽ�β
			    
	   	while((temp=in.read())!=-1)
		{
	   		//���Խ�β
	   		if(counter<END_STR.length()-1)
			{
				if(temp==END_STR.charAt(counter+1))
				{
					counter++;
				}
				else
				{
					counter=-1;
				}
			}
			else
			{
			    break;	
			}
			
	   		//���ԺϷ��ַ�
	   		if((temp>='a'&&temp<='z')||(temp>='A'&&temp<='Z')
	   				||(' '==temp)||('.'==temp)||('='==temp)
	   				||('_'==temp)||('<'==temp)||('>'==temp)
	   				||('\"'==temp)||(','==temp)||('/'==temp))
	   		{
	   			buffer.append((char)temp);
			}
	   		
		}
		in.close();
		
	    if(debug)
        {
        	PrintWriter archiveOut=new PrintWriter(new File("D:/archive.txt"));
            archiveOut.println(buffer.toString());
            archiveOut.close();
        }
        	    
        return getContent(buffer.toString(),translateText.length());
	}
		
	/**
	 * ���������Ӣ�ķ����DEFAULT_LIBRARY_TO[index]ָ��������
	 * 
	 */
	private static String englishTranslateTo(String translateText, int index)

	throws MalformedURLException, IOException, UnsupportedEncodingException,UnknownHostException
	{
		//�Ϸ��Լ��
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		if(4==index)
		{
			return translateText;
		}
		
		// text��google����ҳ���ύʱ�������������ֵı�����
		// langpair��google����ҳ���ύʱ���ڲ��ú��ֻ������Եı����� 
		URL url = new URL(engineUrl);

		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-agent", "IE/6.0");
		connection.setDoOutput(true);
		PrintWriter out=new PrintWriter(connection.getOutputStream());
	    
		out.print("text="+myEncodeText(translateText)+"&");
		
		//���ɾ�����������Ĳ���
		String tl="tl=";
		switch(index)
		{
		case 0:{
			tl+="zh-CN";
			break;
		}
		case 1:{
			tl+="zh-CN";
			break;
		}
		case 2:{
			tl+="zh-CN";
			break;
		}
		case 3:{
			tl+="zh-CN";
			break;
		}
		
		case 4:{
			return translateText;
		}
		case 5:{
			tl+="ja";
			break;
		}
		case 6:{
			tl+="fr";
		break;
		}
		case 7:{
			tl+="de";
			break;
		}
		
		default :{
			tl+="zh-CN";
			break;
		}
			
		}
		out.print("sl=en"+"&"+tl);
				
		if(debug)
		{
			System.out.println("encodeText(translateText): "+myEncodeText(translateText));
		}
		
		out.close();
		
		BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),CHARSET));
		StringBuffer buffer=new StringBuffer();
		int temp=-1;

		in.skip(SKIP+translateText.length());  //����Ϊ��ֱ�Ӱ�ָ���ƶ�����ȡ���͵�λ��
		int counter=-1; //���������Ѱ�ҽ�β
			    
	   	while((temp=in.read())!=-1)
		{
	   		//���Խ�β
	   		if(counter<END_STR.length()-1)
			{
				if(temp==END_STR.charAt(counter+1))
				{
					counter++;
				}
				else
				{
					counter=-1;
				}
			}
			else
			{
			    break;	
			}
			
	   		//���ԺϷ��ַ�
	   		if((temp>='a'&&temp<='z')||(temp>='A'&&temp<='Z')
	   				||(' '==temp)||('.'==temp)||('='==temp)
	   				||('_'==temp)||('<'==temp)||('>'==temp)
	   				||('\"'==temp)||(','==temp)||('/'==temp))
	   		{
	   			buffer.append((char)temp);
			}
	   		else
	   		{
	   			if(temp>127)
	   			{
	   				if(!((4==index)||(6==index)|(7==index)))
	   				{
	   					buffer.append((char)temp);
	   				}
	   			}
	   		}
	   		
		}
		in.close();
		
	    if(debug)
        {
        	PrintWriter archiveOut=new PrintWriter(new File("D:/archive.txt"));
            archiveOut.println(buffer.toString());
            archiveOut.close();
        }
        	    
        return getContent(buffer.toString(),translateText.length());
	}
	
	private static String getContent(String s,int length)
	{
		if(null==s||0==s.length())
		{
			return null;
		}
		
		int startIndex=s.lastIndexOf(START_STR);
		int endIndex=s.indexOf(END_STR,startIndex);
	
		if(startIndex>=0&&endIndex>=0&&(startIndex+START_STR.length()<=endIndex))
		{
			return s.substring(startIndex+START_STR.length(),endIndex);
		}
		else
		{
			return null;
		}
	}
	
	// ���ı�����URL���� 
	/*private static String encodeText(String text)
	{
		try
		{
			String str=URLEncoder.encode(text,"UTF-8");
			return str;
		}
		catch(UnsupportedEncodingException ex)
		{
			System.out.println("Error when encodeText in NetChToEnTrans");
			ex.printStackTrace();
			return null;
		}
	}*/
	
	private static String myEncodeText(String text) {
		if(null==text)
		{
			return null;
		}
		
		char[] array=text.toCharArray();
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<text.length();i++)
		{
			if(!isLetterOrDigit(array[i]))
			{
				if (array[i]!=' ') 
				{
					if ((array[i] <= 127) ||(false))
					{
						StringBuffer code = new StringBuffer();
						int temp = array[i];
						while (temp != 0)
						{
							if (temp % 16 <= 9)
							{
								code.append(temp);
							} 
							else 
							{
								code.append((char)((temp % 16 - 10) + 'A'));
							}

							temp /= 16;
						}
						code = code.reverse();

						buffer.append("%" + code.toString());
					} else {
						buffer.append(array[i]);
					}
				}
				else
				{
					buffer.append("+");
				}
			}
			else
			{
				buffer.append(array[i]);
			}
		}
		
		return buffer.toString();

	}

	public static boolean isLetterOrDigit(char c)
	{
		if(c>='a'&&c<='z')
		{
			return true;
		}
		
		if(c>='A'&&c<='Z')
		{
			return true;
		}
		
		if(c>='0'&&c<='9')
		{
			return true;
		}
		
		return false;
	}
	
	//test
	public static void main(String[] args) {
		try
		{	
			String s=null;
			int index=5;
			System.out.println(s=translate("����һ�����ˣ����Ͼ���ѧ�Ͽ�,����ѧ���̵úܺã�ͬѧ�Ƕ���ϲ������ʱ���������ȥ�˺ܳ�ʱ��....����?DSF",index));
			System.out.println("\n\n");
	        System.out.println(translate(s,index-4));
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
/**
 * �������⣬����ж������Ƿ���ͨ���Խ�ʡʱ��
 */
