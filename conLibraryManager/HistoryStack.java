package conLibraryManager;

import java.util.*;

/**
 * ���������ʵ����ʷ��¼���ܣ�����
 * ����װ��ʷ��¼�����ݽṹ
 * 
 * @author ZYL
 *
 */
class HistoryStack<T>
{
	private List<T> elements=new LinkedList<T>(); 
	
	private int size;
	private int pointer;
	private int capacity;
	
	public HistoryStack(int capacity)
	{
		 if (capacity <= 0)
	            throw new IllegalArgumentException("Illegal Capacity: "+
	                                               capacity);
		
		this.capacity=capacity;
		size=0;
		pointer=-1;
	}
	
	public HistoryStack()
	{
		this(10);
	}
	
	/**
	 * װ��һ��Ԫ�أ����������ɾ������װ����Ǹ�Ԫ��
	 * װ���Ԫ����ָ��pointer��������Ԫ�ؽ�������
	 * ɾ��
	 * 
	 */
	public void push(T element)
	{
		if(pointer<capacity-1)
		{
			elements.add(++pointer, element);
			size=pointer+1;;
		}
		else
		{
			elements.remove(0);
			elements.add(element);
		}
	}
	
	/**
	 * ����pointer����Ԫ�أ�����ɾ��,
	 * pointer���ƶ�
	 * 
	 */
	public T peek()
	{
		if(pointer>=0)
		{
			return elements.get(pointer);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * ����pointer֮ǰ��һ��Ԫ�أ�pointer��ǰ
	 * �ƶ�һλ,��ɾ�������pointer=0���򷵻�null
	 * 
	 */
	public T back()
	{
		if(pointer>0)
		{
			return elements.get(--pointer);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * ����pointer֮���һ��Ԫ�أ�pointer���
	 * �ƶ�һλ,��ɾ�������pointer=size-1���򷵻�null
	 * 
	 */
	public T forth()
	{
		if(pointer<size-1)
		{
			return elements.get(++pointer);
		}
		else
		{
			return null;
		}
	}
	
	public int size()
	{
		return size;
	}
	
	/**
	 * ����ɾ������Ԫ��
	 */
	public void clear()
	{
		pointer=-1;
		size=0;
	}
	
	//test
	/*public static void main(String[] args)
	{
		HistoryStack<String> hs=new HistoryStack<String>(5);
		hs.push("1");
		hs.push("2");
		hs.push("3");
		hs.push("4");
		hs.push("5");
		hs.push("6");
		hs.push("7");
		System.out.println(hs.back());
		System.out.println(hs.back());
		System.out.println(hs.back());
		System.out.println(hs.back());
		
		System.out.println(hs.forth());
		System.out.println(hs.forth());
		System.out.println(hs.forth());
		System.out.println(hs.forth());
		System.out.println(hs.forth());
		System.out.println(hs.forth());
		System.out.println(hs.forth());
	}*/

}
