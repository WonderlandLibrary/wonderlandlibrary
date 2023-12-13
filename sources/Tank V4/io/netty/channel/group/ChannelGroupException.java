package io.netty.channel.group;

import io.netty.channel.ChannelException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ChannelGroupException extends ChannelException implements Iterable {
   private static final long serialVersionUID = -4093064295562629453L;
   private final Collection failed;

   public ChannelGroupException(Collection var1) {
      if (var1 == null) {
         throw new NullPointerException("causes");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("causes must be non empty");
      } else {
         this.failed = Collections.unmodifiableCollection(var1);
      }
   }

   public Iterator iterator() {
      return this.failed.iterator();
   }
}
