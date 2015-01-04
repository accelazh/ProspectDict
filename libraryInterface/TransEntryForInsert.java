package libraryInterface;

/**
 * 这个类是专门为Library.createLibraryFile方法提高效率而设计的，
 * 与WordEntry不同的地方在于，compareTo方法和equals方法以
 * indexOfTrans为准，而不是以word为准
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
