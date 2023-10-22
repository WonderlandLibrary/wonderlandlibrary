package com.sun.webkit.network.about;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

final class AboutURLConnection extends URLConnection {
   private static final String DEFAULT_CHARSET = "UTF-8";
   private static final String DEFAULT_MIMETYPE = "text/html";
   private final AboutRecord record = new AboutRecord("");

   AboutURLConnection(URL var1) {
      super(var1);
   }

   public void connect() throws IOException {
      if (!this.connected) {
         this.connected = this.record != null;
         if (this.connected) {
            this.record.content.reset();
         } else {
            throw new ProtocolException("The URL is not valid and cannot be loaded.");
         }
      }
   }

   public InputStream getInputStream() throws IOException {
      this.connect();
      return this.record.content;
   }

   public String getContentType() {
      try {
         this.connect();
         if (this.record.contentType != null) {
            return this.record.contentType;
         }
      } catch (IOException var2) {
      }

      return "text/html";
   }

   public String getContentEncoding() {
      try {
         this.connect();
         if (this.record.contentEncoding != null) {
            return this.record.contentEncoding;
         }
      } catch (IOException var2) {
      }

      return "UTF-8";
   }

   public int getContentLength() {
      try {
         this.connect();
         return this.record.contentLength;
      } catch (IOException var2) {
         return -1;
      }
   }

   private static final class AboutRecord {
      private final InputStream content;
      private final int contentLength;
      private final String contentEncoding;
      private final String contentType;

      private AboutRecord(String var1) {
         byte[] var2 = new byte[0];

         try {
            var2 = var1.getBytes("UTF-8");
         } catch (UnsupportedEncodingException var4) {
         }

         this.content = new ByteArrayInputStream(var2);
         this.contentLength = var2.length;
         this.contentEncoding = "UTF-8";
         this.contentType = "text/html";
      }

      // $FF: synthetic method
      AboutRecord(String var1, Object var2) {
         this(var1);
      }
   }
}
