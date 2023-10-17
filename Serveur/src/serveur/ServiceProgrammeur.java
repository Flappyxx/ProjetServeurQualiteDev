package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ServiceProgrammeur implements Runnable,AutoCloseable{

    private Socket client;
    private static URLClassLoader urlcl;
    private String username;


    ServiceProgrammeur(Socket socket) {
        client = socket;
        init();
    }

    public void init(){
        // URLClassLoader sur ftp
        try {
            if(urlcl == null)
                urlcl = new URLClassLoader(new URL[]{new URL("ftp://localhost:2121/")});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            System.out.println("Programmeur connexion");
            //Ajout de la connexion du programmeur (username,password)

            while (true) {
                //Afficher ses trois options
                //1 : Installation
                //2 : Désactivation
                //3 : Activation
                //4 : Désinstallation
                //5 : Changer le ftp
                out.println("1 : Installation ##2 : Désactivation ##3 : Activation ##4 : Désintallation ##5 : Changer le ftp##Taper le numéro de l'action souhaité : ");
                String res = in.readLine();
                switch(res){
                    case("1"):
                        installation(in,out);
                    case("2"):
                        desactivation(in,out);
                    case("3"):
                        activation(in,out);
                    case("4"):
                        desinstallation(in,out);
                    case("5"):
                        ftpChange(in,out);
                    case("q"):
                        close();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch(SocketException e){
            System.out.println("Connexion terminée");
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ftpChange(BufferedReader in, PrintWriter out) throws IOException {
        out.print("Tapez le nouveau lien ftp : ");
        String url = in.readLine();
        urlcl = new URLClassLoader(new URL[]{new URL(url)});
        //catch une mauvaise url
    }

    private void desinstallation(BufferedReader in, PrintWriter out) {
        //afficher tout même désactivé
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(ServiceRegistry.printAllServices());
            sb.append("Tapez le numéro du service à désinstaller : ");
            out.print(sb);
            int num = Integer.parseInt(in.readLine());
            ServiceRegistry.desintaller(num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activation(BufferedReader in, PrintWriter out) throws IOException, ClassNotFoundException {
        //afficher seulement désactivé
    	out.print("Tapez le nom de votre classe à activé : ");
    	String classeName = in.readLine();
    	ServiceRegistry.activationService(urlcl.loadClass(classeName).asSubclass(Service.class));
    	System.out.println(ServiceRegistry.toStringue(true));
    	
    }

    private void desactivation(BufferedReader in, PrintWriter out) throws IOException, ClassNotFoundException {
    	out.print("Tapez le nom de votre classe à desactivé : ");
    	String classeName = in.readLine();
    	ServiceRegistry.desactivationService(urlcl.loadClass(classeName).asSubclass(Service.class));
    	System.out.println(ServiceRegistry.toStringue());
        //toStringue
    }

    private void installation(BufferedReader in,PrintWriter out) throws IOException, ClassNotFoundException {
        out.print("Tapez le nom de votre classe : ");
        String classeName = in.readLine();
        //Vérification de nom de package == user
        ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
        System.out.println(ServiceRegistry.toStringue());
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
