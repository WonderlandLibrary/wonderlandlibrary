package org.apache.logging.log4j.core.net;

public enum Protocol {
  TCP, SSL, UDP;
  
  public boolean isEqual(String name) {
    return name().equalsIgnoreCase(name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\Protocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */