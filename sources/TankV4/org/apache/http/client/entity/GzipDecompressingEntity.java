package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class GzipDecompressingEntity extends DecompressingEntity {
   public GzipDecompressingEntity(HttpEntity var1) {
      super(var1);
   }

   InputStream decorate(InputStream var1) throws IOException {
      return new GZIPInputStream(var1);
   }

   public Header getContentEncoding() {
      return null;
   }

   public long getContentLength() {
      return -1L;
   }

   public void writeTo(OutputStream var1) throws IOException {
      super.writeTo(var1);
   }

   public InputStream getContent() throws IOException {
      return super.getContent();
   }
}
