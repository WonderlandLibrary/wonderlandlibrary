package org.apache.http.params;

import java.nio.charset.CodingErrorAction;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
public final class HttpProtocolParams implements CoreProtocolPNames {
   private HttpProtocolParams() {
   }

   public static String getHttpElementCharset(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      String var1 = (String)var0.getParameter("http.protocol.element-charset");
      if (var1 == null) {
         var1 = HTTP.DEF_PROTOCOL_CHARSET.name();
      }

      return var1;
   }

   public static void setHttpElementCharset(HttpParams var0, String var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.protocol.element-charset", var1);
   }

   public static String getContentCharset(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      String var1 = (String)var0.getParameter("http.protocol.content-charset");
      if (var1 == null) {
         var1 = HTTP.DEF_CONTENT_CHARSET.name();
      }

      return var1;
   }

   public static void setContentCharset(HttpParams var0, String var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.protocol.content-charset", var1);
   }

   public static ProtocolVersion getVersion(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      Object var1 = var0.getParameter("http.protocol.version");
      return (ProtocolVersion)(var1 == null ? HttpVersion.HTTP_1_1 : (ProtocolVersion)var1);
   }

   public static void setVersion(HttpParams var0, ProtocolVersion var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.protocol.version", var1);
   }

   public static String getUserAgent(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return (String)var0.getParameter("http.useragent");
   }

   public static void setUserAgent(HttpParams var0, String var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.useragent", var1);
   }

   public static boolean useExpectContinue(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      return var0.getBooleanParameter("http.protocol.expect-continue", false);
   }

   public static void setUseExpectContinue(HttpParams var0, boolean var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setBooleanParameter("http.protocol.expect-continue", var1);
   }

   public static CodingErrorAction getMalformedInputAction(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      Object var1 = var0.getParameter("http.malformed.input.action");
      return var1 == null ? CodingErrorAction.REPORT : (CodingErrorAction)var1;
   }

   public static void setMalformedInputAction(HttpParams var0, CodingErrorAction var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.malformed.input.action", var1);
   }

   public static CodingErrorAction getUnmappableInputAction(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      Object var1 = var0.getParameter("http.unmappable.input.action");
      return var1 == null ? CodingErrorAction.REPORT : (CodingErrorAction)var1;
   }

   public static void setUnmappableInputAction(HttpParams var0, CodingErrorAction var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.unmappable.input.action", var1);
   }
}
