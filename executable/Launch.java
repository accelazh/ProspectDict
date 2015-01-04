package executable;

import guestUserInterface.DictFrame;
import guestUserInterface.personalWordBook.PersonalWordPanel;

import javax.swing.*;
import javax.swing.plaf.synth.*;
import java.io.*;


/**
 * 
 * @author ZYL
 * 这个类用来运行，启动字典程序
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
			//设置可更换外观
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
