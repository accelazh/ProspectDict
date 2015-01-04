package conLibraryManager;

import libraryInterface.*;

/**
 * 这个类没什么实际用途，只是为了算法而设计，
 * 用于更换LibraryAccount的比较方式
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
