package libraryInterface;

import java.io.*;
import java.util.*;

/**
 * 
 * @author ZYL
 * ��Ӧ�ʿ����Ŀ��Ϣ,����library,libraryManager��Ҫͨ�����������
 * �������ʵ���Ͼ����ֵ�ʿ�ı�ʶ��
 * 
 */
public class LibraryAccount implements Serializable
{
	public static final long serialVersionUID=2;
	
	/**
	 * �ʿ�ĵ���ͷ���ļ�
	 */
	private File wordsFile;
    /**
     * �ʿ�Ľ��͵��ļ�
     */
	private File transFile;
	/**
	 * �ʿ��ǽ�ʲô�����ʲô
	 */
	private String from;
	/**
	 * �ʿ��ǽ�ʲô�����ʲô
	 */
	private String to;
	/**
	 * �ʿ������
	 */
	private String name;
	/**
	 * �ʿ�Ľ��Ͳ���һ��ռ�����ַ�(���д�������һ��)��
	 * ������Ȳ�����ÿ�н�β��'\n'
	 */
    private int lengthOfLineInTransFile;
    /**
     * �ʿ�һ���ж�����
     */
    private long totalNumOfTransEntries;
    /**
     * �ʿ��ʹ��Ƶ��
     */
    private long frequency=0;
    /**
     * �ʿ�ĵ��ʵ��ַ���,�����е��������е��ַ��ļ���
     */
    private HashSet<Character> wordChars;
    /**
     * �ʿ��ַ�������СԪ
     */
    private Character minChar;
    /**
     * �ʿ��ַ��������Ԫ 
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
     * ������췽�������ڽ���������LibraryAccount��ֻ���������Ե�
     */
    protected LibraryAccount(String from, String to, String name)
    {
    	this.from=from;
    	this.to=to;
    	this.name=name;
    }

    /**
     * ���������transFile�ļ��ж�д��
     * �����г�������ļ������ڻ���������
     * ���⣬�򷵻�-1
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
     * ����������ÿ�г��ȵ���ȷ�ԣ���ȷ�򷵻�true
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
     * ���������transFile�ļ��ж�д��
     * �����ܴ�������ļ������ڻ���������
     * ���⣬�򷵻�-1
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
     * ���������������Ŀ�Ƿ��������Ϣ
     * ���Ϸ�,���򷵻�true
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

    		if(true||checkLengthOfLineInTransFile())   //�������̫��ʱ�䣬��ʡȥ
    		{
    			if(true||((calculateTotalNumOfTransEntries()==totalNumOfTransEntries)
    	        	&&(totalNumOfTransEntries>0)))  //�������̫��ʱ�䣬��ʡȥ
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
     * �����������libraryAccount����ȷ�ԣ�
     * ���ȫ�棬���Ϸ�ʱ��
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
    				&&(lengthOfLineInTransFile>0))   //�������̫��ʱ�䣬��ʡȥ
    		{
    			if(((calculateTotalNumOfTransEntries()==totalNumOfTransEntries)
    	        	&&(totalNumOfTransEntries>0)))  //�������̫��ʱ�䣬��ʡȥ
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
			//��ʾ�ַ���
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
		
		    //��ʾ�ʿ�����
	    	/*output+="\n====Display Contents====\n";
			ArrayList<WordEntry> wordEntries=Library.readWordEntries(getWordsFile());
			if(null==wordEntries)
			{
				throw new IOException();
			}
			
			//��ʼ�ҽ���
			Library library=Library.createLibrary(this);
			
			for(int i=0;i<wordEntries.size();i++)
			{
				if(wordEntries.get(i)!=null)
				{
					output+=("|"+wordEntries.get(i).getWord()+"|"+"\t");
					output+="|"+(library.getTransEntry(wordEntries.get(i)))+"|"+"\n";
				}
			}*/
			
		    //����
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
