package libraryInterface;

/**
 * 
 * @author ZYL
 * 这个类包装了单词的解释
 */
public class TransEntry 
{
	private String trans;
	
	public TransEntry(String trans)
	{
		this.trans=trans;
	}
	
	public String getTrans()
	{
		return trans;
	}

	@Override
	public String toString()
	{
		return trans;
	}

}
