package serverConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ServerConnection {
	
	public static boolean addUser(List<String> userData, String URL)
	{
		boolean successFlag = false;
		URL url;
		try {
			url = new URL(URL);
			Map params = new LinkedHashMap<>();
			
			params.put("password", password);
		    params.put("employee_id", userData.get(0));
		    params.put("first_name", userData.get(1));
		    params.put("last_name", userData.get(2));
		    params.put("email", userData.get(3));
		    params.put("teams", userData.get(4));
		    
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
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
				{
					//System.out.println(inputLine);
					if(!inputLine.equals(null) && (inputLine.contains("true") || inputLine.contains("Duplicate entry")))
						successFlag = true;
				}
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
		return successFlag;
	}
	
	public static List<String> getData(String URL) throws IOException, InterruptedException {

		List <String> output = new ArrayList<String> ();
		URL url = new URL(URL);
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
	    String inputLine;
	    while ((inputLine = in.readLine()) != null) {
			{
				output.add(inputLine);
			}
		}
		in.close();
		
		return output;
		// print result
		//System.out.println(response.toString());
    }
	
	public static BufferedReader initialiseConnection(String URI) throws Exception
	{
		URL local = new URL(URI);
	    URLConnection lc = local.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(lc.getInputStream()));

	    return in; 
	}
	
	public static boolean checkConnection(String URI) throws Exception
	{
		try
		{
			boolean successFlag = false;
			
		    BufferedReader in = initialiseConnection(URI);
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
	
	public static void main(String args[]) {
		
	}
}
