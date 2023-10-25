package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicMaxAgeHandler extends AbstractCookieAttributeHandler {
  public void parse(SetCookie cookie, String value) throws MalformedCookieException {
    int age;
    Args.notNull(cookie, "Cookie");
    if (value == null)
      throw new MalformedCookieException("Missing value for max-age attribute"); 
    try {
      age = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new MalformedCookieException("Invalid max-age attribute: " + value);
    } 
    if (age < 0)
      throw new MalformedCookieException("Negative max-age attribute: " + value); 
    cookie.setExpiryDate(new Date(System.currentTimeMillis() + age * 1000L));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\BasicMaxAgeHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */