package gnu.trove.list;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.procedure.TCharProcedure;
import java.util.Random;

public interface TCharList extends TCharCollection {
   char getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean add(char var1);

   void add(char[] var1);

   void add(char[] var1, int var2, int var3);

   void insert(int var1, char var2);

   void insert(int var1, char[] var2);

   void insert(int var1, char[] var2, int var3, int var4);

   char get(int var1);

   char set(int var1, char var2);

   void set(int var1, char[] var2);

   void set(int var1, char[] var2, int var3, int var4);

   char replace(int var1, char var2);

   void clear();

   boolean remove(char var1);

   char removeAt(int var1);

   void remove(int var1, int var2);

   void transformValues(TCharFunction var1);

   void reverse();

   void reverse(int var1, int var2);

   void shuffle(Random var1);

   TCharList subList(int var1, int var2);

   char[] toArray();

   char[] toArray(int var1, int var2);

   char[] toArray(char[] var1);

   char[] toArray(char[] var1, int var2, int var3);

   char[] toArray(char[] var1, int var2, int var3, int var4);

   boolean forEach(TCharProcedure var1);

   boolean forEachDescending(TCharProcedure var1);

   void sort();

   void sort(int var1, int var2);

   void fill(char var1);

   void fill(int var1, int var2, char var3);

   int binarySearch(char var1);

   int binarySearch(char var1, int var2, int var3);

   int indexOf(char var1);

   int indexOf(int var1, char var2);

   int lastIndexOf(char var1);

   int lastIndexOf(int var1, char var2);

   boolean contains(char var1);

   TCharList grep(TCharProcedure var1);

   TCharList inverseGrep(TCharProcedure var1);

   char max();

   char min();

   char sum();
}
