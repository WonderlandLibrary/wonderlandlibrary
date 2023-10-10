package gnu.trove.queue;

import gnu.trove.TCharCollection;

public interface TCharQueue extends TCharCollection {
   char element();

   boolean offer(char var1);

   char peek();

   char poll();
}
