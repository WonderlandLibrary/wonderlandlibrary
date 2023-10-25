package org.apache.logging.log4j.core.net;

import java.nio.charset.StandardCharsets;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.util.Builder;

public class MimeMessageBuilder implements Builder<MimeMessage> {
  private final MimeMessage message;
  
  public MimeMessageBuilder(Session session) {
    this.message = new MimeMessage(session);
  }
  
  public MimeMessageBuilder setFrom(String from) throws MessagingException {
    InternetAddress address = parseAddress(from);
    if (null != address) {
      this.message.setFrom((Address)address);
    } else {
      try {
        this.message.setFrom();
      } catch (Exception ex) {
        this.message.setFrom((Address)null);
      } 
    } 
    return this;
  }
  
  public MimeMessageBuilder setReplyTo(String replyTo) throws MessagingException {
    InternetAddress[] addresses = parseAddresses(replyTo);
    if (null != addresses)
      this.message.setReplyTo((Address[])addresses); 
    return this;
  }
  
  public MimeMessageBuilder setRecipients(Message.RecipientType recipientType, String recipients) throws MessagingException {
    InternetAddress[] addresses = parseAddresses(recipients);
    if (null != addresses)
      this.message.setRecipients(recipientType, (Address[])addresses); 
    return this;
  }
  
  public MimeMessageBuilder setSubject(String subject) throws MessagingException {
    if (subject != null)
      this.message.setSubject(subject, StandardCharsets.UTF_8.name()); 
    return this;
  }
  
  @Deprecated
  public MimeMessage getMimeMessage() {
    return build();
  }
  
  public MimeMessage build() {
    return this.message;
  }
  
  private static InternetAddress parseAddress(String address) throws AddressException {
    return (address == null) ? null : new InternetAddress(address);
  }
  
  private static InternetAddress[] parseAddresses(String addresses) throws AddressException {
    return (addresses == null) ? null : InternetAddress.parse(addresses, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\MimeMessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */