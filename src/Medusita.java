import java.awt.Graphics;
import java.awt.Image;


public class Medusita extends Monstruo {
	static Image imagenEstatico;
	static Image imagenDinamico;
	static double Rate;

	Medusita(int x, int y){
		super(x,y);
		ShootRate = Rate;
	}
	
	void dibuja(Graphics g){
	    if(moving) g.drawImage(imagenDinamico,x,y, ANCHO, ALTO, null);
	    else g.drawImage(imagenEstatico,x,y, ANCHO, ALTO, null);
	}
}
