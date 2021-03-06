package guestUserInterface.functions;

import java.io.File;

import javax.swing.filechooser.*;

public class JpgeFileFilter extends FileFilter
{
	//jpg�Ĵ洢��ʽ
	public static final String formation="jpg";
	
	@Override
	public boolean accept(File f)
	{
		if(null==f)
		{
			return false;
		}
		
		if (f.isDirectory()) 
		{
			return true;
	    }

		String extension = getExtension(f);
		
		if (extension != null) 
		{
			if (extension.equals(formation.toLowerCase()))
			{
				return true;
			} 
		}

		return false;
	}
	
	public static String getExtension(File f)
	{
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public String getDescription() 
	{
		return formation;
	}



}
