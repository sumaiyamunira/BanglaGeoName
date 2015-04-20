package com.example.banglageoname.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends javax.mail.Authenticator {

	private Multipart attachements;


	private String fromAddress = "";
	private String accountEmail = "";
	private String accountPassword = "";
	private String smtpHost = "smtp.gmail.com";
	private String smtpPort = "465"; // 465,587
	private String toAddresses = "";
	private String mailSubject = "";
	private String mailBody = "";

	
	public Mail() {
		attachements = new MimeMultipart();


	}

	public Mail(String user, String pass) {
		this();
		accountEmail = user;
		accountPassword = pass;
	}

	public boolean send() throws Exception {

		Properties props = new Properties();
		//props.put("mail.smtp.user", d_email);
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", smtpPort);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		try {
			Session session = Session.getInstance(props, this);
			session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);
			// create the message part 
		    MimeBodyPart messageBodyPart = new MimeBodyPart();
		    //fill message
		    messageBodyPart.setText(mailBody);
		    // add to multipart
		    attachements.addBodyPart(messageBodyPart);
		    
			//msg.setText(mailBody);
			msg.setSubject(mailSubject);
			msg.setFrom(new InternetAddress(fromAddress));
			msg.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddresses));
			msg.setContent(attachements);

			Transport transport = session.getTransport("smtps");
			transport.connect(smtpHost, 465, accountEmail, accountPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addAttachment(String filePath, String fileName) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filePath);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileName);

		attachements.addBodyPart(messageBodyPart);
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(accountEmail, accountPassword);
	}

	/**
	 * Gets the fromAddress.
	 * 
	 * @return <tt> the fromAddress.</tt>
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * Sets the fromAddress.
	 *
	 * @param fromAddress <tt> the fromAddress to set.</tt>
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * Gets the toAddresses.
	 * 
	 * @return <tt> the toAddresses.</tt>
	 */
	public String getToAddresses() {
		return toAddresses;
	}

	/**
	 * Sets the toAddresses.
	 *
	 * @param toAddresses <tt> the toAddresses to set.</tt>
	 */
	public void setToAddresses(String toAddresses) {
		this.toAddresses = toAddresses;
	}

	/**
	 * Gets the mailSubject.
	 * 
	 * @return <tt> the mailSubject.</tt>
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * Sets the mailSubject.
	 *
	 * @param mailSubject <tt> the mailSubject to set.</tt>
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	/**
	 * Gets the mailBody.
	 * 
	 * @return <tt> the mailBody.</tt>
	 */
	public String getMailBody() {
		return mailBody;
	}

	/**
	 * Sets the mailBody.
	 *
	 * @param mailBody <tt> the mailBody to set.</tt>
	 */
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
}