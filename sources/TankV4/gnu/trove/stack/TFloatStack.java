package gnu.trove.stack;

public interface TFloatStack {
   float getNoEntryValue();

   void push(float var1);

   float pop();

   float peek();

   int size();

   void clear();

   float[] toArray();

   void toArray(float[] var1);
}
