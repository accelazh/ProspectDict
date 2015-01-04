package guestUserInterface.functions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Í¼Æ¬ä¯ÀÀÆ÷
 * @author ZYL
 *
 */
public class ImageViewer extends JPanel
{
	private Image image=null;
	private boolean stretched=true;
	private int xCoordinate=0;
	private int yCoordinate=0;
	
	private ImageIcon none=new ImageIcon("pic/showPic/none.jpg");
	
	public ImageViewer()
	{
		image=null;
	}
	public ImageViewer(String locator)
	{
		ImageIcon imageIcon=new ImageIcon(locator);
		this.image=imageIcon.getImage();
		repaint();
	}
	public ImageViewer(Image image)
	{
		this.image=image;
		repaint();
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Image imageNone=none.getImage();
		g.drawImage(imageNone,0,0,
				getSize().width,getSize().height, this);
		if(image!=null)
		{
			if(isStretched())
			{
				g.drawImage(image,0,0,
						getSize().width,getSize().height, this);
			}
			else
			{
				xCoordinate=(getSize().width-image.getWidth(this))/2;
				int width=image.getWidth(this);
				yCoordinate=(getSize().height-image.getHeight(this))/2;
				int height=image.getHeight(this);
				g.drawImage(image,xCoordinate,yCoordinate,this);
				
				g.setColor(Color.BLUE);
				g.drawRect(xCoordinate-1, yCoordinate-1, width+2,height+2);
				g.drawRect(xCoordinate-2, yCoordinate-2, width+4,height+4);
			}
		}
	}
	
	public boolean isStretched()
	{
		return stretched;
	}
	
	public void setImage(Image image)
	{
		this.image=image;
		repaint();
	}
	public void setImage(ImageIcon imageIcon)
	{
		this.image=imageIcon.getImage();
		repaint();
	}
	public void setImage(String s)
	{
		ImageIcon imageIcon=new ImageIcon(s);
		this.image=imageIcon.getImage();
		repaint();
	}
	
	public void setStretched(boolean stretched)
	{
		this.stretched=stretched;
		repaint();
	}
	

	
	

}
