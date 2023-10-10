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

   public MixedAttribute(String var1, long var2) {
      this.limitSize = var2;
      this.attribute = new MemoryAttribute(var1);
   }

   public MixedAttribute(String var1, String var2, long var3) {
      this.limitSize = var3;
      if ((long)var2.length() > this.limitSize) {
         try {
            this.attribute = new DiskAttribute(var1, var2);
         } catch (IOException var9) {
            try {
               this.attribute = new MemoryAttribute(var1, var2);
            } catch (IOException var8) {
               throw new IllegalArgumentException(var9);
            }
         }
      } else {
         try {
            this.attribute = new MemoryAttribute(var1, var2);
         } catch (IOException var7) {
            throw new IllegalArgumentException(var7);
         }
      }

   }

   public void addContent(ByteBuf var1, boolean var2) throws IOException {
      if (this.attribute instanceof MemoryAttribute && this.attribute.length() + (long)var1.readableBytes() > this.limitSize) {
         DiskAttribute var3 = new DiskAttribute(this.attribute.getName());
         if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
            var3.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
         }

         this.attribute = var3;
      }

      this.attribute.addContent(var1, var2);
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

   public String getString(Charset var1) throws IOException {
      return this.attribute.getString(var1);
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

   public boolean renameTo(File var1) throws IOException {
      return this.attribute.renameTo(var1);
   }

   public void setCharset(Charset var1) {
      this.attribute.setCharset(var1);
   }

   public void setContent(ByteBuf var1) throws IOException {
      if ((long)var1.readableBytes() > this.limitSize && this.attribute instanceof MemoryAttribute) {
         this.attribute = new DiskAttribute(this.attribute.getName());
      }

      this.attribute.setContent(var1);
   }

   public void setContent(File var1) throws IOException {
      if (var1.length() > this.limitSize && this.attribute instanceof MemoryAttribute) {
         this.attribute = new DiskAttribute(this.attribute.getName());
      }

      this.attribute.setContent(var1);
   }

   public void setContent(InputStream var1) throws IOException {
      if (this.attribute instanceof MemoryAttribute) {
         this.attribute = new DiskAttribute(this.attribute.getName());
      }

      this.attribute.setContent(var1);
   }

   public InterfaceHttpData.HttpDataType getHttpDataType() {
      return this.attribute.getHttpDataType();
   }

   public String getName() {
      return this.attribute.getName();
   }

   public int compareTo(InterfaceHttpData var1) {
      return this.attribute.compareTo(var1);
   }

   public String toString() {
      return "Mixed: " + this.attribute.toString();
   }

   public String getValue() throws IOException {
      return this.attribute.getValue();
   }

   public void setValue(String var1) throws IOException {
      this.attribute.setValue(var1);
   }

   public ByteBuf getChunk(int var1) throws IOException {
      return this.attribute.getChunk(var1);
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

   public Attribute retain(int var1) {
      this.attribute.retain(var1);
      return this;
   }

   public boolean release() {
      return this.attribute.release();
   }

   public boolean release(int var1) {
      return this.attribute.release(var1);
   }

   public HttpData retain(int var1) {
      return this.retain(var1);
   }

   public HttpData retain() {
      return this.retain();
   }

   public HttpData duplicate() {
      return this.duplicate();
   }

   public HttpData copy() {
      return this.copy();
   }

   public int compareTo(Object var1) {
      return this.compareTo((InterfaceHttpData)var1);
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   public ByteBufHolder retain() {
      return this.retain();
   }

   public ByteBufHolder duplicate() {
      return this.duplicate();
   }

   public ByteBufHolder copy() {
      return this.copy();
   }
}
