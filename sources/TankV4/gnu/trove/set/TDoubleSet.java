package gnu.trove.set;

import gnu.trove.TDoubleCollection;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Collection;

public interface TDoubleSet extends TDoubleCollection {
   double getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean contains(double var1);

   TDoubleIterator iterator();

   double[] toArray();

   double[] toArray(double[] var1);

   boolean add(double var1);

   boolean remove(double var1);

   boolean containsAll(Collection var1);

   boolean containsAll(TDoubleCollection var1);

   boolean containsAll(double[] var1);

   boolean addAll(Collection var1);

   boolean addAll(TDoubleCollection var1);

   boolean addAll(double[] var1);

   boolean retainAll(Collection var1);

   boolean retainAll(TDoubleCollection var1);

   boolean retainAll(double[] var1);

   boolean removeAll(Collection var1);

   boolean removeAll(TDoubleCollection var1);

   boolean removeAll(double[] var1);

   void clear();

   boolean forEach(TDoubleProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
