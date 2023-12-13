package io.netty.handler.codec.http;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DefaultCookie implements Cookie {
   private final String name;
   private String value;
   private String domain;
   private String path;
   private String comment;
   private String commentUrl;
   private boolean discard;
   private Set ports = Collections.emptySet();
   private Set unmodifiablePorts;
   private long maxAge;
   private int version;
   private boolean secure;
   private boolean httpOnly;

   public DefaultCookie(String var1, String var2) {
      this.unmodifiablePorts = this.ports;
      this.maxAge = Long.MIN_VALUE;
      if (var1 == null) {
         throw new NullPointerException("name");
      } else {
         var1 = var1.trim();
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("empty name");
         } else {
            int var3 = 0;

            while(var3 < var1.length()) {
               char var4 = var1.charAt(var3);
               if (var4 > 127) {
                  throw new IllegalArgumentException("name contains non-ascii character: " + var1);
               }

               switch(var4) {
               case '\t':
               case '\n':
               case '\u000b':
               case '\f':
               case '\r':
               case ' ':
               case ',':
               case ';':
               case '=':
                  throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + var1);
               default:
                  ++var3;
               }
            }

            if (var1.charAt(0) == '$') {
               throw new IllegalArgumentException("name starting with '$' not allowed: " + var1);
            } else {
               this.name = var1;
               this.setValue(var2);
            }
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      if (var1 == null) {
         throw new NullPointerException("value");
      } else {
         this.value = var1;
      }
   }

   public String getDomain() {
      return this.domain;
   }

   public void setDomain(String var1) {
      this.domain = validateValue("domain", var1);
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      this.path = validateValue("path", var1);
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String var1) {
      this.comment = validateValue("comment", var1);
   }

   public String getCommentUrl() {
      return this.commentUrl;
   }

   public void setCommentUrl(String var1) {
      this.commentUrl = validateValue("commentUrl", var1);
   }

   public boolean isDiscard() {
      return this.discard;
   }

   public void setDiscard(boolean var1) {
      this.discard = var1;
   }

   public Set getPorts() {
      if (this.unmodifiablePorts == null) {
         this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
      }

      return this.unmodifiablePorts;
   }

   public void setPorts(int... var1) {
      if (var1 == null) {
         throw new NullPointerException("ports");
      } else {
         int[] var2 = (int[])var1.clone();
         if (var2.length == 0) {
            this.unmodifiablePorts = this.ports = Collections.emptySet();
         } else {
            TreeSet var3 = new TreeSet();
            int[] var4 = var2;
            int var5 = var2.length;
            int var6 = 0;

            while(true) {
               if (var6 >= var5) {
                  this.ports = var3;
                  this.unmodifiablePorts = null;
                  break;
               }

               int var7 = var4[var6];
               if (var7 <= 0 || var7 > 65535) {
                  throw new IllegalArgumentException("port out of range: " + var7);
               }

               var3.add(var7);
               ++var6;
            }
         }

      }
   }

   public void setPorts(Iterable var1) {
      TreeSet var2 = new TreeSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         int var4 = (Integer)var3.next();
         if (var4 <= 0 || var4 > 65535) {
            throw new IllegalArgumentException("port out of range: " + var4);
         }

         var2.add(var4);
      }

      if (var2.isEmpty()) {
         this.unmodifiablePorts = this.ports = Collections.emptySet();
      } else {
         this.ports = var2;
         this.unmodifiablePorts = null;
      }

   }

   public long getMaxAge() {
      return this.maxAge;
   }

   public void setMaxAge(long var1) {
      this.maxAge = var1;
   }

   public int getVersion() {
      return this.version;
   }

   public void setVersion(int var1) {
      this.version = var1;
   }

   public boolean isSecure() {
      return this.secure;
   }

   public void setSecure(boolean var1) {
      this.secure = var1;
   }

   public boolean isHttpOnly() {
      return this.httpOnly;
   }

   public void setHttpOnly(boolean var1) {
      this.httpOnly = var1;
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Cookie)) {
         return false;
      } else {
         Cookie var2 = (Cookie)var1;
         if (!this.getName().equalsIgnoreCase(var2.getName())) {
            return false;
         } else {
            if (this.getPath() == null) {
               if (var2.getPath() != null) {
                  return false;
               }
            } else {
               if (var2.getPath() == null) {
                  return false;
               }

               if (!this.getPath().equals(var2.getPath())) {
                  return false;
               }
            }

            if (this.getDomain() == null) {
               return var2.getDomain() == null;
            } else {
               return var2.getDomain() == null ? false : this.getDomain().equalsIgnoreCase(var2.getDomain());
            }
         }
      }
   }

   public int compareTo(Cookie var1) {
      int var2 = this.getName().compareToIgnoreCase(var1.getName());
      if (var2 != 0) {
         return var2;
      } else {
         if (this.getPath() == null) {
            if (var1.getPath() != null) {
               return -1;
            }
         } else {
            if (var1.getPath() == null) {
               return 1;
            }

            var2 = this.getPath().compareTo(var1.getPath());
            if (var2 != 0) {
               return var2;
            }
         }

         if (this.getDomain() == null) {
            return var1.getDomain() != null ? -1 : 0;
         } else if (var1.getDomain() == null) {
            return 1;
         } else {
            var2 = this.getDomain().compareToIgnoreCase(var1.getDomain());
            return var2;
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getName());
      var1.append('=');
      var1.append(this.getValue());
      if (this.getDomain() != null) {
         var1.append(", domain=");
         var1.append(this.getDomain());
      }

      if (this.getPath() != null) {
         var1.append(", path=");
         var1.append(this.getPath());
      }

      if (this.getComment() != null) {
         var1.append(", comment=");
         var1.append(this.getComment());
      }

      if (this.getMaxAge() >= 0L) {
         var1.append(", maxAge=");
         var1.append(this.getMaxAge());
         var1.append('s');
      }

      if (this.isSecure()) {
         var1.append(", secure");
      }

      if (this.isHttpOnly()) {
         var1.append(", HTTPOnly");
      }

      return var1.toString();
   }

   private static String validateValue(String var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         if (var1.isEmpty()) {
            return null;
         } else {
            int var2 = 0;

            while(var2 < var1.length()) {
               char var3 = var1.charAt(var2);
               switch(var3) {
               case '\n':
               case '\u000b':
               case '\f':
               case '\r':
               case ';':
                  throw new IllegalArgumentException(var0 + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + var1 + ')');
               default:
                  ++var2;
               }
            }

            return var1;
         }
      }
   }

   public int compareTo(Object var1) {
      return this.compareTo((Cookie)var1);
   }
}
