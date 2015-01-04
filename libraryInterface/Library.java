package libraryInterface;

import java.io.*;
import java.util.*;



/**
 * 
 * @author ZYL
 * ������ṩ���ֲ�ͬ��ʽ��д�Ĵʿ��һ������
 * ���𴴽���Բ�ͬ�Ĵʿ�library��ʵ�������
 * �������ļ��ĵ�һ��ͨ�����⣬�Լ�����������
 * ��Ľ����ʹʿⴴ�����ϲ�
 * 
 * ��һЩ����progress��ͷ�ģ��������������
 * ʱ��ϳ��ķ����Ľ��ȵģ�Ϊ����ȷʹ�ã�Ӧ��
 * ��ʹ������ǰ������ʼ��Щ����ʱ��ϳ��ķ���
 * ֮ǰ������resetProcessValues()��������
 * �����㣬��ȷ����ȷ
 */
public abstract class Library 
{
	//for debug
	private static final boolean debug=true;
	
	/**
	 * �ʿ��ļ���ͳһ�ķָ��� 
	 */
	public static final char SEPARATOR=' ';
	/**
	 * װ��library�ļ����ļ���
	 */
    public static final File LIBRARY_SOURCE=new File("libraries");
    /**
     * ��ʱ�ļ�
     */
    public static final File TEMP_FILE=new File("libraries/temporary.txt");
    /**
     * ��ʱ�ļ���
     */
    public static final File TEMP_DIR=new File("libraries/temp");
    /**
     * �����ļ���
     */
    public static final File BACKUP_DIR=new File("libraries/backup");
    /**
     * ��LibraryAccountor����˵��
     */
    private static LibraryAccountor libraryAccountor;
     
    static
    {
    	//��ʼ��libraryAccountor
    	libraryAccountor=new LibraryAccountor();
    	
    }
    /**
     * ������ʾ�ʿ������createLibrary�Ľ��ȵ�����ֵ0~100
     */
    private static double progressOfReadWordEntry;
    /**
     * ������ʾ�ʿ�����Ľ��ȵ�����ֵ0~100
     */
    private static double progressOfRawLibSplit;
    /**
     * ������ʾ�ʿ�����Ľ��ȵ�����ֵ0~100
     */
    private static double progressOfInsert;
    /**
     * ������ʾ�ʿ����y�Ľ��ȵ�����ֵ0~100
     */
    private static double progressOfUnifyLength;
    /**
     * ������ʾ�ʿ�����Ľ��ȵ�����ֵ0~100
     */
    private static double progressOfWriteWordEntry;
    /**
     * ��������copyAllLibraryFiles�Ľ���
     */
    private static double progressOfCopyLibraryFiles;
    /**
     * ��������switchLibraryFilesInBackupAndCurrent�Ľ���, �����ʵ�ֵ�ԭ��
     * �������ֻ�ܱ���״̬0��1��2��
     * ʵ���ϵĽ���Ӧ���������㣺
     * progressOfSwitchLibraryFiles*1.0/3+1.0/3*progressOfCopyLibraryFiles; 
     */
    private static int progressOfSwitchLibraryFiles;
    
    //===========================================
    /**
     * ����������Ǹ�LibraryInstance�õģ���¼����ͷ�б�
     */
    private WordEntry[] wordEntries;
    /**
     * ����������Ǹ�LbraryInstance�õģ���¼����ʿ����Ϣ
     */
    private LibraryAccount libraryAccount;
    /**
     * ���ר�Ÿ�����ʿ⣬������ٶ�
     */    
    private MyRandomAccessFile libReader;
   
    //============================================  
    
