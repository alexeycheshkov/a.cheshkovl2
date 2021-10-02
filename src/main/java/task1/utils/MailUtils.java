package task1.utils;

import aquality.selenium.core.logging.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Properties;

public class MailUtils {
    private static final Properties PROPERTIES = FileUtils.loadProperties("src/main/resources/mailconfig.properties");
    private static final String USER_NAME = PROPERTIES.getProperty("mail.pop3.user");
    private static final String USER_PASS = PROPERTIES.getProperty("mail.pop3.password");
    private static final String HOST = PROPERTIES.getProperty("mail.pop3.host");
    private static final String PROTOCOL= PROPERTIES.getProperty("mail.pop3.protocol");
    private static final String MAIL_FOLDER = "INBOX";
    private static final String MAIL_FROM = "noreply@mail.my.kaspersky.com";


    public static String getTextFromLatestMessage(){
        Session emailSession = Session.getDefaultInstance(PROPERTIES);
        String emailText = null;
        Store store = null;
        Folder inboxFolder = null;
        try {
            Logger.getInstance().info("Connecting and opening to email box");
            store = emailSession.getStore(PROTOCOL);
            store.connect(HOST,USER_NAME,USER_PASS);
            inboxFolder = store.getFolder(MAIL_FOLDER);
            inboxFolder.open(Folder.READ_ONLY);
            int count = inboxFolder.getMessageCount();
            for (int i=0; i<60;i++){
                Logger.getInstance().debug("Waiting a new messages");
                Thread.sleep(1000);
                if (count!=inboxFolder.getMessageCount()){
                    Logger.getInstance().info("Received new message in folder "+MAIL_FOLDER);
                    break;
                }
            }
            SearchTerm searchTerm = new AndTerm(new FromTerm(new InternetAddress(MAIL_FROM)), new FlagTerm(new Flags(Flags.Flag.SEEN),false));
            Message[] messages = inboxFolder.search(searchTerm);
            if (messages.length!=0) {
                emailText = getTextFromMessage(messages[messages.length - 1]);
            } else {
                Logger.getInstance().error("New messages didn't found");
            }
        } catch (MessagingException | InterruptedException e) {
            Logger.getInstance().error("Email box didn't opened");
            e.printStackTrace();
        } finally {
            try {
                if (inboxFolder != null) {
                    inboxFolder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return emailText;
    }

    private static String getTextFromMessage(Message message) {
        String result = "";
        try {
            if (message.isMimeType("text/plain")) {
                result = message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                result = getTextFromMimeMultipart(mimeMultipart);
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) {
        String result = "";
        try {
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    result = result + "\n" + bodyPart.getContent();
                } else if (bodyPart.isMimeType("text/html")) {
                    String html = (String) bodyPart.getContent();
                    result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
                } else if (bodyPart.getContent() instanceof MimeMultipart){
                    result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
