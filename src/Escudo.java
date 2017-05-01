import java.awt.Graphics;
import java.awt.Image;

public class Escudo {
	static int ANCHO = 72;
	static int ALTO = 54;
	static Escudo[] array;
	int x,y;
	static Image img;
	
	Escudo(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	void dibuja(Graphics g){
		g.drawImage(img,x,y, ANCHO, ALTO, null);		
	}

	static void inicializacion(){
		int tab = (Juego.ventana.ANCHO-ANCHO*3)/4;
		array = new Escudo[4];
		for(int i=0;i<4;i++){
			array[i] = new Escudo(tab*(i+1),600);
		}
	}
	
}
