package gnu.trove.iterator;

public interface TLongByteIterator extends TAdvancingIterator {
   long key();

   byte value();

   byte setValue(byte var1);
}
