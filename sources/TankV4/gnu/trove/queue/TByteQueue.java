package gnu.trove.queue;

import gnu.trove.TByteCollection;

public interface TByteQueue extends TByteCollection {
   byte element();

   boolean offer(byte var1);

   byte peek();

   byte poll();
}
