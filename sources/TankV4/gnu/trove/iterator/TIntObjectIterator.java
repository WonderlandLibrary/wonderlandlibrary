package gnu.trove.iterator;

public interface TIntObjectIterator extends TAdvancingIterator {
   int key();

   Object value();

   Object setValue(Object var1);
}
