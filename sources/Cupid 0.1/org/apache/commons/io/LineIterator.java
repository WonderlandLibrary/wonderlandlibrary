package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator<String> {
  private final BufferedReader bufferedReader;
  
  private String cachedLine;
  
  private boolean finished = false;
  
  public LineIterator(Reader reader) throws IllegalArgumentException {
    if (reader == null)
      throw new IllegalArgumentException("Reader must not be null"); 
    if (reader instanceof BufferedReader) {
      this.bufferedReader = (BufferedReader)reader;
    } else {
      this.bufferedReader = new BufferedReader(reader);
    } 
  }
  
  public boolean hasNext() {
    if (this.cachedLine != null)
      return true; 
    if (this.finished)
      return false; 
    try {
      while (true) {
        String line = this.bufferedReader.readLine();
        if (line == null) {
          this.finished = true;
          return false;
        } 
        if (isValidLine(line)) {
          this.cachedLine = line;
          return true;
        } 
      } 
    } catch (IOException ioe) {
      close();
      throw new IllegalStateException(ioe);
    } 
  }
  
  protected boolean isValidLine(String line) {
    return true;
  }
  
  public String next() {
    return nextLine();
  }
  
  public String nextLine() {
    if (!hasNext())
      throw new NoSuchElementException("No more lines"); 
    String currentLine = this.cachedLine;
    this.cachedLine = null;
    return currentLine;
  }
  
  public void close() {
    this.finished = true;
    IOUtils.closeQuietly(this.bufferedReader);
    this.cachedLine = null;
  }
  
  public void remove() {
    throw new UnsupportedOperationException("Remove unsupported on LineIterator");
  }
  
  public static void closeQuietly(LineIterator iterator) {
    if (iterator != null)
      iterator.close(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\LineIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */