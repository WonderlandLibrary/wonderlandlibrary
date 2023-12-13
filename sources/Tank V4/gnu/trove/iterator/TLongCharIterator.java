package gnu.trove.iterator;

public interface TLongCharIterator extends TAdvancingIterator {
   long key();

   char value();

   char setValue(char var1);
}
