package gnu.trove.iterator;

public interface TObjectLongIterator extends TAdvancingIterator {
   Object key();

   long value();

   long setValue(long var1);
}
