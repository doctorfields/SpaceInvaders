import java.awt.Graphics;
import java.awt.Image;

public class Onda extends BalaMala{
	static Image img;
	
	Onda(int x, int y){
		super(x,y);
	}
	
	void movimiento(){
		y += 3;
	}
	
	void dibuja(Graphics g){
		g.drawImage(img, x, y, ANCHO, ALTO, null);
	}
}
