package libraryManager;

import java.util.Collections;
import libraryInterface.*;
import java.util.*;

/**
 * ���ܽӽ�LibraryManager��
 * �ر�ΪPersonLibrary�ṩ
 * 
 * @author ZYL
 *
 */
public class PersonalLibraryManager
{
	private PersonalLibrary library;
	
	/**
	 * �����������PersonalLibraryAccountor�е�libraries�Ķ�Ӧindex��PersonalLibraryManager
	 * ����ʽ�Ĺ��췽��
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
	 * �½�һ���ʿ�
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
	
	//==================����ʵ�־���ʵ���Ĺ��ܷ���=======================

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
	 * ��������༭ָ�������Ľ��Ͳ���
	 * @param index 
	 * @param s Ҫд������
	 * @param append ָ����Ҫ�ڴ����Ľ��Ͳ��ֺ���ӣ�����Ҫ��д���Ͳ���
	 */
	public void edit(int index, String s, boolean append)
	{
		library.edit(index, s, append);
	}

	/**
	 * �Զ�����word��������Ӧ�����޸�
	 * @param word
	 * @param s
	 * @param append
	 */
	public void edit(String word, String s, boolean append)
	{
		library.edit(search(new PersonalLibraryEntry(word, "for search")), s, append);
	}
	
	/**
	 * ���ʿ�����
	 */
	public void sort()
	{
		library.sort();
	}
	
	/**
	 * Ѱ��ָ���������±�
	 * @param e
	 * @return ���û���ҵ��򷵻�-1
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
	 * ���ش�����index�±����һ��
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
