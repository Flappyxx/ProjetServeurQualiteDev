package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceProgrammeur {

    private Socket client;
    private URLClassLoader urlcl;


    ServiceProgrammeur(Socket socket) {
        client = socket;

    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            //programmeur doit passer son ftp
            // URLClassLoader sur ftp
            URLClassLoader urlcl = null;
            urlcl = new URLClassLoader(new URL[]{new URL("ftp://localhost:2121/")});
            while (true) {
                //Afficher ses trois options
                //1 : Installation
                //2 : Désactivation
                //3 : Activation
                //4 : Désinstallation
                //5 : Changer le ftp
                out.println("1 : Installation ##2 : Désactivation ##3 : Activation ##4 : Désintallation ##5 : Changer le ftp##Taper le numéro de l'action souhaité : ");
                String res = in.readLine();
                if(res.equals("1")){

                    String classeName = in.readLine();
                    ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
                    System.out.println(ServiceRegistry.toStringue());
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
