package javafx.collections;

public interface ObservableIntegerArray extends ObservableArray {
   void copyTo(int var1, int[] var2, int var3, int var4);

   void copyTo(int var1, ObservableIntegerArray var2, int var3, int var4);

   int get(int var1);

   void addAll(int... var1);

   void addAll(ObservableIntegerArray var1);

   void addAll(int[] var1, int var2, int var3);

   void addAll(ObservableIntegerArray var1, int var2, int var3);

   void setAll(int... var1);

   void setAll(int[] var1, int var2, int var3);

   void setAll(ObservableIntegerArray var1);

   void setAll(ObservableIntegerArray var1, int var2, int var3);

   void set(int var1, int[] var2, int var3, int var4);

   void set(int var1, ObservableIntegerArray var2, int var3, int var4);

   void set(int var1, int var2);

   int[] toArray(int[] var1);

   int[] toArray(int var1, int[] var2, int var3);
}
