package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

            while(true){ //vérfie que le mdp est égal au nom d'utilisateur en majuscule
                out.println("username :");
                username = in.readLine();
                out.println("password :");
                String password = in.readLine();
                if(username.toUpperCase().equals(password)){
                    break;
                }
            }

            while (true) {
                //1 : Installation
                //2 : Désactivation
                //3 : Activation
                //4 : Désinstallation
                //5 : Changer le ftp
                out.println("1 : Installation ##2 : Désactivation ##3 : Activation ##4 : Désintallation ##5 : Changer le ftp##Taper le numéro de l'action souhaité : ");
                String res = in.readLine();
                System.out.println(res);
                switch(res){
                    case("1"):
                        installation(in,out);
                        break;
                    case("2"):
                        desactivation(in,out);
                        break;
                    case("3"):
                        activation(in,out);
                        break;
                    case("4"):
                        desinstallation(in,out);
                        break;
                    case("5"):
                        ftpChange(in,out);
                        break;
                    case("q"):
                        close();
                        break;
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
        out.println("Tapez le nouveau lien ftp : ");
        try {
        String url = in.readLine();
        urlcl = new URLClassLoader(new URL[]{new URL(url)});
        }
        catch(Exception e) {
        	e.printStackTrace();   	
        }
        //catch une mauvaise url
    }

    private void desinstallation(BufferedReader in, PrintWriter out) throws IOException{
        //afficher tout même désactivé
        StringBuilder sb = new StringBuilder();
        sb.append(ServiceRegistry.printAllServices());
        sb.append("Tapez le numéro du service à désinstaller : ");
        out.println(sb);
        try {
        int num = Integer.parseInt(in.readLine());
        if(num > 0 && num < ServiceRegistry.getServiceClassesSize()) {
        	ServiceRegistry.desintaller(num);	
        }
        }
        catch(NumberFormatException e) {
        	e.printStackTrace();
        }
    }

    private void activation(BufferedReader in, PrintWriter out) throws IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(ServiceRegistry.toStringue(false));
        sb.append("Tapez le nom de votre classe à activé : ");
    	out.println(sb);
    	try {
    	int numClasse = Integer.parseInt(in.readLine());
    	if(numClasse > 0 && numClasse < ServiceRegistry.getServiceClassesSize()) {
    		ServiceRegistry.activationService(numClasse);
    	}
    	}
        catch(NumberFormatException e) {
        	e.printStackTrace();
        }
    	
    }

    private void desactivation(BufferedReader in, PrintWriter out) throws IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(ServiceRegistry.toStringue(true));
        sb.append("Tapez le nom de votre classe à desactivé : ");
    	out.println(sb);
    	try {
        int numClasse = Integer.parseInt(in.readLine());
        if(numClasse > 0 && numClasse < ServiceRegistry.getServiceClassesSize()) {
        	ServiceRegistry.desactivationService(numClasse);	
        }
    	}
    	catch(NumberFormatException e) {
    		
    	}
    }

    private void installation(BufferedReader in,PrintWriter out) throws IOException, ClassNotFoundException {
        out.println("Tapez le nom de votre classe : ");
        String classeName = in.readLine();
        Class<?> serviceClass = urlcl.loadClass(classeName).asSubclass(Service.class);
        //Vérification de nom de package == user
        verificationClass(serviceClass);


        ServiceRegistry.addService(serviceClass);
    }

    private boolean verificationClass(Class<?> serviceClass) {
        if(!username.equals(serviceClass.getPackageName()))
            return false;
        //vérifier la méthode run et toStringue

        //vérfier le constructeur avec socket
        try {
            Method run = serviceClass.getMethod("run");
            if(!(run.getReturnType() == void.class))
                return false;
            int modRun = run.getModifiers();
            if(!Modifier.isPublic(modRun))
                return false;

            Method toStringue = serviceClass.getMethod("toStringue");
            if(!(run.getReturnType() == String.class))
                return false;
            int modToStringue = toStringue.getModifiers();
            if(!Modifier.isPublic(modToStringue) || !Modifier.isStatic(modToStringue))
                return false;

            serviceClass.getConstructor(Socket.class);
        } catch (NoSuchMethodException e) {
            return false;
        }

        //vérfier que Service est implementé
        for (Class<?> interfaces : serviceClass.getInterfaces()) {
            if(interfaces == Service.class)
                return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
