package gnu.trove.iterator;

public interface TShortLongIterator extends TAdvancingIterator {
   short key();

   long value();

   long setValue(long var1);
}
