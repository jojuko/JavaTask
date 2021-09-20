package main;

//Luokka säikeen lukemiseen
public class ReaderThread implements Runnable{
	
	private Data data;
	String path;	
	
	//konstruktori
	public ReaderThread(Data data, String path) {
		this.data = data;
		this.path = path;
	}
	
	@Override
	public void run() {
		this.data.read(this.path);
	}

}
