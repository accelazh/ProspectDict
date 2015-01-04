package games.survive;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * 配置面板的界面panel
 * @author ZYL
 *
 */
public class ConfigPanel extends JPanel 
implements ActionListener
{
	private JLabel titleLabel=new JLabel("Config properties: ");
	
	private JTextField ballAVField=new JTextField();
	private JTextField craftVField=new JTextField();
	private JTextField ballGTField=new JTextField();
	
	private JTextField[] fields=new JTextField[]{
			ballAVField,
			craftVField,
			ballGTField,
	};
	
	private JLabel ballAVLabel=new JLabel("Ball Average Velocity");
	private JLabel craftVLabel=new JLabel("Craft Velocity ");
	private JLabel ballGTLabel=new JLabel("Ball Generating Cycle");
	
	private JLabel[] labels=new JLabel[]{
			ballAVLabel,
			craftVLabel,
			ballGTLabel,
	};
	
	private JButton okButton=new JButton("OK");
	private JButton rankButton=new JButton("Rank");  //用来显示记录分数的文件
	private JButton resetButton=new JButton("Reset");
	private JButton cancelButton=new JButton("Cancel");
	
	private GamePanel gamePanel;
	private ConfigDialog configFrame;
	
	public ConfigPanel(GamePanel gamePanel, ConfigDialog configFrame)
	{
		
		this.gamePanel=gamePanel;
		this.configFrame=configFrame;
		this.setLayout(new BorderLayout(5,5));
		
		//set components
		setOpaque(true);
		
		for(int i=0;i<fields.length;i++)
		{
			fields[i].setBorder(new LineBorder(Color.WHITE, 1));
			fields[i].setSelectionColor(new Color(128,255,255));
			fields[i].setSelectedTextColor(Color.BLACK);	
		}
		
		Dimension tempSize=new Dimension(60,26);
		okButton.setPreferredSize(tempSize);
		rankButton.setPreferredSize(tempSize);
		resetButton.setPreferredSize(tempSize);
		cancelButton.setPreferredSize(tempSize);
				
		//add components
		JPanel p3=new JPanel(new BorderLayout(5,5));
		p3.add(titleLabel, BorderLayout.CENTER);
		titleLabel.setFont(new Font("Times", Font.BOLD, 14));
		p3.add(new JSeparator(), BorderLayout.SOUTH);
		this.add(p3, BorderLayout.NORTH);
		
		JPanel p1=new JPanel(new GridLayout(labels.length,1,5,5));
		JPanel[] p1k=new JPanel[labels.length];
		for(int i=0;i<p1k.length;i++)
		{
			p1k[i]=new JPanel(new GridLayout(1,2,10,5));
			p1k[i].add(fields[i]);
			p1k[i].add(labels[i]);
			p1.add(p1k[i]);
		}
		
		this.add(p1, BorderLayout.CENTER);
		
		JPanel p2=new JPanel(new BorderLayout(5,5));
		p2.add(new JSeparator(), BorderLayout.NORTH);
		JPanel p21=new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		p21.add(okButton);
		//p21.add(rankButton);
		p21.add(resetButton);
		p21.add(cancelButton);
		p2.add(p21, BorderLayout.CENTER);
		
		this.add(p2, BorderLayout.SOUTH);
		
		//add listeners
		okButton.addActionListener(this);
		rankButton.addActionListener(this);
		resetButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		//start
		reset();
		
	}
	
	/**
	 * 当用户输入完成后，调用这个方法将输入值包装成ConfigWrap的对象，
	 * 如果输入不合法，将返回null
	 * @return
	 */
	public ConfigWrap wrapValues()
	{
		ConfigWrap wrap=new ConfigWrap();
		
		try
		{
			wrap.setBallAV(Double.parseDouble(ballAVField.getText()));
			wrap.setCraftV(Double.parseDouble(craftVField.getText()));
			wrap.setBallGT(Integer.parseInt(ballGTField.getText()));
			
			if (wrap.checkValid())
			{
				return wrap;
			} 
			else
			{
				return null;
			}
			
		} 
		catch (Exception e)
		{
			return null;
		}
		
	}
	
	public void reset()
	{
		ballAVField.setText(""+ConfigWrap.default_ballAV);
		craftVField.setText(""+ConfigWrap.default_craftV);
		ballGTField.setText(""+ConfigWrap.default_ballGT);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==cancelButton)
		{
			if(configFrame!=null)
			{
				configFrame.dispose();
			}
		}
		
		if(e.getSource()==rankButton)
		{
			GradeViewer.showDialog();
		}
		
		if(e.getSource()==resetButton)
		{
			reset();
		}
		
		if(e.getSource()==okButton)
		{
			if(configFrame!=null&&gamePanel!=null)
			{
				ConfigWrap wrap=null;
				if((wrap=wrapValues())!=null)
				{
					gamePanel.setValues(wrap);
					configFrame.dispose();
				}
				
			}
		}
		
		
	}

	public Insets getInsets()
	{
		return new Insets(8,10,8,10);
	}
}
