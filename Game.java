/**
 * @(#)Game.java
 * FSE for ICS4U
 * "Get the best time" game. 
 * @Wan-yu (Elisa) Chao & Ai-Ching (Iris) Chang
 * @version 1.00 2015/4/28
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
public class Game extends JFrame implements ActionListener {
	Timer myTimer,clockTimer;
 	GamePanel map;
 	public Game() {  //setting up graphic bits
 		super("Game");
	 	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		setSize(800,600);
 		myTimer = new Timer(10,this);
 		clockTimer = new Timer (1000,this);
 		map=new GamePanel(this);
 		map.setLocation(0,0);
 		map.setSize(800,600);
 		add(map);
 		
 		setResizable(false);
 		setVisible(true);
 		
    }
    public static void main (String [] args){ 
		new Game();
	}
    public void start(){
    	myTimer.start();
    	clockTimer.start();
    }
    public void actionPerformed(ActionEvent evt){
    	Object source = evt.getSource();
    	if (map.getChara().getLevelDone()==false&&map.getScreen()=="GAME"){

	    	if (source==clockTimer){
	    		map.second();
	    	}
	    	if (source==myTimer){
	    		map.move();
	    		
	    	}
    	}
    	if (map.getChara().getLevelDone()==true&&map.getScreen()=="GAME"){
    		map.suck();
    	}
    	
    	map.repaint();
    		
    	
		
    }

}
 
class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{ 
	private Image white,lockedPic,keyPic, haskeyPic;
	private Image mainMenuPic, startHover,htPHover,startClick,htPClick;
	private Image helpMenuPic, backHover,backClick;
	private Image levelMenuPic, levelLabelPic;
	private Image overLay;
	private Image oneHover,oneClick,twoHover,twoClick,threeHover,threeClick, fourHover, fourClick;
	private ArrayList<Image> levelLocks = new ArrayList <Image>();
	private String [] bestTime = new String [4];
	private boolean [] levelUnlocked = new boolean [4];
	private BufferedImage sHitBox, hHitBox, bHitBox,oneHitBox, twoHitBox, threeHitBox, fourHitBox;
	private int [][] drawPts = {{224,184},{545,294},{244,404},{578,514}};
	private Game mainFrame;
	private int mosX,mosY,ncamX,ncamY,camX,camY;
	private boolean keyPressed;
	private boolean mousePressed;
	private boolean droppedCam=true;
	private boolean [] keys;
	private boolean canSome=false;
	private boolean jumpOff;
	private boolean sHover,hHover,bHover,oHover,tHover,thHover,fHover;
	private Person chara;
	private Level level;
	private int timePassed=0;
	private int dyingCount=0;
	private static final int RIGHT=1;
	private static final int LEFT=-1;
	private Color ORANGE = new Color (255,153,0);
	private Color INDIGO = new Color (138,0,255);
	private Color YELLOW = new Color(255,252,118);
	private Color ICE = new Color(170,255,236);
	private Color BLUE = new Color (0,165,255);
	private Font font;
	private Font fonts;
	private String onScreen = "MAINMENU";
	public GamePanel(Game m){
		mainFrame=m;
		load();
		mosX=0;
		mosY=0;
		
		int lev = 2; 
		chara=new Person();
		
		
		white = new ImageIcon("white2.png").getImage();
		overLay = new ImageIcon("Menu/overlay.png").getImage();
		lockedPic= new ImageIcon("Images/Interactives/LOCKED.png").getImage();
		keyPic= new ImageIcon("Images/Interactives/key.png").getImage();
		haskeyPic= new ImageIcon("Images/Interactives/HasKey.png").getImage();
		
		mainMenuPic= new ImageIcon("Menu/Main/MAINMENU.png").getImage();
		startHover=new ImageIcon("Menu/Main/SH.png").getImage();
		htPHover=new ImageIcon("Menu/Main/HtPH.png").getImage();
		startClick=new ImageIcon("Menu/Main/SC.png").getImage();
		htPClick=new ImageIcon("Menu/Main/HtPC.png").getImage();
		
		helpMenuPic= new ImageIcon("Menu/Help/HELP.png").getImage();
		backHover=new ImageIcon("Menu/Help/backHover.png").getImage();
		backClick=new ImageIcon("Menu/Help/backClick.png").getImage();
		
		
		levelMenuPic = new ImageIcon("Menu/Level/LEVELSELECTION.png").getImage();
		levelLabelPic = new ImageIcon("Menu/Level/levelLabel.png").getImage();
		oneHover=new ImageIcon("Menu/Level/1Hover.png").getImage();
		oneClick=new ImageIcon("Menu/Level/1Click.png").getImage();
		twoHover=new ImageIcon("Menu/Level/2Hover.png").getImage();
		twoClick=new ImageIcon("Menu/Level/2Click.png").getImage();
		threeHover=new ImageIcon("Menu/Level/3Hover.png").getImage();
		threeClick=new ImageIcon("Menu/Level/3Click.png").getImage();
		fourHover=new ImageIcon("Menu/Level/4Hover.png").getImage();
		fourClick=new ImageIcon("Menu/Level/4Click.png").getImage();
		for (int i=0;i<4;i++){
			int temp=i+1;
			
			Image tmp=new ImageIcon("Menu/Level/"+temp+"UL.png").getImage();
			levelLocks.add(tmp);
		}
		keys = new boolean[65535];
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		mainFrame.start();
	}
	public void levelLoad(int i){
		level = new Level (i);
		chara.setLevel(level);
		chara.setX(level.getDropx());
		chara.setY(level.getDropy());
		camX=level.getDropx();
		camY=level.getDropy()-100;
	}
	public void load(){
		try { //loading the font
			File file=new File("Happening.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, file);
			fonts = font.deriveFont(18f);
			sHitBox=ImageIO.read(new File("Menu/Main/SHitBox.png"));
			hHitBox=ImageIO.read(new File("Menu/Main/HtPHitBox.png"));
     		bHitBox=ImageIO.read(new File("Menu/Help/backHitBox.png"));
     		oneHitBox=ImageIO.read(new File("Menu/Level/1HitBox.png"));
     		twoHitBox=ImageIO.read(new File("Menu/Level/2HitBox.png"));
     		threeHitBox=ImageIO.read(new File("Menu/Level/3HitBox.png"));
     		fourHitBox=ImageIO.read(new File("Menu/Level/4HitBox.png"));
     		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     		ge.registerFont(font);
     		font=font.deriveFont(Font.PLAIN,25);
		}	 
		catch (IOException|FontFormatException e) {
		}
		Scanner infile=null;
		try{
			infile=new Scanner (new File ("info.txt"));
			}
		catch (IOException ex){
			}
		
		for (int i=0;i<4;i++){
			String tmp = infile.nextLine();
			if (tmp.equals("UNLOCKED")){
				levelUnlocked[i]=true;
			}
			else{
				levelUnlocked[i]=false;
			}

			bestTime[i]=infile.nextLine();;


			
				 
		}
		
	}
	public int chooseLevel(){
		Scanner kb = new Scanner(System.in);
		int lev = kb.nextInt();
		return lev;
	}
	public Person getChara(){
		return chara;
	}
	public void move(){
		if(dyingCount==0){
			chara.checkHit(level.getMap());
			moveLevel();
			moveChara();
			moveCam();
		}
		else if(dyingCount >0 && dyingCount<100000){
			dyingCount++;
		}
		else{
			dyingCount=0;
		}
		
	} 
	public void makeFile(int time, int lev){
		ArrayList <String> tmp=new ArrayList<String>();
		for (int i=0;i<4;i++){
			if (levelUnlocked[i]==true){
				tmp.add("UNLOCKED");
			}
			else{
				tmp.add("LOCKED");
			}
			tmp.add(bestTime[i]);
		}
			
				
		if (bestTime[lev].equals("NO BEST TIME")||(!bestTime[lev].equals("NO BEST TIME")&&Integer.parseInt(bestTime[lev])>time)){
			int index= lev*2+1;
			int index2 = lev*2+2;
			bestTime[lev]=time+"";	
			tmp.set(index,time+"");
			if (lev<3){
				tmp.set(index2,"UNLOCKED");
				levelUnlocked[lev+1]=true;
			}
			
				
		}
		
		try {
        	File file = new File("info.txt");
          	BufferedWriter output = new BufferedWriter(new FileWriter(file));
          	for (int i=0;i<8;i++){
          		output.write(tmp.get(i));
          		output.newLine();
          	}
          
          	output.close();
          	
        } 
        
        catch ( IOException e ) {
        	
        }
		
	}
	public void suck(){
		chara.suck();
		if (chara.getScale()==0){
			makeFile(timePassed, level.getLvN()-1);
			onScreen="LEVELSELECT";
		}
	}
	public void second(){
		timePassed+=1;	
	}
	public String getTime(int time){
		int sec=time%60;
		String sSec=sec+"";
		if (sec<10){
			 sSec="0"+sSec;
		}
		int min=time/60;
		String sMin=min+"";
		if (min<10){
			 sMin="0"+sMin;
		}
		String tmp = sMin+": "+sSec;
	
		return tmp;
	}
	public String getScreen(){
		return onScreen;
	}
	public void moveChara(){

		if (chara.getleavingWater()==true){
				
					canSome=false;
		}

		chara.checkWalking(false);
		if (chara.getSwim()==false){
			if (keys[KeyEvent.VK_LEFT]){
				chara.checkWalking(true);
				chara.changeDir(LEFT);
			}
			else if (keys[KeyEvent.VK_RIGHT]){
				chara.checkWalking(true);
				chara.changeDir(RIGHT);
			}
			if (keys[KeyEvent.VK_UP]){
				jumpOff=chara.jump();
				if (jumpOff==true){
					canSome=false;
				}
				if (jumpOff==false&&canSome==true&&chara.getCling()==false){
					canSome=false;
					chara.some("");
				}
				if ((chara.getCling()==true||chara.getPCling()==true)&&canSome==true){
					canSome=false;
					chara.some("OFFWALL");
				}
			}
			else if (keys[KeyEvent.VK_DOWN]){
				if (chara.getCling()==true||chara.getPCling()==true){
					chara.wallPush();
				}
				chara.slide();
			}
			
			if (keys[KeyEvent.VK_UP]==false){
				canSome=true;
			}
			
			if (keys[KeyEvent.VK_DOWN]==false){		
				chara.getUp();
			}
			chara.move();
			chara.gravity();
		}
		else{
			chara.swimSlow();
			chara.sink();
			if (keys[KeyEvent.VK_LEFT]){
				chara.swim("LEFT", level.getMap());
			}
			if (keys[KeyEvent.VK_RIGHT]){
				chara.swim("RIGHT", level.getMap());
			}
			if (keys[KeyEvent.VK_UP]){
				chara.swim("UP", level.getMap());
				
				
			}
			if (keys[KeyEvent.VK_DOWN]){
				chara.swim("DOWN", level.getMap());
			}
			chara.rotate();	
		}
		
	
			
	}
	public void moveCam(){
		if (chara.getMoved()==false){
			if (Math.abs(camX-chara.getX())>3){
				if (Math.abs(camX-chara.getX())>50){
					if (camX>chara.getX()){
						camX-=5;
					}
					else{
						camX+=5;
					}
				}
				else{
				
					if (camX>chara.getX()){
						camX-=2;
					}
					else{
						camX+=2;
					}
				}
			}
			else{
				camX=chara.getX();
			}
		}
		
		else{
			if (Math.abs(camX-chara.getX())>20){
				if (chara.getX()>600&&chara.getDir()==1){
						camX+=chara.getxMoved();
					}
				else if (chara.getX()<1400&&chara.getDir()==-1){
						camX+=chara.getxMoved();
				}
				}
			}
		
		camX=Math.max(400,camX);
		camX=Math.min(level.getWidth(),camX);
		if (chara.getinAir()==false){
			if (Math.abs(camY-chara.getY())>3){
				if (Math.abs(camY-chara.getY())>50){
					if (camY>chara.getY()){
						camY-=5;
					}
					else{
						if(camY<chara.getY()){
						camY+=5;
						}
					}
				}
				else{
					if (camY>chara.getY()){
						camY-=2;
					}
					else{
						if(camY<chara.getY()){
						camY+=2;
						}
					}
				}
				
			}
			else{
				camY=chara.getY();
			}
		}
		else{
			if (chara.getyMoved()>0){
				chara.reachingApex(false);
			}
			if (camY>=300){
				chara.reachingApex(true);
			}
			if (Math.abs(camY-chara.getY())>20||chara.reachedApex()==false&&chara.getyMoved()<0){
				if (chara.reachedApex()==false&&camY>chara.getApex()){
					if (Math.abs(camY-chara.getY())>15){
						camY+=-1*(Math.abs(chara.getyMoved())+3);
					}
					else{
						camY+=-1*Math.abs(chara.getyMoved());
					}
					if (camY<=chara.getApex()){
						chara.reachingApex(true);
					}
				}
					
				else{
					if (Math.abs(camY-chara.getY())>50){
						camY+=-1*(chara.getyMoved()+5);
					}
					else{
						camY+=-1*chara.getyMoved();
					}
				}
				
			} 
		}
		camY=Math.max(300,camY);
		camY=Math.min(level.getHeight(),camY);
		if (chara.getDeath()==true){
			camX=chara.getX();
			camY=chara.getY();
			camX=Math.max(400,camX);
			camX=Math.min(level.getWidth(),camX);
			camY=Math.max(300,camY);
			camY=Math.min(level.getHeight(),camY);
		}
	}
	public void moveLevel(){
		level.movePlatforms();
		level.moveTraps();
		level.meltIce(camX,camY);
	}
	public int camAdjust(String temp,int ori){
		if (temp=="X"){
			return ori-camX+400;
		}
		
		if (temp=="Y"){
			return ori-camY+300;
		}
		return 0;
	}
	public void addNotify() {
        super.addNotify();
        requestFocus();
    }
    public void gamePaint(Graphics g){
    	Graphics2D g2 = (Graphics2D)g;
    	Image pic=chara.getPic();
    	g.drawImage(white,0,0,this);
    	ArrayList<int[]> keyPoints = level.getkeyPoints();
		
		for (int i=0;i < keyPoints.size();i++){
			if(level.getkeyAvailable().get(i) == true){
				g.drawImage(keyPic,camAdjust("X",keyPoints.get(i)[0]),camAdjust("Y",keyPoints.get(i)[1]),this);
			}
		}
    	ArrayList<Platform> tmpP = level.getPlats();
    	ArrayList<Traps> tmpT = level.getTraps();
    	ArrayList<Traps> swingSaw = new ArrayList<Traps>();
    	for(int i =0;i< tmpT.size();i++){
    		if (tmpT.get(i).getType()==4){
    			swingSaw.add(tmpT.get(i));
    		}
    		if (tmpT.get(i).getisSpawned()==true&&tmpT.get(i).getType()!=4){
    		
	    		if (tmpT.get(i).getAng()==0){
	    			g.drawImage(tmpT.get(i).getPic(),camAdjust("X",tmpT.get(i).getDX()),camAdjust("Y",tmpT.get(i).getDY()),this);
	    		}
	    		else{
					AffineTransform saveXform = g2.getTransform();
					AffineTransform at = new AffineTransform();
					at.rotate(Math.toRadians(tmpT.get(i).getAng()),camAdjust("X",tmpT.get(i).getX()),camAdjust("Y",tmpT.get(i).getY()));
					g2.transform(at);
					g2.drawImage(tmpT.get(i).getPic(),camAdjust("X",tmpT.get(i).getDX()),camAdjust("Y",tmpT.get(i).getDY()),this);
					g2.setTransform(saveXform);
	    		}
    		}
    	}
    	
    	if (chara.getSwim()==true&&chara.getLevelDone()==false){
    		AffineTransform saveXform = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians((90-chara.getAn())%360),camAdjust("X",chara.getX()),camAdjust("Y",chara.getY()));
			g2.transform(at);
			g2.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
			g2.setTransform(saveXform);
			
		}
    	g.drawImage(level.getMap(),camAdjust("X",0),camAdjust("Y",0),this);

    	
    	for(int i =0;i< tmpP.size();i++){
    		
    		
    	 //DRAW PLATFORMS
    		if ((tmpP.get(i).getType()).equals("LOCKED")==true&&(tmpP.get(i).getLocked()==true)){
    			g.drawImage(lockedPic,camAdjust("X",tmpP.get(i).getX()),camAdjust("Y",tmpP.get(i).getY()),this);
    		}
    		else if ((tmpP.get(i).getType()).equals("LOCKED")==false){
    		
	    		if ((tmpP.get(i).getType()).equals("MOVING")==true){
	    			g.setColor(ORANGE);
	    		}
	    		if ((tmpP.get(i).getType()).equals("DROPPING")==true){
	    			g.setColor(INDIGO);
	    		}
	    		if ((tmpP.get(i).getType()).equals("BOUNCING")==true){
	    			g.setColor(YELLOW);
	    		}
	    		if ((tmpP.get(i).getType()).equals("ICE")==true){
	    			g.setColor(ICE);
	    		}
	    		g.fillRect(camAdjust("X",tmpP.get(i).getX()),camAdjust("Y",tmpP.get(i).getY()),tmpP.get(i).getWidth(),tmpP.get(i).getHeight());
	    	}
    	}
    	for(int i =0;i< swingSaw.size();i++){
    		AffineTransform saveXform = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(swingSaw.get(i).getAng()),camAdjust("X",swingSaw.get(i).getX()),camAdjust("Y",swingSaw.get(i).getY()));
			g2.transform(at);	
			g2.drawImage(swingSaw.get(i).getPic(),camAdjust("X",swingSaw.get(i).getDX()),camAdjust("Y",swingSaw.get(i).getDY()),this);
			g2.setTransform(saveXform);
    	}
    		
    	ArrayList<Image> checkPics = level.getCheckPics();
		for (int i=0;i < checkPics.size();i++){
			if(level.getCheckPassed().get(i) == true){
				g.drawImage(checkPics.get(i),camAdjust("X",0),camAdjust("Y",0),this);
			}
		}
    	
    	if (chara.getSwim()==false&&chara.getLevelDone()==false){
    		
    		if (chara.getCling()==false){
	    		g.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
    		}
    		else{
    			g.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)+chara.getDir()*5),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
    		}
    	}
		if (chara.getLevelDone()==true){
			AffineTransform saveXform = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians((90-chara.getAn())%360),camAdjust("X",(int)(chara.getX())),camAdjust("Y",(int)(chara.getY())));
			g2.transform(at);                                 
   			g2.setComposite(AlphaComposite.SrcOver.derive((float)chara.getScale())); 
			g2.drawImage(pic,camAdjust("X",chara.getX()-(int)(pic.getWidth(null)/2)),camAdjust("Y",chara.getY()-(int)(pic.getHeight(null)/2)),this);
			g2.setTransform(saveXform);
		
			
		}
		g.drawImage(overLay,0,0,this); 
		g.setFont(fonts); 
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		if (chara.getSwim()==true&&chara.getLevelDone()==false){
    		int tmp=(int)(chara.getBreathCount()/100);
			g.setColor(Color.WHITE);
			g.drawString("BREATHCOUNT:",65,60);
			g.setColor(BLUE);
			g.fillRect(180,50,tmp,10);
		
    	}
    	g.setFont(font); 
		g.setColor(Color.WHITE);
        //makes the font pretty when its drawn
		g.drawString(getTime(timePassed),700,25);
		g.drawString(chara.getDeathCount()+" DEATHS!",50,30);
		if (chara.gethasKey()==true){
			g.drawImage(haskeyPic,280,15,this);
		}
    }
    public void mainmenuPaint(Graphics g){
    	g.drawImage(mainMenuPic,0,0,this);
    	Color c1= new Color(sHitBox.getRGB(mosX,mosY));
    	Color c2= new Color(hHitBox.getRGB(mosX,mosY));
    	sHover=false;
    	hHover=false;
    	if (c1.equals(Color.WHITE)==true){
    		sHover=true;
    		if (mousePressed==false){
    			g.drawImage(startHover,0,0,this);
    			
    		}
	    	if (mousePressed==true){
	    		g.drawImage(startClick,0,0,this);
	    	}
    		
    	}
    	if (c2.equals(Color.WHITE)==true){
    		hHover=true;
    		if (mousePressed==false){
    			g.drawImage(htPHover,0,0,this);
    		}
    		if (mousePressed==true){
	    		g.drawImage(htPClick,0,0,this);
	    		
	    	}
    		
    	}
    }
    public void helpmenuPaint(Graphics g){
    	g.drawImage(helpMenuPic,0,0,this);
    	Color c1= new Color(bHitBox.getRGB(mosX,mosY));
    	bHover=false;	
    	if (c1.equals(Color.WHITE)==true){
    		bHover=true;
    		if (mousePressed==false){
    			g.drawImage(backHover,0,0,this);
    			
    		}
	    	if (mousePressed==true){
	    		g.drawImage(backClick,0,0,this);
	    	}
    		
    	}
    }
    public boolean levelCheckHover(Graphics g, Color c, int i, Image HPic, Image CPic){
    	if (c.equals(Color.WHITE)==true&&levelUnlocked[i]){
    		
    		if (mousePressed==false){
    			g.drawImage(HPic,0,0,this);
    			
    		}
	    	if (mousePressed==true){
	    		g.drawImage(CPic,0,0,this);
	    	}
    		return true;
    	}
    	return false;
    }
    public void drawBestTime(Graphics g, int i){
    	Graphics2D g2 = (Graphics2D)g;
    	g.setColor(Color.BLACK);
    	String tmp;
    	if (bestTime[i].equals("NO BEST TIME")){
    		tmp="NO BEST TIME";
    	}
    	else{
    		tmp="BEST TIME: "+getTime(Integer.parseInt(bestTime[i]));
    	}
		g.setFont(font); 
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //makes the font pretty when its drawn
		FontMetrics metrics = g.getFontMetrics(font);
		int h = metrics.getHeight();
		int w = metrics.stringWidth(tmp);
		g.drawString(tmp,(int)(drawPts[i][0]-w/2),(int)(drawPts[i][1]+5));
    	
    }
    public void levelmenuPaint(Graphics g){
    	g.drawImage(levelMenuPic,0,0,this);
    	Color c= new Color(bHitBox.getRGB(mosX,mosY));
    	Color c1=new Color(oneHitBox.getRGB(mosX,mosY));
    	Color c2=new Color(twoHitBox.getRGB(mosX,mosY));
    	Color c3=new Color(threeHitBox.getRGB(mosX,mosY));
    	Color c4=new Color(fourHitBox.getRGB(mosX,mosY));
    	bHover=false;	
    	oHover=false;
    	tHover=false;
    	thHover=false;
    	fHover=false;
    	if (c.equals(Color.WHITE)==true){
    		bHover=true;
    		if (mousePressed==false){
    			g.drawImage(backHover,0,0,this);
    			
    		}
	    	if (mousePressed==true){
	    		g.drawImage(backClick,0,0,this);
	    	}
    		
    	}
    	oHover=levelCheckHover(g, c1, 0, oneHover, oneClick);
    	tHover=levelCheckHover(g, c2, 1, twoHover, twoClick);
    	thHover=levelCheckHover(g, c3, 2, threeHover, threeClick);
    	fHover=levelCheckHover(g, c4, 3, fourHover, fourClick);
    	for (int i=0;i<4;i++){
    		if (levelUnlocked[i]==false){
    			g.drawImage(levelLocks.get(i-1),0,0,this);
    			break;
    		}
    		drawBestTime(g,i);
    	}
    	g.drawImage(levelLabelPic,0,0,this);
    }
    @Override
    public void paintComponent(Graphics g){
    	if (onScreen.equals("GAME")){
    		gamePaint(g);
    	}
    	if (onScreen.equals("MAINMENU")){
    		mainmenuPaint(g);
    	}
    	if (onScreen.equals("HELP")){
    		helpmenuPaint(g);
    	}
    	if (onScreen.equals("LEVELSELECT")){
    		levelmenuPaint(g);
    	}

		
		
    }
    public void mouseReleased(MouseEvent e){
    	mousePressed=false;
    	if (sHover==true&&onScreen=="MAINMENU"){
    		onScreen="LEVELSELECT";
    		sHover=false;
    	}
    	if (hHover==true&&onScreen=="MAINMENU"){
    		onScreen="HELP";
    		hHover=false;
    	}
    	if (bHover==true&&(onScreen=="LEVELSELECT"||onScreen=="HELP")){
    		onScreen="MAINMENU";
    		bHover=false;
    	}
    	if (onScreen=="LEVELSELECT"){
	    	if (oHover==true){
	    		levelLoad(1);
	    		onScreen="GAME";
	    		oHover=false;
	    	}
	    	if (tHover==true){
	    		levelLoad(2);
	    		onScreen="GAME";
	    		tHover=false;
	    	}
	    	if (thHover==true){
	    		levelLoad(3);
	    		onScreen="GAME";
	    		thHover=false;
	    	}
	    	if (fHover==true){
	    		levelLoad(4);
	    		onScreen="GAME";
	    		fHover=false;
	    	}
    	}
    }
    public void mousePressed(MouseEvent e){
    	mousePressed=true;
    	}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseDragged(MouseEvent e){
    	
    }
    public void mouseMoved(MouseEvent e){
    	mosX=e.getX();
    	mosY=e.getY();}
    
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e){
    	keyPressed=true;
    	keys[e.getKeyCode()]=true;
    }
    public void keyReleased (KeyEvent e){
    	keyPressed=false;
    	keys[e.getKeyCode()]=false;
    }
}