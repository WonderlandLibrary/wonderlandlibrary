package org.apache.logging.log4j.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class StructuredDataCollectionMessage implements StringBuilderFormattable, MessageCollectionMessage<StructuredDataMessage> {
  private static final long serialVersionUID = 5725337076388822924L;
  
  private final List<StructuredDataMessage> structuredDataMessageList;
  
  public StructuredDataCollectionMessage(List<StructuredDataMessage> messages) {
    this.structuredDataMessageList = messages;
  }
  
  public Iterator<StructuredDataMessage> iterator() {
    return this.structuredDataMessageList.iterator();
  }
  
  public String getFormattedMessage() {
    StringBuilder sb = new StringBuilder();
    formatTo(sb);
    return sb.toString();
  }
  
  public String getFormat() {
    StringBuilder sb = new StringBuilder();
    for (StructuredDataMessage msg : this.structuredDataMessageList) {
      if (msg.getFormat() != null) {
        if (sb.length() > 0)
          sb.append(", "); 
        sb.append(msg.getFormat());
      } 
    } 
    return sb.toString();
  }
  
  public void formatTo(StringBuilder buffer) {
    for (StructuredDataMessage msg : this.structuredDataMessageList)
      msg.formatTo(buffer); 
  }
  
  public Object[] getParameters() {
    List<Object[]> objectList = new ArrayList();
    int count = 0;
    for (StructuredDataMessage msg : this.structuredDataMessageList) {
      Object[] arrayOfObject = msg.getParameters();
      if (arrayOfObject != null) {
        objectList.add(arrayOfObject);
        count += arrayOfObject.length;
      } 
    } 
    Object[] objects = new Object[count];
    int index = 0;
    for (Object[] objs : objectList) {
      for (Object obj : objs)
        objects[index++] = obj; 
    } 
    return objects;
  }
  
  public Throwable getThrowable() {
    for (StructuredDataMessage msg : this.structuredDataMessageList) {
      Throwable t = msg.getThrowable();
      if (t != null)
        return t; 
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\StructuredDataCollectionMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */