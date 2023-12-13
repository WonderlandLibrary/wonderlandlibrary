package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractStringLayout extends AbstractLayout {
   private final Charset charset;

   protected AbstractStringLayout(Charset var1) {
      this.charset = var1;
   }

   public byte[] toByteArray(LogEvent var1) {
      return ((String)this.toSerializable(var1)).getBytes(this.charset);
   }

   public String getContentType() {
      return "text/plain";
   }

   protected Charset getCharset() {
      return this.charset;
   }
}
