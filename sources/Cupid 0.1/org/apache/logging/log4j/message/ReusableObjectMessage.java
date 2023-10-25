package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@PerformanceSensitive({"allocation"})
public class ReusableObjectMessage implements ReusableMessage, ParameterVisitable, Clearable {
  private static final long serialVersionUID = 6922476812535519960L;
  
  private transient Object obj;
  
  public void set(Object object) {
    this.obj = object;
  }
  
  public String getFormattedMessage() {
    return String.valueOf(this.obj);
  }
  
  public void formatTo(StringBuilder buffer) {
    StringBuilders.appendValue(buffer, this.obj);
  }
  
  public String getFormat() {
    return (this.obj instanceof String) ? (String)this.obj : null;
  }
  
  public Object getParameter() {
    return this.obj;
  }
  
  public Object[] getParameters() {
    return new Object[] { this.obj };
  }
  
  public String toString() {
    return getFormattedMessage();
  }
  
  public Throwable getThrowable() {
    return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
  }
  
  public Object[] swapParameters(Object[] emptyReplacement) {
    if (emptyReplacement.length == 0) {
      Object[] params = new Object[10];
      params[0] = this.obj;
      return params;
    } 
    emptyReplacement[0] = this.obj;
    return emptyReplacement;
  }
  
  public short getParameterCount() {
    return 1;
  }
  
  public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
    action.accept(this.obj, 0, state);
  }
  
  public Message memento() {
    return new ObjectMessage(this.obj);
  }
  
  public void clear() {
    this.obj = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ReusableObjectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */