package games.flyingBlocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.*;
import java.util.*;

public class MyBlock extends Block
{
	public static final Dimension SIZE=new Dimension(40,40);
	
	public MyBlock()
	{
		setWidth(SIZE.width);
		setHeight(SIZE.height);
		
		setX((GamePanel.DEFAULT_SIZE.width-SIZE.width)/2);
		setY((GamePanel.DEFAULT_SIZE.height-SIZE.height)/2);
		
		setIcon(new ImageIcon("GUISource/games/flyingBlocks/myBlock.gif"));
	}
	
	public void setLocation(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	public void setLocation(Point p)
	{
		setX(p.x);
		setY(p.y);
	}
	
	public boolean hitBorder(int side)
	{
		switch(side)
		{
		case UP:
		{
			if(getY()<=GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case DOWN:
		{
			if(getY()+getHeight()>=
				GamePanel.DEFAULT_SIZE.height
				-GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case LEFT:
		{
			if(getX()<=GamePanel.BORDER_WIDTH)
			{
				return true;
			}
			break;
		}
		case RIGHT:
		{
			if(getX()+getWidth()>=
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
	
	public boolean hitBorder()
	{
		return hitBorder(UP)||hitBorder(DOWN)
		    ||hitBorder(LEFT)||hitBorder(RIGHT);
	}
	
	public boolean hitBlock(Block b)
	{
		return getRect().intersects(b.getRect());
	}
	
	public boolean hitBlock(java.util.List<Block> b)
	{
		Iterator<Block> iterator=b.iterator();
        while(iterator.hasNext())
        {
        	if(hitBlock(iterator.next()))
        	{
        		return true;
        	}
        }
        
        return false;
	}
	
	

}
