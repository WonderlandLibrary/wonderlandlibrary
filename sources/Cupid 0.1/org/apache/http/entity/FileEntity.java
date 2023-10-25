package org.apache.http.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class FileEntity extends AbstractHttpEntity implements Cloneable {
  protected final File file;
  
  @Deprecated
  public FileEntity(File file, String contentType) {
    this.file = (File)Args.notNull(file, "File");
    setContentType(contentType);
  }
  
  public FileEntity(File file, ContentType contentType) {
    this.file = (File)Args.notNull(file, "File");
    if (contentType != null)
      setContentType(contentType.toString()); 
  }
  
  public FileEntity(File file) {
    this.file = (File)Args.notNull(file, "File");
  }
  
  public boolean isRepeatable() {
    return true;
  }
  
  public long getContentLength() {
    return this.file.length();
  }
  
  public InputStream getContent() throws IOException {
    return new FileInputStream(this.file);
  }
  
  public void writeTo(OutputStream outstream) throws IOException {
    Args.notNull(outstream, "Output stream");
    InputStream instream = new FileInputStream(this.file);
    try {
      byte[] tmp = new byte[4096];
      int l;
      while ((l = instream.read(tmp)) != -1)
        outstream.write(tmp, 0, l); 
      outstream.flush();
    } finally {
      instream.close();
    } 
  }
  
  public boolean isStreaming() {
    return false;
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\entity\FileEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */