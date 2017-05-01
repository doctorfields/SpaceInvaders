import java.awt.Graphics;
import java.awt.Image;


public class Calamarcito extends Monstruo {
	static Image imagenEstatico;
	static Image imagenDinamico;
	static double Rate;

	Calamarcito(int x, int y){
		super(x,y);
		ShootRate = Rate;
	}
	
	void dibuja(Graphics g){
	    if(moving) g.drawImage(imagenDinamico,x,y, ANCHO, ALTO, null);
	    else g.drawImage(imagenEstatico,x,y, ANCHO, ALTO, null);
	}
}
