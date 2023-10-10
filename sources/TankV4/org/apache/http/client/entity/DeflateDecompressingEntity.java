package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity {
   public DeflateDecompressingEntity(HttpEntity var1) {
      super(var1);
   }

   InputStream decorate(InputStream var1) throws IOException {
      return new DeflateInputStream(var1);
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
