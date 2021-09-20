package main;

import java.io.File;

/*Luokassa vain metodi tiedoston olemassaolon tarkastukseen.
 * Tämä olisi varmasti ollut fiksumpaa tehdä varmaa tuolla datassa.
 * Lähinnä en tiedä onko tämä tehtävää ajatellen ns. välttämätön toimenpide.
 */
public class Checkup {
	
	public boolean checkFile (String path) {
		File file = new File(path);
		if(file.exists()) {
			return true;
		}
		return false;
	}
}
