package org.apache.http.impl.cookie;

import java.util.Iterator;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BestMatchSpec implements CookieSpec {
   private final String[] datepatterns;
   private final boolean oneHeader;
   private RFC2965Spec strict;
   private RFC2109Spec obsoleteStrict;
   private BrowserCompatSpec compat;

   public BestMatchSpec(String[] var1, boolean var2) {
      this.datepatterns = var1 == null ? null : (String[])var1.clone();
      this.oneHeader = var2;
   }

   public BestMatchSpec() {
      this((String[])null, false);
   }

   private RFC2965Spec getStrict() {
      if (this.strict == null) {
         this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader);
      }

      return this.strict;
   }

   private RFC2109Spec getObsoleteStrict() {
      if (this.obsoleteStrict == null) {
         this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader);
      }

      return this.obsoleteStrict;
   }

   private BrowserCompatSpec getCompat() {
      if (this.compat == null) {
         this.compat = new BrowserCompatSpec(this.datepatterns);
      }

      return this.compat;
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      HeaderElement[] var3 = var1.getElements();
      boolean var4 = false;
      boolean var5 = false;
      HeaderElement[] var6 = var3;
      int var7 = var3.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         HeaderElement var9 = var6[var8];
         if (var9.getParameterByName("version") != null) {
            var4 = true;
         }

         if (var9.getParameterByName("expires") != null) {
            var5 = true;
         }
      }

      if (!var5 && var4) {
         if ("Set-Cookie2".equals(var1.getName())) {
            return this.getStrict().parse(var3, var2);
         } else {
            return this.getObsoleteStrict().parse(var3, var2);
         }
      } else {
         NetscapeDraftHeaderParser var10 = NetscapeDraftHeaderParser.DEFAULT;
         CharArrayBuffer var11;
         ParserCursor var12;
         if (var1 instanceof FormattedHeader) {
            var11 = ((FormattedHeader)var1).getBuffer();
            var12 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var11.length());
         } else {
            String var13 = var1.getValue();
            if (var13 == null) {
               throw new MalformedCookieException("Header value is null");
            }

            var11 = new CharArrayBuffer(var13.length());
            var11.append(var13);
            var12 = new ParserCursor(0, var11.length());
         }

         var3 = new HeaderElement[]{var10.parseHeader(var11, var12)};
         return this.getCompat().parse(var3, var2);
      }
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      if (var1.getVersion() > 0) {
         if (var1 instanceof SetCookie2) {
            this.getStrict().validate(var1, var2);
         } else {
            this.getObsoleteStrict().validate(var1, var2);
         }
      } else {
         this.getCompat().validate(var1, var2);
      }

   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      if (var1.getVersion() > 0) {
         return var1 instanceof SetCookie2 ? this.getStrict().match(var1, var2) : this.getObsoleteStrict().match(var1, var2);
      } else {
         return this.getCompat().match(var1, var2);
      }
   }

   public List formatCookies(List var1) {
      Args.notNull(var1, "List of cookies");
      int var2 = Integer.MAX_VALUE;
      boolean var3 = true;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Cookie var5 = (Cookie)var4.next();
         if (!(var5 instanceof SetCookie2)) {
            var3 = false;
         }

         if (var5.getVersion() < var2) {
            var2 = var5.getVersion();
         }
      }

      if (var2 > 0) {
         if (var3) {
            return this.getStrict().formatCookies(var1);
         } else {
            return this.getObsoleteStrict().formatCookies(var1);
         }
      } else {
         return this.getCompat().formatCookies(var1);
      }
   }

   public int getVersion() {
      return this.getStrict().getVersion();
   }

   public Header getVersionHeader() {
      return this.getStrict().getVersionHeader();
   }

   public String toString() {
      return "best-match";
   }
}
