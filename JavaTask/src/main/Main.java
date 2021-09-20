package main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*Pääohjelma, jossa pyötitetään tiedoston kopiointi toiseen.
 *Koko hommassa käytetään hyväksi java.utils ja java.io paketteja.
 */
public class Main {

	public static void main(String[] args) {
		
		//Tiedostojen polut mihin kirjoitetaan ja mistä luetaan.
		String iFile = "C:\\Users\\Joni\\eclipse-workspace\\JavaTask\\src\\data\\inputFile.txt";
		String oFile = "C:\\Users\\Joni\\eclipse-workspace\\JavaTask\\src\\data\\outputFile.txt";
		
		/*Luodaan merkkipohjanen Array. 
		*Luodaan Data olio.
		*Luodaan Checkoutolio
		*/
		BlockingQueue<Character> q = new ArrayBlockingQueue<Character>(100);
		Data d = new Data(q);
		Checkup check = new Checkup();
		
		//Luodaan säikeet lukemiseen ja kirjoitukseen (passataan tieto Data oliosta ja tiesotojen poluista).
		ReaderThread fr = new ReaderThread(d, iFile);
		WriterThread fw = new WriterThread(d, oFile);
		
		//Tarkastetaan luettavan tiedoston olemassaolo.
		if(!check.checkFile(iFile)) {
			System.out.println("Luettavaa tiedostoa ei ole olemassa");
		}
		else {
			//Käynnistetään säikeet.
			new Thread(fw).start();
			new Thread(fr).start();
		}
	
	}
}
