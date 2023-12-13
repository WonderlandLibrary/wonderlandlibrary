package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelException;
import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.nio.charset.Charset;

public class MemoryFileUpload extends AbstractMemoryHttpData implements FileUpload {
   private String filename;
   private String contentType;
   private String contentTransferEncoding;

   public MemoryFileUpload(String var1, String var2, String var3, String var4, Charset var5, long var6) {
      super(var1, var5, var6);
      this.setFilename(var2);
      this.setContentType(var3);
      this.setContentTransferEncoding(var4);
   }

   public InterfaceHttpData.HttpDataType getHttpDataType() {
      return InterfaceHttpData.HttpDataType.FileUpload;
   }

   public String getFilename() {
      return this.filename;
   }

   public void setFilename(String var1) {
      if (var1 == null) {
         throw new NullPointerException("filename");
      } else {
         this.filename = var1;
      }
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Attribute)) {
         return false;
      } else {
         Attribute var2 = (Attribute)var1;
         return this.getName().equalsIgnoreCase(var2.getName());
      }
   }

   public int compareTo(InterfaceHttpData var1) {
      if (!(var1 instanceof FileUpload)) {
         throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + var1.getHttpDataType());
      } else {
         return this.compareTo((FileUpload)var1);
      }
   }

   public int compareTo(FileUpload var1) {
      int var2 = this.getName().compareToIgnoreCase(var1.getName());
      return var2 != 0 ? var2 : var2;
   }

   public void setContentType(String var1) {
      if (var1 == null) {
         throw new NullPointerException("contentType");
      } else {
         this.contentType = var1;
      }
   }

   public String getContentType() {
      return this.contentType;
   }

   public String getContentTransferEncoding() {
      return this.contentTransferEncoding;
   }

   public void setContentTransferEncoding(String var1) {
      this.contentTransferEncoding = var1;
   }

   public String toString() {
      return "Content-Disposition: form-data; name=\"" + this.getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + (this.charset != null ? "; charset=" + this.charset + "\r\n" : "\r\n") + "Content-Length" + ": " + this.length() + "\r\n" + "Completed: " + this.isCompleted() + "\r\nIsInMemory: " + this.isInMemory();
   }

   public FileUpload copy() {
      MemoryFileUpload var1 = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.copy());
            return var1;
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      } else {
         return var1;
      }
   }

   public FileUpload duplicate() {
      MemoryFileUpload var1 = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.duplicate());
            return var1;
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      } else {
         return var1;
      }
   }

   public FileUpload retain() {
      super.retain();
      return this;
   }

   public FileUpload retain(int var1) {
      super.retain(var1);
      return this;
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
