package org.apache.http.impl.cookie;

import java.util.ArrayList;
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
  private static final String[] DEFAULT_DATE_PATTERNS = new String[] { 
      "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", 
      "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" };
  
  private final String[] datepatterns;
  
  public BrowserCompatSpec(String[] datepatterns, BrowserCompatSpecFactory.SecurityLevel securityLevel) {
    if (datepatterns != null) {
      this.datepatterns = (String[])datepatterns.clone();
    } else {
      this.datepatterns = DEFAULT_DATE_PATTERNS;
    } 
    switch (securityLevel) {
      case SECURITYLEVEL_DEFAULT:
        registerAttribHandler("path", new BasicPathHandler());
        break;
      case SECURITYLEVEL_IE_MEDIUM:
        registerAttribHandler("path", new BasicPathHandler() {
              public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
            });
        break;
      default:
        throw new RuntimeException("Unknown security level");
    } 
    registerAttribHandler("domain", new BasicDomainHandler());
    registerAttribHandler("max-age", new BasicMaxAgeHandler());
    registerAttribHandler("secure", new BasicSecureHandler());
    registerAttribHandler("comment", new BasicCommentHandler());
    registerAttribHandler("expires", new BasicExpiresHandler(this.datepatterns));
    registerAttribHandler("version", new BrowserCompatVersionAttributeHandler());
  }
  
  public BrowserCompatSpec(String[] datepatterns) {
    this(datepatterns, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public BrowserCompatSpec() {
    this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
  }
  
  public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
    Args.notNull(header, "Header");
    Args.notNull(origin, "Cookie origin");
    String headername = header.getName();
    if (!headername.equalsIgnoreCase("Set-Cookie"))
      throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'"); 
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
    } 
    return parse(helems, origin);
  }
  
  private static boolean isQuoteEnclosed(String s) {
    return (s != null && s.startsWith("\"") && s.endsWith("\""));
  }
  
  public List<Header> formatCookies(List<Cookie> cookies) {
    Args.notEmpty(cookies, "List of cookies");
    CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
    buffer.append("Cookie");
    buffer.append(": ");
    for (int i = 0; i < cookies.size(); i++) {
      Cookie cookie = cookies.get(i);
      if (i > 0)
        buffer.append("; "); 
      String cookieName = cookie.getName();
      String cookieValue = cookie.getValue();
      if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
        BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(buffer, (HeaderElement)new BasicHeaderElement(cookieName, cookieValue), false);
      } else {
        buffer.append(cookieName);
        buffer.append("=");
        if (cookieValue != null)
          buffer.append(cookieValue); 
      } 
    } 
    List<Header> headers = new ArrayList<Header>(1);
    headers.add(new BufferedHeader(buffer));
    return headers;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\BrowserCompatSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */