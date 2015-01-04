package libraryInterface;

/**
 * �������ר��ΪLibrary.createLibraryFile�������Ч�ʶ���Ƶģ�
 * ��WordEntry��ͬ�ĵط����ڣ�compareTo������equals������
 * indexOfTransΪ׼����������wordΪ׼
 * @author ZYL
 *
 */
class TransEntryForInsert implements Comparable<TransEntryForInsert>
{
	private String trans;
	private long indexOfTrans;
	
	public TransEntryForInsert(String trans, long indexOfTrans)
	{
		this.trans=trans;
		this.indexOfTrans=indexOfTrans;
	}

	public String getTrans() {
		return trans;
	}

	public long getIndexOfTrans() {
		return indexOfTrans;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return indexOfTrans==((TransEntryForInsert)obj).getIndexOfTrans();
	}

	@Override
	public String toString() 
	{
		return "TransEntryForInsert"+"[trans="+trans+",indexOfTrans="+indexOfTrans+"]";
	}

	public int compareTo(TransEntryForInsert w) 
	{
		return (int)(indexOfTrans-w.getIndexOfTrans());
	}
}
