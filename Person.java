/**
 * @(#)Person.java
 *
 *
 * @author 
 * @version 1.00 2015/4/28
 */
//You will need to add swinging to the list of motions
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.*;
import javax.swing.JPanel.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
public class Person {
	private int x,y,walkCount,airCount,swimCount,breathCount,breathReset,picStall,stopSwim, jumpVel, propelVel,levelSizeX,levelSizeY,keyNum;
	private ArrayList<ArrayList<ArrayList<Image>>> allPics = new ArrayList<ArrayList<ArrayList<Image>>>();
	private ArrayList<ArrayList<Image>> leftPics = new ArrayList<ArrayList<Image>>();
	private ArrayList<ArrayList<Image>> rightPics = new ArrayList<ArrayList<Image>>();
	private ArrayList<Platform> platFall= new ArrayList<Platform>();
	private String [] picNames = new String []{"Walk", "Jump", "Some", "Slid", "Swim", "Dead"};
	private int [] frameTotal = new int []{0,0,0,0,0,0};
	private Image temp= new ImageIcon("profile.png").getImage();
	private double xVel,yVel,sVel,slideCoefficient,clingCoefficient,iceCoefficient,scale;
	private int headAngle;
	private int direction;
	private int swimDir=1;
	private int slideCount=1;
	private int contactRecty = 42;
	private int apex;
	private int lastWallx;
	private int xplatVel=0;
    private int	yplatVel=0;
    private int fallCount=0;
    private int bounceVel;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private boolean inAir=false;
	private boolean doSome=false;
	private boolean isWalking=false;
	private boolean isSliding=false;
	private boolean isCrouching=false;
	private boolean isSwimming=false;
	private boolean isClinging=false;
	private boolean isBouncing=false;
	private boolean gotDown=false;
    private boolean runIntoWall=false;
    private boolean hitGround=false;
    private boolean hitHead=false;
    private boolean isSinking=false;
    private boolean propel=false;
    private boolean leavingWater=false;
    private boolean hitApex=true;
    private boolean canRight=true;
    private boolean canLeft=true;
    private boolean tooRight=false;
    private boolean tooLeft=false;
    private boolean tooUp=false;
    private boolean tooDown=false;
    private boolean onPlat=false;
    private boolean clingPlat=false;
    private boolean wasClingP=false;
    private boolean edgeCling=false;
    private boolean pushedPlat=false;
    private boolean death=false;
    private boolean onIce=false;
    private boolean hasKey=false;
    private boolean levelDone=false;
    private boolean[] WallHits = new boolean[9];
    private boolean[] MovingStuffHits = new boolean[9];
    private boolean [] wallHitinWater = new boolean [9];
    
    private int [] waterHits = new int [9];
 	private int[][] contactPoints = {{0,-25},{-15,-24},{15,-24},{-15,0},{0,0},{15,0},{0,22},{-15,21},{15,21}};
 	private int[][] bcontactPoints = {{0,0},{-15,0},{15,0},{-15,0},{0,0},{15,0},{0,22},{-15,21},{15,21}};
 	private int[][] rotatedPoints = {{0,-23},{-13,-22},{10,-22},{-13,0},{0,0},{10,0},{0,22},{-13,21},{10,21}};
 	private double [] distPoints ={23.0,Math.pow(22*22+13*13,0.5),Math.pow(22*22+10*10,0.5),13.0,0.0,10.0,22.0,Math.pow(21*21+13*13,0.5),Math.pow(21*21+10*10,0.5)};
 	private double [] cptStartAngles={Math.toRadians(90),Math.toRadians(90)+Math.atan(13.0/22.0),Math.atan(22.0/10.0),Math.toRadians(180),-10000,0,Math.toRadians(270),Math.toRadians(180)+Math.atan(21.0/13.0),Math.toRadians(270)+Math.atan(10.0/21.0)};	
 	private Color BLUE = new Color (0,165,255);
	private Color PURPLE = new Color (200,0,200);
	private Color INDIGO = new Color (138,0,255);
	private Color ORANGE = new Color (255,153,0);
	private Color GREY = new Color(130,130,130);
	private Color YELLOW = new Color(255,252,118);
	private Color footColor;
	private Level level;
	private BufferedImage movingStuff;
	private Graphics2D g2;
	private int deathCount=0;
	private int checkPointX,checkPointY,checkPointDir;
	private Platform lastPlat=null;
	private Platform ridingPlat=null;
    public Person(){
    	scale=1;
    	xVel=0;
    	yVel=0;
    	jumpVel=11;
    	propelVel=12;
    	bounceVel=20;
    	breathReset=10000;
    	breathCount=breathReset;
    	headAngle=90;
    	walkCount=0;
    	picStall=0;
    	makePics();
    	
    	slideCoefficient=0.5;
    	iceCoefficient=0.1;
    	clingCoefficient=0.05;
    	
    	
    }
    public void setLevel(Level lev){
    	level=lev;
    	levelSizeX= level.getWidth();
    	levelSizeY= level.getHeight();
    	checkPointX = level.getDropx();
    	checkPointY = level.getDropy();
    	direction = level.getDir();
    	movingStuff = new BufferedImage(level.getWidth(), level.getHeight(),  BufferedImage.TYPE_BYTE_INDEXED);
    	g2 = movingStuff.createGraphics();
    	checkPointDir = direction;
    	scale=1;
    	levelDone=false;
    }
    public void setX(int dropx){
    	x = dropx;
    }
    public void setY(int dropy){
    	y = dropy;
    }
    public void makePics(){
    	for (int j=0; j<5; j++){
    		leftPics.add(new ArrayList<Image>());
    		rightPics.add(new ArrayList<Image>());
    	}
    	Scanner infile = null;
    	try{
    		infile=new Scanner(new File("Images/Chara/info.txt"));   	
    	}
    	catch(IOException ex){
    	}
    	int temp=0;
    	while (infile.hasNextLine()){
    		
    		int line = Integer.parseInt(infile.nextLine());
    		frameTotal[temp]=line;

    		for (int i=0;i<line;i++){
    			int b=i+1;
    			String a=b+"";
    			leftPics.get(temp).add(new ImageIcon("Images/Chara/Left/"+picNames[temp]+"/"+a+".png").getImage());
    			rightPics.get(temp).add(new ImageIcon("Images/Chara/Right/"+picNames[temp]+"/"+a+".png").getImage());
    		}
    		temp+=1;
    		
    	}
    	allPics.add(leftPics);
    	allPics.add(new ArrayList<ArrayList<Image>>());
    	allPics.add(rightPics);
    }
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    public double getxVel(){
    	return xVel;
    }
    public double getyVel(){
    	return yVel;
    }
    public double getxMoved(){
    	return xVel;
    }
    public double getyMoved(){
    	if (inAir==true){
    		return yVel;
    	}
    	if (onPlat==true){
    		return lastPlat.getyVel();
    	}
    	return 0;
    	
    }
    public boolean getMoved(){
    	return isWalking;
    }
    public boolean getinAir(){
    	return inAir;
    }
    public boolean getSome(){
    	return doSome;
    }
    public int getDir(){
    	return direction;
    }
    
