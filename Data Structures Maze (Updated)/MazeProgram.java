
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.GradientPaint;
import javax.sound.sampled.*;

public class MazeProgram extends JPanel implements KeyListener, MouseListener{

	JFrame frame;
	private Thread t;
	private boolean gameOn;
	private int frameX = 1200;
	private int frameY = 1000;
	Explorer player;

	//declare an array to store the maze

	String[][] maze = new String[13][1];

	BufferedImage image;
	BufferedImage imageK;
	BufferedImage imageD;
	BufferedImage imageP;
	BufferedImage imageG;
	GradientPaint paint;
	int y=3,x=1;
	int endX = 0, endY = 0;
	int keyX = 0, keyY = 0;
	int doorX = 0, doorY = 0;
	int ghostX, ghostY = 0;

	private int dir=1;
	private int deltaY = 50;
	private int deltaX = 110;
	private int unitSize=20;
	private int steps = 0;
	private boolean gameOver = false;
	private boolean hasKey = false;
	private boolean eatGhost = false;
	int playerLocationX = 0;
	int playerLocationY = 0;


	private ArrayList<Integer> countWalls = new ArrayList<Integer>();
	private ArrayList<Polygon> polyList = new ArrayList<Polygon>();

	long startTime;
	Clip audioClip;
	Clip audioClip2;


