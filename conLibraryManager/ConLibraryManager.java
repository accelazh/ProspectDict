package conLibraryManager;

import libraryManager.*;
import java.util.*;
import libraryInterface.*;
import settings.*;

/**
 * 这各类用来管理多个LibraryManager
 * 实例的共同工作，即实现多词库共同工作
 * 
 * 在使用的时候，查询必须先运行refreshTypeInWord()
 * 锁定单词后进行查询
 * 
 */
public class ConLibraryManager implements Constants
{
	/**
	 * LibraryManager成员列表
	 */
	private ArrayList<LibraryManager> libraryManagerList=new ArrayList<LibraryManager>();
	/**
	 * 标识是否所有active状态的词库都是同种词库。如果所有active状态的词库不都是同种的
	 * 即它们的from和to不都相同，那么在查询的时候就会有所取舍，有的active
	 * 状态的词库不被查询
	 */
	private boolean allTheSameKind=true;;
	/**
	 * 用来实现历史功能的数据结构
	 */
	private HistoryStack<WordTransEntry> historyStack=new HistoryStack<WordTransEntry>(10);

	
	public ConLibraryManager(LibraryAccount[] libAccs)
	{
		if(null==libAccs)
		{
			libAccs=new LibraryAccount[0];
		}
		
		for(int i=0;i<libAccs.length;i++)
		{
			LibraryManager libM=LibraryManager.createLibraryManager(libAccs[i]);
		    if(libM!=null)
		    {
		    	libraryManagerList.add(libM);
		    	libM.setActive(false);
		    }
		}
		
		//检查是否都是同种词库
		checkIsAllTheSameKind();
	}
	
	public ConLibraryManager()
	{
		this(null);
	}
	
	//检查词库中是否所有词库都是同种的
	//如果只有一个或零个active,那么也算都是同种的
	private void checkIsAllTheSameKind()
	{
		ArrayList<LibraryManager> activeList=new ArrayList<LibraryManager>();
		for(int i=0;i<libraryManagerList.size();i++)
		{
			if(libraryManagerList.get(i).isActive())
			{
				activeList.add(libraryManagerList.get(i));
			}
		}
		
		allTheSameKind=true;
		for(int i=1;i<activeList.size();i++)
		{
			if(!((activeList.get(i-1).getLibrary().getLibraryAccount().getFrom().equals(activeList.get(i).getLibrary().getLibraryAccount().getFrom()))
				&&(activeList.get(i-1).getLibrary().getLibraryAccount().getTo().equals(activeList.get(i).getLibrary().getLibraryAccount().getTo()))))
			{
				allTheSameKind=false;
				break;
			}
		}
	}
	
	/**
	 * 按照libAcc激活一个libraryManager,如果这个libraryManager
	 * 还没有生成，则自动添加
	 * @param libAcc
	 * @return 是否成功
	 */
	public boolean enactive(LibraryAccount libAcc)
	{
		if(null==libAcc)
		{
			return false;
		}
		
		boolean succ=false;
		int index=-1;
    	for(int i=0;i<libraryManagerList.size();i++)
		{
			if(libraryManagerList.get(i).getLibrary().getLibraryAccount().equals(libAcc))
			{
				index=i;
				break;
			}
		}
	    if(index>=0)
		{
			libraryManagerList.get(index).setActive(true);
			
			System.out.println("below is enactived: ");
			System.out.println(libAcc);
			
		    succ=true;
		}
		else
		{
			LibraryManager libM=LibraryManager.createLibraryManager(libAcc);
		    if(libM!=null)
		    {
		    	libraryManagerList.add(libM);
		    	libM.setActive(true);
		    	
		    	System.out.println("below is enactived: ");
				System.out.println(libAcc);
		    	
				succ=true;
		    }
		    else
		    {
		    	succ=false;
		    }
		}
		
		//检查是否所有词库都是同中词库
		checkIsAllTheSameKind();
		
		return succ;
	}
	
	private boolean enactiveWithoutCheckIsAllTheSameKind(LibraryAccount libAcc)
	{
		if(null==libAcc)
		{
			return false;
		}
		
		boolean succ=false;
		int index=-1;
    	for(int i=0;i<libraryManagerList.size();i++)
		{
			if(libraryManagerList.get(i).getLibrary().getLibraryAccount().equals(libAcc))
			{
				index=i;
				break;
			}
		}
		if(index>=0)
		{
			libraryManagerList.get(index).setActive(true);
		    succ=true;
		}
		else
		{
			LibraryManager libM=LibraryManager.createLibraryManager(libAcc);
		    if(libM!=null)
		    {
		    	libraryManagerList.add(libM);
		    	libM.setActive(true);
		    	succ=true;
		    }
		    else
		    {
		    	succ=false;
		    }
		}
		return succ;
	}
	/**
	 * 按照libAccs激活一组libraryManager,如果这个libraryManager
	 * 还没有生成，则自动添加
	 * @param libAccs
	 * @return 是否成功
	 */
	public boolean enactive(LibraryAccount[] libAccs)
	{
		if(null==libAccs||0==libAccs.length)
		{
			return false;
		}
		
		boolean succ=true;
		for(int i=0;i<libAccs.length;i++)
		{
			if(!enactiveWithoutCheckIsAllTheSameKind(libAccs[i]))
			{
				succ=false;
			}
		}
		
		//检查是否所有词库都是同中词库
		checkIsAllTheSameKind();
		return succ;
	}
	
	/**
	 * 按照libAcc将一个libraryManager的激活状态设为false,
	 * @param libAcc
	 * @return 是否成功
	 */
	public boolean deactive(LibraryAccount libAcc)
	{
		boolean succ=false;
		int index=-1;
    	for(int i=0;i<libraryManagerList.size();i++)
		{
			if(libraryManagerList.get(i).getLibrary().getLibraryAccount().equals(libAcc))
			{
				index=i;
				break;
			}
		}
		if(index>=0)
		{
			libraryManagerList.get(index).setActive(false);
		    
			System.out.println("below is deactived: ");
			System.out.println(libAcc);
			
			
			succ=true;
		}
		else
		{
			System.out.println("Error in deactive, not found");
			succ=false;
		}
		
		//检查是否所有词库都是同中词库
		checkIsAllTheSameKind();
		
		return succ;
	}

	public void refreshTypeInWord(WordEntry wordEntry)
	{
		System.out.println("====in rfreshTypeInWord====");
		System.out.println("allTheSameKind: "+allTheSameKind);
		
		LibraryManager libM=null;
		for(int i=0;i<libraryManagerList.size();i++)
		{
			
			if((libM=libraryManagerList.get(i)).isActive())
			{
				if(allTheSameKind)
				{
					libM.refreshTypeInWord(wordEntry);
					System.out.println("libraryManagerList["+i+"] is refreshed");
				}
				else
				{
					libM.setInDomain(libM.findIsInDomainance(wordEntry.getWord()));
					if(libM.isInDomain())
					{
						libM.refreshTypeInWord(wordEntry);
						System.out.println("libraryManagerList["+i+"] is refreshed");
					}
				}
			}
		}
	}
	
