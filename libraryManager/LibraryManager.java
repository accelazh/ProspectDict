package libraryManager;

import libraryInterface.*;
import settings.*;

import java.io.File;
import java.util.*;

/**
 * 
 * @author ZYL
 * �������Խ����û���GUI�������Ϣ��������õ�Ӧ����GUI����Ϣ��
 * ����GUI�ľ���ʵ����Σ�����඼ΪGUI�ṩ��Ҫ����Ϣ��
 * ����������ư�ͼ����ʾ���ڲ����ݵ��������봦�� 
 * 
 * �¹��ܣ��������繦��ʵ�ֳ��Ҿ��ӵĹ���
 *
 * ʹ��ʱ��Ҫ������refreshTypeInWord����ˢ�£�Ȼ���õ��ʵĽ�
 * �ͻ����������Ϣ
 * 
 * һ��LibraryManager�Ķ����Ӧһ��Library�ʿ�,LibraryManager
 * �����Ķ���ʵ������LibraryManagerInstance���ʵ����Library-
 * Manager�ṩ��������createLibraryManager()���Թ���ʵ��
 */
public abstract class LibraryManager implements Constants /*, Comparable<LibraryManager>*/
{
	/**
	 * �����������refreshTypeInWord���ʶ�Ƿ�鵽���ˣ�
	 * ��û�в鵽��pointedIndex��Ϊ-1
	 */
 	private boolean findWord=false;
	/**
 	 * library.wordEntries�б��ҵ��Ĵʵ�index
 	 */
 	private int pointedIndex=-1;
 	private String wordToSearch="";
    /**
     * �󶨵Ĵʵ�
     */
 	private Library library;
 	/**
 	 * Ϊ��ʹ�ö��ַ����ң���wordEntriesתΪarray�洢
 	 */
 	private WordEntry[] wordEntryArray;
 	/**
 	 * �������������󶨵�library����ʼ������ʶ���
 	 * LibraryManager�Ķ����Ƿ�֧���������
 	 */
 	private boolean netSearchSupported=false;
 	/**
 	 * �����ָ����from==DEFAULT_LIBRARY_FROM[supportIndex],
 	 * to==DEFAULT_LIBRARY_TO[supportIndex]��������ұ�֧�֡�
 	 * (Ϊʵ��������ң���Ҫ֪������ʿ���ĸ�������������ס�)
 	 */
 	private int supportIndex=-1;
 	/**
 	 * �����������ʾ����ʿ��Ƿ��ǻ�Ծ�ģ���conLibraryManager�߲�Ĺ���������Ӧ��
 	 */
 	private boolean active=false;
 	/**
 	 * �����������¼Ҫ��ĵ����Ƿ����Լ����ַ����У���conLibraryManager����Ӧ��
 	 */
 	private boolean inDomain=false;
 	/**
 	 * ��������Ǿ�̬�Ĺ��������������������ĳ���ʿ��LibraryManager,
 	 * ��������У������ִ����򷵻�null
 	 * 
 	 * @param libraryAccount
 	 * @return
 	 */
 	public static LibraryManager createLibraryManager(LibraryAccount libraryAccount)
 	{
 		//�½�LibraryManagerInstance
    	LibraryManagerInstance libM=new LibraryManagerInstance();
    	System.out.println("libraryInstance created");
    	
    	//��ʼ��library
    	libM.setLibrary(Library.createLibrary(libraryAccount));
 	    if(null==libM.getLibrary())
 	    {
 	    	System.out.println("Error in createLibraryManager, failed when creating library");
 	        return null;
 	    }
 	    System.out.println("library initialized");
 	   
 	    //��ʼ��netSearchSupported
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
 	    
 	    //����wordEntryArray
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
	 * ���������ˢ�µ���ָ��pointedIndex,�Ա��һ����ȡ��Ϣ
	 * @param wordEntry
	 */
	public void refreshTypeInWord(WordEntry wordEntry)
	{
		//�Ϸ��Լ��
		if(null==wordEntry)
		{
			return;
		}
		
		//����pointedIndex
		pointedIndex=Arrays.binarySearch(wordEntryArray,wordEntry);
		
		//����wordToSearch
		wordToSearch=wordEntry.getWord();
		
		//����findWord
		findWord=pointedIndex<0?false:true;
	}

	/**
	 * ���������ˢ�µ���ָ��pointedIndex,�Ա��һ����ȡ��Ϣ
	 * @param aimWord
	 */
	public void refreshTypeInWord(String aimWord)
	{
		//�Ϸ��Լ��
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
	 * ���������refreshTypeInWord()������ҵ��˵��ʣ�
	 * ����Է��ؽ��ͣ����򷵻�null;
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
	 * ��������ҵ����ʵĿ�ͷ������pointedIndexָ��ĵ��ʡ�
	 * ���磬pointedIndexָ�򵥴�"a",
	 * �򷵻صĵ���Ϊ"aa","ab","ac",...,"aaa","aab",...
	 * ע���п��ܷ��س���Ϊ�������
	 * 
	 * ������������refreshTypeInWord����
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
		
		//��insertIndex������Ҫ��ʼ���ҵ�λ��
		if(insertIndex<0)
		{
			insertIndex=-(insertIndex+1);
		}
		
		//���ɷ���ֵ
		int endIndex=Math.min(wordEntryArray.length, insertIndex+MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED);
		WordEntry[] returnVal=new WordEntry[endIndex-insertIndex];
		
		for(int i=0;i<returnVal.length;i++)
		{
			returnVal[i]=wordEntryArray[insertIndex+i];
		}
		
		return returnVal;
	}

	/**
	 * ������������������ط�����ͬ���������refreshTypeInWord
	 * ���������ʹ��ǰһ��Ҫ��ִ��refreshTypeInWord()����
	 * 
	 */
	public WordEntry[] findSameRootWordEntries()
	{
		int insertIndex=pointedIndex;
		
		if(!findWord)
		{
			insertIndex=-(insertIndex+1);
		}
		
		//���ɷ���ֵ
		int endIndex=Math.min(wordEntryArray.length, insertIndex+MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED);
		WordEntry[] returnVal=new WordEntry[endIndex-insertIndex];
		
		for(int i=0;i<returnVal.length;i++)
		{
			returnVal[i]=wordEntryArray[insertIndex+i];
		}
		
		return returnVal;
	}
	
	/**
	 * �����ν���,���������Ϊnull�򷵻�null��
	 * ����ֵ�п���size()==0
	 * 
	 */
	public WordEntry[] findResembleWordEntries(String aimWord)
	{
		if(null==aimWord)
		{
			return null;
		}
		
		//�õ�Ҫ�õ�����
		LinkedList<WordEntry> resembleWordEntries=new LinkedList<WordEntry>(); 
		
		Character[] wordCharsWrapped=library.getLibraryAccount().getWordChars().toArray(new Character[0]);
		char[] wordChars=new char[wordCharsWrapped.length];
		for(int i=0;i<wordChars.length;i++)
		{
			wordChars[i]=wordCharsWrapped[i].charValue();
		}
		wordCharsWrapped=null;
		
		//=��ʼ����=
		StringBuffer buffer=new StringBuffer(aimWord);
		
		//�����滻��ĸ�Ĵ�
		for(int i=0;i<buffer.length();i++)
		{
			char origChar=buffer.charAt(i);
			for(int j=0;j<wordChars.length;j++)
			{
				if(wordChars[j]==origChar)
				{
					continue;
				}
				
				//�滻������
				buffer.setCharAt(i,wordChars[j]);
				findAndAdd(resembleWordEntries, buffer.toString());
				buffer.setCharAt(i,origChar);
			}
		}
		
		//����ɾ����ĸ�õ��Ĵ�
		for(int i=0;i<buffer.length();i++)
		{
			char origChar=buffer.charAt(i);
			
			buffer.deleteCharAt(i);
			findAndAdd(resembleWordEntries, buffer.toString());
			
			buffer.insert(i,origChar);
		}
		
		//����������ĸ�Ĵ�
		for(int i=0;i<=buffer.length();i++)
		{
			for(int j=0;j<wordChars.length;j++)
			{
				//���벢����
				buffer.insert(i,wordChars[j]);
				findAndAdd(resembleWordEntries, buffer.toString());
				buffer.deleteCharAt(i);
			}
		}
		
		return resembleWordEntries.toArray(new WordEntry[0]);
	}

	/**
	 * �����ν���,���������Ϊnull�򷵻�null��
	 * ����ֵ�п���size()==0
	 * 
	 * �������ʹ��֮ǰ�����ȵ���refreshTypeInWord()
	 * @return
	 */
	public WordEntry[] findResembleWordEntries()
	{
		return findResembleWordEntries(wordToSearch);
	}

	//�����������findResembleWordEntries�������
	private void findAndAdd(LinkedList<WordEntry> resembleWordEntries, String word)
	{
		int index=-1;
		if((index=Arrays.binarySearch(wordEntryArray, new WordEntry(word,-1)))>=0)
		{
			resembleWordEntries.add(wordEntryArray[index]);
		}
	}
	
	/**
	 * ��������������������������ʿⲻ֧��������ң���ô
	 * ������null
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
	 * ��������������������������ʿⲻ֧��������ң���ô
	 * ������null
	 * �������ʹ��֮ǰ�����ȵ���refreshTypeInWord()
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
	 * �������ΪLibraryManager���ӽ���libraryFile�Ĺ��ܣ�
	 * �������ʵ��������Libraryʵ�ֵ�
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
	 * �����������LibraryAccountor,�书��ʵ��������Libraryʵ�ֵ�
	 * 
	 */
	public static LibraryAccountor getLibraryAccountor()
	{
		return Library.getLibraryAccountor();
	}
	
	/**
	 * ���������������TransEntry,��Ϊ������ı�������д��<...>...   <...>...
	 * ����ʽ���ʶ�������������ʹ�����ʵ�Ľ���
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
		
		//��ʼ����
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
		
		//�������
		return new TransEntry(buffer.toString());
	}
	
	/**
     * ɾ���ʿ��ļ��ķ���
     */
	public static boolean deleteLibraryFile(LibraryAccount libraryAccount)
	{
		return Library.deleteLibraryFile(libraryAccount);
	}

	/**
	 * ��������ж�ĳ�������Ƿ����ڸôʿ��п��ܲ鵽��
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
				||(wordBuffer.charAt(i)=='��')
				||(wordBuffer.charAt(i)=='��')
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
