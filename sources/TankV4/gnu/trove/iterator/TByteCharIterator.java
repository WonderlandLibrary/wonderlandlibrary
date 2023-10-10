package gnu.trove.iterator;

public interface TByteCharIterator extends TAdvancingIterator {
   byte key();

   char value();

   char setValue(char var1);
}
