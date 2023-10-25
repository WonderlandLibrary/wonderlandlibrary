package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class SimpleMessage implements Message, StringBuilderFormattable, CharSequence {
  private static final long serialVersionUID = -8398002534962715992L;
  
  private String message;
  
  private transient CharSequence charSequence;
  
  public SimpleMessage() {
    this((String)null);
  }
  
  public SimpleMessage(String message) {
    this.message = message;
    this.charSequence = message;
  }
  
  public SimpleMessage(CharSequence charSequence) {
    this.charSequence = charSequence;
  }
  
  public String getFormattedMessage() {
    return this.message = (this.message == null) ? String.valueOf(this.charSequence) : this.message;
  }
  
  public void formatTo(StringBuilder buffer) {
    buffer.append((this.message != null) ? this.message : this.charSequence);
  }
  
  public String getFormat() {
    return this.message;
  }
  
  public Object[] getParameters() {
    return null;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    SimpleMessage that = (SimpleMessage)o;
    if ((this.charSequence != null) ? !this.charSequence.equals(that.charSequence) : (that.charSequence != null))
      return false; 
  }
  
  public int hashCode() {
    return (this.charSequence != null) ? this.charSequence.hashCode() : 0;
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  public Throwable getThrowable() {
    return null;
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
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    getFormattedMessage();
    out.defaultWriteObject();
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.charSequence = this.message;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\SimpleMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */