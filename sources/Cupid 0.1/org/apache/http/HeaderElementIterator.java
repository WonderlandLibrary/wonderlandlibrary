package org.apache.http;

import java.util.Iterator;

public interface HeaderElementIterator extends Iterator<Object> {
  boolean hasNext();
  
  HeaderElement nextElement();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HeaderElementIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */