package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;

public interface ResponseHandler {
   Object handleResponse(HttpResponse var1) throws ClientProtocolException, IOException;
}
