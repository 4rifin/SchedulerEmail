package com.springscheduler.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springscheduler.GMailAuthenticator;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Transactional
@Service
public class EmailService {
	
	@Value("${admin.email}")
    public static String ADMIN_EMAIL;
	@Value("${admin.password}")
    public static String ADMIN_PASSWORD;
	@Value("${email.sender}")
    public static String EMAIL_SENDER;
	@Value("${mail.smtp.host}")
	public static String SMTP_HOST;
	@Value("${mail.smtp.port}")
	public static String SMTP_PORT;
	@Value("${mail.smtp.auth}")
	public static String SMTP_AUTH;
	@Value("${mail.smtp.starttls.enable}")
	public static String SMTP_STARTTLS;
	@Value("${s3.url}")
	public static String S3_URL;
	@Value("${s3.key.name.voucher}")
	public static String S3_KEY_NAME_VOUCHER;
    @Value("${s3.access.key}")
    public static String S3_ACCESS_KEY;
    @Value("${s3.secret.access.key}")
    public static String S3_SECRET_ACCESS_KEY;
    @Value("${s3.bucket.name}")
    public static String S3_BUCKET_NAME;
    @Value("${realemail_api_key}")
	public static String REALEMAIL_API_KEY;
    
private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

public static final String EMAIL_ALIAS = "ndms.arifin@gmail.com";

    public static void sendEmail(String from, String to, String tocc, String subject, String content) {
    	setProperties();
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);

        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.starttls.enable", SMTP_STARTTLS);

        Session session =  Session.getInstance(props, new GMailAuthenticator(ADMIN_EMAIL, ADMIN_PASSWORD));


