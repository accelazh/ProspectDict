package guestUserInterface;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import libraryManager.*;
import java.util.*;
import libraryInterface.*;
import settings.*;
import java.io.*;

public class MainPopupMenu extends JPopupMenu 
implements ActionListener, Constants
{
	public static final Color LIGHT=new Color(169,207,235);
	public static final Color DARK=new Color(61,137,233);
	
	public static final Font FONT=new Font("Times", Font.BOLD, 14);
	
	private JMenu skin=new JMenu("Skin");
	private JMenu libraries=new JMenu("Libraries");
	private JMenu personalLibraries=new JMenu("User Libraries");
	private JMenu games=new JMenu("Relax");
	private JCheckBoxMenuItem netSearch=new JCheckBoxMenuItem("Net Search On/Off");
	private JCheckBoxMenuItem alwaysOnTop=new JCheckBoxMenuItem("Always On Top");
	private JMenuItem iconify=new JMenuItem("Iconify");
	private JMenuItem about=new JMenuItem("About");
	private JMenuItem exit=new JMenuItem("Exit");
	
	//属于skin的
	private JMenuItem nextBk=new JMenuItem("Next Background");
	private JMenuItem lastBk=new JMenuItem("Last Background");
	private JMenuItem clear=new JMenuItem("Clear");
	private JMenuItem restore=new JMenuItem("Restore");
	
	private JCheckBoxMenuItem activeBk=new JCheckBoxMenuItem("Active Background");
	private JCheckBoxMenuItem paintBkMask=new JCheckBoxMenuItem("Paint Background Mask");
	private JCheckBoxMenuItem freeMove=new JCheckBoxMenuItem("Free Moving");
	private JCheckBoxMenuItem firework=new JCheckBoxMenuItem("Firework Enable");
	private JCheckBoxMenuItem spin=new JCheckBoxMenuItem("Firework Spin");
	
	//属于libraries的
	private JCheckBoxMenuItem[] libMs;
	private LibraryAccount[] libAccArray;   //这个用来装入libraryAccounts的各个元素，与libMs顺序对应（libraryAccounts与libM一般不会顺序对应）
	private Comparator<LibraryAccount> comparator;  //专门用来排序菜单的比较子
	
	//属于personalLibraries的
	private JMenuItem newPL=new JMenuItem("New");
	private JMenuItem[] pls;
	private JMenuItem removePL=new JMenuItem("Remove");
	
	//属于games的
	private JMenuItem moonLanding=new JMenuItem("Moon Landing");
	private JMenuItem survive=new JMenuItem("Survive");
	private JMenuItem flyingBlocks=new JMenuItem("Flying Blocks");
	
	//wordPanel的引用
	private WordPanel wordPanel;
	
	public MainPopupMenu()
	{
		this(null);
	}
	
	public MainPopupMenu(WordPanel wordPanel)
	{
	    //self
		this.wordPanel=wordPanel;
		
		add(skin);
		add(libraries);
		add(personalLibraries);
		add(games);
		add(new JSeparator());
		add(netSearch);
		add(alwaysOnTop);
		add(iconify);
		add(about);
		add(exit);
		
		//components
		skin.setFont(FONT);
		libraries.setFont(FONT);
		personalLibraries.setFont(FONT);
		games.setFont(FONT);
		newPL.setFont(FONT);
		removePL.setFont(FONT);
		netSearch.setFont(FONT);
		alwaysOnTop.setFont(FONT);
		iconify.setFont(FONT);
		about.setFont(FONT);
		exit.setFont(FONT);
		nextBk.setFont(FONT);
		lastBk.setFont(FONT);
		clear.setFont(FONT);
		restore.setFont(FONT);
		activeBk.setFont(FONT);
		paintBkMask.setFont(FONT);
		freeMove.setFont(FONT);
		firework.setFont(FONT);
		spin.setFont(FONT);
		moonLanding.setFont(FONT);
		survive.setFont(FONT);
		flyingBlocks.setFont(FONT);
		
		skin.add(nextBk);
		skin.add(lastBk);
		skin.add(clear);
		skin.add(restore);
		skin.add(new JSeparator());
		skin.add(activeBk);
		skin.add(paintBkMask);
		skin.add(freeMove);
		skin.add(firework);
		skin.add(spin);
		
		games.add(moonLanding);
		games.add(survive);
		games.add(flyingBlocks);
		
		if(wordPanel!=null)
		{
			activeBk.setSelected(wordPanel.isActiveBackground());
			paintBkMask.setSelected(wordPanel.isPaintBkMask());
			freeMove.setSelected(wordPanel.isFreeMoveOpen());
			firework.setSelected(wordPanel.isFireworkEnable());
			spin.setSelected(wordPanel.isRotationEnable());
	
			alwaysOnTop.setSelected(wordPanel.isAlwaysOnTop());
			
			netSearch.setSelected(wordPanel.isOpenNetSearch());
		}
		
		initalizeLibrariesMenu();
		initializePersonalLibrariesMenu();
		
		//add listeners
		alwaysOnTop.addActionListener(this);
		iconify.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);
		netSearch.addActionListener(this);
		newPL.addActionListener(this);
		removePL.addActionListener(this);
		
		nextBk.addActionListener(this);
		lastBk.addActionListener(this);
		clear.addActionListener(this);
		restore.addActionListener(this);
		activeBk.addActionListener(this);
		paintBkMask.addActionListener(this);
		freeMove.addActionListener(this);
		firework.addActionListener(this);
		spin.addActionListener(this);	
		moonLanding.addActionListener(this);
		survive.addActionListener(this);
		flyingBlocks.addActionListener(this);
		
	}
	
	//按照libraryAccountor的内容初始化弹出菜单
	private void initalizeLibrariesMenu()
	{
		//装入并排序libAccArray
	    libAccArray=new LibraryAccount[LibraryManager.getLibraryAccountor().getLibraryAccounts().size()];
	    for(int i=0;i<libAccArray.length;i++)
	    {
	    	libAccArray[i]=LibraryManager.getLibraryAccountor().getLibraryAccounts().get(i);
	    }
	    
	    Arrays.sort(libAccArray,comparator= new Comparator<LibraryAccount>(){

			public int compare(LibraryAccount a, LibraryAccount b) 
			{
				if(null==a||null==b)
				{
					System.out.println("Error in MainPopupMenu's initalizeLibrariesMenu, null a or b in comparator");
					if(null==a&&null==b)
					{
						return 0;
					}
					else
					{
						if(null==a)
						{
							return 1;
						}
						else
						{
							return -1;
						}
					}
				}
				
				String AFrom=a.getFrom();
				String ATo=a.getTo();
				String BFrom=b.getFrom();
				String BTo=b.getTo();
				
				if(AFrom.equals(BFrom)&&ATo.equals(BTo))
				{
					return 0;
				}
				
				int AIndex=-1;
				int BIndex=-1;
				for(int i=0;i<DEFAULT_LIBRARY_FROM.length;i++)
				{
					if(AFrom.equals(DEFAULT_LIBRARY_FROM[i])
							&&ATo.equals(DEFAULT_LIBRARY_TO[i]))
					{
						AIndex=i;
					}
					
					if(BFrom.equals(DEFAULT_LIBRARY_FROM[i])
							&&BTo.equals(DEFAULT_LIBRARY_TO[i]))
					{
						BIndex=i;
					}
				}
				
				if(AIndex>=0&&BIndex>=0)
				{
					//a 和 b 都是标准词库之一，那么英汉词库优先级第一，汉英词库优先级第二
					return DEFAULT_LIBRARY_PRIORITY[AIndex]-DEFAULT_LIBRARY_PRIORITY[BIndex];
				}
				else
				{
					if(AIndex>=0)
					{
						return -1;
					}
					else
					{
						if(BIndex>=0)
						{
							return 1;
						}
						else
						{
							return AFrom.compareTo(BFrom);
						}
					}
				}
			}
	    	
	    });
		
	    //加入按钮
		libMs=new JCheckBoxMenuItem[libAccArray.length];
		for(int i=0;i<libAccArray.length;i++)
        {
			LibraryAccount libAccount=libAccArray[i];
			
			if(i>0&&comparator.compare(libAccArray[i-1],libAccount)!=0)
			{
				System.out.println("separator added");
				libraries.add(new JSeparator());
			}
			
			if(libAccount!=null)
			{
				libMs[i]=new JCheckBoxMenuItem("Library "+libAccount.getName()+" translating "+libAccount.getFrom()+" to "+libAccount.getTo());
				libraries.add(libMs[i]);
			}
			else
			{
				System.out.println("Error in MainPopupMenu's initalizeLibrariesMenu, null libAccount in libAccounts");
				libMs[i]=new JCheckBoxMenuItem("Library null");
			}
			
			libMs[i].addActionListener(this);
			libMs[i].setFont(FONT);
        }
		
		//set selected
		boolean[] selecteds=null;
		try
		{
			ObjectInputStream out=new ObjectInputStream(new FileInputStream(MAJOR_LIBRARY_FILE));
			selecteds=(boolean[])(out.readObject());
			out.close();
			
			ArrayList<LibraryAccount> libAccounts=LibraryManager.getLibraryAccountor().getLibraryAccounts();
			for(int i=0;i<libAccounts.size();i++)
			{
				if(selecteds[i])
				{
					int index=-1;
					for(int j=0;j<libAccArray.length;j++)
					{
						if(libAccArray[j].equals(libAccounts.get(i)))
						{
							index=j;
							break;
						}
					}
					
					if(index>=0)
					{
						libMs[index].setSelected(true);
					}
					else
					{
						System.out.println("Error in MainPopupMenu, failed to find index of libMs");
					}
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error in MainPopupMenu, failed to read selecteds, may be caused by out-of-dated file");
		    ex.printStackTrace();
		}
		
	}

	//初始化personalLibraries
	private void initializePersonalLibrariesMenu()
	{
		personalLibraries.removeAll();
		
		//加入newPL
		personalLibraries.add(newPL);
		personalLibraries.add(new JSeparator());
		
		//加入生词本
		java.util.List<PersonalLibrary> libraries=PersonalLibrary.getLibraryAccountor().getLibraries();
		pls=new JMenuItem[libraries.size()];
		for(int i=0;i<libraries.size();i++)
		{
			PersonalLibrary pl=libraries.get(i);
			if(pl!=null)
			{
				pls[i]=new JMenuItem(" "+pl.getName()+" ");
				
				pls[i].addActionListener(this);
				pls[i].setFont(FONT);
				personalLibraries.add(pls[i]);
			}
			else
			{
				pls[i]=null;
				System.out.println("Error in initializePersonalLibrariesMenu, null pl["+i+"]");
			}
		}
		
		//加入remove按钮
		personalLibraries.add(new JSeparator());
		personalLibraries.add(removePL);
		
	}
	
	/**
	 * 弹出之前应进行刷新，才能保证数据的正确性
	 */
	public void refresh()
	{
		initializePersonalLibrariesMenu();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(null==wordPanel)
		{
			return;
		}
		
		if(e.getSource()==netSearch)
		{
			wordPanel.setOpenNetSearch(netSearch.isSelected());
		}
		
		if(e.getSource()==alwaysOnTop)
		{
			wordPanel.setAlwaysOnTop(alwaysOnTop.isSelected());
		}
		
		if(e.getSource()==iconify)
		{
			wordPanel.getDictFrame().setState(JFrame.ICONIFIED);
		    wordPanel.getDictFrame().windowIconified(null);  //模拟发起一次事件
		}

		if(e.getSource()==about)
		{
			AboutDialog.showDialog(wordPanel.getDictFrame());
		}
		
		if(e.getSource()==exit)
		{
			System.exit(0);
		}
				
		if(e.getSource()==nextBk)
		{
			wordPanel.scrollBackground(true);
		}
		
		if(e.getSource()==lastBk)
		{
			wordPanel.scrollBackground(false);
		}
		
		if(e.getSource()==clear)
		{
			wordPanel.clearBackground();
		}
		
		if(e.getSource()==restore)
		{
			wordPanel.restoreBackground();
		}
		
		if(e.getSource()==activeBk)
		{
			wordPanel.setActiveBackground(activeBk.isSelected());
		}
		
		if(e.getSource()==paintBkMask)
		{
			wordPanel.setPaintBkMask(paintBkMask.isSelected());
		}
		
		if(e.getSource()==freeMove)
		{
			wordPanel.setFreeMoveOpen(freeMove.isSelected());
		}
		
		if(e.getSource()==firework)
		{
			wordPanel.setFireworkEnable(firework.isSelected());
		}
		
		if(e.getSource()==spin)
		{
			wordPanel.setRotationEnable(spin.isSelected());
		}
		
		for(int i=0;i<libMs.length;i++)
		{
			if(e.getSource()==libMs[i])
			{
				if(libMs[i].isSelected())
				{
					wordPanel.enactive(libAccArray[i]);
				}
				else
				{
					wordPanel.deactive(libAccArray[i]);
				}
			}
		}
		
		if(e.getSource()==newPL)
		{
			wordPanel.showNewPersonalLibraryDialog();
		}
		
		if(e.getSource()==removePL)
		{
			wordPanel.showRemovePersonalLibraryDialog();
		}
		
		for(int i=0;i<pls.length;i++)
		{
			if(e.getSource()==pls[i])
			{
				wordPanel.showPersonalLibraryDialog(i);
			}
		}
		
		if(e.getSource()==moonLanding)
		{
			new games.moonLanding.GameFrame();
		}
		
		if(e.getSource()==survive)
		{
			new games.survive.GameFrame();
		}
		
		if(e.getSource()==flyingBlocks)
		{
			new games.flyingBlocks.GameFrame();
		}
		
	}

	public Comparator<LibraryAccount> getComparator() {
		return comparator;
	}
	
}
