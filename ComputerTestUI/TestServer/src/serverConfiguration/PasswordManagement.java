package serverConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class PasswordManagement {
	
	private static String adminUserURI = "http://localhost/extract_admin_users.php";
	private static String accessHistoryURI = "http://localhost/update_user_access_table.php";
	
	private static String passwordFilePath = "password.bin";
	
	private static String key = "t6w9y$B&E)H@McQf"; // 128 bit key
	
	private static String systemGeneratedEmailAddress = "srvmodak656@gmail.com", systemEmailPassword = "anu#@583";
	
	private static boolean systemAccessFlag = false;
	
	private static void writeAccessHistory(List<String> usernames, String URI)
	{
		if (usernames.size() > 0)
		{
			try
			{
				URL url = new URL(URI);
			    Map params = new LinkedHashMap<>();
			    systemAccessFlag = true;
			    params.put("password", getPasswordSystem());
			    params.put("usernames", usernames);
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
			    String output = "";
			    while ((inputLine = in.readLine()) != null) {
					{
						output += inputLine+"\n";
					}
				}
				in.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Wrong data");
		}
		
	}
	
	private static boolean authenticationAdmin()
	{
		List<String> userList = new ArrayList<>();
		System.out.println("Initialising OTP authentication process...");
		Scanner scan = new Scanner(System.in);
		boolean flag = false;
		try {
			userList = getData(adminUserURI);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			Random rand = new Random(); 
			List <Integer>otpList = new ArrayList<Integer>();
			for(int i = 0; i < userList.size(); i++)
				otpList.add(rand.nextInt(1000000));
			
			String subject = "", Body = "";
			for(int i = 0; i < userList.size(); i++)
			{
				subject = "[CAUTION] OTP for the Password change for the verified database requested on "+dtf.format(now);
				Body = "Hi "+userList.get(i).split("@")[0]+",\n\t\t*THIS IS VERY IMPORTANT INFORMATION*\n"+
						"It has been found that, a change/access of password has been requested by one of the admin.\n"+
						"If intensional all the admin may find as OTP as it is present below.\n"+
						"The usernames of the admin will be duely recorded for future audit and verification purposes.\n"+
						"If unintensional, all the admin are supposed to come up with detailed report of how it happened and why.\n"+
						"Any unauthorised access must be thoroughly investigated.\n\nThe OTP is "+otpList.get(i)+"\n"+
						"\n\nThanks and Regards\nSourav Modak";
			    Mail.sendSimpleMail(userList.get(i), "", "", "", systemGeneratedEmailAddress, systemEmailPassword, subject, Body, "smtp.gmail.com", "587");
			    System.out.println("OTP SENT to user "+userList.get(i));
			}
			
		for(int i = 0; i<userList.size(); i++)
		{
			System.out.println("Enter the OTP sent to the user : "+userList.get(i));
			int j = 0;
			for(j = 0; j<3; j++)
			{
				int enteredOtp = scan.nextInt();
				if(enteredOtp != otpList.get(i))
				{
					System.out.println("Wrong OTP entered\nPlease try again. Chances remaining : "+((int)3-(j+1)));
				}
				else
					{
						flag = true;
						break;
					}
			}
			if(j == 3)
			{
				System.out.println("Chances exhausted, system will reset");
				flag = false;
				break;
			}
		}
			
		
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(flag == true)
			System.out.println("ACCESS GRANTED");
		writeAccessHistory(userList, accessHistoryURI);
		return flag;
	}
	
	private static List<String> getAdminUsers(String URL) {
		List <String> output = new ArrayList<String> ();
		try 
		{
			URL url = new URL(URL);
		    Map params = new LinkedHashMap<>();
		    systemAccessFlag = true;
		    params.put("password", getPasswordSystem());
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
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return output;
		// print result
		//System.out.println(response.toString());
	}
	
	private static List<String> getData(String URL) throws IOException, InterruptedException {

		List <String> output = new ArrayList<String> ();
		URL url = new URL(URL);
	    Map params = new LinkedHashMap<>();
	    systemAccessFlag = true;
	    params.put("password", getPasswordSystem());
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
    }
	
	static String getPasswordSystem()
	{
		String password = "";
		File passwordFile = new File(passwordFilePath);
		boolean auth = false;	// Should be false
		auth = systemAccessFlag;
		if (auth == true)
		{
			systemAccessFlag = false;
			try 
			{
				if(!passwordFile.exists())
				{
					System.out.println("Password file doesn't exist, please create a password");
					systemAccessFlag = true;
					changePasswordSystem();
				}
				ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(passwordFilePath));
				String cypherText = (String) objIn.readObject();
				password = decrypt(cypherText);
				objIn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return password;
	}
	
	static String getPasswordAdmin()
	{
		String password = "";
		File passwordFile = new File(passwordFilePath);
		boolean auth = false;	// Should be false
		auth = authenticationAdmin();
		if (auth == true)
		{
			try 
			{
				if(!passwordFile.exists())
				{
					System.out.println("Password file doesn't exist, please create a password");
					changePasswordAdmin();
				}
				ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(passwordFilePath));
				String cypherText = (String) objIn.readObject();
				password = decrypt(cypherText);
				objIn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return password;
	}
	
	private static void writePasswordSystem(String password)
	{
		boolean auth = false;	// Should be false
		auth = systemAccessFlag;
		if (auth == true)
		{
			systemAccessFlag = false;
			File passwordFile = new File(passwordFilePath);
			try
			{
				if(!passwordFile.exists())
				{
					passwordFile.createNewFile();
				}
				ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(passwordFilePath));
				String cypherText = encrypt(password);
				objOut.writeObject(cypherText);
				objOut.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void writePasswordAdmin(String password)
	{
		boolean auth = true;	// Should be false
		auth = authenticationAdmin();
		if (auth == true)
		{
			File passwordFile = new File(passwordFilePath);
			try
			{
				if(!passwordFile.exists())
				{
					passwordFile.createNewFile();
				}
				ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(passwordFilePath));
				String cypherText = encrypt(password);
				objOut.writeObject(cypherText);
				objOut.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String encrypt(String text) 
    {
        try 
        {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return new String(encrypted);
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }
	
	private static String decrypt(String encrypted)
	{
		try 
        {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted.getBytes()));
            return new String(decrypted);
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            return null;
        }
	}
	
	private static void changePasswordAdmin()
	{
		Scanner scan = new Scanner(System.in);
		File file = new File(passwordFilePath);
		if(file.exists())
		{
			systemAccessFlag = true;
			String oldPassword = getPasswordSystem();
			System.out.println("Enter the old password");
			String enteredOldPassword = scan.next();
			if(oldPassword.equals(enteredOldPassword))
			{
				System.out.println("Enter a new password");
				String newEnteredPassword1 = scan.next();
				System.out.println("Enter again");
				String newEnteredPassword2 = scan.next();
				
				if(newEnteredPassword1.equals(newEnteredPassword2))
				{
					writePasswordAdmin(newEnteredPassword1); //write to file
					//write to mysql db 
					//write to apache ini
					//write to mysql ini
					System.out.println("PASSWORD CHANGED");
				}
				else
				{
					System.out.println("Passwords doesn't match");
				}
			}
			else
			{
				System.out.println("Incorrect password");
			}
		}
		else
		{
			System.out.println("Enter a new password");
			String newEnteredPassword1 = scan.next();
			System.out.println("Enter again");
			String newEnteredPassword2 = scan.next();
			
			if(newEnteredPassword1.equals(newEnteredPassword2))
			{
				writePasswordAdmin(newEnteredPassword1); //write to file
				//write to mysql db 
				//write to apache ini
				//write to mysql ini
				System.out.println("PASSWORD CHANGED");
			}
			else
			{
				System.out.println("Passwords doesn't match");
			}
		}
	}
	
	private static void changePasswordSystem()
	{
		if(!systemAccessFlag)
			return;
		systemAccessFlag = false;
		Scanner scan = new Scanner(System.in);
		File file = new File(passwordFilePath);
		if(file.exists())
		{
			systemAccessFlag = true;
			String oldPassword = getPasswordSystem();
			System.out.println("Enter the old password");
			String enteredOldPassword = scan.next();
			if(oldPassword.equals(enteredOldPassword))
			{
				System.out.println("Enter a new password");
				String newEnteredPassword1 = scan.next();
				System.out.println("Enter again");
				String newEnteredPassword2 = scan.next();
				
				if(newEnteredPassword1.equals(newEnteredPassword2))
				{
					systemAccessFlag = true;
					writePasswordSystem(newEnteredPassword1); //write to file
					//write to mysql db 
					//write to apache ini
					//write to mysql ini
					System.out.println("PASSWORD CHANGED");
				}
				else
				{
					System.out.println("Passwords doesn't match");
				}
			}
			else
			{
				System.out.println("Incorrect password");
			}
		}
		else
		{
			System.out.println("Enter a new password");
			String newEnteredPassword1 = scan.next();
			System.out.println("Enter again");
			String newEnteredPassword2 = scan.next();
			
			if(newEnteredPassword1.equals(newEnteredPassword2))
			{
				systemAccessFlag = true;
				writePasswordSystem(newEnteredPassword1); //write to file
				//write to mysql db 
				//write to apache ini
				//write to mysql ini
				System.out.println("PASSWORD CHANGED");
			}
			else
			{
				System.out.println("Passwords doesn't match");
			}
		}
	}
	
	public static void runProgram()
	{
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			System.out.println("1. Get password");
			System.out.println("2. Change password");
			System.out.println("3. Add authority");
			System.out.println("4. Delete authority");
			System.out.println("5. Display Authority(s)");
			System.out.println("6. Display admin");
			System.out.println("7. Exit");
			int ch = scan.nextInt();
			switch(ch)
			{
				case 1:
					System.out.println(getPasswordAdmin());
				break;
				
				case 2:
					changePasswordAdmin();
				break;
				
				case 5:
					System.out.println(getAdminUsers(adminUserURI));
				break;
				
				case 7:
					System.out.println("TERMINATED");
					System.exit(0);
				
				
			}
			System.out.println();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		runProgram();
	}

}
