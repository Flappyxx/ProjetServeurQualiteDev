package examples;

import java.io.*;
import java.net.*;

import serveur.Service;

// rien à ajouter ici
public class ServiceCalculatrice implements Service{
	
	private final Socket client;
	
	public ServiceCalculatrice(Socket socket) {
		client = socket;
	}

@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			boolean ok = false;

			while(!ok) {
				out.println("1: Addition ##2: Soustraction ##3: Multiplication ##4: Division");
				String res = in.readLine();
				ok = true;
				switch (res) {
					case "1":
						operationMessage(in,out,"+");
						break;
					case "2":
						operationMessage(in,out, "-");
						break;
					case "3":
						operationMessage(in,out, "*");
						break;
					case "4":
						operationMessage(in,out, "/");
						break;
					default :
						ok = false;
				}

		}
		}
		catch (IOException e) {
			//Fin du service d'inversion
		}
	}

	private void operationMessage(BufferedReader in, PrintWriter out, String operation) {
		try {
			out.println("Entrez le premier operande :");
			int operande1 = Integer.parseInt(in.readLine());
			out.println("Entrez le deuxième operande :");
			int operande2 = Integer.parseInt(in.readLine());
			out.println(calculOperation(operande1, operande2, operation) + "+++");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	private float calculOperation(int operande1, int operande2, String operation) {
		float res = 0;
		if(operation.equals("+")) {
			res = operande1 + operande2;
		}
		if(operation.equals("-")) {
			res = operande1 - operande2;
		}
		if(operation.equals("*")) {
			res = operande1 * operande2;
		}
		if(operation.equals("/")) {
			res = (float) operande1/ operande2 ;
		}
		return res;
	}

	public static String toStringue() {
		return "Calculatrice";
	}
}
