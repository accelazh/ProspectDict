package games.flyingBlocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.*;

public class Block implements Constants
{
	private double width=0;
	private double height=0;
	
	private double x=0;
	private double y=0;
	
	private double vx=0;
	private double vy=0;
	
	private ImageIcon icon=new ImageIcon("GUISource/games/flyingBlocks/cpuBlock.gif");
	
	public Block()
	{
		
	}
	
	public Block(double x, double y, double width, double height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		
		double v=Configure.averageV;
		double arc=2*Math.PI*Math.random();
		this.vx=v*Math.cos(arc);
		this.vy=v*Math.sin(arc);
	}
	
	public void paint(Graphics g, ImageObserver o)
	{
		g.drawImage(icon.getImage(), (int)x, (int)y, (int)width, (int)height, o);
	}
	
	/**
	 * 是否已经撞到了游戏界面的边界,
	 * side指明是哪一边的边界，side的合法值有：
	 *  UP,DOWN,LEFT,RIGHT
	 */
	public boolean hitBorder(int side)
	{
		switch(side)
		{
		case UP:
		{
			if(vy<0&&y<=GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case DOWN:
		{
			if(vy>0&&y+height>=
				GamePanel.DEFAULT_SIZE.height
				-GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case LEFT:
		{
			if(vx<0&&x<=GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case RIGHT:
		{
			if(vx>0&&x+width>=
				GamePanel.DEFAULT_SIZE.width
				-GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		default:
		{
			break;
		}
		}
		
		return false;
	}

	public void move()
	{
		x+=vx*TIMER_INTERVAL*1.0/1000;
		y+=vy*TIMER_INTERVAL*1.0/1000;
		
		if(hitBorder(UP)||(hitBorder(DOWN)))
		{
			vy=-vy;
		}
		
		if(hitBorder(LEFT)||(hitBorder(RIGHT)))
		{
			vx=-vx;
		}
		
	}
	
	public Rectangle getRect()
	{
		return new Rectangle((int)x,(int)y,(int)width,(int)height);
	}
	
	public boolean contains(Point p)
	{
		return getRect().contains(p);
	}
	
	public void selfProcess()
	{
		move();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	
}
