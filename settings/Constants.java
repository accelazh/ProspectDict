package settings;

import java.awt.Color;
import java.io.*;

/**
 * ����ӿڶ���һЩ���õĳ���
 * 
 * @author ZYL
 *
 */
public interface Constants 
{
	/**
	 * ������ṩһЩĬ�ϵĴʿ⣬��Щ�ʿ�֧��������ң�������Щ�ʿ��
	 * LibraryAccount��from
	 */
	public static final String[] DEFAULT_LIBRARY_FROM={
		"EN","JP","FN","GE", "CH","CH","CH","CH",
	};
	/**
	 * ������ṩһЩĬ�ϵĴʿ⣬��Щ�ʿ�֧��������ң�������Щ�ʿ��
	 * LibraryAccount��to
	 */
	public static final String[] DEFAULT_LIBRARY_TO={
		"CH","CH","CH","CH", "EN","JP","FN","GE",
	};
	/**
	 * �����ֵ�ʿ��ڲ˵��е�����ֵС��������ǰ
	 */
	public static final int[] DEFAULT_LIBRARY_PRIORITY={
		 0,   4,   2,   3,    1,   7,   5,   6,                   
	};
	/**
	 * �ҵ���һ������ͬ���ĵ���ʱ��ȡǰNODEMATE_NUMBER��
	 */
	public static final int MAX_SAME_ROOT_WORD_NUMBER_WHEN_MAXIFIED=34;
	/**
	 * �ҵ���һ������ͬ���ĵ���ʱ��ȡǰNODEMATE_NUMBER��
	 */
	public static final int MAX_SAME_ROOT_WORD_NUMBER_WHEN_UNMAXIFIED=16;
	/**
	 * ��������οյ�ɫ
	 */
    public static final Color SHARED_BACKGROUND=new Color(27,75,146);  
	/**
	 * ��¼ѡ��ı������ļ�
	 */
	public static final File SELECTED_BK_FILE=new File("GUISource\\cfg\\selectedBk.dat");
	/**
	 * ��¼ѡ���activeBk�Ƿ񿪹ص��ļ�
	 */
	public static final File ACTIVE_BK_FILE=new File("GUISource\\cfg\\activeBk.dat");
	/**
	 * ��¼ѡ���paintBkMask�Ƿ񿪹ص��ļ�
	 */
	public static final File PAINT_BK_MASK_FILE=new File("GUISource\\cfg\\paintBkMask.dat");
	/**
	 * ��¼ѡ���freeMoving�Ƿ�򿪵��ļ�
	 */
	public static final File FREE_MOVE_FILE=new File("GUISource\\cfg\\freeMoving.dat");
	/**
	 * ��¼ѡ���firework�Ƿ񿪹ص��ļ�
	 */
	public static final File FIREWORK_BK_FILE=new File("GUISource\\cfg\\firework.dat");
	/**
	 * ��¼ѡ���firework��spinЧ���Ƿ񿪹ص��ļ�
	 */
	public static final File SPIN_BK_FILE=new File("GUISource\\cfg\\spin.dat");
    /**
     * ��¼����ʱ���ص�majorLibrary��д�����һ��integer
     */
	public static final File MAJOR_LIBRARY_FILE=new File("libraries\\cfg\\majorLibrary.dat");
    /**
     * ��¼����ʱ���ص�secondaryLibraries��д�����һ����������
     */
	public static final File SECONDARY_LIBRARY_FILE=new File("libraries\\cfg\\secondaryLibrary.dat");
}

