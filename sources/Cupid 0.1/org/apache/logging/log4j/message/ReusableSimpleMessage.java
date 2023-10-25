package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public class ReusableSimpleMessage implements ReusableMessage, CharSequence, ParameterVisitable, Clearable {
  private static final long serialVersionUID = -9199974506498249809L;
  
  private CharSequence charSequence;
  
  public void set(String message) {
    this.charSequence = message;
  }
  
  public void set(CharSequence charSequence) {
    this.charSequence = charSequence;
  }
  
  public String getFormattedMessage() {
    return String.valueOf(this.charSequence);
  }
  
  public String getFormat() {
    return (this.charSequence instanceof String) ? (String)this.charSequence : null;
  }
  
  public Object[] getParameters() {
    return Constants.EMPTY_OBJECT_ARRAY;
  }
  
  public Throwable getThrowable() {
    return null;
  }
  
  public void formatTo(StringBuilder buffer) {
    buffer.append(this.charSequence);
  }
  
  public Object[] swapParameters(Object[] emptyReplacement) {
    return emptyReplacement;
  }
  
  public short getParameterCount() {
    return 0;
  }
  
  public <S> void forEachParameter(ParameterConsumer<S> action, S state) {}
  
  public Message memento() {
    return new SimpleMessage(this.charSequence);
  }
  
  public int length() {
    return (this.charSequence == null) ? 0 : this.charSequence.length();
  }
  
  public char charAt(int index) {
    return this.charSequence.charAt(index);
  }
  
  public CharSequence subSequence(int start, int end) {
    return this.charSequence.subSequence(start, end);
  }
  
  public void clear() {
    this.charSequence = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ReusableSimpleMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */