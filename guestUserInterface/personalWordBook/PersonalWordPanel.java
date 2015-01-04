package guestUserInterface.personalWordBook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import libraryInterface.*;
import libraryManager.*;
import javax.swing.table.*;
import settings.*;
import javax.swing.border.*;
import guestUserInterface.*;
import java.util.*;

public class PersonalWordPanel extends JPanel
implements ActionListener, MouseListener, Constants
{

	//for debug
	private static final boolean debug=false;
	private static final boolean debug2=true;
	
	public static final Dimension DEFAULT_SIZE=new Dimension(500,400);
	public static final Dimension SEARCH_BUTTON_SIZE=new Dimension(80,40); 
	public static final Dimension TABLE_HEADER_SIZE=new Dimension(460,40);
	public static final Dimension TABLE_SIZE=new Dimension(TABLE_HEADER_SIZE.width,300);
	
	
	private JButton searchButton=new JButton("Search");
	private JTextField searchField=new JTextField();
	
	private JTable wordTable;
	
	//��������
	private JButton addButton=new JButton("Add");
	
	//�ƶ�����
	private JButton upButton=new JButton("Up");
	private JButton downButton=new JButton("Down");
	private JButton topButton=new JButton("Top");
	private JButton bottomButton=new JButton("Bottom");

	//�ʿⲥ�Ź���
	private JButton playButton=new JButton("P.L.");
	
	//̸���˵�
	private JPopupMenu pop;  //��ʱ��û�м���
	
	//�ʿ�
	private PersonalLibraryManager libraryManager;
	
	private PersonalWordDialog wordDialog;
	/**
	 * �����indexָ��������ʱ��Ľ����Ǻ���һ�����ʱ�
	 * �ʿ�󶨵�
	 * @param index
	 */
	public PersonalWordPanel(int index, PersonalWordDialog wordDialog)
	{	
		//=��ʼ������=
		this.wordDialog=wordDialog;
		this.libraryManager=PersonalLibraryManager.createPersonalLibraryManager(index);
		if(null==libraryManager)
		{
			throw new IllegalArgumentException("Error in PersonalWordPanel's initialization, failed to create PersonalLibraryManager");
		}
		setLayout(new BorderLayout());
		setPreferredSize(DEFAULT_SIZE);
		
		//=�����ʼ��=
		searchField.setSelectionColor(new Color(128,255,255));
		searchField.setSelectedTextColor(Color.BLACK);
				
		wordTable=new JTable(new MyTableMode());
		wordTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		wordTable.getColumnModel().getColumn(1).setPreferredWidth(DEFAULT_SIZE.width-wordTable.getColumnModel().getColumn(0).getPreferredWidth());
		wordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wordTable.setPreferredScrollableViewportSize(TABLE_SIZE);
		wordTable.getTableHeader().setPreferredSize(TABLE_HEADER_SIZE);
		wordTable.setSelectionBackground(new Color(100,158,232));
		
		wordTable.getTableHeader().setDefaultRenderer(new CellRenderer());
		wordTable.setDefaultRenderer(Object.class, new CellRenderer());
		wordTable.setDefaultEditor(Object.class, new CellEditor());
		
		searchButton.setPreferredSize(SEARCH_BUTTON_SIZE);
		
		playButton.setToolTipText("  Play Library  ");
		
		refreshWordTable();
		
		//=�������=
		//������
		JScrollPane scrollPane=null;
		JPanel p1=new JPanel(new BorderLayout());
		p1.add(wordTable.getTableHeader(), BorderLayout.PAGE_START);
		p1.add(wordTable, BorderLayout.CENTER);
		this.add(scrollPane=new JScrollPane(p1),BorderLayout.CENTER);
		scrollPane.setWheelScrollingEnabled(true);
		
		//����������
		JPanel p2=new JPanel(new BorderLayout());
		p2.add(searchButton, BorderLayout.WEST);
		p2.add(searchField, BorderLayout.CENTER);
		this.add(p2, BorderLayout.NORTH);
		
		//���������
		this.add(new ControlPanel(), BorderLayout.SOUTH);
		
		//=���������=
		addButton.addActionListener(this);
		searchButton.addActionListener(this);
		searchField.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		topButton.addActionListener(this);
		bottomButton.addActionListener(this);
		playButton.addActionListener(this);
		
		wordTable.addMouseListener(this);
		
		//=��ʼ�������˵�=
		pop=new PersonalPopupMenu(this);
		
		
		//=��ʼ=
		if(debug)
		{
			System.out.println("totalNumOfWords: "+libraryManager.size());
		}
	}
	
	/**
	 * �������������ָ����ѡ��
	 * @param index
	 */
	public void setSelectedRow(int index)
	{
		if(index>=0&&index<libraryManager.size())
		{
			wordTable.getSelectionModel().setSelectionInterval(index, index);
			wordTable.scrollRectToVisible(wordTable.getCellRect(index,0, true));
		}
	}
	
	
	/**
	 * ��������Զ�����һ���µĿյĴ�����
	 */
	public void insertEntry()
	{
		int index=-1;
		if((index=wordTable.getSelectedRow())!=-1)
		{
			libraryManager.insert(index,new PersonalLibraryEntry("empty", "empty"));
		    refreshWordTable();
		}
	}
	
	/**
	 * �����������libraryManager������ˢ��wordTable
	 */
	public void refreshWordTable()
	{
		wordTable.setModel(new MyTableMode());
	}
	/**
	 * ɾ����focus��һ�У��ڴʿ�ͱ����
	 */
	public void removeEntry()
	{
		int index=-1;
		if((index=wordTable.getSelectedRow())!=-1)
		{
			libraryManager.remove(index);
			refreshWordTable();
		}
	}
	
	/**
	 * �ڴʿ�ͱ���н�ѡ�е�һ���ö�
	 */
	public void topMoveEntry()
	{
		int index=-1;
		if((index=wordTable.getSelectedRow())!=-1)
		{
			libraryManager.topMove(index);
			refreshWordTable();
			setSelectedRow(1);
		}
	}
	
	/**
	 * 
	 * ����������ʿ������е��ʼ�����������������һ���ַ�����
	 * ��ѡ�е㿪ʼ�����û��ѡ�е㣬��ӵ�һ�п�ʼ
	 */
	private String generateAllWordsString()
	{
		return generateAllWordsString(wordTable.getSelectedRow());
	}
	
	/**
	 * ����������ʿ������е��ʼ�����������������һ���ַ�����
	 * ��index��ʼ
	 * @param index
	 * @return
	 */
	private String generateAllWordsString(int index)
	{
		if(index<1||index>=libraryManager.size()-1)
		{
			index=1;  //�ʿ�ĵ�һ�������һ��������Ԥ���Ŀմ���
		}
		
		StringBuffer preOutput=new StringBuffer();
		StringBuffer pastOutput=new StringBuffer();
		java.util.List<PersonalLibraryEntry> entries=libraryManager.getLibrary().getEntries();
		ListIterator<PersonalLibraryEntry> iterator=entries.listIterator();
		iterator.next();
		for(int i=1;i<libraryManager.size()-1;i++)
		{
			PersonalLibraryEntry ple=iterator.next();
			if (ple != null) 
			{
				if (i < index)
				{
				    preOutput.append(ple.getWord() + ":   " + ple.getTrans()+"  ,        ");
				} 
				else 
				{
					pastOutput.append(ple.getWord() + ":   " + ple.getTrans()+"  ,        ");
				}
			}
			
		}
		
		if(debug2)
		{
			System.out.println("generateAllWordsString(): "+
					pastOutput.append(preOutput).toString());
		}
		
		return pastOutput.append(preOutput).toString();
			
	}
	
	public PersonalWordDialog getWordDialog() {
		return wordDialog;
	}
	
	/**
	 * �ڴʿ�ͱ���н�ѡ�е�һ���õ�
	 */
	public void bottomMoveEntry()
	{
		int index=-1;
		if((index=wordTable.getSelectedRow())!=-1)
		{
			libraryManager.bottomMove(index);
			refreshWordTable();
			setSelectedRow(libraryManager.size()-2);
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==addButton)
		{
			libraryManager.append(new PersonalLibraryEntry("empty", "empty"));
     		refreshWordTable();
		}
		
		if(e.getSource()==searchButton)
		{
			int index=libraryManager.search(searchField.getText().trim());
			
			if(index>=0)
			{
				wordTable.scrollRectToVisible(wordTable.getCellRect(index,0, false));
			    setSelectedRow(index);
			}
			else
			{
				ShowMessageDialog.showDialog(getWordDialog(), "Sorry, word not found!");
			}
		}
		
		if(e.getSource()==searchField)
		{
            int index=libraryManager.search(searchField.getText().trim());
			
			if(index>=0)
			{
				wordTable.scrollRectToVisible(wordTable.getCellRect(index,0, false));
				setSelectedRow(index);
			}
			else
			{
				ShowMessageDialog.showDialog(getWordDialog(), "Sorry, word not found!");
			}
		}
		
		if(e.getSource()==upButton)
		{
			int index=-1;
			if(libraryManager.upMove(index=wordTable.getSelectedRow()))
			{
				refreshWordTable();
				setSelectedRow(index-1);
			}
		    
		}
		
		if(e.getSource()==downButton)
		{
			int index=-1;
			if(libraryManager.downMove(index=wordTable.getSelectedRow()))
			{
				refreshWordTable();
				setSelectedRow(index+1);
			}
		}
		
		if(e.getSource()==topButton)
		{
			if(libraryManager.topMove(wordTable.getSelectedRow()))
			{
				refreshWordTable();
				setSelectedRow(1);
			}
		}
		
		if(e.getSource()==bottomButton)
		{
			if(libraryManager.bottomMove(wordTable.getSelectedRow()))
			{
				refreshWordTable();
				setSelectedRow(libraryManager.size()-2);
			}
		}
		
		if(e.getSource()==playButton)
		{
			new PLDialog(generateAllWordsString());
		}
		
	}

	public void mouseClicked(MouseEvent e) 
	{
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) 
	{
		
	
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if(e.getSource()==wordTable)
		{
			if(e.isPopupTrigger())
		    {
				int index=wordTable.getSelectedRow();
				if(index!=-1)
				{
					if(wordTable.getCellRect(index,0,true).union(wordTable.getCellRect(index,1,true)).contains(e.getPoint()))
					{
						pop.show((Component)(e.getSource()),e.getX(),e.getY());
					}
				}
			    
		    }
		}
	}

	private class MyTableMode extends AbstractTableModel 
	{
		 public int getColumnCount() {
			return 2;
		}

		public int getRowCount() {
			return libraryManager.getLibrary().size();
		}

		public String getColumnName(int col) {
			switch (col) {
			case 0: {
				return "Words";
			}
			case 1: {
				return "Translations";
			}
			default: {
				return "";
			}
			}
		}

		public Object getValueAt(int row, int col) {
			PersonalLibraryEntry temp = libraryManager.get(row);
			if (temp != null) {
				return 0 == col ? temp.getWord() : temp.getTrans();
			} else {
				return null;
			}

		}

		//��������д���һ������
		public boolean isCellEditable(int row, int col) 
		{
			if(0==row||row==libraryManager.size()-1)
			{
				return false;
			}
			
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			if (debug) {
				System.out.println("table value changed");
			}

			if (1 == col)
			{
				if (value instanceof String) 
				{
					String temp = (String) value;
					libraryManager.rewriteTrans(row, temp);
				}
			}
			else
			{
				if (value instanceof String) 
				{
					String temp = (String) value;
					libraryManager.rewriteWord(row, temp);
				}
			}

			fireTableCellUpdated(row, col);
		}

	}
	
	private class TextFieldTableCellRenderer extends JTextField implements TableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			this.setHorizontalAlignment(JTextField.CENTER);
			
			System.out.println("renderer is invoked");
			
			setText((String)value);
			
			if(hasFocus)
			{
				setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
			}
			else
			{
				setBorder(null);
			}
			
			if(isSelected)
			{
				this.setBackground(new Color(128,255,255));
			}
			else
			{
				this.setBackground(SHARED_BACKGROUND);
			}
			
			return this;
		}
	}

	private class CellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			 setHorizontalAlignment(SwingConstants.CENTER);
			 if(hasFocus)
			 {
				 this.setBorder(new LineBorder(Color.YELLOW,2));
			 }
			 else
			 {
				 this.setBorder(null);
			 }
			 
			 setFont(new Font("Times", Font.BOLD, 14));
			 
			 if(isSelected)
			 {
				 super.setForeground(table.getSelectionForeground());
				 super.setBackground(table.getSelectionBackground());
			 }
			 else
			 {
				 super.setForeground(table.getForeground());
                 super.setBackground(table.getBackground());
			 }
			 
			 setValue(value);
			 
			 return this;
		}
	}

	private class CellEditor extends AbstractCellEditor implements TableCellEditor
	{
		private JTextField textField=new JTextField();
		
		public CellEditor()
		{
			textField.setBorder(null);
			textField.setMargin(new Insets(1,1,1,1));
			textField.setOpaque(false);
			textField.setFont(new Font("Times", Font.BOLD, 14));
			textField.setHorizontalAlignment(SwingConstants.CENTER);
		    textField.setBorder(new LineBorder(Color.YELLOW,2));
		    textField.setSelectionColor(new Color(128,255,255));
		    textField.setSelectedTextColor(Color.BLACK);
		    //textField.setBackground(new Color(100,158,232));
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) 
		{
			textField.setText(value.toString());
			return textField;
		}

		public Object getCellEditorValue()
		{
			return textField.getText();
		}
		
		
		
	}
	
	private class ControlPanel extends JPanel
	{
		public ControlPanel()
		{
			this.setLayout(new GridLayout(2,3,5,5));
			
			this.setBorder(new JTextField().getBorder());
			this.setBackground(new Color(61,137,233));
			this.setOpaque(true);
			
			this.add(addButton);
			this.add(upButton);
			this.add(downButton);
			this.add(topButton);
			this.add(bottomButton);
			this.add(playButton);	
			
			
		}
		
		public Insets getInsets()
		{
			return new Insets(15,20,15,20);
		}
	}

}
