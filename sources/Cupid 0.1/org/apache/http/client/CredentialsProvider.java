package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

public interface CredentialsProvider {
  void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials);
  
  Credentials getCredentials(AuthScope paramAuthScope);
  
  void clear();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\CredentialsProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */