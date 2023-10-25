package org.apache.http.impl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
  private final TreeSet<Cookie> cookies = new TreeSet<Cookie>((Comparator<? super Cookie>)new CookieIdentityComparator());
  
  public synchronized void addCookie(Cookie cookie) {
    if (cookie != null) {
      this.cookies.remove(cookie);
      if (!cookie.isExpired(new Date()))
        this.cookies.add(cookie); 
    } 
  }
  
  public synchronized void addCookies(Cookie[] cookies) {
    if (cookies != null)
      for (Cookie cooky : cookies)
        addCookie(cooky);  
  }
  
  public synchronized List<Cookie> getCookies() {
    return new ArrayList<Cookie>(this.cookies);
  }
  
  public synchronized boolean clearExpired(Date date) {
    if (date == null)
      return false; 
    boolean removed = false;
    for (Iterator<Cookie> it = this.cookies.iterator(); it.hasNext();) {
      if (((Cookie)it.next()).isExpired(date)) {
        it.remove();
        removed = true;
      } 
    } 
    return removed;
  }
  
  public synchronized void clear() {
    this.cookies.clear();
  }
  
  public synchronized String toString() {
    return this.cookies.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\BasicCookieStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */