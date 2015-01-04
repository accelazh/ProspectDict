package games.moonLanding;

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
	private JLabel titleLabel=new JLabel("Config properties of the craft: ");
	
	private JTextField gravityField=new JTextField();
	private JTextField vLimitField=new JTextField();
	private JTextField leftAccField=new JTextField();
	private JTextField rightAccField=new JTextField();
	private JTextField upAccField=new JTextField();
	private JTextField downAccField=new JTextField();
	private JTextField totalFuelField=new JTextField();
	
	private JTextField[] fields=new JTextField[]{
			gravityField,
			vLimitField,
			downAccField,
			leftAccField,
			rightAccField,
			upAccField,
			totalFuelField,
	};
	
	private JLabel gravityLabel=new JLabel("Gravity");
	private JLabel vLimitLabel=new JLabel("Velocity Limit");
	private JLabel leftAccLabel=new JLabel("Left Acceleration");
	private JLabel rightAccLabel=new JLabel("Right Acceleration");
	private JLabel upAccLabel=new JLabel("Up Acceleration");
	private JLabel downAccLabel=new JLabel("Down Acceleration");
	private JLabel totalFuelLabel=new JLabel("Total Fuel");
	
	private JLabel[] labels=new JLabel[]{
			gravityLabel,
			vLimitLabel,
			downAccLabel,
			leftAccLabel,
			rightAccLabel,
			upAccLabel,
			totalFuelLabel,
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
			wrap.setGravity(Double.parseDouble(gravityField.getText()));
			wrap.setVLimit(Double.parseDouble(vLimitField.getText()));
			wrap.setDownAcc(Double.parseDouble(downAccField.getText()));
			wrap.setUpAcc(Double.parseDouble(upAccField.getText()));
			wrap.setLeftAcc(Double.parseDouble(leftAccField.getText()));
			wrap.setRightAcc(Double.parseDouble(rightAccField.getText()));
			wrap.setTotalFuel((int) Double.parseDouble(totalFuelField.getText()));
			
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
		gravityField.setText(""+ConfigWrap.default_gravity);
		vLimitField.setText(""+ConfigWrap.default_vLimit);
		downAccField.setText(""+ConfigWrap.default_downAcc);
		leftAccField.setText(""+ConfigWrap.default_leftAcc);
		rightAccField.setText(""+ConfigWrap.default_rightAcc);
		upAccField.setText(""+ConfigWrap.default_upAcc);
		totalFuelField.setText(""+ConfigWrap.default_totalFuel);
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