    /**
     * ���������libraryAccounָ�������ĸ��ʿ��libraryInstance��
     * �Ӷ�����һ�������ض��ʿ��Library����,�������ֵ�Ƿ����򷵻�
     * null
     * 
     */
    public static LibraryInstance createLibrary(LibraryAccount libraryAccount)
    {
    	//�Ϸ��Լ��
    	if(null==libraryAccount
    			||null==libraryAccountor
    			||libraryAccountor.isLibraryAccountsNull()
    	        ||(!libraryAccountor.getLibraryAccounts().contains(libraryAccount))
    	        ||(!libraryAccount.isLibraryAccountValid()))    //��������ٶ�̫������ʡȥ
    	{
    		System.out.println("Error in createLibrary, invalid libraryAccount argument");
    		return null;
    	}
    	
    	//��ʼ������ָʾ��¼
    	progressOfReadWordEntry=0;
    	
    	//==����libraryInstance==
    	LibraryInstance lib=new LibraryInstance();
    	
    	//��ʼ��libraryAccount
    	lib.setLibraryAccount(libraryAccount);
    	
    	//��ʼ��wordEntries
    	ArrayList<WordEntry> wordEntryList=readWordEntries(libraryAccount.getWordsFile());
    	if(null==wordEntryList)
    	{
    		System.out.println("Error in createLibrary, failed to create wordEntries");
    		return null;
    	}
    	lib.setWordEntries(wordEntryList.toArray(new WordEntry[0]));
    	    	
    	//������дָ��
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
    
    //============�����Ǹ�libraryInstance�õķ���============
    
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
	 * �����������WordEntry�Ż���Ӧ�Ľ���,
	 * ����������ʱ����null
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
		
		//��ʼѰ�ҽ���
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
	
	
	//================������ʵ�ִʿ�������ܵĲ���=================
	
	/**
	 * ����������𽫴���Ĵʿ��ļ��������ڸ��������ӦĿ¼�½��������������ֵ�ʿ�
	 * �������ϴʿ⡣
	 * ����ٶ��������Ĵʿ��У�һ�����������м�����ֿ���һ��������һ�����ʺ�һ������
	 * ��ɣ��м��ɵ��ʺͽ��͵ķָ����������м�����뵥�ʺͽ��͵ļ������ͬ��
	 * �ʿ����������һЩ���Ǵ������ı���
	 * ע�⣬��������ʿ��from,to,��dictName����ͬ���򽫻ᱻ��Ϊ��ͬ�����кϲ�
	 * 
	 * @param rawLibraryFile ��Ҫ�����Ĵʿ��ļ�������˵�մ��������صĴʿ⣩
	 * @param separatorOfWordAndTrans ��Ҫ�����Ĵʿ��ļ��У����ʺͽ���֮��ķָ���
	 * @param separatorOfEntryAndEntry ��Ҫ�����Ĵʿ��ļ��У����������֮��ķָ���
	 * @param from ����ʿ��ǽ�ʲô�����ʲô��
	 * @param to ����ʿ��ǽ�ʲô�����ʲô��
	 * @param dictName ������ֵ�ʿ�����֣������null�򡰡��Ļ������Զ���Ϊ"Default",
	 *        ������ֽ�������ڵ��ʵĽ��͵�ǰ�档
	 * @return ����½��ɹ����򷵻�����ʿ��LibraryAccount, ������������򷵻�null
	 */
	public static LibraryAccount createLibraryFile(File rawLibraryFile, 
			char separatorOfWordAndTrans, char separatorOfEntryAndEntry,
			String from, String to, String dictName)
	{
		//�������ĺϷ���
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
		
		//���ý��ȼ�¼
		progressOfReadWordEntry=0;
		progressOfRawLibSplit=0;
		progressOfInsert=0;
		progressOfUnifyLength=0;
		progressOfWriteWordEntry=0;
		
		//����dictName
		dictName=clarifyName(dictName);
		if(null==dictName)
		{
			dictName="Default";
		}
		
		//����from��to
		from=clarifyName(from);
		to=clarifyName(to);
		
		if(null==from||null==to)
		{
			System.out.println("Error in createLibraryFile, invalid arguments");
			return null;
		}
		
		//�Ƚ��б���
		backupLibraryFiles();
		progressOfCopyLibraryFiles=1;
		
		//��ʼ���� 
		File wordFile=new File("libraries\\"+dictName+"_from"+from+"To"+to+"Word.dic");
		File transFile=new File("libraries\\"+dictName+"_from"+from+"To"+to+"Trans.dic");
	
		ArrayList<WordEntry> wordEntries=null;
		MyRandomAccessFile outTrans;
		long totalNumOfEntries=0;
		int maxLineLengthOfTrans=0;  //transFile��ÿ�е���󳤶ȣ�ͳһ������ƶ��ļ�ָ��,������ʵ���ļ��е��ֽڳ���
		
		//���ɴʿ��Ƿ���ڣ����¾ɴʿ��from��to��ͬ��,������ʼ�趨
		if(libraryAccountor.getLibraryAccounts().contains(new LibraryAccount(from,to,dictName))
				&&(wordFile.exists())&&(transFile.exists()))
		{
			System.out.println("old library file already exists, now rewrite");
			
			try
			{
				//����wordEntries
			    wordEntries=readWordEntries(wordFile);
		        
			    if(null==wordEntries)
			    {
			    	System.out.println("Error in creatingLibraryFile, failed to readWordEntries");
			        return null;
			    }
			    
		        //����totalNumOfEntries
		        MyRandomAccessFile inForNum=new MyRandomAccessFile(transFile,"rw");
		        while(inForNum.readUnicodeLine()!=null)
		        {
		        	totalNumOfEntries++;
		        }
		        
		        //����outTrans����ָ���ļ�β
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
		
		
		// ͳ��rawLibraryFile�е�������
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
		
		//��ʼ�����´ʿ�
		List<TransEntryForInsert> toBeInsertedTransEntries=new LinkedList<TransEntryForInsert>();  //��Ϊ�´ʺ;ɴʿ����ظ�����ʱ��Ҫ�����ǵĽ��ͺϲ��������������ţ��������롣���������¼����Ľ��ͺ���Ӧ�ò����index	
		try
		{
		    BufferedReader inRawLib=new BufferedReader(new FileReader(rawLibraryFile));
		    String temp=null;
			while((temp=readLineBy(separatorOfEntryAndEntry,inRawLib))!=null)
			{
				if(isLegalRawEntry(temp,separatorOfWordAndTrans))
				{
					//���ɾ�������ĵ���String�ͽ���String
					temp=temp.trim();
					StringTokenizer tokens=new StringTokenizer(temp,""+separatorOfWordAndTrans);
					
					String word=tokens.nextToken();
			    	String trans="";
			    	while(tokens.hasMoreTokens())
			    	{
			    	    trans+=tokens.nextToken();	
			    	}
			    	
			    	word=word.replaceAll("\n"," ");  //��words�еĻ��з���ȥ������Ϊ�����Ǵʿ��ļ�������Ҫ���ܵ��ַ�
			    	word=word.replaceAll(""+(char)13, " ");
			    	word=word.trim();
			    	trans=trans.replaceAll("\n", " ");  //��trans�еĻ��з���ȥ������Ϊ�����Ǵʿ��ļ�������Ҫ���ܵ��ַ�
			    	trans=trans.replaceAll(""+(char)13, " ");
			    	trans=trans.trim();  
			    	
			    	//���word����ǰ�Ƿ��ظ�
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
			    		
			    		//��¼���������ʲô
			    		//toBeInsertedTrans.add(toBeInserted);
			    	    //toBeInsertedIndexes.add(new Long(wordEntries.get(repeatIndex).getIndexOfTrans()));
			    	    toBeInsertedTransEntries.add(new TransEntryForInsert(toBeInserted, wordEntries.get(repeatIndex).getIndexOfTrans()));
			    	}
			    	else
			    	{
			    		//�����´���
			    		WordEntry newWordEntry=new WordEntry(word,totalNumOfEntries);
			    		totalNumOfEntries++;
			    		wordEntries.add(newWordEntry);
			    		
			    		//�����½���
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
		
		
		// ���ڸ���toBeInsertedTrans��toBeInsertedIndexes�������	
		if (toBeInsertedTransEntries.size()>0)
		{
			long currentNumOfInsert = 0; //�����������
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
			//ֱ��������һ��
			progressOfInsert=1;
		}
		
		
		// ���ڽ��н��ʹʿ��ÿ�г��ȵ�ͳһ
		long currentNumOfUnifyLength=0;  //�����������
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
			
			//���ڻ��maxLineLength,׼����д�ļ�
			if (!copyFile(transFile, TEMP_FILE))
			{
				throw new IOException();
			}
			
			//��ʼ��д
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

		// WordEntry���뼰����д����ϡ����ڽ���wordEntries������д��
       	Collections.sort(wordEntries);
		
       	HashSet<Character> wordChars=new HashSet<Character>();  //�����洢���ʵ��ַ���
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
		        //��ȡ���ʵ��ַ���
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
		
		//����Ƿ�һ���ɹ��Ĵ���Ҳû�н���
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
		
		// ���ڽ���LibraryAccount���½�������,������ˢ��
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
		
		//libraryAccountor���¼�¼
		libraryAccountor.writeCurrentLibraryAccounts();
				
       	return libAccount;
	}

	// �����ļ�,�ɹ��򷵻�true
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

    //���������ָ���ķָ�����Ϊ�зָ���������һ��
    private static String readLineBy(char separator, BufferedReader in)
    {
    	 if(null==in)
    	 {
    		 return null;
    	 }
    	 
    	 StringBuffer buffer=new StringBuffer();
    	 int temp=0;
    	 char tempChar=0;
    	 boolean readSomething=false;  //��������������ǲ����Ѿ�������β
    	 
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

    /*��������жϸ������ַ����ǲ��ǺϷ���δ�������Ĵʿ�Ĵ���
     * һ���Ϸ�������������
     * ����trim�󰴸����ָ����ֿ��������������飬���鶼����Ϊ�գ�trim�󣩣����ȴ�����
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
    	
    	//��ȡ���ʲ��ֺͽ��Ͳ���
    	String word=tokens.nextToken();
    	String trans="";
    	while(tokens.hasMoreTokens())
    	{
    	    trans+=tokens.nextToken();	
    	}
    	
    	//����
    	word=word.replaceAll("\n"," ");  //��words�еĻ��з���ȥ������Ϊ�����Ǵʿ��ļ�������Ҫ���ܵ��ַ�
    	word=word.replaceAll(""+(char)13, " ");
    	word=word.trim();
    	trans=trans.replaceAll("\n", " ");  //��trans�еĻ��з���ȥ������Ϊ�����Ǵʿ��ļ�������Ҫ���ܵ��ַ�
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
    
    //�����ж��Ƿ��ǺϷ��Ĺ������ֵ��ַ�
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

    //������������,��������Ϻ�õ�����Ϊ����ַ������򷵻�null
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
  
    //========================����֧�ַ���===========================
    
    
    /**
     * ���������ȡwordFile������wordEntries,
     * ����װ��ArrayList<WordEntry>���ء�
     * ��������򷵻�null
     * 
     */
    protected static ArrayList<WordEntry> readWordEntries(File wordFile)
    {
    	if(null==wordFile||!wordFile.exists())
    	{
    		System.out.println("Error in readWordEntries, illegal argument");
    	    return null;
    	}
    	
    	//��ʼ�����ȼ�¼
    	progressOfReadWordEntry=0;
    	
    	//ͳ�Ƶ�������
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
    	
    	
    	//��ʼ��ȡ
    	ArrayList<WordEntry> wordEntries=new ArrayList<WordEntry>();
    	try
    	{
    	    BufferedReader in=new BufferedReader(new FileReader(wordFile));
     	   	String temp=null;
    	    while((temp=in.readLine())!=null)
    	    {
    	    	//���һ���ո�ǰ�涼�ǵ��ʣ�������indexOfTrans
    	    	//Ѱ�����һ���ո�
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
    	    
    	       	//���½���
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
     * ɾ���ʿ��ļ��ķ���
     */
    public static boolean deleteLibraryFile(LibraryAccount libraryAccount)
    {
    	//�Ϸ��Լ��
    	if(null==libraryAccount
    			||null==libraryAccountor
    			||libraryAccountor.isLibraryAccountsNull()
    	        ||(!libraryAccountor.getLibraryAccounts().contains(libraryAccount))
    	        ||(!libraryAccount.isLibraryAccountValid()))
    	{
    		System.out.println("Error in deleteLibrary, invalid libraryAccount argument");
    		return false;
    	}
    	
    	//ɾ���ļ�
    	if(libraryAccount.getTransFile().exists())
    	{
    		libraryAccount.getTransFile().delete();
    	}
    	if(libraryAccount.getWordsFile().exists())
    	{
    		libraryAccount.getWordsFile().delete();
    	}
    	
    	//ɾ��libraryAccount
    	boolean temp=libraryAccountor.getLibraryAccounts().remove(libraryAccount);
    	libraryAccountor.writeCurrentLibraryAccounts();
    	
    	return temp;
    	
    }

    /**
     * ���������ָ�����ļ���fromDir�������дʿ��ļ���toDir�£�
     * fromDir��toDirӦ����Ŀ¼,����ֵ�����ɹ����
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
    	
    	//����libraryAccounts
    	if(!copyFile(new File(fromDir,LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName()),new File(toDir,LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName())))
    	{
    		return false;
    	}

    	//��ȡlibraryAccounts
    	File libraryAccountsFile=new File(fromDir, LibraryAccountor.LIBRARY_ACCOUNT_FILE.getName());
    	ArrayList<LibraryAccount> libraryAccounts=null;
    	if (libraryAccountsFile.exists()) 
		{
			// ��ȡ�ļ�
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
    	
    	//�����ʿ��ļ�
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
     * ������������дʿ��ļ�������BACKUP_FILEָ����Ŀ¼��,
     * ����ֵָ���ɹ����
     */
    public static boolean backupLibraryFiles()
    {
    	return copyAllLibraryFiles(LIBRARY_SOURCE, BACKUP_DIR);
    }
    
    /**
     * ������������дʿ��ļ���BACKUP_FILEָ����Ŀ¼�»ָ�,
     * ���Ḳ�����е��ļ�������ֵָ���ɹ����
     * 
     */
    public static boolean restoreLibraryFiles()
    {
    	return copyAllLibraryFiles(BACKUP_DIR, LIBRARY_SOURCE);
    }
    
    /**
     * ������������ڴ��е�LibraryAccountor���Ӷ������˴ʿ�
     * ���˻���¼libraryAccounts��һ����restoreLibraryFiles
     * ��switchLibraryFilesInBackupAndCurrent��ʹ�á���
     * �������������Ƚ�Σ�գ�ֻ�ڴʿ����ó�����ʹ��
     */ 
    public static void refreshLibraryAccountor()
    {
    	libraryAccountor=new LibraryAccountor();
    }
    
    /**
     * ����������������ݵĴʿ��ļ���������ʹ�õĴʿ��ļ�
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
     * ����������������б�ʾ���ȵ�ֵ��������������Щ��ϵ������ֵ�ķ�����
     * ����ֵ��������
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
