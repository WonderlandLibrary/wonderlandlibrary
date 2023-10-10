package gnu.trove.iterator;

public interface TObjectIntIterator extends TAdvancingIterator {
   Object key();

   int value();

   int setValue(int var1);
}
