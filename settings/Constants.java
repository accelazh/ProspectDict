package settings;

import java.awt.Color;
import java.io.*;

/**
 * 这个接口定义一些公用的常量
 * 
 * @author ZYL
 *
 */
public interface Constants 
{
	/**
	 * 本软件提供一些默认的词库，这些词库支持网络查找，这是这些词库的
	 * LibraryAccount的from
	 */
	public static final String[] DEFAULT_LIBRARY_FROM={
		"EN","JP","FN","GE", "CH","CH","CH","CH",
	};
	/**
	 * 本软件提供一些默认的词库，这些词库支持网络查找，这是这些词库的
	 * LibraryAccount的to
	 */
	public static final String[] DEFAULT_LIBRARY_TO={
		"CH","CH","CH","CH", "EN","JP","FN","GE",
	};
	/**
	 * 定义字典词库在菜单中的排序，值小的排列在前
	 */
	public static final int[] DEFAULT_LIBRARY_PRIORITY={
		 0,   4,   2,   3,    1,   7,   5,   6,                   
	};
	/**
	 * 找到和一个单词同结点的单词时，取前NODEMATE_NUMBER个
	 */
	public static final int MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED=34;
	/**
	 * 找到和一个单词同结点的单词时，取前NODEMATE_NUMBER个
	 */
	public static final int MAX_SAME_ROOT_WORD_NUMBER_WHEN_UNMAXIFIED=16;
	/**
	 * 各组件的镂空底色
	 */
    public static final Color SHARED_BACKGROUND=new Color(27,75,146);  
	/**
	 * 记录选择的背景的文件
	 */
	public static final File SELECTED_BK_FILE=new File("GUISource\\cfg\\selectedBk.dat");
	/**
	 * 记录选择的activeBk是否开关的文件
	 */
	public static final File ACTIVE_BK_FILE=new File("GUISource\\cfg\\activeBk.dat");
	/**
	 * 记录选择的paintBkMask是否开关的文件
	 */
	public static final File PAINT_BK_MASK_FILE=new File("GUISource\\cfg\\paintBkMask.dat");
	/**
	 * 记录选择的freeMoving是否打开的文件
	 */
	public static final File FREE_MOVE_FILE=new File("GUISource\\cfg\\freeMoving.dat");
	/**
	 * 记录选择的firework是否开关的文件
	 */
	public static final File FIREWORK_BK_FILE=new File("GUISource\\cfg\\firework.dat");
	/**
	 * 记录选择的firework的spin效果是否开关的文件
	 */
	public static final File SPIN_BK_FILE=new File("GUISource\\cfg\\spin.dat");
    /**
     * 记录启动时加载的majorLibrary，写入的是一个integer
     */
	public static final File MAJOR_LIBRARY_FILE=new File("libraries\\cfg\\majorLibrary.dat");
    /**
     * 记录启动时加载的secondaryLibraries，写入的是一个整数数组
     */
	public static final File SECONDARY_LIBRARY_FILE=new File("libraries\\cfg\\secondaryLibrary.dat");
}

