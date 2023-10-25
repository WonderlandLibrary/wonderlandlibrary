package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

@Deprecated
public class CopyUtils {
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  public static void copy(byte[] input, OutputStream output) throws IOException {
    output.write(input);
  }
  
  public static void copy(byte[] input, Writer output) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(input);
    copy(in, output);
  }
  
  public static void copy(byte[] input, Writer output, String encoding) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(input);
    copy(in, output, encoding);
  }
  
  public static int copy(InputStream input, OutputStream output) throws IOException {
    byte[] buffer = new byte[4096];
    int count = 0;
    int n = 0;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    } 
    return count;
  }
  
  public static int copy(Reader input, Writer output) throws IOException {
    char[] buffer = new char[4096];
    int count = 0;
    int n = 0;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    } 
    return count;
  }
  
  public static void copy(InputStream input, Writer output) throws IOException {
    InputStreamReader in = new InputStreamReader(input);
    copy(in, output);
  }
  
  public static void copy(InputStream input, Writer output, String encoding) throws IOException {
    InputStreamReader in = new InputStreamReader(input, encoding);
    copy(in, output);
  }
  
  public static void copy(Reader input, OutputStream output) throws IOException {
    OutputStreamWriter out = new OutputStreamWriter(output);
    copy(input, out);
    out.flush();
  }
  
  public static void copy(String input, OutputStream output) throws IOException {
    StringReader in = new StringReader(input);
    OutputStreamWriter out = new OutputStreamWriter(output);
    copy(in, out);
    out.flush();
  }
  
  public static void copy(String input, Writer output) throws IOException {
    output.write(input);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\CopyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */