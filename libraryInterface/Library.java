package libraryInterface;

import java.io.*;
import java.util.*;



/**
 * 
 * @author ZYL
 * 这个类提供各种不同格式书写的词库的一个抽象，
 * 负责创建针对不同的词库library的实例，解决
 * 程序与文件的第一层通信问题，以及对于任意字
 * 典的解析和词库创建、合并
 * 
 * 有一些量（progress开头的）是用来监控运行
 * 时间较长的方法的进度的，为了正确使用，应该
 * 在使用它们前，即开始那些运行时间较长的方法
 * 之前，调用resetProcessValues()方法将他
 * 们清零，以确保正确
 */
public abstract class Library 
{
	//for debug
	private static final boolean debug=true;
	
	/**
	 * 词库文件的统一的分隔符 
	 */
	public static final char SEPARATOR=' ';
	/**
	 * 装有library文件的文件夹
	 */
    public static final File LIBRARY_SOURCE=new File("libraries");
    /**
     * 临时文件
     */
    public static final File TEMP_FILE=new File("libraries/temporary.txt");
    /**
     * 临时文件夹
     */
    public static final File TEMP_DIR=new File("libraries/temp");
    /**
     * 备份文件夹
     */
    public static final File BACKUP_DIR=new File("libraries/backup");
    /**
     * 见LibraryAccountor的类说明
     */
    private static LibraryAccountor libraryAccountor;
     
    static
    {
    	//初始化libraryAccountor
    	libraryAccountor=new LibraryAccountor();
    	
    }
    /**
     * 用来表示词库解析及createLibrary的进度的量，值0~100
     */
    private static double progressOfReadWordEntry;
    /**
     * 用来表示词库解析的进度的量，值0~100
     */
    private static double progressOfRawLibSplit;
    /**
     * 用来表示词库解析的进度的量，值0~100
     */
    private static double progressOfInsert;
    /**
     * 用来表示词库解析y的进度的量，值0~100
     */
    private static double progressOfUnifyLength;
    /**
     * 用来表示词库解析的进度的量，值0~100
     */
    private static double progressOfWriteWordEntry;
    /**
     * 用来表明copyAllLibraryFiles的进度
     */
    private static double progressOfCopyLibraryFiles;
    /**
     * 用来表明switchLibraryFilesInBackupAndCurrent的进度, 因具体实现的原因，
     * 这个进度只能表明状态0，1，2；
     * 实际上的进度应该这样计算：
     * progressOfSwitchLibraryFiles*1.0/3+1.0/3*progressOfCopyLibraryFiles; 
     */
    private static int progressOfSwitchLibraryFiles;
    
    //===========================================
    /**
     * 这个数据域是给LibraryInstance用的，记录单词头列表
     */
    private WordEntry[] wordEntries;
    /**
     * 这个数据域是给LbraryInstance用的，记录具体词库的信息
     */
    private LibraryAccount libraryAccount;
    /**
     * 这个专门负责读词库，以提高速度
     */    
    private MyRandomAccessFile libReader;
   
    //============================================  
    
    /**
     * 这个方法用libraryAccoun指明创建哪个词库的libraryInstance，
     * 从而创建一个绑定于特定词库的Library对象,如果传入值非法，则返回
     * null
     * 
     */
    public static LibraryInstance createLibrary(LibraryAccount libraryAccount)
    {
    	//合法性检查
    	if(null==libraryAccount
    			||null==libraryAccountor
    			||libraryAccountor.isLibraryAccountsNull()
    	        ||(!libraryAccountor.getLibraryAccounts().contains(libraryAccount))
    	        ||(!libraryAccount.isLibraryAccountValid()))    //这条检查速度太慢，先省去
    	{
    		System.out.println("Error in createLibrary, invalid libraryAccount argument");
    		return null;
    	}
    	
    	//初始化进度指示纪录
    	progressOfReadWordEntry=0;
    	
    	//==创建libraryInstance==
    	LibraryInstance lib=new LibraryInstance();
    	
    	//初始化libraryAccount
    	lib.setLibraryAccount(libraryAccount);
    	
    	//初始化wordEntries
    	ArrayList<WordEntry> wordEntryList=readWordEntries(libraryAccount.getWordsFile());
    	if(null==wordEntryList)
    	{
    		System.out.println("Error in createLibrary, failed to create wordEntries");
    		return null;
    	}
    	lib.setWordEntries(wordEntryList.toArray(new WordEntry[0]));
    	    	
    	//建立读写指针
    	try
    	{
    	    lib.setLibReader(new MyRandomAccessFile(lib.getLibraryAccount().getTransFile(),"r"));
    	}
    	catch(FileNotFoundException ex)
    	{
    		System.out.println("Error in createLibrary, failed to create libReader");
    		return null;
    	}
    	
    	return lib;
    }
   
    public static LibraryAccountor getLibraryAccountor()
    {
    	return libraryAccountor;
    }
    
    //============下面是给libraryInstance用的方法============
    
	public LibraryAccount getLibraryAccount() {
		return libraryAccount;
	}

	protected void setLibraryAccount(LibraryAccount libraryAccount) {
		this.libraryAccount = libraryAccount;
	}

	public WordEntry[] getWordEntries() {
		return wordEntries;
	}

	protected void setWordEntries(WordEntry[] wordEntries) {
		this.wordEntries = wordEntries;
	}
	
	protected MyRandomAccessFile getLibReader() {
		return libReader;
	}

	protected void setLibReader(MyRandomAccessFile libReader) {
		this.libReader = libReader;
	}
	
	/**
	 * 这个方法根据WordEntry放回相应的解释,
	 * 当出现问题时返回null
	 */
	public TransEntry getTransEntry(WordEntry we)
	{
		if(null==we)
		{
			return null;
		}
		
		int index=Arrays.binarySearch(wordEntries, we);

		if(index<0)
		{
			return null;
		}
		
		//开始寻找解释
		try
		{
		    libReader.seek(we.getIndexOfTrans()*(2*(libraryAccount.getLengthOfLineInTransFile()+1)));
		        
		    return new TransEntry(libReader.readUnicodeLine());
		}
		catch(UnsupportedOperationException ex)
		{
			System.out.println("Error in getTransEntry, UnsupportedOperationException");
			ex.printStackTrace();
			return null;
		}
		catch(EOFException ex)
		{
			System.out.println("Error in getTransEntry, EOFException");
			ex.printStackTrace();
			return null;
		}
		catch(IOException ex)
		{
			System.out.println("Error in getTransEntry, IOException");
			ex.printStackTrace();
			return null;
		}
	}
	
	
	//================下面是实现词库解析功能的部分=================
	
	/**
	 * 这个方法负责将传入的词库文件解析，在该软件的相应目录下建立经过解析的字典词库
	 * 或者整合词库。
	 * 这里假定待解析的词库中，一个个词条由行间隔符分开；一个词条由一个单词和一个解释
	 * 组成，中间由单词和解释的分隔符隔开；行间个符与单词和解释的间隔符不同；
	 * 词库中允许插入一些不是词条的文本。
	 * 注意，如果两个词库的from,to,和dictName都相同，则将会被认为相同并进行合并
	 * 
	 * @param rawLibraryFile 将要解析的词库文件（比如说刚从网上下载的词库）
	 * @param separatorOfWordAndTrans 将要解析的词库文件中，单词和解释之间的分隔符
	 * @param separatorOfEntryAndEntry 将要解析的词库文件中，词条与词条之间的分隔符
	 * @param from 这个词库是将什么翻译成什么的
	 * @param to 这个词库是将什么翻译成什么的
	 * @param dictName 这个是字典词库的名字，如果填null或“”的话，将自动设为"Default",
	 *        这个名字将被添加在单词的解释的前面。
	 * @return 如果新建成功，则返回这个词库的LibraryAccount, 如果出现问题则返回null
	 */
	public static LibraryAccount createLibraryFile(File rawLibraryFile, 
			char separatorOfWordAndTrans, char separatorOfEntryAndEntry,
			String from, String to, String dictName)
	{
		//检查输入的合法性
		if(null==rawLibraryFile
				||!rawLibraryFile.exists()
				||!(separatorOfWordAndTrans>0&&separatorOfWordAndTrans<=127)
				||!(separatorOfEntryAndEntry>0&&separatorOfEntryAndEntry<=127)
				||null==from
				||null==to)
		{
			System.out.println("Error in createLibraryFile, invalid arguments");
			return null;
		}
		
		if(separatorOfWordAndTrans==separatorOfEntryAndEntry)
		{
			System.out.println("Error in createLibraryFile, invalid arguments");
			return null;
		}
		
		//重置进度纪录
		progressOfReadWordEntry=0;
		progressOfRawLibSplit=0;
		progressOfInsert=0;
		progressOfUnifyLength=0;
		progressOfWriteWordEntry=0;
		
		//净化dictName
		dictName=clarifyName(dictName);
		if(null==dictName)
		{
			dictName="Default";
		}
		
		//净化from和to
		from=clarifyName(from);
		to=clarifyName(to);
		
		if(null==from||null==to)
		{
			System.out.println("Error in createLibraryFile, invalid arguments");
			return null;
		}
		
		//先进行备份
		backupLibraryFiles();
		progressOfCopyLibraryFiles=1;
		
		//开始正文 
		File wordFile=new File("libraries\\"+dictName+"_from"+from+"To"+to+"Word.dic");
		File transFile=new File("libraries\\"+dictName+"_from"+from+"To"+to+"Trans.dic");
	
		ArrayList<WordEntry> wordEntries=null;
		MyRandomAccessFile outTrans;
		long totalNumOfEntries=0;
		int maxLineLengthOfTrans=0;  //transFile中每行的最大长度，统一后便于移动文件指针,长度是实际文件中的字节长度
		
		//检查旧词库是否存在（即新旧词库的from和to相同）,并作初始设定
		if(libraryAccountor.getLibraryAccounts().contains(new LibraryAccount(from,to,dictName))
				&&(wordFile.exists())&&(transFile.exists()))
		{
			System.out.println("old library file already exists, now rewrite");
			
			try
			{
				//读入wordEntries
			    wordEntries=readWordEntries(wordFile);
		        
			    if(null==wordEntries)
			    {
			    	System.out.println("Error in creatingLibraryFile, failed to readWordEntries");
			        return null;
			    }
			    
		        //计算totalNumOfEntries
		        MyRandomAccessFile inForNum=new MyRandomAccessFile(transFile,"rw");
		        while(inForNum.readUnicodeLine()!=null)
		        {
		        	totalNumOfEntries++;
		        }
		        
		        //生成outTrans，其指向文件尾
		        outTrans=inForNum;
	
			}
			catch(IOException ex)
			{
				System.out.println("Error in createLibraryFile, IOException");
				ex.printStackTrace();
				return null;
			}
		}
		else
		{
			wordEntries=new ArrayList<WordEntry>();
			try
			{
			    outTrans=new MyRandomAccessFile(transFile,"rw");
			}
			catch(IOException ex)
			{
				System.out.println("Error in createLibraryFile, IOException");
				ex.printStackTrace();
				return null;
			}
			progressOfReadWordEntry=1;
		}
		
		
		// 统计rawLibraryFile中的总条数
		progressOfRawLibSplit = 0;

		long totalNumOfEntriesInRawLib = 0;
		long currentNumOfEntriesInRawLib = 0;
		try {
			BufferedReader inRawLibForNum = new BufferedReader(new FileReader(
					rawLibraryFile));
			while (readLineBy(separatorOfEntryAndEntry, inRawLibForNum) != null) {
				totalNumOfEntriesInRawLib++;
			}
			inRawLibForNum.close();
		} catch (IOException ex) {
			System.out.println("Error in createLibraryFile, IOException");
			ex.printStackTrace();
			return null;
		}
		
		//开始解析新词库
		List<TransEntryForInsert> toBeInsertedTransEntries=new LinkedList<TransEntryForInsert>();  //因为新词和旧词可能重复，此时就要把它们的解释合并起来（两个留着），即插入。这个变量纪录插入的解释和其应该插入的index	
		try
		{
		    BufferedReader inRawLib=new BufferedReader(new FileReader(rawLibraryFile));
		    String temp=null;
			while((temp=readLineBy(separatorOfEntryAndEntry,inRawLib))!=null)
			{
				if(isLegalRawEntry(temp,separatorOfWordAndTrans))
				{
					//生成经过整理的单词String和解释String
					temp=temp.trim();
					StringTokenizer tokens=new StringTokenizer(temp,""+separatorOfWordAndTrans);
					
					String word=tokens.nextToken();
			    	String trans="";
			    	while(tokens.hasMoreTokens())
			    	{
			    	    trans+=tokens.nextToken();	
			    	}
			    	
			    	word=word.replaceAll("\n"," ");  //将words中的换行符都去掉，因为换行是词库文件中有重要功能的字符
			    	word=word.replaceAll(""+(char)13, " ");
			    	word=word.trim();
			    	trans=trans.replaceAll("\n", " ");  //将trans中的换行符都去掉，因为换行是词库文件中有重要功能的字符
			    	trans=trans.replaceAll(""+(char)13, " ");
			    	trans=trans.trim();  
			    	
			    	//检查word和以前是否重复
			    	boolean repeated=false;
			    	int repeatIndex=-1;
			    	if((repeatIndex=wordEntries.indexOf(new WordEntry(word,-1)))!=-1)
			    	{
			    		repeated=true;
			    	}
			    	
			    	if(repeated)
			    	{
			    		String toBeInserted=trans;
			    		toBeInserted=" || <"+dictName+"> "+toBeInserted;
			    		
			    		//记录在哪里，插入什么
			    		//toBeInsertedTrans.add(toBeInserted);
			    	    //toBeInsertedIndexes.add(new Long(wordEntries.get(repeatIndex).getIndexOfTrans()));
			    	    toBeInsertedTransEntries.add(new TransEntryForInsert(toBeInserted, wordEntries.get(repeatIndex).getIndexOfTrans()));
			    	}
			    	else
			    	{
			    		//加入新词条
			    		WordEntry newWordEntry=new WordEntry(word,totalNumOfEntries);
			    		totalNumOfEntries++;
			    		wordEntries.add(newWordEntry);
			    		
			    		//加入新解释
			    		outTrans.writeUnicodeLine("<"+dictName+"> "+trans);
			    		
			    		
			    	}
				}
				
				currentNumOfEntriesInRawLib++;
				progressOfRawLibSplit=(1.0*currentNumOfEntriesInRawLib/totalNumOfEntriesInRawLib);
					
			}
			
			outTrans.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error in createLibraryFile, IOException");
			ex.printStackTrace();
			return null;
		}
		
		
		// 现在根据toBeInsertedTrans和toBeInsertedIndexes插入解释	
		if (toBeInsertedTransEntries.size()>0)
		{
			long currentNumOfInsert = 0; //用来计算进度
			Collections.sort(toBeInsertedTransEntries);
			try {
				if (!copyFile(transFile, TEMP_FILE)) {
					throw new IOException();
				}

				MyRandomAccessFile inForInsert = new MyRandomAccessFile(
						TEMP_FILE, "r");
				MyRandomAccessFile outForInsert = new MyRandomAccessFile(
						transFile, "rw");

				String temp = null;
				long lineIndex = -1;
				while ((temp = inForInsert.readUnicodeLine()) != null) {
					lineIndex++;
					int tempIndex = -1;

					if ((tempIndex = toBeInsertedTransEntries
							.indexOf(new TransEntryForInsert("", lineIndex))) != -1) {
						temp = temp.trim();
						temp += toBeInsertedTransEntries.get(tempIndex)
								.getTrans();
					}

					outForInsert.writeUnicodeLine(temp);

					currentNumOfInsert++;
					progressOfInsert = (1.0 * currentNumOfInsert / totalNumOfEntries);
				}

				inForInsert.close();
				outForInsert.close();
			} catch (IOException ex) {
				System.out.println("Error in createLibraryFile, IOException");
				ex.printStackTrace();
				return null;
			}
		}
		else
		{	
			//直接跳过这一步
			progressOfInsert=1;
		}
		
		
		// 现在进行解释词库的每行长度的统一
		long currentNumOfUnifyLength=0;  //用来计算进度
		try
		{
			int maxLineLength=0;
			MyRandomAccessFile inForUnifyLineLength = new MyRandomAccessFile(transFile,"r");
            String temp=null;
			while((temp=inForUnifyLineLength.readUnicodeLine())!=null)
			{
			    maxLineLength=Math.max(maxLineLength,temp.length());
			    
			    currentNumOfUnifyLength++;
			    progressOfUnifyLength=1.0*currentNumOfUnifyLength/totalNumOfEntries*0.5;
			}
			inForUnifyLineLength.close();
			
			//现在获得maxLineLength,准备重写文件
			if (!copyFile(transFile, TEMP_FILE))
			{
				throw new IOException();
			}
			
			//开始重写
			currentNumOfUnifyLength=0;
			MyRandomAccessFile inForLineLength = new MyRandomAccessFile(TEMP_FILE,"r");
			MyRandomAccessFile outForLineLength = new MyRandomAccessFile(transFile,"rw");
			
			while((temp=inForLineLength.readUnicodeLine())!=null)
			{
				outForLineLength.writeUnicodeString(temp);
				byte[] bytes=new byte[2*(maxLineLength-temp.length())];
				for(int i=0;i<maxLineLength-temp.length();i++)
				{
					bytes[2*i]=0;
					bytes[2*i+1]=(byte)(' ');
				}
				
				outForLineLength.write(bytes);
				outForLineLength.writeChar('\n');
				
				currentNumOfUnifyLength++;
			    progressOfUnifyLength=0.5+1.0*currentNumOfUnifyLength/totalNumOfEntries*0.5;
			}
			
			inForLineLength.close();
			outForLineLength.close();
			
		    maxLineLengthOfTrans=maxLineLength;
		} 
		catch (IOException ex)
		{
			System.out.println("Error in createLibraryFile, IOException");
			ex.printStackTrace();
			return null;
		}

		// WordEntry加入及解释写入完毕。现在进行wordEntries的整理及写入
       	Collections.sort(wordEntries);
		
       	HashSet<Character> wordChars=new HashSet<Character>();  //用来存储单词的字符集
		Character minChar=null;
		Character maxChar=null;
		try
		{
			PrintWriter outForWordEntries=new PrintWriter(new BufferedWriter(new FileWriter(wordFile)));
		    for(int i=0;i<wordEntries.size();i++)
		    {
		    	if(null==wordEntries.get(i))
		    	{
		    		throw new IOException();
		    	}
		    	
		    	String currentWord=wordEntries.get(i).getWord();
		    	outForWordEntries.println(currentWord+" "+wordEntries.get(i).getIndexOfTrans());
		        //提取单词的字符集
		    	for(int n=0;n<currentWord.length();n++)
		    	{
		    		Character ch=new Character(currentWord.charAt(n));
		    		wordChars.add(ch);
		    		if(null==minChar)
		    		{
		    			minChar=ch;
		    		}
		    		else
		    		{
		    			if(ch<minChar)
		    			{
		    				minChar=ch;
		    			}
		    		}
		    		
		    		if(null==maxChar)
		    		{
		    			maxChar=ch;
		    		}
		    		else
		    		{
		    			if(ch>maxChar)
		    			{
		    				maxChar=ch;
		    			}
		    		}
		    	
		    	}
		    	
		    	progressOfWriteWordEntry=1.0*(i+1)/wordEntries.size();
		    }
		    
		    outForWordEntries.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error in createLibraryFile, IOException");
			ex.printStackTrace();
			return null;
		}
		
		//检测是否一个成功的词条也没有建成
		if(totalNumOfEntries<1||null==minChar||null==maxChar)
		{
			System.out.println("Error in createLibraryFile, totalNumOfEntries < 1");
			
			if(wordFile.exists())
			{
				wordFile.delete();
			}
			
			if(transFile.exists())
			{
				transFile.delete();
			}
			
			return null;
		}
		
		// 现在进行LibraryAccount的新建及加入,或者是刷新
		LibraryAccount libAccount=new LibraryAccount(from, to, dictName, wordFile, transFile,maxLineLengthOfTrans,totalNumOfEntries,wordChars,minChar,maxChar);	
		int tempIndex=-1;
		if((tempIndex=libraryAccountor.getLibraryAccounts().indexOf(libAccount))!=-1)
		{
			libraryAccountor.getLibraryAccounts().set(tempIndex,libAccount);
		}
		else
		{
			libraryAccountor.getLibraryAccounts().add(libAccount);
		}
		
		//libraryAccountor重新记录
		libraryAccountor.writeCurrentLibraryAccounts();
				
       	return libAccount;
	}

