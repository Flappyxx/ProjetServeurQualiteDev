package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import serveur.Service;

public class ServiceMessagerie implements Service, AutoCloseable{
private final Socket client;
	
	public ServiceMessagerie(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println("Entrez le pseudo du destinataire :");
			String nomDestinataire = in.readLine();
			out.println("Entrez votre message : ");
			String message = in.readLine();
			
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		client.close();
	}
	
	public static String toStringue() {
		return "Messagerie";
	}


}
