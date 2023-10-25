package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.AbstractReferenceCounted;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

final class InternalAttribute extends AbstractReferenceCounted implements InterfaceHttpData {
  private final List<ByteBuf> value = new ArrayList<ByteBuf>();
  
  private final Charset charset;
  
  private int size;
  
  InternalAttribute(Charset charset) {
    this.charset = charset;
  }
  
  public InterfaceHttpData.HttpDataType getHttpDataType() {
    return InterfaceHttpData.HttpDataType.InternalAttribute;
  }
  
  public void addValue(String value) {
    if (value == null)
      throw new NullPointerException("value"); 
    ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
    this.value.add(buf);
    this.size += buf.readableBytes();
  }
  
  public void addValue(String value, int rank) {
    if (value == null)
      throw new NullPointerException("value"); 
    ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
    this.value.add(rank, buf);
    this.size += buf.readableBytes();
  }
  
  public void setValue(String value, int rank) {
    if (value == null)
      throw new NullPointerException("value"); 
    ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
    ByteBuf old = this.value.set(rank, buf);
    if (old != null) {
      this.size -= old.readableBytes();
      old.release();
    } 
    this.size += buf.readableBytes();
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
    if (!(o instanceof InternalAttribute))
      throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType()); 
    return compareTo((InternalAttribute)o);
  }
  
  public int compareTo(InternalAttribute o) {
    return getName().compareToIgnoreCase(o.getName());
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (ByteBuf elt : this.value)
      result.append(elt.toString(this.charset)); 
    return result.toString();
  }
  
  public int size() {
    return this.size;
  }
  
  public ByteBuf toByteBuf() {
    return (ByteBuf)Unpooled.compositeBuffer().addComponents(this.value).writerIndex(size()).readerIndex(0);
  }
  
  public String getName() {
    return "InternalAttribute";
  }
  
  protected void deallocate() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\InternalAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */