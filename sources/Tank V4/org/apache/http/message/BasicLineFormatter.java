package org.apache.http.message;

import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicLineFormatter implements LineFormatter {
   /** @deprecated */
   @Deprecated
   public static final BasicLineFormatter DEFAULT = new BasicLineFormatter();
   public static final BasicLineFormatter INSTANCE = new BasicLineFormatter();

   protected CharArrayBuffer initBuffer(CharArrayBuffer var1) {
      CharArrayBuffer var2 = var1;
      if (var1 != null) {
         var1.clear();
      } else {
         var2 = new CharArrayBuffer(64);
      }

      return var2;
   }

   public static String formatProtocolVersion(ProtocolVersion var0, LineFormatter var1) {
      return ((LineFormatter)(var1 != null ? var1 : INSTANCE)).appendProtocolVersion((CharArrayBuffer)null, var0).toString();
   }

   public CharArrayBuffer appendProtocolVersion(CharArrayBuffer var1, ProtocolVersion var2) {
      Args.notNull(var2, "Protocol version");
      CharArrayBuffer var3 = var1;
      int var4 = this.estimateProtocolVersionLen(var2);
      if (var1 == null) {
         var3 = new CharArrayBuffer(var4);
      } else {
         var1.ensureCapacity(var4);
      }

      var3.append(var2.getProtocol());
      var3.append('/');
      var3.append(Integer.toString(var2.getMajor()));
      var3.append('.');
      var3.append(Integer.toString(var2.getMinor()));
      return var3;
   }

   protected int estimateProtocolVersionLen(ProtocolVersion var1) {
      return var1.getProtocol().length() + 4;
   }

   public static String formatRequestLine(RequestLine var0, LineFormatter var1) {
      return ((LineFormatter)(var1 != null ? var1 : INSTANCE)).formatRequestLine((CharArrayBuffer)null, var0).toString();
   }

   public CharArrayBuffer formatRequestLine(CharArrayBuffer var1, RequestLine var2) {
      Args.notNull(var2, "Request line");
      CharArrayBuffer var3 = this.initBuffer(var1);
      this.doFormatRequestLine(var3, var2);
      return var3;
   }

   protected void doFormatRequestLine(CharArrayBuffer var1, RequestLine var2) {
      String var3 = var2.getMethod();
      String var4 = var2.getUri();
      int var5 = var3.length() + 1 + var4.length() + 1 + this.estimateProtocolVersionLen(var2.getProtocolVersion());
      var1.ensureCapacity(var5);
      var1.append(var3);
      var1.append(' ');
      var1.append(var4);
      var1.append(' ');
      this.appendProtocolVersion(var1, var2.getProtocolVersion());
   }

   public static String formatStatusLine(StatusLine var0, LineFormatter var1) {
      return ((LineFormatter)(var1 != null ? var1 : INSTANCE)).formatStatusLine((CharArrayBuffer)null, var0).toString();
   }

   public CharArrayBuffer formatStatusLine(CharArrayBuffer var1, StatusLine var2) {
      Args.notNull(var2, "Status line");
      CharArrayBuffer var3 = this.initBuffer(var1);
      this.doFormatStatusLine(var3, var2);
      return var3;
   }

   protected void doFormatStatusLine(CharArrayBuffer var1, StatusLine var2) {
      int var3 = this.estimateProtocolVersionLen(var2.getProtocolVersion()) + 1 + 3 + 1;
      String var4 = var2.getReasonPhrase();
      if (var4 != null) {
         var3 += var4.length();
      }

      var1.ensureCapacity(var3);
      this.appendProtocolVersion(var1, var2.getProtocolVersion());
      var1.append(' ');
      var1.append(Integer.toString(var2.getStatusCode()));
      var1.append(' ');
      if (var4 != null) {
         var1.append(var4);
      }

   }

   public static String formatHeader(Header var0, LineFormatter var1) {
      return ((LineFormatter)(var1 != null ? var1 : INSTANCE)).formatHeader((CharArrayBuffer)null, var0).toString();
   }

   public CharArrayBuffer formatHeader(CharArrayBuffer var1, Header var2) {
      Args.notNull(var2, "Header");
      CharArrayBuffer var3;
      if (var2 instanceof FormattedHeader) {
         var3 = ((FormattedHeader)var2).getBuffer();
      } else {
         var3 = this.initBuffer(var1);
         this.doFormatHeader(var3, var2);
      }

      return var3;
   }

   protected void doFormatHeader(CharArrayBuffer var1, Header var2) {
      String var3 = var2.getName();
      String var4 = var2.getValue();
      int var5 = var3.length() + 2;
      if (var4 != null) {
         var5 += var4.length();
      }

      var1.ensureCapacity(var5);
      var1.append(var3);
      var1.append(": ");
      if (var4 != null) {
         var1.append(var4);
      }

   }
}
