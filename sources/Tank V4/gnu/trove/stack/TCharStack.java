package gnu.trove.stack;

public interface TCharStack {
   char getNoEntryValue();

   void push(char var1);

   char pop();

   char peek();

   int size();

   void clear();

   char[] toArray();

   void toArray(char[] var1);
}
