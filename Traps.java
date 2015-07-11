/**
 * @(#)Traps.java
 *
 *
 * @Iris Chang
 * @version 1.00 2015/5/5
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JPanel.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.LinkedList;

public class Traps {
	private int type;
	public static final int SPIKES = 0;
	public static final int CIRC_SAW = 1;
	public static final int NINJA_STAR_ST = 2;
	public static final int LASER = 3;
	public static final int SWING_SAW =4;
	public static final int ROT_STAR = 5;
	public static final int NINJA_STAR_BO = 6;
	private boolean stationary;
	private int scale;
	private int newWidth,oWidth;
	private int newHeight,oHeight;
	private int x,y,ox,oy;
	private int dx,dy;
	private int velx,vely;
	private int dirx,diry,odirx,odiry;
	private int angVel;
	private int ang=0;
	private int spawnCount, resize;
	private ArrayList<Image> resizedPics = new ArrayList<Image>();
	private Image pic;
	private Image resizedPic;
	private boolean isSpawned=true;
	private int growth=1;
	private int growing=0;
	private Level level;
	private BufferedImage map;
	private Color GREY = new Color(130,130,130);
    public Traps(String line, Level lev) {
    	level=lev;
    	map=level.getMap();
    	//data structure: x, y, dx, dy, velx, vely, dirx, diry, angVel, newWidth, newHeight,spawnCount, resize, TYPE
    	String [] data = line.split(",");
    	x=Integer.parseInt(data[0]);
    	y=Integer.parseInt(data[1]);
    	ox = x;
		oy = y;
    	dx=Integer.parseInt(data[2]);
    	dy=Integer.parseInt(data[3]);
    	velx=Integer.parseInt(data[4]);
    	vely=Integer.parseInt(data[5]);
    	dirx=Integer.parseInt(data[6]);
    	diry=Integer.parseInt(data[7]);
    	odirx=dirx;
    	odiry=diry;
    	angVel=Integer.parseInt(data[8]);
    	newWidth=Integer.parseInt(data[9]);
    	newHeight=Integer.parseInt(data[10]);
    	oWidth=newWidth;
    	oHeight=newHeight;
    	spawnCount=Integer.parseInt(data[11]);
    	resize=Integer.parseInt(data[12]);
    	type=Integer.parseInt(data[13]);
    	
    	if (type==2||type==6){
    		isSpawned=false;
    	}
    
    	pic =new ImageIcon("Images/Interactives/"+type+".png").getImage();
    	resizedPic=pic.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
    	if (resize!=0){
    		for (int i=0;i<resize+1;i++){
    			Image temp=pic.getScaledInstance(newWidth+i, newHeight+i, Image.SCALE_FAST);
    			resizedPics.add(temp);
    		}
    		
    	}
    }
    public int getType(){
    	return type;
    }
    public void move(){
    	
    	if (isSpawned==false){
    		spawnCount-=1;
    		if (spawnCount==0){
    			isSpawned=true;
    		}
    	}
    	ang=(ang+angVel)%360;
    	if (type==2){
    		if (isSpawned==true){
    			if (velx!=0){
    			
		    		x += velx*dirx;
					if ((x-ox)*dirx==dx){
						
						x=ox;
						isSpawned=false;
						spawnCount=30;
					}
				}
				if (vely!=0){
					y += vely*diry;
					if ((y-oy)*diry==dy){
						
						y=oy;
						isSpawned=false;
						spawnCount=30;
					}
				}
    		}
    	}
    	else if (type==6){
    		if (isSpawned==true){
    			if (dx==0){
    				x=ox;
					isSpawned=false;
					spawnCount=1;
					y=oy;
					dx=dy;
					dirx=odirx;
    				diry=odiry;
    			}

	    		Color c = new Color (map.getRGB(x,y));
	    		if (c.equals(GREY)==true){
	    			Color c1 = new Color (map.getRGB(x+1,y));
	    			Color c2 = new Color (map.getRGB(x-1,y));
	    			Color c3 = new Color (map.getRGB(x,y+1));
	    			Color c4 = new Color (map.getRGB(x,y-1));
	    			
	    			if (c1.equals(GREY)==false||c2.equals(GREY)==false){
	    				dirx*=-1;
	    			}
	    			else if (c3.equals(GREY)==false||c4.equals(GREY)==false){
	    				diry*=-1;
	    			}
	    			else{
	    				diry*=-1;
	    				dirx*=-1;
	    			}
	    		}
	    		x += velx*dirx;
	    		y += vely*diry;
	    		dx-=1;
    		}
    	}
    	else{
	    	if (dx!=0){
				if ((x-ox)*dirx<dx){
					x += velx*dirx;

				}
				else{
					dirx*=-1;
					x += velx*dirx;
				}
			}
			else if (dy!=0){
				if ((y-oy)*diry<dy){
					y += vely*diry;
				
				}
				else{
					diry*=-1;
					y += vely*diry;
				}
			}
    	}
    //should the main program check if the trap is stationary before calling this method or implement the distinction withint the method?
    	
    }
    public boolean getisSpawned(){
    	return isSpawned;
    }
    public int getAng(){
    	return ang;
    }
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y;
    }
    public int getDX(){
    	if (resize!=0){
    	
    	}
    	return (int)(x-newWidth/2);
    }
    public int getDY(){
    	return (int)(y-newHeight/2);
    }
    public Image getPic(){
    	if (resize!=0){
    		Image tmp = resizedPics.get(growing);
    		newWidth=tmp.getWidth(null);
    		newHeight=newWidth;
    		growing+=growth;
    	
    		if (growing==resize-1){
    			growth=-1;
    			
    		}
    		if (growing==0){
    			growth=1;
    		
    		}
    		return tmp;
    	}

    	return resizedPic;
    	
    }
}