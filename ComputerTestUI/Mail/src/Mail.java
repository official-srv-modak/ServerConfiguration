import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.jdi.connect.Transport;

public class Mail {


	public static void sendSimpleMail(String toRecipient, String ccRecipient, String bccRecipient, String attachment, String username, String password, String subject, String messageString, String smtpHost, String smtpPort)
	{

		  Properties properties = System.getProperties();  
		  properties.put("mail.smtp.auth", "true");
		  properties.put("mail.smtp.starttls.enable", "true");
		  properties.put("mail.smtp.host", smtpHost);
		  properties.put("mail.smtp.port", smtpPort); 

		  Session session = Session.getDefaultInstance(properties,  
		   new javax.mail.Authenticator() {  
		   protected PasswordAuthentication getPasswordAuthentication() {  
		   return new PasswordAuthentication(username, password);  
		   }  
		  });  

		  try{  

		    MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(username));  
		    message.addRecipient(Message.RecipientType.TO,new InternetAddress(toRecipient));  
		    if (!ccRecipient.isEmpty())
				message.setRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipient));
			if (!bccRecipient.isEmpty())
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(bccRecipient));
		    message.setSubject(subject);  

		    BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setText(messageString);  

		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    Multipart multipart = new MimeMultipart();

		    if(!attachment.isEmpty())
		    {
		    	String filename = attachment; 
			    DataSource source = new FileDataSource(filename);  
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(filename); 
			    multipart.addBodyPart(messageBodyPart1);  
			    multipart.addBodyPart(messageBodyPart2); 
		    }
		    else
		    	multipart.addBodyPart(messageBodyPart1); 

		    message.setContent(multipart);  

		    javax.mail.Transport.send(message);

		   System.out.println("message sent");  
		   }
		  	catch (MessagingException ex) {
		  		ex.printStackTrace();
		   }  
	}  

	public static void sendMultipleRecipientMail(List<String> toRecipient, List<String> ccRecipient, List<String> bccRecipient, String attachment, String username, String password, String subject, String messageString, String smtpHost, String smtpPort)
	{

		  Properties properties = System.getProperties();  
		  properties.put("mail.smtp.auth", "true");
		  properties.put("mail.smtp.starttls.enable", "true");
		  properties.put("mail.smtp.host", smtpHost);
		  properties.put("mail.smtp.port", smtpPort); 

		  Session session = Session.getDefaultInstance(properties,  
		   new javax.mail.Authenticator() {  
		   protected PasswordAuthentication getPasswordAuthentication() {  
		   return new PasswordAuthentication(username, password);  
		   }  
		  });  

		  try{  

		    MimeMessage message = new MimeMessage(session);  
		    message.setFrom(new InternetAddress(username));
		    List<Address> toAddresses = new ArrayList<Address>();
		    List<Address> ccAddresses = new ArrayList<Address>();
		    List<Address> bccAddresses = new ArrayList<Address>();

		    for (int i = 0; i<toAddresses.size() && !toRecipient.get(i).isEmpty(); i++)
		    	toAddresses.set(i, new InternetAddress(toRecipient.get(i)));

		    for (int i = 0; i<ccAddresses.size() && !ccRecipient.get(i).isEmpty(); i++)
		    	ccAddresses.set(i, new InternetAddress(ccRecipient.get(i)));

		    for (int i = 0; i<bccAddresses.size() && !bccRecipient.get(i).isEmpty(); i++)
		    	bccAddresses.set(i, new InternetAddress(bccRecipient.get(i)));

		    message.addRecipients(Message.RecipientType.TO, (Address[])toAddresses.toArray());

		    if (ccAddresses.size() != 0)
				message.addRecipients(Message.RecipientType.CC, (Address[])ccAddresses.toArray());

		    if (bccAddresses.size() != 0)
				message.addRecipients(Message.RecipientType.BCC, (Address[])bccAddresses.toArray());

		    message.setSubject(subject);  

		    BodyPart messageBodyPart1 = new MimeBodyPart();  
		    messageBodyPart1.setText(messageString);  

		    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    Multipart multipart = new MimeMultipart();

		    if(!attachment.isEmpty())
		    {
		    	String filename = attachment; 
			    DataSource source = new FileDataSource(filename);  
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(filename); 
			    multipart.addBodyPart(messageBodyPart1);  
			    multipart.addBodyPart(messageBodyPart2); 
		    }
		    else
		    	multipart.addBodyPart(messageBodyPart1); 

		    message.setContent(multipart);  

		    javax.mail.Transport.send(message);

		   System.out.println("message sent");  
		   }
		  	catch (MessagingException ex) {
		  		ex.printStackTrace();
		   }  
	}  

}