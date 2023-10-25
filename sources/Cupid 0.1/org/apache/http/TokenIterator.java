package org.apache.http;

import java.util.Iterator;

public interface TokenIterator extends Iterator<Object> {
  boolean hasNext();
  
  String nextToken();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\TokenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */