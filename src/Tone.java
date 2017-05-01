import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Tone extends Thread {
    static int SAMPLE_RATE = 128*1024; // ~16KHz
    int f; int d;
    boolean melodia;
    
    Tone(int f, int d){   	
    	this.f = f;
    	this.d = d;
    	if(f == 0 && d == 0) melodia = true;
    	else melodia = false;
    }
    
    public void run(){
    	if(melodia){
    		while(Menu.menuON){
    			melodia();
    		}
    	}
    	else{
	    	try {
				tone(f,d);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public static void melodia(){
		try {
			tone(450,500); if(!Menu.menuON) return;
			tone(400,250); if(!Menu.menuON) return;
			tone(350,500); if(!Menu.menuON) return;
			tone(300,250); if(!Menu.menuON) return;
			tone(450,500); if(!Menu.menuON) return;
			tone(400,250); if(!Menu.menuON) return;
			tone(350,500); if(!Menu.menuON) return;
			tone(300,250); if(!Menu.menuON) return;
			tone(450,500); if(!Menu.menuON) return;
			tone(400,500); if(!Menu.menuON) return;
			tone(350,500); if(!Menu.menuON) return;
			tone(300,500); if(!Menu.menuON) return;
			tone(500,500); if(!Menu.menuON) return;
			tone(600,500); if(!Menu.menuON) return;
			tone(700,500); if(!Menu.menuON) return;
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void tone(int frequency, int duration) throws LineUnavailableException{
        byte[] buf = new byte[(duration*SAMPLE_RATE)/1000];
        final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, SAMPLE_RATE);
        line.start();    	
    	for(int i = 0;i < buf.length;i++){
    		double angle = 2.0*Math.PI*frequency*i/SAMPLE_RATE;
    		buf[i] = (byte) (Math.sin(angle)*1000);
    	}
    	line.write(buf, 0, buf.length);
    	line.drain();
    	line.close();	
    }
}