	// 拷贝文件,成功则返回true
    private static boolean copyFile(File from, File to)
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

    //这个方法用指定的分隔符作为行分隔符，读出一行
    private static String readLineBy(char separator, BufferedReader in)
    {
    	 if(null==in)
    	 {
    		 return null;
    	 }
    	 
    	 StringBuffer buffer=new StringBuffer();
    	 int temp=0;
    	 char tempChar=0;
    	 boolean readSomething=false;  //这个量用来考察是不是已经到了流尾
    	 
    	 try
    	 {
    	     while((temp=(in.read()))!=-1)
    	     {
    	    	 tempChar=(char)temp;
    	    	 readSomething=true;
    	      	 if(tempChar!=separator)
    	    	 {
    	    		 buffer.append(tempChar);
    	    	 }
    	      	 else
    	      	 {
    	      		 break;
    	      	 }
    	     }
    	     
    	     return readSomething?buffer.toString():null;
    	 }
    	 catch(IOException ex)
    	 {
    		 System.out.println("Error in readLineBy, IOExceptioin");
    		 ex.printStackTrace();
    		 return null;
    	 }
    }

    /*这个方法判断给定的字符串是不是合法的未经解析的词库的词条
     * 一个合法词条的条件：
     * 词条trim后按给定分隔符分开后，至少生成两块，两块都不能为空（trim后），长度大于零
     */
    private static boolean isLegalRawEntry(String s, char separator)
    {
    	if(null==s||0==s.length()||0==separator)
    	{
    		return false;
    	}
    	
    	s=s.trim();
    	StringTokenizer tokens=new StringTokenizer(s,""+separator);
    	
    	if(tokens.countTokens()<2)
    	{
    		return false;
    	}
    	
    	//提取单词部分和解释部分
    	String word=tokens.nextToken();
    	String trans="";
    	while(tokens.hasMoreTokens())
    	{
    	    trans+=tokens.nextToken();	
    	}
    	
    	//检验
    	word=word.replaceAll("\n"," ");  //将words中的换行符都去掉，因为换行是词库文件中有重要功能的字符
    	word=word.replaceAll(""+(char)13, " ");
    	word=word.trim();
    	trans=trans.replaceAll("\n", " ");  //将trans中的换行符都去掉，因为换行是词库文件中有重要功能的字符
    	trans=trans.replaceAll(""+(char)13, " ");
    	trans=trans.trim();  
    	
    	if(word.length()>0&&trans.length()>0)
    	{
    		return true;
    	}
    	else
    	{
    	    return false;
    	}
    }
    
