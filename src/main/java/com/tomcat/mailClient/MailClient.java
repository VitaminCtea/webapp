package com.tomcat.mailClient;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.function.Function;

public record MailClient(String fromAddress, String toAddress) {

    public void sendMessage(Function<Session, Message> createMessage) throws MessagingException {
        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");   // 发送邮件时分配给协议的名称
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.user", fromAddress);
        props.setProperty("mail.password", "tfevajssfzwohgab");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
            }
        });

        session.setDebug(true);

        Transport transport = session.getTransport();
        transport.connect();

        Message message = createMessage.apply(session);
        transport.sendMessage(message, message.getAllRecipients());
    }

    private void saveMailMessageToFile(Message message) throws IOException, MessagingException {
        StringJoiner joiner = new StringJoiner(File.separator);
        for (String path : new String[]{System.getProperty("user.dir"), "src", "main", "webapp", "javaMail", "sendMail.eml"})
            joiner.add(path);
        File file = new File(joiner.toString());
        if (!file.getParentFile().exists()) file.getParentFile().mkdir();
        if (!file.exists()) file.createNewFile();
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
            message.writeTo(out);
        }
    }

    interface CustomMessage {
        Message createSimpleMessage(Session session) throws MessagingException;

        Message createMimeMessage(Session session) throws UnsupportedEncodingException, MessagingException;
    }

    public class JavaMailMessage implements CustomMessage {
        private final Message message;

        public JavaMailMessage(Session session, String fromName) {
            this.message = new MimeMessage(session);
            try {
                this.message.setFrom(new InternetAddress(MimeUtility.encodeWord(fromName) + "< " + fromAddress + " >"));
                this.message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            } catch (UnsupportedEncodingException | MessagingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Message createMimeMessage(Session session) throws UnsupportedEncodingException, MessagingException {
            message.setSubject("JavaMail application with text, pictures, and attachments");

            MimeBodyPart image = new MimeBodyPart();
            DataHandler imageDataHandler = new DataHandler(new FileDataSource(new File(getFilePath("log.png"))));
            image.setDataHandler(imageDataHandler);
            image.setContentID("IMAGE_LOG");
            image.setFileName(MimeUtility.encodeText(imageDataHandler.getName()));

            MimeBodyPart text = new MimeBodyPart();
            text.setContent("这是一张日志库的图片，讲述了日志的架构<br /><img src='cid:IMAGE_LOG' />", "text/html;charset=UTF-8");

            MimeMultipart mimeMultipart_text_image = new MimeMultipart();
            mimeMultipart_text_image.addBodyPart(text);
            mimeMultipart_text_image.addBodyPart(image);
            mimeMultipart_text_image.setSubType("related");

            MimeBodyPart text_image = new MimeBodyPart();
            text_image.setContent(mimeMultipart_text_image);

            MimeBodyPart attachment = new MimeBodyPart();
            DataHandler audioDataHandler = new DataHandler(new FileDataSource(new File(getFilePath("feel_the_fire.mp3"))));
            attachment.setDataHandler(audioDataHandler);
            attachment.setFileName(MimeUtility.encodeText(audioDataHandler.getName()));

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(text_image);
            mimeMultipart.addBodyPart(attachment);
            mimeMultipart.setSubType("mixed");

            message.setContent(mimeMultipart);
            return setSentDateAndSaveChanges();
        }

        @Override
        public Message createSimpleMessage(Session session) throws MessagingException {
            message.setSubject("JavaMail Application Test");
            message.setContent(
                    "If you did not attempt to sign in to your account, your password may be compromised. " +
                            "Visit https://github.com/settings/security to create a new, strong password for your GitHub account.\n" + "\n" +
                            "If you'd like to automatically verify devices in the future, consider enabling two-factor authentication on your account. " +
                            "Visit https://docs.github.com/articles/configuring-two-factor-authentication to learn about two-factor authentication.\n",
                    "text/plain"
            );

            return setSentDateAndSaveChanges();
        }

        private Message setSentDateAndSaveChanges() throws MessagingException {
            message.setSentDate(new Date());
            message.saveChanges();
            return message;
        }

        private String getFilePath(String filename) {
            StringJoiner joiner = new StringJoiner(File.separator);
            for (String path : new String[]{System.getProperty("user.dir"), "src", "main", "webapp", "store"})
                joiner.add(path);
            joiner.add(filename);
            return joiner.toString();
        }
    }

    public void receiveMessage() throws Exception {
        Properties props = new Properties();

        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.163.com");
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.user", toAddress);

        Session session = Session.getInstance(props);
        session.setDebug(true);

        IMAPStore store = (IMAPStore) session.getStore("imap");
        store.connect(null, "SOHJQFZUUMLPLHAH");

        // 官方网易邮箱imap协议的做法，POP3不需要
        store.id(
                new HashMap<>() {{
                    put("name", "myname");
                    put("version", "1.0.0");
                    put("vendor", "myclient");
                    put("support-email", "testmail@test.com");
                }}
        );

        browseMessagesFromFolder(store);
        store.close();
    }

    private void browseMessagesFromFolder(IMAPStore store) throws Exception {
        IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
        if (folder == null) throw new Exception("INBOX" + "邮件夹不存在");
        browseMessagesFromFolder(folder);
    }

    private void browseMessagesFromFolder(IMAPFolder folder) throws MessagingException, IOException {
        folder.open(Folder.READ_ONLY);

        System.out.println("You have " + folder.getMessageCount() + " messages in inbox");
        System.out.println("You have " + folder.getUnreadMessageCount() + " unread messages in inbox");

        Message[] messages = folder.getMessages();
        for (int i = 0; i < messages.length; i++) {
            System.out.println("------------------------------------第 " + (i + 1) + " 封邮件------------------------------------");
            folder.getMessage(i + 1).writeTo(System.out);
            System.out.println();
        }

        folder.close();
    }

    public static void main(String[] args) throws Exception {
        MailClient client = new MailClient("1194300942@qq.com", "jiazhuangme@163.com");
        client.sendMessage(session -> {
            try {
                CustomMessage javaMailMessage = client.new JavaMailMessage(session, "花椒是花花的椒");
                return javaMailMessage.createMimeMessage(session);
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
//        client.receiveMessage();
    }
}
