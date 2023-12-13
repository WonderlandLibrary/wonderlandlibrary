package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class ByteArrayEntity extends AbstractHttpEntity implements Cloneable {
   /** @deprecated */
   @Deprecated
   protected final byte[] content;
   private final byte[] b;
   private final int off;
   private final int len;

   public ByteArrayEntity(byte[] var1, ContentType var2) {
      Args.notNull(var1, "Source byte array");
      this.content = var1;
      this.b = var1;
      this.off = 0;
      this.len = this.b.length;
      if (var2 != null) {
         this.setContentType(var2.toString());
      }

   }

   public ByteArrayEntity(byte[] var1, int var2, int var3, ContentType var4) {
      Args.notNull(var1, "Source byte array");
      if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 >= 0 && var2 + var3 <= var1.length) {
         this.content = var1;
         this.b = var1;
         this.off = var2;
         this.len = var3;
         if (var4 != null) {
            this.setContentType(var4.toString());
         }

      } else {
         throw new IndexOutOfBoundsException("off: " + var2 + " len: " + var3 + " b.length: " + var1.length);
      }
   }

   public ByteArrayEntity(byte[] var1) {
      this(var1, (ContentType)null);
   }

   public ByteArrayEntity(byte[] var1, int var2, int var3) {
      this(var1, var2, var3, (ContentType)null);
   }

   public boolean isRepeatable() {
      return true;
   }

   public long getContentLength() {
      return (long)this.len;
   }

   public InputStream getContent() {
      return new ByteArrayInputStream(this.b, this.off, this.len);
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      var1.write(this.b, this.off, this.len);
      var1.flush();
   }

   public boolean isStreaming() {
      return false;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
