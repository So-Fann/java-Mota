import java.io.BufferedInputStream;
import java.io.InputStream;
import javazoom.jl.player.Player;
public class PlaySounds extends Thread {
	Player player;
	InputStream input;
	public PlaySounds(InputStream input){
		this.input=input;
		BufferedInputStream buffer=new BufferedInputStream(input);
		try {
			player=new Player(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized void run() {
		try {
			player.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
