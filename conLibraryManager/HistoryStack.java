package conLibraryManager;

import java.util.*;

/**
 * 这个类用来实现历史记录功能，这是
 * 用来装历史记录的数据结构
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
	 * 装入一个元素，如果满了则删除最先装入的那个元素
	 * 装入的元素在指针pointer处，其后的元素将被懒惰
	 * 删除
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
	 * 返回pointer处的元素，但不删除,
	 * pointer不移动
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
	 * 返回pointer之前的一个元素，pointer向前
	 * 移动一位,不删除，如果pointer=0，则返回null
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
	 * 返回pointer之后的一个元素，pointer向后
	 * 移动一位,不删除，如果pointer=size-1，则返回null
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
	 * 懒惰删除所有元素
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
