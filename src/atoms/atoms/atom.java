package atoms;



import java.awt.Color;

import acm.program.*;
import acm.util.RandomGenerator;
import acm.graphics.*;

public class atom extends GraphicsProgram {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RandomGenerator rand = new RandomGenerator();
	public static final int SW=1275;
	public static final int SH=738;
	public static final int DIA = 10;
	
public void run(){
	int n = 4, m = 4;
	int t = m*n + 1;
	ball balls[] = new ball[t];
	Thread threads[] = new Thread[t];
	ball.initialize(t, balls, threads, this);
	
	GPoint loc[] = new GPoint[t];//(SW/2-400,SH/2-200);
	int gap = 100;
	int v = 0;
	for(int i = 0; i < m ; i++ ){
		for(int j = 0; j < n ; j++){
		
		loc[v++] = new GPoint(SW/2-gap*(m-1)/2D+i*gap,SH/2-gap*(n-1)/2D+j*gap);
		}
	}
	System.out.println("Here is V: "+ v);
	
	loc[v] = new GPoint(SW/2 + DIA /2 ,SH/2 + DIA/2);
	Acceleration vel[] = new Acceleration[t];
	int f = 10;
	for(int i = 0; i < t-1 ; i++ ){
		vel[i] = new Acceleration( (SH/2 - loc[i].getY())/f,-(SW/2 - loc[i].getX())/f);
	}
	
	vel[v] = new Acceleration(-0.1,0.1);
	
	for(int i = 0; i < t-1; i++ ){
	balls[i] = new ball(rand.nextInt(1, 3), loc[i], vel[i] , rand.nextInt(5, 10), rand.nextColor());
	add(balls[i]);
	}
	
	balls[v] = new ball(800, loc[v], vel[v] , 10, Color.red);
	add(balls[v]);
	
	waitForClick();
	
	ball.resumeAll();
	
	
	



	
	
}

}
