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
 * ����鵥�ʽ����JPanel
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
	//��Щ�������������λ�ü���С
	public static final int BAR_SIZE=31;
	public static final Dimension DEFAULT_SIZE=new Dimension(500,400);
	public static final Dimension BUTTON_SIZE=new Dimension(70,40);
	public static final Dimension WORD_LIST_SIZE=new Dimension(140,DEFAULT_SIZE.height-BAR_SIZE-BUTTON_SIZE.height);
	public static final Dimension WORD_INFO_SIZE=new Dimension(DEFAULT_SIZE.width-WORD_LIST_SIZE.width,WORD_LIST_SIZE.height);
	public static final Dimension TYPE_IN_WORD_SIZE=new Dimension(430,40);
	
	//�ʿ������
    private ConLibraryManager conLibraryManager=null;
    
	//���������������
	private JButton buttonGo=new JButton("");
	private JTextField typeInField=new JTextField("");
	
	//�����б�
	private JList wordList=new JList(new String[]{"none"});
	private WordEntry[] wordListItems;
		
	//������д���ʵĽ��͵�����
	private JTextArea wordInfo=new JTextArea();
		
	//��������οյ�ɫ
	private Color sharedBackground=SHARED_BACKGROUND;
	
	//����ͼ
	private ImageIcon backgroundImgMask=new ImageIcon("GUISource\\backgroundMask.png");
	private boolean paintBkMask=true;   //���������ָ���Ƿ���backgroundImgMask
	private ImageIcon backgroundImg=null;
	private boolean activeBackground=false; //�����ָ����������ʾģʽ
	private int currentBk=0;   //��ǰ�ı���ͼ
	private int numOfBks=0;   //����ͼ������
	private ImageIcon backgroundImgMaskWhenMaxified=new ImageIcon("GUISource\\backgroundImgMaskWhenMaxified.png"); 
	
	//����Ƿ������
	private boolean isMaxified=false;
	
	//��Ч
	private SoundPlayer clickClip=new SoundPlayer(WordPanel.class.getResource("buttonClick.wav"));
	private SoundPlayer onClip=new SoundPlayer(WordPanel.class.getResource("buttonMouseOn.wav"));
	private SoundPlayer releaseClip=new SoundPlayer(WordPanel.class.getResource("buttonReleased.wav"));
	
	//�Ƿ���������ҹ���
	private boolean openNetSearch=true;
	
	//�����˵�
	private MainPopupMenu mainPopupMenu;
	
	//==============================
	//==������������Ǹ���ʵ���̻����ܵ�==
	//==============================
	private ArrayList<Firework> fireworks=new ArrayList<Firework>();
	private double fai=Math.PI/2;   //����������������ƽ�淨����
	private double thita=Math.PI; 
	private static final double distance=600;
	private PlaneCoordinateSystemIn3D plane;
	private static final int OFFSET=100;
	private Dimension panelSize=new Dimension(DEFAULT_SIZE.width,Toolkit.getDefaultToolkit().getScreenSize().height-27);
	
	//��Ӧ����϶�
	private double step=1.0/180*Math.PI;//�����ƶ�����
	
	//����
	private boolean fireworkOpen=false;
	private boolean fireworkEnable=true;  //������󻯵�ʱ���Ƿ��Զ����̻�Ч��
	
	//�̻���ת����
	private boolean rotate=false;
	private boolean rotationEnable=false;  //������󻯵�ʱ���Ƿ��Զ����̻���תЧ��
	
	//�̻�����timer
	public static final int TIMER_INTERVAL=10;
	private javax.swing.Timer timer=new javax.swing.Timer(TIMER_INTERVAL,this);

	//�̻��Ĳ���
	private double fireworkOccurProbability=0.015;
	private int boomingDisplayTimeCount=35;
	
	//�����������¼��Ļ��С
	private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		
	//==============================
	//==������������Ǹ���ʵ���̻����ܵ�==
	//==============================
	
	/**
	 * װ��wordList�ķ��봰��
	 */
	private WordListDialog wordListDialog;
	/**
	 * װ��wordInfo�ķ��봰��
	 */
	private WordInfoDialog wordInfoDialog;
	/**
	 * ��װ��typeInField��buttonGo�ķ��봰�ڵ�����
	 */
	private DictFrame dictFrame;
	/**
	 * ��ʶ�Ƿ������봰��Ч���ı���
	 */
	private boolean freeMoveOpen=true;
	/**
	 * Ϊ��wordPanel�����wordListԤ����λ�ã�
	 * ���ر�freeMoveЧ����ʱ��wordList����
	 * ���뵽wordPanel��,��freeMoveЧ��ʱ��
	 * wordList���ᱻ����ɾ��
	 */
	private JPanel wordListReserved;
	/**
	 * Ϊ��wordPanel�����wordListԤ����λ�ã�
	 * ���ر�freeMoveЧ����ʱ��wordInfo����
	 * ���뵽wordPanel�д�freeMoveЧ��ʱ��
	 * wordList���ᱻ����ɾ��
	 */
	private JPanel wordInfoReserved;
	/**
	 * �������ʶ���з��봰�ڵ�alwaysOnTop��״̬
	 */
	private boolean alwaysOnTop=true;
	
	//����Ի�����keyLibraryLost������µ���
	private ShowMessageDialog warningDialog;
	
	public WordPanel(DictFrame dictFrame)
	{	
		this.dictFrame=dictFrame;
		dictFrame.setWordPanel(this);
		
		//��ʼ��GUI���
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
		
		//�����û��Զ����������
		customAppearanceSetting();
		
		//����GUI���
		JPanel p1=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		p1.add(buttonGo);
		p1.add(typeInField);
		p1.setOpaque(false);
		p1.setBackground(new Color(0,0,0,0));
		add(p1,BorderLayout.NORTH);
		
		//ΪwordListԤ��λ��
		JPanel p2=new JPanel(new BorderLayout());
		p2.setPreferredSize(WORD_LIST_SIZE);
		p2.setOpaque(false);
		p2.setBackground(new Color(0,0,0,0));
		add(p2,BorderLayout.WEST);
		p2.setVisible(false);
		wordListReserved=p2;
		
		//ΪwordInfoԤ��λ��
		JPanel p3=new JPanel(new BorderLayout());
		p3.setOpaque(false);
		p3.setBackground(new Color(0,0,0,0));
		add(p3,BorderLayout.CENTER);
		p3.setVisible(false);
		wordInfoReserved=p3;
		
		//��ʼ�����봰��
		MagGroup group=new MagGroup();
		
		wordListDialog=new WordListDialog(group);
		wordListDialog.setLocation(600,0);
		wordListDialog.pack();
		wordListDialog.setLocation(dictFrame.getLocation().x, dictFrame.getLocation().y+BAR_SIZE+BUTTON_SIZE.height);
				
		wordInfoDialog=new WordInfoDialog(group);
		wordInfoDialog.pack();	
		wordInfoDialog.setLocation(dictFrame.getLocation().x+WORD_LIST_SIZE.width, dictFrame.getLocation().y+BAR_SIZE+BUTTON_SIZE.height);
		
		//������������
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
		
	    //���ó�ʼ��
		initialize();
		
		//��ʼ�������˵�
		mainPopupMenu=new MainPopupMenu(this);
		
	}
	
	//����������������ļ�����Ϣ���г�ʼ��
	private void initialize()
	{
		//==Ƥ���趨��ʼ��==
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
		
		//==�ʿ���س�ʼ��==
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
	
	//��ʼ��wordList
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
	 * ���û������ַ���ʱ�򣬻����ַ����Զ�����ʱ�������������
	 * ˢ��libraryManager���ַ�������
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
	 * ����ˢ�µ����б�
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
	
	
	//�������������ʷ��ѯ��һ�����ҹ��ĵ���
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
	
	//�������������ʷ��ѯ��һ�����ҹ��ĵ���
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
	
	// ���Ҳ������ʵ�ʱ�򣬵�������������Ƚ���������ң�����Ҳ�����������ν���
	private void wordNotFound()
	{
		//��������
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
		//Ѱ�������
		Runnable runnable=new SetTextRunnable("    Sorry, word or sentence not found.\n"+
				"    (Possible words listed left-hand.)");
		EventQueue.invokeLater(runnable);
        initWordList(conLibraryManager.findResembleWordEntries());
	}
	
	//����ʵ��wordNotFound���߳�
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
		
		//���ﴦ���̻�
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
		
		if(e.getKeyCode()==KeyEvent.VK_UP) //����ʵ����ʷ��ѯ����
		{
			findPreviousInHistory();
		}
		
		if(e.getKeyCode()==KeyEvent.VK_DOWN)  //����ʵ����ʷ��ѯ����
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
		
		//�����Ǹ��𻭳��̻�
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
	 * �������������󻯵�ʱ����е���
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
	 * ��������������󻯻ָ���ʱ����е���
	 */
	public void demaxified()
	{
		isMaxified=false;
		closeFirework();
	}
	
	
	//=============================
	//=====������������̻�Ч��======
	//=============================
	
	//�����ռ�ƽ��ֱ������ϵplane
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
		
		//���㷨����n
		MyVector3D I=new MyVector3D(1,0,0).mutiply(Math.sin(fai)*Math.cos(thita));
		MyVector3D J=new MyVector3D(0,1,0).mutiply(Math.sin(fai)*Math.sin(thita));
		MyVector3D K=new MyVector3D(0,0,1).mutiply(Math.cos(fai));
				
		MyVector3D n=I.addition(J.addition(K));
		
		//��������ƽ�����ز���
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
	 * �����̻�������
	 */
	protected void selfProcess()
	{
		if((fireworkOpen)&&(Math.random()<fireworkOccurProbability))
		{
            MyVector3D arrow=new MyVector3D(0,0,1);
			
			//�����������굥λ����
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
	
	//����ʱҪ�ر�timer
	public void finalize() throws java.lang.Throwable
	{
		timer.stop();
	}

	//��������ǵ���ʱ�õģ�������ʾ��ǰ�Ĵʿ�״̬
	private void printLibraryManagers()
	{
		System.out.println(conLibraryManager);
	}

	/**
	 * ����������Խ�����ͼ�趨����һ�Ż�ǰһ�ţ�
	 * next==true��ʱ������һ��
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
	 * ��������趨freeMoveOpen�������Ʒ��봰��Ч�����صı�����
	 * ����Ϊtrueʱ���÷��������ı������ֵ������Ϊfalseʱ����
	 * ���������������д�������������������ϻָ���һ������
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
	 * ��������������߳�����wordInfo��text��
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
		//����װ��wordList����壬�Ա��Ժ�wordInfoȡ����װ��
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
         * ������������Լ��ڱ�׼λ�õ�ʱ�������wordPanel��λ������
         * 
         */
		protected Point getRelativeLocationToWordPanel()
		{
			return new Point(0, BUTTON_SIZE.height);
		}
		
		/**
		 * ����ͼ���ڲ���
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
		//����װ��wordInfo����壬�Ա��Ժ�wordInfoȡ����װ��
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
         * ������������Լ��ڱ�׼λ�õ�ʱ�������wordPanel��λ������
         * 
         */
		protected Point getRelativeLocationToWordPanel()
		{
			return new Point(WORD_LIST_SIZE.width, BUTTON_SIZE.height);
		}
		
		/**
		 * ����ͼ���ڲ���
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
	 * ������ñ���ͼƬ̫���ˣ����Ե�������������������
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
	 * �����clearBackground()����������������ô�����
	 * ����˱���ͼ�󣬿����������ָ�
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
	 * ���������indexָ�������ĸ����ʱ����棬
	 * ������֮
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
	 * �����������ʾ�û��������ִ����µ����ʱ��Ľ���
	 */
	public void showNewPersonalLibraryDialog()
	{
		NewPLDialog.showDialog(getDictFrame());
	}
	
	/**
	 * �����������ʾ�û�ѡ��Ҫɾ�������ʱ��Ľ���
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
	 * �����������չ������û�ʹ�ã����Ǹ÷������Գ�ʼ���������
	 * ����getButtonGo(), getTypeInField(), getWordList(),
	 * getWordInfo()�������Ի�ö��ĸ���Ҫ����ķ��ʡ�
	 * �÷������Զ��ڹ��췽������������ɳ�ʼ��
	 */
	protected void customAppearanceSetting()
	{
		
	}

	public JDialog getWarningDialog() {
		return warningDialog;
	}

	
}
