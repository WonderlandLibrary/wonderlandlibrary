package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class HeaderGroup implements Cloneable, Serializable {
  private static final long serialVersionUID = 2608834160639271617L;
  
  private final List<Header> headers = new ArrayList<Header>(16);
  
  public void clear() {
    this.headers.clear();
  }
  
  public void addHeader(Header header) {
    if (header == null)
      return; 
    this.headers.add(header);
  }
  
  public void removeHeader(Header header) {
    if (header == null)
      return; 
    this.headers.remove(header);
  }
  
  public void updateHeader(Header header) {
    if (header == null)
      return; 
    for (int i = 0; i < this.headers.size(); i++) {
      Header current = this.headers.get(i);
      if (current.getName().equalsIgnoreCase(header.getName())) {
        this.headers.set(i, header);
        return;
      } 
    } 
    this.headers.add(header);
  }
  
  public void setHeaders(Header[] headers) {
    clear();
    if (headers == null)
      return; 
    Collections.addAll(this.headers, headers);
  }
  
  public Header getCondensedHeader(String name) {
    Header[] hdrs = getHeaders(name);
    if (hdrs.length == 0)
      return null; 
    if (hdrs.length == 1)
      return hdrs[0]; 
    CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
    valueBuffer.append(hdrs[0].getValue());
    for (int i = 1; i < hdrs.length; i++) {
      valueBuffer.append(", ");
      valueBuffer.append(hdrs[i].getValue());
    } 
    return new BasicHeader(name.toLowerCase(Locale.ENGLISH), valueBuffer.toString());
  }
  
  public Header[] getHeaders(String name) {
    List<Header> headersFound = new ArrayList<Header>();
    for (int i = 0; i < this.headers.size(); i++) {
      Header header = this.headers.get(i);
      if (header.getName().equalsIgnoreCase(name))
        headersFound.add(header); 
    } 
    return headersFound.<Header>toArray(new Header[headersFound.size()]);
  }
  
  public Header getFirstHeader(String name) {
    for (int i = 0; i < this.headers.size(); i++) {
      Header header = this.headers.get(i);
      if (header.getName().equalsIgnoreCase(name))
        return header; 
    } 
    return null;
  }
  
  public Header getLastHeader(String name) {
    for (int i = this.headers.size() - 1; i >= 0; i--) {
      Header header = this.headers.get(i);
      if (header.getName().equalsIgnoreCase(name))
        return header; 
    } 
    return null;
  }
  
  public Header[] getAllHeaders() {
    return this.headers.<Header>toArray(new Header[this.headers.size()]);
  }
  
  public boolean containsHeader(String name) {
    for (int i = 0; i < this.headers.size(); i++) {
      Header header = this.headers.get(i);
      if (header.getName().equalsIgnoreCase(name))
        return true; 
    } 
    return false;
  }
  
  public HeaderIterator iterator() {
    return new BasicListHeaderIterator(this.headers, null);
  }
  
  public HeaderIterator iterator(String name) {
    return new BasicListHeaderIterator(this.headers, name);
  }
  
  public HeaderGroup copy() {
    HeaderGroup clone = new HeaderGroup();
    clone.headers.addAll(this.headers);
    return clone;
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public String toString() {
    return this.headers.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\HeaderGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */