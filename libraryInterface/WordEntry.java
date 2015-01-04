package libraryInterface;

/**
 * 
 * @author 这类包装了单词和其解释
 * 所在的相应文件中的index
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
