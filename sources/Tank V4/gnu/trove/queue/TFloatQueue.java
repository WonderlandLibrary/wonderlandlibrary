package gnu.trove.queue;

import gnu.trove.TFloatCollection;

public interface TFloatQueue extends TFloatCollection {
   float element();

   boolean offer(float var1);

   float peek();

   float poll();
}