    public void getUp(){
    	
    	if (slideCount==1){
    		isSliding=false;
    		isCrouching=false;
    		gotDown=false;
    	}
    	else{
    		slideCount-=1;
    	}	
    }
    public boolean getSwim(){
    	return isSwimming;
    }
    public int getAn(){
    	return headAngle;
    }	
   	public int getApex(){
   		return apex;
   	}
   	public boolean getCling(){
   		return isClinging;
   	}
   	public boolean getPCling(){
   		return clingPlat;
   	}
   	public boolean getLevelDone(){
   		return levelDone;
   	}
   	public boolean reachedApex(){
   		return hitApex;
   	}
   	public void reachingApex(boolean tmp){
   		hitApex=tmp;
   	}
    public boolean getleavingWater(){
    	return leavingWater;
    }
    public int [][] getPoints(){
    	return rotatedPoints;
    }
    public boolean getDeath(){
    	return death;
    }
    public String getDeathCount(){
    	return deathCount+"";
    }
    public int getBreathCount(){
    	return 	breathCount;
    }
    public boolean gethasKey(){
    	return hasKey;
    }
    public double getScale(){
    	return scale;
    }
    public Image getPic(){
    	
    	if (isSwimming==true&&isSinking==false){
    		if (stopSwim>0){
	    		picStall+=1;
		    	if (picStall==10){
		    		swimCount=(swimCount+1)%6;
		    		picStall=0;
		    	}
		    	return allPics.get(direction+1).get(4).get(swimCount);
    		}
    		return allPics.get(direction+1).get(4).get(2);
    		
    	}
    	 if (isSliding==true){

	    	if (slideCount<10){
	    		
		    	if (gotDown==false){
		    		return allPics.get(direction+1).get(3).get((int)slideCount/5);
		    	}
		    	else{
		    		if (slideCount>5){
		    			return allPics.get(direction+1).get(3).get(3);
		    		}
		    		else{
		    			return allPics.get(direction+1).get(3).get(4);
		    		}
		    	}
	    	}
	    	else{
	    		return allPics.get(direction+1).get(3).get(2);
	    	}
    	}
    	else if (isCrouching==true){
    		return allPics.get(direction+1).get(3).get(5);
    		
    	}
    	if (inAir==true&&isClinging==true){
    		return allPics.get(direction+1).get(1).get(4);
    	}
    	if (inAir==false&&Math.abs(xVel)>0&&isWalking==true){
   	    	picStall+=1;
	    	if (picStall==10){
	    		walkCount=(walkCount+1)%10;
	    		picStall=0;
	    	}
	    	return allPics.get(direction+1).get(0).get(walkCount);
	    }
	    if (inAir==true){
	    	if (doSome==true){
	    		if (yVel<=10&&yVel>=7){
		    		allPics.get(direction+1).get(2).get(0);
		    	}
		    	else if (yVel<7&&yVel>=4){
					return allPics.get(direction+1).get(2).get(1);
		    	}
		    	else if (yVel<4&&yVel>=1){
					return allPics.get(direction+1).get(2).get(2);
		    	}
		    	else if (yVel<1&&yVel>=-1){
					return allPics.get(direction+1).get(2).get(3);
		    	}
		    	else if (yVel<-1&&yVel>=-3){
					return allPics.get(direction+1).get(2).get(4);
		    	}
		    	else if (yVel<-3&&yVel>=-5){
					return allPics.get(direction+1).get(2).get(5);
		    	}
		    	else if (yVel<=-6){
					return allPics.get(direction+1).get(2).get(0);
		    	}
	    		
	    	}
	    	else{
	    	
		    	if (yVel<=10&&yVel>=6){
		    		return allPics.get(direction+1).get(1).get(0);
		    	}
		    	else if (yVel<6&&yVel>=0){
					return allPics.get(direction+1).get(1).get(1);
		    	}
		    	else if (yVel<0&&yVel>=-6){
					return allPics.get(direction+1).get(1).get(2);
		    	}
		    	else if (yVel<-6){
					return allPics.get(direction+1).get(1).get(3);
		    	}
	    	}
	    	
	    }
	    if(isWalking==false){
	    	walkCount=0;
	    }

    	return allPics.get(direction+1).get(0).get(10);
    }
    
