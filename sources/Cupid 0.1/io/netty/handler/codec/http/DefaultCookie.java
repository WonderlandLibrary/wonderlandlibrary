package io.netty.handler.codec.http;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DefaultCookie implements Cookie {
  private final String name;
  
  private String value;
  
  private String domain;
  
  private String path;
  
  private String comment;
  
  private String commentUrl;
  
  private boolean discard;
  
  private Set<Integer> ports = Collections.emptySet();
  
  private Set<Integer> unmodifiablePorts = this.ports;
  
  private long maxAge = Long.MIN_VALUE;
  
  private int version;
  
  private boolean secure;
  
  private boolean httpOnly;
  
  public DefaultCookie(String name, String value) {
    if (name == null)
      throw new NullPointerException("name"); 
    name = name.trim();
    if (name.isEmpty())
      throw new IllegalArgumentException("empty name"); 
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (c > '')
        throw new IllegalArgumentException("name contains non-ascii character: " + name); 
      switch (c) {
        case '\t':
        case '\n':
        case '\013':
        case '\f':
        case '\r':
        case ' ':
        case ',':
        case ';':
        case '=':
          throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + name);
      } 
    } 
    if (name.charAt(0) == '$')
      throw new IllegalArgumentException("name starting with '$' not allowed: " + name); 
    this.name = name;
    setValue(value);
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String value) {
    if (value == null)
      throw new NullPointerException("value"); 
    this.value = value;
  }
  
  public String getDomain() {
    return this.domain;
  }
  
  public void setDomain(String domain) {
    this.domain = validateValue("domain", domain);
  }
  
  public String getPath() {
    return this.path;
  }
  
  public void setPath(String path) {
    this.path = validateValue("path", path);
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public void setComment(String comment) {
    this.comment = validateValue("comment", comment);
  }
  
  public String getCommentUrl() {
    return this.commentUrl;
  }
  
  public void setCommentUrl(String commentUrl) {
    this.commentUrl = validateValue("commentUrl", commentUrl);
  }
  
  public boolean isDiscard() {
    return this.discard;
  }
  
  public void setDiscard(boolean discard) {
    this.discard = discard;
  }
  
  public Set<Integer> getPorts() {
    if (this.unmodifiablePorts == null)
      this.unmodifiablePorts = Collections.unmodifiableSet(this.ports); 
    return this.unmodifiablePorts;
  }
  
  public void setPorts(int... ports) {
    if (ports == null)
      throw new NullPointerException("ports"); 
    int[] portsCopy = (int[])ports.clone();
    if (portsCopy.length == 0) {
      this.unmodifiablePorts = this.ports = Collections.emptySet();
    } else {
      Set<Integer> newPorts = new TreeSet<Integer>();
      for (int p : portsCopy) {
        if (p <= 0 || p > 65535)
          throw new IllegalArgumentException("port out of range: " + p); 
        newPorts.add(Integer.valueOf(p));
      } 
      this.ports = newPorts;
      this.unmodifiablePorts = null;
    } 
  }
  
  public void setPorts(Iterable<Integer> ports) {
    Set<Integer> newPorts = new TreeSet<Integer>();
    for (Iterator<Integer> i$ = ports.iterator(); i$.hasNext(); ) {
      int p = ((Integer)i$.next()).intValue();
      if (p <= 0 || p > 65535)
        throw new IllegalArgumentException("port out of range: " + p); 
      newPorts.add(Integer.valueOf(p));
    } 
    if (newPorts.isEmpty()) {
      this.unmodifiablePorts = this.ports = Collections.emptySet();
    } else {
      this.ports = newPorts;
      this.unmodifiablePorts = null;
    } 
  }
  
  public long getMaxAge() {
    return this.maxAge;
  }
  
  public void setMaxAge(long maxAge) {
    this.maxAge = maxAge;
  }
  
  public int getVersion() {
    return this.version;
  }
  
  public void setVersion(int version) {
    this.version = version;
  }
  
  public boolean isSecure() {
    return this.secure;
  }
  
  public void setSecure(boolean secure) {
    this.secure = secure;
  }
  
  public boolean isHttpOnly() {
    return this.httpOnly;
  }
  
  public void setHttpOnly(boolean httpOnly) {
    this.httpOnly = httpOnly;
  }
  
  public int hashCode() {
    return getName().hashCode();
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Cookie))
      return false; 
    Cookie that = (Cookie)o;
    if (!getName().equalsIgnoreCase(that.getName()))
      return false; 
    if (getPath() == null) {
      if (that.getPath() != null)
        return false; 
    } else {
      if (that.getPath() == null)
        return false; 
      if (!getPath().equals(that.getPath()))
        return false; 
    } 
    if (getDomain() == null) {
      if (that.getDomain() != null)
        return false; 
    } else {
      if (that.getDomain() == null)
        return false; 
      return getDomain().equalsIgnoreCase(that.getDomain());
    } 
    return true;
  }
  
  public int compareTo(Cookie c) {
    int v = getName().compareToIgnoreCase(c.getName());
    if (v != 0)
      return v; 
    if (getPath() == null) {
      if (c.getPath() != null)
        return -1; 
    } else {
      if (c.getPath() == null)
        return 1; 
      v = getPath().compareTo(c.getPath());
      if (v != 0)
        return v; 
    } 
    if (getDomain() == null) {
      if (c.getDomain() != null)
        return -1; 
    } else {
      if (c.getDomain() == null)
        return 1; 
      v = getDomain().compareToIgnoreCase(c.getDomain());
      return v;
    } 
    return 0;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(getName());
    buf.append('=');
    buf.append(getValue());
    if (getDomain() != null) {
      buf.append(", domain=");
      buf.append(getDomain());
    } 
    if (getPath() != null) {
      buf.append(", path=");
      buf.append(getPath());
    } 
    if (getComment() != null) {
      buf.append(", comment=");
      buf.append(getComment());
    } 
    if (getMaxAge() >= 0L) {
      buf.append(", maxAge=");
      buf.append(getMaxAge());
      buf.append('s');
    } 
    if (isSecure())
      buf.append(", secure"); 
    if (isHttpOnly())
      buf.append(", HTTPOnly"); 
    return buf.toString();
  }
  
  private static String validateValue(String name, String value) {
    if (value == null)
      return null; 
    value = value.trim();
    if (value.isEmpty())
      return null; 
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\n':
        case '\013':
        case '\f':
        case '\r':
        case ';':
          throw new IllegalArgumentException(name + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + value + ')');
      } 
    } 
    return value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultCookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */