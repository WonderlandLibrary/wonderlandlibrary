package org.apache.http;

import java.io.IOException;

public interface HttpServerConnection extends HttpConnection {
   HttpRequest receiveRequestHeader() throws HttpException, IOException;

   void receiveRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException;

   void sendResponseHeader(HttpResponse var1) throws HttpException, IOException;

   void sendResponseEntity(HttpResponse var1) throws HttpException, IOException;

   void flush() throws IOException;
}
