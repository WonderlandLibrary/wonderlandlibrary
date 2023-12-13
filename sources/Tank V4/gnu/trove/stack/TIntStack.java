package gnu.trove.stack;

public interface TIntStack {
   int getNoEntryValue();

   void push(int var1);

   int pop();

   int peek();

   int size();

   void clear();

   int[] toArray();

   void toArray(int[] var1);
}
