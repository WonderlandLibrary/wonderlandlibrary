package org.apache.http.message;

import java.util.List;
import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@NotThreadSafe
public class BasicListHeaderIterator implements HeaderIterator {
  protected final List<Header> allHeaders;
  
  protected int currentIndex;
  
  protected int lastIndex;
  
  protected String headerName;
  
  public BasicListHeaderIterator(List<Header> headers, String name) {
    this.allHeaders = (List<Header>)Args.notNull(headers, "Header list");
    this.headerName = name;
    this.currentIndex = findNext(-1);
    this.lastIndex = -1;
  }
  
  protected int findNext(int pos) {
    int from = pos;
    if (from < -1)
      return -1; 
    int to = this.allHeaders.size() - 1;
    boolean found = false;
    while (!found && from < to) {
      from++;
      found = filterHeader(from);
    } 
    return found ? from : -1;
  }
  
  protected boolean filterHeader(int index) {
    if (this.headerName == null)
      return true; 
    String name = ((Header)this.allHeaders.get(index)).getName();
    return this.headerName.equalsIgnoreCase(name);
  }
  
  public boolean hasNext() {
    return (this.currentIndex >= 0);
  }
  
  public Header nextHeader() throws NoSuchElementException {
    int current = this.currentIndex;
    if (current < 0)
      throw new NoSuchElementException("Iteration already finished."); 
    this.lastIndex = current;
    this.currentIndex = findNext(current);
    return this.allHeaders.get(current);
  }
  
  public final Object next() throws NoSuchElementException {
    return nextHeader();
  }
  
  public void remove() throws UnsupportedOperationException {
    Asserts.check((this.lastIndex >= 0), "No header to remove");
    this.allHeaders.remove(this.lastIndex);
    this.lastIndex = -1;
    this.currentIndex--;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\BasicListHeaderIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */