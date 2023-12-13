package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class EntityEnclosingRequestWrapper extends RequestWrapper implements HttpEntityEnclosingRequest {
   private HttpEntity entity;
   private boolean consumed;

   public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest var1) throws ProtocolException {
      super(var1);
      this.setEntity(var1.getEntity());
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public void setEntity(HttpEntity var1) {
      this.entity = var1 != null ? new EntityEnclosingRequestWrapper.EntityWrapper(this, var1) : null;
      this.consumed = false;
   }

   public boolean expectContinue() {
      Header var1 = this.getFirstHeader("Expect");
      return var1 != null && "100-continue".equalsIgnoreCase(var1.getValue());
   }

   public boolean isRepeatable() {
      return this.entity == null || this.entity.isRepeatable() || !this.consumed;
   }

   static boolean access$002(EntityEnclosingRequestWrapper var0, boolean var1) {
      return var0.consumed = var1;
   }

   class EntityWrapper extends HttpEntityWrapper {
      final EntityEnclosingRequestWrapper this$0;

      EntityWrapper(EntityEnclosingRequestWrapper var1, HttpEntity var2) {
         super(var2);
         this.this$0 = var1;
      }

      public void consumeContent() throws IOException {
         EntityEnclosingRequestWrapper.access$002(this.this$0, true);
         super.consumeContent();
      }

      public InputStream getContent() throws IOException {
         EntityEnclosingRequestWrapper.access$002(this.this$0, true);
         return super.getContent();
      }

      public void writeTo(OutputStream var1) throws IOException {
         EntityEnclosingRequestWrapper.access$002(this.this$0, true);
         super.writeTo(var1);
      }
   }
}
