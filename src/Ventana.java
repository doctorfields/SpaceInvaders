import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Ventana extends Frame implements WindowListener{
	/**Esto me lo puso Eclipse el solito para sacar un warning.
	 * Aca me imagino que vendria un poco la explicacion del juego.
	 */
	private static final long serialVersionUID = 1L;
	int ANCHO = 800, ALTO = 800;
	Image IMAGE;
	Graphics GRAPHICS;
	Menu menu;
	Juego Partida;
	
	public static void main(String[] args) {
		new Ventana();
	}
	
	Ventana(){
		super("Space Invaders");
		setSize(ANCHO,ALTO);
		setVisible(true);
		addWindowListener(this);
		IMAGE = this.createImage(ANCHO,ALTO);
		GRAPHICS = IMAGE.getGraphics();
		menu = new Menu(this);
		menu.start();
	}
	
	// Estas dos funciones no las tengo del todo claras
	// pero creo que son las que le dan fluidez
	public void paint(Graphics g){
		g.drawImage(IMAGE, 0, 0, null);
	}
	
	public void update(Graphics g){
		paint(g);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
