package serveur;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


class ServiceAmateur implements Runnable {
	
	private Socket client;
	
	ServiceAmateur(Socket socket) {
		client = socket;
	}

	public void run() {
		System.out.println("Amateur connexion");
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			while(true) {
				out.println(ServiceRegistry.toStringue() + "##Tapez le numéro de service désiré :");

				try {
				int choix = Integer.parseInt(in.readLine());

				Service service = ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client);
				// instancier le service numéro "choix" en lui passant la socket "client"

				service.getClass().getMethod("run").invoke(service);}
				catch(NullPointerException | NumberFormatException e){
					out.println("entrée invalide+++");
				}
			}
				
			}

		catch (SocketException e){
			System.out.println("Amateur déconnexion");
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		}

		try {client.close();} catch (IOException e2) {}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

}
