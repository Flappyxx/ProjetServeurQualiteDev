package serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServiceMessage {
	private static Map<String, ArrayList<String>> messageMap;
	
	static {
		messageMap = new HashMap<>();
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
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < this.messageMap.get(login).size(); i++) {
			str.append(this.messageMap.get(login).get(i));
		}
		return str.toString();
	}
	
	public String toStringue() {
		StringBuilder str = new StringBuilder();
		for(Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
		    str.append("Liste de message pour l'utilisateur " + entry.getKey() + " : ");
		    for(int i = 0; i < messageMap.get(entry.getKey()).size(); i++){
		    	str.append(messageMap.get(entry.getKey()).get(i));
		    	str.append(" ");
		    }
		    str.append("##");
		}		
		return str.toString();
	}
}
