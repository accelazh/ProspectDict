package libraryInterface;

import java.io.*;
import java.util.*;

/**
 * 
 * @author ZYL
 * �����ģ��һ���Ը�����ͬ����Ĵʿ�Ĺ�����,
 * �ڲ�װ��һ�����,ÿ��Ԫ������Ӧ�ʿ����Ŀ
 * ��ϢLibraryAccount
 * 
 */
public class LibraryAccountor 
{
	/**
	 * ���Զ�ȡ��Ŀ��¼���ļ�
	 */
	public static final File LIBRARY_ACCOUNT_FILE=new File("libraries/libraryAccounts.dat");

	private ArrayList<LibraryAccount> libraryAccounts=new ArrayList<LibraryAccount>();
	
	public LibraryAccountor()
	{
		if (LIBRARY_ACCOUNT_FILE.exists()) 
		{
			// ��ȡ�ļ�
			try 
			{
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(
								LIBRARY_ACCOUNT_FILE)));

				Object temp = in.readObject();
				if (temp instanceof ArrayList)
				{
					libraryAccounts = (ArrayList<LibraryAccount>) temp;
				} 
				else 
				{
					System.out
							.println("Error when create libraryAccountor, Type cast error");
					libraryAccounts=new ArrayList<LibraryAccount>();
				}

				in.close();
			} 
			catch (FileNotFoundException ex) 
			{
				System.out
						.println("Error when create libraryAccountor, LIBRARY_ACCOUNT_FILE not found!");
				ex.printStackTrace();
				libraryAccounts=new ArrayList<LibraryAccount>();
			}
			catch (IOException ex) 
			{
				System.out
						.println("Error when create libraryAccountor, IOException");
				ex.printStackTrace();
				libraryAccounts=new ArrayList<LibraryAccount>();
			}
			catch (ClassNotFoundException ex) 
			{
				System.out
						.println("Error when create libraryAccountor, ClassNotFoundException");
				ex.printStackTrace();
				libraryAccounts=new ArrayList<LibraryAccount>();
			}
		}
		else
		{
			System.out.println("Error when initializing LibraryAccountor, LIBRARY_ACCOUNT_FILE not found!");
			libraryAccounts=new ArrayList<LibraryAccount>();
		}
	}
	
	public boolean isLibraryAccountsNull()
	{
		return null==libraryAccounts;
	}
	
	public ArrayList<LibraryAccount> getLibraryAccounts()
	{
		return libraryAccounts;
	}
	
	/**
	 * libraryAccounts����ʱ����д��libraryAccounts
	 */
	public void writeCurrentLibraryAccounts()
	{
		try
		{
			ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(LIBRARY_ACCOUNT_FILE)));
	        if(libraryAccounts!=null)
	        {
	        	out.writeObject(libraryAccounts);
	        }
	        else
	        {
	        	System.out.println("Error when writing libraryAccountor, libraryAccounts == null");
	        }
	        
	        out.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error when writing libraryAccountor, IOException");
		    ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * ���������libraryAccounts��ȡ���±�Ϊindex
	 * ��Ԫ�أ���indexԽ�磬�򷵻�null
	 */
	public LibraryAccount get(int index)
	{
		if(index>=0&&index<libraryAccounts.size())
		{
			return libraryAccounts.get(index);
		}
		else
		{
			return null;
		}
	}
}
