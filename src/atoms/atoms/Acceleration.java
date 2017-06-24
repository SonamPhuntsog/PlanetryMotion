package atoms;

public class Acceleration {
	private double X;
	private double Y;
	
	Acceleration ( double x, double y){
		X = x;
		Y = y;
	}
	Acceleration(){
		X = 0;
		Y = 0;
	} 
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public void addX(double a){
		X+= a;
	}
	public void addY(double a){
		Y+= a;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public void setLocation(double x,double y){
		X = x;
		Y = y;
	}
	public void add(Acceleration a){
		X += a.getX();
		Y += a.getY();
	}
	
	public void mul(double a){
		X *= a;
		Y *= a;
	}
	public String toString(){
		return "("+X+",  "+Y+ ")"; 
	}
	
}
