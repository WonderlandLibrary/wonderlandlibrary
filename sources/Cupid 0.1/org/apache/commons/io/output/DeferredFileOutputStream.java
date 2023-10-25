package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {
  private ByteArrayOutputStream memoryOutputStream;
  
  private OutputStream currentOutputStream;
  
  private File outputFile;
  
  private final String prefix;
  
  private final String suffix;
  
  private final File directory;
  
  private boolean closed = false;
  
  public DeferredFileOutputStream(int threshold, File outputFile) {
    this(threshold, outputFile, null, null, null);
  }
  
  public DeferredFileOutputStream(int threshold, String prefix, String suffix, File directory) {
    this(threshold, null, prefix, suffix, directory);
    if (prefix == null)
      throw new IllegalArgumentException("Temporary file prefix is missing"); 
  }
  
  private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory) {
    super(threshold);
    this.outputFile = outputFile;
    this.memoryOutputStream = new ByteArrayOutputStream();
    this.currentOutputStream = this.memoryOutputStream;
    this.prefix = prefix;
    this.suffix = suffix;
    this.directory = directory;
  }
  
  protected OutputStream getStream() throws IOException {
    return this.currentOutputStream;
  }
  
  protected void thresholdReached() throws IOException {
    if (this.prefix != null)
      this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory); 
    FileOutputStream fos = new FileOutputStream(this.outputFile);
    this.memoryOutputStream.writeTo(fos);
    this.currentOutputStream = fos;
    this.memoryOutputStream = null;
  }
  
  public boolean isInMemory() {
    return !isThresholdExceeded();
  }
  
  public byte[] getData() {
    if (this.memoryOutputStream != null)
      return this.memoryOutputStream.toByteArray(); 
    return null;
  }
  
  public File getFile() {
    return this.outputFile;
  }
  
  public void close() throws IOException {
    super.close();
    this.closed = true;
  }
  
  public void writeTo(OutputStream out) throws IOException {
    if (!this.closed)
      throw new IOException("Stream not closed"); 
    if (isInMemory()) {
      this.memoryOutputStream.writeTo(out);
    } else {
      FileInputStream fis = new FileInputStream(this.outputFile);
      try {
        IOUtils.copy(fis, out);
      } finally {
        IOUtils.closeQuietly(fis);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\output\DeferredFileOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */