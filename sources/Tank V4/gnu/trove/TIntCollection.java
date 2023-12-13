package gnu.trove;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.util.Collection;

public interface TIntCollection {
   long serialVersionUID = 1L;

   int getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean contains(int var1);

   TIntIterator iterator();

   int[] toArray();

   int[] toArray(int[] var1);

   boolean add(int var1);

   boolean remove(int var1);

   boolean containsAll(Collection var1);

   boolean containsAll(TIntCollection var1);

   boolean containsAll(int[] var1);

   boolean addAll(Collection var1);

   boolean addAll(TIntCollection var1);

   boolean addAll(int[] var1);

   boolean retainAll(Collection var1);

   boolean retainAll(TIntCollection var1);

   boolean retainAll(int[] var1);

   boolean removeAll(Collection var1);

   boolean removeAll(TIntCollection var1);

   boolean removeAll(int[] var1);

   void clear();

   boolean forEach(TIntProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
