package org.apache.http.conn.ssl;

import java.net.Socket;
import java.util.Map;

public interface PrivateKeyStrategy {
  String chooseAlias(Map<String, PrivateKeyDetails> paramMap, Socket paramSocket);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\PrivateKeyStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */