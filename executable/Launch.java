package executable;

import guestUserInterface.DictFrame;
import guestUserInterface.personalWordBook.PersonalWordPanel;

import javax.swing.*;
import javax.swing.plaf.synth.*;
import java.io.*;


/**
 * 
 * @author ZYL
 * ������������У������ֵ����
 *
 */
public class Launch 
{
	private Launch()
	{
		
	}
	
	static 
	{
		try
		{
			//���ÿɸ������
			SynthLookAndFeel synth = new SynthLookAndFeel();
			synth.load(new FileInputStream("GUISource\\dictFace.xml"),Launch.class);
			UIManager.setLookAndFeel(synth);
			
    	}
		catch(Exception ex)
		{
			System.out.println(ex);			
		}
		
	}
	public static void main(String[] args)
	{
		new DictFrame();
	}
	
}
