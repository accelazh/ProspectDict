package libraryInterface;

/**
 * 这个类是为了实现历史记录查询，
 * 而设计的储存单词和解释的类
 * @author ZYL
 *
 */
public class WordTransEntry 
{
	private String word;
	private String trans;
	
	public WordTransEntry(String word, String trans)
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

}
