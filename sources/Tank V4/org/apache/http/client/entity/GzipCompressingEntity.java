package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.Args;

public class GzipCompressingEntity extends HttpEntityWrapper {
   private static final String GZIP_CODEC = "gzip";

   public GzipCompressingEntity(HttpEntity var1) {
      super(var1);
   }

   public Header getContentEncoding() {
      return new BasicHeader("Content-Encoding", "gzip");
   }

   public long getContentLength() {
      return -1L;
   }

   public boolean isChunked() {
      return true;
   }

   public InputStream getContent() throws IOException {
      throw new UnsupportedOperationException();
   }

   public void writeTo(OutputStream var1) throws IOException {
      Args.notNull(var1, "Output stream");
      GZIPOutputStream var2 = new GZIPOutputStream(var1);
      this.wrappedEntity.writeTo(var2);
      var2.close();
   }
}
