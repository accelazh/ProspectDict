package guestUserInterface;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.*;

import com.ibm.iwt.util.*;
import com.ibm.iwt.window.*;

import java.awt.event.*;

import guestUserInterface.magneticWindows.*;

/**
 * 
 * @author ZYL
 * 这个类负责字典的Frame
 *
 */
public class DictFrame extends MagIFrameForSpecialUse 
{
	public static final Dimension TITLEBAR_SIZE=new Dimension(WordPanel.DEFAULT_SIZE.width,30);
	
	private static final Dimension FRAME_SIZE=new Dimension(500,400);
	private boolean isMaxified=false;  //标记窗口是否被最大化
	private Dimension screenSize;
	
	//WordPanel 的引用,这个变量会在wordPanel的初始化过程中，由wordPanel来赋值
	private WordPanel wordPanel;
	
	//这个量只在开始的时候用一次，因为如果在开始的时候要求关掉freeMoving效果时，
	//无法将wordPanel.wordListDialog和wordPanel.wordInfoDialog设为
	//setVisible(false),故此时需要借助这个变量，在开始的时候不将它们的visible
	//设为true
	private boolean shunDownFreeMovingWhenStart=false;
	
	public DictFrame()
	{
		super();
		setSize(WordPanel.DEFAULT_SIZE.width, WordPanel.BAR_SIZE+WordPanel.BUTTON_SIZE.height);
		setTitle("Window");
		IWTUtilities.setApplicationBorderSize(this, new Insets(0, 7, 7, 7));
		IWTUtilities.setDiagonalSize(20);
		getIContentPane().setBackground(new Color(255, 255, 255));
		setTitleBar(new TitleBar());
		
		screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-FRAME_SIZE.width)/2,(screenSize.height-FRAME_SIZE.height)/2);
		
		//方便用户覆盖以指定启动哪一个wordPanel的方法
		wordPanel=createWordPanel();
		
        getContentPane().add(wordPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Simple Dictionary");
		
		wordPanel.getWordListDialog().getGroup().add(this);
		this.setGroup(wordPanel.getWordListDialog().getGroup());
		
		setAlwaysOnTop(wordPanel.isAlwaysOnTop());
		
		if (wordPanel!=null)
		{
			if (wordPanel.getWarningDialog() == null)
			{
				setVisible(true);
				if (!shunDownFreeMovingWhenStart)
				{
					wordPanel.getWordListDialog().setVisible(true);
					wordPanel.getWordInfoDialog().setVisible(true);
				}
			}
		}
		else
		{
			setVisible(true);
			if (!shunDownFreeMovingWhenStart)
			{
				wordPanel.getWordListDialog().setVisible(true);
				wordPanel.getWordInfoDialog().setVisible(true);
			}
		}

	}
	
	/**
	 * 这个方法在wordPanel.setFreeMoveOpen(false)的时候使用，
	 * 功能是将各个窗体归为，连接起来外观上成为一个窗体
	 */
	public void repositionWindows()
	{
		if(wordPanel!=null)
		{
			WordPanel.WordListDialog wordListDialog=wordPanel.getWordListDialog();
			WordPanel.WordInfoDialog wordInfoDialog=wordPanel.getWordInfoDialog();
			
			if(wordListDialog!=null)
			{
				wordListDialog.setLocation(wordListDialog.getRelativeLocationToWordPanel().x+getLocation().x, 
						wordListDialog.getRelativeLocationToWordPanel().y+WordPanel.BAR_SIZE+getLocation().y);
			}
			
			if(wordInfoDialog!=null)
			{
				wordInfoDialog.setLocation(wordInfoDialog.getRelativeLocationToWordPanel().x+getLocation().x, 
						wordInfoDialog.getRelativeLocationToWordPanel().y+WordPanel.BAR_SIZE+getLocation().y);
			}
		}
		
		repaint();
	}
	/**
	 * 这个方法关掉freeMoving,即分离窗口效果
	 */
	public void shutDownFreeMoving()
	{
		if(wordPanel!=null)
		{
			//将两个副窗体的magEnable关掉,并设为不可见,并删除它们的wordList和wordInfo
			WordPanel.WordListDialog wordListDialog=wordPanel.getWordListDialog();
			WordPanel.WordInfoDialog wordInfoDialog=wordPanel.getWordInfoDialog();
			
			if(wordListDialog!=null)
			{
				 wordListDialog.setMagEnable(false);
				 shunDownFreeMovingWhenStart=true;
				 wordListDialog.setVisible(false);
				 wordListDialog.getWordListReservedInDialog().removeAll();
				 wordListDialog.getWordListReservedInDialog().setVisible(false);
				 wordListDialog.validate();
			}
			
			if(wordInfoDialog!=null)
			{
				wordInfoDialog.setMagEnable(false);
				shunDownFreeMovingWhenStart=true;
				wordInfoDialog.setVisible(false);
				wordInfoDialog.getWordInfoReservedInDialog().removeAll();
				wordInfoDialog.getWordInfoReservedInDialog().setVisible(false);
				wordInfoDialog.validate();
			}
		
		    //将wordList和wordInfo加入自己
		    wordPanel.getWordListReserved().add(wordPanel.getWordList(), BorderLayout.CENTER);
		    wordPanel.getWordListReserved().setVisible(true);
		    wordPanel.getWordInfoReserved().add(wordPanel.getWordInfo(), BorderLayout.CENTER);		    
		    wordPanel.getWordInfoReserved().setVisible(true);
		    
		    //调整自己的大小
		    setSize(FRAME_SIZE);
		
		    //重新布局
		    validate();
		}
	}
	
	/**
	 * 这个方法开启freeMoving,即分离效果
	 */
	public void openFreeMoving()
	{
		if(wordPanel!=null)
		{
			//将wordList和wordInfo从自己中删除
		    wordPanel.getWordListReserved().removeAll();
		    wordPanel.getWordListReserved().setVisible(false);
		    wordPanel.getWordInfoReserved().removeAll();		
		    wordPanel.getWordInfoReserved().setVisible(false);		
			
		    //调整自己的大小
		    setSize(WordPanel.DEFAULT_SIZE.width, WordPanel.BAR_SIZE+WordPanel.BUTTON_SIZE.height);
		
		    //检查此时是否是最大化的
		    if(isMaxified)
		    {
		    	DictFrame.this.setLocation(DictFrame.this.getLocation().x,(screenSize.height-FRAME_SIZE.height)/2);
				isMaxified=false;
				wordPanel.demaxified();
		    }
		    
		    //重新布局
		    validate();
		    
		    //将两个副窗体的magEnable打开,并设为可见,并加入wordList和wordInfo，并重新布局
			WordPanel.WordListDialog wordListDialog=wordPanel.getWordListDialog();
			WordPanel.WordInfoDialog wordInfoDialog=wordPanel.getWordInfoDialog();
			
			if(wordListDialog!=null)
			{
				wordListDialog.getWordListReservedInDialog().add(wordPanel.getWordList());
				wordListDialog.getWordListReservedInDialog().setVisible(true);
				wordListDialog.setMagEnable(true);
				wordListDialog.validate();			 
			}
			
			if(wordInfoDialog!=null)
			{
				wordInfoDialog.getWordInfoReservedInDialog().add(wordPanel.getWordInfo());
				wordInfoDialog.getWordInfoReservedInDialog().setVisible(true);
				wordInfoDialog.setMagEnable(true);
				wordInfoDialog.validate();
			}
			
			if(wordListDialog!=null&&wordInfoDialog!=null)
			{
				repositionWindows();
				wordListDialog.setVisible(true);
				wordInfoDialog.setVisible(true);
			}
					   
		}
		
	}
	
	private class TitleBar extends IWindowTitleBar
	{
		
		public TitleBar()
		{
			setPreferredSize(TITLEBAR_SIZE);
			setFont(new Font("Verdana", Font.BOLD, 22));
			setResizable(false);
			removeWindowDecorations();
		}
		
		public boolean isTransparent(int x, int y)
		{
			if ( (x < (int)getWidth()*.1 || x > (int)getWidth()*.9) &&
				y < 10)
				return true;
			return false;
			
		}
		
		protected void paintComponent(Graphics g)
		{
            super.paintComponent(g);
			setTransparent(this, g, 0, 0, TITLEBAR_SIZE.width,TITLEBAR_SIZE.height);
			g.drawImage(Toolkit.getDefaultToolkit().getImage("GUISource/titleBar.png"), 
					0,0,TITLEBAR_SIZE.width,TITLEBAR_SIZE.height, this);
		}

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if (!isInsideTitleBar(e.getX(), e.getY()))
				return;
			
			if(wordPanel.isFreeMoveOpen())
			{
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
			    {
         			repositionWindows();				
			    }
			}
			else
			{
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
			    {
					if(!isMaxified)
					{
						DictFrame.this.setLocation(DictFrame.this.getLocation().x,0);
					    DictFrame.this.setSize(DictFrame.this.getSize().width,Toolkit.getDefaultToolkit().getScreenSize().height-27);
			            isMaxified=true;
			            wordPanel.maxified();
					}
					else
					{
						DictFrame.this.setSize(FRAME_SIZE);
						DictFrame.this.setLocation(DictFrame.this.getLocation().x,(screenSize.height-FRAME_SIZE.height)/2);
						isMaxified=false;
						wordPanel.demaxified();
					}
         	    }
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) 
		{
			if (!isMaxified)
			{
				if (wordPanel != null && wordPanel.isFreeMoveOpen())
				{
					dragMoving(e.getPoint());
				}
				else 
				{
					dragMovingOnlyScreenSensitive(e.getPoint());
				}
			}
			else
			{
				dragMovingWhenMaxified(e.getPoint());		
			}	
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{
        	mouseReleasedProcess(e);
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			mousePressedProcess(e);
		}
		
	}
	
	@Override
	public void windowIconified(WindowEvent e) 
	{
		super.windowIconified(e);
		
		if(wordPanel!=null)
		{
			if(wordPanel.getWordListDialog()!=null)
			{
				wordPanel.getWordListDialog().setVisible(false);
			}
			if(wordPanel.getWordInfoDialog()!=null)
			{
				wordPanel.getWordInfoDialog().setVisible(false);
			}
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) 
	{
		super.windowDeactivated(e);
		
		if(wordPanel!=null)
		{
			if(wordPanel.getWordListDialog()!=null)
			{
				if(wordPanel.isFreeMoveOpen())
				{
					wordPanel.getWordListDialog().setVisible(true);
				}
			}
			if(wordPanel.getWordInfoDialog()!=null)
			{
				if(wordPanel.isFreeMoveOpen())
				{
					wordPanel.getWordInfoDialog().setVisible(true);
			    }
			}
		}
	}

	public void setWordPanel(WordPanel wordPanel) {
		this.wordPanel = wordPanel;
	}
	
	/**
	 * 这个方法给覆盖本类的用户使用。当已经扩展了
	 * WordPanel后，需要扩展DictFrame并覆盖这
	 * 个方法，以指定启动用户扩展的新WordPanel的
	 * 子类
	 * @return 要启动的WordPanel实例
	 * 
	 */
    protected WordPanel createWordPanel()
    {
    	return new WordPanel(this);
    }
}


