package com.mojang.authlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HttpAuthenticationService extends BaseAuthenticationService {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Proxy proxy;

   protected HttpAuthenticationService(Proxy var1) {
      Validate.notNull(var1);
      this.proxy = var1;
   }

   public Proxy getProxy() {
      return this.proxy;
   }

   protected HttpURLConnection createUrlConnection(URL var1) throws IOException {
      Validate.notNull(var1);
      LOGGER.debug("Opening connection to " + var1);
      HttpURLConnection var2 = (HttpURLConnection)var1.openConnection(this.proxy);
      var2.setConnectTimeout(15000);
      var2.setReadTimeout(15000);
      var2.setUseCaches(false);
      return var2;
   }

   public String performPostRequest(URL var1, String var2, String var3) throws IOException {
      Validate.notNull(var1);
      Validate.notNull(var2);
      Validate.notNull(var3);
      HttpURLConnection var4 = this.createUrlConnection(var1);
      byte[] var5 = var2.getBytes(Charsets.UTF_8);
      var4.setRequestProperty("Content-Type", var3 + "; charset=utf-8");
      var4.setRequestProperty("Content-Length", "" + var5.length);
      var4.setDoOutput(true);
      LOGGER.debug("Writing POST data to " + var1 + ": " + var2);
      OutputStream var6 = null;
      var6 = var4.getOutputStream();
      IOUtils.write(var5, var6);
      IOUtils.closeQuietly(var6);
      LOGGER.debug("Reading data from " + var1);
      InputStream var7 = null;

      String var9;
      try {
         var7 = var4.getInputStream();
         String var8 = IOUtils.toString(var7, Charsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + var4.getResponseCode());
         LOGGER.debug("Response: " + var8);
         var9 = var8;
      } catch (IOException var12) {
         IOUtils.closeQuietly(var7);
         var7 = var4.getErrorStream();
         if (var7 != null) {
            LOGGER.debug("Reading error page from " + var1);
            var9 = IOUtils.toString(var7, Charsets.UTF_8);
            LOGGER.debug("Successful read, server response was " + var4.getResponseCode());
            LOGGER.debug("Response: " + var9);
            IOUtils.closeQuietly(var7);
            return var9;
         }

         LOGGER.debug((String)"Request failed", (Throwable)var12);
         throw var12;
      }

      IOUtils.closeQuietly(var7);
      return var9;
   }

   public String performGetRequest(URL var1) throws IOException {
      Validate.notNull(var1);
      HttpURLConnection var2 = this.createUrlConnection(var1);
      LOGGER.debug("Reading data from " + var1);
      InputStream var3 = null;

      String var5;
      try {
         var3 = var2.getInputStream();
         String var4 = IOUtils.toString(var3, Charsets.UTF_8);
         LOGGER.debug("Successful read, server response was " + var2.getResponseCode());
         LOGGER.debug("Response: " + var4);
         var5 = var4;
      } catch (IOException var8) {
         IOUtils.closeQuietly(var3);
         var3 = var2.getErrorStream();
         if (var3 != null) {
            LOGGER.debug("Reading error page from " + var1);
            var5 = IOUtils.toString(var3, Charsets.UTF_8);
            LOGGER.debug("Successful read, server response was " + var2.getResponseCode());
            LOGGER.debug("Response: " + var5);
            IOUtils.closeQuietly(var3);
            return var5;
         }

         LOGGER.debug((String)"Request failed", (Throwable)var8);
         throw var8;
      }

      IOUtils.closeQuietly(var3);
      return var5;
   }

   public static URL constantURL(String var0) {
      try {
         return new URL(var0);
      } catch (MalformedURLException var2) {
         throw new Error("Couldn't create constant for " + var0, var2);
      }
   }

   public static String buildQuery(Map var0) {
      if (var0 == null) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder();
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            if (var1.length() > 0) {
               var1.append('&');
            }

            try {
               var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException var6) {
               LOGGER.error((String)"Unexpected exception building query", (Throwable)var6);
            }

            if (var3.getValue() != null) {
               var1.append('=');

               try {
                  var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
               } catch (UnsupportedEncodingException var5) {
                  LOGGER.error((String)"Unexpected exception building query", (Throwable)var5);
               }
            }
         }

         return var1.toString();
      }
   }

   public static URL concatenateURL(URL var0, String var1) {
      try {
         return var0.getQuery() != null && var0.getQuery().length() > 0 ? new URL(var0.getProtocol(), var0.getHost(), var0.getPort(), var0.getFile() + "&" + var1) : new URL(var0.getProtocol(), var0.getHost(), var0.getPort(), var0.getFile() + "?" + var1);
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", var3);
      }
   }
}
