// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.text.MessageFormat;
import java.util.Locale;
import org.apache.logging.log4j.Logger;

public class MessageFormatMessage implements Message
{
    private static final Logger LOGGER;
    private static final long serialVersionUID = 1L;
    private static final int HASHVAL = 31;
    private String messagePattern;
    private transient Object[] parameters;
    private String[] serializedParameters;
    private transient String formattedMessage;
    private transient Throwable throwable;
    private final Locale locale;
    
    public MessageFormatMessage(final Locale locale, final String messagePattern, final Object... parameters) {
        this.locale = locale;
        this.messagePattern = messagePattern;
        this.parameters = parameters;
        final int length = (parameters == null) ? 0 : parameters.length;
        if (length > 0 && parameters[length - 1] instanceof Throwable) {
            this.throwable = (Throwable)parameters[length - 1];
        }
    }
    
    public MessageFormatMessage(final String messagePattern, final Object... parameters) {
        this(Locale.getDefault(Locale.Category.FORMAT), messagePattern, parameters);
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            this.formattedMessage = this.formatMessage(this.messagePattern, this.parameters);
        }
        return this.formattedMessage;
    }
    
    @Override
    public String getFormat() {
        return this.messagePattern;
    }
    
    @Override
    public Object[] getParameters() {
        if (this.parameters != null) {
            return this.parameters;
        }
        return this.serializedParameters;
    }
    
    protected String formatMessage(final String msgPattern, final Object... args) {
        try {
            final MessageFormat temp = new MessageFormat(msgPattern, this.locale);
            return temp.format(args);
        }
        catch (IllegalFormatException ife) {
            MessageFormatMessage.LOGGER.error("Unable to format msg: " + msgPattern, ife);
            return msgPattern;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MessageFormatMessage that = (MessageFormatMessage)o;
        if (this.messagePattern != null) {
            if (this.messagePattern.equals(that.messagePattern)) {
                return Arrays.equals(this.serializedParameters, that.serializedParameters);
            }
        }
        else if (that.messagePattern == null) {
            return Arrays.equals(this.serializedParameters, that.serializedParameters);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
        result = 31 * result + ((this.serializedParameters != null) ? Arrays.hashCode(this.serializedParameters) : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.getFormattedMessage();
        out.writeUTF(this.formattedMessage);
        out.writeUTF(this.messagePattern);
        final int length = (this.parameters == null) ? 0 : this.parameters.length;
        out.writeInt(length);
        this.serializedParameters = new String[length];
        if (length > 0) {
            for (int i = 0; i < length; ++i) {
                out.writeUTF(this.serializedParameters[i] = String.valueOf(this.parameters[i]));
            }
        }
    }
    
    private void readObject(final ObjectInputStream in) throws IOException {
        this.parameters = null;
        this.throwable = null;
        this.formattedMessage = in.readUTF();
        this.messagePattern = in.readUTF();
        final int length = in.readInt();
        this.serializedParameters = new String[length];
        for (int i = 0; i < length; ++i) {
            this.serializedParameters[i] = in.readUTF();
        }
    }
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
