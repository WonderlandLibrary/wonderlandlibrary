package gnu.trove.iterator;

public interface TObjectCharIterator extends TAdvancingIterator {
   Object key();

   char value();

   char setValue(char var1);
}
