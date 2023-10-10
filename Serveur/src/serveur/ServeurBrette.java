package serveur;


import java.io.*;
import java.net.*;


public class ServeurBrette implements Runnable,AutoCloseable {
	private ServerSocket listen_socket;
	private final static int PORT_AMATEUR = 3000;
	private final static int PORT_PROGRAMMEUR = 4000;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBrette(int port) {
		try {
			listen_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		try {
			while(true){
				if(listen_socket.getLocalPort() == PORT_AMATEUR)
					new Thread(new ServiceAmateur(listen_socket.accept())).start();
				if(listen_socket.getLocalPort() == PORT_PROGRAMMEUR)
					new Thread(new ServiceAmateur(listen_socket.accept())).start();
			}
		}
		catch (IOException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	 // restituer les ressources --> finalize
	public void close() {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}

}
