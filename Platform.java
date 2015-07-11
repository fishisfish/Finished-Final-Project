
/**
 * @Platform.java
 * 	- stores information on all types of platforms besides normal and water
 *
 * @Iris Chang 
 * @version 1.00 2015/5/05
 */
 
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Platform{
	private int x,y,ox,oy,dx,dy,width,height,dir,vel;
	private double oheight,iheight; //special height keepers for ice platforms
	private String [] data;
	private String type;
	private boolean falling=false;
	private boolean willFall=false;
	private boolean isBouncing=false;
	private boolean isMelting=false;
	private boolean isLocked;
	private int fallCount=0;
	private int countDown=0;
	private int squishCount=0;
	private int bounceVel;
	private int lockType;
	public Platform(String line){
		data = line.split(",");
		x = Integer.parseInt(data[0]);
		y = Integer.parseInt(data[1]);
		ox = x;
		oy = y;
		width = Integer.parseInt(data[2]);
		height = Integer.parseInt(data[3]);
		oheight = height;
		dx = Integer.parseInt(data[4]);
		dy = Integer.parseInt(data[5]);
		dir = Integer.parseInt(data[6]);
		vel = Integer.parseInt(data[7]);
		type=data[8];
		
		if(type.equals("ICE")){
			iheight = height;
		}
		bounceVel= Integer.parseInt(data[9]);
		lockType=Integer.parseInt(data[10]);
		if (type.equals("LOCKED")==true){
			isLocked=true;
		}

	}
	public int getX(){;return x;}
	public int getY(){return y;}
	public boolean getLocked(){
		return isLocked;
	}
	public int getxVel(){
		if (dx!=0){
			return vel*dir;
		}
		else{
			return 0;
		}
	}
	public int getyVel(){
		if (dy!=0){
			return vel*dir;
		}
		else{
			return 0;
		}
	}
	public String getType(){

		return type;
	}
	public void fallPrepare(){
		if (willFall==false){
			countDown=30;	
			willFall=true;
		}
		
	}
	public void unLock(){
		isLocked=false;
		if (lockType!=0){
			type="BOUNCING";
			x+=28;
			y+=29;
			width=44;
			height=42;
		}
		else{
			if (lockType!=0){
				x=0;
				y=0;
				width=0;
				height=0;
		}
		}
	}
	public void bounce(){
		isBouncing=true;
	}
	public void melt(){
		isMelting=true;
	}
	public boolean getMelt(){
		return isMelting;
	}
	public int getbVel(){
		return bounceVel;
	}
	public int getsquishCount(){
		return squishCount;
	}
	public int getWidth(){return width;}
	public int getHeight(){
		if(type.equals("ICE")){
			return (int)(iheight);
		}
		return height;}
	
	public void move(){
		if (dx!=0){
			if ((x-ox)*dir<dx){
				x += vel*dir;
				String velo = vel+"";

			}
			else{
				dir*=-1;
				x += vel*dir;
			}
		}
		else if (dy!=0){
			if ((y-oy)*dir<dy){
				y += vel*dir;
				String velo = vel+"";

			}
			else{

				dir*=-1;
				y += vel*dir;
			}
		}
		if (willFall==true){
			countDown-=1;
			if (countDown==0){
				falling=true;
				willFall=false;
				countDown=0;
			}
		}
		if (falling==true){
			y+=6;
			fallCount+=1;
			if (fallCount==100){
				falling=false;
				fallCount=0;
				y=oy;
			}
		}
		if (isBouncing==true){
			if (squishCount<=15){
				y+=1;
				height-=1;
				if (squishCount==15){
					dir*=-1;
				}
			}
			
			else if (squishCount>15&&squishCount<=30){
				y-=1;
				height+=1;
			}
			else{
				squishCount=0;
				isBouncing=false;
			}
			squishCount+=1;
		}
		if(isMelting==true){
			iheight -= 0.01;
			iheight = Math.max(0,iheight);
		}
	}
	public void regen(){
		isMelting = false;
		iheight = oheight;
	}
	public String toString(){
		return "{"+data[0]+","+data[1]+"}";
	}
	
}