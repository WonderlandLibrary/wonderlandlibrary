package javafx.collections;

public interface ObservableFloatArray extends ObservableArray {
   void copyTo(int var1, float[] var2, int var3, int var4);

   void copyTo(int var1, ObservableFloatArray var2, int var3, int var4);

   float get(int var1);

   void addAll(float... var1);

   void addAll(ObservableFloatArray var1);

   void addAll(float[] var1, int var2, int var3);

   void addAll(ObservableFloatArray var1, int var2, int var3);

   void setAll(float... var1);

   void setAll(float[] var1, int var2, int var3);

   void setAll(ObservableFloatArray var1);

   void setAll(ObservableFloatArray var1, int var2, int var3);

   void set(int var1, float[] var2, int var3, int var4);

   void set(int var1, ObservableFloatArray var2, int var3, int var4);

   void set(int var1, float var2);

   float[] toArray(float[] var1);

   float[] toArray(int var1, float[] var2, int var3);
}
