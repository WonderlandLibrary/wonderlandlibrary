package gnu.trove;

import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.util.Collection;

public interface TCharCollection {
   long serialVersionUID = 1L;

   char getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean contains(char var1);

   TCharIterator iterator();

   char[] toArray();

   char[] toArray(char[] var1);

   boolean add(char var1);

   boolean remove(char var1);

   boolean containsAll(Collection var1);

   boolean containsAll(TCharCollection var1);

   boolean containsAll(char[] var1);

   boolean addAll(Collection var1);

   boolean addAll(TCharCollection var1);

   boolean addAll(char[] var1);

   boolean retainAll(Collection var1);

   boolean retainAll(TCharCollection var1);

   boolean retainAll(char[] var1);

   boolean removeAll(Collection var1);

   boolean removeAll(TCharCollection var1);

   boolean removeAll(char[] var1);

   void clear();

   boolean forEach(TCharProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
