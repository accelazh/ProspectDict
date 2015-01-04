package libraryInterface;

import java.util.*;
import java.io.*;

/**
 * 这各类用来实现一个类此生词本的功能，
 * 这是一个特殊的词库，允许用户自由更
 * 改
 * 
 * @author ZYL
 *
 */
public class PersonalLibrary implements Serializable,Comparable<PersonalLibrary>
{
	public static final long serialVersionUID=1112;
	
	public static PersonalLibraryAccountor libraryAccountor;
	
	static
	{
		libraryAccountor=new PersonalLibraryAccountor();
	}
	
	private String name="";
	
	private List<PersonalLibraryEntry> entries=new LinkedList<PersonalLibraryEntry>();
	
	public PersonalLibrary(String name)
	{
		if(name!=null)
		{
			this.name=name;
		}
		
		entries.add(new PersonalLibraryEntry("\t","\t"));
		entries.add(new PersonalLibraryEntry("\t","\t"));
	}
	
	public String getName()
	{
		return name;
	}
	
	/**
	 * 安全的重命名方法在PersonalLibraryAccountor中
	 * 
	 */
	protected void rename(String name)
	{
		if(name!=null&&name.length()>0)
		{
			this.name=name;
		}
	}
	
	public void append(PersonalLibraryEntry e)
	{
		if(e!=null&&e.checkValid())
		{
			entries.add(entries.size()-1,e);
		}
	}
	
	public void insert(int index, PersonalLibraryEntry e)
	{
		if (index >= 0 && index < entries.size())
		{
			if (e != null && e.checkValid())
			{
				entries.add(index, e);
			}
		}
	}
	
	public void remove(int index)
	{
		if(index>=0&&index<entries.size())
		{
			entries.remove(index);
		}
	}
	
	public void remove(PersonalLibraryEntry e)
	{
		if (e != null && e.checkValid())
		{
		    entries.remove(e);
		}
	}	
		
	
	/**
	 * 这个方法编辑指定词条的解释部分
	 * @param index 
	 * @param s 要写的内容
	 * @param append 指明是要在词条的解释部分后添加，还是要重写解释部分
	 */
	public void edit(int index, String s, boolean append)
	{
		if(index>=0&&index<entries.size())
		{
			if(append)
			{
				entries.get(index).appendTrans(s);
			}
			else
			{
				entries.get(index).rewriteTrans(s);
			}
		}
	}

	/**
	 * 将词库排序
	 */
	public void sort()
	{
		Collections.sort(entries);
	}
	
	/**
	 * 寻找指定词条的下标
	 * @param e
	 * @return 如果没有找到则返回-1
	 */
	public int search(PersonalLibraryEntry e)
	{
		if (e != null && e.checkValid())
		{
			return entries.indexOf(e);
		}
		else
		{
			return -1;
		}
	}
	
	public PersonalLibraryEntry get(int index)
	{
		if(index>=0&&index<entries.size())
		{
			return entries.get(index);
		}
		else
		{
			return null;
		}
	}
	
	public int compareTo(PersonalLibrary o) 
	{
		
		return name.compareTo(o.getName());
	}

	public boolean equals(Object o)
	{
		return name.equals(((PersonalLibrary)o).getName());
	}

	public static PersonalLibraryAccountor getLibraryAccountor() {
		return libraryAccountor;
	}
	
	public List<PersonalLibraryEntry> getEntries()
	{
		return this.entries;
	}
	/**
	 * 将指定词条上移一位
	 * @param index
	 */
	public boolean upMove(int index)
	{
		if(index>=2&&index<entries.size())
		{
			PersonalLibraryEntry e=entries.remove(index);
			entries.add(index-1,e);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将指定词条下移一位
	 * @param index
	 */
	public boolean downMove(int index)
	{
		if(index>=0&&index<entries.size()-2)
		{
			PersonalLibraryEntry e=entries.remove(index);
			entries.add(index+1,e);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将指定词条置顶
	 * @param index
	 */
	public boolean topMove(int index)
	{
		if(index>=2&&index<entries.size())
		{
			PersonalLibraryEntry e=entries.remove(index);
			entries.add(1,e);
			return true;
		}
		return false;
	}
	
	/**
	 * 将指定词条置底
	 * @param index
	 */
	public boolean bottomMove(int index)
	{
		if(index>=0&&index<entries.size()-2)
		{
			PersonalLibraryEntry e=entries.remove(index);
			entries.add(entries.size()-1,e);
			return true;
		}
		return false;
	}
	
	public int size()
	{
		return entries.size();
	}
	
	public void rewriteTrans(int index, String s)
	{
		if(index>=0&&index<entries.size())
		{
			entries.get(index).rewriteTrans(s);
		}
	}
	
	public void rewriteWord(int index, String s)
	{
		if(index>=0&&index<entries.size())
		{
			entries.get(index).rewriteWord(s);
		}
	}
}