    public void changeDir(int dir){
    	double tmp= xVel+0.15*dir;

    	if (tmp<-4){
    		xVel=-4;
    	}
    	else if (tmp>4){
    		xVel=4;
    	}
    	else{
    		xVel=tmp;
    	}
    	direction=dir;
    }
    public void move(){
    	if (inAir==false){
    		fallCount=0;
    	}
    	if (inAir==true&&yVel<0){
    		fallCount+=1;
    		if (fallCount>200){
    			die();	//death by falling
    			
    		}
    	}
    	if (inAir==false && runIntoWall == false && (int)(x+xVel) >=0 && (int)(x+xVel) <= levelSizeX){
	    	x=(int)(x+xVel);
    	}
    	
    	else if (inAir==true&&isWalking==true&&runIntoWall==false){
    		x+=(int)(4*direction);
    	}
    	if (inAir==true){

    			if (direction*xVel<0){
    				xVel*=-1;
    			}
    	}
    	else if (isWalking==false&&Math.abs(xVel)>0){
    		double tmp;
    		if(onIce == false){
    			if(direction*xVel>0){
    				tmp=xVel+direction*-1*slideCoefficient;
    			}
				else{
					tmp=xVel+direction*slideCoefficient;
				}
    		}
    		else{
    			if(direction*xVel>0){
    				tmp=xVel+direction*-1*iceCoefficient;
    			}
				else{
					tmp=xVel+direction*iceCoefficient;
				}
    		}
			if (tmp*xVel<0){
	    		xVel=0;
	    	}
	    	else{
	    		xVel=tmp;
	    	}
    		
    		
    	}	
    }
    public boolean jump(){

    	if (inAir==false){
    		isCrouching=false;
    		inAir=true;
    		isClinging=false;
    		clingPlat=false;
    		wasClingP=false;
    		if (isBouncing==true){

    			yVel=bounceVel;

    		}
    		else if (propel==true){
    			yVel=propelVel;
    		}
    		else {

    			yVel=jumpVel;
    		}
    		
    		
    		
    		return true;
 		
    	}

    	return false;
    }
    public void some(String tmp){

    	if (inAir==true && doSome==false){

    		
    		if (tmp=="OFFWALL"){
    			isCrouching=false;
	    		inAir=true;
	    		isClinging=false;
	    		clingPlat=false;
	    		wasClingP=false;
    			yVel=jumpVel;

    		}
    		else{
    			doSome=true;
    		}
    	}	
    }
    