	public MazeProgram(){

		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameX, frameY);
		frame.setVisible(true);
		frame.addKeyListener(this);
		setWalls();
		music();

	}
	public void paintComponent(Graphics g){

		super.paintComponent(g);
		g.setColor(Color.BLACK);	//this will set the background color
		g.fillRect(0,0,frameX,frameY);

		//drawBoard here!
		g.setColor(Color.BLUE); //color of maze

		for (int i=0;i<maze.length;i++){
			for (int j=0;j<maze[0].length;j++){

				if (maze[i][j].equals("+"))
					g.drawRect(j*unitSize+deltaX, i*unitSize+deltaY, unitSize, unitSize);
				if(hasKey && maze[i][j].equals("D"))
					maze[i][j] = " ";

			}
		}

		if(hasKey){

			g.setFont(new Font("ARIAL",Font.BOLD,25));
			g.setColor(Color.WHITE);
			g.drawString("You Unlocked the door!" , frameX/2-600, frameY/2+150);
		}

		g.setColor(Color.GREEN); //color of end goal
		g.fillOval(endX*unitSize+deltaX, endY*unitSize+deltaY, unitSize, unitSize);

		//g.setColor(Color.GREEN);
		//g.fillOval(keyX*unitSize+deltaX, keyY*unitSize+deltaY, unitSize, unitSize);

		g.setFont(new Font("ARIAL",Font.BOLD,22));
		g.setColor(Color.RED); //color of step counter
		g.drawString("Steps: "+steps, frameX/2-50,340);


		g.setColor(Color.GRAY); //color of 3D background
		g.fillRect(348, 348, 494, 494);

		g.setColor(Color.BLACK); //color of 3D outline
		g.drawRect(350, 350, 490, 490);
		for (int i=350+70;i<=770;i+=70){
			g.drawLine(350, i, 350+490, i);
		}

		g.setColor(Color.BLACK); //color of 3D walls
		for (int i=1;i<4;i++){
			g.fillRect(350+70*(i-1), 350+70*i, 70, 350-70*2*(i-1));
		}
		for (int i=1;i<4;i++){
			g.fillRect(770-70*(i-1), 350+70*i, 70, 350-70*2*(i-1));
		}
		g.fillRect(560, 560, 70, 70);
		g.setColor(Color.BLACK);
		g.drawRect(560, 560, 70, 70);

		for (Polygon item: polyList){
			g.setColor(Color.BLUE);
			g.fillPolygon(item);
			g.setColor(Color.BLACK);
			g.drawPolygon(item);

		}

		//0=up, 1=right, 2=down, 3=left
				if(dir == 0){
					try{
						image = ImageIO.read(new File("aUP.png"));
						imageP = ImageIO.read(new File("pacmanUp.png"));
						System.out.println("UP");
					}catch(IOException e){}
				}
				if(dir == 1){
					try{
						image = ImageIO.read(new File("aRight.png"));
						imageP = ImageIO.read(new File("pacman.png"));
					}catch(IOException e){}
				}
				if(dir == 2){
					try{
						image = ImageIO.read(new File("aDown.png"));
						imageP = ImageIO.read(new File("pacmanDown.png"));
					}catch(IOException e){}
				}
				if(dir == 3){
					try{
						image = ImageIO.read(new File("aLeft.png"));
						imageP = ImageIO.read(new File("pacmanLeft.jpg"));
					}catch(IOException e){}
				}

				try{
					imageK = ImageIO.read(new File("cherryBomb.png"));
					imageG = ImageIO.read(new File("pacmanGhost.jpg"));
				}
				catch(IOException e){}

				try{
					imageD = ImageIO.read(new File("door.png"));
				}catch(IOException e){}
				playerLocationX = x*unitSize+deltaX;
				playerLocationY = y*unitSize+deltaY;
				g.drawImage(imageP,playerLocationX,playerLocationY,unitSize-1,unitSize-1,null);
				g.drawImage(image, 10,100,50,50,null);

				if(!hasKey){
				g.drawImage(imageK, keyX*unitSize+deltaX, keyY*unitSize+deltaY, unitSize-1, unitSize-1, null);
				g.drawImage(imageD, doorX*unitSize+deltaX, doorY*unitSize+deltaY, unitSize-1, unitSize-1, null);
				if(playerLocationX == 310 && playerLocationY == 270 && dir == 3){
					g.drawImage(imageK, 560+(70/4), 560+(70/4), 35, 35, null);
				}
				if(playerLocationX == 290 && playerLocationY == 270 && dir == 3){
					g.drawImage(imageK, 560, 560, 70, 70, null);
				}
				if(playerLocationX == 270 && playerLocationY == 270 & dir == 3){
					g.drawImage(imageK, 560-35, 560+35, 140, 140, null);
				}
				if(playerLocationX == 910 && playerLocationY == 170 && dir == 0){
					g.drawImage(imageD, 560+(70/4), 560+(70/4), 35, 35, null);
				}
				if(playerLocationX == 910 && playerLocationY == 150 && dir == 0){
					g.drawImage(imageD, 560, 560, 70, 70, null);
				}
				if(playerLocationX == 910 && playerLocationY == 130 & dir == 0){
					g.drawImage(imageD, 490, 490, 210, 210, null);
					g.setFont(new Font("ARIAL",Font.BOLD,25));
					g.setColor(Color.WHITE);
					g.drawString("Find Key to Unlock Door!" , frameX/2-600, frameY/2+150);
				}
				if(playerLocationX == 910 && playerLocationY == 110 & dir == 0){
					g.drawImage(imageD, 350, 350, 494, 494, null);
					g.setFont(new Font("ARIAL",Font.BOLD,25));
					g.setColor(Color.WHITE);
					g.drawString("Find Key to Unlock Door!" , frameX/2-600, frameY/2+150);
				}

			}


			if(!eatGhost){
					g.drawImage(imageG,ghostX*unitSize+deltaX, ghostY*unitSize+deltaY, unitSize-5, unitSize-5,null);
				if(playerLocationX == 890 && playerLocationY == 210 && dir == 1){
					g.drawImage(imageG, 560+(70/4), 560+(70/4), 35, 35, null);
				}
				if(playerLocationX == 910 && playerLocationY == 210 && dir == 1){
					g.drawImage(imageG, 560, 560, 70, 70, null);
				}
				if(playerLocationX == 930 && playerLocationY == 210 & dir == 1){
					g.drawImage(imageG, 560-35, 560+35, 140, 140, null);
				}

					}

			if(eatGhost){
				steps-=10;
				g.setFont(new Font("ARIAL",Font.BOLD,25));
				g.setColor(Color.WHITE);
				g.drawString("You Saved 10 steps!", frameX/2-600, frameY/2+80);

					}


				System.out.println(playerLocationX);
				System.out.println(y*unitSize+deltaY);

		//other commands that might come in handy


		//you can also use Font.BOLD, Font.ITALIC, Font.BOLD|Font.Italic
		//g.drawOval(x,y,10,10);
		//g.fillRect(x,y,100,100);
		//g.fillOval(x,y,10,10);

		if (gameOver){
			g.setFont(new Font("ARIAL",Font.BOLD,50));
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, frameX, frameY);
			g.setColor(Color.WHITE);
			g.drawString("You Win!", frameX/2-200, frameY/2-50);
			g.drawString(steps + " Steps", frameX/2-220, frameY/2);
			g.drawString((System.currentTimeMillis()-startTime)/1000.0 + " seconds", frameX/2-300, frameY/2+50);

		}



	}
	public void setBoard()
	{

		File name = new File("maze1.txt");
		int row=0;
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			while( (text=input.readLine())!= null){

				maze[row] = text.split("");
				row++;
			}

			for (int i=0;i<maze.length;i++){
				for (int j=0;j<maze[0].length;j++){
					if (maze[i][j].equals("S")){
						y = i;
						x = j;
					}
					if (maze[i][j].equals("E")){
						endX = j-1;
						endY = i;
					}
					if(maze[i][j].equals("K")){
						keyX = j;
						keyY = i;
					}
					if(maze[i][j].equals("D")){
						doorX = j;
						doorY = i;
					}
					if(maze[i][j].equals("G")){
						ghostX = j;
						ghostY = i;
					}
				}
			}

			player = new Explorer(x,y,dir,steps,gameOver,hasKey,eatGhost);


		}
		catch (IOException io)
		{
			System.err.println("File error");
		}
		startTime = System.currentTimeMillis();
		setWalls();
	}

	public void music(){

	       try{
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("pacman_beginning.wav"));
	            audioClip = AudioSystem.getClip();
	            audioClip.open(audioStream);
	        }catch(Exception e){
				System.out.println("Null");
			}
			if(gameOver){
				audioClip.stop();
			}else{
	        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		}

    }

    public void cherryMusic(){
		if(hasKey){
			try{
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("pacman_eatfruit.wav"));
		audioClip = AudioSystem.getClip();
		audioClip.open(audioStream);
		}catch(Exception e){
			System.out.println("Null");
		}
		audioClip.start();
		}
	}

	public void setWalls(){



		countWalls = new ArrayList<Integer>();

		if (dir == 1){//right
			if (x+3 < maze[0].length){
				for (int i = 1;i<=3;i++){
					if (maze[y-1][x+i].equals("+"))
						countWalls.add(i);
					if (maze[y+1][x+i].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y][x+3].equals("+"))
					countWalls.add(7);
				if (maze[y][x+2].equals("+"))
					countWalls.add(8);
				if (maze[y][x+1].equals("+"))
					countWalls.add(9);
			}
			else if (x+2 < maze[0].length){
				for (int i = 1;i<=2;i++){
					if (maze[y-1][x+i].equals("+"))
						countWalls.add(i);
					if (maze[y+1][x+i].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y][x+2].equals("+"))
					countWalls.add(8);
				if (maze[y][x+1].equals("+"))
					countWalls.add(9);
			}
			else{
				if (maze[y-1][x+1].equals("+"))
					countWalls.add(1);
				if (maze[y+1][x+1].equals("+"))
					countWalls.add(1+3);
				if (maze[y][x+1].equals("+"))
					countWalls.add(9);
			}
		}
		if (dir == 2){ //down
			if (y+3 < maze.length){
				for (int i = 1;i<=3;i++){
					if (maze[y+i][x+1].equals("+"))
						countWalls.add(i);
					if (maze[y+i][x-1].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y+3][x].equals("+"))
					countWalls.add(7);
				if (maze[y+2][x].equals("+"))
					countWalls.add(8);
				if (maze[y+1][x].equals("+"))
					countWalls.add(9);
			}
			else if (y+2 < maze.length){
				for (int i = 1;i<=2;i++){
					if (maze[y+i][x+1].equals("+"))
						countWalls.add(i);
					if (maze[y+i][x-1].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y+2][x].equals("+"))
					countWalls.add(8);
				if (maze[y+1][x].equals("+"))
					countWalls.add(9);
			}
			else{
				if (maze[y+1][x+1].equals("+"))
					countWalls.add(1);
				if (maze[y+1][x-1].equals("+"))
					countWalls.add(1+3);
				if (maze[y+1][x].equals("+"))
					countWalls.add(9);
			}
		}
		if (dir == 3){ //left
			if (x-3 >= 0){
				for (int i = 1;i<=3;i++){
					if (maze[y+1][x-i].equals("+"))
						countWalls.add(i);
					if (maze[y-1][x-i].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y][x-3].equals("+"))
					countWalls.add(7);
				if (maze[y][x-2].equals("+"))
					countWalls.add(8);
				if (maze[y][x-1].equals("+"))
					countWalls.add(9);
			}
			else if (x-2 >= 0){
				for (int i = 1;i<=2;i++){
					if (maze[y+1][x-i].equals("+"))
						countWalls.add(i);
					if (maze[y-1][x-i].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y][x-2].equals("+"))
					countWalls.add(8);
				if (maze[y][x-1].equals("+"))
					countWalls.add(9);
			}
			else{
				if (maze[y+1][x-1].equals("+"))
					countWalls.add(1);
				if (maze[y-1][x-1].equals("+"))
						countWalls.add(1+3);
				if (maze[y][x-1].equals("+"))
					countWalls.add(9);
			}
		}
		if (dir == 0){ //up
			if (y-3 >= 0){
				for (int i = 1;i<=3;i++){
					if (maze[y-i][x-1].equals("+"))
						countWalls.add(i);
					if (maze[y-i][x+1].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y-3][x].equals("+"))
					countWalls.add(7);
				if (maze[y-2][x].equals("+"))
					countWalls.add(8);
				if (maze[y-1][x].equals("+"))
					countWalls.add(9);
			}
			else if (y-2 >= 0){
				for (int i = 1;i<=2;i++){
					if (maze[y-i][x-1].equals("+"))
						countWalls.add(i);
					if (maze[y-i][x+1].equals("+"))
						countWalls.add(i+3);
				}
				if (maze[y-2][x].equals("+"))
					countWalls.add(8);
				if (maze[y-1][x].equals("+"))
					countWalls.add(9);
			}
			else{
				if (maze[y-1][x-1].equals("+"))
					countWalls.add(1);
				if (maze[y-1][x+1].equals("+"))
					countWalls.add(1+3);
				if (maze[y-1][x].equals("+"))
					countWalls.add(9);
			}
		}

		polyList = new ArrayList<Polygon>();
		for (Integer wall: countWalls){
			polyList.add((new Wall(wall)).getPoly());
		}
	}

	public void keyPressed(KeyEvent key){

		player.move(key.getKeyCode(),maze);
		x = player.getX();
		y = player.getY();
		dir = player.getDir();
		steps = player.getSteps();
		gameOver = player.getGame();
		hasKey = player.returnKey();
		eatGhost = player.ateGhost();
		setWalls();
		repaint();

	}
	public void keyReleased(KeyEvent ke){

	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}