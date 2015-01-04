package libraryManager;

import java.io.*;
import java.net.*;
import settings.*;

public class SearchingEngineOnLine implements Constants
{
	//debug
	private static final boolean debug=false;
	
	// 定义编码常数 
	public static final String CHARSET = "GBK";

	// google在线翻译引擎url 
	static final String engineUrl = "http://translate.google.com/translate_t";

	//skip常数，纪录跳过多少字节再读取
	private static final long SKIP=8160;
	
	//这两个是解释部分的位置的标记
	private static final String END_STR="</div>";
	private static final String START_STR="<div id=result_box dir=\"ltr\">";
	
	/** 
	 * 利用google在线翻译引擎实现翻译，并获取翻译内容
	 * 在网上翻译，似乎只有英文和其他语言之间的翻译能够成功，故采取两次翻译，利用英语搭桥
	 * 传入值非法的或其他异常情况，返回null
	 *  @param  translateText 要翻译的文本内容
	 *  @param  index  针对Constants.DEFAULT_LIBRARY_FROM和Constants.DEFAULT_LIBRARY_TO而言，用来是确定哪种翻译
	 */
	public static String translate(String translateText, int index)
	{
		//合法性检查
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		try 
		{
			// 第一次翻译
			translateText = translateToEnglish(translateText, index);

			System.out.println("firstTranslate: " + translateText);

			// 第二次翻译
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
	 * 这个方法将DEFAULT_LIBRARY_FROM[index]指定的语言翻译成英文
	 * 
	 */
	private static String translateToEnglish(String translateText, int index)
	throws MalformedURLException, IOException, UnsupportedEncodingException,UnknownHostException
	{
		//合法性检查
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		if(0==index)
		{
			return translateText;
		}
		
		// text是google翻译页面提交时对于欲翻译文字的变量名
		// langpair是google翻译页面提交时对于采用何种互对语言的变量名 
		URL url = new URL(engineUrl);

		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-agent", "IE/6.0");
		connection.setDoOutput(true);
		PrintWriter out=new PrintWriter(connection.getOutputStream());
		
		out.print("text="+myEncodeText(translateText)+"&");
		
		//生成决定翻译种类的参数
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

		in.skip(SKIP+translateText.length());  //这是为了直接把指针移动到读取解释的位置
		int counter=-1; //这个量用来寻找结尾
			    
	   	while((temp=in.read())!=-1)
		{
	   		//测试结尾
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
			
	   		//测试合法字符
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
	 * 这个方法将英文翻译成DEFAULT_LIBRARY_TO[index]指定的语言
	 * 
	 */
	private static String englishTranslateTo(String translateText, int index)

	throws MalformedURLException, IOException, UnsupportedEncodingException,UnknownHostException
	{
		//合法性检查
		if(null==translateText||0==translateText.length()||index<0||index>=DEFAULT_LIBRARY_FROM.length)
		{
			System.out.println("Error in method translage, illegal argument");
			return null;
		}
		
		if(4==index)
		{
			return translateText;
		}
		
		// text是google翻译页面提交时对于欲翻译文字的变量名
		// langpair是google翻译页面提交时对于采用何种互对语言的变量名 
		URL url = new URL(engineUrl);

		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-agent", "IE/6.0");
		connection.setDoOutput(true);
		PrintWriter out=new PrintWriter(connection.getOutputStream());
	    
		out.print("text="+myEncodeText(translateText)+"&");
		
		//生成决定翻译种类的参数
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

		in.skip(SKIP+translateText.length());  //这是为了直接把指针移动到读取解释的位置
		int counter=-1; //这个量用来寻找结尾
			    
	   	while((temp=in.read())!=-1)
		{
	   		//测试结尾
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
			
	   		//测试合法字符
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
	
	// 将文本进行URL编码 
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
			System.out.println(s=translate("他是一个好人，在南京大学上课,教数学，教得很好，同学们都很喜欢他，时间就这样过去了很长时间....。。?DSF",index));
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
 * 遗留问题，如何判断网络是否连通，以节省时间
 */
