import java.awt.Graphics;
import java.awt.Image;

public class Nave {
	static int ANCHO=39,ALTO=24;
	int x,y;
	static int speed = 2;
	int velocidad = 0;
	int margen = 20;
	boolean shooter = true;
	
	static Image img;
	
	Nave(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	void dibuja(Graphics g){
		g.drawImage(img,x,y, ANCHO, ALTO, null);
	}
	
	void movimiento(){
		if(x<margen && velocidad < 0){
			return; // Choque contra la pared izquierda
		}
		if(x>Juego.ventana.ANCHO-ANCHO-margen && velocidad > 0){
			return; // Choque contra la pared derecha
		}
		else{
			x += velocidad;
		}
	}
	
	void dispara(){
		if (this.shooter){
			Bala.array.addElement(new BalaBuena(x+ANCHO/2,y));
			this.shooter = false;
			Juego.sonido(3000,50);
		}
	}
}
