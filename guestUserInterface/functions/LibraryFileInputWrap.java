package guestUserInterface.functions;

import java.io.*;

/**
 * ��ΪcreateLibraryFile��Ҫ��������������������
 * �����װ������ʹ�á�����౾������������ȷ��
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
