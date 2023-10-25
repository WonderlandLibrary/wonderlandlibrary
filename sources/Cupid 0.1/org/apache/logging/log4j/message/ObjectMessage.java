package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;

public class ObjectMessage implements Message, StringBuilderFormattable {
  private static final long serialVersionUID = -5903272448334166185L;
  
  private transient Object obj;
  
  private transient String objectString;
  
  public ObjectMessage(Object obj) {
    this.obj = (obj == null) ? "null" : obj;
  }
  
  public String getFormattedMessage() {
    if (this.objectString == null)
      this.objectString = String.valueOf(this.obj); 
    return this.objectString;
  }
  
  public void formatTo(StringBuilder buffer) {
    if (this.objectString != null) {
      buffer.append(this.objectString);
    } else {
      StringBuilders.appendValue(buffer, this.obj);
    } 
  }
  
  public String getFormat() {
    return getFormattedMessage();
  }
  
  public Object getParameter() {
    return this.obj;
  }
  
  public Object[] getParameters() {
    return new Object[] { this.obj };
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    ObjectMessage that = (ObjectMessage)o;
    return (this.obj == null) ? ((that.obj == null)) : equalObjectsOrStrings(this.obj, that.obj);
  }
  
  private boolean equalObjectsOrStrings(Object left, Object right) {
    return (left.equals(right) || String.valueOf(left).equals(String.valueOf(right)));
  }
  
  public int hashCode() {
    return (this.obj != null) ? this.obj.hashCode() : 0;
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    if (this.obj instanceof java.io.Serializable) {
      out.writeObject(this.obj);
    } else {
      out.writeObject(String.valueOf(this.obj));
    } 
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.obj = in.readObject();
  }
  
  public Throwable getThrowable() {
    return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ObjectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */