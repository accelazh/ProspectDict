package conLibraryManager;

import libraryInterface.*;

/**
 * �����ûʲôʵ����;��ֻ��Ϊ���㷨����ƣ�
 * ���ڸ���LibraryAccount�ıȽϷ�ʽ
 */
class LibraryAccountByFromTo 
{
	private LibraryAccount libraryAccount;
	
	public LibraryAccountByFromTo(LibraryAccount libAcc)
	{
		this.libraryAccount=libAcc;
	}
	
	public LibraryAccount getLibraryAccount() {
		return libraryAccount;
	}

	public boolean equals(Object o)
	{
		System.out.println("libraryAccountByFromTo's equals is run");
		LibraryAccountByFromTo e=(LibraryAccountByFromTo)o;
		if((getLibraryAccount().getFrom().equals(e.getLibraryAccount().getFrom()))
			&&(getLibraryAccount().getTo().equals(e.getLibraryAccount().getTo())))
		{
			return true;
		}
		else
		{
			return false;
		}
			
	}

}
