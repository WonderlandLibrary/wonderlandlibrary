package gnu.trove.queue;

import gnu.trove.TIntCollection;

public interface TIntQueue extends TIntCollection {
   int element();

   boolean offer(int var1);

   int peek();

   int poll();
}
