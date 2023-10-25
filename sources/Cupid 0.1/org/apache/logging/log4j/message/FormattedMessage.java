package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormattedMessage implements Message {
  private static final long serialVersionUID = -665975803997290697L;
  
  private static final int HASHVAL = 31;
  
  private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
  
  private static final Pattern MSG_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
  
  private String messagePattern;
  
  private transient Object[] argArray;
  
  private String[] stringArgs;
  
  private transient String formattedMessage;
  
  private final Throwable throwable;
  
  private Message message;
  
  private final Locale locale;
  
  public FormattedMessage(Locale locale, String messagePattern, Object arg) {
    this(locale, messagePattern, new Object[] { arg }, (Throwable)null);
  }
  
  public FormattedMessage(Locale locale, String messagePattern, Object arg1, Object arg2) {
    this(locale, messagePattern, new Object[] { arg1, arg2 });
  }
  
  public FormattedMessage(Locale locale, String messagePattern, Object... arguments) {
    this(locale, messagePattern, arguments, (Throwable)null);
  }
  
  public FormattedMessage(Locale locale, String messagePattern, Object[] arguments, Throwable throwable) {
    this.locale = locale;
    this.messagePattern = messagePattern;
    this.argArray = arguments;
    this.throwable = throwable;
  }
  
  public FormattedMessage(String messagePattern, Object arg) {
    this(messagePattern, new Object[] { arg }, (Throwable)null);
  }
  
  public FormattedMessage(String messagePattern, Object arg1, Object arg2) {
    this(messagePattern, new Object[] { arg1, arg2 });
  }
  
  public FormattedMessage(String messagePattern, Object... arguments) {
    this(messagePattern, arguments, (Throwable)null);
  }
  
  public FormattedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
    this.locale = Locale.getDefault(Locale.Category.FORMAT);
    this.messagePattern = messagePattern;
    this.argArray = arguments;
    this.throwable = throwable;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    FormattedMessage that = (FormattedMessage)o;
    if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null))
      return false; 
    if (!Arrays.equals((Object[])this.stringArgs, (Object[])that.stringArgs))
      return false; 
    return true;
  }
  
  public String getFormat() {
    return this.messagePattern;
  }
  
  public String getFormattedMessage() {
    if (this.formattedMessage == null) {
      if (this.message == null)
        this.message = getMessage(this.messagePattern, this.argArray, this.throwable); 
      this.formattedMessage = this.message.getFormattedMessage();
    } 
    return this.formattedMessage;
  }
  
  protected Message getMessage(String msgPattern, Object[] args, Throwable aThrowable) {
    try {
      MessageFormat format = new MessageFormat(msgPattern);
      Format[] formats = format.getFormats();
      if (formats != null && formats.length > 0)
        return new MessageFormatMessage(this.locale, msgPattern, args); 
    } catch (Exception exception) {}
    try {
      if (MSG_PATTERN.matcher(msgPattern).find())
        return new StringFormattedMessage(this.locale, msgPattern, args); 
    } catch (Exception exception) {}
    return new ParameterizedMessage(msgPattern, args, aThrowable);
  }
  
  public Object[] getParameters() {
    if (this.argArray != null)
      return this.argArray; 
    return (Object[])this.stringArgs;
  }
  
  public Throwable getThrowable() {
    if (this.throwable != null)
      return this.throwable; 
    if (this.message == null)
      this.message = getMessage(this.messagePattern, this.argArray, null); 
    return this.message.getThrowable();
  }
  
  public int hashCode() {
    int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
    result = 31 * result + ((this.stringArgs != null) ? Arrays.hashCode((Object[])this.stringArgs) : 0);
    return result;
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.formattedMessage = in.readUTF();
    this.messagePattern = in.readUTF();
    int length = in.readInt();
    this.stringArgs = new String[length];
    for (int i = 0; i < length; i++)
      this.stringArgs[i] = in.readUTF(); 
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    getFormattedMessage();
    out.writeUTF(this.formattedMessage);
    out.writeUTF(this.messagePattern);
    out.writeInt(this.argArray.length);
    this.stringArgs = new String[this.argArray.length];
    int i = 0;
    for (Object obj : this.argArray) {
      String string = String.valueOf(obj);
      this.stringArgs[i] = string;
      out.writeUTF(string);
      i++;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\FormattedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */