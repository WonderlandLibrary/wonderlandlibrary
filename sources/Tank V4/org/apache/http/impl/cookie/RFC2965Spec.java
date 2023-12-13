package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class RFC2965Spec extends RFC2109Spec {
   public RFC2965Spec() {
      this((String[])null, false);
   }

   public RFC2965Spec(String[] var1, boolean var2) {
      super(var1, var2);
      this.registerAttribHandler("domain", new RFC2965DomainAttributeHandler());
      this.registerAttribHandler("port", new RFC2965PortAttributeHandler());
      this.registerAttribHandler("commenturl", new RFC2965CommentUrlAttributeHandler());
      this.registerAttribHandler("discard", new RFC2965DiscardAttributeHandler());
      this.registerAttribHandler("version", new RFC2965VersionAttributeHandler());
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      if (!var1.getName().equalsIgnoreCase("Set-Cookie2")) {
         throw new MalformedCookieException("Unrecognized cookie header '" + var1.toString() + "'");
      } else {
         HeaderElement[] var3 = var1.getElements();
         return this.createCookies(var3, adjustEffectiveHost(var2));
      }
   }

   protected List parse(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      return this.createCookies(var1, adjustEffectiveHost(var2));
   }

   private List createCookies(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      ArrayList var3 = new ArrayList(var1.length);
      HeaderElement[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         HeaderElement var7 = var4[var6];
         String var8 = var7.getName();
         String var9 = var7.getValue();
         if (var8 == null || var8.length() == 0) {
            throw new MalformedCookieException("Cookie name may not be empty");
         }

         BasicClientCookie2 var10 = new BasicClientCookie2(var8, var9);
         var10.setPath(getDefaultPath(var2));
         var10.setDomain(getDefaultDomain(var2));
         var10.setPorts(new int[]{var2.getPort()});
         NameValuePair[] var11 = var7.getParameters();
         HashMap var12 = new HashMap(var11.length);

         for(int var13 = var11.length - 1; var13 >= 0; --var13) {
            NameValuePair var14 = var11[var13];
            var12.put(var14.getName().toLowerCase(Locale.ENGLISH), var14);
         }

         Iterator var18 = var12.entrySet().iterator();

         while(var18.hasNext()) {
            Entry var19 = (Entry)var18.next();
            NameValuePair var15 = (NameValuePair)var19.getValue();
            String var16 = var15.getName().toLowerCase(Locale.ENGLISH);
            var10.setAttribute(var16, var15.getValue());
            CookieAttributeHandler var17 = this.findAttribHandler(var16);
            if (var17 != null) {
               var17.parse(var10, var15.getValue());
            }
         }

         var3.add(var10);
      }

      return var3;
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      super.validate(var1, adjustEffectiveHost(var2));
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      return super.match(var1, adjustEffectiveHost(var2));
   }

   protected void formatCookieAsVer(CharArrayBuffer var1, Cookie var2, int var3) {
      super.formatCookieAsVer(var1, var2, var3);
      if (var2 instanceof ClientCookie) {
         String var4 = ((ClientCookie)var2).getAttribute("port");
         if (var4 != null) {
            var1.append("; $Port");
            var1.append("=\"");
            if (var4.trim().length() > 0) {
               int[] var5 = var2.getPorts();
               if (var5 != null) {
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     if (var7 > 0) {
                        var1.append(",");
                     }

                     var1.append(Integer.toString(var5[var7]));
                  }
               }
            }

            var1.append("\"");
         }
      }

   }

   private static CookieOrigin adjustEffectiveHost(CookieOrigin var0) {
      String var1 = var0.getHost();
      boolean var2 = true;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == '.' || var4 == ':') {
            var2 = false;
            break;
         }
      }

      if (var2) {
         var1 = var1 + ".local";
         return new CookieOrigin(var1, var0.getPort(), var0.getPath(), var0.isSecure());
      } else {
         return var0;
      }
   }

   public int getVersion() {
      return 1;
   }

   public Header getVersionHeader() {
      CharArrayBuffer var1 = new CharArrayBuffer(40);
      var1.append("Cookie2");
      var1.append(": ");
      var1.append("$Version=");
      var1.append(Integer.toString(this.getVersion()));
      return new BufferedHeader(var1);
   }

   public String toString() {
      return "rfc2965";
   }
}
