import java.awt.Graphics;

abstract public class Monstruo {
	int ANCHO = 36; int ALTO = 24;
	int x,y;
	static int direccion = 1;
	boolean isAlive = true;
	boolean shooter = false;
	double ShootRate;
	static boolean moving = false;
	static double Periodo;
	static int vivos;
	
	Monstruo(int x, int y){
		this.x = x;
		this.y = y;
	}


	static int filas = 5;
	static int columnas = 11;
	static Monstruo[][] array;
	static void inicializacion(int dificultad){
		// Subirle la frecuencia de tiro
		if(dificultad == 0){
			Medusita.Rate = 0.0003;
			Cangrejito.Rate = 0.0006;
			Calamarcito.Rate = 0.0012;	
		}
		else{
			Medusita.Rate *= 2;
			Cangrejito.Rate *= 2;
			Calamarcito.Rate *= 2;
		}
		
		// Crear la array
		int tabH = 150;
		int tabV = 170+20*dificultad;
		array = new Monstruo[filas][columnas];
		for(int i=0;i<filas;i++){
			for(int j=0;j<columnas;j++){
				if (i==0) array[i][j] = new Calamarcito(tabH+48*j,tabV+54*i);
				if (i==1 || i==2) array[i][j] = new Cangrejito(tabH+48*j,tabV+54*i);
				if (i==3 || i==4) array[i][j] = new Medusita(tabH+48*j,tabV+54*i);
			}
		}
		
		// Hacer disparadores a los que no tienen nada delante
		for(int j=0;j<columnas;j++){
			array[filas-1][j].shooter = true;
		}	
		
		// Numero de monstruos vivos
		vivos = filas*columnas;
		
		// Ritmo
		if(dificultad < 10) Periodo = 150-20*dificultad;
	}
	
	static void movimiento(){		
			moving = !moving;
			if(checkCambioDireccion()){
				for(int i=0;i<Monstruo.filas;i++){
					for(int j=0;j<Monstruo.columnas;j++){
						Monstruo.array[i][j].y += 10;
					}
				}
				Monstruo.direccion *= -1;
			}
			else{
				for(int i=0;i<Monstruo.filas;i++){
					for(int j=0;j<Monstruo.columnas;j++){
						Monstruo.array[i][j].x += direccion*3;
					}
				}
			}
	}
	
	static boolean checkCambioDireccion(){
		int tab = 20;
		for(int i=0;i<Monstruo.filas;i++){
			for(int j=0;j<Monstruo.columnas;j++){
				Monstruo monstruito = Monstruo.array[i][j];
				if(monstruito.isAlive &&
					( (monstruito.x > Juego.ventana.ANCHO-tab-monstruito.ANCHO && Monstruo.direccion > 0) ||
							(monstruito.x < tab && Monstruo.direccion < 0) ) ){
					return true;
				}
			}
		}
		return false;
	}

	abstract void dibuja(Graphics g);
	
	void dispara(){
		if(Math.random()<ShootRate && shooter == true && isAlive == true){
			Bala nuevaBala;
			if(Math.random()<0.7) nuevaBala = new Cruz(x + ANCHO/2, y + ALTO);
			else nuevaBala = new Onda(x + ANCHO/2, y + ALTO);
			Bala.array.addElement(nuevaBala);
		}
	}
	
	public static boolean MonstruosGanan(){
		for(int i=0;i<filas;i++){
			for(int j=0;j<columnas;j++){
				Monstruo monstruo = array[i][j];			
				if (monstruo.isAlive && monstruo.y > 580-24) return true;
			}
		}
		return false;
	}
}
