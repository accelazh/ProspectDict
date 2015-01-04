package libraryInterface;

import java.util.*;
import java.io.*;

/**
 * ��Ϊʵ���˿��Դ���������ʱ��Ĺ��ܣ����
 * ��Ҫ�������Ϊͳһ������
 * @author ZYL
 *
 */
public class PersonalLibraryAccountor 
{
	public static final File PERSONAL_LIBRARIES_FILE=new File("libraries/personalLibraries.dat");
	
	private List<PersonalLibrary> libraries=new ArrayList<PersonalLibrary>();
	
	public PersonalLibraryAccountor()
	{
		if (PERSONAL_LIBRARIES_FILE.exists()) 
		{
			// ��ȡ�ļ�
			try 
			{
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(new FileInputStream(
								PERSONAL_LIBRARIES_FILE)));

				Object temp = in.readObject();
				if (temp instanceof ArrayList)
				{
					libraries = (ArrayList<PersonalLibrary>) temp;
				} 
				else 
				{
					System.out
							.println("Error when create PersonalLibraryAccountor, Type cast error");
				}

				in.close();
			} 
			catch (FileNotFoundException ex) 
			{
				System.out
						.println("Error when create PersonalLibraryAccountor, PERSONAL_LIBRARIES_FILE not found!");
				ex.printStackTrace();
			}
			catch (IOException ex) 
			{
				System.out
						.println("Error when create PersonalLibraryAccountor, IOException");
				ex.printStackTrace();
			}
			catch (ClassNotFoundException ex) 
			{
				System.out
						.println("Error when create PersonalLibraryAccountor, ClassNotFoundException");
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("Error when initializing PersonalLibraryAccountor, PERSONAL_LIBRARIES_FILE not found!");
		}
	}

	/**
	 * ��Ӳ��д��libraries,�ڳ��������ʱ����Ҫ��¼,
	 * ��˽���������ӵ�finalize������
	 */
	public void writeCurrentPersonalLibraries()
	{
		try
		{
			ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(PERSONAL_LIBRARIES_FILE)));
	        if(libraries!=null)
	        {
	        	out.writeObject(libraries);
	        }
	        else
	        {
	        	System.out.println("Error when writing PersonalLibraryAccountor, libraryAccounts == null");
	        }
	        
	        out.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error when writing PersonalLibraryAccountor, IOException");
		    ex.printStackTrace();
		}
	}
	
	public PersonalLibrary get(int index)
	{
		if(index>=0&&index<libraries.size())
		{
			return libraries.get(index);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * @param index
	 * @param name
	 * @return �����ɹ����
	 */
	public boolean rename(int index, String name)
	{
		if(!checkNameValid(name))
		{
			return false;
		}
		
		name=name.trim();
		
		if(index>=0&&index<libraries.size())
		{
			libraries.get(index).rename(name);
			return true;
		}
		
		return false;
	}
	
	/**
	 * ��Ч������Ӧ�ò�Ϊnull,���ȴ����㣬
	 * ���Ҳ��͹�ȥ�������ظ�
	 * @param name
	 * @return
	 */
	public boolean checkNameValid(String name)
	{
		if(null==name||0==name.trim().length())
		{
			return false;
		}
		
		name=name.trim();
		
		for(int i=0;i<libraries.size();i++)
		{
			if(libraries.get(i).getName().equals(name))
			{
				return false;
			}
		}
		
		return true;
	}

	/**
	 * �½�һ���ʿ⣬Ҫ��������Ч�Ҳ��ظ�,
	 * ����ֵ�����Ƿ�ɹ�
	 * @param name
	 * @return
	 */
    public boolean create(String name)
    {
    	if(!checkNameValid(name))
    	{
    		return false;
    	}
    	
    	libraries.add(new PersonalLibrary(name));
    	return true;
    }
    
    public void remove(PersonalLibrary e)
    {
    	libraries.remove(e);
    }
    
    public void remove(int index)
    {
    	if(index>=0&&index<libraries.size())
    	{
    		libraries.remove(index);
    	}
    }
    
    public boolean contains(PersonalLibrary e)
    {
    	return libraries.contains(e);
    }
    
 	public List<PersonalLibrary> getLibraries() {
		return libraries;
	}
	
	public int size()
	{
		return libraries.size();
	}
	
	public int indexOf(PersonalLibrary e)
	{
		return libraries.indexOf(e);
	}
	
	public void sort()
	{
		Collections.sort(libraries);
	}
}
