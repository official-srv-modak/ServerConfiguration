package serverConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestServerConfiguration {

	public static String URI = "http://localhost";
	public static String password = PasswordManagement.getPasswordSystem();
	
	public static BufferedReader initialiseConnection(String URI) throws Exception
	{
		URL local = new URL(URI);
		HttpURLConnection lc = (HttpURLConnection) local.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(lc.getInputStream()));

	    return in; 
	}
	
	public static BufferedReader initialiseConnectionPassword(String URI) throws Exception
	{
		List <String> output = new ArrayList<String> ();
		URL url = new URL(URI);
	    Map params = new LinkedHashMap<>();
	    params.put("password", password);
	    StringBuilder postData = new StringBuilder();
	    Set<Map.Entry> s = params.entrySet();
	    for (Map.Entry param : s) {
	        if (postData.length() != 0) postData.append('&');
	        postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
	        postData.append('=');
	        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	    }
	    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	    conn.setDoOutput(true);
	    conn.getOutputStream().write(postDataBytes);
	    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    
	    return in;
	}
	
	public static boolean checkConnection(String URI) throws Exception
	{
		try
		{
			boolean successFlag = false;
			
		    BufferedReader in = initialiseConnectionPassword(URI);
		    String inputLine, tmp = null;

		    while ((inputLine = in.readLine()) != null) 
		        {
		    		System.out.println(inputLine);
		    		if(inputLine != null)
		    			tmp = inputLine;
		        }
		    in.close();
		    
		    if(tmp.equalsIgnoreCase("Connection Success"))
		    	return true;
		    else
		    	return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(checkConnection("http://localhost/check_connection.php"));
	}

}