        try {
        	LOGGER.info("email senderrrrrrrrrrrrrrrrrrrrrrr " + EMAIL_SENDER);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_SENDER, EMAIL_ALIAS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tocc));
            message.setSubject(subject);
            message.setText(content);
           
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }   
    
    public static void sendAttachment(String from, String to, String tocc, String subject, String content, Long bookingId, String pathVoucher) {
    	setProperties();
    	Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);

        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.starttls.enable", SMTP_STARTTLS);

        Session session =  Session.getInstance(props, new GMailAuthenticator(ADMIN_EMAIL, ADMIN_PASSWORD));


        try {
        	LOGGER.info("email senderrrrrrrrrrrrrrrrrrrrrrr " + EMAIL_SENDER);
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(EMAIL_SENDER, EMAIL_ALIAS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tocc));
            message.setSubject(subject);
            
            // attachment
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            messageBodyPart.setText(content);
            
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String filename = pathVoucher+"VOUCHER_"+bookingId+".pdf";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("VOUCHER_"+bookingId+".pdf");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
     }

	public static void sendEmailAsHtmlWithMailGun(String from, String to, String tocc, String subject, String content) {
    	setProperties();
        Properties props = new Properties();
        props.put("mail.smtp.user","postmaster@sandboxa1f246f7acfe4854a7052b351a6cd431.mailgun.org"); 
	    props.put("mail.smtp.password", "985e9bed1c376519fe197bdf9e4ee1e2-9b1bf5d3-41e9bc1f");
	    props.put("mail.smtp.host", "smtp.mailgun.org"); 
	    props.put("mail.smtp.port", "587"); 
		props.put("mail.smtp.auth", "true"); 
	    props.put("mail.smtp.starttls.enable","false"); 
	    props.put("mail.smtp.EnableSSL.enable","true");
 
        Authenticator authenticator = new Authenticator () {
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("postmaster@sandboxa1f246f7acfe4854a7052b351a6cd431.mailgun.org","985e9bed1c376519fe197bdf9e4ee1e2-9b1bf5d3-41e9bc1f");
            }
        };

		Session session = Session.getDefaultInstance(props, authenticator); 
        
        try {
        	LOGGER.info("email send from " + ADMIN_EMAIL);
        	LOGGER.info("email send to " + to);
        	LOGGER.info("email send tocc " + tocc);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("postmaster@sandboxa1f246f7acfe4854a7052b351a6cd431.mailgun.org", EMAIL_ALIAS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to!=null?to:tocc));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tocc!=null?tocc:to));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");           
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
        	LOGGER.error("email failed send " +e);
        	e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }
	
	public static void sendEmailAsHtmlWithAPI(String from, String to, String tocc, String subject, String content) {
		 try {
			 sendSimpleMessage();
		 } catch (Exception e) {
	        	LOGGER.error("email failed send " +e);
	        	e.printStackTrace();
	            //throw new RuntimeException(e);
	        }
	}
	
	public static JsonNode sendSimpleMessage() throws UnirestException {
		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + "sandboxa1f246f7acfe4854a7052b351a6cd431.mailgun.org" + "/messages")
                .basicAuth("api", "key-5eda34e2d1559de8b1e012ea9b94f755")
                .field("from", "Excited User <USER@YOURDOMAIN.COM>")
                .field("to", "ndms.arifin@gmail.com")
                .field("subject", "Hello")
                .field("text", "Testing out some Mailgun awesomeness!")
                .field("html", "<html>HTML version </html>")
                .asJson();

           return request.getBody();
	}
	
	public static void sendEmailAsHtml(String from, String to, String tocc, String subject, String content) {
    	setProperties();
        Properties props = new Properties();
        props.put("mail.smtp.user",ADMIN_EMAIL); 
	    props.put("mail.smtp.password", ADMIN_PASSWORD);
	    props.put("mail.smtp.host", SMTP_HOST); 
	    props.put("mail.smtp.port", SMTP_PORT); 
	    props.put("mail.smtp.auth", "true"); 
	    props.put("mail.smtp.starttls.enable","false");
	    props.put("mail.smtp.EnableSSL.enable","true");

        //Session session =  Session.getInstance(props, new GMailAuthenticator(ADMIN_EMAIL, ADMIN_PASSWORD)); 
        Authenticator authenticator = new Authenticator () {
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(ADMIN_EMAIL,ADMIN_PASSWORD);
            }
        };

		Session session = Session.getDefaultInstance(props, authenticator); 
        
        try {
        	LOGGER.info("email send from " + ADMIN_EMAIL);
        	LOGGER.info("email send to " + to);
        	LOGGER.info("email send tocc " + tocc);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ADMIN_EMAIL, EMAIL_ALIAS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to!=null?to:tocc));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tocc!=null?tocc:to));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");           
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
        	LOGGER.error("email failed send " +e);
        	e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }
    
    public static void setProperties(){
    	Properties properties = new Properties();
        try {
         InputStream inStream = EmailService.class.getClassLoader().getResourceAsStream("application.properties");
         properties.load(inStream);
         System.out.println("Success loading properties file...");
        } catch (Exception e) {
            System.out.println("Error loading properties file...");
         e.printStackTrace();
        }

        SMTP_HOST = properties.getProperty("mail.smtp.host");
        SMTP_PORT = properties.getProperty("mail.smtp.port");
        SMTP_AUTH = properties.getProperty("mail.smtp.auth");
        SMTP_STARTTLS = properties.getProperty("mail.smtp.starttls.enable");;
        ADMIN_EMAIL = properties.getProperty("admin.email");
        ADMIN_PASSWORD = properties.getProperty("admin.password");
        EMAIL_SENDER = properties.getProperty("email.sender");
        S3_URL = properties.getProperty("s3.url");
        S3_KEY_NAME_VOUCHER = properties.getProperty("s3.key.name.voucher");
        S3_ACCESS_KEY = properties.getProperty("s3.access.key");
        S3_SECRET_ACCESS_KEY = properties.getProperty("s3.secret.access.key");
        S3_BUCKET_NAME = properties.getProperty("s3.bucket.name");
    }
    
    public static void setupEmailHtml(String toEmail,String uri){
    	sendEmailAsHtml(EmailService.ADMIN_EMAIL, toEmail, "","RESET PASSWORD",
				""
				+ "<div style='font-family:verdana;'>"
				+ "<table width='100%'>"
				+ "				<caption><h2>We got your request to change your password!</h2></caption>"
				+ "				<thead>"
				+ "				<tr>"
				+ "				</tr>"
				+ "				</tbody></table>"
				+ "				<br></div>"
				+ "	<div style='font-family:verdana;'>"
				+ "			<table width='100%'>"
				+ "				<table width='30%' cellspacing='0' cellpadding='0' border='0' align='center'>"
						+ "									<tbody>"
						+ "									<tr>"
						+ "										<td style='padding:14px 20px 14px 20px;background-color:#133455;border-radius:4px' align='center'>"
						+ "											<a class='m_-6014688869329807025m_overflow280' href="+uri+" style='font-family:helvetica neue,helvetica,arial,sans-serif;font-weight:bold;font-size:18px;line-height:22px;color:#ffffff;text-decoration:none;display:block;text-align:center;max-width:400px;overflow:hidden;text-overflow:ellipsis' target='_blank'> Reset password</a>'"
						+ "										</td>"
						+ "									</tr>"
						+ "									</tbody>"
						+ "			</table></table>"
				+ "<br></div>"
				+ "<div style='font-family:verdana;'>"
			+ "<table width='100%'>"
			+ "	<caption><p>ndms.arifin@gmail.com</p></caption>"
			+ "	<thead>"
			+ "	<tr>"
+ "				</tr>"
+ "				</tbody></table>"
+ "				<br></div>");
    }
    
    public static void setupEmailLogin(String toEmail,String uri,String userName,String password){
    	sendEmailAsHtml(EmailService.ADMIN_EMAIL, toEmail, "","ACCOUNT LOGIN",
				""
				+ "<div style='font-family:verdana;'>"
				+ "<table width='100%'>"
				+ "				<caption><h2>Thank you for add user</h2></caption>"
				+ "				<thead>"
				+ "				<tr>"
				+ "				</tr>"
				+ "				</tbody></table>"
				+ "				<br></div>"
				+ "	<div style='font-family:verdana;'>"
				+ "<table align='center'  border = '1'>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						Username"
						+ "					</td>"
						+ "					<td>"
						+ "						"+userName+""
						+ "					</td>"
						+ "				</tr>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						email"
						+ "					</td>"
						+ "					<td>"
						+ "						"+toEmail+""
						+ "					</td>"
						+ "				</tr>"
						+ "				</table>"
						+ "							<table width='100%'>"
				+ "				<table width='30%' cellspacing='0' cellpadding='0' border='0' align='center'>"
						+ "									<tbody>"
						+ "									<tr>"
						+ "										<td style='padding:14px 20px 14px 20px;background-color:#133455;border-radius:4px' align='center'>"
						+ "											<a class='m_-6014688869329807025m_overflow280' href="+uri+" style='font-family:helvetica neue,helvetica,arial,sans-serif;font-weight:bold;font-size:18px;line-height:22px;color:#ffffff;text-decoration:none;display:block;text-align:center;max-width:400px;overflow:hidden;text-overflow:ellipsis' target='_blank'> Login</a>'"
						+ "										</td>"
						+ "									</tr>"
						+ "									</tbody>"
						+ "			</table></table>"
				+ "<br></div>"
				+ "<div style='font-family:verdana;'>"
			+ "<table width='100%'>"
			+ "	<caption><p>ndms.arifin@gmail.com</p></caption>"
			+ "	<thead>"
			+ "	<tr>"
+ "				</tr>"
+ "				</tbody></table>"
+ "				<br></div>");
    }
    
    public static void setupEmailUser(String toEmail,String uri,String userName){
    	sendEmailAsHtml(EmailService.ADMIN_EMAIL, toEmail, "","ACCOUNT USER",
				""
				+ "<div style='font-family:verdana;'>"
				+ "<table width='100%'>"
				+ "				<caption><h2>Thank you for add user</h2></caption>"
				+ "				<thead>"
				+ "				<tr>"
				+ "				</tr>"
				+ "				</tbody></table>"
				+ "				<br></div>"
				+ "	<div style='font-family:verdana;'>"
				+ "<table align='center'  border = '1'>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						Username"
						+ "					</td>"
						+ "					<td>"
						+ "						"+userName+""
						+ "					</td>"
						+ "				</tr>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						email"
						+ "					</td>"
						+ "					<td>"
						+ "						"+toEmail+""
						+ "					</td>"
						+ "				</tr>"
						+ "				</table>"
						+ "							<table width='100%'>"
				+ "				<table width='30%' cellspacing='0' cellpadding='0' border='0' align='center'>"
						+ "									<tbody>"
						+ "									<tr>"
						+ "										<td style='padding:14px 20px 14px 20px;background-color:#133455;border-radius:4px' align='center'>"
						+ "											<a class='m_-6014688869329807025m_overflow280' href="+uri+" style='font-family:helvetica neue,helvetica,arial,sans-serif;font-weight:bold;font-size:18px;line-height:22px;color:#ffffff;text-decoration:none;display:block;text-align:center;max-width:400px;overflow:hidden;text-overflow:ellipsis' target='_blank'> Login</a>'"
						+ "										</td>"
						+ "									</tr>"
						+ "									</tbody>"
						+ "			</table></table>"
				+ "<br></div>"
				+ "<div style='font-family:verdana;'>"
			+ "<table width='100%'>"
			+ "	<caption><p>ndms.arifin@gmail.com</p></caption>"
			+ "	<thead>"
			+ "	<tr>"
+ "				</tr>"
+ "				</tbody></table>"
+ "				<br></div>");
    }
    public static void setupEmailUserWitMailGun(String toEmail,String uri,String userName){
    	sendEmailAsHtmlWithMailGun(EmailService.ADMIN_EMAIL, toEmail, "","ACCOUNT USER",
				""
				+ "<div style='font-family:verdana;'>"
				+ "<table width='100%'>"
				+ "				<caption><h2>Thank you for add user</h2></caption>"
				+ "				<thead>"
				+ "				<tr>"
				+ "				</tr>"
				+ "				</tbody></table>"
				+ "				<br></div>"
				+ "	<div style='font-family:verdana;'>"
				+ "<table align='center'  border = '1'>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						Username"
						+ "					</td>"
						+ "					<td>"
						+ "						"+userName+""
						+ "					</td>"
						+ "				</tr>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						email"
						+ "					</td>"
						+ "					<td>"
						+ "						"+toEmail+""
						+ "					</td>"
						+ "				</tr>"
						+ "				</table>"
						+ "							<table width='100%'>"
				+ "				<table width='30%' cellspacing='0' cellpadding='0' border='0' align='center'>"
						+ "									<tbody>"
						+ "									<tr>"
						+ "										<td style='padding:14px 20px 14px 20px;background-color:#133455;border-radius:4px' align='center'>"
						+ "											<a class='m_-6014688869329807025m_overflow280' href="+uri+" style='font-family:helvetica neue,helvetica,arial,sans-serif;font-weight:bold;font-size:18px;line-height:22px;color:#ffffff;text-decoration:none;display:block;text-align:center;max-width:400px;overflow:hidden;text-overflow:ellipsis' target='_blank'> Login</a>'"
						+ "										</td>"
						+ "									</tr>"
						+ "									</tbody>"
						+ "			</table></table>"
				+ "<br></div>"
				+ "<div style='font-family:verdana;'>"
			+ "<table width='100%'>"
			+ "	<caption><p>ndms.arifin@gmail.com</p></caption>"
			+ "	<thead>"
			+ "	<tr>"
+ "				</tr>"
+ "				</tbody></table>"
+ "				<br></div>");
    }
    
    public static void setupEmailUserWithAPI(String toEmail,String uri,String userName){
    	sendEmailAsHtmlWithAPI(EmailService.ADMIN_EMAIL, toEmail, "","ACCOUNT USER",
				""
				+ "<div style='font-family:verdana;'>"
				+ "<table width='100%'>"
				+ "				<caption><h2>Thank you for add user</h2></caption>"
				+ "				<thead>"
				+ "				<tr>"
				+ "				</tr>"
				+ "				</tbody></table>"
				+ "				<br></div>"
				+ "	<div style='font-family:verdana;'>"
				+ "<table align='center'  border = '1'>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						Username"
						+ "					</td>"
						+ "					<td>"
						+ "						"+userName+""
						+ "					</td>"
						+ "				</tr>"
						+ "				<tr>"
						+ "					<td style = 'background-color:red;'>"
						+ "						email"
						+ "					</td>"
						+ "					<td>"
						+ "						"+toEmail+""
						+ "					</td>"
						+ "				</tr>"
						+ "				</table>"
						+ "							<table width='100%'>"
				+ "				<table width='30%' cellspacing='0' cellpadding='0' border='0' align='center'>"
						+ "									<tbody>"
						+ "									<tr>"
						+ "										<td style='padding:14px 20px 14px 20px;background-color:#133455;border-radius:4px' align='center'>"
						+ "											<a class='m_-6014688869329807025m_overflow280' href="+uri+" style='font-family:helvetica neue,helvetica,arial,sans-serif;font-weight:bold;font-size:18px;line-height:22px;color:#ffffff;text-decoration:none;display:block;text-align:center;max-width:400px;overflow:hidden;text-overflow:ellipsis' target='_blank'> Login</a>'"
						+ "										</td>"
						+ "									</tr>"
						+ "									</tbody>"
						+ "			</table></table>"
				+ "<br></div>"
				+ "<div style='font-family:verdana;'>"
			+ "<table width='100%'>"
			+ "	<caption><p>ndms.arifin@gmail.com</p></caption>"
			+ "	<thead>"
			+ "	<tr>"
+ "				</tr>"
+ "				</tbody></table>"
+ "				<br></div>");
    }
}
