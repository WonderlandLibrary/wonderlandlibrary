package gnu.trove.iterator;

public interface TLongShortIterator extends TAdvancingIterator {
   long key();

   short value();

   short setValue(short var1);
}
