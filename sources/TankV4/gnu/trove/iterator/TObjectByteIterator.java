package gnu.trove.iterator;

public interface TObjectByteIterator extends TAdvancingIterator {
   Object key();

   byte value();

   byte setValue(byte var1);
}
