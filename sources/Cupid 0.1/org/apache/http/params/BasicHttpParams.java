package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {
  private static final long serialVersionUID = -7086398485908701455L;
  
  private final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();
  
  public Object getParameter(String name) {
    return this.parameters.get(name);
  }
  
  public HttpParams setParameter(String name, Object value) {
    if (name == null)
      return this; 
    if (value != null) {
      this.parameters.put(name, value);
    } else {
      this.parameters.remove(name);
    } 
    return this;
  }
  
  public boolean removeParameter(String name) {
    if (this.parameters.containsKey(name)) {
      this.parameters.remove(name);
      return true;
    } 
    return false;
  }
  
  public void setParameters(String[] names, Object value) {
    for (String name : names)
      setParameter(name, value); 
  }
  
  public boolean isParameterSet(String name) {
    return (getParameter(name) != null);
  }
  
  public boolean isParameterSetLocally(String name) {
    return (this.parameters.get(name) != null);
  }
  
  public void clear() {
    this.parameters.clear();
  }
  
  public HttpParams copy() {
    try {
      return (HttpParams)clone();
    } catch (CloneNotSupportedException ex) {
      throw new UnsupportedOperationException("Cloning not supported");
    } 
  }
  
  public Object clone() throws CloneNotSupportedException {
    BasicHttpParams clone = (BasicHttpParams)super.clone();
    copyParams(clone);
    return clone;
  }
  
  public void copyParams(HttpParams target) {
    for (Map.Entry<String, Object> me : this.parameters.entrySet())
      target.setParameter(me.getKey(), me.getValue()); 
  }
  
  public Set<String> getNames() {
    return new HashSet<String>(this.parameters.keySet());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\params\BasicHttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */