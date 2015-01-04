package guestUserInterface.functions;

import java.awt.*;

import javax.swing.*;

/**
 * 
 * @author �����Ϊ�˿��Է���ص���inset�����
 *
 */
public class InsetJPanel extends JPanel 
{
	private int topInset;
	private int leftInset;
	private int bottomInset;
	private int rightInset;
	
	public InsetJPanel()
	{
		super();
	}
	
	public InsetJPanel(LayoutManager l)
	{
		super(l);
	}
	
	public InsetJPanel(int topInset, int leftInset, int bottomInset, int rightInset)
	{
		super();
		this.topInset=topInset;
		this.leftInset=leftInset;
		this.bottomInset=bottomInset;
		this.rightInset=rightInset;
	}
	
	public InsetJPanel(LayoutManager l, int topInset, int leftInset, int bottomInset, int rightInset)
	{
		this(l);
		this.topInset=topInset;
		this.leftInset=leftInset;
		this.bottomInset=bottomInset;
		this.rightInset=rightInset;
	}
	
	@Override
	public Insets getInsets() 
	{
		// TODO Auto-generated method stub
		return new Insets(topInset,leftInset,bottomInset,rightInset);
	}

	public int getTopInset() {
		return topInset;
	}

	public void setTopInset(int topInset) {
		this.topInset = topInset;
	}

	public int getLeftInset() {
		return leftInset;
	}

	public void setLeftInset(int leftInset) {
		this.leftInset = leftInset;
	}

	public int getBottomInset() {
		return bottomInset;
	}

	public void setBottomInset(int bottomInset) {
		this.bottomInset = bottomInset;
	}

	public int getRightInset() {
		return rightInset;
	}

	public void setRightInset(int rightInset) {
		this.rightInset = rightInset;
	}

}
