package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeader implements Header, Cloneable, Serializable {
  private static final long serialVersionUID = -5427236326487562174L;
  
  private final String name;
  
  private final String value;
  
  public BasicHeader(String name, String value) {
    this.name = (String)Args.notNull(name, "Name");
    this.value = value;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public String toString() {
    return BasicLineFormatter.INSTANCE.formatHeader((CharArrayBuffer)null, this).toString();
  }
  
  public HeaderElement[] getElements() throws ParseException {
    if (this.value != null)
      return BasicHeaderValueParser.parseElements(this.value, (HeaderValueParser)null); 
    return new HeaderElement[0];
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\BasicHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */