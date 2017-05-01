import java.awt.Graphics;
import java.awt.Image;

public class Cruz extends BalaMala{
	static Image img;
	
	Cruz(int x, int y){
		super(x,y);
	}
	
	void movimiento(){
		y += 2;
	}
	
	void dibuja(Graphics g){
		g.drawImage(img, x, y, ANCHO, ALTO, null);
	}
}
