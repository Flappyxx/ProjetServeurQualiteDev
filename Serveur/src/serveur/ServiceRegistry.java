package serveur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new ArrayList<>();
		servicesStates = new ArrayList<>();
	}
	private static List<Class<?>> servicesClasses ;
	private static List<Boolean> servicesStates ;

// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(Class<?> classService) {
		// vérifier la conformité par introspection
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector

		servicesClasses.add(classService);
		servicesStates.add(true);
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<? extends Service> getServiceClass(int numService) {
		return (Class<? extends Service>) servicesClasses.get(numService);
	}
	
	
	public static void activationService(Class<?> classService) {
		for(int i = 0; i < servicesClasses.size(); i++) {
			if(servicesClasses.get(i).equals(classService)) {
				servicesStates.set(i, true);
			}
		}
	}
	
	public static void desactivationService(Class<?> classService) {
		for(int i = 0; i < servicesClasses.size(); i++) {
			if(servicesClasses.get(i).equals(classService)) {
				servicesStates.set(i, false);
			}
		}
	}
	
// liste les activités présentes
	public static String toStringue()  {
		String result = "Activités présentes :##";
		for (int i = 0; i < servicesClasses.size(); i++) {
			try{
				result += servicesClasses.get(i).getMethod("toStringue").invoke(servicesClasses);
				result += servicesStates.get(i);
				result += "##";
			;}
			catch (NoSuchMethodException e){
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String toStringue(boolean val) {
		String result = "";
		if(val == true) {
			result += "Activités désactivé présentes :##";
		}
		else {
			result += "Activités désactivé présentes :##";
		}
		for(int i = 0; i < servicesStates.size(); i++) {
			try{
			if(servicesStates.get(i) == false) {
				result += servicesClasses.get(i).getMethod("toStringue", boolean.class).invoke(servicesClasses);
				result += "##";
			}
			else {
				result += servicesClasses.get(i).getMethod("toStringue", boolean.class).invoke(servicesClasses);
				result += "##";
			}
			}
			catch (NoSuchMethodException e){
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void desintaller(int num) {
		servicesClasses.remove(num);
		servicesStates.remove(num);
	}

	public static String printAllServices(){
		String result = "Activités présentes :##";
		for (Class<?> servicesClass : servicesClasses) {
			try{
				result += servicesClass.getMethod("toStringue").invoke(servicesClass);
				result += "##";
				;}
			catch (NoSuchMethodException e){
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
