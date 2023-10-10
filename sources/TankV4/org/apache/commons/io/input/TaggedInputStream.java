package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedInputStream extends ProxyInputStream {
   private final Serializable tag = UUID.randomUUID();

   public TaggedInputStream(InputStream var1) {
      super(var1);
   }

   public boolean isCauseOf(Throwable var1) {
      return TaggedIOException.isTaggedWith(var1, this.tag);
   }

   public void throwIfCauseOf(Throwable var1) throws IOException {
      TaggedIOException.throwCauseIfTaggedWith(var1, this.tag);
   }

   protected void handleIOException(IOException var1) throws IOException {
      throw new TaggedIOException(var1, this.tag);
   }
}
