package gnu.trove;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.util.Collection;

public interface TByteCollection {
   long serialVersionUID = 1L;

   byte getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean contains(byte var1);

   TByteIterator iterator();

   byte[] toArray();

   byte[] toArray(byte[] var1);

   boolean add(byte var1);

   boolean remove(byte var1);

   boolean containsAll(Collection var1);

   boolean containsAll(TByteCollection var1);

   boolean containsAll(byte[] var1);

   boolean addAll(Collection var1);

   boolean addAll(TByteCollection var1);

   boolean addAll(byte[] var1);

   boolean retainAll(Collection var1);

   boolean retainAll(TByteCollection var1);

   boolean retainAll(byte[] var1);

   boolean removeAll(Collection var1);

   boolean removeAll(TByteCollection var1);

   boolean removeAll(byte[] var1);

   void clear();

   boolean forEach(TByteProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