	public void refreshTypeInWord(String aimWord)
	{
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

	public boolean isFindWord()
	{
		for(int i=0;i<libraryManagerList.size();i++)
    	{
    		if(!libraryManagerList.get(i).isActive())
    		{
    			continue;
    		}
    		
    		if(!allTheSameKind)
    		{
    			if(!libraryManagerList.get(i).isInDomain())
    			{
    				continue;
    			}
    		}
    		
    		if(libraryManagerList.get(i).isFindWord())
    		{
    			return true;
    		}
    	}
		
		return false;
	}
	
    public WordEntry[] findSameRootWordEntries()
    {
    	System.out.println("====in findSameRootWordEntries====");
		System.out.println("allTheSameKind: "+allTheSameKind);
		    	
    	WordEntry[] wordEntries=null;
    	WordEntry[] maxLengthWordEntries=null;
    	
    	
    	for(int i=0;i<libraryManagerList.size();i++)
    	{
    		if(!libraryManagerList.get(i).isActive())
    		{
    			continue;
    		}
    		
    		if(!allTheSameKind)
    		{
    			if(!libraryManagerList.get(i).isInDomain())
    			{
    				continue;
    			}
    		}
    		
    		System.out.println("libraryManagerList["+i+"] is checked");
    		
    		if((wordEntries=libraryManagerList.get(i).findSameRootWordEntries()).length>MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED/2)
    		{
    			return wordEntries;
    		}
    		
    		if(null==maxLengthWordEntries)
    		{
    			maxLengthWordEntries=wordEntries;
    		}
    		else
    		{
    			if(wordEntries.length>maxLengthWordEntries.length)
    			{
    				maxLengthWordEntries=wordEntries;
    			}
    		}
    	}
    	
    	if(maxLengthWordEntries!=null)
    	{
    		return maxLengthWordEntries;
    	}
    	else
    	{
    		return new WordEntry[0];
    	}
    }

    public WordEntry[] findResembleWordEntries()
    {
    	Set<WordEntry> wordEntrySet=new HashSet<WordEntry>();
    	
    	LibraryManager libM=null;
    	WordEntry[] temp=null;
    	for(int i=0;i<libraryManagerList.size();i++)
    	{
    		libM=libraryManagerList.get(i);
    		if(!libM.isActive())
    		{
    			continue;
    		}
    		
    		if(!allTheSameKind)
    		{
    			if(!libraryManagerList.get(i).isInDomain())
    			{
    				continue;
    			}
    		}
    		
    		if((temp=libM.findResembleWordEntries())!=null)
    		{
    			for(int j=0;j<temp.length;j++)
    			{
    				wordEntrySet.add(temp[j]);
    			}
    		}
    	}
    	
    	Arrays.sort(temp=wordEntrySet.toArray(new WordEntry[0]));
    	return temp;
    }

    public TransEntry findTransEntry()
	{
    	LibraryManager libM=null;
    	TransEntry temp=null;
    	String wordToBeTranslated="";  //为了历史记录之用
    	String output="";
    	for(int i=0;i<libraryManagerList.size();i++)
    	{
    		libM=libraryManagerList.get(i);
    		if(!libM.isActive())
    		{
    			continue;
    		}
    		
    		if(!allTheSameKind)
    		{
    			if(!libraryManagerList.get(i).isInDomain())
    			{
    				continue;
    			}
    		}
    		
    		System.out.println("libraryManagerList["+i+"] is checked");
    		
    	    if((temp=libM.findTransEntry())!=null)
    	    {
    	    	output+=temp.getTrans()+"\n";
    	    	wordToBeTranslated=libM.getWordToSearch();
    	    }
    	}
    	
    	if(output.trim().length()>0)
    	{
    		historyStack.push(new WordTransEntry(wordToBeTranslated, output));
    		return new TransEntry(output);
    	}
    	else
    	{
    		return null;
    	}
    }

    public String findTransOnLine()
    {
    	System.out.println("====enter net search area====");
    	System.out.println("allTheSameKind: "+allTheSameKind);
    	if(allTheSameKind)
    	{
    		LibraryManager libM=null;
    		for(int i=0;i<libraryManagerList.size();i++)
    		{
    			if(libraryManagerList.get(i).isActive())
    			{
    				libM=libraryManagerList.get(i);
    				break;
    			}
    		}
    		
    		if(libM!=null)
    		{
    			System.out.println("below's net Search is checked:");
    			System.out.println(libM);
    			String temp=libM.findTransOnLine();
    			
    			if (temp != null)
    			{
    				temp=temp.trim();
					if (temp.length() > 0) 
					{
						if (!temp.equals(libM.getWordToSearch().trim())) {
							historyStack.push(new WordTransEntry(libM
									.getWordToSearch(), temp));
							return temp;
						}
					}
				}
    			else
    			{
    				return null;
    			}
    		}
    		
    		return null;
    	}
    	else
    	{
    		List<LibraryAccountByFromTo> tempAccs=new ArrayList<LibraryAccountByFromTo>();
    		LibraryManager libM=null;
    		String wordToBeTranslated=""; //为了历史记录之用
    		String output="";
    		for(int i=0;i<libraryManagerList.size();i++)
        	{
    			libM=libraryManagerList.get(i);
    			if(!libM.isActive())
        		{
        			continue;
        		}
        		
        		if(!libM.isInDomain())
        		{
        			continue;
        		}
        		
        		System.out.println("when allTheSameKind is false, libraryManagerList["+i+"] has passed the active and doman check");
        		
        		if(!tempAccs.contains(new LibraryAccountByFromTo(libM.getLibrary().getLibraryAccount())))
        		{
        		    String tempStr=libM.findTransOnLine();
        		    System.out.println("below's net Search is checked:");
        			System.out.println(libM);
        		    if(tempStr!=null
        		    		&&tempStr.trim().length()>0
        		    		&&(!tempStr.trim().equals((wordToBeTranslated=libM.getWordToSearch()).trim())))
        		    {
        		    	output+="<"+libM.getLibrary().getLibraryAccount().getName()+">"+
        		    	    "\n"+"    "+tempStr+"\n";
        		    }
        		    tempAccs.add(new LibraryAccountByFromTo(libM.getLibrary().getLibraryAccount()));
        		}
        		
        	}
    		
    		if(output.trim().length()>0)
    		{
    			historyStack.push(new WordTransEntry(wordToBeTranslated, output));
    			return output;
    		}
    		else
    		{
    			return null;
    		}
    	
    	}
    }
    
    /**
     * 这个方法返回上一个查询过的词及其解释,
     * 用以实现历史查询功能
     * 
     */
    public WordTransEntry findPrevious()
    {
    	return historyStack.back();
    }
    
    /**
     * 这个方法返回下一个查询过的词及其解释
     * 用以实现历史查询功能
     */
    public WordTransEntry findNext()
    {
    	return historyStack.forth();
    }
    
    public String toString()
    {
    	String output="";
    	output+="allTheSameKind "+allTheSameKind+"\n\n";
    	for(int i=0;i<libraryManagerList.size();i++)
    	{
    		output+="==libraryManagerList["+i+"]==\n";
    		output+=libraryManagerList.get(i).toString();
    		output+="\n";
    	}
    	
    	return output;
    }
}
