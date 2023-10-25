package com.mojang.authlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HttpAuthenticationService extends BaseAuthenticationService {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final Proxy proxy;
  
  protected HttpAuthenticationService(Proxy proxy) {
    Validate.notNull(proxy);
    this.proxy = proxy;
  }
  
  public Proxy getProxy() {
    return this.proxy;
  }
  
  protected HttpURLConnection createUrlConnection(URL url) throws IOException {
    Validate.notNull(url);
    LOGGER.debug("Opening connection to " + url);
    HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
    connection.setConnectTimeout(15000);
    connection.setReadTimeout(15000);
    connection.setUseCaches(false);
    return connection;
  }
  
  public String performPostRequest(URL url, String post, String contentType) throws IOException {
    Validate.notNull(url);
    Validate.notNull(post);
    Validate.notNull(contentType);
    HttpURLConnection connection = createUrlConnection(url);
    byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
    connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
    connection.setRequestProperty("Content-Length", "" + postAsBytes.length);
    connection.setDoOutput(true);
    LOGGER.debug("Writing POST data to " + url + ": " + post);
    OutputStream outputStream = null;
    try {
      outputStream = connection.getOutputStream();
      IOUtils.write(postAsBytes, outputStream);
    } finally {
      IOUtils.closeQuietly(outputStream);
    } 
    LOGGER.debug("Reading data from " + url);
    InputStream inputStream = null;
    try {
      inputStream = connection.getInputStream();
      String result = IOUtils.toString(inputStream, Charsets.UTF_8);
      LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
      LOGGER.debug("Response: " + result);
      return result;
    } catch (IOException e) {
      IOUtils.closeQuietly(inputStream);
      inputStream = connection.getErrorStream();
      if (inputStream != null) {
        LOGGER.debug("Reading error page from " + url);
        String result = IOUtils.toString(inputStream, Charsets.UTF_8);
        LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
        LOGGER.debug("Response: " + result);
        return result;
      } 
      LOGGER.debug("Request failed", e);
      throw e;
    } finally {
      IOUtils.closeQuietly(inputStream);
    } 
  }
  
  public String performGetRequest(URL url) throws IOException {
    Validate.notNull(url);
    HttpURLConnection connection = createUrlConnection(url);
    LOGGER.debug("Reading data from " + url);
    InputStream inputStream = null;
    try {
      inputStream = connection.getInputStream();
      String result = IOUtils.toString(inputStream, Charsets.UTF_8);
      LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
      LOGGER.debug("Response: " + result);
      return result;
    } catch (IOException e) {
      IOUtils.closeQuietly(inputStream);
      inputStream = connection.getErrorStream();
      if (inputStream != null) {
        LOGGER.debug("Reading error page from " + url);
        String result = IOUtils.toString(inputStream, Charsets.UTF_8);
        LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
        LOGGER.debug("Response: " + result);
        return result;
      } 
      LOGGER.debug("Request failed", e);
      throw e;
    } finally {
      IOUtils.closeQuietly(inputStream);
    } 
  }
  
  public static URL constantURL(String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException ex) {
      throw new Error("Couldn't create constant for " + url, ex);
    } 
  }
  
  public static String buildQuery(Map<String, Object> query) {
    if (query == null)
      return ""; 
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, Object> entry : query.entrySet()) {
      if (builder.length() > 0)
        builder.append('&'); 
      try {
        builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("Unexpected exception building query", e);
      } 
      if (entry.getValue() != null) {
        builder.append('=');
        try {
          builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          LOGGER.error("Unexpected exception building query", e);
        } 
      } 
    } 
    return builder.toString();
  }
  
  public static URL concatenateURL(URL url, String query) {
    try {
      if (url.getQuery() != null && url.getQuery().length() > 0)
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query); 
      return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
    } catch (MalformedURLException ex) {
      throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", ex);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\HttpAuthenticationService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */