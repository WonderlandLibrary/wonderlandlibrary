package org.apache.http;

import java.io.Serializable;
import org.apache.http.annotation.Immutable;

@Immutable
public final class HttpVersion extends ProtocolVersion implements Serializable {
   private static final long serialVersionUID = -5856653513894415344L;
   public static final String HTTP = "HTTP";
   public static final HttpVersion HTTP_0_9 = new HttpVersion(0, 9);
   public static final HttpVersion HTTP_1_0 = new HttpVersion(1, 0);
   public static final HttpVersion HTTP_1_1 = new HttpVersion(1, 1);

   public HttpVersion(int var1, int var2) {
      super("HTTP", var1, var2);
   }

   public ProtocolVersion forVersion(int var1, int var2) {
      if (var1 == this.major && var2 == this.minor) {
         return this;
      } else {
         if (var1 == 1) {
            if (var2 == 0) {
               return HTTP_1_0;
            }

            if (var2 == 1) {
               return HTTP_1_1;
            }
         }

         return var1 == 0 && var2 == 9 ? HTTP_0_9 : new HttpVersion(var1, var2);
      }
   }
}
