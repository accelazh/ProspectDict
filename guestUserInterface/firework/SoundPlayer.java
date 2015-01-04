package guestUserInterface.firework;

import java.net.*;
import java.applet.*;

/**
 * 
 * �����������������
 * ����ܼ��ز���ĳ��
 * �����ļ����������
 * ���ᵼ��playʱ��
 * һ�β��ű���ֹ
 */
public class SoundPlayer 
{
	private AudioClip[] clips;
	private int counter=0;
	
	public SoundPlayer(URL url, int size)
	{
		if(null==url||size<=0)
		{
			return;
		}
		
		clips=new AudioClip[size];
		
		for(int i=0;i<clips.length;i++)
		{
			clips[i]=Applet.newAudioClip(url);
		}
		
		counter=0;
	}
	
	public SoundPlayer(URL url)
	{
		this(url,10);
	}
	
	private void increaseCounter()
	{
		if(null==clips||0==clips.length)
		{
			return;
		}
		counter=(++counter)%(clips.length);
	}
	
	public void play()
	{
		if(null==clips)
		{
			return;
		}
		
		clips[counter].stop();
		clips[counter].play();
		increaseCounter();
	}
	
	public void stop()
	{
		if(null==clips)
		{
			return;
		}
		
		for(int i=0;i<clips.length;i++)
		{
			if(clips[i]!=null)
			{
				clips[i].stop();
			}
		}
	}
}