    //用来判断是否是合法的构造名字的字符
    public static boolean isLegalCharOfName(char c)
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
    	
    	if(' '==c)
    	{
    		return true;
    	}
    	
    	if('.'==c)
    	{
    		return true;
    	}
    	
    	if('-'==c)
    	{
    		return true;
    	}
    	
    	return false;
    }

    //用来净化名字,若净化完毕后得到长度为零的字符串，则返回null
    public static String clarifyName(String name)
    {
    	if(null==name||0==name.length())
    	{
    		return null;
    	}
    	
    	name=name.trim();
    	StringBuffer buffer=new StringBuffer(name);
    	for(int i=0;i<buffer.length();i++)
    	{
    		if(!isLegalCharOfName(buffer.charAt(i)))
    		{
    			buffer.deleteCharAt(i);
    			i--;
    		}
    			
    	}
    	name=buffer.toString().trim();
    	
    	return 0==name.length()?null:name;
    }

    private void failAndDelete(File file)
    {
    	if(null==file||!file.exists())
    	{
    		return;
    	}
    	
    	file.delete();
    }
  
    //========================其他支持方法===========================
    
    
    /**
     * 这个方法读取wordFile，产生wordEntries,
     * 将其装入ArrayList<WordEntry>返回。
     * 如果出错，则返回null
     * 
     */
    protected static ArrayList<WordEntry> readWordEntries(File wordFile)
    {
    	if(null==wordFile||!wordFile.exists())
    	{
    		System.out.println("Error in readWordEntries, illegal argument");
    	    return null;
    	}
    	
    	//初始化进度纪录
    	progressOfReadWordEntry=0;
    	
    	//统计单词总数
    	long totalNum=0;
    	long currentNum=0;
    	try
    	{
       	    BufferedReader inForNum=new BufferedReader(new FileReader(wordFile));
    	    while(inForNum.readLine()!=null)
    	    {
    	    	totalNum++;
    	    }
    	    inForNum.close();
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in readWordEntries, IOException");
    		ex.printStackTrace();
    		return null;
    	}
    	
    	
    	//开始读取
    	ArrayList<WordEntry> wordEntries=new ArrayList<WordEntry>();
    	try
    	{
    	    BufferedReader in=new BufferedReader(new FileReader(wordFile));
     	   	String temp=null;
    	    while((temp=in.readLine())!=null)
    	    {
    	    	//最后一个空格前面都是单词，后面是indexOfTrans
    	    	//寻找最后一个空格
    	    	int spaceIndex=-1;
    	    	for(int i=0;i<temp.length();i++)
    	    	{
    	    		if(temp.charAt(i)==' ')
    	    		{
    	    			spaceIndex=i;
    	    		}
    	    	}
    	    	if(spaceIndex>=temp.length()-1)
    	    	{
    	    		throw new NumberFormatException();
    	    	}
    	    	
    	    	String word=temp.substring(0,spaceIndex);
    	    	long indexOfTrans=Long.parseLong(temp.substring(spaceIndex+1));
    	    
    	       	wordEntries.add(new WordEntry(word,indexOfTrans));
    	    
    	       	//更新进度
    	       	currentNum++;
    	       	progressOfReadWordEntry=(1.0*currentNum/totalNum);
    	    }
    	    
    	    in.close();
    
    	}
    	catch(IOException ex)
    	{
    		System.out.println("Error in readWordEntries, IOException");
    		ex.printStackTrace();
    		return null;
    	}
    	catch(NumberFormatException ex)
		{
			System.out.println("Error in readWordEntries, long type transition failed");
			ex.printStackTrace();
			return null;
		}
    	
    	return wordEntries;
    }
    
    //====================================================================================
    
    /**
     * 删除词库文件的方法
     */
    public static boolean deleteLibraryFile(LibraryAccount libraryAccount)
    {
    	//合法性检查
    	if(null==libraryAccount
    			||null==libraryAccountor
    			||libraryAccountor.isLibraryAccountsNull()
    	        ||(!libraryAccountor.getLibraryAccounts().contains(libraryAccount))
    	        ||(!libraryAccount.isLibraryAccountValid()))
    	{
    		System.out.println("Error in deleteLibrary, invalid libraryAccount argument");
    		return false;
    	}
    	
    	//删除文件
    	if(libraryAccount.getTransFile().exists())
    	{
    		libraryAccount.getTransFile().delete();
    	}
    	if(libraryAccount.getWordsFile().exists())
    	{
    		libraryAccount.getWordsFile().delete();
    	}
    	
    	//删除libraryAccount
    	boolean temp=libraryAccountor.getLibraryAccounts().remove(libraryAccount);
    	libraryAccountor.writeCurrentLibraryAccounts();
    	
    	return temp;
    	
    }

    /**
     * 这个方法从指定的文件夹fromDir拷贝所有词库文件到toDir下，
     * fromDir和toDir应该是目录,返回值标明成功与否
     */
    public static boolean copyAllLibraryFiles(File fromDir, File toDir)
    {
    	if(null==fromDir||!fromDir.isDirectory())
    	{
    		System.out.println("fromDir is not a Directory");
    		return false;
    	}

    	if(null==toDir||!toDir.isDirectory())
    	{
    		System.out.println("toDir is not a Directory");
    		return false;
    	}
    	
    	progressOfCopyLibraryFiles=0;
    	
    	//拷贝libraryAccounts
    	if(!copyFile(new File(fromDir,LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName()),new File(toDir,LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName())))
    	{
    		return false;
    	}

    	//读取libraryAccounts
    	File libraryAccountsFile=new File(fromDir, LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName());
    	ArrayList<LibraryAccount> libraryAccounts=null;
    	if (libraryAccountsFile.exists()) 
		{
			// 读取文件
			try 
			{
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(
								libraryAccountsFile)));

				Object temp = in.readObject();
				if (temp instanceof ArrayList)
				{
					libraryAccounts = (ArrayList<LibraryAccount>) temp;
				} 
				else 
				{
					return false;
				}

				in.close();
			} 
			catch (FileNotFoundException ex) 
			{
				return false;
			}
			catch (IOException ex) 
			{
				return false;
			}
			catch (ClassNotFoundException ex) 
			{
				return false;
			}
		}
		else
		{
			return false;
		}
    	
    	//拷贝词库文件
    	if(null==libraryAccounts)
    	{
    		return false;
    	}
    	
    	for(int i=0;i<libraryAccounts.size();i++)
    	{
    		if(null==libraryAccounts.get(i).getWordsFile()
    				||null==libraryAccounts.get(i).getTransFile())
    		{
    			return false;
    		}
    		
    		File wordFrom=new File(fromDir, libraryAccounts.get(i).getWordsFile().getName());
    		File transFrom=new File(fromDir, libraryAccounts.get(i).getTransFile().getName());
    		
    		File wordTo=new File(toDir, libraryAccounts.get(i).getWordsFile().getName());
    		File transTo=new File(toDir, libraryAccounts.get(i).getTransFile().getName());
    		    		
    		if(!copyFile(wordFrom,wordTo))
    		{
    			return false;
    		}
    		
    		if(!copyFile(transFrom, transTo))
    		{
    			return false;
    		}
    		
    		progressOfCopyLibraryFiles=1.0*(i+1)/libraryAccounts.size();
    	}
    	
    	return true;
    }
    /**
     * 
     * 这个方法将所有词库文件拷贝到BACKUP_FILE指定的目录下,
     * 返回值指明成功与否
     */
    public static boolean backupLibraryFiles()
    {
    	return copyAllLibraryFiles(LIBRARY_SOURCE, BACKUP_DIR);
    }
    
    /**
     * 这个方法将所有词库文件从BACKUP_FILE指定的目录下恢复,
     * 将会覆盖现有的文件，返回值指明成功与否
     * 
     */
    public static boolean restoreLibraryFiles()
    {
    	return copyAllLibraryFiles(BACKUP_DIR, LIBRARY_SOURCE);
    }
    
    /**
     * 这个方法更新内存中的LibraryAccountor，从而更新了词库
     * 的账户记录libraryAccounts，一般在restoreLibraryFiles
     * 和switchLibraryFilesInBackupAndCurrent后使用。这
     * 个方法用起来比较危险，只在词库配置程序里使用
     */ 
    public static void refreshLibraryAccountor()
    {
    	libraryAccountor=new LibraryAccountor();
    }
    
    /**
     * 这个方法将交换备份的词库文件和现在正使用的词库文件
     */
    public static boolean switchLibraryFilesInBackupAndCurrent()
    {
    	progressOfSwitchLibraryFiles=0;
    	
    	boolean stepFlag1=true;
    	if(!copyAllLibraryFiles(LIBRARY_SOURCE, TEMP_DIR))
    	{
    		stepFlag1=false;
    		System.out.println("Warning, library source file lost!");
    	}
    	
    	progressOfSwitchLibraryFiles=1;
    	
    	if(!copyAllLibraryFiles(BACKUP_DIR, LIBRARY_SOURCE))
    	{
    		progressOfSwitchLibraryFiles=2;
    		copyAllLibraryFiles(TEMP_DIR, LIBRARY_SOURCE);
    		return false;
    	}
    	
    	progressOfSwitchLibraryFiles=2;
    	
    	if(!copyAllLibraryFiles(TEMP_DIR, BACKUP_DIR))
    	{
    		return false;
    	}
    	
    	System.out.println("switchLibraryFiles finished");
    	
    	return stepFlag1;
    }

    /**
     * 用这个方法重置所有表示进度的值，否则在运行那些关系到进度值的方法后，
     * 进度值不会清零
     */
    public static void resetProcessValues() 
    {
		progressOfReadWordEntry = 0;
		progressOfRawLibSplit = 0;
		progressOfInsert = 0;
		progressOfUnifyLength = 0;
		progressOfWriteWordEntry = 0;
		progressOfCopyLibraryFiles = 0;
		progressOfSwitchLibraryFiles = 0;
	}
    
	public static double getProgressOfReadWordEntry() {
		return progressOfReadWordEntry;
	}

	public static double getProgressOfRawLibSplit() {
		return progressOfRawLibSplit;
	}

	public static double getProgressOfInsert() {
		return progressOfInsert;
	}

	public static double getProgressOfUnifyLength() {
		return progressOfUnifyLength;
	}

	public static double getProgressOfWriteWordEntry() {
		return progressOfWriteWordEntry;
	}

	public static double getProgressOfCopyLibraryFiles() {
		return progressOfCopyLibraryFiles;
	}

	public static int getProgressOfSwitchLibraryFiles() {
		return progressOfSwitchLibraryFiles;
	}
	
	

	
}
