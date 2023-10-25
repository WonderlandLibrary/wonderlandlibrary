package org.apache.http.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class AuthOption {
  private final AuthScheme authScheme;
  
  private final Credentials creds;
  
  public AuthOption(AuthScheme authScheme, Credentials creds) {
    Args.notNull(authScheme, "Auth scheme");
    Args.notNull(creds, "User credentials");
    this.authScheme = authScheme;
    this.creds = creds;
  }
  
  public AuthScheme getAuthScheme() {
    return this.authScheme;
  }
  
  public Credentials getCredentials() {
    return this.creds;
  }
  
  public String toString() {
    return this.authScheme.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\auth\AuthOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */