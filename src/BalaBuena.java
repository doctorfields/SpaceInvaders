import java.awt.Graphics;
import java.awt.Image;

public class BalaBuena extends Bala{
	static Image img;
	static int ANCHO = 3;
	static int ALTO = 15;
	
	BalaBuena(int x, int y){
		super(x,y);
	}
	
	void movimiento(){
		y -= 5;
	}
	
	void dibuja(Graphics g){
		g.drawImage(img, x, y, ANCHO, ALTO, null);
	}
}
