package gnu.trove.list;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Random;

public interface TFloatList extends TFloatCollection {
   float getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean add(float var1);

   void add(float[] var1);

   void add(float[] var1, int var2, int var3);

   void insert(int var1, float var2);

   void insert(int var1, float[] var2);

   void insert(int var1, float[] var2, int var3, int var4);

   float get(int var1);

   float set(int var1, float var2);

   void set(int var1, float[] var2);

   void set(int var1, float[] var2, int var3, int var4);

   float replace(int var1, float var2);

   void clear();

   boolean remove(float var1);

   float removeAt(int var1);

   void remove(int var1, int var2);

   void transformValues(TFloatFunction var1);

   void reverse();

   void reverse(int var1, int var2);

   void shuffle(Random var1);

   TFloatList subList(int var1, int var2);

   float[] toArray();

   float[] toArray(int var1, int var2);

   float[] toArray(float[] var1);

   float[] toArray(float[] var1, int var2, int var3);

   float[] toArray(float[] var1, int var2, int var3, int var4);

   boolean forEach(TFloatProcedure var1);

   boolean forEachDescending(TFloatProcedure var1);

   void sort();

   void sort(int var1, int var2);

   void fill(float var1);

   void fill(int var1, int var2, float var3);

   int binarySearch(float var1);

   int binarySearch(float var1, int var2, int var3);

   int indexOf(float var1);

   int indexOf(int var1, float var2);

   int lastIndexOf(float var1);

   int lastIndexOf(int var1, float var2);

   boolean contains(float var1);

   TFloatList grep(TFloatProcedure var1);

   TFloatList inverseGrep(TFloatProcedure var1);

   float max();

   float min();

   float sum();
}
