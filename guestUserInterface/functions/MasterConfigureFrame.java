package guestUserInterface.functions;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * 所有管理设置的GUI面板在这里汇总,生成JFrame。
 * 运行配置界面从这里开始。
 * @author ZYL
 *
 */
public class MasterConfigureFrame extends JFrame
{
	public MasterConfigureFrame()
	{
		setLayout(new BorderLayout());
		getContentPane().add(new MasterConfigure());
		setTitle("Configure");
		setSize(500+20,400+35);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		JFrame frame=new MasterConfigureFrame();
	}
}
