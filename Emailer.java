
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;



public final class Emailer {
	public Emailer(){

	}

	/*public static void main(String argv[]) {
		Emailer em = new Emailer();
		String msg = "Ce message de bienvenue est envoy\u00e9 automatiquement au cours de l'inscription \u00e0";
		em.sendEmail("bishopp@lexmark.com", "bishopp@lexmark.com", "Test2", msg, null);
	}*/

	/*public static void sendEmail(String pFromEmailAddr, String pToEmailAddr,
			String pSubject, String pBody, String pAttachment) {
		
		String[] pToEmailAddrAry = {pToEmailAddr} ;
		sendEmail(pFromEmailAddr, pToEmailAddrAry, pSubject, pBody, pAttachment);
		
	}*/
	
	public static void sendEmail(String pFromEmailAddr, String pToEmailAddr,
			String pSubject, String pBody, String pAttachment) {

		//String smtpHost = "smtp5b.notes.lexmark.com";
		//Lotus Notes is being decommissioned 2014.  Use Gmail SMTP server
		String smtpHost = "mail.lexmark.com";
		Properties props = new Properties();
		// Setup mail server
		props.put("mail.smtp.host", smtpHost);

		try {
			// Get session
	          Session session = Session.getDefaultInstance(props, null);

	          // Define message
	          MimeMessage message = new MimeMessage(session);
	          InternetAddress addressFrom = new InternetAddress(pFromEmailAddr);
	          message.setFrom(addressFrom);
	           
	          String[] addr = pToEmailAddr.split(",");
	          System.out.println("addr.length="+addr.length);
	          InternetAddress[] addressTo = new InternetAddress[addr.length];
	          for (int i = 0; i < addr.length; i++) {
	           System.out.println(addr[i]); 
	           addressTo[i] = new InternetAddress(addr[i]);
	          }
	          message.setRecipients(Message.RecipientType.TO, addressTo);

	          message.setSubject(pSubject);
	          message.setHeader("Content-Type","text/plain;charset=utf-8");

	          // Create the multi-part
	          Multipart multipart = new MimeMultipart();

	          // Create part one
	          BodyPart messageBodyPart = new MimeBodyPart();

	          // Fill the message
	          messageBodyPart.setHeader("Content-Type","text/plain;charset=utf-8");
	          messageBodyPart.setText(pBody);

	          // Add the first part
	          multipart.addBodyPart(messageBodyPart);

	          if ((pAttachment != null) && (pAttachment.length() > 0)) {
	             // Part two is attachment
	             messageBodyPart = new MimeBodyPart();
	             DataSource source = new FileDataSource(pAttachment);
	             messageBodyPart.setDataHandler(new DataHandler(source));
	             messageBodyPart.setFileName(pAttachment);

	             // Add the second part
	             multipart.addBodyPart(messageBodyPart);
	          }
	          // Put parts in message
	          message.setContent(multipart);

	          // Send message
	          Transport.send(message);

		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
	}
}
