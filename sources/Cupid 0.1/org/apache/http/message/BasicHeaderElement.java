package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public class BasicHeaderElement implements HeaderElement, Cloneable {
  private final String name;
  
  private final String value;
  
  private final NameValuePair[] parameters;
  
  public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
    this.name = (String)Args.notNull(name, "Name");
    this.value = value;
    if (parameters != null) {
      this.parameters = parameters;
    } else {
      this.parameters = new NameValuePair[0];
    } 
  }
  
  public BasicHeaderElement(String name, String value) {
    this(name, value, null);
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public NameValuePair[] getParameters() {
    return (NameValuePair[])this.parameters.clone();
  }
  
  public int getParameterCount() {
    return this.parameters.length;
  }
  
  public NameValuePair getParameter(int index) {
    return this.parameters[index];
  }
  
  public NameValuePair getParameterByName(String name) {
    Args.notNull(name, "Name");
    NameValuePair found = null;
    for (NameValuePair current : this.parameters) {
      if (current.getName().equalsIgnoreCase(name)) {
        found = current;
        break;
      } 
    } 
    return found;
  }
  
  public boolean equals(Object object) {
    if (this == object)
      return true; 
    if (object instanceof HeaderElement) {
      BasicHeaderElement that = (BasicHeaderElement)object;
      return (this.name.equals(that.name) && LangUtils.equals(this.value, that.value) && LangUtils.equals((Object[])this.parameters, (Object[])that.parameters));
    } 
    return false;
  }
  
  public int hashCode() {
    int hash = 17;
    hash = LangUtils.hashCode(hash, this.name);
    hash = LangUtils.hashCode(hash, this.value);
    for (NameValuePair parameter : this.parameters)
      hash = LangUtils.hashCode(hash, parameter); 
    return hash;
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(this.name);
    if (this.value != null) {
      buffer.append("=");
      buffer.append(this.value);
    } 
    for (NameValuePair parameter : this.parameters) {
      buffer.append("; ");
      buffer.append(parameter);
    } 
    return buffer.toString();
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\BasicHeaderElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */