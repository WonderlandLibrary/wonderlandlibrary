package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.ReferenceCounted;
import java.io.IOException;

public class DiskAttribute extends AbstractDiskHttpData implements Attribute {
  public static String baseDirectory;
  
  public static boolean deleteOnExitTemporaryFile = true;
  
  public static final String prefix = "Attr_";
  
  public static final String postfix = ".att";
  
  public DiskAttribute(String name) {
    super(name, HttpConstants.DEFAULT_CHARSET, 0L);
  }
  
  public DiskAttribute(String name, String value) throws IOException {
    super(name, HttpConstants.DEFAULT_CHARSET, 0L);
    setValue(value);
  }
  
  public InterfaceHttpData.HttpDataType getHttpDataType() {
    return InterfaceHttpData.HttpDataType.Attribute;
  }
  
  public String getValue() throws IOException {
    byte[] bytes = get();
    return new String(bytes, this.charset.name());
  }
  
  public void setValue(String value) throws IOException {
    if (value == null)
      throw new NullPointerException("value"); 
    byte[] bytes = value.getBytes(this.charset.name());
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    if (this.definedSize > 0L)
      this.definedSize = buffer.readableBytes(); 
    setContent(buffer);
  }
  
  public void addContent(ByteBuf buffer, boolean last) throws IOException {
    int localsize = buffer.readableBytes();
    if (this.definedSize > 0L && this.definedSize < this.size + localsize)
      this.definedSize = this.size + localsize; 
    super.addContent(buffer, last);
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
    if (!(o instanceof Attribute))
      throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType()); 
    return compareTo((Attribute)o);
  }
  
  public int compareTo(Attribute o) {
    return getName().compareToIgnoreCase(o.getName());
  }
  
  public String toString() {
    try {
      return getName() + '=' + getValue();
    } catch (IOException e) {
      return getName() + "=IoException";
    } 
  }
  
  protected boolean deleteOnExit() {
    return deleteOnExitTemporaryFile;
  }
  
  protected String getBaseDirectory() {
    return baseDirectory;
  }
  
  protected String getDiskFilename() {
    return getName() + ".att";
  }
  
  protected String getPostfix() {
    return ".att";
  }
  
  protected String getPrefix() {
    return "Attr_";
  }
  
  public Attribute copy() {
    DiskAttribute attr = new DiskAttribute(getName());
    attr.setCharset(getCharset());
    ByteBuf content = content();
    if (content != null)
      try {
        attr.setContent(content.copy());
      } catch (IOException e) {
        throw new ChannelException(e);
      }  
    return attr;
  }
  
  public Attribute duplicate() {
    DiskAttribute attr = new DiskAttribute(getName());
    attr.setCharset(getCharset());
    ByteBuf content = content();
    if (content != null)
      try {
        attr.setContent(content.duplicate());
      } catch (IOException e) {
        throw new ChannelException(e);
      }  
    return attr;
  }
  
  public Attribute retain(int increment) {
    super.retain(increment);
    return this;
  }
  
  public Attribute retain() {
    super.retain();
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\DiskAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */