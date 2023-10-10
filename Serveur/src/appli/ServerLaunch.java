package appli;

import java.net.MalformedURLException;

import serveur.ServeurBrette;

public class ServerLaunch {
	private final static int PORT_AMATEUR = 3000;
	private final static int PORT_PROGRAMMEUR = 4000;
	
	public static void main(String[] args) throws MalformedURLException {
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		new Thread(new ServeurBrette(PORT_AMATEUR)).start();
		new Thread(new ServeurBrette(PORT_PROGRAMMEUR)).start();


	}
}
