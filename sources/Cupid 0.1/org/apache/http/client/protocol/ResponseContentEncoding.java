package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;

@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor {
  public static final String UNCOMPRESSED = "http.client.response.uncompressed";
  
  public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
    HttpEntity entity = response.getEntity();
    if (entity != null && entity.getContentLength() != 0L) {
      Header ceheader = entity.getContentEncoding();
      if (ceheader != null) {
        HeaderElement[] codecs = ceheader.getElements();
        boolean uncompressed = false;
        HeaderElement[] arr$ = codecs;
        int len$ = arr$.length, i$ = 0;
        if (i$ < len$) {
          HeaderElement codec = arr$[i$];
          String codecname = codec.getName().toLowerCase(Locale.US);
          if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
            response.setEntity((HttpEntity)new GzipDecompressingEntity(response.getEntity()));
            uncompressed = true;
          } else if ("deflate".equals(codecname)) {
            response.setEntity((HttpEntity)new DeflateDecompressingEntity(response.getEntity()));
            uncompressed = true;
          } else {
            if ("identity".equals(codecname))
              return; 
            throw new HttpException("Unsupported Content-Coding: " + codec.getName());
          } 
        } 
        if (uncompressed) {
          response.removeHeaders("Content-Length");
          response.removeHeaders("Content-Encoding");
          response.removeHeaders("Content-MD5");
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\protocol\ResponseContentEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */