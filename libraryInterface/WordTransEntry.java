package libraryInterface;

/**
 * �������Ϊ��ʵ����ʷ��¼��ѯ��
 * ����ƵĴ��浥�ʺͽ��͵���
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
