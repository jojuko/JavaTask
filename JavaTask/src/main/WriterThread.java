package main;

//Luokka säikeen kirjoittamiseen
public class WriterThread implements Runnable{
	
	private Data data;
	private String path;
	
	//konstruktori
	public WriterThread(Data data, String path) {
		this.data = data;
		this.path = path;
	}
	
	@Override
	public void run() {
		this.data.write(this.path);
	}

}
