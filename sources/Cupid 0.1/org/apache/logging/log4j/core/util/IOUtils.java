package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class IOUtils {
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  public static final int EOF = -1;
  
  public static int copy(Reader input, Writer output) throws IOException {
    long count = copyLarge(input, output);
    if (count > 2147483647L)
      return -1; 
    return (int)count;
  }
  
  public static long copyLarge(Reader input, Writer output) throws IOException {
    return copyLarge(input, output, new char[4096]);
  }
  
  public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
    long count = 0L;
    int n;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    } 
    return count;
  }
  
  public static String toString(Reader input) throws IOException {
    StringBuilderWriter sw = new StringBuilderWriter();
    copy(input, sw);
    return sw.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */