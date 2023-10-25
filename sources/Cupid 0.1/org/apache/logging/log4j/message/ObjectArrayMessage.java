package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.apache.logging.log4j.util.Constants;

public final class ObjectArrayMessage implements Message {
  private static final long serialVersionUID = -5903272448334166185L;
  
  private transient Object[] array;
  
  private transient String arrayString;
  
  public ObjectArrayMessage(Object... obj) {
    this.array = (obj == null) ? Constants.EMPTY_OBJECT_ARRAY : obj;
  }
  
  private boolean equalObjectsOrStrings(Object[] left, Object[] right) {
    return (Arrays.equals(left, right) || Arrays.toString(left).equals(Arrays.toString(right)));
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    ObjectArrayMessage that = (ObjectArrayMessage)o;
    return (this.array == null) ? ((that.array == null)) : equalObjectsOrStrings(this.array, that.array);
  }
  
  public String getFormat() {
    return getFormattedMessage();
  }
  
  public String getFormattedMessage() {
    if (this.arrayString == null)
      this.arrayString = Arrays.toString(this.array); 
    return this.arrayString;
  }
  
  public Object[] getParameters() {
    return this.array;
  }
  
  public Throwable getThrowable() {
    return null;
  }
  
  public int hashCode() {
    return Arrays.hashCode(this.array);
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    this.array = (Object[])in.readObject();
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    out.writeObject(this.array);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ObjectArrayMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */