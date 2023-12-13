package org.apache.http;

public interface HttpRequestFactory {
   HttpRequest newHttpRequest(RequestLine var1) throws MethodNotSupportedException;

   HttpRequest newHttpRequest(String var1, String var2) throws MethodNotSupportedException;
}
