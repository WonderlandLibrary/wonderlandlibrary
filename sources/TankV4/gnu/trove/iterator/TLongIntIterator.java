package gnu.trove.iterator;

public interface TLongIntIterator extends TAdvancingIterator {
   long key();

   int value();

   int setValue(int var1);
}
