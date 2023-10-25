package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Locale;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class MessageFormatMessage implements Message {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final long serialVersionUID = 1L;
  
  private static final int HASHVAL = 31;
  
  private String messagePattern;
  
  private transient Object[] parameters;
  
  private String[] serializedParameters;
  
  private transient String formattedMessage;
  
  private transient Throwable throwable;
  
  private final Locale locale;
  
  public MessageFormatMessage(Locale locale, String messagePattern, Object... parameters) {
    this.locale = locale;
    this.messagePattern = messagePattern;
    this.parameters = parameters;
    int length = (parameters == null) ? 0 : parameters.length;
    if (length > 0 && parameters[length - 1] instanceof Throwable)
      this.throwable = (Throwable)parameters[length - 1]; 
  }
  
  public MessageFormatMessage(String messagePattern, Object... parameters) {
    this(Locale.getDefault(Locale.Category.FORMAT), messagePattern, parameters);
  }
  
  public String getFormattedMessage() {
    if (this.formattedMessage == null)
      this.formattedMessage = formatMessage(this.messagePattern, this.parameters); 
    return this.formattedMessage;
  }
  
  public String getFormat() {
    return this.messagePattern;
  }
  
  public Object[] getParameters() {
    if (this.parameters != null)
      return this.parameters; 
    return (Object[])this.serializedParameters;
  }
  
  protected String formatMessage(String msgPattern, Object... args) {
    try {
      MessageFormat temp = new MessageFormat(msgPattern, this.locale);
      return temp.format(args);
    } catch (IllegalFormatException ife) {
      LOGGER.error("Unable to format msg: " + msgPattern, ife);
      return msgPattern;
    } 
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    MessageFormatMessage that = (MessageFormatMessage)o;
    if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null))
      return false; 
    return Arrays.equals((Object[])this.serializedParameters, (Object[])that.serializedParameters);
  }
  
  public int hashCode() {
    int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
    result = 31 * result + ((this.serializedParameters != null) ? Arrays.hashCode((Object[])this.serializedParameters) : 0);
    return result;
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    getFormattedMessage();
    out.writeUTF(this.formattedMessage);
    out.writeUTF(this.messagePattern);
    int length = (this.parameters == null) ? 0 : this.parameters.length;
    out.writeInt(length);
    this.serializedParameters = new String[length];
    if (length > 0)
      for (int i = 0; i < length; i++) {
        this.serializedParameters[i] = String.valueOf(this.parameters[i]);
        out.writeUTF(this.serializedParameters[i]);
      }  
  }
  
  private void readObject(ObjectInputStream in) throws IOException {
    this.parameters = null;
    this.throwable = null;
    this.formattedMessage = in.readUTF();
    this.messagePattern = in.readUTF();
    int length = in.readInt();
    this.serializedParameters = new String[length];
    for (int i = 0; i < length; i++)
      this.serializedParameters[i] = in.readUTF(); 
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\MessageFormatMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */