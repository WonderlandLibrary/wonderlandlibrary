package org.apache.logging.log4j.core.net;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.helpers.Charsets;

public class MimeMessageBuilder {
   private final MimeMessage message;

   public MimeMessageBuilder(Session var1) {
      this.message = new MimeMessage(var1);
   }

   public MimeMessageBuilder setFrom(String var1) throws MessagingException {
      InternetAddress var2 = parseAddress(var1);
      if (null != var2) {
         this.message.setFrom(var2);
      } else {
         try {
            this.message.setFrom();
         } catch (Exception var4) {
            this.message.setFrom((InternetAddress)null);
         }
      }

      return this;
   }

   public MimeMessageBuilder setReplyTo(String var1) throws MessagingException {
      InternetAddress[] var2 = parseAddresses(var1);
      if (null != var2) {
         this.message.setReplyTo(var2);
      }

      return this;
   }

   public MimeMessageBuilder setRecipients(RecipientType var1, String var2) throws MessagingException {
      InternetAddress[] var3 = parseAddresses(var2);
      if (null != var3) {
         this.message.setRecipients(var1, var3);
      }

      return this;
   }

   public MimeMessageBuilder setSubject(String var1) throws MessagingException {
      if (var1 != null) {
         this.message.setSubject(var1, Charsets.UTF_8.name());
      }

      return this;
   }

   public MimeMessage getMimeMessage() {
      return this.message;
   }

   private static InternetAddress parseAddress(String var0) throws AddressException {
      return var0 == null ? null : new InternetAddress(var0);
   }

   private static InternetAddress[] parseAddresses(String var0) throws AddressException {
      return var0 == null ? null : InternetAddress.parse(var0, true);
   }
}
