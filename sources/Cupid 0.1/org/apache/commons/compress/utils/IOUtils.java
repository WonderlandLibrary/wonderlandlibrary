package org.apache.commons.compress.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IOUtils {
  private static final int COPY_BUF_SIZE = 8024;
  
  private static final int SKIP_BUF_SIZE = 4096;
  
  private static final byte[] SKIP_BUF = new byte[4096];
  
  public static long copy(InputStream input, OutputStream output) throws IOException {
    return copy(input, output, 8024);
  }
  
  public static long copy(InputStream input, OutputStream output, int buffersize) throws IOException {
    byte[] buffer = new byte[buffersize];
    int n = 0;
    long count = 0L;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    } 
    return count;
  }
  
  public static long skip(InputStream input, long numToSkip) throws IOException {
    long available = numToSkip;
    while (numToSkip > 0L) {
      long skipped = input.skip(numToSkip);
      if (skipped == 0L)
        break; 
      numToSkip -= skipped;
    } 
    while (numToSkip > 0L) {
      int read = readFully(input, SKIP_BUF, 0, (int)Math.min(numToSkip, 4096L));
      if (read < 1)
        break; 
      numToSkip -= read;
    } 
    return available - numToSkip;
  }
  
  public static int readFully(InputStream input, byte[] b) throws IOException {
    return readFully(input, b, 0, b.length);
  }
  
  public static int readFully(InputStream input, byte[] b, int offset, int len) throws IOException {
    if (len < 0 || offset < 0 || len + offset > b.length)
      throw new IndexOutOfBoundsException(); 
    int count = 0, x = 0;
    while (count != len) {
      x = input.read(b, offset + count, len - count);
      if (x == -1)
        break; 
      count += x;
    } 
    return count;
  }
  
  public static byte[] toByteArray(InputStream input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output);
    return output.toByteArray();
  }
  
  public static void closeQuietly(Closeable c) {
    if (c != null)
      try {
        c.close();
      } catch (IOException ignored) {} 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compres\\utils\IOUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */