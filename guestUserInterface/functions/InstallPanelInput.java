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
public class InstallPanelInput extends InstallerPanelSuper
implements MouseListener
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
	
	private JLabel title=new JLabel("Input information: ");
	private JFileChooser fc=new JFileChooser();
	
	private JLabel infoLabel=new JLabel("Help info.");
	
	private Installer installer;
	
	public InstallPanelInput()
	{
		getContentPanel().setLayout(new BorderLayout(5,15));
		
		//initial components
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new TextFileFilter());
		
		libFile.setToolTipText("Select the file of the library to be added. ");
		from.setToolTipText("what language the new library translates from. ");
		to.setToolTipText("what language the new library translates to. ");
		swt.setToolTipText("You can use \' \\ n \' and \' \\ r \' for Enters who have ASCII codes of 10 and 13, and use \' \\ t \' for tab. ");
		see.setToolTipText("You can use \' \\ n \' and \' \\ r \' for Enters who have ASCII codes of 10 and 13, and use \' \\ t \' for tab. ");
		dictName.setToolTipText("You can give the new library a name. ");
		
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
		
		//add listeners
		for(int i=0;i<textFields.length;i++)
		{
			textFields[i].addMouseListener(this);
		}
	}

	/**
	 * 这个方法依次检查各个输入的正确性，如果某个输入不正确，则弹出
	 * 提示，并返回false
	 * 
	 */
	public boolean checkValid()
	{
		//check libFile
		File file=new File(libFile.getText());
		if(null==file)
		{
			JOptionPane.showMessageDialog(this, "Choose a right library file, please.");
			return false;
		}
		
		if(!file.exists())
		{
			JOptionPane.showMessageDialog(this, "Choose a right library file, please.");
			return false;
		}
		
		if(!(new TextFileFilter().acceptExcludeDirectory(file)))
		{
			JOptionPane.showMessageDialog(this, "Choose a right library file, please.");
			return false;
		}
		
		//check from
		if(!checkNameValid(from.getText()))
		{
			JOptionPane.showMessageDialog(this, "Sorry, illegal \"from\".");
			return false;
		}
		
		//check from
		if(!checkNameValid(to.getText()))
		{
			JOptionPane.showMessageDialog(this, "Sorry, illegal \"to\".");
			return false;
		}
		
		//check swt
		if(getChar(swt.getText())<=0)
		{
			JOptionPane.showMessageDialog(this, "Sorry, illegal \"separator\".");
			return false;
		}
		System.out.println((int)getChar(swt.getText()));
		
	    //check see
		if(getChar(see.getText())<=0)
		{
			JOptionPane.showMessageDialog(this, "Sorry, illegal \"separator\".");
			return false;
		}
		
		if(getChar(see.getText())==getChar(swt.getText()))
		{
			JOptionPane.showMessageDialog(this, "Sorry, two \"separator\"s can not be the same.");
			return false;
		}
		System.out.println((int)getChar(see.getText()));
		
		
		//check dictName
		if(dictName.getText().length()!=0)
		{
			if(!checkNameValid(dictName.getText()))
			{
				JOptionPane.showMessageDialog(this, "Sorry, illegal \"name\".");
				return false;
			}
		}
		
		//finally
		return true;
		
	}
	
	public LibraryFileInputWrap getLibraryFileInputWrap()
	{
		return new LibraryFileInputWrap(new File(libFile.getText()),from.getText(),to.getText(),(char)getChar(swt.getText()),(char)getChar(see.getText()),dictName.getText());
	}
	
	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer, "installPanelChoose");
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextButtonPressed() 
	{
		if(!checkValid())
		{
			return;
		}
		
		System.out.println("check pass");
		
		installer.getInstallPanelInputConfirm().setInWrap(getLibraryFileInputWrap());
		installer.getCardLayout().show(installer, "installPanelInputConfirm");
	}

	
    public void mouseClicked(MouseEvent e)
    {
		if(e.getSource()==libFile)
		{
			System.out.println(fc.getSelectedFile());
			File file=fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION?fc.getSelectedFile():null;
	 		if(file!=null)
	 		{
	 			libFile.setText(file.getAbsolutePath());
	 		}
		}
		
	}

	public void mouseEntered(MouseEvent e) 
	{
		if(e.getSource()==libFile)
		{
			infoLabel.setText("Select the file of the library to be added. ");
		}
		
		if(e.getSource()==from)
		{
			infoLabel.setText("what language the new library translates from. ");
		}
		
		if(e.getSource()==to)
		{
			infoLabel.setText("what language the new library translates to. ");
		}
		
		if(e.getSource()==swt)
		{
			infoLabel.setText("Separator of a word and its translation in the new library. "); 
					
		}
		
		if(e.getSource()==see)
		{
			infoLabel.setText("Separator of one entry and another in the new library. ");
		}
		
		if(e.getSource()==dictName)
		{
			infoLabel.setText("You can give the new library a name. ");
		}
		
	}
	
	private boolean checkNameValid(String s)
	{
		if(null==s||0==s.length())
		{
			return false;
		}
		
		if(!s.equals(s.trim()))
		{
			return false;
		}
		
		for(int i=0;i<s.length();i++)
		{
			if(!Library.isLegalCharOfName(s.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	//这个方法用于从textField获得输入的字符，如果输入不正确，则返回-1
	private int getChar(String s)
	{
		if(null==s||0==s.length())
		{
			return -1;
		}
		
		if(1==s.length())
		{
			if(s.charAt(0)>127)
			{
				return -1;
			}
			else
			{
				return s.charAt(0);
			}
		}
		
		if(2==s.length())
		{
			if(s.charAt(0)!='\\')
			{
				return -1;
			}
			else
			{
				if('n'==s.charAt(1))
				{
					return '\n';
				}
				
				if('r'==s.charAt(1))
				{
					return '\r';
				}
				
				if('t'==s.charAt(1))
				{
					return '\t';
				}
			}
		}
		
		return -1;
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelInput());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		System.out.println((int)('\n'));
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	
}
