package org.apache.http;

import java.io.IOException;

public class NoHttpResponseException extends IOException {
   private static final long serialVersionUID = -7658940387386078766L;

   public NoHttpResponseException(String var1) {
      super(var1);
   }
}
