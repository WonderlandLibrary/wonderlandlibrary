package org.apache.http.impl.entity;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public class EntityDeserializer {
   private final ContentLengthStrategy lenStrategy;

   public EntityDeserializer(ContentLengthStrategy var1) {
      this.lenStrategy = (ContentLengthStrategy)Args.notNull(var1, "Content length strategy");
   }

   protected BasicHttpEntity doDeserialize(SessionInputBuffer var1, HttpMessage var2) throws HttpException, IOException {
      BasicHttpEntity var3 = new BasicHttpEntity();
      long var4 = this.lenStrategy.determineLength(var2);
      if (var4 == -2L) {
         var3.setChunked(true);
         var3.setContentLength(-1L);
         var3.setContent(new ChunkedInputStream(var1));
      } else if (var4 == -1L) {
         var3.setChunked(false);
         var3.setContentLength(-1L);
         var3.setContent(new IdentityInputStream(var1));
      } else {
         var3.setChunked(false);
         var3.setContentLength(var4);
         var3.setContent(new ContentLengthInputStream(var1, var4));
      }

      Header var6 = var2.getFirstHeader("Content-Type");
      if (var6 != null) {
         var3.setContentType(var6);
      }

      Header var7 = var2.getFirstHeader("Content-Encoding");
      if (var7 != null) {
         var3.setContentEncoding(var7);
      }

      return var3;
   }

   public HttpEntity deserialize(SessionInputBuffer var1, HttpMessage var2) throws HttpException, IOException {
      Args.notNull(var1, "Session input buffer");
      Args.notNull(var2, "HTTP message");
      return this.doDeserialize(var1, var2);
   }
}
