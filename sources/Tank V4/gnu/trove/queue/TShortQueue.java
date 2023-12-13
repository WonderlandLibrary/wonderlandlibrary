package gnu.trove.queue;

import gnu.trove.TShortCollection;

public interface TShortQueue extends TShortCollection {
   short element();

   boolean offer(short var1);

   short peek();

   short poll();
}
