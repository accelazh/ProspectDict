package libraryInterface;

import java.io.*;
import java.util.*;

/**
 * 
 * @author ZYL
 * 对应词库的账目信息,建立library,libraryManager都要通过这个方法，
 * 这个方法实际上就是字典词库的标识符
 * 
 */
public class LibraryAccount implements Serializable
{
	public static final long serialVersionUID=2;
	
	/**
	 * 词库的单词头的文件
	 */
	private File wordsFile;
    /**
     * 词库的解释的文件
     */
	private File transFile;
	/**
	 * 词库是将什么翻译成什么
	 */
	private String from;
	/**
	 * 词库是将什么翻译成什么
	 */
	private String to;
	/**
	 * 词库的名字
	 */
	private String name;
	/**
	 * 词库的解释部分一条占多少字符(所有词条长度一致)，
	 * 这个长度不包括每行结尾的'\n'
	 */
    private int lengthOfLineInTransFile;
    /**
     * 词库一共有多少条
     */
    private long totalNumOfTransEntries;
    /**
     * 词库的使用频数
     */
    private long frequency=0;
    /**
     * 词库的单词的字符集,即所有单词所含有的字符的集合
     */
    private HashSet<Character> wordChars;
    /**
     * 词库字符集的最小元
     */
    private Character minChar;
    /**
     * 词库字符集的最大元 
     */
    private Character maxChar;
    
  	public LibraryAccount(String from, String to, String name, File wordsFile, File transFile, int lengthOfLine, long totalNumOfEntries, HashSet<Character> wordChars, Character minChar, Character maxChar)
    {
    	this.from=from;
    	this.to=to;
    	this.name=name;
    	this.wordsFile=wordsFile;
    	this.transFile=transFile;
    	this.lengthOfLineInTransFile=lengthOfLine;
    	this.totalNumOfTransEntries=totalNumOfEntries;
        this.wordChars=wordChars;
        this.minChar=minChar;
        this.maxChar=maxChar;
    }
    
    /**
     * 这个构造方法不用于建立真正的LibraryAccount，只是用来测试的
     */
    protected LibraryAccount(String from, String to, String name)
    {
    	this.from=from;
    	this.to=to;
    	this.name=name;
    }

    /**
     * 这个方法在transFile文件中读写以
     * 计算行长度如果文件不存在或者有其他
     * 问题，则返回-1
     */   
    public int calculateLengthOfLineInTransFile()
    {
    	if(null==transFile)
    	{
    		return -1;
    	}
    	
    	int length=-1;
    	
    	try
    	{
    		MyRandomAccessFile in=new MyRandomAccessFile(transFile,"r");
    	    int temp=-1;
    	    while((temp=in.calculateUnicodeLineLength())!=-1)
    	    {
    	    	if(length!=-1)
    	    	{
    	    		if(temp!=length)
    	    		{
    	    			return -1;
    	    		}
    	    	}
    	    	else
    	    	{
    	    		length=temp;
    	    	}
    	    }
    	    in.close();
    	    
    	    return length;
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in calculateLengthOfLineInTransFile()");
    		ex.printStackTrace();
    		
    		return -1;
    	}
    }

    /**
     * 
     * 这个方法检查每行长度的正确性，正确则返回true
     */
    public boolean checkLengthOfLineInTransFile()
    {
    	if(null==transFile||lengthOfLineInTransFile<=0)
    	{
    		return false;
    	}
    	
    	try
    	{
    		MyRandomAccessFile in=new MyRandomAccessFile(transFile,"r");
    	    byte[] lineBytes=new byte[2*(lengthOfLineInTransFile+1)];
    		while(in.read(lineBytes)!=-1)
    	    {
    	    	if(lineBytes[lineBytes.length-1]!=(byte)('\n'))
    	    	{
    	    		return false;
    	    	}
    	    }
    		
    		return true;
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in calculateLengthOfLineInTransFile()");
    		ex.printStackTrace();
    		
    		return false;
    	}
    }
    
    /**
     * 这个方法在transFile文件中读写以
     * 计算总词数如果文件不存在或者有其他
     * 问题，则返回-1
     */ 
    public long calculateTotalNumOfTransEntries()
    {
    	if(null==transFile)
    	{
    		return -1;
    	}
    	
    	long num=0;
    	
    	try
    	{
    		MyRandomAccessFile in=new MyRandomAccessFile(transFile,"r");
    	    while(in.readUnicodeLine()!=null)
    	    {
    	    	num++;
    	    }
    	    in.close();
    	    
    	    return num;
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in calculateLengthOfLineInTransFile()");
    		ex.printStackTrace();
    		
    		return -1;
    	}
    }
    
    /**
     * 这个方法检查这个帐目是否包含的信息
     * 都合法,是则返回true
     * 
     */
    public boolean isLibraryAccountValid()
    {
    	if(wordsFile!=null
    			&&wordsFile.exists()
    			&&transFile!=null
    			&&transFile.exists()
    			&&from!=null
    			&&to!=null
    			&&from.length()!=0
    			&&to.length()!=0
    			&&name!=null
    	        &&name.length()!=0
    	        &&wordChars!=null
    	        &&wordChars.size()>0
    	        &&minChar!=null
    	        &&maxChar!=null
    	        &&minChar<=maxChar
    			&&lengthOfLineInTransFile>0
    			&&totalNumOfTransEntries>0)
    	{

    		if(true||checkLengthOfLineInTransFile())   //这条检查太费时间，先省去
    		{
    			if(true||((calculateTotalNumOfTransEntries()==totalNumOfTransEntries)
    	        	&&(totalNumOfTransEntries>0)))  //这条检查太费时间，先省去
    			{
    				return true;
    			}
    			else
    			{
    				System.out.println("LibraryAccout check valid failed: wrong totalNumOfTransEntries");
    				 return false;
    			}
    		}
    		else
    		{
    			System.out.println("LibraryAccout check valid failed: wrong line length");
        	    return false;
    		}
    
    	}
    	else
    	{
    		System.out.println("LibraryAccout check valid failed: invalid data filed");
    	    return false;
    	}
 	   	
    }
    
