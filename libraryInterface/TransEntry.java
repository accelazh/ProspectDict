package libraryInterface;

/**
 * 
 * @author ZYL
 * ������װ�˵��ʵĽ���
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
