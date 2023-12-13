package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class HeaderGroup implements Cloneable, Serializable {
   private static final long serialVersionUID = 2608834160639271617L;
   private final List headers = new ArrayList(16);

   public void clear() {
      this.headers.clear();
   }

   public void addHeader(Header var1) {
      if (var1 != null) {
         this.headers.add(var1);
      }
   }

   public void removeHeader(Header var1) {
      if (var1 != null) {
         this.headers.remove(var1);
      }
   }

   public void updateHeader(Header var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < this.headers.size(); ++var2) {
            Header var3 = (Header)this.headers.get(var2);
            if (var3.getName().equalsIgnoreCase(var1.getName())) {
               this.headers.set(var2, var1);
               return;
            }
         }

         this.headers.add(var1);
      }
   }

   public void setHeaders(Header[] var1) {
      this.clear();
      if (var1 != null) {
         Collections.addAll(this.headers, var1);
      }
   }

   public Header getCondensedHeader(String var1) {
      Header[] var2 = this.getHeaders(var1);
      if (var2.length == 0) {
         return null;
      } else if (var2.length == 1) {
         return var2[0];
      } else {
         CharArrayBuffer var3 = new CharArrayBuffer(128);
         var3.append(var2[0].getValue());

         for(int var4 = 1; var4 < var2.length; ++var4) {
            var3.append(", ");
            var3.append(var2[var4].getValue());
         }

         return new BasicHeader(var1.toLowerCase(Locale.ENGLISH), var3.toString());
      }
   }

   public Header[] getHeaders(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.headers.size(); ++var3) {
         Header var4 = (Header)this.headers.get(var3);
         if (var4.getName().equalsIgnoreCase(var1)) {
            var2.add(var4);
         }
      }

      return (Header[])var2.toArray(new Header[var2.size()]);
   }

   public Header getFirstHeader(String var1) {
      for(int var2 = 0; var2 < this.headers.size(); ++var2) {
         Header var3 = (Header)this.headers.get(var2);
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public Header getLastHeader(String var1) {
      for(int var2 = this.headers.size() - 1; var2 >= 0; --var2) {
         Header var3 = (Header)this.headers.get(var2);
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public Header[] getAllHeaders() {
      return (Header[])this.headers.toArray(new Header[this.headers.size()]);
   }

   public boolean containsHeader(String var1) {
      for(int var2 = 0; var2 < this.headers.size(); ++var2) {
         Header var3 = (Header)this.headers.get(var2);
         if (var3.getName().equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }

   public HeaderIterator iterator() {
      return new BasicListHeaderIterator(this.headers, (String)null);
   }

   public HeaderIterator iterator(String var1) {
      return new BasicListHeaderIterator(this.headers, var1);
   }

   public HeaderGroup copy() {
      HeaderGroup var1 = new HeaderGroup();
      var1.headers.addAll(this.headers);
      return var1;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public String toString() {
      return this.headers.toString();
   }
}
