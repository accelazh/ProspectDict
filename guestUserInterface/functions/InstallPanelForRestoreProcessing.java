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
public class InstallPanelForRestoreProcessing extends InstallerPanelSuper
{
    public static final String LABEL_INFO="Restore Processing. Please wait... \n";
    public static final String WARNING_INFO="    WARNING: If Exit When Unfinished, the Library is Very Possible to be DAMAGED! ";
 	
	private JTextArea label=new JTextArea(LABEL_INFO);
	private JTextArea warning=new JTextArea(WARNING_INFO);
	
	private JProgressBar progressBar=new JProgressBar();
	
	private Thread processThread;   //负责主进程
	private Thread barThread;  //负责更新进度条
	
    private Installer installer;
	
    private boolean finish=false;;
    
	public InstallPanelForRestoreProcessing()
	{
		super();
		
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
		getCancelButton().setVisible(true);
		getNextButton().setText("Finish");
		getNextButton().setEnabled(false);
		
		//加入组件
		getContentPanel().setLayout(new BorderLayout(5,10));
		getContentPanel().add(label,BorderLayout.NORTH);
		getContentPanel().add(warning,BorderLayout.CENTER);
		getContentPanel().add(progressBar,BorderLayout.SOUTH);
	    
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
		processThread=new Thread(new Runnable(){
			public void run()
			{
				System.out.println("process thread run");
				if(!Library.switchLibraryFilesInBackupAndCurrent())
				{
					JOptionPane.showMessageDialog(InstallPanelForRestoreProcessing.this,"Error occurs when restore!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				finished();
			}
		});
		
		barThread=new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					System.out.println("bar thread run");
				    EventQueue.invokeLater(new Runnable(){
					    public void run()
				    	{
				    		System.out.println("refresh process bar run");
				    		progressBar.setValue((int)((Library.getProgressOfSwitchLibraryFiles()*1.0/3+1.0/3*Library.getProgressOfCopyLibraryFiles())*progressBar.getMaximum()));
				    	}
				    });
				    
				    try
				    {
				    	Thread.sleep(100);
				    }
				    catch(InterruptedException ex)
				    {
				    	
				    }
				    
		    		if((Library.getProgressOfSwitchLibraryFiles()*1.0/3+1.0/3*Library.getProgressOfCopyLibraryFiles())>0.99||finish)
		    		{
		    			progressBar.setValue(progressBar.getMaximum());
		    			break;
		    		}
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
		installer.getCardLayout().show(installer, "installPanelInfoForRestore");
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
		Library.refreshLibraryAccountor();
		System.out.println("Processing finished!");
		installer.getCardLayout().show(installer, "installPanelForRestoreFinished");
	    finish=true;
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelForRestoreProcessing());
		
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

}
