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

   public DiskFileUpload(String var1, String var2, String var3, String var4, Charset var5, long var6) {
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
      return "Content-Disposition: form-data; name=\"" + this.getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + (this.charset != null ? "; charset=" + this.charset + "\r\n" : "\r\n") + "Content-Length" + ": " + this.length() + "\r\n" + "Completed: " + this.isCompleted() + "\r\nIsInMemory: " + this.isInMemory() + "\r\nRealFile: " + this.file.getAbsolutePath() + " DefaultDeleteAfter: " + deleteOnExitTemporaryFile;
   }

   protected boolean deleteOnExit() {
      return deleteOnExitTemporaryFile;
   }

   protected String getBaseDirectory() {
      return baseDirectory;
   }

   protected String getDiskFilename() {
      File var1 = new File(this.filename);
      return var1.getName();
   }

   protected String getPostfix() {
      return ".tmp";
   }

   protected String getPrefix() {
      return "FUp_";
   }

   public FileUpload copy() {
      DiskFileUpload var1 = new DiskFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.copy());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public FileUpload duplicate() {
      DiskFileUpload var1 = new DiskFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.duplicate());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public FileUpload retain(int var1) {
      super.retain(var1);
      return this;
   }

   public FileUpload retain() {
      super.retain();
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
