import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sound.sampled.LineUnavailableException;


public class Highscores extends Thread implements KeyListener{
	static Ventana ventana;
	static Graphics graphics;
	static String[] jugadores;
	static int[] puntuaciones;
	static boolean on;

	Highscores(Ventana V){
		V.addKeyListener(this);
		ventana = V;
		graphics = V.GRAPHICS;
	}
	
	public void run() {
		inicializacion();
		while(on){
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
		graphics.setColor(Color.GREEN);
		font = font.deriveFont(Font.PLAIN,30);
		graphics.setFont(font);
		graphics.drawString("***  TOP 10 HIGHSCORES  ***",100,130);
		int tabH = 150;
		int tabV = 230;
		graphics.setColor(Color.WHITE);
		for(int i = 0; i<10; i++){
			String ScoreString = puntuaciones[i]+"";
			FontMetrics fontmetrics = graphics.getFontMetrics();
			graphics.drawString(jugadores[i],tabH,tabV+50*i);
			graphics.drawString(ScoreString,ventana.ANCHO-tabH-fontmetrics.stringWidth(ScoreString),tabV+50*i);
		}
	}
	
	public void inicializacion(){
		on = true;
		cargarFuente();
		try {
			cargarRecords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void cargarRecords() throws IOException{
		FileInputStream input = null;
		try {
			input = new FileInputStream("highscores/highscores.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		//r.read(); // sacando el primer ? que no se xqe genera el archivo
		
		jugadores = new String[10];
		puntuaciones = new int[10];
		String[] aux;
		String line;
		for(int i = 0; i<10; i++){
			line = r.readLine();
			aux = line.split(" ");
			jugadores[i] = aux[0];
			puntuaciones[i] = Integer.parseInt(aux[1]);
		}
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
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()){
			case 27:
				ventana.removeKeyListener(this);
				try {
					Tone.tone(100,50);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				on = false;
				Menu menu;
				menu = new Menu(ventana);
				menu.start();
				break;
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