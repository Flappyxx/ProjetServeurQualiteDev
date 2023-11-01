package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import serveur.Service;

public class ServiceMessagerie implements Service, AutoCloseable{
	private final Socket client;
	private static Map<String, ArrayList<String>> messageMap;

	static {
		messageMap = new HashMap<>();
	}
	
	public ServiceMessagerie(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			while(true) {
				out.println("1: Consultation des messages ## 2: Envoie d'un message");
				String res = in.readLine();
				switch (res) {
					case "1":
						consultationMessage(in,out);
						break;
					case "2":
						envoieMessage(in,out);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void envoieMessage(BufferedReader in, PrintWriter out) throws IOException {
		out.println("Entrez le pseudo du destinataire :");
		String nomDestinataire = in.readLine();
		out.println("Entrez votre message : ");
		String message = in.readLine();
		addMessage(nomDestinataire,message);
	}

	private void consultationMessage(BufferedReader in, PrintWriter out) throws IOException {
		out.println("Entrez votre pseudo :");
		String pseudo = in.readLine();
		out.println(getMessages(pseudo) + "Faites une entrée clavier pour revenir à l'accueil");
		in.readLine();
	}

	@Override
	public void close() throws Exception {
		client.close();
	}

	public void addMessage(String login, String message) {
		if(this.messageMap.get(login) != null) {
			this.messageMap.get(login).add(message);
		}
		else {
			ArrayList<String> mes = new ArrayList<>();
			mes.add(message);
			this.messageMap.put(login, mes);
		}
	}

	public String getMessages(String login) {

		if(this.messageMap.get(login) == null){
			return "Aucun message";
		}
		StringBuilder str = new StringBuilder("Liste de messages : ##");
		for(int i = 0; i < this.messageMap.get(login).size(); i++) {
			str.append(this.messageMap.get(login).get(i));
			str.append("##");
		}
		return str.toString();
	}

	public static String toStringue() {
		return "Messagerie";
	}


}