    /**
     * 这个方法检查该libraryAccount的正确性，
     * 检查全面，但较费时间
     * 
     */
    public boolean checkValidStrictly()
    {
    	if(wordsFile!=null
    			&&wordsFile.exists()
    			&&transFile!=null
    			&&transFile.exists()
    			&&from!=null
    			&&to!=null
    			&&from.length()!=0
    			&&to.length()!=0
    			&&name!=null
    	        &&name.length()!=0
    	        &&wordChars!=null
    	        &&wordChars.size()>0
    	        &&minChar!=null
    	        &&maxChar!=null
    	        &&minChar<=maxChar
    			&&lengthOfLineInTransFile>0
    			&&totalNumOfTransEntries>0)
    	{

    		if((calculateLengthOfLineInTransFile()==lengthOfLineInTransFile)
    				&&(lengthOfLineInTransFile>0))   //这条检查太费时间，先省去
    		{
    			if(((calculateTotalNumOfTransEntries()==totalNumOfTransEntries)
    	        	&&(totalNumOfTransEntries>0)))  //这条检查太费时间，先省去
    			{
    				return true;
    			}
    			else
    			{
    				System.out.println("LibraryAccout check valid failed: wrong totalNumOfTransEntries");
    				 return false;
    			}
    		}
    		else
    		{
    			System.out.println("LibraryAccout check valid failed: wrong line length");
        	    return false;
    		}
    
    	}
    	else
    	{
    		System.out.println("LibraryAccout check valid failed: invalid data filed");
    	    return false;
    	}
    }
    
	public File getWordsFile() {
		return wordsFile;
	}

	public File getTransFile() {
		return transFile;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
	public String getName() {
		return name;
	}

	public int getLengthOfLineInTransFile() {
		return lengthOfLineInTransFile;
	}

	public long getTotalNumOfTransEntries() {
		return totalNumOfTransEntries;
	}

	public long getFrequency() {
		return frequency;
	}
	
	public void increaseFrequency()
	{
		frequency++;
	}

	@Override
	public boolean equals(Object o) 
	{
		LibraryAccount libAccount=(LibraryAccount)o;
		if((libAccount.getFrom().equals(this.getFrom()))
			&&(libAccount.getTo().equals(this.getTo()))
			&&(libAccount.getName().equals(this.getName())))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	
	public String briefToString()
	{
		String output="LibraryAccount: \n";
		output+="wordsFile: "+wordsFile+"\n";
		output+="transFile: "+transFile+"\n";
		output+="from: "+from+"\n";
		output+="to: "+to+"\n";
		output+="name: "+name+"\n";
		output+="lengthOfLineInTransFile: "+lengthOfLineInTransFile+"\n";
		output+="totalNumOfTransEntries: "+totalNumOfTransEntries+"\n";
		output+="minChar: "+minChar;
		output+="maxChar: "+maxChar;
		output+="isValid: "+isLibraryAccountValid()+"\n";
	
		return output;
	}
	
	@Override
	public String toString()
	{
		String output="LibraryAccount: \n";
		output+="wordsFile: "+wordsFile+"\n";
		output+="transFile: "+transFile+"\n";
		output+="from: "+from+"\n";
		output+="to: "+to+"\n";
		output+="name: "+name+"\n";
		output+="lengthOfLineInTransFile: "+lengthOfLineInTransFile+"\n";
		output+="totalNumOfTransEntries: "+totalNumOfTransEntries+"\n";
		output+="minChar: "+minChar;
		output+="maxChar: "+maxChar;
		output+="isValid: "+isLibraryAccountValid()+"\n";
		
		try
		{
			//显示字符集
			output+="wordChars: "+"\n";
		    if(null==wordChars)
		    {
		    	throw new IOException();
		    }
		    Character[] wordCharCharacters=wordChars.toArray(new Character[0]);
		    for(int i=0;i<wordCharCharacters.length;i++)
		    {
		    	output+=wordCharCharacters[i]+", ";
		    }
		    output+="\n";
		
		    //显示词库内容
	    	/*output+="\n====Display Contents====\n";
			ArrayList<WordEntry> wordEntries=Library.readWordEntries(getWordsFile());
			if(null==wordEntries)
			{
				throw new IOException();
			}
			
			//开始找解释
			Library library=Library.createLibrary(this);
			
			for(int i=0;i<wordEntries.size();i++)
			{
				if(wordEntries.get(i)!=null)
				{
					output+=("|"+wordEntries.get(i).getWord()+"|"+"\t");
					output+="|"+(library.getTransEntry(wordEntries.get(i)))+"|"+"\n";
				}
			}*/
			
		    //返回
			return output;
		}
		catch(IOException ex)
		{
			System.out.println("Error in toString of LibraryAccount, IOException");
			ex.printStackTrace();
			return null;
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Error in toString of LibraryAccount, NumberFormatException");
			ex.printStackTrace();
			return null;
		}
		
	}

	public HashSet<Character> getWordChars() {
		return wordChars;
	}
	
	 public Character getMinChar() {
		return minChar;
	}

	public Character getMaxChar() {
		return maxChar;
	}


}
