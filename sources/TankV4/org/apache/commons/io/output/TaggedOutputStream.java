package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedOutputStream extends ProxyOutputStream {
   private final Serializable tag = UUID.randomUUID();

   public TaggedOutputStream(OutputStream var1) {
      super(var1);
   }

   public boolean isCauseOf(Exception var1) {
      return TaggedIOException.isTaggedWith(var1, this.tag);
   }

   public void throwIfCauseOf(Exception var1) throws IOException {
      TaggedIOException.throwCauseIfTaggedWith(var1, this.tag);
   }

   protected void handleIOException(IOException var1) throws IOException {
      throw new TaggedIOException(var1, this.tag);
   }
}