    public void slide(){

    	double tmp=0;
    	if (onIce == false){
    		tmp=xVel+direction*0.2*-1;
    	}
    	else{
    		tmp=xVel+direction*0.1*-1;
    	}
    	if (tmp*xVel<=0||direction*xVel<=0){
    		xVel=0;
    	}
    	else{
    		xVel=tmp;
    	}
    	if (xVel==0){
    		isWalking=false;
    	}
    	if (inAir==false){
	    	if (isWalking==true&&isCrouching==false){
	    		slideCount=Math.min(20,slideCount+1);
	    		isSliding=true;
	    	}
	    	else{
	    		isCrouching=true;
	    	}
    	}
    	
    	if (slideCount>10){
    		gotDown=true;
    	}
    }
    public void wallPush(){
    	x+=direction*-1*3;
    	inAir=true;
    	isClinging=false;
    	clingPlat=false;
    	wasClingP=false;
    }
    public void gravity(){
    	if (inAir == true){

    		xVel=Math.abs(xVel)*direction;
    		if (yVel>0){
    			if(isClinging==false&&clingPlat==false){	
    				yVel-=0.55;
    			}
    			else{
    				if (edgeCling==false){
    					yVel=Math.max(-3,yVel-clingCoefficient);	
    				}
    				else if (edgeCling==true){
    					yVel=0;
    				}

    			}
    		}
    		else if (yVel==0&&hitHead==true){
    			y+=1;
    		}
    		else{
    			if (isClinging==false&&clingPlat==false){

    				yVel=Math.max(-6,yVel-0.55);

    			}
    			else{
    				if (edgeCling==false){
    				yVel=Math.max(-3,yVel-clingCoefficient);
    				}
    				else if (edgeCling==true){
    					yVel=0;
    				}

    			}
    			if (propel==true){
    				propel=false;
    			}
    		}
    	}
    	y=(int)(y-yVel);
    	y=(Math.max(25,y));
   
    	if (y<apex){
    		apex=y;
    		hitApex=false;
    	}
    }
    public void swim (String temp, BufferedImage map){

    	double ang=Math.toRadians(headAngle);
    	sVel=Math.min(10,sVel+0.30);
    	stopSwim=1;
    	
    	if (temp=="LEFT"){
    		if (canLeft==true){
    			headAngle=(headAngle+1)%360;
    		}
    		/*else{
    		/*if 
    			if (canSwim(ang,swimDir*-1,map)==true){
    				x=x+(int)(3*Math.cos(ang)*swimDir*-1);
    				y=y-(int)(3*Math.sin(ang)*swimDir*-1);
    			}

    		}*/
    	}
    	else if (temp=="RIGHT"){
    		if (canRight==true){
    			headAngle=(360+headAngle-1)%360;
    		}
    		/*else{
    			if (canSwim(ang,swimDir*-1,map)==true){
    				x=x+(int)(3*Math.cos(ang)*swimDir*-1);
    				y=y-(int)(3*Math.sin(ang)*swimDir*-1);
    			}

    		}*/
    	}
    	else{
    		if (temp=="UP"){
    			if (canSwim(ang, 1, map)==true){
    				x=x+(int)(3*Math.cos(ang));
    				y=y-(int)(3*Math.sin(ang));

    			}
    			
    			swimDir=1;
    		}
    		if (temp=="DOWN"){
    			if (canSwim(ang, -1, map)==true){
	    			x=x-(int)(2*Math.cos(ang));
	    			y=y+(int)(2*Math.sin(ang));

    			}
    			swimDir=-1; 
    		}
    	}
    	
    }
    public void swimSlow(){
    	sVel=Math.max(0,sVel-0.20);
    	stopSwim-=1;
    }
    public void sink(){
    	if (isSinking==true){
    		y+=4;
    		
    	}
    }
    public boolean checkWallCling(int tmp){
    	if (tmp==LEFT&&(WallHits[1]==true||WallHits[3]==true||WallHits[7]==true)){
    		return true;
    	}
    	if (tmp==RIGHT&&(WallHits[2]==true||WallHits[5]==true||WallHits[8]==true)){
    		return true;
    	}
    	else{
    		return false;
    	}
    		
    }
    public void cling(BufferedImage map){
    		Color c1=new Color(map.getRGB(x-16,y-24));
    		Color c2=new Color(map.getRGB(x+16,y-24));	
    	if ((direction==LEFT&&c1.equals(GREY))||(direction==RIGHT&&c2.equals(GREY))){
    		Color c3=new Color(map.getRGB(x-16,y-28));
    		Color c4=new Color(map.getRGB(x+16,y-28));

    		if (c3.equals(GREY)==false&&direction==LEFT||c4.equals(GREY)==false&&direction==RIGHT){
    			edgeCling=true;
    
    		}
	    	if (direction*xVel>=0&&isSinking==false){
	
	    		if((direction==RIGHT&&checkWallCling(RIGHT)==true)||(direction==LEFT&&checkWallCling(LEFT)==true)||(xVel==0&&(direction==LEFT&&checkWallCling(LEFT)==true)||(direction==RIGHT&&checkWallCling(RIGHT)==true))){

	    			if (lastWallx!=x+15*direction){
	    				

						yVel=Math.min(yVel,0);

						xVel=0;
						isWalking=false;
						isClinging=true;
						doSome=false;
						lastPlat=null;
						lastWallx=x+15*direction;
						apex=y;
	    			}
				}
	    	}
    	}
    }
    public void checkWalking(boolean temp){
    	isWalking=temp;
    }
    public void rotate(){
    	int angleRotated=headAngle-90;
    	for (int i=0;i<9;i++){
    		if (i!=4){
    			double newAng=Math.toRadians(angleRotated)+cptStartAngles[i];
    			rotatedPoints[i][0]=(int)Math.round(distPoints[i]*Math.cos(newAng));
    			rotatedPoints[i][1]=(int)Math.round(distPoints[i]*-1*Math.sin(newAng));
    		}			
    	}
    }
    public int [] rotateTest(double An, int i){
    	double angleRotated=An-(Math.PI/2);
    	double newAng=angleRotated+cptStartAngles[i];
    	int a=(int)Math.round(distPoints[i]*Math.cos(newAng));
    	int b=(int)Math.round(distPoints[i]*-1*Math.sin(newAng));
    	int [] tmp={a,b};
    	return tmp;			
    	
    }
    public int add(int [] tmp){
    	int sum=0;
    	for (int i: tmp){
    		sum+=i;
    	}
    	return sum;
    }
    public boolean canSwim(double An, int dir, BufferedImage map){

    	int tempX=x+(int)(3*Math.cos(An)*dir);
    	int tempY=y-(int)(3*Math.sin(An)*dir);
    	for (int i=0;i<9;i++){
    		int [] tmp= rotatedPoints[i];
    		Color c = new Color (map.getRGB(tempX+tmp[0],tempY+tmp[1]));
    		if (c.equals(GREY)==true){

    			return false;
    		}
    	}

		return true;
    }
    public boolean inRange(int [] tmp, Platform plat, String type){
    	if (type=="LAND"||type=="HITHEAD"){
    		if (y+tmp[1]>=plat.getY()&&y+tmp[1]<=plat.getY()+plat.getHeight()&&x+xVel+tmp[0]>=plat.getX()&&x+xVel+tmp[0]<=plat.getX()+plat.getWidth()){
    			return true;
    		}
    	}
    	else if (type=="CLING"){
    	 	if (y+tmp[1]+yVel>=plat.getY()&&y+tmp[1]+yVel<=plat.getY()+plat.getHeight()&&x+tmp[0]+xVel>=plat.getX()&&x+tmp[0]+xVel<=plat.getX()+plat.getWidth()){
    			return true;
    	 	}
    	}
    	else if (type=="CHECK"){
    		if (y+tmp[1]+yVel>=plat.getY()&&y+tmp[1]+yVel<=plat.getY()+plat.getHeight()&&x+xVel+tmp[0]>=plat.getX()&&x+xVel+tmp[0]<=plat.getX()+plat.getWidth()){
    			return true;
    	 	}
    	}
    	return false;
    	
    }
	public boolean checkPlatCollide(Platform plat, boolean temp){
		boolean landed=false;
		boolean unlocked=false;
    	for (int i=0;i<9;i++){
    		int [] tmp=contactPoints[i];
    		if (hasKey==true&&(plat.getType().equals("LOCKED"))){
    			if (inRange(tmp, plat, "CHECK")){
    				plat.unLock();
    				unlocked=true;
    				hasKey=false;
    			}
    		}
    		if (i==0){
    			if (inRange(tmp, plat, "HITHEAD")==true){
    				hitHead=true;
			    	yVel=0;
			    	y=plat.getY()+plat.getHeight()+26;
    			}
    		}
    		if (i==6){
	    		if (inRange(tmp, plat, "LAND")==true){
	    			if (i==6||i==7&&direction==LEFT||i==8&&direction==RIGHT){

	    				onPlat=true;
	    				inAir=false;
	    				landed=true;
	    				isClinging=false;
	    				clingPlat=false;
	    				ridingPlat=plat;
	    				lastPlat=null;
	    				y=plat.getY()-21;
	    				yVel=0;
	    				lastWallx=x;
	    				xplatVel=plat.getxVel();
	    				yplatVel=plat.getyVel();
	    				if ((plat.getType()).equals("DROPPING")==true){
	    					isBouncing=false;
	
	    					plat.fallPrepare();
	    				}
	    				if ((plat.getType()).equals("BOUNCING")==true){
	    
	    					doSome=false;
	    					isBouncing=true;
	    					plat.bounce();
	    				}
	    				if((plat.getType()).equals("ICE")==true){
	    					isBouncing=false;
	    					onIce = true;
	    				}
	    				else{
	    					onIce = false;
	    				}
	    			}
	    			
	    		}
    		}
    		if (landed==false&&((i==2||i==5)&&direction==RIGHT)||((i==1||i==3)&&direction==LEFT)||i==4){
    			if (onPlat==true){
    				y=ridingPlat.getY()-21;
    			}
    			if (inRange(tmp, plat, "CHECK")==true){
    				runIntoWall=true;

    			}
    		}
    	}
    	return onPlat;
    }
    public boolean plateCling(Platform plat, boolean temp){
    	boolean notinPlatform=false;
    	for (int i=0;i<9;i++){
    		int [] tmp=contactPoints[i];

    		
			if ((i==2||i==5||i==8)&&direction==RIGHT){
				if (inRange(tmp, plat, "CLING")==true){
					pushedPlat=true;
					
		    		if (inRange(contactPoints[2], plat, "CLING")==true&&((wasClingP==false&&lastPlat!=plat)||wasClingP==true)){

			    		
			    		if (y-34<=plat.getY()){
			    			y=plat.getY()+24;
			    			edgeCling=true;

			    		}
		    			isBouncing=false;

		    			clingPlat=true;
		    			isClinging=true;
		    			
		    			x=plat.getX()-12;
		    			xplatVel=plat.getxVel();
	    				yplatVel=plat.getyVel();
	    				xVel=0;
	    					
	    				if (yVel>0){
	    					yVel=0;
	    				}
						isWalking=false;
						doSome=false;
						lastPlat=plat;

		    		}
				}	
	    	}
	    	if ((i==1||i==3||i==7)&&direction==LEFT){
	    		if (inRange(tmp, plat, "CLING")==true){
					pushedPlat=true;

		    		if (inRange(contactPoints[1], plat, "CLING")==true&&((wasClingP==false&&lastPlat!=plat)||wasClingP==true)){
		    			if (y-30<=plat.getY()){
			    			y=plat.getY()+24;
			    			edgeCling=true;
			    		}
		    			clingPlat=true;
		    			isClinging=true;
				    	x=plat.getX()+plat.getWidth()+12;
				    	xplatVel=plat.getxVel();
	    				yplatVel=plat.getyVel();
	    				if (yVel>0){
	    					yVel=0;
	    				}
						isWalking=false;
						doSome=false;
						lastPlat=plat;
						isBouncing=false;	
		    		}
	    		}
    		}
    	}
    	return clingPlat;
    }
    public int swimWall (int tempX, int tempY, int i, int dir, int waterWallhitCount){
    	wallHitinWater[i]=true;
		waterWallhitCount+=1;
		if (dir==RIGHT){
			canRight=false;
		}
		else{
			canLeft=false;
		}
		if (x>tempX){
			x+=2;
		}
		if (x<tempX){
			x-=2;
		}
		if (y>tempY){
			y+=2;	
		}
		if (y<tempY){
			y-=2;
		}
		return waterWallhitCount;
	}
	public void hitGroundreset(){
		inAir=false;
		doSome=false;
		yVel=0;
		hitGround=true;
		airCount=0;
		apex=y;
		xVel=Math.abs(xVel)*direction;
		lastWallx=x;
		isClinging=false;
		clingPlat=false;
	    lastPlat=null;
	    isBouncing=false;
	}
	public void adjust(int tx, int ty, int dir, int num, String temp, BufferedImage map){
		int j=0;
		while (true){
			if (j==20){
				if (temp=="y"){
					y=y+num+j*dir;
				}
				else if (temp=="x"){
					x=x+num+j*dir;
				}
				break;
			}
			
			if (temp=="y"){
				Color c2= new Color (map.getRGB(tx,ty+j*dir));
				if (c2.equals(GREY)==true){
					 y=y+num+j*dir;
					 break;
				}
			}
			else if (temp=="x"){
				Color c2= new Color (map.getRGB(tx+j*dir,ty));
				if (c2.equals(GREY)==true){
					 x=x+num+j*dir;
					 break;
				}
			}
		
		j+=1;
		}
	}
	public int swimCheckStuff(int i, int waterWallhitCount, BufferedImage map){
		Color cS = new Color (map.getRGB(x+rotatedPoints[i][0],y+rotatedPoints[i][1]));
		Color cSm = new Color (movingStuff.getRGB(x+rotatedPoints[i][0],y+rotatedPoints[i][1]));
		if (cS.equals(Color.RED)==true||cSm.equals(Color.RED)==true){
			die();
			return 0;
		}
		else{
		
			if (cS.equals(BLUE)==true){
				waterHits[i]=1;
				}
			double angR=Math.toRadians((360+headAngle-1)%360);
			double angL=Math.toRadians((360+headAngle+1)%360);
			int tempRx=x+rotateTest(angR, i)[0];
			int tempRy=y+rotateTest(angR, i)[1];
			int tempLx=x+rotateTest(angL, i)[0];
			int tempLy=y+rotateTest(angL, i)[1];
			
			Color cR = new Color (map.getRGB(tempRx,tempRy));
			Color cL = new Color (map.getRGB(tempLx,tempLy));

			if (cR.equals(GREY)==true){
				waterWallhitCount=swimWall ( tempRx,  tempRy,  i, RIGHT, waterWallhitCount);
			}
			if (cL.equals(GREY)==true){
				waterWallhitCount=swimWall ( tempLx,  tempLy,  i, LEFT, waterWallhitCount);
			}
				
			if (isSinking==true&&cS.equals(BLUE)==true&&i==0){	

				y+=3;
				yVel=0;

				apex=y;
				isSinking=false;
				inAir=false;
				doSome=false;
				breathCount=breathReset;
				if (isClinging==true){
					isClinging=false;
					x+=direction*-1*1;	
				}
			}
			if (cS.equals(BLUE)==false&&cS.equals(GREY)==false&&isSinking==false){
				breathCount=breathReset;

    			leavingWater=true;
    			propel=true;
    			isSwimming=false;

    			headAngle=90;

    			jump();
    				
    		}
    		return waterWallhitCount;
		}
	}
	public void otherCheckStuff(int i, BufferedImage map){
		int tempX, tempY;
		if (isSliding==false){
		
			tempX=(int)(x+contactPoints[i][0]+xVel);
		    tempY=(int)(y+contactPoints[i][1]+yVel);
		}
		else{

			tempX=(int)(x+bcontactPoints[i][0]+xVel);
	    	tempY=(int)(y+bcontactPoints[i][1]+yVel);
		}
		if (tempX>=0&&tempX<=levelSizeX&&tempY>=0&&tempY<=levelSizeY){
			Color c = new Color (map.getRGB(tempX,tempY));
			Color cM = new Color (movingStuff.getRGB(tempX,tempY));
			
			if (c.equals(Color.RED)==true||cM.equals(Color.RED)==true){
				die();
				//death by trap (spike)
			}
			else{
    			WallHits[i]=c.equals(GREY);
    			MovingStuffHits[i]=cM.equals(ORANGE);
    			
    			if (i==6){
    					footColor=c;
    					if (c.equals(BLUE)==true&&isSwimming==false&&propel==false){

    						isSwimming=true;
    						isSinking=true;
    						clingPlat=false;
    						isClinging=false;
							lastPlat=null;
							isBouncing=false;
						
    					}
    					if (c.equals(GREY)==false&&isSwimming==false&&isSinking==false&&onPlat==false){
    						inAir=true;
    						isSliding=false;
    						isBouncing=false;
    						
    					}
    					
    			}
    			
    			
    			if (c.equals(GREY)==true){
    				
    				if (i==0&&inAir==true){

    					hitHead=true;
    					yVel=0;
    					adjust(tempX,  y, -1, 26, "y", map);	
    				}
    				if (i==6&&inAir==true){
    					if (fallCount>=150){

    						die();	//death by falling
    					}
    					hitGroundreset();
    					adjust(tempX,  y, 1, -22, "y", map);
    				}
					if (xVel<0&&isSwimming==false&&clingPlat==false){
						if ((i==1||i==3||i==7)&&WallHits[i]==true){
							adjust(x, tempY, -1, 14, "x", map);

							runIntoWall=true;
							lastPlat=null;
							isBouncing=false;

						}
					}
					if (xVel>0&&isSwimming==false&&clingPlat==false){
						if ((i==2||i==5||i==8)&&WallHits[i]==true){
							adjust(x, tempY, 1, -14, "x", map);

							runIntoWall=true;
							lastPlat=null;
							isBouncing=false;
						}	
    				}
    				
    			}
			}
			
		}
	}
	public void platCheckStuff(){
		g2 = movingStuff.createGraphics();
    	g2.setColor(Color.WHITE);
    	g2.fillRect(0,0,level.getWidth(),level.getHeight());
    	ArrayList<Traps> tmpT = level.getTraps();
    	for(int i =0;i<tmpT.size();i++){
    		if (tmpT.get(i).getisSpawned()==true){
	    		if (tmpT.get(i).getAng()==0){
	    			g2.drawImage(tmpT.get(i).getPic(),tmpT.get(i).getDX(),tmpT.get(i).getDY(),null);
	    		}
	    		else{
					AffineTransform saveXform = g2.getTransform();
					AffineTransform at = new AffineTransform();
					at.rotate(Math.toRadians(tmpT.get(i).getAng()),tmpT.get(i).getX(),tmpT.get(i).getY());
					g2.transform(at);
					g2.drawImage(tmpT.get(i).getPic(),tmpT.get(i).getDX(),tmpT.get(i).getDY(),null);
					g2.setTransform(saveXform);
	    		}
    		}
    	}
    	ArrayList<Platform> tmpP = level.getPlats();
    	onPlat=false;
    	clingPlat=false;
    	xplatVel=0;
    	yplatVel=0;

    	for(int i =0;i< tmpP.size();i++){
    		if ((tmpP.get(i).getType()).equals("MOVING")==true){
    			g2.setColor(ORANGE);
    		}
    		if ((tmpP.get(i).getType()).equals("DROPPING")==true){
    			g2.setColor(INDIGO);
    		}
    		g2.fillRect(tmpP.get(i).getX(), tmpP.get(i).getY(),tmpP.get(i).getWidth(),tmpP.get(i).getHeight());
    		onPlat=checkPlatCollide(tmpP.get(i),onPlat);

    	}

		if (onPlat==false&&hitHead==false&&inAir==true){
	    	for(int i =0;i< tmpP.size();i++){	
	    			clingPlat=plateCling(tmpP.get(i),clingPlat);
	    		}
    			
    	}

    	if (onPlat==true||clingPlat==true||pushedPlat==true){
    		x+=xplatVel;
    		if(onPlat==true||clingPlat==true){

	    		apex=y;
	    		fallCount=0;
	    		y+=yplatVel;
    		}
    	}
    	if (onPlat==true&&ridingPlat.getsquishCount()==15){
    		isBouncing=true;
    		onPlat=false;
    		inAir=false;
    		bounceVel=ridingPlat.getbVel();
    		jump();

    	}
    	wasClingP=false;
    	if (clingPlat==true){
    		wasClingP=true;
    	}
	}
    public void checkHit(BufferedImage map){
    	edgeCling=false;
    	runIntoWall = false;
    	hitHead = false;
    	hitGround=false;
    	leavingWater=false;
    	onIce=false;
    	canRight=true;
    	canLeft=true;
    	tooRight=false;
    	tooLeft=false;
    	tooUp=false;
    	tooDown=false;
		pushedPlat=false;
		death=false;
		
		Color cD = new Color (map.getRGB(x,y));
		if (cD.equals(PURPLE)==true){
			levelDone=true;
		}
		

			platCheckStuff();

	    	int waterWallhitCount=0;
	    	for (int i=0;i<9;i++){
				waterHits[i]=0;
				wallHitinWater[i]=false;
			} 
	
	    	for (int i=0;i<9;i++){
	    		int tempX,tempY;
	    		if(x+rotatedPoints[i][0] <= levelSizeX && x+rotatedPoints[i][0] >=0 && y+rotatedPoints[i][1] <= levelSizeY && y+rotatedPoints[i][1] >=0){
					for (int j=0;j<level.getCheckPoints().size();j++){
	    				checkCPoint(map,i,j);
	    			}
	    			for (int n=0;n<level.getkeyPoints().size();n++){
	    				checkKPoint(map,i,n);
	    			}
					if (isSwimming==true){
						if (breathCount==0){
							breathCount=breathReset;
							
							die();
						}
						breathCount-=1;
						fallCount=0;
						isBouncing=false;
						waterWallhitCount=swimCheckStuff(i, waterWallhitCount, map);
					}
						
		
		    		if (isSwimming==false){
		    			otherCheckStuff(i, map);
		    			
		    		}
	
	    		}
	    		
	    		else{
	    			die();	//death by being out of bounds (in water)
	    		}
	    	}
	    	if (clingPlat==true){
	    		runIntoWall=true;
	    	}
	    	g2.dispose();
	
	    	
	    	if ((WallHits[0]==true||MovingStuffHits[0]==true)&&(WallHits[6]==true||MovingStuffHits[6]==true)){

	    		die();	//death by squishing between two platforms (vertical)
	    	}
	    	if (WallHits[1]==false&&WallHits[2]==false&&WallHits[3]==false&&WallHits[5]==false&&WallHits[7]==false&&WallHits[8]==false&&clingPlat==false){

	    		isClinging=false;
	    	}
	    	else{
	    		if (inAir==true&&isSwimming==false&&footColor.equals(BLUE)==false){

					cling(map);
				}
	    	}
	    	if (waterWallhitCount>=2){

	
	    		if ((wallHitinWater[2]==true||wallHitinWater[5]==true||wallHitinWater[8]==true)&&(wallHitinWater[6]==true||wallHitinWater[7]==true)){

	    			x+=-3*direction;
	    			if (y+rotatedPoints[8][1]<=y){
	    				y-=3;
	    			}
	    		}
	    	}

    }
    public void die(){
    	if (hasKey==true){
    		level.getkeyAvailable().set(keyNum,true);
    		hasKey=false;
    	}
    	death=true;
   		deathCount++;
   		int lastCheck=-1;
   		for (int i=0;i<level.getCheckPassed().size();i++){
   			
   			if (level.getCheckPassed().get(i)==true){
   				lastCheck=i;
   			}	
   		}
   		
    	x = checkPointX;
    	y = checkPointY-50;
    	if (lastCheck>-1){
   			x = level.getCheckPoints().get(lastCheck)[0];
    		y = level.getCheckPoints().get(lastCheck)[1];
   		}
    	
    	xVel = 0;
    	yVel = 0;
    
    	direction = checkPointDir;
    	headAngle = 90;
    	apex = y;
    	inAir = true;
    	doSome=false;
		isWalking=false;
		isSliding=false;
		isCrouching=false;
		isSwimming=false;
		isClinging=false;
		gotDown=false;
    	runIntoWall=false;
    	hitGround=false;
    	hitHead=false;
    	isSinking=false;
    	propel=false;
    	leavingWater=false;
    	hitApex=true;
    	canRight=true;
    	canLeft=true;
    	tooRight=false;
    	tooLeft=false;
    	tooUp=false;
    	tooDown=false;
    	walkCount=0;
    	picStall=0;
    	fallCount=0;
    	level.iceRegen();
    }
    public void suck(){
    	scale-=0.005;
    	scale=Math.max(0,scale);
    	if (x>level.getEP()[0]){
			x-=1;
		}
		else if (x<level.getEP()[0]){
			x+=1;
		}
		if (y>level.getEP()[1]){
			y-=1;
		}
		else if (y<level.getEP()[1]){
			y+=1;
		}
		headAngle=(headAngle+1*direction)%360;
		
    }
    public void checkCPoint(BufferedImage map, int i, int j){
    	ArrayList<int[]> checkPoints = level.getCheckPoints();
    	if ((new Color(map.getRGB(x+contactPoints[i][0],y+contactPoints[i][1]))).equals(Color.BLACK)){
    		if (x+contactPoints[i][0] >= checkPoints.get(j)[0] && x+contactPoints[i][0] <= checkPoints.get(j)[0]+30 && y+contactPoints[i][1] >= checkPoints.get(j)[1] && y+contactPoints[i][1] <= checkPoints.get(j)[1]+50){
    			if (level.getCheckPassed().get(j)== false){
    				level.setCheckPassed(j);
    			}
    		}
    		
    	}
    }
    public void checkKPoint(BufferedImage map, int i, int n){
    	ArrayList<int[]> keyPoints = level.getkeyPoints();
    	if (x+contactPoints[i][0] >= keyPoints.get(n)[0] && x+contactPoints[i][0] <= keyPoints.get(n)[0]+17 && y+contactPoints[i][1] >= keyPoints.get(n)[1] && y+contactPoints[i][1] <= keyPoints.get(n)[1]+40){
    		if (level.getkeyAvailable().get(n)== true&&hasKey==false){
    			level.setkeyGot(n);
    			hasKey=true;
    			keyNum=n;
    		}
    	}
    }

}