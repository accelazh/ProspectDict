package games.survive;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;

/**
 * 提供浏览过去的分数的功能
 * @author ZYL
 *
 */
public class GradeViewer extends JPanel
{
	public static final Dimension DEFAULT_SIZE=new Dimension(408,300);
	
	private JTextArea info=new JTextArea();
	
	public GradeViewer()
	{
		try
		{
			BufferedReader in=new BufferedReader(new FileReader(GamePanel.GRADES_FILE));
			String temp=null;
			StringBuffer buffer=new StringBuffer();
			while((temp=in.readLine())!=null)
			{
				if(temp.length()>0&&temp.charAt(0)!='=')
				{
					buffer.append("    ");
				}
				buffer.append(temp);
				buffer.append("\n");
			}
			
			info.setText(buffer.toString());
		}
		catch(IOException ex)
		{
			info.setText("    Sorry, error occurs!");
		}
		
		info.setEditable(false);
		
		this.setLayout(new BorderLayout(5,5));
		add(new JScrollPane(info), BorderLayout.CENTER);
		
		setPreferredSize(DEFAULT_SIZE);
	}
	
	public static void showDialog()
	{
		final JDialog dialog=new JDialog();
		dialog.setLayout(new BorderLayout(5,5));
		dialog.getContentPane().add(new GradeViewer(), BorderLayout.CENTER);
		
		JButton okButton=new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.dispose();
			}
		});
		
		JPanel p1=new JPanel(new BorderLayout(5,5));
		p1.add(new JSeparator(), BorderLayout.NORTH);
		JPanel p11=new JPanel(new FlowLayout());
		p11.add(okButton);
		p1.add(p11, BorderLayout.CENTER);
		dialog.getContentPane().add(p1, BorderLayout.SOUTH);
		
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setTitle("Grades View");
		dialog.setAlwaysOnTop(true);
		
		dialog.setVisible(true);
		
		
			
		
	}

}
