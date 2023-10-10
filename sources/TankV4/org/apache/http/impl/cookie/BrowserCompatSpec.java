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
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BrowserCompatSpec extends CookieSpecBase {
   private static final String[] DEFAULT_DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z"};
   private final String[] datepatterns;

   public BrowserCompatSpec(String[] var1, BrowserCompatSpecFactory.SecurityLevel var2) {
      if (var1 != null) {
         this.datepatterns = (String[])var1.clone();
      } else {
         this.datepatterns = DEFAULT_DATE_PATTERNS;
      }

      switch(var2) {
      case SECURITYLEVEL_DEFAULT:
         this.registerAttribHandler("path", new BasicPathHandler());
         break;
      case SECURITYLEVEL_IE_MEDIUM:
         this.registerAttribHandler("path", new BasicPathHandler(this) {
            final BrowserCompatSpec this$0;

            {
               this.this$0 = var1;
            }

            public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
            }
         });
         break;
      default:
         throw new RuntimeException("Unknown security level");
      }

      this.registerAttribHandler("domain", new BasicDomainHandler());
      this.registerAttribHandler("max-age", new BasicMaxAgeHandler());
      this.registerAttribHandler("secure", new BasicSecureHandler());
      this.registerAttribHandler("comment", new BasicCommentHandler());
      this.registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
      this.registerAttribHandler("version", new BrowserCompatVersionAttributeHandler());
   }

   public BrowserCompatSpec(String[] var1) {
      this(var1, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpec() {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      String var3 = var1.getName();
      if (!var3.equalsIgnoreCase("Set-Cookie")) {
         throw new MalformedCookieException("Unrecognized cookie header '" + var1.toString() + "'");
      } else {
         HeaderElement[] var4 = var1.getElements();
         boolean var5 = false;
         boolean var6 = false;
         HeaderElement[] var7 = var4;
         int var8 = var4.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            HeaderElement var10 = var7[var9];
            if (var10.getParameterByName("version") != null) {
               var5 = true;
            }

            if (var10.getParameterByName("expires") != null) {
               var6 = true;
            }
         }

         if (var6 || !var5) {
            NetscapeDraftHeaderParser var11 = NetscapeDraftHeaderParser.DEFAULT;
            CharArrayBuffer var12;
            ParserCursor var13;
            if (var1 instanceof FormattedHeader) {
               var12 = ((FormattedHeader)var1).getBuffer();
               var13 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var12.length());
            } else {
               String var14 = var1.getValue();
               if (var14 == null) {
                  throw new MalformedCookieException("Header value is null");
               }

               var12 = new CharArrayBuffer(var14.length());
               var12.append(var14);
               var13 = new ParserCursor(0, var12.length());
            }

            var4 = new HeaderElement[]{var11.parseHeader(var12, var13)};
         }

         return this.parse(var4, var2);
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

         String var5 = var4.getName();
         String var6 = var4.getValue();
         if (var4.getVersion() > 0 && var6 != null) {
            BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(var2, new BasicHeaderElement(var5, var6), false);
         } else {
            var2.append(var5);
            var2.append("=");
            if (var6 != null) {
               var2.append(var6);
            }
         }
      }

      ArrayList var7 = new ArrayList(1);
      var7.add(new BufferedHeader(var2));
      return var7;
   }

   public int getVersion() {
      return 0;
   }

   public Header getVersionHeader() {
      return null;
   }

   public String toString() {
      return "compatibility";
   }
}
