package com.sun.webkit.network;

import java.net.URI;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Cookie {
   private static final Logger logger = Logger.getLogger(Cookie.class.getName());
   private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
   private final String name;
   private final String value;
   private final long expiryTime;
   private String domain;
   private String path;
   private ExtendedTime creationTime;
   private long lastAccessTime;
   private final boolean persistent;
   private boolean hostOnly;
   private final boolean secureOnly;
   private final boolean httpOnly;

   private Cookie(String var1, String var2, long var3, String var5, String var6, ExtendedTime var7, long var8, boolean var10, boolean var11, boolean var12, boolean var13) {
      this.name = var1;
      this.value = var2;
      this.expiryTime = var3;
      this.domain = var5;
      this.path = var6;
      this.creationTime = var7;
      this.lastAccessTime = var8;
      this.persistent = var10;
      this.hostOnly = var11;
      this.secureOnly = var12;
      this.httpOnly = var13;
   }

   static Cookie parse(String var0, ExtendedTime var1) {
      logger.log(Level.FINEST, "setCookieString: [{0}]", var0);
      String[] var2 = var0.split(";", -1);
      String[] var3 = var2[0].split("=", 2);
      if (var3.length != 2) {
         logger.log(Level.FINEST, "Name-value pair string lacks '=', ignoring cookie");
         return null;
      } else {
         String var4 = var3[0].trim();
         String var5 = var3[1].trim();
         if (var4.length() == 0) {
            logger.log(Level.FINEST, "Name string is empty, ignoring cookie");
            return null;
         } else {
            Long var6 = null;
            Long var7 = null;
            String var8 = null;
            String var9 = null;
            boolean var10 = false;
            boolean var11 = false;

            for(int var12 = 1; var12 < var2.length; ++var12) {
               String[] var13 = var2[var12].split("=", 2);
               String var14 = var13[0].trim();
               String var15 = (var13.length > 1 ? var13[1] : "").trim();

               try {
                  if ("Expires".equalsIgnoreCase(var14)) {
                     var6 = parseExpires(var15);
                  } else if ("Max-Age".equalsIgnoreCase(var14)) {
                     var7 = parseMaxAge(var15, var1.baseTime());
                  } else if ("Domain".equalsIgnoreCase(var14)) {
                     var8 = parseDomain(var15);
                  } else if ("Path".equalsIgnoreCase(var14)) {
                     var9 = parsePath(var15);
                  } else if ("Secure".equalsIgnoreCase(var14)) {
                     var10 = true;
                  } else if ("HttpOnly".equalsIgnoreCase(var14)) {
                     var11 = true;
                  } else {
                     logger.log(Level.FINEST, "Unknown attribute: [{0}], ignoring", var14);
                  }
               } catch (ParseException var17) {
                  logger.log(Level.FINEST, "{0}, ignoring", var17.getMessage());
               }
            }

            long var18;
            boolean var19;
            if (var7 != null) {
               var19 = true;
               var18 = var7;
            } else if (var6 != null) {
               var19 = true;
               var18 = var6;
            } else {
               var19 = false;
               var18 = Long.MAX_VALUE;
            }

            if (var8 == null) {
               var8 = "";
            }

            Cookie var20 = new Cookie(var4, var5, var18, var8, var9, var1, var1.baseTime(), var19, false, var10, var11);
            logger.log(Level.FINEST, "result: {0}", var20);
            return var20;
         }
      }
   }

   private static long parseExpires(String var0) throws ParseException {
      try {
         return Math.max(DateParser.parse(var0), 0L);
      } catch (ParseException var2) {
         throw new ParseException("Error parsing Expires attribute", 0);
      }
   }

   private static long parseMaxAge(String var0, long var1) throws ParseException {
      try {
         long var3 = Long.parseLong(var0);
         if (var3 <= 0L) {
            return 0L;
         } else {
            return var3 > (Long.MAX_VALUE - var1) / 1000L ? Long.MAX_VALUE : var1 + var3 * 1000L;
         }
      } catch (NumberFormatException var5) {
         throw new ParseException("Error parsing Max-Age attribute", 0);
      }
   }

   private static String parseDomain(String var0) throws ParseException {
      if (var0.length() == 0) {
         throw new ParseException("Domain attribute is empty", 0);
      } else {
         if (var0.startsWith(".")) {
            var0 = var0.substring(1);
         }

         return var0.toLowerCase();
      }
   }

   private static String parsePath(String var0) {
      return var0.startsWith("/") ? var0 : null;
   }

   String getName() {
      return this.name;
   }

   String getValue() {
      return this.value;
   }

   long getExpiryTime() {
      return this.expiryTime;
   }

   String getDomain() {
      return this.domain;
   }

   void setDomain(String var1) {
      this.domain = var1;
   }

   String getPath() {
      return this.path;
   }

   void setPath(String var1) {
      this.path = var1;
   }

   ExtendedTime getCreationTime() {
      return this.creationTime;
   }

   void setCreationTime(ExtendedTime var1) {
      this.creationTime = var1;
   }

   long getLastAccessTime() {
      return this.lastAccessTime;
   }

   void setLastAccessTime(long var1) {
      this.lastAccessTime = var1;
   }

   boolean getPersistent() {
      return this.persistent;
   }

   boolean getHostOnly() {
      return this.hostOnly;
   }

   void setHostOnly(boolean var1) {
      this.hostOnly = var1;
   }

   boolean getSecureOnly() {
      return this.secureOnly;
   }

   boolean getHttpOnly() {
      return this.httpOnly;
   }

   boolean hasExpired() {
      return System.currentTimeMillis() > this.expiryTime;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Cookie)) {
         return false;
      } else {
         Cookie var2 = (Cookie)var1;
         return equal(this.name, var2.name) && equal(this.domain, var2.domain) && equal(this.path, var2.path);
      }
   }

   private static boolean equal(Object var0, Object var1) {
      return var0 == null && var1 == null || var0 != null && var0.equals(var1);
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 53 * var1 + hashCode(this.name);
      var1 = 53 * var1 + hashCode(this.domain);
      var1 = 53 * var1 + hashCode(this.path);
      return var1;
   }

   private static int hashCode(Object var0) {
      return var0 != null ? var0.hashCode() : 0;
   }

   public String toString() {
      return "[name=" + this.name + ", value=" + this.value + ", expiryTime=" + this.expiryTime + ", domain=" + this.domain + ", path=" + this.path + ", creationTime=" + this.creationTime + ", lastAccessTime=" + this.lastAccessTime + ", persistent=" + this.persistent + ", hostOnly=" + this.hostOnly + ", secureOnly=" + this.secureOnly + ", httpOnly=" + this.httpOnly + "]";
   }

   static boolean domainMatches(String var0, String var1) {
      return var0.endsWith(var1) && (var0.length() == var1.length() || var0.charAt(var0.length() - var1.length() - 1) == '.' && !isIpAddress(var0));
   }

   private static boolean isIpAddress(String var0) {
      Matcher var1 = IP_ADDRESS_PATTERN.matcher(var0);
      if (!var1.matches()) {
         return false;
      } else {
         for(int var2 = 1; var2 <= var1.groupCount(); ++var2) {
            if (Integer.parseInt(var1.group(var2)) > 255) {
               return false;
            }
         }

         return true;
      }
   }

   static String defaultPath(URI var0) {
      String var1 = var0.getPath();
      if (var1 != null && var1.startsWith("/")) {
         var1 = var1.substring(0, var1.lastIndexOf("/"));
         return var1.length() == 0 ? "/" : var1;
      } else {
         return "/";
      }
   }

   static boolean pathMatches(String var0, String var1) {
      return var0 != null && var0.startsWith(var1) && (var0.length() == var1.length() || var1.endsWith("/") || var0.charAt(var1.length()) == '/');
   }
}
