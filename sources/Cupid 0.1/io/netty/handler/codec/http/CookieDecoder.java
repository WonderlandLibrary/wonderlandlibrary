package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class CookieDecoder {
  private static final char COMMA = ',';
  
  public static Set<Cookie> decode(String header) {
    int i;
    List<String> names = new ArrayList<String>(8);
    List<String> values = new ArrayList<String>(8);
    extractKeyValuePairs(header, names, values);
    if (names.isEmpty())
      return Collections.emptySet(); 
    int version = 0;
    if (((String)names.get(0)).equalsIgnoreCase("Version")) {
      try {
        version = Integer.parseInt(values.get(0));
      } catch (NumberFormatException e) {}
      i = 1;
    } else {
      i = 0;
    } 
    if (names.size() <= i)
      return Collections.emptySet(); 
    Set<Cookie> cookies = new TreeSet<Cookie>();
    for (; i < names.size(); i++) {
      String name = names.get(i);
      String value = values.get(i);
      if (value == null)
        value = ""; 
      Cookie c = new DefaultCookie(name, value);
      boolean discard = false;
      boolean secure = false;
      boolean httpOnly = false;
      String comment = null;
      String commentURL = null;
      String domain = null;
      String path = null;
      long maxAge = Long.MIN_VALUE;
      List<Integer> ports = new ArrayList<Integer>(2);
      for (int j = i + 1; j < names.size(); j++, i++) {
        name = names.get(j);
        value = values.get(j);
        if ("Discard".equalsIgnoreCase(name)) {
          discard = true;
        } else if ("Secure".equalsIgnoreCase(name)) {
          secure = true;
        } else if ("HTTPOnly".equalsIgnoreCase(name)) {
          httpOnly = true;
        } else if ("Comment".equalsIgnoreCase(name)) {
          comment = value;
        } else if ("CommentURL".equalsIgnoreCase(name)) {
          commentURL = value;
        } else if ("Domain".equalsIgnoreCase(name)) {
          domain = value;
        } else if ("Path".equalsIgnoreCase(name)) {
          path = value;
        } else if ("Expires".equalsIgnoreCase(name)) {
          try {
            long maxAgeMillis = HttpHeaderDateFormat.get().parse(value).getTime() - System.currentTimeMillis();
            maxAge = maxAgeMillis / 1000L + ((maxAgeMillis % 1000L != 0L) ? 1L : 0L);
          } catch (ParseException e) {}
        } else if ("Max-Age".equalsIgnoreCase(name)) {
          maxAge = Integer.parseInt(value);
        } else if ("Version".equalsIgnoreCase(name)) {
          version = Integer.parseInt(value);
        } else if ("Port".equalsIgnoreCase(name)) {
          String[] portList = StringUtil.split(value, ',');
          for (String s1 : portList) {
            try {
              ports.add(Integer.valueOf(s1));
            } catch (NumberFormatException e) {}
          } 
        } else {
          break;
        } 
      } 
      c.setVersion(version);
      c.setMaxAge(maxAge);
      c.setPath(path);
      c.setDomain(domain);
      c.setSecure(secure);
      c.setHttpOnly(httpOnly);
      if (version > 0)
        c.setComment(comment); 
      if (version > 1) {
        c.setCommentUrl(commentURL);
        c.setPorts(ports);
        c.setDiscard(discard);
      } 
      cookies.add(c);
    } 
    return cookies;
  }
  
  private static void extractKeyValuePairs(String header, List<String> names, List<String> values) {
    int headerLen = header.length();
    int i = 0;
    label63: while (i != headerLen) {
      switch (header.charAt(i)) {
        case '\t':
        case '\n':
        case '\013':
        case '\f':
        case '\r':
        case ' ':
        case ',':
        case ';':
          i++;
          continue;
      } 
      label58: while (i != headerLen) {
        if (header.charAt(i) == '$') {
          i++;
          continue;
        } 
        if (i == headerLen) {
          String name = null;
          String value = null;
          continue label63;
        } 
        int newNameStart = i;
        break label58;
      } 
      break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\CookieDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */