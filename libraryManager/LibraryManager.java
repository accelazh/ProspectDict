package libraryManager;

import libraryInterface.*;
import settings.*;

import java.io.File;
import java.util.*;

/**
 * 
 * @author ZYL
 * 这个类可以接收用户向GUI输入的信息，并处理得到应传给GUI的信息，
 * 无论GUI的具体实现如何，这个类都为GUI提供需要的信息。
 * 用这个类的设计把图形显示与内部数据的运作分离处理。 
 * 
 * 新功能，利用网络功能实现超找句子的功能
 *
 * 使用时，要先是用refreshTypeInWord方法刷新，然后获得单词的解
 * 释或其他相关信息
 * 
 * 一个LibraryManager的对象对应一个Library词库,LibraryManager
 * 这个类的对象实际上是LibraryManagerInstance类的实例，Library-
 * Manager提供工厂方法createLibraryManager()，以构造实例
 */
public abstract class LibraryManager implements Constants /*, Comparable<LibraryManager>*/
{
	/**
	 * 这个量用来在refreshTypeInWord后标识是否查到词了，
	 * 若没有查到，pointedIndex会为-1
	 */
 	private boolean findWord=false;
	/**
 	 * library.wordEntries中被找到的词的index
 	 */
 	private int pointedIndex=-1;
 	private String wordToSearch="";
    /**
     * 绑定的词典
     */
 	private Library library;
 	/**
 	 * 为了使用二分法查找，将wordEntries转为array存储
 	 */
 	private WordEntry[] wordEntryArray;
 	/**
 	 * 这个量会根据所绑定的library被初始化，标识这个
 	 * LibraryManager的对象是否支持网络查找
 	 */
 	private boolean netSearchSupported=false;
 	/**
 	 * 这个量指明，from==DEFAULT_LIBRARY_FROM[supportIndex],
 	 * to==DEFAULT_LIBRARY_TO[supportIndex]的网络查找被支持。
 	 * (为实现网络查找，需要知道这个词库和哪个网络查找相配套。)
 	 */
 	private int supportIndex=-1;
 	/**
 	 * 这个量用来表示这个词库是否是活跃的，在conLibraryManager高层的功能中有着应用
 	 */
 	private boolean active=false;
 	/**
 	 * 这个量用来记录要查的单词是否在自己的字符集中，在conLibraryManager中有应用
 	 */
 	private boolean inDomain=false;
 	/**
 	 * 这个方法是静态的工厂方法，用来构造绑定于某个词库的LibraryManager,
 	 * 构造过程中，若出现错误，则返回null
 	 * 
 	 * @param libraryAccount
 	 * @return
 	 */
 	public static LibraryManager createLibraryManager(LibraryAccount libraryAccount)
 	{
 		//新建LibraryManagerInstance
    	LibraryManagerInstance libM=new LibraryManagerInstance();
    	System.out.println("libraryInstance created");
    	
    	//初始化library
    	libM.setLibrary(Library.createLibrary(libraryAccount));
 	    if(null==libM.getLibrary())
 	    {
 	    	System.out.println("Error in createLibraryManager, failed when creating library");
 	        return null;
 	    }
 	    System.out.println("library initialized");
 	   
 	    //初始化netSearchSupported
 	    for(int i=0;i<DEFAULT_LIBRARY_FROM.length;i++)
 	    {
 	    	if(libM.getLibrary().getLibraryAccount().getFrom().equals(DEFAULT_LIBRARY_FROM[i])
 	    			&&libM.getLibrary().getLibraryAccount().getTo().equals(DEFAULT_LIBRARY_TO[i]))
 	    	{
 	    		libM.setNetSearchSupported(true);
 	    		libM.setSupportIndex(i);
 	    		break;
 	    	}
 	    }
 	   System.out.println("net work support checked, "+"support: "+libM.isNetSearchSupported());
 	    
 	    //生成wordEntryArray
 	    libM.setWordEntryArray(libM.getLibrary().getWordEntries());
 	    if(null==libM.getWordEntryArray())
 	    {
 	    	System.out.println("Error in createLibraryManager, failure when creating wordEntryArray");
 	        return null;
 	    }
 	   System.out.println("wordEntryArray created");
 	    
 	    return libM;
 	}

