package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicExpiresHandler extends AbstractCookieAttributeHandler {
  private final String[] datepatterns;
  
  public BasicExpiresHandler(String[] datepatterns) {
    Args.notNull(datepatterns, "Array of date patterns");
    this.datepatterns = datepatterns;
  }
  
  public void parse(SetCookie cookie, String value) throws MalformedCookieException {
    Args.notNull(cookie, "Cookie");
    if (value == null)
      throw new MalformedCookieException("Missing value for expires attribute"); 
    Date expiry = DateUtils.parseDate(value, this.datepatterns);
    if (expiry == null)
      throw new MalformedCookieException("Unable to parse expires attribute: " + value); 
    cookie.setExpiryDate(expiry);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\BasicExpiresHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */