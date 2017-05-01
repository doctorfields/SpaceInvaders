import java.awt.Graphics;
import java.util.Vector;

abstract public class Bala {
	int r = 10;
	int x,y;
	static Vector<Bala> array;
	
	Bala(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	abstract void movimiento();
	
	abstract void dibuja(Graphics g);
	

}
