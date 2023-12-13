package com.sun.javafx.runtime.async;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractRemoteResource extends AbstractAsyncOperation {
   protected final String url;
   protected final String method;
   protected final String outboundContent;
   protected int fileSize;
   private Map headers;
   private Map responseHeaders;

   protected AbstractRemoteResource(String var1, AsyncOperationListener var2) {
      this(var1, "GET", var2);
   }

   protected AbstractRemoteResource(String var1, String var2, AsyncOperationListener var3) {
      this(var1, var2, (String)null, var3);
   }

   protected AbstractRemoteResource(String var1, String var2, String var3, AsyncOperationListener var4) {
      super(var4);
      this.headers = new HashMap();
      this.responseHeaders = new HashMap();
      this.url = var1;
      this.method = var2;
      this.outboundContent = var3;
   }

   protected abstract Object processStream(InputStream var1) throws IOException;

   public Object call() throws IOException {
      URL var1 = new URL(this.url);
      ProgressInputStream var2 = null;
      String var3 = var1.getProtocol();
      if (!var3.equals("http") && !var3.equals("https")) {
         URLConnection var12 = var1.openConnection();
         this.setProgressMax(var12.getContentLength());
         var2 = new ProgressInputStream(var12.getInputStream());
      } else {
         HttpURLConnection var4 = (HttpURLConnection)var1.openConnection();
         var4.setRequestMethod(this.method);
         var4.setDoInput(true);
         Iterator var5 = this.headers.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            String var7 = (String)var6.getKey();
            String var8 = (String)var6.getValue();
            if (var8 != null && !var8.equals("")) {
               var4.setRequestProperty(var7, var8);
            }
         }

         if (this.outboundContent != null && this.method.equals("POST")) {
            var4.setDoOutput(true);
            byte[] var14 = this.outboundContent.getBytes("utf-8");
            var4.setRequestProperty("Content-Length", String.valueOf(var14.length));
            OutputStream var15 = var4.getOutputStream();
            var15.write(var14);
            var15.close();
         }

         var4.connect();
         this.fileSize = var4.getContentLength();
         this.setProgressMax(this.fileSize);
         this.responseHeaders = var4.getHeaderFields();
         var2 = new ProgressInputStream(var4.getInputStream());
      }

      Object var13;
      try {
         var13 = this.processStream(var2);
      } finally {
         var2.close();
      }

      return var13;
   }

   public void setHeader(String var1, String var2) {
      this.headers.put(var1, var2);
   }

   public String getResponseHeader(String var1) {
      String var2 = null;
      List var3 = (List)this.responseHeaders.get(var1);
      if (var3 != null) {
         StringBuilder var4 = new StringBuilder();
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            var4.append(var5.next());
            if (var5.hasNext()) {
               var4.append(',');
            }
         }

         var2 = var4.toString();
      }

      return var2;
   }

   protected class ProgressInputStream extends BufferedInputStream {
      public ProgressInputStream(InputStream var2) {
         super(var2);
      }

      public synchronized int read() throws IOException {
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
         } else {
            int var1 = super.read();
            AbstractRemoteResource.this.addProgress(1);
            return var1;
         }
      }

      public synchronized int read(byte[] var1, int var2, int var3) throws IOException {
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
         } else {
            int var4 = super.read(var1, var2, var3);
            AbstractRemoteResource.this.addProgress(var4);
            return var4;
         }
      }

      public int read(byte[] var1) throws IOException {
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
         } else {
            int var2 = super.read(var1);
            AbstractRemoteResource.this.addProgress(var2);
            return var2;
         }
      }
   }
}
