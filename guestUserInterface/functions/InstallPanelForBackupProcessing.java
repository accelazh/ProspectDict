package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.swing.border.*;

import settings.*;

import libraryInterface.*;

/**
 * ���������������װ���棬����restore�����֮һ
 * @author ZYL
 *
 */
public class InstallPanelForBackupProcessing extends InstallerPanelSuper
{
    public static final String LABEL_INFO="Back-up Processing. Please wait... \n";
    public static final String WARNING_INFO="    WARNING: If Exit When Unfinished, the Back-up Files are Very Possible to be DAMAGED! ";
 	
	private JTextArea label=new JTextArea(LABEL_INFO);
	private JTextArea warning=new JTextArea(WARNING_INFO);
	
	private JProgressBar progressBar=new JProgressBar();
	
	private Thread processThread;   //����������
	private Thread barThread;  //������½�����
	
    private Installer installer;
	   
    private boolean finish=false;
    
	public InstallPanelForBackupProcessing()
	{
		super();
		
		//��ʼ�����
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
		
		//�������
		getContentPanel().setLayout(new BorderLayout(5,10));
		getContentPanel().add(label,BorderLayout.NORTH);
		getContentPanel().add(warning,BorderLayout.CENTER);
		getContentPanel().add(progressBar,BorderLayout.SOUTH);
	    
	}

	/**
	 * �����������ʼ�������Ĵ�������
	 */
	public void startThreads()
	{
		Library.resetProcessValues();
		progressBar.setValue(0);
		finish=false;
		
		//�����߳�
		processThread=new Thread(new Runnable(){
			public void run()
			{
				System.out.println("process thread run");
				if(!Library.backupLibraryFiles())
				{
					JOptionPane.showMessageDialog(InstallPanelForBackupProcessing.this,"Error occurs when backuping!", "Error", JOptionPane.ERROR_MESSAGE);
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
				    		progressBar.setValue((int)(Library.getProgressOfCopyLibraryFiles()*progressBar.getMaximum()));
				    	}
				    });
				    
				    try
				    {
				    	Thread.sleep(100);
				    }
				    catch(InterruptedException ex)
				    {
				    	
				    }
				    
		    		if(Library.getProgressOfCopyLibraryFiles()>0.99||finish)
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
		installer.getCardLayout().show(installer, "installPanelInfoForBackup");
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
	 * ��������ɵ�ʱ����õķ���
	 */
	public void finished()
	{
		System.out.println("Processing finished!");
		installer.getCardLayout().show(installer, "installPanelForBackupFinished");
	    finish=true;
	}
	
	//test
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();
		frame.getContentPane().add(new InstallPanelForBackupProcessing());
		
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
