import java.awt.*;
import java.awt.Polygon;
import javax.sound.sampled.*;
import java.io.File;

public class Explorer
{
		int dir;
		int x;
		int y;
		int steps;
		private boolean gameOver;
		private boolean hasKey;
		private boolean eatGhost;

		public Explorer(int x, int y, int dir,int steps,boolean gameOver, boolean hasKey, boolean eatGhost)
		{

			this.x = x;
			this.y = y;
			this.dir = dir;
			this.gameOver = gameOver;
			this.steps = steps;
			this.hasKey = hasKey;
			this.eatGhost = eatGhost;

		}
		public void move(int key, String[][]maze){
		System.out.println(key+"key");
			try
			{
		if(key==39){ // right   		0=up, 1=right, 2=down, 3=left
					if(dir==3)
						dir=0;		//dir=(dir+1)%4;
					else dir++;
				}
				if(key==37){ // left
					if(dir==0)
						dir=3;
					else dir--;
				}

				if(key==38){ // up
					if (dir==1){
						if (maze[y][x+1].equals(" ")){
							x++;
							steps++;
						}
						if (maze[y][x+1].equals("E")){
							x++;
							gameOver = true;
						}
						if (maze[y][x+1].equals("K")){
							x++;
							steps++;
							hasKey = true;
						}
						if (maze[y][x+1].equals("G")){
							x++;
							steps++;
							eatGhost = true;
						}
					}
					else if(dir==2){
						if (maze[y+1][x].equals(" ")){
							y++;
							steps++;
						}
						if (maze[y+1][x].equals("E")){
							y++;
							gameOver = true;
						}
						if (maze[y+1][x].equals("K")){
							y++;
							steps++;
							hasKey = true;
						}
						if (maze[y+1][x].equals("G")){
							y++;
							steps++;
							eatGhost = true;
						}
					}
					else if(dir==3){
						if (maze[y][x-1].equals(" ")){
							x--;
							steps++;
						}
						if (maze[y][x-1].equals("E")){
							x--;
							gameOver = true;
						}
						if (maze[y][x-1].equals("K")){
							x--;
							steps++;
							hasKey = true;
						}
						if (maze[y][x-1].equals("G")){
							x--;
							steps++;
							eatGhost = true;
						}
					}
					else if(dir==0){
						if (maze[y-1][x].equals(" ")){
							y--;
							steps++;
						}
						if (maze[y-1][x].equals("E")){
							y--;
							gameOver = true;
						}
						if(maze[y-1][x].equals("K")){
							y--;
							steps++;
							hasKey = true;
						}
						if(maze[y-1][x].equals("G")){
							y--;
							steps++;
							eatGhost = true;
						}
							}
						}
					}catch(Exception e)
						{
						}

			}

			public int getX(){
				return x;
			}

			public int getY(){
				return y;
			}

			public int getDir(){
				return dir;
			}
			public int getSteps(){
				return steps;
			}
			public boolean getGame(){
				return gameOver;
			}
			public boolean returnKey(){
				return hasKey;
			}

			public boolean ateGhost(){
				return eatGhost;
			}




		}

