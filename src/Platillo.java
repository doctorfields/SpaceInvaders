import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

public class Platillo {
	static Image img;
	static int ANCHO = 48; static int ALTO = 21;
	int direccion;
	int VALOR;
	int x,y;
	double X;
	double velocidad;
	static Vector<Platillo> array;
	
	Platillo(){
		y = 135;
		if(Math.random()<0.5){
			x = -ANCHO;
			X = (double) x;
			direccion = 1;
		}
		else{
			x = Juego.ventana.ANCHO;
			X = (double) x;
			direccion = -1;
		}
		velocidad = Math.random();
		if(velocidad < 0.5) velocidad += 0.5;
		VALOR = (int)(velocidad * 10) * 50; 
	}
	
	void dibuja(Graphics g){
		g.drawImage(img, x, y, ANCHO, ALTO,null);
	}
	
	void movimiento(){
		X += direccion*velocidad;
		x = (int) X;
	}
}