	public Library getLibrary() {
		return library;
	}

	protected void setLibrary(Library library) {
		this.library = library;
	}

	public boolean isNetSearchSupported() {
		return netSearchSupported;
	}

	protected void setNetSearchSupported(boolean netSearchSupported) {
		this.netSearchSupported = netSearchSupported;
	}

	public int getPointedIndex() {
		return pointedIndex;
	}

	public boolean isFindWord() {
		return findWord;
	}

	public int getSupportIndex() {
		return supportIndex;
	}

	protected void setSupportIndex(int supportIndex) {
		this.supportIndex = supportIndex;
	}
	
	public WordEntry[] getWordEntryArray() {
		return wordEntryArray;
	}

	protected void setWordEntryArray(WordEntry[] wordEntryArray) {
		this.wordEntryArray = wordEntryArray;
	}
	
	/**
	 * 这个词用来刷新单词指针pointedIndex,以便进一步获取信息
	 * @param wordEntry
	 */
	public void refreshTypeInWord(WordEntry wordEntry)
	{
		//合法性检查
		if(null==wordEntry)
		{
			return;
		}
		
		//更新pointedIndex
		pointedIndex=Arrays.binarySearch(wordEntryArray,wordEntry);
		
		//更新wordToSearch
		wordToSearch=wordEntry.getWord();
		
		//更新findWord
		findWord=pointedIndex<0?false:true;
	}

	/**
	 * 这个词用来刷新单词指针pointedIndex,以便进一步获取信息
	 * @param aimWord
	 */
	public void refreshTypeInWord(String aimWord)
	{
		//合法性检查
		if(null==aimWord)
		{
			return;
		}
		
		aimWord=aimWord.trim();
		if(aimWord.length()==0)
		{
			return;
		}
		
		refreshTypeInWord(new WordEntry(aimWord,-1));
	}
	
	/**
	 * 这个方法在refreshTypeInWord()后，如果找到了单词，
	 * 则可以返回解释，否则返回null;
	 * 
	 */
	public TransEntry findTransEntry()
	{
		if(!findWord)
		{
			return null;
		}
		
		return formatTransEntry(library.getTransEntry(library.getWordEntries()[pointedIndex]));
	}
	
	/**
	 * 这个方法找到单词的开头部分是pointedIndex指向的单词。
	 * 例如，pointedIndex指向单词"a",
	 * 则返回的单词为"aa","ab","ac",...,"aaa","aab",...
	 * 注意有可能返回长度为零的数组
	 * 
	 * 这个方法不配合refreshTypeInWord工作
	 *  
	 * 
	 */
	public WordEntry[] findSameRootWordEntries(String aimWord)
	{
		if(null==aimWord)
		{
			return null;
		}
		
		int insertIndex=Arrays.binarySearch(wordEntryArray,new WordEntry(aimWord,-1));
		
		//将insertIndex调整到要开始查找的位置
		if(insertIndex<0)
		{
			insertIndex=-(insertIndex+1);
		}
		
		//生成返回值
		int endIndex=Math.min(wordEntryArray.length, insertIndex+MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED);
		WordEntry[] returnVal=new WordEntry[endIndex-insertIndex];
		
		for(int i=0;i<returnVal.length;i++)
		{
			returnVal[i]=wordEntryArray[insertIndex+i];
		}
		
		return returnVal;
	}

	/**
	 * 这个方法能与它的重载方法相同，但是配合refreshTypeInWord
	 * 工作，因此使用前一定要先执行refreshTypeInWord()方法
	 * 
	 */
	public WordEntry[] findSameRootWordEntries()
	{
		int insertIndex=pointedIndex;
		
		if(!findWord)
		{
			insertIndex=-(insertIndex+1);
		}
		
		//生成返回值
		int endIndex=Math.min(wordEntryArray.length, insertIndex+MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED);
		WordEntry[] returnVal=new WordEntry[endIndex-insertIndex];
		
		for(int i=0;i<returnVal.length;i++)
		{
			returnVal[i]=wordEntryArray[insertIndex+i];
		}
		
		return returnVal;
	}
	
