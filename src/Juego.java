import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.imageio.ImageIO;

public class Juego extends Thread implements KeyListener{
	static Ventana ventana;
	static Graphics graphics;
	Nave Jugador;
	int nivel = 0;
	boolean vivo;
	
	
	static GraphicsEnvironment ge;
	static boolean ver_nave;
	static boolean PAUSA;
	static int STEP_TIME = 5;
	int PUNTUACION;
	int timer;
	int vidas;
	int cont;
	String[] jugadores;
	int[] puntuaciones;
	
	
	Juego(Ventana V){
		V.addKeyListener(this);
		Juego.ventana = V;
		graphics = V.GRAPHICS;
	}
	
	public void run() {
		inicializacion();
		try {
			while(vivo){
				if(PAUSA){
					Font font = graphics.getFont();
					font = font.deriveFont(Font.PLAIN,40);
					graphics.setFont(font);
					// Parpadeo
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0,100,ventana.ANCHO,500);
					ventana.repaint();
					sleep(100);
					graphics.setColor(Color.WHITE);
					graphics.drawString("PAUSED", 325, 350);
					ventana.repaint();
					sleep(100);
					font = font.deriveFont(Font.PLAIN,20);
					graphics.setFont(font);
				}
				else{
					long StartTime = System.nanoTime();
					calcularMovimientos();
					interacciones();
					disparanEnemigos();
					dibuja();
					checkMuerte();
					checkVictoria();
					ventana.repaint();
					timer += 1;
					long EndTime = System.nanoTime();
					// Invento raro para hacer cada step igual de largo
					if (STEP_TIME > (EndTime-StartTime)/1000000) sleep(STEP_TIME-(EndTime-StartTime)/1000000);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void inicializacion(){
		vivo = true;
		PAUSA = false;
		ver_nave = true;
		PUNTUACION = 0;
		vidas = 3;
		timer = 0;
		cont = 0;
		try {
			cargarRecords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarImagenes();
	    cargarFuente();
		Bala.array = new Vector<Bala>(0);
		Platillo.array = new Vector<Platillo>(0);
		Jugador = new Nave(500,700);
		Monstruo.inicializacion(nivel);
		Escudo.inicializacion();
	}
	
	void disparanEnemigos(){
		for(int i = 0; i < Monstruo.filas; i++){
			for(int j = 0; j < Monstruo.columnas; j++){
				Monstruo.array[i][j].dispara();
			}
		}  
	}
	
	void checkVictoria(){
		if(Monstruo.vivos == 0){
			nivel += 1;
			vidas += 1;
			PUNTUACION += 1000;
			try{
				sonido(800,100); sleep(100);
				sonido(900,100); sleep(100);
				sonido(1000,100); sleep(100);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
			
			Monstruo.inicializacion(nivel);
		}
	}
	
	void checkMuerte(){
		if(vidas == 0 || Monstruo.MonstruosGanan()){
			vivo = false;
			Font font = graphics.getFont();
			font = font.deriveFont(Font.PLAIN,50);
			graphics.setFont(font);
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0,100,ventana.ANCHO,560);
			graphics.setColor(Color.RED);
			graphics.drawString("Game Over",240,350);
			ventana.repaint();
			try {
				Tone.tone(100,500);
				Tone.tone(90,500);
				Tone.tone(80,500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ventana.removeKeyListener(this);
			if(PUNTUACION > puntuaciones[9]){
				Register registro;
				registro = new Register(ventana,PUNTUACION);
				registro.start();
			}
			else{
				Menu menu;
				menu = new Menu(ventana);
				menu.start();
			}
		}		
		for(int i = 0; i < Bala.array.size(); i++){
			Bala bala = Bala.array.get(i); 
			if (bala.x > Jugador.x && bala.x < Jugador.x+Nave.ANCHO && bala.y > Jugador.y && bala.y < Jugador.y + Nave.ALTO){
				vidas -= 1;
				// Parpadeo
				try {
					ver_nave = false; dibuja(); ventana.repaint(); sonido(300,100); sleep(100);
					ver_nave = true; dibuja(); ventana.repaint(); sonido(400,100); sleep(100);
					ver_nave = false; dibuja(); ventana.repaint(); sonido(300,100); sleep(100);
					ver_nave = true; dibuja(); ventana.repaint(); sonido(400,100); sleep(100);
					ver_nave = false; dibuja(); ventana.repaint(); sonido(300,100); sleep(100);
					ver_nave = true; dibuja(); ventana.repaint(); sonido(400,100); sleep(100);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Borrar todas las balas
				Bala.array = new Vector<Bala>(0);
				Jugador.shooter = true;
				break;
			}
		}
	}
	
	void calcularMovimientos(){
		// Movimiento monstruos
		if(timer == (int) Monstruo.Periodo){
			cont++;
			timer = 0;
			Monstruo.movimiento();
			sonido(50*(cont%4)+100,100);
			if(Monstruo.Periodo > 10) Monstruo.Periodo -= 5*Monstruo.Periodo*Monstruo.Periodo/(200*200);
		}
		
		// Movimiento jugador
		Jugador.movimiento();
		
		// Movimiento balas
		for(int i=0;i<Bala.array.size();i++){
			Bala.array.get(i).movimiento();
		}
		
		// Movimiento platillo
		for(int i=0;i<Platillo.array.size();i++){
			Platillo ovni = Platillo.array.get(i);
			ovni.movimiento();
			if(ovni.x>ventana.ANCHO || ovni.x<0-Platillo.ANCHO){
				Platillo.array.removeElementAt(i);
				i -= 1;
			}
		}

	}
	
	void interacciones(){
		for(int k=0;k<Bala.array.size();k++){
			Bala bala = Bala.array.get(k);
			
			// Bala que acierta
			for(int i=0;i<Monstruo.filas;i++){
				for(int j=0;j<Monstruo.columnas;j++){
					Monstruo enemigo = Monstruo.array[i][j];
					if(bala instanceof BalaBuena
							&& enemigo.isAlive
							&& bala.x > enemigo.x && bala.x < enemigo.x+enemigo.ANCHO
							&& bala.y > enemigo.y && bala.y < enemigo.y+enemigo.ALTO){
						enemigo.isAlive = false;
						sonido(20,50);						
						Monstruo.vivos -= 1;
						if(i!=0) Monstruo.array[i-1][j].shooter = true;
						if(enemigo instanceof Medusita) PUNTUACION += 10;
						if(enemigo instanceof Cangrejito)PUNTUACION += 20;
						if(enemigo instanceof Calamarcito) PUNTUACION += 40;
						if (bala instanceof BalaBuena) Jugador.shooter = true;
						Bala.array.removeElementAt(k);
						k -= 1;
					}
				}
			}			
			
			// Bala perdida
			int tabDOWN = 60;
			int tabUP = 120;
			if(bala.y<tabUP || bala.y > Juego.ventana.ALTO-tabDOWN){
				if (bala instanceof BalaBuena) Jugador.shooter = true;
				Bala.array.removeElementAt(k);
				k -= 1;
			}
			
			// Bala contra escudo
			for(int i=0;i<Escudo.array.length;i++){
				Escudo escudo = Escudo.array[i];
				if(bala.x > escudo.x && bala.x < escudo.x+Escudo.ANCHO && bala.y > escudo.y && bala.y < escudo.y+Escudo.ALTO){
					if (bala instanceof BalaBuena) Jugador.shooter = true;
					Bala.array.removeElementAt(k);
					k -= 1;
				}
			}
			
			// Bala contra platillo
			for(int i=0;i<Platillo.array.size();i++){
				Platillo ovni = Platillo.array.get(i);
				if(bala instanceof BalaBuena
						&& bala.x > ovni.x && bala.x < ovni.x+Platillo.ANCHO 
						&& bala.y > ovni.y && bala.y < ovni.y+Platillo.ALTO){
					Jugador.shooter = true;
					PUNTUACION += ovni.VALOR;
					Bala.array.removeElementAt(k);
					k -= 1;
					Platillo.array.removeElementAt(i);
					i -= 1;
					// Escribir los puntos
					try {
						dibuja(); graphics.setColor(Color.RED); graphics.drawString(""+ovni.VALOR,ovni.x,ovni.y);
						ventana.repaint();
						sonido(300,50);
						sleep(100);
						dibuja(); graphics.setColor(Color.WHITE); graphics.drawString(""+ovni.VALOR,ovni.x,ovni.y);
						ventana.repaint();
						sonido(300,50);
						sleep(100);
						dibuja(); graphics.setColor(Color.RED); graphics.drawString(""+ovni.VALOR,ovni.x,ovni.y);
						ventana.repaint();
						sonido(300,50);
						sleep(100);
						dibuja(); graphics.setColor(Color.WHITE); graphics.drawString(""+ovni.VALOR,ovni.x,ovni.y);
						ventana.repaint();
						sonido(300,50);
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		// Ovnis
		double probabilidadOvni = 0.0001;
		if(Math.random()<probabilidadOvni && Platillo.array.size() == 0) Platillo.array.addElement(new Platillo());
	}
	
	void dibuja(){
		Font font = graphics.getFont();
		font = font.deriveFont(Font.PLAIN,28);
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0,0,ventana.ANCHO,ventana.ALTO);
		if (ver_nave) Jugador.dibuja(graphics);
		for(int i=0;i<Monstruo.filas;i++){
			for(int j=0;j<Monstruo.columnas;j++){
				if(Monstruo.array[i][j].isAlive){
					Monstruo.array[i][j].dibuja(graphics);
				}	
			}
		}
		for(int i=0;i<Bala.array.size();i++){
			Bala.array.get(i).dibuja(graphics);
		}
		for(int i=0;i<Escudo.array.length;i++){
			Escudo.array[i].dibuja(graphics);
		}
		for(int i=0;i<Platillo.array.size();i++){
			Platillo.array.get(i).dibuja(graphics);
		}
		graphics.setColor(Color.WHITE);
		graphics.drawString("Score: ", 60, 100);
		graphics.drawString("Lives: ", 370, 100);
		graphics.setColor(Color.GREEN);
		graphics.drawString(""+PUNTUACION, 200, 100);
		if(vidas<5){
			for(int i=0;i<vidas;i++){
				graphics.drawImage(Nave.img, 510+50*i, 78, Nave.ANCHO, Nave.ALTO, null);
			}
		}
		else{
			graphics.drawString(vidas+"", 500, 100);
			graphics.drawImage(Nave.img, 535, 78, Nave.ANCHO, Nave.ALTO, null);
		}
		graphics.drawLine(0,ventana.ALTO-60,ventana.ANCHO,ventana.ALTO-60);
		graphics.setColor(Color.WHITE);
		drawDashedLine(graphics,0,ventana.ANCHO,580,30,50);
	}

	private void drawDashedLine(Graphics g,int x1,int x2,int y,int dash,int space){
		int l = x2-x1;
		int n = l/(dash+space);
		for(int i = 0; i<n+1; i++){
			graphics.drawLine((dash+space)*i-10, y,(dash+space)*i-10+dash, y);
		}
	}
	
	void cargarFuente(){
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("font/space_invaders.ttf"));
			font = font.deriveFont(Font.PLAIN,28);
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
			Nave.img = ImageIO.read(new File("img/nave.png"));
			Platillo.img = ImageIO.read(new File("img/platillo.png"));
			BalaBuena.img = ImageIO.read(new File("img/balabuena.png"));
			Cruz.img = ImageIO.read(new File("img/balamala1.png"));
			Onda.img = ImageIO.read(new File("img/balamala2.png"));
			Medusita.imagenEstatico = ImageIO.read(new File("img/medusita1.png"));
			Medusita.imagenDinamico = ImageIO.read(new File("img/medusita2.png"));
			Cangrejito.imagenEstatico = ImageIO.read(new File("img/cangrejito1.png"));
			Cangrejito.imagenDinamico = ImageIO.read(new File("img/cangrejito2.png"));
			Calamarcito.imagenEstatico = ImageIO.read(new File("img/calamarcito1.png"));
			Calamarcito.imagenDinamico = ImageIO.read(new File("img/calamarcito2.png"));
			Escudo.img = ImageIO.read(new File("img/shield.png"));
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
	
	public static void sonido(int f,int d){
		Tone tono = new Tone(f,d);
		tono.start();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()){
			case 37: Jugador.velocidad = -Nave.speed; break;
			case 39: Jugador.velocidad = Nave.speed; break;
			case 32: Jugador.dispara(); break;
			case 40: Jugador.dispara(); break;
			case 10: PAUSA = !PAUSA; sonido(100,300); break;
			case 80: PAUSA = !PAUSA; sonido(100,300); break;
			case 27:
				vivo = false;
				ventana.removeKeyListener(this);
				Menu menu;
				menu = new Menu(ventana);
				menu.start();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getKeyCode()){
			case 37: Jugador.velocidad = 0; break;
			case 39: Jugador.velocidad = 0; break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
