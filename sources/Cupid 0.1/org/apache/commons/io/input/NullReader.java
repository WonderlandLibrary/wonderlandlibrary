package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader extends Reader {
  private final long size;
  
  private long position;
  
  private long mark = -1L;
  
  private long readlimit;
  
  private boolean eof;
  
  private final boolean throwEofException;
  
  private final boolean markSupported;
  
  public NullReader(long size) {
    this(size, true, false);
  }
  
  public NullReader(long size, boolean markSupported, boolean throwEofException) {
    this.size = size;
    this.markSupported = markSupported;
    this.throwEofException = throwEofException;
  }
  
  public long getPosition() {
    return this.position;
  }
  
  public long getSize() {
    return this.size;
  }
  
  public void close() throws IOException {
    this.eof = false;
    this.position = 0L;
    this.mark = -1L;
  }
  
  public synchronized void mark(int readlimit) {
    if (!this.markSupported)
      throw new UnsupportedOperationException("Mark not supported"); 
    this.mark = this.position;
    this.readlimit = readlimit;
  }
  
  public boolean markSupported() {
    return this.markSupported;
  }
  
  public int read() throws IOException {
    if (this.eof)
      throw new IOException("Read after end of file"); 
    if (this.position == this.size)
      return doEndOfFile(); 
    this.position++;
    return processChar();
  }
  
  public int read(char[] chars) throws IOException {
    return read(chars, 0, chars.length);
  }
  
  public int read(char[] chars, int offset, int length) throws IOException {
    if (this.eof)
      throw new IOException("Read after end of file"); 
    if (this.position == this.size)
      return doEndOfFile(); 
    this.position += length;
    int returnLength = length;
    if (this.position > this.size) {
      returnLength = length - (int)(this.position - this.size);
      this.position = this.size;
    } 
    processChars(chars, offset, returnLength);
    return returnLength;
  }
  
  public synchronized void reset() throws IOException {
    if (!this.markSupported)
      throw new UnsupportedOperationException("Mark not supported"); 
    if (this.mark < 0L)
      throw new IOException("No position has been marked"); 
    if (this.position > this.mark + this.readlimit)
      throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]"); 
    this.position = this.mark;
    this.eof = false;
  }
  
  public long skip(long numberOfChars) throws IOException {
    if (this.eof)
      throw new IOException("Skip after end of file"); 
    if (this.position == this.size)
      return doEndOfFile(); 
    this.position += numberOfChars;
    long returnLength = numberOfChars;
    if (this.position > this.size) {
      returnLength = numberOfChars - this.position - this.size;
      this.position = this.size;
    } 
    return returnLength;
  }
  
  protected int processChar() {
    return 0;
  }
  
  protected void processChars(char[] chars, int offset, int length) {}
  
  private int doEndOfFile() throws EOFException {
    this.eof = true;
    if (this.throwEofException)
      throw new EOFException(); 
    return -1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\NullReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */