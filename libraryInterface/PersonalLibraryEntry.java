package libraryInterface;

import java.io.*;

/**
 * �����ģ��������ʱ��ʿ�Ĵ���
 * @author ZYL
 *
 */
public class PersonalLibraryEntry implements Serializable,Comparable<PersonalLibraryEntry>
{
	public static final long serialVersionUID=1111;
	
	private String word;
	private String trans;
	
	public PersonalLibraryEntry(String word, String trans)
	{
		this.word=word;
		this.trans=trans;	
	}

	public String getWord() {
		return word;
	}

	public String getTrans() {
		return trans;
	}
	
	public int compareTo(PersonalLibraryEntry o) 
	{
		return word.compareTo(o.getWord());
	}
	
	public boolean equals(Object o)
	{
		return word.equals(((PersonalLibraryEntry)o).getWord());
	}

	public boolean checkValid()
	{
		if(word!=null&&word.length()>0)
		{
			if(trans!=null&&trans.length()>0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �������
	 * @param s
	 */
	public void appendTrans(String s)
	{
		if(s!=null&&s.length()>0)
		{
			if(trans!=null&&trans.length()>0)
			{
				trans+=s;
			}
			else
			{
				trans=s;
			}
		}	
	}
	
	/**
	 * ��д���Ͳ���
	 * @param s
	 */
	public void rewriteTrans(String s)
	{
		if(s!=null&&s.length()>0)
		{
			this.trans=s;
		}
		else
		{
			this.trans="empty";
		}
	}
	
	public void rewriteWord(String s)
	{
		if(s!=null&&s.length()>0)
		{
			this.word=s;
		}
		else
		{
			this.word="empty";
		}
	}
	
	public String toString()
	{
		return "PersonalLibraryEntry["+"word="+word+", trans="+trans+"]";
	}

}
