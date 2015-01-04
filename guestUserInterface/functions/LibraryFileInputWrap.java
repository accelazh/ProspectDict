package guestUserInterface.functions;

import java.io.*;

/**
 * 因为createLibraryFile需要六个参数，因此用这个类
 * 将其包装，方便使用。这个类本身不检查输入的正确性
 */
public class LibraryFileInputWrap 
{
	private File libFile;
	private String from;
	private String to;
	private char swt;  //separator of word and its translation
	private char see;  //separator of entry and entry
	private String dictName;

	public LibraryFileInputWrap()
	{
		
	}
	
	public LibraryFileInputWrap(File libFile, String from, String to, 
			char swt, char see, String dictName)
	{
		this.libFile=libFile;
		this.from=from;
		this.to=to;
		this.swt=swt;
		this.see=see;
		this.dictName=dictName;
	}

	public File getLibFile() {
		return libFile;
	}

	public void setLibFile(File libFile) {
		this.libFile = libFile;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public char getSwt() {
		return swt;
	}

	public void setSwt(char swt) {
		this.swt = swt;
	}

	public char getSee() {
		return see;
	}

	public void setSee(char see) {
		this.see = see;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
}
