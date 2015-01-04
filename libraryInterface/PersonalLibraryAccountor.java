package libraryInterface;

import java.util.*;
import java.io.*;

/**
 * 因为实现了可以创建多个生词本的功能，因此
 * 需要这个类作为统一管理者
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
			// 读取文件
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
	 * 向硬盘写入libraries,在程序结束的时候需要记录,
	 * 因此将这个方法加到finalize方法中
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
	 * @return 表明成功与否
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
	 * 有效的名字应该不为null,长度大于零，
	 * 并且不和过去的名字重复
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
	 * 新建一个词库，要求名字有效且不重复,
	 * 返回值表明是否成功
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
