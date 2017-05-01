import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

public class Menu extends Thread implements KeyListener{
	static Juego Partida;
	static Highscores Records;
	static Ventana ventana;
	static Graphics graphics;
	static Image logo;
	static Image flecha;
	static boolean HighscoresSelected;
	static boolean menuON;
	static Tone melodia;

	Menu(Ventana V){
		V.addKeyListener(this);
		ventana = V;
		graphics = V.GRAPHICS;
	}
	
	public void run() {
		inicializacion();
		while(menuON){
			/* Esto primero lo pongo para que no parpadee */
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dibuja();
			ventana.repaint();
		}
	}
	
	void dibuja(){
		Font font = graphics.getFont();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0,0,ventana.ANCHO,ventana.ALTO);
		int logo_ancho = 500; int logo_alto = 300;
		graphics.drawImage(logo,(ventana.ANCHO-logo_ancho)/2,130, logo_ancho, logo_alto, null);
		graphics.setColor(Color.YELLOW);
		font = font.deriveFont(Font.PLAIN,30);
		graphics.setFont(font);
		graphics.drawString("Play Game", 310, 550);
		graphics.drawString("Highscores", 295, 650);
		font = font.deriveFont(Font.PLAIN,20);
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		graphics.drawString("Martin Campos - NIU: 1332484", 365, 780);
		if(HighscoresSelected){
			graphics.drawImage(flecha, 240, 628, 30, 25, null);
		}
		else{
			graphics.drawImage(flecha, 250, 528, 30, 25, null);
		}
	}
	
	public void inicializacion(){
		menuON = true;
		HighscoresSelected = false;
		melodia = new Tone(0,0);
		melodia.start();
		cargarImagenes();
		cargarFuente();
	}
	
	void cargarFuente(){
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("font/space_invaders.ttf"));
			font = font.deriveFont(Font.PLAIN,20);
			graphics.setFont(font);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	void cargarImagenes(){
		try {
			logo = ImageIO.read(new File("img/logo.png"));
			flecha = ImageIO.read(new File("img/flecha.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()){
			case 38:
				HighscoresSelected = false;
				try {
					Tone.tone(100, 50) ;
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 40:
				HighscoresSelected = true;
				try {
					Tone.tone(100, 50);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 10:
				try {
					Tone.tone(1000, 50);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(HighscoresSelected){
					Records = new Highscores(ventana);
					Records.start();
					menuON = false;
					ventana.removeKeyListener(this);
				}
				else{
					Partida = new Juego(ventana);
					Partida.start();
					menuON = false;
					ventana.removeKeyListener(this);
				}
				break;
			case 27: System.exit(0); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}