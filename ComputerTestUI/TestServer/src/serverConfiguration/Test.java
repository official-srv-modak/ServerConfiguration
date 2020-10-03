package serverConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class Test {
	
    public static void main(String[] args) throws Exception {
    	ServerConnection dc = new ServerConnection();
    	System.out.println(dc.checkConnection("http://10.94.13.58/checkConnection.php"));
    	//dc.sendPOST("http://10.94.13.58/extractUserValue.php", "sourav");
    	List <String> userData = new ArrayList<String>();
    	userData.add("19736");
    	userData.add("Abdul");
    	userData.add("Muizz");
    	userData.add("abdulmuizzf@temenos.com");
    	userData.add("Corporate");
    	System.out.println(dc.addUser(userData, "http://10.94.13.58/writeData.php"));
        System.out.println(dc.getData("http://10.94.13.58/extractUserValue.php"));
        //System.out.println(dc.checkUser("18801","http://10.94.13.58/checkUser.php"));
    }
}
