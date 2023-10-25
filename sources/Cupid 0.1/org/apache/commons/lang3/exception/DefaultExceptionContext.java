package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {
  private static final long serialVersionUID = 20110706L;
  
  private final List<Pair<String, Object>> contextValues = new ArrayList<Pair<String, Object>>();
  
  public DefaultExceptionContext addContextValue(String label, Object value) {
    this.contextValues.add(new ImmutablePair(label, value));
    return this;
  }
  
  public DefaultExceptionContext setContextValue(String label, Object value) {
    for (Iterator<Pair<String, Object>> iter = this.contextValues.iterator(); iter.hasNext(); ) {
      Pair<String, Object> p = iter.next();
      if (StringUtils.equals(label, (CharSequence)p.getKey()))
        iter.remove(); 
    } 
    addContextValue(label, value);
    return this;
  }
  
  public List<Object> getContextValues(String label) {
    List<Object> values = new ArrayList();
    for (Pair<String, Object> pair : this.contextValues) {
      if (StringUtils.equals(label, (CharSequence)pair.getKey()))
        values.add(pair.getValue()); 
    } 
    return values;
  }
  
  public Object getFirstContextValue(String label) {
    for (Pair<String, Object> pair : this.contextValues) {
      if (StringUtils.equals(label, (CharSequence)pair.getKey()))
        return pair.getValue(); 
    } 
    return null;
  }
  
  public Set<String> getContextLabels() {
    Set<String> labels = new HashSet<String>();
    for (Pair<String, Object> pair : this.contextValues)
      labels.add(pair.getKey()); 
    return labels;
  }
  
  public List<Pair<String, Object>> getContextEntries() {
    return this.contextValues;
  }
  
  public String getFormattedExceptionMessage(String baseMessage) {
    StringBuilder buffer = new StringBuilder(256);
    if (baseMessage != null)
      buffer.append(baseMessage); 
    if (this.contextValues.size() > 0) {
      if (buffer.length() > 0)
        buffer.append('\n'); 
      buffer.append("Exception Context:\n");
      int i = 0;
      for (Pair<String, Object> pair : this.contextValues) {
        buffer.append("\t[");
        buffer.append(++i);
        buffer.append(':');
        buffer.append((String)pair.getKey());
        buffer.append("=");
        Object value = pair.getValue();
        if (value == null) {
          buffer.append("null");
        } else {
          String str;
          try {
            str = value.toString();
          } catch (Exception e) {
            str = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace(e);
          } 
          buffer.append(str);
        } 
        buffer.append("]\n");
      } 
      buffer.append("---------------------------------");
    } 
    return buffer.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\exception\DefaultExceptionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */