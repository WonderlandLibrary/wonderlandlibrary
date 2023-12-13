package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@NotThreadSafe
public class BasicHttpEntity extends AbstractHttpEntity {
   private InputStream content;
   private long length = -1L;

   public long getContentLength() {
      return this.length;
   }

   public InputStream getContent() throws IllegalStateException {
      Asserts.check(this.content != null, "Content has not been provided");
      return this.content;
   }

   public boolean isRepeatable() {
      return false;
   }

   public void setContentLength(long var1) {
      this.length = var1;
   }

   public void setContent(InputStream var1) {
      this.content = var1;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      InputStream var2 = this.getContent();
      byte[] var4 = new byte[4096];

      int var3;
      while((var3 = var2.read(var4)) != -1) {
         var1.write(var4, 0, var3);
      }

      var2.close();
   }

   public boolean isStreaming() {
      return this.content != null;
   }
}
