package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;

abstract class DecompressingEntity extends HttpEntityWrapper {
   private static final int BUFFER_SIZE = 2048;
   private InputStream content;

   public DecompressingEntity(HttpEntity var1) {
      super(var1);
   }

   abstract InputStream decorate(InputStream var1) throws IOException;

   private InputStream getDecompressingStream() throws IOException {
      InputStream var1 = this.wrappedEntity.getContent();
      return new LazyDecompressingInputStream(var1, this);
   }

   public InputStream getContent() throws IOException {
      if (this.wrappedEntity.isStreaming()) {
         if (this.content == null) {
            this.content = this.getDecompressingStream();
         }

         return this.content;
      } else {
         return this.getDecompressingStream();
      }
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      InputStream var2 = this.getContent();
      byte[] var3 = new byte[2048];

      int var4;
      while((var4 = var2.read(var3)) != -1) {
         var1.write(var3, 0, var4);
      }

      var2.close();
   }
}
