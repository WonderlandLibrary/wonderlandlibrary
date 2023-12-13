package gnu.trove.queue;

import gnu.trove.TDoubleCollection;

public interface TDoubleQueue extends TDoubleCollection {
   double element();

   boolean offer(double var1);

   double peek();

   double poll();
}