	/**
	 * 查找形近词,若传入参数为null则返回null，
	 * 返回值有可能size()==0
	 * 
	 */
	public WordEntry[] findResembleWordEntries(String aimWord)
	{
		if(null==aimWord)
		{
			return null;
		}
		
		//得到要用到的量
		LinkedList<WordEntry> resembleWordEntries=new LinkedList<WordEntry>(); 
		
		Character[] wordCharsWrapped=library.getLibraryAccount().getWordChars().toArray(new Character[0]);
		char[] wordChars=new char[wordCharsWrapped.length];
		for(int i=0;i<wordChars.length;i++)
		{
			wordChars[i]=wordCharsWrapped[i].charValue();
		}
		wordCharsWrapped=null;
		
		//=开始查找=
		StringBuffer buffer=new StringBuffer(aimWord);
		
		//查找替换字母的词
		for(int i=0;i<buffer.length();i++)
		{
			char origChar=buffer.charAt(i);
			for(int j=0;j<wordChars.length;j++)
			{
				if(wordChars[j]==origChar)
				{
					continue;
				}
				
				//替换并查找
				buffer.setCharAt(i,wordChars[j]);
				findAndAdd(resembleWordEntries, buffer.toString());
				buffer.setCharAt(i,origChar);
			}
		}
		
		//查找删除字母得到的词
		for(int i=0;i<buffer.length();i++)
		{
			char origChar=buffer.charAt(i);
			
			buffer.deleteCharAt(i);
			findAndAdd(resembleWordEntries, buffer.toString());
			
			buffer.insert(i,origChar);
		}
		
		//查找增加字母的词
		for(int i=0;i<=buffer.length();i++)
		{
			for(int j=0;j<wordChars.length;j++)
			{
				//插入并查找
				buffer.insert(i,wordChars[j]);
				findAndAdd(resembleWordEntries, buffer.toString());
				buffer.deleteCharAt(i);
			}
		}
		
		return resembleWordEntries.toArray(new WordEntry[0]);
	}

	/**
	 * 查找形近词,若传入参数为null则返回null，
	 * 返回值有可能size()==0
	 * 
	 * 这个方法使用之前必须先调用refreshTypeInWord()
	 * @return
	 */
	public WordEntry[] findResembleWordEntries()
	{
		return findResembleWordEntries(wordToSearch);
	}

	//这个方法帮助findResembleWordEntries完成任务
	private void findAndAdd(LinkedList<WordEntry> resembleWordEntries, String word)
	{
		int index=-1;
		if((index=Arrays.binarySearch(wordEntryArray, new WordEntry(word,-1)))>=0)
		{
			resembleWordEntries.add(wordEntryArray[index]);
		}
	}
	
