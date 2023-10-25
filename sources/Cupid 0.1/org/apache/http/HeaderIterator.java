package org.apache.http;

import java.util.Iterator;

public interface HeaderIterator extends Iterator<Object> {
  boolean hasNext();
  
  Header nextHeader();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HeaderIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */