package org.apache.http.impl.cookie;

import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
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
  
  public BestMatchSpec(String[] datepatterns, boolean oneHeader) {
    this.datepatterns = (datepatterns == null) ? null : (String[])datepatterns.clone();
    this.oneHeader = oneHeader;
  }
  
  public BestMatchSpec() {
    this(null, false);
  }
  
  private RFC2965Spec getStrict() {
    if (this.strict == null)
      this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader); 
    return this.strict;
  }
  
  private RFC2109Spec getObsoleteStrict() {
    if (this.obsoleteStrict == null)
      this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader); 
    return this.obsoleteStrict;
  }
  
  private BrowserCompatSpec getCompat() {
    if (this.compat == null)
      this.compat = new BrowserCompatSpec(this.datepatterns); 
    return this.compat;
  }
  
  public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
    Args.notNull(header, "Header");
    Args.notNull(origin, "Cookie origin");
    HeaderElement[] helems = header.getElements();
    boolean versioned = false;
    boolean netscape = false;
    for (HeaderElement helem : helems) {
      if (helem.getParameterByName("version") != null)
        versioned = true; 
      if (helem.getParameterByName("expires") != null)
        netscape = true; 
    } 
    if (netscape || !versioned) {
      CharArrayBuffer buffer;
      ParserCursor cursor;
      NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
      if (header instanceof FormattedHeader) {
        buffer = ((FormattedHeader)header).getBuffer();
        cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
      } else {
        String s = header.getValue();
        if (s == null)
          throw new MalformedCookieException("Header value is null"); 
        buffer = new CharArrayBuffer(s.length());
        buffer.append(s);
        cursor = new ParserCursor(0, buffer.length());
      } 
      helems = new HeaderElement[] { parser.parseHeader(buffer, cursor) };
      return getCompat().parse(helems, origin);
    } 
    if ("Set-Cookie2".equals(header.getName()))
      return getStrict().parse(helems, origin); 
    return getObsoleteStrict().parse(helems, origin);
  }
  
  public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    if (cookie.getVersion() > 0) {
      if (cookie instanceof org.apache.http.cookie.SetCookie2) {
        getStrict().validate(cookie, origin);
      } else {
        getObsoleteStrict().validate(cookie, origin);
      } 
    } else {
      getCompat().validate(cookie, origin);
    } 
  }
  
  public boolean match(Cookie cookie, CookieOrigin origin) {
    Args.notNull(cookie, "Cookie");
    Args.notNull(origin, "Cookie origin");
    if (cookie.getVersion() > 0) {
      if (cookie instanceof org.apache.http.cookie.SetCookie2)
        return getStrict().match(cookie, origin); 
      return getObsoleteStrict().match(cookie, origin);
    } 
    return getCompat().match(cookie, origin);
  }
  
  public List<Header> formatCookies(List<Cookie> cookies) {
    Args.notNull(cookies, "List of cookies");
    int version = Integer.MAX_VALUE;
    boolean isSetCookie2 = true;
    for (Cookie cookie : cookies) {
      if (!(cookie instanceof org.apache.http.cookie.SetCookie2))
        isSetCookie2 = false; 
      if (cookie.getVersion() < version)
        version = cookie.getVersion(); 
    } 
    if (version > 0) {
      if (isSetCookie2)
        return getStrict().formatCookies(cookies); 
      return getObsoleteStrict().formatCookies(cookies);
    } 
    return getCompat().formatCookies(cookies);
  }
  
  public int getVersion() {
    return getStrict().getVersion();
  }
  
  public Header getVersionHeader() {
    return getStrict().getVersionHeader();
  }
  
  public String toString() {
    return "best-match";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\BestMatchSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */