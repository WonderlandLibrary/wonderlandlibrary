package gnu.trove;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.util.Collection;

public interface TLongCollection {
   long serialVersionUID = 1L;

   long getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean contains(long var1);

   TLongIterator iterator();

   long[] toArray();

   long[] toArray(long[] var1);

   boolean add(long var1);

   boolean remove(long var1);

   boolean containsAll(Collection var1);

   boolean containsAll(TLongCollection var1);

   boolean containsAll(long[] var1);

   boolean addAll(Collection var1);

   boolean addAll(TLongCollection var1);

   boolean addAll(long[] var1);

   boolean retainAll(Collection var1);

   boolean retainAll(TLongCollection var1);

   boolean retainAll(long[] var1);

   boolean removeAll(Collection var1);

   boolean removeAll(TLongCollection var1);

   boolean removeAll(long[] var1);

   void clear();

   boolean forEach(TLongProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
