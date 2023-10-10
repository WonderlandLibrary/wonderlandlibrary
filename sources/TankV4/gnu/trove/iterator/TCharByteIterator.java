package gnu.trove.iterator;

public interface TCharByteIterator extends TAdvancingIterator {
   char key();

   byte value();

   byte setValue(byte var1);
}
