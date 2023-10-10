package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class EntityTemplate extends AbstractHttpEntity {
   private final ContentProducer contentproducer;

   public EntityTemplate(ContentProducer var1) {
      this.contentproducer = (ContentProducer)Args.notNull(var1, "Content producer");
   }

   public long getContentLength() {
      return -1L;
   }

   public InputStream getContent() throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      this.writeTo(var1);
      return new ByteArrayInputStream(var1.toByteArray());
   }

   public boolean isRepeatable() {
      return true;
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      this.contentproducer.writeTo(var1);
   }

   public boolean isStreaming() {
      return false;
   }
}
