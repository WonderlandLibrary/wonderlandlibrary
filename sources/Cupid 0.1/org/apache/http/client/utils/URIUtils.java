package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class URIUtils {
  @Deprecated
  public static URI createURI(String scheme, String host, int port, String path, String query, String fragment) throws URISyntaxException {
    StringBuilder buffer = new StringBuilder();
    if (host != null) {
      if (scheme != null) {
        buffer.append(scheme);
        buffer.append("://");
      } 
      buffer.append(host);
      if (port > 0) {
        buffer.append(':');
        buffer.append(port);
      } 
    } 
    if (path == null || !path.startsWith("/"))
      buffer.append('/'); 
    if (path != null)
      buffer.append(path); 
    if (query != null) {
      buffer.append('?');
      buffer.append(query);
    } 
    if (fragment != null) {
      buffer.append('#');
      buffer.append(fragment);
    } 
    return new URI(buffer.toString());
  }
  
  public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
    Args.notNull(uri, "URI");
    if (uri.isOpaque())
      return uri; 
    URIBuilder uribuilder = new URIBuilder(uri);
    if (target != null) {
      uribuilder.setScheme(target.getSchemeName());
      uribuilder.setHost(target.getHostName());
      uribuilder.setPort(target.getPort());
    } else {
      uribuilder.setScheme(null);
      uribuilder.setHost(null);
      uribuilder.setPort(-1);
    } 
    if (dropFragment)
      uribuilder.setFragment(null); 
    if (TextUtils.isEmpty(uribuilder.getPath()))
      uribuilder.setPath("/"); 
    return uribuilder.build();
  }
  
  public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
    return rewriteURI(uri, target, false);
  }
  
  public static URI rewriteURI(URI uri) throws URISyntaxException {
    Args.notNull(uri, "URI");
    if (uri.isOpaque())
      return uri; 
    URIBuilder uribuilder = new URIBuilder(uri);
    if (uribuilder.getUserInfo() != null)
      uribuilder.setUserInfo(null); 
    if (TextUtils.isEmpty(uribuilder.getPath()))
      uribuilder.setPath("/"); 
    if (uribuilder.getHost() != null)
      uribuilder.setHost(uribuilder.getHost().toLowerCase(Locale.ENGLISH)); 
    uribuilder.setFragment(null);
    return uribuilder.build();
  }
  
  public static URI resolve(URI baseURI, String reference) {
    return resolve(baseURI, URI.create(reference));
  }
  
  public static URI resolve(URI baseURI, URI reference) {
    Args.notNull(baseURI, "Base URI");
    Args.notNull(reference, "Reference URI");
    URI ref = reference;
    String s = ref.toString();
    if (s.startsWith("?"))
      return resolveReferenceStartingWithQueryString(baseURI, ref); 
    boolean emptyReference = (s.length() == 0);
    if (emptyReference)
      ref = URI.create("#"); 
    URI resolved = baseURI.resolve(ref);
    if (emptyReference) {
      String resolvedString = resolved.toString();
      resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf('#')));
    } 
    return normalizeSyntax(resolved);
  }
  
  private static URI resolveReferenceStartingWithQueryString(URI baseURI, URI reference) {
    String baseUri = baseURI.toString();
    baseUri = (baseUri.indexOf('?') > -1) ? baseUri.substring(0, baseUri.indexOf('?')) : baseUri;
    return URI.create(baseUri + reference.toString());
  }
  
  private static URI normalizeSyntax(URI uri) {
    if (uri.isOpaque() || uri.getAuthority() == null)
      return uri; 
    Args.check(uri.isAbsolute(), "Base URI must be absolute");
    String path = (uri.getPath() == null) ? "" : uri.getPath();
    String[] inputSegments = path.split("/");
    Stack<String> outputSegments = new Stack<String>();
    for (String inputSegment : inputSegments) {
      if (inputSegment.length() != 0 && !".".equals(inputSegment))
        if ("..".equals(inputSegment)) {
          if (!outputSegments.isEmpty())
            outputSegments.pop(); 
        } else {
          outputSegments.push(inputSegment);
        }  
    } 
    StringBuilder outputBuffer = new StringBuilder();
    for (String outputSegment : outputSegments)
      outputBuffer.append('/').append(outputSegment); 
    if (path.lastIndexOf('/') == path.length() - 1)
      outputBuffer.append('/'); 
    try {
      String scheme = uri.getScheme().toLowerCase();
      String auth = uri.getAuthority().toLowerCase();
      URI ref = new URI(scheme, auth, outputBuffer.toString(), null, null);
      if (uri.getQuery() == null && uri.getFragment() == null)
        return ref; 
      StringBuilder normalized = new StringBuilder(ref.toASCIIString());
      if (uri.getQuery() != null)
        normalized.append('?').append(uri.getRawQuery()); 
      if (uri.getFragment() != null)
        normalized.append('#').append(uri.getRawFragment()); 
      return URI.create(normalized.toString());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    } 
  }
  
  public static HttpHost extractHost(URI uri) {
    if (uri == null)
      return null; 
    HttpHost target = null;
    if (uri.isAbsolute()) {
      int port = uri.getPort();
      String host = uri.getHost();
      if (host == null) {
        host = uri.getAuthority();
        if (host != null) {
          int at = host.indexOf('@');
          if (at >= 0)
            if (host.length() > at + 1) {
              host = host.substring(at + 1);
            } else {
              host = null;
            }  
          if (host != null) {
            int colon = host.indexOf(':');
            if (colon >= 0) {
              int pos = colon + 1;
              int len = 0;
              for (int i = pos; i < host.length() && 
                Character.isDigit(host.charAt(i)); i++)
                len++; 
              if (len > 0)
                try {
                  port = Integer.parseInt(host.substring(pos, pos + len));
                } catch (NumberFormatException ex) {} 
              host = host.substring(0, colon);
            } 
          } 
        } 
      } 
      String scheme = uri.getScheme();
      if (host != null)
        target = new HttpHost(host, port, scheme); 
    } 
    return target;
  }
  
  public static URI resolve(URI originalURI, HttpHost target, List<URI> redirects) throws URISyntaxException {
    URIBuilder uribuilder;
    Args.notNull(originalURI, "Request URI");
    if (redirects == null || redirects.isEmpty()) {
      uribuilder = new URIBuilder(originalURI);
    } else {
      uribuilder = new URIBuilder(redirects.get(redirects.size() - 1));
      String frag = uribuilder.getFragment();
      for (int i = redirects.size() - 1; frag == null && i >= 0; i--)
        frag = ((URI)redirects.get(i)).getFragment(); 
      uribuilder.setFragment(frag);
    } 
    if (uribuilder.getFragment() == null)
      uribuilder.setFragment(originalURI.getFragment()); 
    if (target != null && !uribuilder.isAbsolute()) {
      uribuilder.setScheme(target.getSchemeName());
      uribuilder.setHost(target.getHostName());
      uribuilder.setPort(target.getPort());
    } 
    return uribuilder.build();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\clien\\utils\URIUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */