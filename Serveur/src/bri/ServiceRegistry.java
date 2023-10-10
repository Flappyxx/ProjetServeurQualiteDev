package bri;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = null;
	}
	private static List<Class<?>> servicesClasses = new ArrayList<>();

// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(Class<?> classService) {
		// vérifier la conformité par introspection
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector

		servicesClasses.add(classService);
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<? extends Service> getServiceClass(int numService) {
		return (Class<? extends Service>) servicesClasses.get(numService);
	}
	
// liste les activités présentes
	public static String toStringue()  {
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
