package libraryManager;

import java.util.Collections;
import libraryInterface.*;
import java.util.*;

/**
 * 功能接近LibraryManager，
 * 特别为PersonLibrary提供
 * 
 * @author ZYL
 *
 */
public class PersonalLibraryManager
{
	private PersonalLibrary library;
	
	/**
	 * 这个方法建立PersonalLibraryAccountor中的libraries的对应index的PersonalLibraryManager
	 * 工厂式的构造方法
	 * @param index
	 * @return
	 */
	public static PersonalLibraryManager createPersonalLibraryManager(int index)
	{
		PersonalLibrary libP=PersonalLibrary.getLibraryAccountor().get(index);
		
		if(null==libP)
		{
			System.out.println("Error when creating PersonalLibraryManager, failed to create PersonalLibrary");
			return null;
		}
		
		PersonalLibraryManager libMP=new PersonalLibraryManager();
		libMP.setLibrary(libP);
		
		return libMP;
	}

	/**
	 * 新建一个词库
	 * @param name
	 * @return
	 */
	public static boolean createPersonalLibrary(String name)
	{
		return getLibraryAccountor().create(name);
	}
	
	public static PersonalLibraryAccountor getLibraryAccountor()
	{
		return PersonalLibrary.getLibraryAccountor();
	}
	
	public PersonalLibrary getLibrary()
	{
		return library;
	}

	protected void setLibrary(PersonalLibrary library) 
	{
		this.library = library;
	}
	
	//==================下面实现具体实例的功能方法=======================

	public String getName()
	{
		return library.getName();
	}
	
	public boolean rename(String name)
	{
		return getLibraryAccountor().rename(getLibraryAccountor().indexOf(library), name);
	}
	
	public void append(PersonalLibraryEntry e)
	{
		library.append(e);	
	}
	
	public void insert(int index, PersonalLibraryEntry e)
	{
		library.insert(index, e);
	}
	
	public void remove(int index)
	{
		library.remove(index);
	}
	
	public void remove(PersonalLibraryEntry e)
	{
		library.remove(e);
	}	
		
	
	/**
	 * 这个方法编辑指定词条的解释部分
	 * @param index 
	 * @param s 要写的内容
	 * @param append 指明是要在词条的解释部分后添加，还是要重写解释部分
	 */
	public void edit(int index, String s, boolean append)
	{
		library.edit(index, s, append);
	}

	/**
	 * 自动查找word并将其相应词条修改
	 * @param word
	 * @param s
	 * @param append
	 */
	public void edit(String word, String s, boolean append)
	{
		library.edit(search(new PersonalLibraryEntry(word, "for search")), s, append);
	}
	
	/**
	 * 将词库排序
	 */
	public void sort()
	{
		library.sort();
	}
	
	/**
	 * 寻找指定词条的下标
	 * @param e
	 * @return 如果没有找到则返回-1
	 */
	public int search(PersonalLibraryEntry e)
	{
		return library.search(e);
	}
	
	public int search(String word)
	{
		return library.search(new PersonalLibraryEntry(word, "for search"));
	}
	
	/**
	 * 返回词条的index下标的那一个
	 * @param index
	 * @return
	 */
	public PersonalLibraryEntry get(int index)
	{
		return library.get(index);
	}
	
	public List<PersonalLibraryEntry> getEntyList()
	{
		return library.getEntries();
	}
	
	public boolean upMove(int index)
	{
		return library.upMove(index);
	}
	
	public boolean downMove(int index)
	{
		return library.downMove(index);
	}
	
	public boolean topMove(int index)
	{
		return library.topMove(index);
	}
	
	public boolean bottomMove(int index)
	{
		return library.bottomMove(index);
	}
	
	public int size()
	{
		return library.size();
	}
	
	public void rewriteTrans(int index, String s)
	{
		library.rewriteTrans(index, s);
	}
	
	public void rewriteWord(int index, String s)
	{
		library.rewriteWord(index, s);
	}
}
