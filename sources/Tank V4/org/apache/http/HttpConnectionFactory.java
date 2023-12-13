package org.apache.http;

import java.io.IOException;
import java.net.Socket;

public interface HttpConnectionFactory {
   HttpConnection createConnection(Socket var1) throws IOException;
}
