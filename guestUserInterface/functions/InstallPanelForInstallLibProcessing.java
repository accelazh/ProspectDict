package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

import libraryInterface.*;

/**
 * 这个类用于制作安装界面，负责restore的面板之一
 * @author ZYL
 *
 */
public class InstallPanelForInstallLibProcessing extends InstallerPanelSuper
{
    public static final String LABEL_INFO="Install Library File Processing. Please wait... \n";
    public static final String WARNING_INFO="    WARNING: If Exit When Unfinished, the Library is Very Possible to be DAMAGED! ";
 	
	private JTextArea label=new JTextArea(LABEL_INFO);
	private JTextArea warning=new JTextArea(WARNING_INFO);
	
	private JProgressBar progressBar=new JProgressBar();
	
	private Thread processThread;   //负责主进程
	private JLabel progressLabel=new JLabel("Starting...");   //负责显示进度位置
	private Thread barThread;  //负责更新进度条
	
	private LibraryFileInputWrap inWrap;
	
	private Installer installer;
	
	private boolean finish=false;
	
	//用来制作提示动画
	private int counter=0;  
	private int nOfDots=0;
	private JLabel dotLabel=new JLabel();
	
	public InstallPanelForInstallLibProcessing(LibraryFileInputWrap inWrap)
	{
		super();
		
		this.inWrap=inWrap;
		
		//初始化组件
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		label.setFont(new Font("Times",Font.BOLD,14));
		label.setBackground(getContentPanel().getBackground());
		
		warning.setLineWrap(true);
		warning.setWrapStyleWord(true);
		warning.setEditable(false);
		warning.setForeground(Color.RED);
		warning.setFont(new Font("Times",Font.BOLD,16));
		warning.setBackground(getContentPanel().getBackground());
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(1000);
		progressBar.setValue(0);
		progressBar.setForeground(new Color(30,142,255));
		progressBar.setBackground(Color.WHITE);
		progressBar.setStringPainted(true);
		
		getBackButton().setEnabled(false);
		getNextButton().setText("Finish");
		getNextButton().setEnabled(false);
		getCancelButton().setVisible(true);
		
		//加入组件
		getContentPanel().setLayout(new BorderLayout(5,10));
		getContentPanel().add(label,BorderLayout.NORTH);
		
		JPanel p2=new JPanel(new BorderLayout(5,5));
		p2.add(warning,BorderLayout.CENTER);
		p2.add(dotLabel, BorderLayout.SOUTH);
		getContentPanel().add(p2, BorderLayout.CENTER);
		
		JPanel p1=new JPanel(new BorderLayout(5,5));
		p1.add(progressLabel,BorderLayout.NORTH);
		p1.add(progressBar, BorderLayout.CENTER);
		getContentPanel().add(p1,BorderLayout.SOUTH);
	    	
	}

	/**
	 * 这个方法将开始这个界面的处理任务
	 */
	public void startThreads()
	{
		Library.resetProcessValues();
		progressBar.setValue(0);
	    finish=false;
		
		//设置线程
        processThread=new Thread(new ProcessThreadRun());
		
		barThread=new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					double progress=(Library.getProgressOfCopyLibraryFiles()
		    				+Library.getProgressOfReadWordEntry()
		    				+Library.getProgressOfRawLibSplit()
		    				+Library.getProgressOfInsert()
		    				+Library.getProgressOfUnifyLength()
		    				+Library.getProgressOfWriteWordEntry())/6;
		    		
				    EventQueue.invokeLater(new Runnable(){
					    public void run()
				    	{
				    		double progress=(Library.getProgressOfCopyLibraryFiles()
				    				+Library.getProgressOfReadWordEntry()
				    				+Library.getProgressOfRawLibSplit()
				    				+Library.getProgressOfInsert()
				    				+Library.getProgressOfUnifyLength()
				    				+Library.getProgressOfWriteWordEntry())/6;
				    		
				    		progressBar.setValue((int)(progress*progressBar.getMaximum()));
				    	
				    		//刷新processLabel
				    		if(Library.getProgressOfCopyLibraryFiles()<1e-6)
				    		{
				    			progressLabel.setText("Starting...");
				    		}
				    		else
				    		{
				    			if(Library.getProgressOfReadWordEntry()<1e-6)
				    			{
				    				progressLabel.setText("Backuping library files...");
				    			}
				    			else
				    			{
				    				if(Library.getProgressOfRawLibSplit()<1e-6)
				    				{
				    					progressLabel.setText("Reading word entries...");
				    				}
				    				else
				    				{
				    					if(Library.getProgressOfInsert()<1e-6)
				    					{
				    						progressLabel.setText("Analizing new library file...");
				    					}
				    					else
				    					{
				    						if(Library.getProgressOfUnifyLength()<1e-6)
				    						{
				    							progressLabel.setText("Inserting repeated entries...");
				    						}
				    						else
				    						{
				    							if(Library.getProgressOfWriteWordEntry()<1e-6)
				    							{
				    								progressLabel.setText("Unifying line length...");
				    							}
				    							else
				    							{
				    								progressLabel.setText("Writing word entries...");
				    							}
				    						}
				    					}
				    				}
				    			}
				    		}
				    		
				    	}
				    });
				    
				    try
				    {
				    	Thread.sleep(100);
				    }
				    catch(InterruptedException ex)
				    {
				    	
				    }
				    
		    		if(progress>0.99||finish)
		    		{
		    			progressBar.setValue(progressBar.getMaximum());
		    			System.out.println("bar Thread finished!");
		    			break;
		    		}
		    		
		    		//提示动画
		    		EventQueue.invokeLater(new Runnable(){
		    			public void run()
		    			{
		    				if(counter>=5)
				    		{
				    			counter=0;
				    			
				    			nOfDots=(nOfDots+1)%15;
				    			String s="";
				    			for(int i=0;i<nOfDots;i++)
				    			{
				    				s+="●　";
				    			}
				    			dotLabel.setText(s);
				    		}
					        counter++;
		    			}
		    			
		    		});
		    	}
			}
		});
		
		processThread.start();
		barThread.start();
	}
	
	public void stopThreads()
	{
		processThread.stop();
		barThread.stop();
		Library.resetProcessValues();
	}
	
	@Override
	public void backButtonPressed() 
	{
		installer.getCardLayout().show(installer, "installPanelInput");
	}

	@Override
	public void nextButtonPressed()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void cancel()
	{
		stopThreads();
		getBackButton().setEnabled(true);
	}

	/**
	 * 当处理完成的时候调用的方法
	 */
	public void finished()
	{
		System.out.println("Processing finished!");
		installer.getCardLayout().show(installer, "installPanelForInstallLibFinished");
	    finish=true;
	}
	
	private class ProcessThreadRun implements Runnable
	{
		public void run()
		{
			if(null==inWrap)
			{
				System.out.println("null inWrap, exit");
				return;
			}
			
			System.out.println("process thread run");
			if(null==Library.createLibraryFile(inWrap.getLibFile(), inWrap.getSwt(), inWrap.getSee(), inWrap.getFrom(), inWrap.getTo(), inWrap.getDictName()))
			{
				JOptionPane.showMessageDialog(InstallPanelForInstallLibProcessing.this,"Error occurs when installing!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			finished();
		}
	}

	public LibraryFileInputWrap getInWrap() {
		return inWrap;
	}

	public void setInWrap(LibraryFileInputWrap inWrap) {
		this.inWrap = inWrap;
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}
	

}
