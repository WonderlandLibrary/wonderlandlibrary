package gnu.trove.stack;

public interface TByteStack {
   byte getNoEntryValue();

   void push(byte var1);

   byte pop();

   byte peek();

   int size();

   void clear();

   byte[] toArray();

   void toArray(byte[] var1);
}
