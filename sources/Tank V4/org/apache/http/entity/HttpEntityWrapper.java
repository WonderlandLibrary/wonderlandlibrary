package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class HttpEntityWrapper implements HttpEntity {
   protected HttpEntity wrappedEntity;

   public HttpEntityWrapper(HttpEntity var1) {
      this.wrappedEntity = (HttpEntity)Args.notNull(var1, "Wrapped entity");
   }

   public boolean isRepeatable() {
      return this.wrappedEntity.isRepeatable();
   }

   public boolean isChunked() {
      return this.wrappedEntity.isChunked();
   }

   public long getContentLength() {
      return this.wrappedEntity.getContentLength();
   }

   public Header getContentType() {
      return this.wrappedEntity.getContentType();
   }

   public Header getContentEncoding() {
      return this.wrappedEntity.getContentEncoding();
   }

   public InputStream getContent() throws IOException {
      return this.wrappedEntity.getContent();
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.wrappedEntity.writeTo(var1);
   }

   public boolean isStreaming() {
      return this.wrappedEntity.isStreaming();
   }

   /** @deprecated */
   @Deprecated
   public void consumeContent() throws IOException {
      this.wrappedEntity.consumeContent();
   }
}
