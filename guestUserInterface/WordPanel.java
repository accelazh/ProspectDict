package guestUserInterface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import libraryManager.*;
import settings.*;
import libraryInterface.*;

import java.util.*;
import guestUserInterface.firework.*;

import java.io.*;

import conLibraryManager.*;
import guestUserInterface.magneticWindows.*;

import guestUserInterface.personalWordBook.*;

/**
 * 
 * @author ZYL
 * 负责查单词界面的JPanel
 *
 */
public class WordPanel extends JPanel
implements Constants, ActionListener, ListSelectionListener, KeyListener, MouseWheelListener,MouseListener,MouseMotionListener
{
	private static final long serialVersionUID=0;
	
	//for debug
	public static final boolean debug=false;
	public static final boolean debug2=false;
    public static final boolean debug3=false;
    public static final boolean debug4=false;
	
	//data field
	//这些常量控制组件的位置及大小
	public static final int BAR_SIZE=31;
	public static final Dimension DEFAULT_SIZE=new Dimension(500,400);
	public static final Dimension BUTTON_SIZE=new Dimension(70,40);
	public static final Dimension WORD_LIST_SIZE=new Dimension(140,DEFAULT_SIZE.height-BAR_SIZE-BUTTON_SIZE.height);
	public static final Dimension WORD_INFO_SIZE=new Dimension(DEFAULT_SIZE.width-WORD_LIST_SIZE.width,WORD_LIST_SIZE.height);
	public static final Dimension TYPE_IN_WORD_SIZE=new Dimension(430,40);
	
	//词库管理者
    private ConLibraryManager conLibraryManager=null;
    
	//单词输入的相关组件
	private JButton buttonGo=new JButton("");
	private JTextField typeInField=new JTextField("");
	
	//单词列表
	private JList wordList=new JList(new String[]{"none"});
	private WordEntry[] wordListItems;
		
	//用于书写单词的解释的区域
	private JTextArea wordInfo=new JTextArea();
		
	//各组件的镂空底色
	private Color sharedBackground=SHARED_BACKGROUND;
	
	//背景图
	private ImageIcon backgroundImgMask=new ImageIcon("GUISource\\backgroundMask.png");
	private boolean paintBkMask=true;   //这个量用来指明是否画上backgroundImgMask
	private ImageIcon backgroundImg=null;
	private boolean activeBackground=false; //这个量指明背景的显示模式
	private int currentBk=0;   //当前的背景图
	private int numOfBks=0;   //背景图的总数
	private ImageIcon backgroundImgMaskWhenMaxified=new ImageIcon("GUISource\\backgroundImgMaskWhenMaxified.png"); 
	
	//标记是否最大化了
	private boolean isMaxified=false;
	
	//音效
	private SoundPlayer clickClip=new SoundPlayer(WordPanel.class.getResource("buttonClick.wav"));
	private SoundPlayer onClip=new SoundPlayer(WordPanel.class.getResource("buttonMouseOn.wav"));
	private SoundPlayer releaseClip=new SoundPlayer(WordPanel.class.getResource("buttonReleased.wav"));
	
	//是否开启网络查找功能
	private boolean openNetSearch=true;
	
	//弹出菜单
	private MainPopupMenu mainPopupMenu;
	
	//==============================
	//==下面这个区域是负责实现烟花功能的==
	//==============================
	private ArrayList<Firework> fireworks=new ArrayList<Firework>();
	private double fai=Math.PI/2;   //这两个量描述坐标平面法向量
	private double thita=Math.PI; 
	private static final double distance=600;
	private PlaneCoordinateSystemIn3D plane;
	private static final int OFFSET=100;
	private Dimension panelSize=new Dimension(DEFAULT_SIZE.width,Toolkit.getDefaultToolkit().getScreenSize().height-27);
	
	//相应鼠标拖动
	private double step=1.0/180*Math.PI;//控制移动步长
	
	//开关
	private boolean fireworkOpen=false;
	private boolean fireworkEnable=true;  //控制最大化的时候是否自动打开烟花效果
	
	//烟花旋转开关
	private boolean rotate=false;
	private boolean rotationEnable=false;  //控制最大化的时候是否自动打开烟花旋转效果
	
	//烟花驱动timer
	public static final int TIMER_INTERVAL=10;
	private javax.swing.Timer timer=new javax.swing.Timer(TIMER_INTERVAL,this);

	//烟花的参数
	private double fireworkOccurProbability=0.015;
	private int boomingDisplayTimeCount=35;
	
	//这个量用来记录屏幕大小
	private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		
	//==============================
	//==上面这个区域是负责实现烟花功能的==
	//==============================
	
	/**
	 * 装有wordList的分离窗口
	 */
	private WordListDialog wordListDialog;
	/**
	 * 装有wordInfo的分离窗口
	 */
	private WordInfoDialog wordInfoDialog;
	/**
	 * 对装有typeInField和buttonGo的分离窗口的引用
	 */
	private DictFrame dictFrame;
	/**
	 * 标识是否开启分离窗口效果的变量
	 */
	private boolean freeMoveOpen=true;
	/**
	 * 为在wordPanel中添加wordList预留的位置，
	 * 当关闭freeMove效果的时候，wordList将被
	 * 加入到wordPanel中,打开freeMove效果时，
	 * wordList将会被从中删除
	 */
	private JPanel wordListReserved;
	/**
	 * 为在wordPanel中添加wordList预留的位置，
	 * 当关闭freeMove效果的时候，wordInfo将被
	 * 加入到wordPanel中打开freeMove效果时，
	 * wordList将会被从中删除
	 */
	private JPanel wordInfoReserved;
	/**
	 * 这个量标识所有分离窗口的alwaysOnTop的状态
	 */
	private boolean alwaysOnTop=true;
	
	//警告对话框，在keyLibraryLost的情况下弹出
	private ShowMessageDialog warningDialog;
	
	public WordPanel(DictFrame dictFrame)
	{	
		this.dictFrame=dictFrame;
		dictFrame.setWordPanel(this);
		
		//初始化GUI组件
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(DEFAULT_SIZE.width, BAR_SIZE+BUTTON_SIZE.height));
		setBackground(sharedBackground);
		
		buttonGo.setPreferredSize(BUTTON_SIZE);
		buttonGo.setName("buttonGo");
		buttonGo.setFocusable(false);
		
		typeInField.setPreferredSize(TYPE_IN_WORD_SIZE);
		typeInField.setSelectionColor(new Color(128,255,255));
		typeInField.setSelectedTextColor(Color.BLACK);
		typeInField.setFont(new Font("Times",Font.BOLD,16));
		typeInField.setOpaque(false);
		
		wordInfo.setLineWrap(true);
		wordInfo.setWrapStyleWord(true);
		wordInfo.setPreferredSize(WORD_INFO_SIZE);
		wordInfo.setEditable(false);
		wordInfo.setFont(new Font("Times",Font.BOLD,14));
		wordInfo.setSelectionColor(new Color(128,255,255));
		wordInfo.setSelectedTextColor(Color.BLACK);
		wordInfo.setOpaque(false);
		wordInfo.setFocusable(false);
		
		wordList.setPreferredSize(WORD_LIST_SIZE);
		wordList.setFont(new Font("Times",Font.BOLD,16));
		wordList.setSelectionBackground(new Color(255,251,185));
		wordList.setSelectionForeground(Color.BLACK);
		wordList.setBackground(new Color(0,0,0,0));
		wordList.setOpaque(false);
		wordList.setFocusable(true);
		FontMetrics fm2=getFontMetrics(wordList.getFont());
		wordList.setFixedCellHeight(fm2.getHeight()-4);
		
		//运行用户自定义外观设置
		customAppearanceSetting();
		
		//加入GUI组件
		JPanel p1=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		p1.add(buttonGo);
		p1.add(typeInField);
		p1.setOpaque(false);
		p1.setBackground(new Color(0,0,0,0));
		add(p1,BorderLayout.NORTH);
		
		//为wordList预留位置
		JPanel p2=new JPanel(new BorderLayout());
		p2.setPreferredSize(WORD_LIST_SIZE);
		p2.setOpaque(false);
		p2.setBackground(new Color(0,0,0,0));
		add(p2,BorderLayout.WEST);
		p2.setVisible(false);
		wordListReserved=p2;
		
		//为wordInfo预留位置
		JPanel p3=new JPanel(new BorderLayout());
		p3.setOpaque(false);
		p3.setBackground(new Color(0,0,0,0));
		add(p3,BorderLayout.CENTER);
		p3.setVisible(false);
		wordInfoReserved=p3;
		
		//初始化分离窗体
		MagGroup group=new MagGroup();
		
		wordListDialog=new WordListDialog(group);
		wordListDialog.setLocation(600,0);
		wordListDialog.pack();
		wordListDialog.setLocation(dictFrame.getLocation().x, dictFrame.getLocation().y+BAR_SIZE+BUTTON_SIZE.height);
				
		wordInfoDialog=new WordInfoDialog(group);
		wordInfoDialog.pack();	
		wordInfoDialog.setLocation(dictFrame.getLocation().x+WORD_LIST_SIZE.width, dictFrame.getLocation().y+BAR_SIZE+BUTTON_SIZE.height);
		
		//加入加入监听器
		buttonGo.addActionListener(this);
		buttonGo.addMouseListener(this);
		
		typeInField.addActionListener(this);
		typeInField.addKeyListener(this);
		typeInField.setFocusable(true);
		typeInField.addMouseListener(this);
		
		wordList.addListSelectionListener(this);
		wordList.addMouseWheelListener(this);
		wordList.addMouseListener(this);
		wordList.addMouseMotionListener(this);
		
		wordInfo.addMouseListener(this);
		
	    //配置初始化
		initialize();
		
		//初始化弹出菜单
		mainPopupMenu=new MainPopupMenu(this);
		
	}
	
	//这个方法根据配置文件的信息进行初始化
	private void initialize()
	{
		//==皮肤设定初始化==
		//1.activeBK
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(ACTIVE_BK_FILE));
		    activeBackground=in.readBoolean();
		    in.close();
		}
		catch(Exception ex)
		{
			System.out.println("ACTIVE_BK_FILE lost");
			activeBackground=true;
		}
		
		//2.selectedBK
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(SELECTED_BK_FILE));
			int value=in.readInt();
			File pic=new File("GUISource\\backgrounds\\"+value+".jpg");
			if(pic.exists())
			{
				backgroundImg=new ImageIcon(pic.getAbsolutePath());
				currentBk=value;
			}
			else
			{
				throw new IOException();
			}
			in.close();
		}
		catch(Exception ex)
		{
			System.out.println("SELECTED_BK_FILE lost, or some other error");
			backgroundImg=new ImageIcon("GUISource\\backgrounds\\0.jpg");
			currentBk=0;
		}
		
		//3.firework
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(FIREWORK_BK_FILE));
			fireworkEnable=in.readBoolean();
			in.close();
		}
		catch(Exception ex)
		{
			System.out.println("FIREWORK_BK_FILE lost");
			fireworkEnable=true;
		}
		
		//3.spin
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(SPIN_BK_FILE));
			rotationEnable=in.readBoolean();
			in.close();
		}
		catch(Exception ex)
		{
			System.out.println("SPIN_BK_FILE lost");
			rotationEnable=false;
		}
		
		//4.get number of backgrounds
		File picFile;
		int n=0;
		do
		{
			picFile=new File("GUISource\\backgrounds\\"+n+".jpg");
		    n++;
		}while(picFile.exists());
		numOfBks=n-1;
		
		//5.paintBkMask
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(PAINT_BK_MASK_FILE));
		    paintBkMask=in.readBoolean();
		    in.close();
		}
		catch(Exception ex)
		{
			System.out.println("PAINT_BK_MASK_FILE lost");
			paintBkMask=true;
		}
		
		//6.freeMoveOpen
		try
		{
			DataInputStream in=new DataInputStream(new FileInputStream(FREE_MOVE_FILE));
		    freeMoveOpen=in.readBoolean();
		    setFreeMoveOpen(freeMoveOpen);
		    in.close();
		}
		catch(Exception ex)
		{
			System.out.println("PAINT_BK_MASK_FILE lost");
			freeMoveOpen=true;
		}
		
		//==词库加载初始化==
		ArrayList<LibraryAccount> libAccToBeAdded=new ArrayList<LibraryAccount>();
		ArrayList<LibraryAccount> libAccOfMajor=new ArrayList<LibraryAccount>();
		//1.major library
		try
		{
			boolean[] selecteds=null;
		    ObjectInputStream out=new ObjectInputStream(new FileInputStream(MAJOR_LIBRARY_FILE));
			selecteds=(boolean[])(out.readObject());
			for(int i=0;i<selecteds.length;i++)
			{
				if(selecteds[i])
				{
					libAccToBeAdded.add(Library.getLibraryAccountor().getLibraryAccounts().get(i));
					libAccOfMajor.add(Library.getLibraryAccountor().getLibraryAccounts().get(i));
				}
			}
			out.close();
		}
		catch(Exception ex)
		{
			System.out.println("MAJOR_LIBRARY_FILE lost");
			if(LibraryManager.getLibraryAccountor().getLibraryAccounts().size()>0)
			{
				libAccToBeAdded.add(Library.getLibraryAccountor().getLibraryAccounts().get(0));
				libAccOfMajor.add(Library.getLibraryAccountor().getLibraryAccounts().get(0));
			}
			else
			{
				System.out.println("Error, Key Library Lost!");
				warningDialog=ShowMessageDialog.showDialog(null, "Error, Key Library Lost! Please instore a library. ");
				warningDialog.setAutoShutDown(true);
				setAlwaysOnTop(false);
			}
		}
		
		//2.secondary libraries
		try
		{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(SECONDARY_LIBRARY_FILE));
			boolean[] bs=(boolean[])(in.readObject());
			ArrayList<LibraryAccount> libAccounts=LibraryManager.getLibraryAccountor().getLibraryAccounts();
			for(int i=0;i<bs.length;i++)
			{
				if(i<libAccounts.size())
				{
					if(libAccounts.get(i)!=null)
					{
						if(bs[i])
						{
							libAccToBeAdded.add(Library.getLibraryAccountor().getLibraryAccounts().get(i));
						}
					}
				}
			}
			in.close();
		}
		catch(Exception ex)
		{
			System.out.println("SECONDARY_LIBRARY_FILE lost");
		}
		
		//3.add libraryManagers and setActive
	    conLibraryManager=new ConLibraryManager(libAccToBeAdded.toArray(new LibraryAccount[0]));
		conLibraryManager.enactive(libAccOfMajor.toArray(new LibraryAccount[0]));
	    
	    System.out.println("\n\n==now display libraryManagers==");
	    printLibraryManagers();
		
	}

	public boolean enactive(LibraryAccount libAcc)
	{
		if(null==conLibraryManager)
		{
			return false;
		}
		boolean temp=conLibraryManager.enactive(libAcc);
		printLibraryManagers();
		return temp;
	}
	
	public boolean deactive(LibraryAccount libAcc)
	{
		if(null==conLibraryManager)
		{
			return false;
		}
		boolean temp=conLibraryManager.deactive(libAcc);
		printLibraryManagers();
		return temp;
	}
	
	//初始化wordList
	private void initWordList(WordEntry[] array)
	{
		
	
		if(null==array)
		{
			initWordList(new String[]{"none"});
		}
		
		if(0==array.length)
		{
			initWordList(new String[]{"none"});
		}
		
		String[] words=new String[array.length];
		for(int i=0;i<array.length;i++)
		{
			words[i]=array[i].getWord();
		}

		wordListItems=array;
		wordList.setListData(words);
    }
	
	private void initWordList(String[] array)
	{
        if(null==array)
		{
			initWordList();
		}
		
		if(0==array.length)
		{
			initWordList();
		}
	
		wordListItems=null;
		wordList.setListData(array);
		
	}
	
	private void initWordList()
	{
		wordListItems=null;
		wordList.setListData(new String[]{"none"});
	}
	/**
	 * 当用户输入字符的时候，或者字符被自动更新时调用这个方法，
	 * 刷新libraryManager和字符输入区
	 */
	private void refreshTypeInField()
	{
		if(debug)
		{
			System.out.println("refreshTypeInField");
		}
		conLibraryManager.refreshTypeInWord(typeInField.getText());
	}
	
	/**
	 * 用来刷新单词列表
	 */
	private void refreshWordList()
	{
		if(debug)
		{
			System.out.println("refreshWordList");
		}
		WordEntry[] wordListWordEntries=conLibraryManager.findSameRootWordEntries();
				
		if(wordListWordEntries!=null)
		{
			if(!isMaxified&&wordListWordEntries.length>MAX_SAME_ROOT_WORD_NUMBER_WHEN_UNMAXIFIED)
			{
				WordEntry[] newWordListWordEntries=new WordEntry[MAX_SAME_ROOT_WORD_NUMBER_WHEN_UNMAXIFIED];
				for(int i=0;i<newWordListWordEntries.length;i++)
				{
					newWordListWordEntries[i]=wordListWordEntries[i];
				}
				wordListWordEntries=newWordListWordEntries;
			}
			
			initWordList(wordListWordEntries);
		}
		
	}
	
	private void refreshWordInfo()
	{
		if (debug) 
		{
			System.out.println("refreshWordInfo");
		}

	    TransEntry transEntry = conLibraryManager.findTransEntry();
		if (transEntry != null)
		{
			wordInfo.setText(transEntry.getTrans());
		}
	
	}
	
	
	//这个方法用来历史查询上一个查找过的单词
	private void findPreviousInHistory()
	{
		WordTransEntry wte=conLibraryManager.findPrevious();
		if(wte!=null)
		{
			typeInField.setText(wte.getWord());
			refreshTypeInField();
			refreshWordList();
			wordInfo.setText(wte.getTrans());
		}
		
	}
	
	//这个方法用来历史查询下一个查找过的单词
	private void findNextInHistory()
	{
		WordTransEntry wte=conLibraryManager.findNext();
		if(wte!=null)
		{
			typeInField.setText(wte.getWord());
			refreshTypeInField();
			refreshWordList();
			wordInfo.setText(wte.getTrans());
		}
		
	}
	
	// 当找不到单词的时候，调用这个方法，先进行网络查找，如果找不到，则查找形近词
	private void wordNotFound()
	{
		//网络搜索
		if (openNetSearch)
		{
			String netTrans = null;
			if ((netTrans = conLibraryManager.findTransOnLine()) != null) 
			{
				Runnable runnable=new SetTextRunnable(netTrans);
				EventQueue.invokeLater(runnable);
				return;
			}
		}
		else
		{
			System.out.println("netSearch not open");
		}
		//寻找相近词
		Runnable runnable=new SetTextRunnable("    Sorry, word or sentence not found.\n"+
				"    (Possible words listed left-hand.)");
		EventQueue.invokeLater(runnable);
        initWordList(conLibraryManager.findResembleWordEntries());
	}
	
	//用以实现wordNotFound的线程
	private void wordNotFoundThread()
	{
		Thread t=new Thread(new Runnable(){
			public void run()
			{
				wordNotFound();
			}
		});
		t.start();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		refreshTypeInField();
		
		if(e.getSource()==buttonGo)
		{
			if(conLibraryManager.isFindWord())
			{
				refreshWordInfo();
			}
			else
			{
				if (typeInField.getText().length() > 0) 
				{
					wordInfo.setText("    Sorry, input not found. Now Searching online.");
					wordNotFoundThread();
				}
			}
		}
		
		if(e.getSource()==typeInField)
		{
			if(debug)
			{
				System.out.println("TypeInField Enter Pressed");
			}
			
			if(conLibraryManager.isFindWord())
			{
				refreshWordInfo();
			}
			else
			{
				if (typeInField.getText().length() > 0) 
				{
					wordInfo.setText("    Sorry, input not found. Now Searching online.");
					wordNotFoundThread();
				}
			}
			
			clickClip.play();
		}
		
		if(e.getSource()==wordList)
		{
			onClip.play();
		}
		
		//typeInField.requestFocusInWindow();
		
		//这里处理烟花
		if(e.getSource()==timer)
		{
			selfProcess();
		}
	}

	
	public void valueChanged(ListSelectionEvent e) 
	{
		if (wordListItems != null) 
		{
			int index=wordList.getSelectedIndex();
			if (index >= 0 && index < wordListItems.length) 
			{
				WordEntry item = wordListItems[wordList.getSelectedIndex()];

				typeInField.setText(item.getWord());
				refreshTypeInField();
				refreshWordInfo();
			}
		}
		//typeInField.requestFocus();
		
    	repaint();
	}

	
	public void keyPressed(KeyEvent arg0) 
	{
		repaint();
	}

	
	public void keyReleased(KeyEvent e) 
	{
		if ((e.getKeyCode() >= 'a' && e.getKeyCode() <= 'z')
			|| (e.getKeyCode() >= 'A' && e.getKeyCode() <= 'Z')
			||(e.getKeyCode()>='0'&&e.getKeyCode()<='9')
			|| (e.getKeyCode() == ' ' || e.getKeyCode() == '.' || e
				.getKeyCode() == '-') ||(e.getKeyCode()=='_')||
				(e.getKeyCode()==KeyEvent.VK_BACK_SPACE))
		{
			refreshTypeInField();
			refreshWordList();
		}
		
		if(e.getKeyCode()==KeyEvent.VK_UP) //用以实现历史查询功能
		{
			findPreviousInHistory();
		}
		
		if(e.getKeyCode()==KeyEvent.VK_DOWN)  //用以实现历史查询功能
		{
     		findNextInHistory();
		}
		
		if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&e.isShiftDown())
		{
			typeInField.setText("");
		}
		
    	repaint();
	}
	
	public void keyTyped(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if (e.getSource() == wordList) 
		{
			if (null == wordListItems || null == wordList) {
				return;
			}
			if (e.getWheelRotation() == 1) {
				wordList.setSelectedIndex(wordList.getSelectedIndex() + 1);
			}
			if (e.getWheelRotation() == -1) {
				wordList.setSelectedIndex(wordList.getSelectedIndex() - 1);
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent e) {
		if(e.getSource()==buttonGo)
		{
			onClip.play();
		}
		
	}

	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e)
	{
		if(e.getSource()==buttonGo)
		{
			clickClip.play();
		}
		if(e.getSource()==wordList)
		{
			onClip.play();
		}
		
		repaint();
	}

	
	public void mouseReleased(MouseEvent e)
	{
		if(e.getSource()==buttonGo)
		{
			releaseClip.play();
		}
		
		if(e.getSource()==wordInfo
				||e.getSource()==typeInField
				||e.getSource()==wordList)
		{
			if(e.isPopupTrigger())
			{
				mainPopupMenu.refresh();
				mainPopupMenu.show((Component)(e.getSource()),e.getX(), e.getY());
			}
		}
		
		repaint();		
	}

	
	public void mouseDragged(MouseEvent e) 
	{
		repaint();
		
	}

	
	public void mouseMoved(MouseEvent e) 
	{
		repaint();
		
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(debug3)
		{
			System.out.println("paintComponent");
			System.out.println("image: "+backgroundImg);
		}
		if(!isMaxified)
		{
			if(activeBackground)
			{
				g.drawImage(backgroundImg.getImage(),-dictFrame.getLocation().x,-dictFrame.getLocation().y ,screenSize.width ,screenSize.height,this);
			}
			else
			{
				g.drawImage(backgroundImg.getImage(),0,0,DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE,this);
			}
			if(paintBkMask)
			{
				g.drawImage(backgroundImgMask.getImage(),0,0,DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE,this);
			}
		}
		else
		{
			g.setColor(sharedBackground);
			g.fillRect(0,0,getWidth(),getHeight());
			if(paintBkMask)
			{
				g.drawImage(backgroundImgMaskWhenMaxified.getImage(),0,0,getWidth() , getHeight() ,this);
			}
		}
		
		//这里是负责画出烟花
		if (isMaxified) 
		{
			for (int i = 0; i < fireworks.size(); i++) 
			{
				Firework firework = fireworks.get(i);
				if (firework != null) {
					firework.paint(g, plane);
				}
			}
		}
		
		if(wordListDialog!=null)
		{
			wordListDialog.repaint();
		}
		if(wordInfoDialog!=null)
		{
			wordInfoDialog.repaint();
		}		

	}
	
	/**
	 * 这个方法负责最大化的时候进行调整
	 */
	public void maxified()
	{
		isMaxified=true;
		if(fireworkEnable)
		{
	    	openFirework();
		}
	    if(rotationEnable)
	    {
	    	openRotation();
	    }
	}
	
	/**
	 * 这个方法负责从最大化恢复的时候进行调整
	 */
	public void demaxified()
	{
		isMaxified=false;
		closeFirework();
	}
	
	
	//=============================
	//=====下面的区域负责烟花效果======
	//=============================
	
	//建立空间平面直角坐标系plane
	public void openFirework()
	{
		fireworkOpen = true;
		timer.start();
		constructPlane();
	}
	
	public void closeFirework()
	{
		fireworkOpen=false;
	}
	
	public void openRotation()
	{
		fireworkOccurProbability=0.01;
		boomingDisplayTimeCount=140;
		rotate=true;
	}
	
	public void closeRotation()
	{
		fireworkOccurProbability=0.015;
		boomingDisplayTimeCount=35;
		rotate=false;
	}
	
	private void constructPlane()
	{
		
		if(debug)
		{
			System.out.println("====in constructPlane of OuterBackgroundPanel====");
		   
			System.out.println("fai: "+fai);
			System.out.println("thita: "+thita);
		}
		
		//计算法向量n
		MyVector3D I=new MyVector3D(1,0,0).mutiply(Math.sin(fai)*Math.cos(thita));
		MyVector3D J=new MyVector3D(0,1,0).mutiply(Math.sin(fai)*Math.sin(thita));
		MyVector3D K=new MyVector3D(0,0,1).mutiply(Math.cos(fai));
				
		MyVector3D n=I.addition(J.addition(K));
		
		//计算坐标平面的相关参数
		MyPoint3D center=new MyPoint3D(distance/2,panelSize.width/2,panelSize.height);
		center.move(n.toUnitVector().mutiply(-distance/2));
		MyVector3D grossI=new MyVector3D(n.getY(),-n.getX(),0);
		MyVector3D grossJ=n.outerProduct(grossI);
		
		center.move(grossI.toUnitVector().mutiply(-panelSize.width/2));
		plane=new PlaneCoordinateSystemIn3D(center,grossI,grossJ);
	
		if(debug)
		{
			System.out.println("====end of constructPlane of OuterBackgroundPanel====");
		}
		
	}
	
	/**
	 * 管理烟花的运行
	 */
	protected void selfProcess()
	{
		if((fireworkOpen)&&(Math.random()<fireworkOccurProbability))
		{
            MyVector3D arrow=new MyVector3D(0,0,1);
			
			//计算三个坐标单位向量
			MyVector3D roughI=new MyVector3D(arrow.getZ(),0,-arrow.getX());
			MyVector3D roughJ=roughI.outerProduct(arrow);
		
			MyVector3D I=roughI.toUnitVector().mutiply(Math.random()*0.1*((Math.random()>0.5)?1:-1));
			MyVector3D J=roughJ.toUnitVector().mutiply(Math.random()*0.1*((Math.random()>0.5)?1:-1));
			MyVector3D K=arrow.toUnitVector();
			
			MyVector3D randomArrow=K.addition(I.addition(J));
			MyPoint3D startPoint=new MyPoint3D(distance*Math.random(),panelSize.width*(1.0/3+1.0/3*Math.random()),OFFSET*Math.random());
			
			Firework firework=null;
			fireworks.add(firework=new Firework(startPoint,randomArrow, (Math.random()>0.5)?true:false,true));
	        firework.setBoomClip(WordPanel.class.getResource("firework\\firework.wav"));
		    firework.setBoomingDisplayTimeCount(boomingDisplayTimeCount);
		}
		
		boolean allNull=true;
		for(int i=0;i<fireworks.size();i++)
		{
			Firework firework=fireworks.get(i);
			if(firework!=null)
			{
				allNull=false;
				firework.selfProcess();
				if(!firework.isUseable())
				{
					fireworks.remove(i);
				}
			}
		}
		
		if(rotate)
		{
			thita+=step;
			constructPlane();
		}
		
		if(!fireworkOpen&&allNull)
		{
			timer.stop();
		}
		
		repaint();
	}
	
	public boolean isOpen() {
		return fireworkOpen;
	}

	public void setOpen(boolean open) {
		this.fireworkOpen = open;
	}
	
	//回收时要关闭timer
	public void finalize() throws java.lang.Throwable
	{
		timer.stop();
	}

	//这个方法是调试时用的，用于显示当前的词库状态
	private void printLibraryManagers()
	{
		System.out.println(conLibraryManager);
	}

	/**
	 * 这个方法可以将背景图设定到下一张或前一张，
	 * next==true的时候是下一张
	 */
	public void scrollBackground(boolean next)
	{
		if (next)
		{
			currentBk++;
			File pic = new File("GUISource\\backgrounds\\" + currentBk + ".jpg");
			if (pic.exists()) {
				backgroundImg = new ImageIcon(pic.getAbsolutePath());
			} else {
				currentBk = 0;
				backgroundImg = new ImageIcon("GUISource\\backgrounds\\"
						+ currentBk + ".jpg");
			}
		}
		else
		{
			currentBk--;
			if(currentBk>=0)
			{
				backgroundImg = new ImageIcon("GUISource\\backgrounds\\" + currentBk + ".jpg");
			}
			else
			{
				currentBk=numOfBks-1;
				backgroundImg = new ImageIcon("GUISource\\backgrounds\\" + currentBk + ".jpg");
			}
		}
		
		repaint();
	}
		
	public boolean isActiveBackground() {
		return activeBackground;
	}

	public void setActiveBackground(boolean activeBackground)
	{
		this.activeBackground = activeBackground;
		repaint();
	}

	public boolean isFireworkEnable() 
	{
		return fireworkEnable;
	}

	public void setFireworkEnable(boolean fireworkEnable) 
	{
		this.fireworkEnable = fireworkEnable;
		
		if(!fireworkEnable)
		{	if(fireworkOpen)
		    {
		    	closeFirework();
	    	}
	    }
		else
		{
			if (isMaxified) 
			{
				if(!fireworkOpen)
				{
					openFirework();
				}
			}
		}
		
		repaint();
	}

	public boolean isRotationEnable() {
		return rotationEnable;
	}

	public void setRotationEnable(boolean rotationEnable) 
	{
		this.rotationEnable = rotationEnable;
		
		if(!rotationEnable)
		{
			if(rotate)
			{
				closeRotation();
			}
		}
		else
		{
			if(!rotate)
			{
				openRotation();
			}
		}
		
		repaint();
	}

	public boolean isOpenNetSearch() {
		return openNetSearch;
	}

	public void setOpenNetSearch(boolean openNetSearch) {
		this.openNetSearch = openNetSearch;
	}

	public boolean isPaintBkMask() {
		return paintBkMask;
	}

	public void setPaintBkMask(boolean paintBkMask) 
	{
		this.paintBkMask = paintBkMask;
		repaint();
	}
	
	public WordListDialog getWordListDialog() {
		return wordListDialog;
	}

	public WordInfoDialog getWordInfoDialog() {
		return wordInfoDialog;
	}

	public DictFrame getDictFrame() {
		return dictFrame;
	}

	public boolean isFreeMoveOpen() {
		return freeMoveOpen;
	}

	/**
	 * 这个方法设定freeMoveOpen，即控制分离窗体效果开关的变量，
	 * 当设为true时，该方法仅仅改变变量的值，当设为false时，这
	 * 个方法将会是所有窗体连接起来，即外观上恢复成一个窗体
	 * 
	 */
	public void setFreeMoveOpen(boolean freeMoveOpen) 
	{
		this.freeMoveOpen = freeMoveOpen;
		if(dictFrame!=null)
		{
			if(!freeMoveOpen)
			{
				dictFrame.shutDownFreeMoving();
			}
			else
			{
				dictFrame.openFreeMoving();
			}
		}
	}

	public JPanel getWordListReserved() {
		return wordListReserved;
	}

	public JPanel getWordInfoReserved() {
		return wordInfoReserved;
	}

	public JList getWordList() {
		return wordList;
	}

	public JTextArea getWordInfo() {
		return wordInfo;
	}
	
	/**
	 * 这个类用来给副线程设置wordInfo的text用
	 * @author ZYL
	 *
	 */
	private class SetTextRunnable implements Runnable
	{
		private String text="";
		
		public SetTextRunnable(String s)
		{
			this.text=s;
		}
			
		public void run() 
		{
			wordInfo.setText(text);	
			repaint();
		}
		
	}
	
	//==========================================================
	
	class WordListDialog extends MagDialog
	{
		//保留装有wordList的面板，以便以后将wordInfo取出或装入
		private JPanel wordListReservedInDialog;
		
		public WordListDialog(MagGroup group)
		{
			super(group);
			this.setLayout(new BorderLayout());
			
			GraphicsPanelOfWordListDialog p2=new GraphicsPanelOfWordListDialog(new BorderLayout());
			p2.setPreferredSize(WORD_LIST_SIZE);
			wordListReservedInDialog=p2;
			
			p2.add(wordList);
			p2.setOpaque(true);
			this.getContentPane().add(p2,BorderLayout.CENTER);
			wordList.addMouseListener(this);
			wordList.addMouseMotionListener(this);
			setResizable(false);
			setAlwaysOnTop(alwaysOnTop);
			
		}
		
        /**
         * 这个方法返回自己在标准位置的时候，相对与wordPanel的位置坐标
         * 
         */
		protected Point getRelativeLocationToWordPanel()
		{
			return new Point(0, BUTTON_SIZE.height);
		}
		
		/**
		 * 负责画图的内部类
		 * @author ZYL
		 *
		 */
		private class GraphicsPanelOfWordListDialog extends JPanel
		{
			public GraphicsPanelOfWordListDialog()
			{
				super();
			}
			
			public GraphicsPanelOfWordListDialog(LayoutManager layout)
			{
				super(layout);
			}
			
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
	
				if(!isMaxified)
				{
					if(activeBackground)
					{
						g.drawImage(backgroundImg.getImage(),-WordListDialog.this.getLocation().x,-WordListDialog.this.getLocation().y ,screenSize.width ,screenSize.height,this);
					}
					else
					{
						g.drawImage(backgroundImg.getImage(),-getRelativeLocationToWordPanel().x,-getRelativeLocationToWordPanel().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE, this);
					}
					if(paintBkMask)
					{
						g.drawImage(backgroundImgMask.getImage(),-getRelativeLocationToWordPanel().x,-getRelativeLocationToWordPanel().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE, this);
					}
				}
				else
				{
					g.setColor(sharedBackground);
					g.fillRect(0,0,getWidth(),getHeight());
				}
			}
		}

		public JPanel getWordListReservedInDialog() {
			return wordListReservedInDialog;
		}
		
	}
	
	class WordInfoDialog extends MagDialog
	{
		//保留装有wordInfo的面板，以便以后将wordInfo取出或装入
		private JPanel wordInfoReservedInDialog;
		
		public WordInfoDialog(MagGroup group)
		{
			super(group);
			this.setLayout(new BorderLayout());
			
			GraphicsPanelOfWordInfoDialog p3=new GraphicsPanelOfWordInfoDialog(new BorderLayout());
			wordInfoReservedInDialog=p3;
			
			p3.add(wordInfo);
			p3.setOpaque(true);
			this.getContentPane().add(p3,BorderLayout.CENTER);
			wordInfo.addMouseListener(this);
			wordInfo.addMouseMotionListener(this);
     		setResizable(false);
     		setAlwaysOnTop(alwaysOnTop);
     		
		}
		
		  /**
         * 这个方法返回自己在标准位置的时候，相对与wordPanel的位置坐标
         * 
         */
		protected Point getRelativeLocationToWordPanel()
		{
			return new Point(WORD_LIST_SIZE.width, BUTTON_SIZE.height);
		}
		
		/**
		 * 负责画图的内部类
		 * @author ZYL
		 *
		 */
		private class GraphicsPanelOfWordInfoDialog extends JPanel
		{
			public GraphicsPanelOfWordInfoDialog()
			{
				super();
			}
			
			public GraphicsPanelOfWordInfoDialog(LayoutManager layout)
			{
				super(layout);
			}
			
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				
		    	if(!isMaxified)
				{
					if(activeBackground)
					{
						g.drawImage(backgroundImg.getImage(),-WordInfoDialog.this.getLocation().x,-WordInfoDialog.this.getLocation().y ,screenSize.width ,screenSize.height,this);
					}
					else
					{
						g.drawImage(backgroundImg.getImage(),-getRelativeLocationToWordPanel().x,-getRelativeLocationToWordPanel().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE, this);
					}
					if(paintBkMask)
					{
						g.drawImage(backgroundImgMask.getImage(),-getRelativeLocationToWordPanel().x,-getRelativeLocationToWordPanel().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height-BAR_SIZE, this);
					}
				}
				else
				{
					g.setColor(sharedBackground);
					g.fillRect(0,0,getWidth(),getHeight());
				}
			}
		}

		public JPanel getWordInfoReservedInDialog() {
			return wordInfoReservedInDialog;
		}
		
	}

	public boolean isAlwaysOnTop() 
	{
		return alwaysOnTop;
	}

	public void setAlwaysOnTop(boolean alwaysOnTop) 
	{
		this.alwaysOnTop = alwaysOnTop;
		wordListDialog.setAlwaysOnTop(alwaysOnTop);
		wordInfoDialog.setAlwaysOnTop(alwaysOnTop);
		if(dictFrame!=null)
		{
			dictFrame.setAlwaysOnTop(alwaysOnTop);
		}
	}
	
	/**
	 * 如果觉得背景图片太花了，可以调用这个方法将背景清空
	 */
	public void clearBackground()
	{
		File pic=new File("GUISource\\backgrounds\\0.jpg");
		if(pic.exists())
		{
			backgroundImg=new ImageIcon(pic.getAbsolutePath());
		    repaint();
		}
		else
		{
			System.out.println("Error in clearBackground, can't find file 0.jpg");
		}
	}
	

	/**
	 * 相对于clearBackground()方法，这个方法的用处在于
	 * 清空了背景图后，可以用它来恢复
	 */
	public void restoreBackground()
	{
		File pic=new File("GUISource\\backgrounds\\"+currentBk+".jpg");
		if(pic.exists())
		{
			backgroundImg=new ImageIcon(pic.getAbsolutePath());
		    repaint();
		}
		else
		{
			System.out.println("Error in restoreBackground, can't find pic file");
		}
	}
	
	/**
	 * 这个方法用index指定创建哪个生词本届面，
	 * 并创建之
	 * @param index
	 */
	public void showPersonalLibraryDialog(int index)
	{
		if(index>=0&&index<PersonalLibrary.getLibraryAccountor().size())
		{
			new PersonalWordDialog(index);
		}
	}
	
	/**
	 * 这个方生成提示用户输入名字创建新的生词本的界面
	 */
	public void showNewPersonalLibraryDialog()
	{
		NewPLDialog.showDialog(getDictFrame());
	}
	
	/**
	 * 这个方生成提示用户选择要删除的生词本的界面
	 */
	public void showRemovePersonalLibraryDialog()
	{
		RemovePLDialog.showDialog(getDictFrame());
	}

	public JTextField getTypeInField() {
		return typeInField;
	}
	
	public JButton getButtonGo() {
		return buttonGo;
	}

	/**
	 * 这个方法给扩展该类的用户使用，覆盖该方法可以初始化外观设置
	 * 利用getButtonGo(), getTypeInField(), getWordList(),
	 * getWordInfo()方法可以获得对四个主要组件的访问。
	 * 该方法会自动在构造方法中运行已完成初始化
	 */
	protected void customAppearanceSetting()
	{
		
	}

	public JDialog getWarningDialog() {
		return warningDialog;
	}

	
}
