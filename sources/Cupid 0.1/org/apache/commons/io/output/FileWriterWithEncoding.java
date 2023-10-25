package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileWriterWithEncoding extends Writer {
  private final Writer out;
  
  public FileWriterWithEncoding(String filename, String encoding) throws IOException {
    this(new File(filename), encoding, false);
  }
  
  public FileWriterWithEncoding(String filename, String encoding, boolean append) throws IOException {
    this(new File(filename), encoding, append);
  }
  
  public FileWriterWithEncoding(String filename, Charset encoding) throws IOException {
    this(new File(filename), encoding, false);
  }
  
  public FileWriterWithEncoding(String filename, Charset encoding, boolean append) throws IOException {
    this(new File(filename), encoding, append);
  }
  
  public FileWriterWithEncoding(String filename, CharsetEncoder encoding) throws IOException {
    this(new File(filename), encoding, false);
  }
  
  public FileWriterWithEncoding(String filename, CharsetEncoder encoding, boolean append) throws IOException {
    this(new File(filename), encoding, append);
  }
  
  public FileWriterWithEncoding(File file, String encoding) throws IOException {
    this(file, encoding, false);
  }
  
  public FileWriterWithEncoding(File file, String encoding, boolean append) throws IOException {
    this.out = initWriter(file, encoding, append);
  }
  
  public FileWriterWithEncoding(File file, Charset encoding) throws IOException {
    this(file, encoding, false);
  }
  
  public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
    this.out = initWriter(file, encoding, append);
  }
  
  public FileWriterWithEncoding(File file, CharsetEncoder encoding) throws IOException {
    this(file, encoding, false);
  }
  
  public FileWriterWithEncoding(File file, CharsetEncoder encoding, boolean append) throws IOException {
    this.out = initWriter(file, encoding, append);
  }
  
  private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
    if (file == null)
      throw new NullPointerException("File is missing"); 
    if (encoding == null)
      throw new NullPointerException("Encoding is missing"); 
    boolean fileExistedAlready = file.exists();
    OutputStream stream = null;
    Writer writer = null;
    try {
      stream = new FileOutputStream(file, append);
      if (encoding instanceof Charset) {
        writer = new OutputStreamWriter(stream, (Charset)encoding);
      } else if (encoding instanceof CharsetEncoder) {
        writer = new OutputStreamWriter(stream, (CharsetEncoder)encoding);
      } else {
        writer = new OutputStreamWriter(stream, (String)encoding);
      } 
    } catch (IOException ex) {
      IOUtils.closeQuietly(writer);
      IOUtils.closeQuietly(stream);
      if (!fileExistedAlready)
        FileUtils.deleteQuietly(file); 
      throw ex;
    } catch (RuntimeException ex) {
      IOUtils.closeQuietly(writer);
      IOUtils.closeQuietly(stream);
      if (!fileExistedAlready)
        FileUtils.deleteQuietly(file); 
      throw ex;
    } 
    return writer;
  }
  
  public void write(int idx) throws IOException {
    this.out.write(idx);
  }
  
  public void write(char[] chr) throws IOException {
    this.out.write(chr);
  }
  
  public void write(char[] chr, int st, int end) throws IOException {
    this.out.write(chr, st, end);
  }
  
  public void write(String str) throws IOException {
    this.out.write(str);
  }
  
  public void write(String str, int st, int end) throws IOException {
    this.out.write(str, st, end);
  }
  
  public void flush() throws IOException {
    this.out.flush();
  }
  
  public void close() throws IOException {
    this.out.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\output\FileWriterWithEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */