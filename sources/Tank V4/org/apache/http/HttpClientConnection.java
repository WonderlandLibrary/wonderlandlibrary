package org.apache.http;

import java.io.IOException;

public interface HttpClientConnection extends HttpConnection {
   boolean isResponseAvailable(int var1) throws IOException;

   void sendRequestHeader(HttpRequest var1) throws HttpException, IOException;

   void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException;

   HttpResponse receiveResponseHeader() throws HttpException, IOException;

   void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException;

   void flush() throws IOException;
}
