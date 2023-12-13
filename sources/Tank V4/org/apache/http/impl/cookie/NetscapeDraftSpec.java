package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class NetscapeDraftSpec extends CookieSpecBase {
   protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";
   private final String[] datepatterns;

   public NetscapeDraftSpec(String[] var1) {
      if (var1 != null) {
         this.datepatterns = (String[])var1.clone();
      } else {
         this.datepatterns = new String[]{"EEE, dd-MMM-yy HH:mm:ss z"};
      }

      this.registerAttribHandler("path", new BasicPathHandler());
      this.registerAttribHandler("domain", new NetscapeDomainHandler());
      this.registerAttribHandler("max-age", new BasicMaxAgeHandler());
      this.registerAttribHandler("secure", new BasicSecureHandler());
      this.registerAttribHandler("comment", new BasicCommentHandler());
      this.registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
   }

   public NetscapeDraftSpec() {
      this((String[])null);
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      if (!var1.getName().equalsIgnoreCase("Set-Cookie")) {
         throw new MalformedCookieException("Unrecognized cookie header '" + var1.toString() + "'");
      } else {
         NetscapeDraftHeaderParser var3 = NetscapeDraftHeaderParser.DEFAULT;
         CharArrayBuffer var4;
         ParserCursor var5;
         if (var1 instanceof FormattedHeader) {
            var4 = ((FormattedHeader)var1).getBuffer();
            var5 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var4.length());
         } else {
            String var6 = var1.getValue();
            if (var6 == null) {
               throw new MalformedCookieException("Header value is null");
            }

            var4 = new CharArrayBuffer(var6.length());
            var4.append(var6);
            var5 = new ParserCursor(0, var4.length());
         }

         return this.parse(new HeaderElement[]{var3.parseHeader(var4, var5)}, var2);
      }
   }

   public List formatCookies(List var1) {
      Args.notEmpty((Collection)var1, "List of cookies");
      CharArrayBuffer var2 = new CharArrayBuffer(20 * var1.size());
      var2.append("Cookie");
      var2.append(": ");

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         Cookie var4 = (Cookie)var1.get(var3);
         if (var3 > 0) {
            var2.append("; ");
         }

         var2.append(var4.getName());
         String var5 = var4.getValue();
         if (var5 != null) {
            var2.append("=");
            var2.append(var5);
         }
      }

      ArrayList var6 = new ArrayList(1);
      var6.add(new BufferedHeader(var2));
      return var6;
   }

   public int getVersion() {
      return 0;
   }

   public Header getVersionHeader() {
      return null;
   }

   public String toString() {
      return "netscape";
   }
}
