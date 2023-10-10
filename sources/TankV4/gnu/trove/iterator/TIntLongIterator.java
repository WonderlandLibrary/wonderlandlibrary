package gnu.trove.iterator;

public interface TIntLongIterator extends TAdvancingIterator {
   int key();

   long value();

   long setValue(long var1);
}
