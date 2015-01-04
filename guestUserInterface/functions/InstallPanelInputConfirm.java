package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.File;

import javax.swing.border.*;

import settings.*;
import libraryInterface.*;

/**
 * 这个类负责生成词库安装界面的输入界面
 * @author ZYL
 *
 */
public class InstallPanelInputConfirm extends InstallerPanelSuper
{
	private JTextField libFile=new JTextField(); //where your library is
	private JTextField from=new JTextField(); //what language your library translates from 
	private JTextField to=new JTextField();   //what language your library translates to
	private JTextField swt=new JTextField();  //separator of word and its translation
	private JTextField see=new JTextField();  //separator of entry and entry
	private JTextField dictName=new JTextField();   //the name for your dict library
		
	private JLabel libFileLabel=new JLabel("File"); 
	private JLabel fromLabel=new JLabel("Translate from"); 
	private JLabel toLabel=new JLabel("Translate to");   
	private JLabel swtLabel=new JLabel("Separator of a word and its translation");  
	private JLabel seeLabel=new JLabel("Separator of one entry and one another");  
	private JLabel dictNameLabel=new JLabel("Library name");   
	
	private JTextField[] textFields={
			libFile,
			from,
			to,
			swt,
			see,
			dictName,
	};
	
	private JLabel[] labels={
			libFileLabel,
			fromLabel,
			toLabel,
			swtLabel,
			seeLabel,
			dictNameLabel,
	};
	
	private JLabel title=new JLabel("Confirm your input: ");
	private JLabel infoLabel=new JLabel("Click next and begin to process, it takes some time. ");
	
	private LibraryFileInputWrap inWrap;
	
	private Installer installer;
	
	public InstallPanelInputConfirm()
	{
		this(null);
	}
	
	public InstallPanelInputConfirm(LibraryFileInputWrap inWrap)
	{
		this.inWrap=inWrap;
		getContentPanel().setLayout(new BorderLayout(5,15));
		
		//initial components
		libFile.setToolTipText("Select the file of the library to be added. ");
		from.setToolTipText("what language the new library translates from. ");
		to.setToolTipText("what language the new library translates to. ");
		swt.setToolTipText("You can use \' \\ n \' and \' \\ r \' for Enters who have ASCII codes of 10 and 13, and use \' \\ t \' for tab. ");
		see.setToolTipText("You can use \' \\ n \' and \' \\ r \' for Enters who have ASCII codes of 10 and 13, and use \' \\ t \' for tab. ");
		dictName.setToolTipText("You can give the new library a name. ");
		
		for(int i=0;i<labels.length;i++)
		{
			textFields[i].setEditable(false);
		}
		
		title.setForeground(Color.BLUE);
		infoLabel.setForeground(Color.RED);
		
		//process inWrap
		if(inWrap!=null)
		{
			libFile.setText(inWrap.getLibFile()!=null?inWrap.getLibFile().getAbsolutePath():"");
			from.setText(inWrap.getFrom()!=null?inWrap.getFrom():"");
			to.setText(inWrap.getTo()!=null?inWrap.getTo():"");
			dictName.setText(inWrap.getDictName()!=null?inWrap.getDictName():"");
		
			swt.setText(getStrFromChar(inWrap.getSwt()));
			see.setText(getStrFromChar(inWrap.getSee()));
		}
		
		//addComponents
		JPanel p1=new JPanel(new BorderLayout());
		p1.add(title,BorderLayout.CENTER);
		getContentPanel().add(p1,BorderLayout.NORTH);
		
		JPanel p2=new JPanel(new GridLayout(6,1,5,10));
		JPanel[] subp2=new JPanel[labels.length];
		for(int i=0;i<subp2.length;i++)
		{
			subp2[i]=new JPanel(new BorderLayout(10, 5));
		    subp2[i].add(textFields[i],BorderLayout.CENTER);
		    subp2[i].add(labels[i],BorderLayout.EAST);
		    
		    p2.add(subp2[i]);
		}
		getContentPanel().add(p2,BorderLayout.CENTER);
		
		JPanel p3=new JPanel(new BorderLayout());
		p3.add(infoLabel, BorderLayout.CENTER);
		getContentPanel().add(p3,BorderLayout.SOUTH);
		
	}
	
	public void setInWrap(LibraryFileInputWrap inWrap) 
	{
		this.inWrap = inWrap;
		
		if(inWrap!=null)
		{
			libFile.setText(inWrap.getLibFile()!=null?inWrap.getLibFile().getAbsolutePath():"");
			from.setText(inWrap.getFrom()!=null?inWrap.getFrom():"");
			to.setText(inWrap.getTo()!=null?inWrap.getTo():"");
			dictName.setText(inWrap.getDictName()!=null?inWrap.getDictName():"");
		
			swt.setText(getStrFromChar(inWrap.getSwt()));
			see.setText(getStrFromChar(inWrap.getSee()));
		}
	}


	//这个方法用于从char中获得字符串，自动将'\n','\r','\t'转化
	private String getStrFromChar(char c)
	{
		if(c=='\n')
		{
			return "\\n";
		}
		
		if(c=='\r')
		{
			return "\\r";
		}
		
		if(c=='\t')
		{
			return "\\t";
		}
		
		return ""+c;
	}
	
	public LibraryFileInputWrap getInWrap() {
		return inWrap;
	}
	
	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer, "installPanelInput");
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextButtonPressed() 
	{
		installer.getInstallPanelForInstallLibProcessing().getBackButton().setEnabled(false);
		installer.getInstallPanelForInstallLibProcessing().setInWrap(inWrap);
		installer.getCardLayout().show(installer, "installPanelForInstallLibProcessing");
	    installer.getInstallPanelForInstallLibProcessing().startThreads();
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelInputConfirm());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		System.out.println((int)('\n'));
		
		System.out.println(""+"|"+(char)(97)+"|");
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	
	
}
