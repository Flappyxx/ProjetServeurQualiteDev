package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import serveur.Service;

public class ServicePalindrome  implements Service, AutoCloseable {

	private final Socket client;
	
	public ServicePalindrome(Socket socket) {
		client = socket;
	}

@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Tapez un mot/un texte pour savoir si c'est un palindrome : ");

			String line = in.readLine();

			boolean invLine = new StringBuffer(line).reverse().equals(line);
			 
			if(invLine) {
				out.println("Le mot " + line + " est un palindrome");
			}
			else {
				out.println("Le mot " + line + " n'est pas un palindrome");
			}

			client.close();
		}
		catch (IOException e) {
			//Fin du service d'inversion
		}
	}
	
	public void close() {
		 try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static String toStringue() {
		return "Palindrome";
	}
}
