package org.apache.http.auth;

import java.security.Principal;

public interface Credentials {
  Principal getUserPrincipal();
  
  String getPassword();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\auth\Credentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */