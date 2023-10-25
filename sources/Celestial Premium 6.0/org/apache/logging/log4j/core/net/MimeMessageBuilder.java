/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.mail.Address
 *  javax.mail.Message$RecipientType
 *  javax.mail.MessagingException
 *  javax.mail.Session
 *  javax.mail.internet.AddressException
 *  javax.mail.internet.InternetAddress
 *  javax.mail.internet.MimeMessage
 */
package org.apache.logging.log4j.core.net;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.helpers.Charsets;

public class MimeMessageBuilder {
    private final MimeMessage message;

    public MimeMessageBuilder(Session session) {
        this.message = new MimeMessage(session);
    }

    public MimeMessageBuilder setFrom(String from) throws MessagingException {
        InternetAddress address = MimeMessageBuilder.parseAddress(from);
        if (null != address) {
            this.message.setFrom((Address)address);
        } else {
            try {
                this.message.setFrom();
            }
            catch (Exception ex) {
                this.message.setFrom((Address)((InternetAddress)null));
            }
        }
        return this;
    }

    public MimeMessageBuilder setReplyTo(String replyTo) throws MessagingException {
        InternetAddress[] addresses = MimeMessageBuilder.parseAddresses(replyTo);
        if (null != addresses) {
            this.message.setReplyTo((Address[])addresses);
        }
        return this;
    }

    public MimeMessageBuilder setRecipients(Message.RecipientType recipientType, String recipients) throws MessagingException {
        InternetAddress[] addresses = MimeMessageBuilder.parseAddresses(recipients);
        if (null != addresses) {
            this.message.setRecipients(recipientType, (Address[])addresses);
        }
        return this;
    }

    public MimeMessageBuilder setSubject(String subject) throws MessagingException {
        if (subject != null) {
            this.message.setSubject(subject, Charsets.UTF_8.name());
        }
        return this;
    }

    public MimeMessage getMimeMessage() {
        return this.message;
    }

    private static InternetAddress parseAddress(String address) throws AddressException {
        return address == null ? null : new InternetAddress(address);
    }

    private static InternetAddress[] parseAddresses(String addresses) throws AddressException {
        return addresses == null ? null : InternetAddress.parse((String)addresses, (boolean)true);
    }
}

