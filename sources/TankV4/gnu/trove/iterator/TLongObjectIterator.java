package gnu.trove.iterator;

public interface TLongObjectIterator extends TAdvancingIterator {
   long key();

   Object value();

   Object setValue(Object var1);
}
