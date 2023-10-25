package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public interface HttpClient {
  @Deprecated
  HttpParams getParams();
  
  @Deprecated
  ClientConnectionManager getConnectionManager();
  
  HttpResponse execute(HttpUriRequest paramHttpUriRequest) throws IOException, ClientProtocolException;
  
  HttpResponse execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext) throws IOException, ClientProtocolException;
  
  HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest) throws IOException, ClientProtocolException;
  
  HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws IOException, ClientProtocolException;
  
  <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler) throws IOException, ClientProtocolException;
  
  <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext) throws IOException, ClientProtocolException;
  
  <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler) throws IOException, ClientProtocolException;
  
  <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext) throws IOException, ClientProtocolException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\HttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */