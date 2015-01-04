package libraryInterface;

/**
 * 
 * @author �����װ�˵��ʺ������
 * ���ڵ���Ӧ�ļ��е�index
 *
 */
public class WordEntry implements Comparable<WordEntry>
{
	private String word;
	private long indexOfTrans;
	
	public WordEntry(String word, long indexOfTrans)
	{
		this.word=word;
		this.indexOfTrans=indexOfTrans;
	}

	public String getWord() {
		return word;
	}

	public long getIndexOfTrans() {
		return indexOfTrans;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return word.equals(((WordEntry)obj).getWord());
	}

	@Override
	public String toString() 
	{
		return "WordEntry"+"[word="+word+",indexOfTrans="+indexOfTrans+"]";
	}

	public int compareTo(WordEntry o) 
	{
		return word.compareTo(o.getWord());
	}
	
}
