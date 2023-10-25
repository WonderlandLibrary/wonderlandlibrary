package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
public interface SpnegoTokenGenerator {
  byte[] generateSpnegoDERObject(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\auth\SpnegoTokenGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */