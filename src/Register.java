import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.sound.sampled.LineUnavailableException;


public class Register extends Thread implements KeyListener{
	static Ventana ventana;
	static Graphics graphics;
	static String[] jugadores;
	static int[] puntuaciones;
	static boolean on;
	char[] name;
	int cursor;
	int Score;

	Register(Ventana V,int Score){
		V.addKeyListener(this);
		ventana = V;
		graphics = V.GRAPHICS;
		this.Score = Score;
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
		graphics.drawString("Congratulations!",220,130);
		graphics.drawString("You are in the TOP 10",170,180);
		graphics.setColor(Color.WHITE);
		graphics.drawString("Enter your name:",220,250);
		font = font.deriveFont(Font.PLAIN,50);
		graphics.setFont(font);
		for(int i = 0; i<cursor; i++){
			graphics.drawString(name[i]+"",120+60*i,500);
		}
		if(cursor<10) graphics.fillRect(120+60*cursor, 500, 55, 7);

	}
	
	public void inicializacion(){
		on = true;
		cursor = 0;
		name = new char[10];
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
	
	void registrar(String name, int puntuacion){
	    PrintWriter writer;
		try {
			writer = new PrintWriter("highscores/highscores.txt", "UTF-8");
		    for(int i = 0; i<10; i++){
		    	if(puntuaciones[i]>=puntuacion) writer.println(jugadores[i]+" "+puntuaciones[i]);
		    	else{
		    		writer.println(name+" "+puntuacion);
		    		for(int j = i; j<10-1;j++) writer.println(jugadores[j]+" "+puntuaciones[j]);
		    		break;
		    	}
		    }
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==8){
			if(cursor != 0) cursor--;
		}
		else{
			if(arg0.getKeyCode()==10){
				String Name = "";
				for(int i = 0; i<cursor; i++){
					Name += name[i];
				}
				Name = Name.toUpperCase(Locale.ENGLISH);
				System.out.println(Name);
				registrar(Name,Score);
				on = false;
				Highscores Records = new Highscores(ventana);
				Records.start();
				ventana.removeKeyListener(this);
			}
			else{
				if(cursor == 10){
					try {
						Tone.tone(100, 50);
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					if(arg0.getKeyCode() != 32){
						char tecla = arg0.getKeyChar();
						name[cursor] = tecla;
						cursor += 1;
					}
				}
			}
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