	/**
	 * 这个方法返回网查结果，如果这个词库不支持网络查找，那么
	 * 将返回null
	 *  
	 */
	public String findTransOnLine(String aimWord)
	{
		if(null==aimWord)
		{
			return null;
		}
		
		if(netSearchSupported)
		{
			return SearchingEngineOnLine.translate(aimWord, supportIndex);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 这个方法返回网查结果，如果这个词库不支持网络查找，那么
	 * 将返回null
	 * 这个方法使用之前必须先调用refreshTypeInWord()
	 *  
	 */
	public String findTransOnLine()
	{
		return findTransOnLine(wordToSearch);
	}

	public String getWordToSearch() {
		return wordToSearch;
	}

	/**
	 * 这个方法为LibraryManager增加建立libraryFile的功能，
	 * 这个功能实际上是由Library实现的
	 * 
	 * @param rawLibraryFile
	 * @param separatorOfWordAndTrans
	 * @param separatorOfEntryAndEntry
	 * @param from
	 * @param to
	 * @param dictName
	 * @return
	 */
	public static LibraryAccount createLibraryFile(File rawLibraryFile, 
			char separatorOfWordAndTrans, char separatorOfEntryAndEntry,
			String from, String to, String dictName)
	{
		return Library.createLibraryFile(rawLibraryFile, separatorOfWordAndTrans, separatorOfEntryAndEntry, from, to, dictName);
	}
	
	/**
	 * 这个方法返回LibraryAccountor,其功能实际上是由Library实现的
	 * 
	 */
	public static LibraryAccountor getLibraryAccountor()
	{
		return Library.getLibraryAccountor();
	}
	
	/**
	 * 这个方法用来处理TransEntry,因为翻译的文本经常被写作<...>...   <...>...
	 * 的形式，故而这里再作整理，使其更像词典的解释
	 */
	public static TransEntry formatTransEntry(TransEntry transEntry)
	{
		if(null==transEntry)
		{
			return null;
		}
		
		String trans=transEntry.getTrans().trim();
		
		if(null==trans||0==trans.length())
		{
			return null;
		}
		
		//开始整理
		trans=trans.trim();
		StringTokenizer tokens=new StringTokenizer(trans,"||");
		StringBuffer buffer=new StringBuffer();
		while(tokens.hasMoreTokens())
		{
			StringBuffer tempBuffer=new StringBuffer(tokens.nextToken().trim());
			for(int i=0;i<tempBuffer.length();i++)
			{
				if(tempBuffer.charAt(i)=='>')
				{
					tempBuffer.insert(i+1, "\n    ");
					break;
				}
			}
			tempBuffer.append('\n');
			
			buffer.append(tempBuffer);
		}
		
		//整理完毕
		return new TransEntry(buffer.toString());
	}
	
	/**
     * 删除词库文件的方法
     */
	public static boolean deleteLibraryFile(LibraryAccount libraryAccount)
	{
		return Library.deleteLibraryFile(libraryAccount);
	}

	/**
	 * 这个方法判断某个单词是否是在该词库中可能查到的
	 */
	public boolean findIsInDomainance(String word)
	{
		if(null==word||0==word.length())
		{
			return false;
		}
		
		char minCh=getLibrary().getLibraryAccount().getMinChar().charValue();
		char maxCh=getLibrary().getLibraryAccount().getMaxChar().charValue();
		
		StringBuffer wordBuffer=new StringBuffer(word);
		for(int i=0;i<wordBuffer.length();i++)
		{
			if((wordBuffer.charAt(i)==' ')
				||(wordBuffer.charAt(i)=='.')
				||(wordBuffer.charAt(i)=='-')
				||(wordBuffer.charAt(i)=='_')
				||(wordBuffer.charAt(i)=='，')
				||(wordBuffer.charAt(i)=='。')
				||(wordBuffer.charAt(i)==' '))
			{
				wordBuffer.deleteCharAt(i);
				i--;
			}
		}
		word=wordBuffer.toString();
		
		for(int i=0;i<word.length();i++)
		{	
			if(word.charAt(i)<minCh||word.charAt(i)>maxCh)
			{
				return false;
			}
		}
		
		Set<Character> wordChars=getLibrary().getLibraryAccount().getWordChars();
		
		for(int i=0;i<word.length();i++)
		{
			if(!wordChars.contains(word.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/*public int compareTo(LibraryManager e)
	{
		if(null==e)
		{
			System.out.println("Error when compare libraryManager");
		    return -1;
		}
		String str1=getLibrary().getLibraryAccount().getFrom()+getLibrary().getLibraryAccount().getTo();
		String str2=e.getLibrary().getLibraryAccount().getFrom()+e.getLibrary().getLibraryAccount().getTo();
		return str1.compareTo(str2);
	}*/
	
	public boolean equals(Object o)
	{
		LibraryManager e=(LibraryManager)o;
		return getLibrary().getLibraryAccount().equals(e.getLibrary().getLibraryAccount());
	}
	
	public String toString()
	{
		String output="";
		output+="LibraryManager: "+"\n";
		output+="From: "+library.getLibraryAccount().getFrom()+"\n";;
		output+="To: "+library.getLibraryAccount().getTo()+"\n";;
		output+="Name: "+library.getLibraryAccount().getName()+"\n";
		output+="active: "+active+"\n";
		output+="inDoman: "+inDomain+"\n";
		return output;
	}

	
	public boolean isActive() {
		return active;
	}

	
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isInDomain() {
		return inDomain;
	}

	public void setInDomain(boolean inDomain) {
		this.inDomain = inDomain;
	}
	
	
}
