package atoms;
import java.awt.Color;

import acm.graphics.*;


public class ball extends GOval implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SW=1275;
	public static final int SH=738;
	public static final int DEL = 50; //50
	public static final double G = 100;//100
	public static final double CF = 0.6;//contact factor
	public static double [][] table ;// mass, x - position  , y - position
	public static int DIA = 10; // default diameter of balls 
	static int totalBalls = 0;
	static int count = 0;
	static atom canvas;
	static ball balls[];
	static Thread threads[];
	
	int ballID;
	Acceleration velocity = new Acceleration();
	double mass;
	int dia;
	
	boolean suspendThread = true;
	
	public ball(double mass, GPoint location, Acceleration velocity, int dia,Color c){
		
		super(location.getX(), location.getY(), dia, dia);
		ballID = count++;
		this.dia = dia;
		setFilled(true);
		setColor(c);
		setFillColor(c);
		setVisible(true);
		
		System.out.println("ball id:" + ballID);
		System.out.println("location: "+location);
		System.out.println("velocity: "+velocity);
		
		this.mass = mass;
		setLocation(location);
		this.velocity = velocity;
		
		table[0][ballID] = mass;
		table[1][ballID] = getX() + dia/2;
		table[2][ballID] = getY() + dia/2;
		
		threads[ballID] = new Thread (this);
		threads[ballID].start();
		
	}
	
	static void resumeAll(){
		for(int i = 0; i<count; i++ ){
			balls[i].myResume();
		}
	}
	
	void mySuspend(){
		suspendThread = true;
	}
	synchronized void myResume(){
		suspendThread = false;
		notify();
	}
	static void initialize(int n, ball b[], Thread th[], atom can){
		totalBalls = n;
		table = new double[3][totalBalls];
		balls = b;
		threads = th;
		canvas = can;
	}
	
	
	int getBallID(){
		return ballID;
	}
	void setMass(double mass){
		this.mass = mass;
		table[ballID][0] = mass;
	}
	
	
	
	void setVelocity(Acceleration velocity){
		this.velocity = velocity;
	}
	static void updateTable(ball b){
		
		table[1][b.ballID] =  b.getX() + b.dia/2;
		table[2][b.ballID] =  b.getY() + b.dia/2;
	}
	
	/*
	 Calculates acceleration of index due to all other balls except for balls at same location. 
	 */
	static  Acceleration  getAcceleration(int index){
		
		Acceleration acc = new Acceleration();
		double dX,dY,r,a,sine,cos;
		for(int i = 0; i < count ; i++){
			
			if(i == index)
				continue;
			
			dX = table[1][i] - table[1][index]; // Delta X
			dY = table[2][i] - table[2][index]; // Delta Y
			
			if(dX == dY && dY == 0)
				continue;
			
			r = Math.sqrt(dX*dX + dY*dY);		   // r 
			a = table[0][i] / (dX*dX + dY*dY) ;    // Acceleration 
			sine = dY / r;					   // Y component of the a
			cos = dX / r;					   // X component of the a 
			
			
			acc.addX( a * cos);
			acc.addY( a * sine);
			
		}
			
		acc.mul(G);
		return acc;
	}
	
	/*
	 checks if any ball is in contact with a, if so , then merge them into a bigger ball by removing one of them  
	 */
	static synchronized void check(ball a){
		int id = -1; 
		double p, q, r,siz;
		for(int i = 0; i < count; i++)
		{
				if (a.ballID == i)
					continue;
				p = table[1][i] - table[1][a.ballID]; // Delta X
				q = table[2][i] - table[2][a.ballID]; // Delta Y
				r = Math.sqrt(p*p + q*q);		   // r 
				
				if(r < CF*(a.getHeight() + balls[i].getHeight())/2 && table[0][i] != 0)
				{
					id = i;
					break;
				}
		}
		 	
		if(id  >= 0)
		{
			balls[id].mySuspend();
			//System.out.println("\n\n///////////////////////////  "+id+"  ////////////////////////////////\n\n");	
			a.velocity.setLocation((table[0][a.ballID]*a.velocity.getX() + table[0][id]*balls[id].velocity.getX())/(table[0][a.ballID] + table[0][id]),
						(table[0][a.ballID]*a.velocity.getY() + table[0][id]*balls[id].velocity.getY())/(table[0][a.ballID] + table[0][id]));
			table[0][id] = 0;
			table[0][a.ballID] += table[0][id];
			siz = 2*Math.pow(Math.pow(a.getHeight()/2, 3) + Math.pow(balls[id].getHeight()/2, 3),1D/3);
			a.setSize(siz,siz);
			//a.move((a.getX() + balls[id].getX())/2,(a.getY() + balls[id].getY())/2);
			//table[1][a.ballID] = (a.getX() + balls[id].getX())/2;
			//table[2][a.ballID] = (a.getY() + balls[id].getY())/2;
			canvas.remove(balls[id]);
	
		}
	}
	
	void stop(){
		this.mass = 0;
	
	}
	
	public  void  run(){
		while(true)
		{
			synchronized (this)
			{
				 while(suspendThread)
				 try{
						wait();
					} 
				 catch (InterruptedException e) {
						e.printStackTrace();
					}
			} 
			 
			check(this);
			velocity.add(getAcceleration(ballID));
			
			move(velocity.getX(), velocity.getY());
			updateTable(this);
			//System.out.println("acceleration("+ballID+"): "+ getAcceleration(ballID));
			//System.out.println("velocity("+ballID+"): "+ velocity);
			//printTable();
			pause(DEL);
			
			
		}
		
	}
	
	public static void printTable(){
		//System.out.println("ballID\tMass\tX\tY");
		for(int i = 0; i < count; i++){
			System.out.println(i+"\t"+table[0][i]+"\t"+table[1][i]+"\t"+table[2][i]);
			}
		System.out.println();
	}

}

