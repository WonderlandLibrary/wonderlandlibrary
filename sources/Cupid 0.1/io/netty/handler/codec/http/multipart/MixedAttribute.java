package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MixedAttribute implements Attribute {
  private Attribute attribute;
  
  private final long limitSize;
  
  public MixedAttribute(String name, long limitSize) {
    this.limitSize = limitSize;
    this.attribute = new MemoryAttribute(name);
  }
  
  public MixedAttribute(String name, String value, long limitSize) {
    this.limitSize = limitSize;
    if (value.length() > this.limitSize) {
      try {
        this.attribute = new DiskAttribute(name, value);
      } catch (IOException e) {
        try {
          this.attribute = new MemoryAttribute(name, value);
        } catch (IOException e1) {
          throw new IllegalArgumentException(e);
        } 
      } 
    } else {
      try {
        this.attribute = new MemoryAttribute(name, value);
      } catch (IOException e) {
        throw new IllegalArgumentException(e);
      } 
    } 
  }
  
  public void addContent(ByteBuf buffer, boolean last) throws IOException {
    if (this.attribute instanceof MemoryAttribute && 
      this.attribute.length() + buffer.readableBytes() > this.limitSize) {
      DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName());
      if (((MemoryAttribute)this.attribute).getByteBuf() != null)
        diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false); 
      this.attribute = diskAttribute;
    } 
    this.attribute.addContent(buffer, last);
  }
  
  public void delete() {
    this.attribute.delete();
  }
  
  public byte[] get() throws IOException {
    return this.attribute.get();
  }
  
  public ByteBuf getByteBuf() throws IOException {
    return this.attribute.getByteBuf();
  }
  
  public Charset getCharset() {
    return this.attribute.getCharset();
  }
  
  public String getString() throws IOException {
    return this.attribute.getString();
  }
  
  public String getString(Charset encoding) throws IOException {
    return this.attribute.getString(encoding);
  }
  
  public boolean isCompleted() {
    return this.attribute.isCompleted();
  }
  
  public boolean isInMemory() {
    return this.attribute.isInMemory();
  }
  
  public long length() {
    return this.attribute.length();
  }
  
  public boolean renameTo(File dest) throws IOException {
    return this.attribute.renameTo(dest);
  }
  
  public void setCharset(Charset charset) {
    this.attribute.setCharset(charset);
  }
  
  public void setContent(ByteBuf buffer) throws IOException {
    if (buffer.readableBytes() > this.limitSize && 
      this.attribute instanceof MemoryAttribute)
      this.attribute = new DiskAttribute(this.attribute.getName()); 
    this.attribute.setContent(buffer);
  }
  
  public void setContent(File file) throws IOException {
    if (file.length() > this.limitSize && 
      this.attribute instanceof MemoryAttribute)
      this.attribute = new DiskAttribute(this.attribute.getName()); 
    this.attribute.setContent(file);
  }
  
  public void setContent(InputStream inputStream) throws IOException {
    if (this.attribute instanceof MemoryAttribute)
      this.attribute = new DiskAttribute(this.attribute.getName()); 
    this.attribute.setContent(inputStream);
  }
  
  public InterfaceHttpData.HttpDataType getHttpDataType() {
    return this.attribute.getHttpDataType();
  }
  
  public String getName() {
    return this.attribute.getName();
  }
  
  public int compareTo(InterfaceHttpData o) {
    return this.attribute.compareTo(o);
  }
  
  public String toString() {
    return "Mixed: " + this.attribute.toString();
  }
  
  public String getValue() throws IOException {
    return this.attribute.getValue();
  }
  
  public void setValue(String value) throws IOException {
    this.attribute.setValue(value);
  }
  
  public ByteBuf getChunk(int length) throws IOException {
    return this.attribute.getChunk(length);
  }
  
  public File getFile() throws IOException {
    return this.attribute.getFile();
  }
  
  public Attribute copy() {
    return this.attribute.copy();
  }
  
  public Attribute duplicate() {
    return this.attribute.duplicate();
  }
  
  public ByteBuf content() {
    return this.attribute.content();
  }
  
  public int refCnt() {
    return this.attribute.refCnt();
  }
  
  public Attribute retain() {
    this.attribute.retain();
    return this;
  }
  
  public Attribute retain(int increment) {
    this.attribute.retain(increment);
    return this;
  }
  
  public boolean release() {
    return this.attribute.release();
  }
  
  public boolean release(int decrement) {
    return this.attribute.release(decrement);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\MixedAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */