package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

//Data luokka, jossa hoitetaan tiedoston lukeminen ja kirjoittaminen
//sisältää avustavia systeemituloksia
public class Data {
	
	private BlockingQueue<Character> queue;
	private int low;
	private int high;
	private static boolean truth = false; //vakio, jota käytetään kirjoituksen ja lukemisen silmukan ylläpitoon
	private static boolean bothStarted = false; //vakio, jolla varmistetaan kirjoittajan käynnistyminen
	
	//data-olion konstruktori
	public Data (BlockingQueue<Character> queue) {
		this.queue =  queue; //BlockingQueueu, toimii muistina molemmille säikeille
		this.low = queue.size(); //vakio for-loopille otetaan BlockinQueuesta
		this.high = queue.remainingCapacity(); //vakio for-loopille otetaan BlockinQueuesta
	}
	
	//metodi, mikä avaa avaa tiedoston ja kirjoittaa merkit siihen
	public synchronized void write(String path) {
		
		FileOutputStream outputStream = null;
		
		try {
			System.out.println("Avataan filu kirjoitukseen");
			outputStream = new FileOutputStream(path);
			System.out.println("Avataan filu kirjoitukseen onnistui?");
			
			while(!truth) {
				
				waitWriting();
				
				for(int i = 0; i < high; i ++) {
					outputStream.write(this.queue.take());
					System.out.println("Kirjoittaja hommissa, high on " + high);
					if(this.queue.peek() == null) {
						break;
					}
				}
				notifyAll();
			}
			
		} 
		catch (IOException | InterruptedException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		finally {
				try {
					outputStream.close();
					System.out.println("Suljetaan kirjoittajassa filu");
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	//metodi,joka avaa tiedoston ja lukee merkit siitä
	public synchronized void read(String path) {
		
		FileInputStream inputStream = null;
		int r;
		
		try {
			System.out.println("Avataan filu lukuun");
			inputStream = new FileInputStream(path);
			System.out.println("Avataan filu lukuun onnistui?");
			
			while(!truth) {
				
				bothStarted();
				waitReading();
				
				for(int i = 0; i < high; i++) {
					if((r = inputStream.read()) == -1) {
						truth = true;
						break;
					}
					else {
						this.queue.put((char)r);
						System.out.println("Lukija hommissa");
					}
					
				}
				System.out.println(queue);
				notifyAll();

			}
		}
		catch(IOException | InterruptedException | IllegalArgumentException e){
			e.printStackTrace();
		}
		finally {
				try {
					inputStream.close();
					System.out.println("Suljetaan lukijassa filu");
				}
				catch(IOException e){
					e.printStackTrace();
				}
		}	
	}
	
	//lukemisen jonotus monitoriin
	public synchronized void waitReading() throws InterruptedException {
		while(low != queue.size()) {
			System.out.println("lukija jonossa");
			wait();	
		}
	}
	
	//kirjoituksen jonotus monitoriin ja varmistaa lukemisen käynnistyneen
	public synchronized void waitWriting() throws InterruptedException {
		while(high > this.queue.size()) {
			if(truth) {
				break;
			}
			if(!bothStarted) {
				bothStarted = true;
				notifyAll();
			}
			System.out.println("kirjoittaja jonossa");
			wait();
		}
	}
	
	//varmistaa lukijan odottavan kirjoituksen alkaneen
	public synchronized void bothStarted() throws InterruptedException {
		while(!bothStarted) {
			System.out.println("odotetaan kirjoittajan käynnistyvän");
			wait();
		}
	}

}
