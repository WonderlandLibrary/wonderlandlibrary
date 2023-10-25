package io.netty.handler.codec.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public final class ServerCookieEncoder {
  public static String encode(String name, String value) {
    return encode(new DefaultCookie(name, value));
  }
  
  public static String encode(Cookie cookie) {
    if (cookie == null)
      throw new NullPointerException("cookie"); 
    StringBuilder buf = CookieEncoderUtil.stringBuilder();
    CookieEncoderUtil.add(buf, cookie.getName(), cookie.getValue());
    if (cookie.getMaxAge() != Long.MIN_VALUE)
      if (cookie.getVersion() == 0) {
        CookieEncoderUtil.addUnquoted(buf, "Expires", HttpHeaderDateFormat.get().format(new Date(System.currentTimeMillis() + cookie.getMaxAge() * 1000L)));
      } else {
        CookieEncoderUtil.add(buf, "Max-Age", cookie.getMaxAge());
      }  
    if (cookie.getPath() != null)
      if (cookie.getVersion() > 0) {
        CookieEncoderUtil.add(buf, "Path", cookie.getPath());
      } else {
        CookieEncoderUtil.addUnquoted(buf, "Path", cookie.getPath());
      }  
    if (cookie.getDomain() != null)
      if (cookie.getVersion() > 0) {
        CookieEncoderUtil.add(buf, "Domain", cookie.getDomain());
      } else {
        CookieEncoderUtil.addUnquoted(buf, "Domain", cookie.getDomain());
      }  
    if (cookie.isSecure()) {
      buf.append("Secure");
      buf.append(';');
      buf.append(' ');
    } 
    if (cookie.isHttpOnly()) {
      buf.append("HTTPOnly");
      buf.append(';');
      buf.append(' ');
    } 
    if (cookie.getVersion() >= 1) {
      if (cookie.getComment() != null)
        CookieEncoderUtil.add(buf, "Comment", cookie.getComment()); 
      CookieEncoderUtil.add(buf, "Version", 1L);
      if (cookie.getCommentUrl() != null)
        CookieEncoderUtil.addQuoted(buf, "CommentURL", cookie.getCommentUrl()); 
      if (!cookie.getPorts().isEmpty()) {
        buf.append("Port");
        buf.append('=');
        buf.append('"');
        for (Iterator<Integer> i$ = cookie.getPorts().iterator(); i$.hasNext(); ) {
          int port = ((Integer)i$.next()).intValue();
          buf.append(port);
          buf.append(',');
        } 
        buf.setCharAt(buf.length() - 1, '"');
        buf.append(';');
        buf.append(' ');
      } 
      if (cookie.isDiscard()) {
        buf.append("Discard");
        buf.append(';');
        buf.append(' ');
      } 
    } 
    return CookieEncoderUtil.stripTrailingSeparator(buf);
  }
  
  public static List<String> encode(Cookie... cookies) {
    if (cookies == null)
      throw new NullPointerException("cookies"); 
    List<String> encoded = new ArrayList<String>(cookies.length);
    for (Cookie c : cookies) {
      if (c == null)
        break; 
      encoded.add(encode(c));
    } 
    return encoded;
  }
  
  public static List<String> encode(Collection<Cookie> cookies) {
    if (cookies == null)
      throw new NullPointerException("cookies"); 
    List<String> encoded = new ArrayList<String>(cookies.size());
    for (Cookie c : cookies) {
      if (c == null)
        break; 
      encoded.add(encode(c));
    } 
    return encoded;
  }
  
  public static List<String> encode(Iterable<Cookie> cookies) {
    if (cookies == null)
      throw new NullPointerException("cookies"); 
    List<String> encoded = new ArrayList<String>();
    for (Cookie c : cookies) {
      if (c == null)
        break; 
      encoded.add(encode(c));
    } 
    return encoded;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\ServerCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */