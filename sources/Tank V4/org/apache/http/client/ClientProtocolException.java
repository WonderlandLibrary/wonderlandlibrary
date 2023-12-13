package org.apache.http.client;

import java.io.IOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class ClientProtocolException extends IOException {
   private static final long serialVersionUID = -5596590843227115865L;

   public ClientProtocolException() {
   }

   public ClientProtocolException(String var1) {
      super(var1);
   }

   public ClientProtocolException(Throwable var1) {
      this.initCause(var1);
   }

   public ClientProtocolException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
