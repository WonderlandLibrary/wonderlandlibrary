package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelException;
import io.netty.util.ReferenceCounted;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class DiskFileUpload extends AbstractDiskHttpData implements FileUpload {
  public static String baseDirectory;
  
  public static boolean deleteOnExitTemporaryFile = true;
  
  public static final String prefix = "FUp_";
  
  public static final String postfix = ".tmp";
  
  private String filename;
  
  private String contentType;
  
  private String contentTransferEncoding;
  
  public DiskFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
    super(name, charset, size);
    setFilename(filename);
    setContentType(contentType);
    setContentTransferEncoding(contentTransferEncoding);
  }
  
  public InterfaceHttpData.HttpDataType getHttpDataType() {
    return InterfaceHttpData.HttpDataType.FileUpload;
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public void setFilename(String filename) {
    if (filename == null)
      throw new NullPointerException("filename"); 
    this.filename = filename;
  }
  
  public int hashCode() {
    return getName().hashCode();
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Attribute))
      return false; 
    Attribute attribute = (Attribute)o;
    return getName().equalsIgnoreCase(attribute.getName());
  }
  
  public int compareTo(InterfaceHttpData o) {
    if (!(o instanceof FileUpload))
      throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType()); 
    return compareTo((FileUpload)o);
  }
  
  public int compareTo(FileUpload o) {
    int v = getName().compareToIgnoreCase(o.getName());
    if (v != 0)
      return v; 
    return v;
  }
  
  public void setContentType(String contentType) {
    if (contentType == null)
      throw new NullPointerException("contentType"); 
    this.contentType = contentType;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public String getContentTransferEncoding() {
    return this.contentTransferEncoding;
  }
  
  public void setContentTransferEncoding(String contentTransferEncoding) {
    this.contentTransferEncoding = contentTransferEncoding;
  }
  
  public String toString() {
    return "Content-Disposition: form-data; name=\"" + getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + ((this.charset != null) ? ("; charset=" + this.charset + "\r\n") : "\r\n") + "Content-Length" + ": " + length() + "\r\n" + "Completed: " + isCompleted() + "\r\nIsInMemory: " + isInMemory() + "\r\nRealFile: " + ((this.file != null) ? this.file.getAbsolutePath() : "null") + " DefaultDeleteAfter: " + deleteOnExitTemporaryFile;
  }
  
  protected boolean deleteOnExit() {
    return deleteOnExitTemporaryFile;
  }
  
  protected String getBaseDirectory() {
    return baseDirectory;
  }
  
  protected String getDiskFilename() {
    File file = new File(this.filename);
    return file.getName();
  }
  
  protected String getPostfix() {
    return ".tmp";
  }
  
  protected String getPrefix() {
    return "FUp_";
  }
  
  public FileUpload copy() {
    DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
    ByteBuf buf = content();
    if (buf != null)
      try {
        upload.setContent(buf.copy());
      } catch (IOException e) {
        throw new ChannelException(e);
      }  
    return upload;
  }
  
  public FileUpload duplicate() {
    DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
    ByteBuf buf = content();
    if (buf != null)
      try {
        upload.setContent(buf.duplicate());
      } catch (IOException e) {
        throw new ChannelException(e);
      }  
    return upload;
  }
  
  public FileUpload retain(int increment) {
    super.retain(increment);
    return this;
  }
  
  public FileUpload retain() {
    super.retain();
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\DiskFileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */