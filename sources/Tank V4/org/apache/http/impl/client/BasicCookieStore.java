package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;

@ThreadSafe
public class BasicCookieStore implements CookieStore, Serializable {
   private static final long serialVersionUID = -7581093305228232025L;
   @GuardedBy("this")
   private final TreeSet cookies = new TreeSet(new CookieIdentityComparator());

   public synchronized void addCookie(Cookie var1) {
      if (var1 != null) {
         this.cookies.remove(var1);
         if (!var1.isExpired(new Date())) {
            this.cookies.add(var1);
         }
      }

   }

   public synchronized void addCookies(Cookie[] var1) {
      if (var1 != null) {
         Cookie[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Cookie var5 = var2[var4];
            this.addCookie(var5);
         }
      }

   }

   public synchronized List getCookies() {
      return new ArrayList(this.cookies);
   }

   public synchronized boolean clearExpired(Date var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;
         Iterator var3 = this.cookies.iterator();

         while(var3.hasNext()) {
            if (((Cookie)var3.next()).isExpired(var1)) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }
   }

   public synchronized void clear() {
      this.cookies.clear();
   }

   public synchronized String toString() {
      return this.cookies.toString();
   }
}
