package org.apache.logging.log4j.message;

import java.util.Arrays;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;

public class ParameterizedMessage implements Message, StringBuilderFormattable {
  private static final int DEFAULT_STRING_BUILDER_SIZE = 255;
  
  public static final String RECURSION_PREFIX = "[...";
  
  public static final String RECURSION_SUFFIX = "...]";
  
  public static final String ERROR_PREFIX = "[!!!";
  
  public static final String ERROR_SEPARATOR = "=>";
  
  public static final String ERROR_MSG_SEPARATOR = ":";
  
  public static final String ERROR_SUFFIX = "!!!]";
  
  private static final long serialVersionUID = -665975803997290697L;
  
  private static final int HASHVAL = 31;
  
  private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
  
  private String messagePattern;
  
  private transient Object[] argArray;
  
  private String formattedMessage;
  
  private transient Throwable throwable;
  
  private int[] indices;
  
  private int usedCount;
  
  @Deprecated
  public ParameterizedMessage(String messagePattern, String[] arguments, Throwable throwable) {
    this.argArray = (Object[])arguments;
    this.throwable = throwable;
    init(messagePattern);
  }
  
  public ParameterizedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
    this.argArray = arguments;
    this.throwable = throwable;
    init(messagePattern);
  }
  
  public ParameterizedMessage(String messagePattern, Object... arguments) {
    this.argArray = arguments;
    init(messagePattern);
  }
  
  public ParameterizedMessage(String messagePattern, Object arg) {
    this(messagePattern, new Object[] { arg });
  }
  
  public ParameterizedMessage(String messagePattern, Object arg0, Object arg1) {
    this(messagePattern, new Object[] { arg0, arg1 });
  }
  
  private void init(String messagePattern) {
    this.messagePattern = messagePattern;
    int len = Math.max(1, (messagePattern == null) ? 0 : (messagePattern.length() >> 1));
    this.indices = new int[len];
    int placeholders = ParameterFormatter.countArgumentPlaceholders2(messagePattern, this.indices);
    initThrowable(this.argArray, placeholders);
    this.usedCount = Math.min(placeholders, (this.argArray == null) ? 0 : this.argArray.length);
  }
  
  private void initThrowable(Object[] params, int usedParams) {
    if (params != null) {
      int argCount = params.length;
      if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable)
        this.throwable = (Throwable)params[argCount - 1]; 
    } 
  }
  
  public String getFormat() {
    return this.messagePattern;
  }
  
  public Object[] getParameters() {
    return this.argArray;
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
  
  public String getFormattedMessage() {
    if (this.formattedMessage == null) {
      StringBuilder buffer = getThreadLocalStringBuilder();
      formatTo(buffer);
      this.formattedMessage = buffer.toString();
      StringBuilders.trimToMaxSize(buffer, Constants.MAX_REUSABLE_MESSAGE_SIZE);
    } 
    return this.formattedMessage;
  }
  
  private static StringBuilder getThreadLocalStringBuilder() {
    StringBuilder buffer = threadLocalStringBuilder.get();
    if (buffer == null) {
      buffer = new StringBuilder(255);
      threadLocalStringBuilder.set(buffer);
    } 
    buffer.setLength(0);
    return buffer;
  }
  
  public void formatTo(StringBuilder buffer) {
    if (this.formattedMessage != null) {
      buffer.append(this.formattedMessage);
    } else if (this.indices[0] < 0) {
      ParameterFormatter.formatMessage(buffer, this.messagePattern, this.argArray, this.usedCount);
    } else {
      ParameterFormatter.formatMessage2(buffer, this.messagePattern, this.argArray, this.usedCount, this.indices);
    } 
  }
  
  public static String format(String messagePattern, Object[] arguments) {
    return ParameterFormatter.format(messagePattern, arguments);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    ParameterizedMessage that = (ParameterizedMessage)o;
    if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null))
      return false; 
    if (!Arrays.equals(this.argArray, that.argArray))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
    result = 31 * result + ((this.argArray != null) ? Arrays.hashCode(this.argArray) : 0);
    return result;
  }
  
  public static int countArgumentPlaceholders(String messagePattern) {
    return ParameterFormatter.countArgumentPlaceholders(messagePattern);
  }
  
  public static String deepToString(Object o) {
    return ParameterFormatter.deepToString(o);
  }
  
  public static String identityToString(Object obj) {
    return ParameterFormatter.identityToString(obj);
  }
  
  public String toString() {
    return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + 
      Arrays.toString(this.argArray) + ", throwable=" + this.throwable + ']';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ParameterizedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */