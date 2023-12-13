package io.netty.util.internal.chmv8;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

public class ConcurrentHashMapV8 implements ConcurrentMap, Serializable {
   private static final long serialVersionUID = 7249069246763182397L;
   private static final int MAXIMUM_CAPACITY = 1073741824;
   private static final int DEFAULT_CAPACITY = 16;
   static final int MAX_ARRAY_SIZE = 2147483639;
   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
   private static final float LOAD_FACTOR = 0.75F;
   static final int TREEIFY_THRESHOLD = 8;
   static final int UNTREEIFY_THRESHOLD = 6;
   static final int MIN_TREEIFY_CAPACITY = 64;
   private static final int MIN_TRANSFER_STRIDE = 16;
   static final int MOVED = -1;
   static final int TREEBIN = -2;
   static final int RESERVED = -3;
   static final int HASH_BITS = Integer.MAX_VALUE;
   static final int NCPU = Runtime.getRuntime().availableProcessors();
   private static final ObjectStreamField[] serialPersistentFields;
   transient volatile ConcurrentHashMapV8.Node[] table;
   private transient volatile ConcurrentHashMapV8.Node[] nextTable;
   private transient volatile long baseCount;
   private transient volatile int sizeCtl;
   private transient volatile int transferIndex;
   private transient volatile int transferOrigin;
   private transient volatile int cellsBusy;
   private transient volatile ConcurrentHashMapV8.CounterCell[] counterCells;
   private transient ConcurrentHashMapV8.KeySetView keySet;
   private transient ConcurrentHashMapV8.ValuesView values;
   private transient ConcurrentHashMapV8.EntrySetView entrySet;
   static final AtomicInteger counterHashCodeGenerator;
   static final int SEED_INCREMENT = 1640531527;
   static final ThreadLocal threadCounterHashCode;
   private static final Unsafe U;
   private static final long SIZECTL;
   private static final long TRANSFERINDEX;
   private static final long TRANSFERORIGIN;
   private static final long BASECOUNT;
   private static final long CELLSBUSY;
   private static final long CELLVALUE;
   private static final long ABASE;
   private static final int ASHIFT;

   static final int spread(int var0) {
      return (var0 ^ var0 >>> 16) & Integer.MAX_VALUE;
   }

   private static final int tableSizeFor(int var0) {
      int var1 = var0 - 1;
      var1 |= var1 >>> 1;
      var1 |= var1 >>> 2;
      var1 |= var1 >>> 4;
      var1 |= var1 >>> 8;
      var1 |= var1 >>> 16;
      return var1 < 0 ? 1 : (var1 >= 1073741824 ? 1073741824 : var1 + 1);
   }

   static Class comparableClassFor(Object var0) {
      if (var0 instanceof Comparable) {
         Class var1;
         if ((var1 = var0.getClass()) == String.class) {
            return var1;
         }

         Type[] var2;
         if ((var2 = var1.getGenericInterfaces()) != null) {
            for(int var6 = 0; var6 < var2.length; ++var6) {
               Type[] var3;
               Type var4;
               ParameterizedType var5;
               if ((var4 = var2[var6]) instanceof ParameterizedType && (var5 = (ParameterizedType)var4).getRawType() == Comparable.class && (var3 = var5.getActualTypeArguments()) != null && var3.length == 1 && var3[0] == var1) {
                  return var1;
               }
            }
         }
      }

      return null;
   }

   static int compareComparables(Class var0, Object var1, Object var2) {
      return var2 != null && var2.getClass() == var0 ? ((Comparable)var1).compareTo(var2) : 0;
   }

   static final ConcurrentHashMapV8.Node tabAt(ConcurrentHashMapV8.Node[] var0, int var1) {
      return (ConcurrentHashMapV8.Node)U.getObjectVolatile(var0, ((long)var1 << ASHIFT) + ABASE);
   }

   static final boolean casTabAt(ConcurrentHashMapV8.Node[] var0, int var1, ConcurrentHashMapV8.Node var2, ConcurrentHashMapV8.Node var3) {
      return U.compareAndSwapObject(var0, ((long)var1 << ASHIFT) + ABASE, var2, var3);
   }

   static final void setTabAt(ConcurrentHashMapV8.Node[] var0, int var1, ConcurrentHashMapV8.Node var2) {
      U.putObjectVolatile(var0, ((long)var1 << ASHIFT) + ABASE, var2);
   }

   public ConcurrentHashMapV8() {
   }

   public ConcurrentHashMapV8(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         int var2 = var1 >= 536870912 ? 1073741824 : tableSizeFor(var1 + (var1 >>> 1) + 1);
         this.sizeCtl = var2;
      }
   }

   public ConcurrentHashMapV8(Map var1) {
      this.sizeCtl = 16;
      this.putAll(var1);
   }

   public ConcurrentHashMapV8(int var1, float var2) {
      this(var1, var2, 1);
   }

   public ConcurrentHashMapV8(int var1, float var2, int var3) {
      if (var2 > 0.0F && var1 >= 0 && var3 > 0) {
         if (var1 < var3) {
            var1 = var3;
         }

         long var4 = (long)(1.0D + (double)((float)((long)var1) / var2));
         int var6 = var4 >= 1073741824L ? 1073741824 : tableSizeFor((int)var4);
         this.sizeCtl = var6;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int size() {
      long var1 = this.sumCount();
      return var1 < 0L ? 0 : (var1 > 2147483647L ? Integer.MAX_VALUE : (int)var1);
   }

   public boolean isEmpty() {
      return this.sumCount() <= 0L;
   }

   public Object get(Object var1) {
      int var8 = spread(var1.hashCode());
      ConcurrentHashMapV8.Node[] var2;
      ConcurrentHashMapV8.Node var3;
      int var5;
      if ((var2 = this.table) != null && (var5 = var2.length) > 0 && (var3 = tabAt(var2, var5 - 1 & var8)) != null) {
         int var6;
         Object var7;
         if ((var6 = var3.hash) == var8) {
            if ((var7 = var3.key) == var1 || var7 != null && var1.equals(var7)) {
               return var3.val;
            }
         } else if (var6 < 0) {
            ConcurrentHashMapV8.Node var4;
            return (var4 = var3.find(var8, var1)) != null ? var4.val : null;
         }

         while((var3 = var3.next) != null) {
            if (var3.hash == var8 && ((var7 = var3.key) == var1 || var7 != null && var1.equals(var7))) {
               return var3.val;
            }
         }
      }

      return null;
   }

   public boolean containsKey(Object var1) {
      return this.get(var1) != null;
   }

   public boolean containsValue(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         ConcurrentHashMapV8.Node[] var2;
         if ((var2 = this.table) != null) {
            ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

            ConcurrentHashMapV8.Node var4;
            while((var4 = var3.advance()) != null) {
               Object var5;
               if ((var5 = var4.val) == var1 || var5 != null && var1.equals(var5)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public Object put(Object var1, Object var2) {
      return this.putVal(var1, var2, false);
   }

   final Object putVal(Object var1, Object var2, boolean var3) {
      if (var1 != null && var2 != null) {
         int var4 = spread(var1.hashCode());
         int var5 = 0;
         ConcurrentHashMapV8.Node[] var6 = this.table;

         while(true) {
            int var8;
            while(var6 == null || (var8 = var6.length) == 0) {
               var6 = this.initTable();
            }

            ConcurrentHashMapV8.Node var7;
            int var9;
            if ((var7 = tabAt(var6, var9 = var8 - 1 & var4)) == null) {
               if (casTabAt(var6, var9, (ConcurrentHashMapV8.Node)null, new ConcurrentHashMapV8.Node(var4, var1, var2, (ConcurrentHashMapV8.Node)null))) {
                  break;
               }
            } else {
               int var10;
               if ((var10 = var7.hash) == -1) {
                  var6 = this.helpTransfer(var6, var7);
               } else {
                  Object var11 = null;
                  synchronized(var7){}
                  if (tabAt(var6, var9) == var7) {
                     if (var10 >= 0) {
                        label87: {
                           var5 = 1;

                           ConcurrentHashMapV8.Node var13;
                           Object var14;
                           for(var13 = var7; var13.hash != var4 || (var14 = var13.key) != var1 && (var14 == null || !var1.equals(var14)); ++var5) {
                              ConcurrentHashMapV8.Node var15 = var13;
                              if ((var13 = var13.next) == null) {
                                 var15.next = new ConcurrentHashMapV8.Node(var4, var1, var2, (ConcurrentHashMapV8.Node)null);
                                 break label87;
                              }
                           }

                           var11 = var13.val;
                           if (!var3) {
                              var13.val = var2;
                           }
                        }
                     } else if (var7 instanceof ConcurrentHashMapV8.TreeBin) {
                        var5 = 2;
                        ConcurrentHashMapV8.TreeNode var17;
                        if ((var17 = ((ConcurrentHashMapV8.TreeBin)var7).putTreeVal(var4, var1, var2)) != null) {
                           var11 = var17.val;
                           if (!var3) {
                              var17.val = var2;
                           }
                        }
                     }
                  }

                  if (var5 != 0) {
                     if (var5 >= 8) {
                        this.treeifyBin(var6, var9);
                     }

                     if (var11 != null) {
                        return var11;
                     }
                     break;
                  }
               }
            }
         }

         this.addCount(1L, var5);
         return null;
      } else {
         throw new NullPointerException();
      }
   }

   public void putAll(Map var1) {
      this.tryPresize(var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.putVal(var3.getKey(), var3.getValue(), false);
      }

   }

   public Object remove(Object var1) {
      return this.replaceNode(var1, (Object)null, (Object)null);
   }

   final Object replaceNode(Object var1, Object var2, Object var3) {
      int var4 = spread(var1.hashCode());
      ConcurrentHashMapV8.Node[] var5 = this.table;

      ConcurrentHashMapV8.Node var6;
      int var7;
      int var8;
      while(var5 != null && (var7 = var5.length) != 0 && (var6 = tabAt(var5, var8 = var7 - 1 & var4)) != null) {
         int var9;
         if ((var9 = var6.hash) == -1) {
            var5 = this.helpTransfer(var5, var6);
         } else {
            Object var10 = null;
            boolean var11 = false;
            synchronized(var6){}
            if (tabAt(var5, var8) == var6) {
               Object var16;
               if (var9 >= 0) {
                  label105: {
                     var11 = true;
                     ConcurrentHashMapV8.Node var13 = var6;
                     ConcurrentHashMapV8.Node var14 = null;

                     Object var15;
                     while(var13.hash != var4 || (var15 = var13.key) != var1 && (var15 == null || !var1.equals(var15))) {
                        var14 = var13;
                        if ((var13 = var13.next) == null) {
                           break label105;
                        }
                     }

                     var16 = var13.val;
                     if (var3 == null || var3 == var16 || var16 != null && var3.equals(var16)) {
                        var10 = var16;
                        if (var2 != null) {
                           var13.val = var2;
                        } else if (var14 != null) {
                           var14.next = var13.next;
                        } else {
                           setTabAt(var5, var8, var13.next);
                        }
                     }
                  }
               } else if (var6 instanceof ConcurrentHashMapV8.TreeBin) {
                  var11 = true;
                  ConcurrentHashMapV8.TreeBin var18 = (ConcurrentHashMapV8.TreeBin)var6;
                  ConcurrentHashMapV8.TreeNode var19;
                  ConcurrentHashMapV8.TreeNode var20;
                  if ((var19 = var18.root) != null && (var20 = var19.findTreeNode(var4, var1, (Class)null)) != null) {
                     var16 = var20.val;
                     if (var3 == null || var3 == var16 || var16 != null && var3.equals(var16)) {
                        var10 = var16;
                        if (var2 != null) {
                           var20.val = var2;
                        } else if (var18.removeTreeNode(var20)) {
                           setTabAt(var5, var8, untreeify(var18.first));
                        }
                     }
                  }
               }
            }

            if (var11) {
               if (var10 != null) {
                  if (var2 == null) {
                     this.addCount(-1L, -1);
                  }

                  return var10;
               }
               break;
            }
         }
      }

      return null;
   }

   public void clear() {
      long var1 = 0L;
      int var3 = 0;
      ConcurrentHashMapV8.Node[] var4 = this.table;

      while(var4 != null && var3 < var4.length) {
         ConcurrentHashMapV8.Node var6 = tabAt(var4, var3);
         if (var6 == null) {
            ++var3;
         } else {
            int var5;
            if ((var5 = var6.hash) == -1) {
               var4 = this.helpTransfer(var4, var6);
               var3 = 0;
            } else {
               synchronized(var6){}
               if (tabAt(var4, var3) == var6) {
                  for(Object var8 = var5 >= 0 ? var6 : (var6 instanceof ConcurrentHashMapV8.TreeBin ? ((ConcurrentHashMapV8.TreeBin)var6).first : null); var8 != null; var8 = ((ConcurrentHashMapV8.Node)var8).next) {
                     --var1;
                  }

                  setTabAt(var4, var3++, (ConcurrentHashMapV8.Node)null);
               }
            }
         }
      }

      if (var1 != 0L) {
         this.addCount(var1, -1);
      }

   }

   public ConcurrentHashMapV8.KeySetView keySet() {
      ConcurrentHashMapV8.KeySetView var1;
      return (var1 = this.keySet) != null ? var1 : (this.keySet = new ConcurrentHashMapV8.KeySetView(this, (Object)null));
   }

   public Collection values() {
      ConcurrentHashMapV8.ValuesView var1;
      return (var1 = this.values) != null ? var1 : (this.values = new ConcurrentHashMapV8.ValuesView(this));
   }

   public Set entrySet() {
      ConcurrentHashMapV8.EntrySetView var1;
      return (var1 = this.entrySet) != null ? var1 : (this.entrySet = new ConcurrentHashMapV8.EntrySetView(this));
   }

   public int hashCode() {
      int var1 = 0;
      ConcurrentHashMapV8.Node[] var2;
      ConcurrentHashMapV8.Node var4;
      if ((var2 = this.table) != null) {
         for(ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length); (var4 = var3.advance()) != null; var1 += var4.key.hashCode() ^ var4.val.hashCode()) {
         }
      }

      return var1;
   }

   public String toString() {
      ConcurrentHashMapV8.Node[] var1;
      int var2 = (var1 = this.table) == null ? 0 : var1.length;
      ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var1, var2, 0, var2);
      StringBuilder var4 = new StringBuilder();
      var4.append('{');
      ConcurrentHashMapV8.Node var5;
      if ((var5 = var3.advance()) != null) {
         while(true) {
            Object var6 = var5.key;
            Object var7 = var5.val;
            var4.append(var6 == this ? "(this Map)" : var6);
            var4.append('=');
            var4.append(var7 == this ? "(this Map)" : var7);
            if ((var5 = var3.advance()) == null) {
               break;
            }

            var4.append(',').append(' ');
         }
      }

      return var4.append('}').toString();
   }

   public boolean equals(Object var1) {
      if (var1 != this) {
         if (!(var1 instanceof Map)) {
            return false;
         } else {
            Map var2 = (Map)var1;
            ConcurrentHashMapV8.Node[] var3;
            int var4 = (var3 = this.table) == null ? 0 : var3.length;
            ConcurrentHashMapV8.Traverser var5 = new ConcurrentHashMapV8.Traverser(var3, var4, 0, var4);

            Object var7;
            Object var8;
            do {
               ConcurrentHashMapV8.Node var6;
               if ((var6 = var5.advance()) == null) {
                  Iterator var11 = var2.entrySet().iterator();

                  Object var9;
                  Object var10;
                  Entry var12;
                  do {
                     if (!var11.hasNext()) {
                        return true;
                     }

                     var12 = (Entry)var11.next();
                  } while((var8 = var12.getKey()) != null && (var9 = var12.getValue()) != null && (var10 = this.get(var8)) != null && (var9 == var10 || var9.equals(var10)));

                  return false;
               }

               var7 = var6.val;
               var8 = var2.get(var6.key);
            } while(var8 != null && (var8 == var7 || var8.equals(var7)));

            return false;
         }
      } else {
         return true;
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      int var2 = 0;

      int var3;
      for(var3 = 1; var3 < 16; var3 <<= 1) {
         ++var2;
      }

      int var4 = 32 - var2;
      int var5 = var3 - 1;
      ConcurrentHashMapV8.Segment[] var6 = (ConcurrentHashMapV8.Segment[])(new ConcurrentHashMapV8.Segment[16]);

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var6[var7] = new ConcurrentHashMapV8.Segment(0.75F);
      }

      var1.putFields().put("segments", var6);
      var1.putFields().put("segmentShift", var4);
      var1.putFields().put("segmentMask", var5);
      var1.writeFields();
      ConcurrentHashMapV8.Node[] var10;
      if ((var10 = this.table) != null) {
         ConcurrentHashMapV8.Traverser var8 = new ConcurrentHashMapV8.Traverser(var10, var10.length, 0, var10.length);

         ConcurrentHashMapV8.Node var9;
         while((var9 = var8.advance()) != null) {
            var1.writeObject(var9.key);
            var1.writeObject(var9.val);
         }
      }

      var1.writeObject((Object)null);
      var1.writeObject((Object)null);
      var6 = null;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.sizeCtl = -1;
      var1.defaultReadObject();
      long var2 = 0L;
      ConcurrentHashMapV8.Node var4 = null;

      while(true) {
         Object var5 = var1.readObject();
         Object var6 = var1.readObject();
         if (var5 == null || var6 == null) {
            if (var2 == 0L) {
               this.sizeCtl = 0;
            } else {
               int var22;
               if (var2 >= 536870912L) {
                  var22 = 1073741824;
               } else {
                  int var23 = (int)var2;
                  var22 = tableSizeFor(var23 + (var23 >>> 1) + 1);
               }

               ConcurrentHashMapV8.Node[] var24 = (ConcurrentHashMapV8.Node[])(new ConcurrentHashMapV8.Node[var22]);
               int var7 = var22 - 1;

               long var8;
               ConcurrentHashMapV8.Node var11;
               for(var8 = 0L; var4 != null; var4 = var11) {
                  var11 = var4.next;
                  int var13 = var4.hash;
                  int var14 = var13 & var7;
                  boolean var10;
                  ConcurrentHashMapV8.Node var12;
                  if ((var12 = tabAt(var24, var14)) == null) {
                     var10 = true;
                  } else {
                     Object var15 = var4.key;
                     if (var12.hash < 0) {
                        ConcurrentHashMapV8.TreeBin var25 = (ConcurrentHashMapV8.TreeBin)var12;
                        if (var25.putTreeVal(var13, var15, var4.val) == null) {
                           ++var8;
                        }

                        var10 = false;
                     } else {
                        int var16 = 0;
                        var10 = true;

                        ConcurrentHashMapV8.Node var17;
                        for(var17 = var12; var17 != null; var17 = var17.next) {
                           Object var18;
                           if (var17.hash == var13 && ((var18 = var17.key) == var15 || var18 != null && var15.equals(var18))) {
                              var10 = false;
                              break;
                           }

                           ++var16;
                        }

                        if (var10 && var16 >= 8) {
                           var10 = false;
                           ++var8;
                           var4.next = var12;
                           ConcurrentHashMapV8.TreeNode var19 = null;
                           ConcurrentHashMapV8.TreeNode var20 = null;

                           for(var17 = var4; var17 != null; var17 = var17.next) {
                              ConcurrentHashMapV8.TreeNode var21 = new ConcurrentHashMapV8.TreeNode(var17.hash, var17.key, var17.val, (ConcurrentHashMapV8.Node)null, (ConcurrentHashMapV8.TreeNode)null);
                              if ((var21.prev = var20) == null) {
                                 var19 = var21;
                              } else {
                                 var20.next = var21;
                              }

                              var20 = var21;
                           }

                           setTabAt(var24, var14, new ConcurrentHashMapV8.TreeBin(var19));
                        }
                     }
                  }

                  if (var10) {
                     ++var8;
                     var4.next = var12;
                     setTabAt(var24, var14, var4);
                  }
               }

               this.table = var24;
               this.sizeCtl = var22 - (var22 >>> 2);
               this.baseCount = var8;
            }

            return;
         }

         var4 = new ConcurrentHashMapV8.Node(spread(var5.hashCode()), var5, var6, var4);
         ++var2;
      }
   }

   public Object putIfAbsent(Object var1, Object var2) {
      return this.putVal(var1, var2, true);
   }

   public boolean remove(Object var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var2 != null && this.replaceNode(var1, (Object)null, var2) != null;
      }
   }

   public boolean replace(Object var1, Object var2, Object var3) {
      if (var1 != null && var2 != null && var3 != null) {
         return this.replaceNode(var1, var3, var2) != null;
      } else {
         throw new NullPointerException();
      }
   }

   public Object replace(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         return this.replaceNode(var1, var2, (Object)null);
      } else {
         throw new NullPointerException();
      }
   }

   public Object getOrDefault(Object var1, Object var2) {
      Object var3;
      return (var3 = this.get(var1)) == null ? var2 : var3;
   }

   public void forEach(ConcurrentHashMapV8.BiAction var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         ConcurrentHashMapV8.Node[] var2;
         if ((var2 = this.table) != null) {
            ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

            ConcurrentHashMapV8.Node var4;
            while((var4 = var3.advance()) != null) {
               var1.apply(var4.key, var4.val);
            }
         }

      }
   }

   public void replaceAll(ConcurrentHashMapV8.BiFun var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         ConcurrentHashMapV8.Node[] var2;
         if ((var2 = this.table) != null) {
            ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

            ConcurrentHashMapV8.Node var4;
            while((var4 = var3.advance()) != null) {
               Object var5 = var4.val;
               Object var6 = var4.key;

               while(true) {
                  Object var7 = var1.apply(var6, var5);
                  if (var7 == null) {
                     throw new NullPointerException();
                  }

                  if (this.replaceNode(var6, var7, var5) != null || (var5 = this.get(var6)) == null) {
                     break;
                  }
               }
            }
         }

      }
   }

   public Object computeIfAbsent(Object var1, ConcurrentHashMapV8.Fun var2) {
      if (var1 != null && var2 != null) {
         int var3 = spread(var1.hashCode());
         Object var4 = null;
         int var5 = 0;
         ConcurrentHashMapV8.Node[] var6 = this.table;

         while(true) {
            int var8;
            while(var6 == null || (var8 = var6.length) == 0) {
               var6 = this.initTable();
            }

            ConcurrentHashMapV8.Node var7;
            int var9;
            ConcurrentHashMapV8.Node var13;
            if ((var7 = tabAt(var6, var9 = var8 - 1 & var3)) == null) {
               ConcurrentHashMapV8.ReservationNode var18 = new ConcurrentHashMapV8.ReservationNode();
               synchronized(var18){}
               if (casTabAt(var6, var9, (ConcurrentHashMapV8.Node)null, var18)) {
                  var5 = 1;
                  var13 = null;
                  if ((var4 = var2.apply(var1)) != null) {
                     var13 = new ConcurrentHashMapV8.Node(var3, var1, var4, (ConcurrentHashMapV8.Node)null);
                  }

                  setTabAt(var6, var9, var13);
               }

               if (var5 != 0) {
                  break;
               }
            } else {
               int var10;
               if ((var10 = var7.hash) == -1) {
                  var6 = this.helpTransfer(var6, var7);
               } else {
                  boolean var11 = false;
                  synchronized(var7){}
                  if (tabAt(var6, var9) == var7) {
                     if (var10 >= 0) {
                        label108: {
                           var5 = 1;

                           Object var14;
                           for(var13 = var7; var13.hash != var3 || (var14 = var13.key) != var1 && (var14 == null || !var1.equals(var14)); ++var5) {
                              ConcurrentHashMapV8.Node var16 = var13;
                              if ((var13 = var13.next) == null) {
                                 if ((var4 = var2.apply(var1)) != null) {
                                    var11 = true;
                                    var16.next = new ConcurrentHashMapV8.Node(var3, var1, var4, (ConcurrentHashMapV8.Node)null);
                                 }
                                 break label108;
                              }
                           }

                           var4 = var13.val;
                        }
                     } else if (var7 instanceof ConcurrentHashMapV8.TreeBin) {
                        var5 = 2;
                        ConcurrentHashMapV8.TreeBin var19 = (ConcurrentHashMapV8.TreeBin)var7;
                        ConcurrentHashMapV8.TreeNode var15;
                        ConcurrentHashMapV8.TreeNode var20;
                        if ((var20 = var19.root) != null && (var15 = var20.findTreeNode(var3, var1, (Class)null)) != null) {
                           var4 = var15.val;
                        } else if ((var4 = var2.apply(var1)) != null) {
                           var11 = true;
                           var19.putTreeVal(var3, var1, var4);
                        }
                     }
                  }

                  if (var5 != 0) {
                     if (var5 >= 8) {
                        this.treeifyBin(var6, var9);
                     }

                     if (!var11) {
                        return var4;
                     }
                     break;
                  }
               }
            }
         }

         if (var4 != null) {
            this.addCount(1L, var5);
         }

         return var4;
      } else {
         throw new NullPointerException();
      }
   }

   public Object computeIfPresent(Object var1, ConcurrentHashMapV8.BiFun var2) {
      if (var1 != null && var2 != null) {
         int var3 = spread(var1.hashCode());
         Object var4 = null;
         byte var5 = 0;
         int var6 = 0;
         ConcurrentHashMapV8.Node[] var7 = this.table;

         while(true) {
            int var9;
            while(var7 == null || (var9 = var7.length) == 0) {
               var7 = this.initTable();
            }

            ConcurrentHashMapV8.Node var8;
            int var10;
            if ((var8 = tabAt(var7, var10 = var9 - 1 & var3)) == null) {
               break;
            }

            int var11;
            if ((var11 = var8.hash) == -1) {
               var7 = this.helpTransfer(var7, var8);
            } else {
               synchronized(var8){}
               if (tabAt(var7, var10) == var8) {
                  if (var11 >= 0) {
                     label92: {
                        var6 = 1;
                        ConcurrentHashMapV8.Node var13 = var8;

                        ConcurrentHashMapV8.Node var14;
                        Object var15;
                        for(var14 = null; var13.hash != var3 || (var15 = var13.key) != var1 && (var15 == null || !var1.equals(var15)); ++var6) {
                           var14 = var13;
                           if ((var13 = var13.next) == null) {
                              break label92;
                           }
                        }

                        var4 = var2.apply(var1, var13.val);
                        if (var4 != null) {
                           var13.val = var4;
                        } else {
                           var5 = -1;
                           ConcurrentHashMapV8.Node var16 = var13.next;
                           if (var14 != null) {
                              var14.next = var16;
                           } else {
                              setTabAt(var7, var10, var16);
                           }
                        }
                     }
                  } else if (var8 instanceof ConcurrentHashMapV8.TreeBin) {
                     var6 = 2;
                     ConcurrentHashMapV8.TreeBin var18 = (ConcurrentHashMapV8.TreeBin)var8;
                     ConcurrentHashMapV8.TreeNode var19;
                     ConcurrentHashMapV8.TreeNode var20;
                     if ((var19 = var18.root) != null && (var20 = var19.findTreeNode(var3, var1, (Class)null)) != null) {
                        var4 = var2.apply(var1, var20.val);
                        if (var4 != null) {
                           var20.val = var4;
                        } else {
                           var5 = -1;
                           if (var18.removeTreeNode(var20)) {
                              setTabAt(var7, var10, untreeify(var18.first));
                           }
                        }
                     }
                  }
               }

               if (var6 != 0) {
                  break;
               }
            }
         }

         if (var5 != 0) {
            this.addCount((long)var5, var6);
         }

         return var4;
      } else {
         throw new NullPointerException();
      }
   }

   public Object compute(Object var1, ConcurrentHashMapV8.BiFun var2) {
      if (var1 != null && var2 != null) {
         int var3 = spread(var1.hashCode());
         Object var4 = null;
         byte var5 = 0;
         int var6 = 0;
         ConcurrentHashMapV8.Node[] var7 = this.table;

         while(true) {
            int var9;
            while(var7 == null || (var9 = var7.length) == 0) {
               var7 = this.initTable();
            }

            ConcurrentHashMapV8.Node var8;
            int var10;
            ConcurrentHashMapV8.Node var14;
            if ((var8 = tabAt(var7, var10 = var9 - 1 & var3)) == null) {
               ConcurrentHashMapV8.ReservationNode var12 = new ConcurrentHashMapV8.ReservationNode();
               synchronized(var12){}
               if (casTabAt(var7, var10, (ConcurrentHashMapV8.Node)null, var12)) {
                  var6 = 1;
                  var14 = null;
                  if ((var4 = var2.apply(var1, (Object)null)) != null) {
                     var5 = 1;
                     var14 = new ConcurrentHashMapV8.Node(var3, var1, var4, (ConcurrentHashMapV8.Node)null);
                  }

                  setTabAt(var7, var10, var14);
               }

               if (var6 != 0) {
                  break;
               }
            } else {
               int var11;
               if ((var11 = var8.hash) == -1) {
                  var7 = this.helpTransfer(var7, var8);
               } else {
                  synchronized(var8){}
                  if (tabAt(var7, var10) == var8) {
                     if (var11 >= 0) {
                        label122: {
                           var6 = 1;
                           ConcurrentHashMapV8.Node var13 = var8;

                           Object var15;
                           for(var14 = null; var13.hash != var3 || (var15 = var13.key) != var1 && (var15 == null || !var1.equals(var15)); ++var6) {
                              var14 = var13;
                              if ((var13 = var13.next) == null) {
                                 var4 = var2.apply(var1, (Object)null);
                                 if (var4 != null) {
                                    var5 = 1;
                                    var14.next = new ConcurrentHashMapV8.Node(var3, var1, var4, (ConcurrentHashMapV8.Node)null);
                                 }
                                 break label122;
                              }
                           }

                           var4 = var2.apply(var1, var13.val);
                           if (var4 != null) {
                              var13.val = var4;
                           } else {
                              var5 = -1;
                              ConcurrentHashMapV8.Node var16 = var13.next;
                              if (var14 != null) {
                                 var14.next = var16;
                              } else {
                                 setTabAt(var7, var10, var16);
                              }
                           }
                        }
                     } else if (var8 instanceof ConcurrentHashMapV8.TreeBin) {
                        var6 = 1;
                        ConcurrentHashMapV8.TreeBin var18 = (ConcurrentHashMapV8.TreeBin)var8;
                        ConcurrentHashMapV8.TreeNode var19;
                        ConcurrentHashMapV8.TreeNode var20;
                        if ((var19 = var18.root) != null) {
                           var20 = var19.findTreeNode(var3, var1, (Class)null);
                        } else {
                           var20 = null;
                        }

                        Object var21 = var20 == null ? null : var20.val;
                        var4 = var2.apply(var1, var21);
                        if (var4 != null) {
                           if (var20 != null) {
                              var20.val = var4;
                           } else {
                              var5 = 1;
                              var18.putTreeVal(var3, var1, var4);
                           }
                        } else if (var20 != null) {
                           var5 = -1;
                           if (var18.removeTreeNode(var20)) {
                              setTabAt(var7, var10, untreeify(var18.first));
                           }
                        }
                     }
                  }

                  if (var6 != 0) {
                     if (var6 >= 8) {
                        this.treeifyBin(var7, var10);
                     }
                     break;
                  }
               }
            }
         }

         if (var5 != 0) {
            this.addCount((long)var5, var6);
         }

         return var4;
      } else {
         throw new NullPointerException();
      }
   }

   public Object merge(Object var1, Object var2, ConcurrentHashMapV8.BiFun var3) {
      if (var1 != null && var2 != null && var3 != null) {
         int var4 = spread(var1.hashCode());
         Object var5 = null;
         byte var6 = 0;
         int var7 = 0;
         ConcurrentHashMapV8.Node[] var8 = this.table;

         while(true) {
            int var10;
            while(var8 == null || (var10 = var8.length) == 0) {
               var8 = this.initTable();
            }

            ConcurrentHashMapV8.Node var9;
            int var11;
            if ((var9 = tabAt(var8, var11 = var10 - 1 & var4)) == null) {
               if (casTabAt(var8, var11, (ConcurrentHashMapV8.Node)null, new ConcurrentHashMapV8.Node(var4, var1, var2, (ConcurrentHashMapV8.Node)null))) {
                  var6 = 1;
                  var5 = var2;
                  break;
               }
            } else {
               int var12;
               if ((var12 = var9.hash) == -1) {
                  var8 = this.helpTransfer(var8, var9);
               } else {
                  synchronized(var9){}
                  if (tabAt(var8, var11) == var9) {
                     if (var12 >= 0) {
                        label111: {
                           var7 = 1;
                           ConcurrentHashMapV8.Node var14 = var9;

                           ConcurrentHashMapV8.Node var15;
                           Object var16;
                           for(var15 = null; var14.hash != var4 || (var16 = var14.key) != var1 && (var16 == null || !var1.equals(var16)); ++var7) {
                              var15 = var14;
                              if ((var14 = var14.next) == null) {
                                 var6 = 1;
                                 var5 = var2;
                                 var15.next = new ConcurrentHashMapV8.Node(var4, var1, var2, (ConcurrentHashMapV8.Node)null);
                                 break label111;
                              }
                           }

                           var5 = var3.apply(var14.val, var2);
                           if (var5 != null) {
                              var14.val = var5;
                           } else {
                              var6 = -1;
                              ConcurrentHashMapV8.Node var17 = var14.next;
                              if (var15 != null) {
                                 var15.next = var17;
                              } else {
                                 setTabAt(var8, var11, var17);
                              }
                           }
                        }
                     } else if (var9 instanceof ConcurrentHashMapV8.TreeBin) {
                        var7 = 2;
                        ConcurrentHashMapV8.TreeBin var19 = (ConcurrentHashMapV8.TreeBin)var9;
                        ConcurrentHashMapV8.TreeNode var20 = var19.root;
                        ConcurrentHashMapV8.TreeNode var21 = var20 == null ? null : var20.findTreeNode(var4, var1, (Class)null);
                        var5 = var21 == null ? var2 : var3.apply(var21.val, var2);
                        if (var5 != null) {
                           if (var21 != null) {
                              var21.val = var5;
                           } else {
                              var6 = 1;
                              var19.putTreeVal(var4, var1, var5);
                           }
                        } else if (var21 != null) {
                           var6 = -1;
                           if (var19.removeTreeNode(var21)) {
                              setTabAt(var8, var11, untreeify(var19.first));
                           }
                        }
                     }
                  }

                  if (var7 != 0) {
                     if (var7 >= 8) {
                        this.treeifyBin(var8, var11);
                     }
                     break;
                  }
               }
            }
         }

         if (var6 != 0) {
            this.addCount((long)var6, var7);
         }

         return var5;
      } else {
         throw new NullPointerException();
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean contains(Object var1) {
      return this.containsValue(var1);
   }

   public Enumeration keys() {
      ConcurrentHashMapV8.Node[] var1;
      int var2 = (var1 = this.table) == null ? 0 : var1.length;
      return new ConcurrentHashMapV8.KeyIterator(var1, var2, 0, var2, this);
   }

   public Enumeration elements() {
      ConcurrentHashMapV8.Node[] var1;
      int var2 = (var1 = this.table) == null ? 0 : var1.length;
      return new ConcurrentHashMapV8.ValueIterator(var1, var2, 0, var2, this);
   }

   public long mappingCount() {
      long var1 = this.sumCount();
      return var1 < 0L ? 0L : var1;
   }

   public static ConcurrentHashMapV8.KeySetView newKeySet() {
      return new ConcurrentHashMapV8.KeySetView(new ConcurrentHashMapV8(), Boolean.TRUE);
   }

   public static ConcurrentHashMapV8.KeySetView newKeySet(int var0) {
      return new ConcurrentHashMapV8.KeySetView(new ConcurrentHashMapV8(var0), Boolean.TRUE);
   }

   public ConcurrentHashMapV8.KeySetView keySet(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return new ConcurrentHashMapV8.KeySetView(this, var1);
      }
   }

   private final ConcurrentHashMapV8.Node[] initTable() {
      ConcurrentHashMapV8.Node[] var1;
      while((var1 = this.table) == null || var1.length == 0) {
         int var2;
         if ((var2 = this.sizeCtl) < 0) {
            Thread.yield();
         } else if (U.compareAndSwapInt(this, SIZECTL, var2, -1)) {
            if ((var1 = this.table) == null || var1.length == 0) {
               int var3 = var2 > 0 ? var2 : 16;
               ConcurrentHashMapV8.Node[] var4 = (ConcurrentHashMapV8.Node[])(new ConcurrentHashMapV8.Node[var3]);
               var1 = var4;
               this.table = var4;
               var2 = var3 - (var3 >>> 2);
            }

            this.sizeCtl = var2;
            break;
         }
      }

      return var1;
   }

   private final void addCount(long var1, int var3) {
      ConcurrentHashMapV8.CounterCell[] var4;
      long var5;
      long var7;
      if ((var4 = this.counterCells) != null || !U.compareAndSwapLong(this, BASECOUNT, var5 = this.baseCount, var7 = var5 + var1)) {
         boolean var14 = true;
         ConcurrentHashMapV8.CounterHashCode var9;
         ConcurrentHashMapV8.CounterCell var10;
         long var11;
         int var13;
         if ((var9 = (ConcurrentHashMapV8.CounterHashCode)threadCounterHashCode.get()) == null || var4 == null || (var13 = var4.length - 1) < 0 || (var10 = var4[var13 & var9.code]) == null || !(var14 = U.compareAndSwapLong(var10, CELLVALUE, var11 = var10.value, var11 + var1))) {
            this.fullAddCount(var1, var9, var14);
            return;
         }

         if (var3 <= 1) {
            return;
         }

         var7 = this.sumCount();
      }

      ConcurrentHashMapV8.Node[] var15;
      int var17;
      if (var3 >= 0) {
         for(; var7 >= (long)(var17 = this.sizeCtl) && (var15 = this.table) != null && var15.length < 1073741824; var7 = this.sumCount()) {
            if (var17 < 0) {
               ConcurrentHashMapV8.Node[] var16;
               if (var17 == -1 || this.transferIndex <= this.transferOrigin || (var16 = this.nextTable) == null) {
                  break;
               }

               if (U.compareAndSwapInt(this, SIZECTL, var17, var17 - 1)) {
                  this.transfer(var15, var16);
               }
            } else if (U.compareAndSwapInt(this, SIZECTL, var17, -2)) {
               this.transfer(var15, (ConcurrentHashMapV8.Node[])null);
            }
         }
      }

   }

   final ConcurrentHashMapV8.Node[] helpTransfer(ConcurrentHashMapV8.Node[] var1, ConcurrentHashMapV8.Node var2) {
      ConcurrentHashMapV8.Node[] var3;
      if (var2 instanceof ConcurrentHashMapV8.ForwardingNode && (var3 = ((ConcurrentHashMapV8.ForwardingNode)var2).nextTable) != null) {
         int var4;
         if (var3 == this.nextTable && var1 == this.table && this.transferIndex > this.transferOrigin && (var4 = this.sizeCtl) < -1 && U.compareAndSwapInt(this, SIZECTL, var4, var4 - 1)) {
            this.transfer(var1, var3);
         }

         return var3;
      } else {
         return this.table;
      }
   }

   private final void tryPresize(int var1) {
      int var2 = var1 >= 536870912 ? 1073741824 : tableSizeFor(var1 + (var1 >>> 1) + 1);

      int var3;
      while((var3 = this.sizeCtl) >= 0) {
         ConcurrentHashMapV8.Node[] var4 = this.table;
         int var5;
         if (var4 != null && (var5 = var4.length) != 0) {
            if (var2 <= var3 || var5 >= 1073741824) {
               break;
            }

            if (var4 == this.table && U.compareAndSwapInt(this, SIZECTL, var3, -2)) {
               this.transfer(var4, (ConcurrentHashMapV8.Node[])null);
            }
         } else {
            var5 = var3 > var2 ? var3 : var2;
            if (U.compareAndSwapInt(this, SIZECTL, var3, -1)) {
               if (this.table == var4) {
                  ConcurrentHashMapV8.Node[] var6 = (ConcurrentHashMapV8.Node[])(new ConcurrentHashMapV8.Node[var5]);
                  this.table = var6;
                  var3 = var5 - (var5 >>> 2);
               }

               this.sizeCtl = var3;
            }
         }
      }

   }

   private final void transfer(ConcurrentHashMapV8.Node[] var1, ConcurrentHashMapV8.Node[] var2) {
      int var3 = var1.length;
      int var4;
      if ((var4 = NCPU > 1 ? (var3 >>> 3) / NCPU : var3) < 16) {
         var4 = 16;
      }

      if (var2 == null) {
         try {
            ConcurrentHashMapV8.Node[] var5 = (ConcurrentHashMapV8.Node[])(new ConcurrentHashMapV8.Node[var3 << 1]);
            var2 = var5;
         } catch (Throwable var29) {
            this.sizeCtl = Integer.MAX_VALUE;
            return;
         }

         this.nextTable = var2;
         this.transferOrigin = var3;
         this.transferIndex = var3;
         ConcurrentHashMapV8.ForwardingNode var30 = new ConcurrentHashMapV8.ForwardingNode(var1);
         int var6 = var3;

         while(var6 > 0) {
            int var7 = var6 > var4 ? var6 - var4 : 0;

            int var8;
            for(var8 = var7; var8 < var6; ++var8) {
               var2[var8] = var30;
            }

            for(var8 = var3 + var7; var8 < var3 + var6; ++var8) {
               var2[var8] = var30;
            }

            var6 = var7;
            U.putOrderedInt(this, TRANSFERORIGIN, var7);
         }
      }

      int var31 = var2.length;
      ConcurrentHashMapV8.ForwardingNode var32 = new ConcurrentHashMapV8.ForwardingNode(var2);
      boolean var33 = true;
      boolean var34 = false;
      int var9 = 0;
      int var10 = 0;

      while(true) {
         while(true) {
            while(!var33) {
               if (var9 >= 0 && var9 < var3 && var9 + var3 < var31) {
                  ConcurrentHashMapV8.Node var14;
                  if ((var14 = tabAt(var1, var9)) == null) {
                     if (casTabAt(var1, var9, (ConcurrentHashMapV8.Node)null, var32)) {
                        setTabAt(var2, var9, (ConcurrentHashMapV8.Node)null);
                        setTabAt(var2, var9 + var3, (ConcurrentHashMapV8.Node)null);
                        var33 = true;
                     }
                  } else {
                     int var13;
                     if ((var13 = var14.hash) == -1) {
                        var33 = true;
                     } else {
                        synchronized(var14){}
                        if (tabAt(var1, var9) == var14) {
                           if (var13 >= 0) {
                              int var18 = var13 & var3;
                              ConcurrentHashMapV8.Node var19 = var14;

                              ConcurrentHashMapV8.Node var20;
                              int var21;
                              for(var20 = var14.next; var20 != null; var20 = var20.next) {
                                 var21 = var20.hash & var3;
                                 if (var21 != var18) {
                                    var18 = var21;
                                    var19 = var20;
                                 }
                              }

                              ConcurrentHashMapV8.Node var16;
                              ConcurrentHashMapV8.Node var17;
                              if (var18 == 0) {
                                 var16 = var19;
                                 var17 = null;
                              } else {
                                 var17 = var19;
                                 var16 = null;
                              }

                              for(var20 = var14; var20 != var19; var20 = var20.next) {
                                 var21 = var20.hash;
                                 Object var22 = var20.key;
                                 Object var23 = var20.val;
                                 if ((var21 & var3) == 0) {
                                    var16 = new ConcurrentHashMapV8.Node(var21, var22, var23, var16);
                                 } else {
                                    var17 = new ConcurrentHashMapV8.Node(var21, var22, var23, var17);
                                 }
                              }

                              setTabAt(var2, var9, var16);
                              setTabAt(var2, var9 + var3, var17);
                              setTabAt(var1, var9, var32);
                              var33 = true;
                           } else if (var14 instanceof ConcurrentHashMapV8.TreeBin) {
                              ConcurrentHashMapV8.TreeBin var36 = (ConcurrentHashMapV8.TreeBin)var14;
                              ConcurrentHashMapV8.TreeNode var38 = null;
                              ConcurrentHashMapV8.TreeNode var39 = null;
                              ConcurrentHashMapV8.TreeNode var40 = null;
                              ConcurrentHashMapV8.TreeNode var41 = null;
                              int var42 = 0;
                              int var24 = 0;

                              for(Object var25 = var36.first; var25 != null; var25 = ((ConcurrentHashMapV8.Node)var25).next) {
                                 int var26 = ((ConcurrentHashMapV8.Node)var25).hash;
                                 ConcurrentHashMapV8.TreeNode var27 = new ConcurrentHashMapV8.TreeNode(var26, ((ConcurrentHashMapV8.Node)var25).key, ((ConcurrentHashMapV8.Node)var25).val, (ConcurrentHashMapV8.Node)null, (ConcurrentHashMapV8.TreeNode)null);
                                 if ((var26 & var3) == 0) {
                                    if ((var27.prev = var39) == null) {
                                       var38 = var27;
                                    } else {
                                       var39.next = var27;
                                    }

                                    var39 = var27;
                                    ++var42;
                                 } else {
                                    if ((var27.prev = var41) == null) {
                                       var40 = var27;
                                    } else {
                                       var41.next = var27;
                                    }

                                    var41 = var27;
                                    ++var24;
                                 }
                              }

                              Object var35 = var42 <= 6 ? untreeify(var38) : (var24 != 0 ? new ConcurrentHashMapV8.TreeBin(var38) : var36);
                              Object var37 = var24 <= 6 ? untreeify(var40) : (var42 != 0 ? new ConcurrentHashMapV8.TreeBin(var40) : var36);
                              setTabAt(var2, var9, (ConcurrentHashMapV8.Node)var35);
                              setTabAt(var2, var9 + var3, (ConcurrentHashMapV8.Node)var37);
                              setTabAt(var1, var9, var32);
                              var33 = true;
                           }
                        }
                     }
                  }
               } else {
                  if (var34) {
                     this.nextTable = null;
                     this.table = var2;
                     this.sizeCtl = (var3 << 1) - (var3 >>> 1);
                     return;
                  }

                  int var15;
                  int var10003;
                  do {
                     var10003 = var15 = this.sizeCtl;
                     ++var15;
                  } while(!U.compareAndSwapInt(this, SIZECTL, var10003, var15));

                  if (var15 != -1) {
                     return;
                  }

                  var33 = true;
                  var34 = true;
                  var9 = var3;
               }
            }

            --var9;
            if (var9 < var10 && !var34) {
               int var11;
               if ((var11 = this.transferIndex) <= this.transferOrigin) {
                  var9 = -1;
                  var33 = false;
               } else {
                  int var12;
                  if (U.compareAndSwapInt(this, TRANSFERINDEX, var11, var12 = var11 > var4 ? var11 - var4 : 0)) {
                     var10 = var12;
                     var9 = var11 - 1;
                     var33 = false;
                  }
               }
            } else {
               var33 = false;
            }
         }
      }
   }

   private final void treeifyBin(ConcurrentHashMapV8.Node[] var1, int var2) {
      if (var1 != null) {
         int var4;
         if ((var4 = var1.length) < 64) {
            int var5;
            if (var1 == this.table && (var5 = this.sizeCtl) >= 0 && U.compareAndSwapInt(this, SIZECTL, var5, -2)) {
               this.transfer(var1, (ConcurrentHashMapV8.Node[])null);
            }
         } else {
            ConcurrentHashMapV8.Node var3;
            if ((var3 = tabAt(var1, var2)) != null && var3.hash >= 0) {
               synchronized(var3){}
               if (tabAt(var1, var2) == var3) {
                  ConcurrentHashMapV8.TreeNode var7 = null;
                  ConcurrentHashMapV8.TreeNode var8 = null;

                  for(ConcurrentHashMapV8.Node var9 = var3; var9 != null; var9 = var9.next) {
                     ConcurrentHashMapV8.TreeNode var10 = new ConcurrentHashMapV8.TreeNode(var9.hash, var9.key, var9.val, (ConcurrentHashMapV8.Node)null, (ConcurrentHashMapV8.TreeNode)null);
                     if ((var10.prev = var8) == null) {
                        var7 = var10;
                     } else {
                        var8.next = var10;
                     }

                     var8 = var10;
                  }

                  setTabAt(var1, var2, new ConcurrentHashMapV8.TreeBin(var7));
               }
            }
         }
      }

   }

   static ConcurrentHashMapV8.Node untreeify(ConcurrentHashMapV8.Node var0) {
      ConcurrentHashMapV8.Node var1 = null;
      ConcurrentHashMapV8.Node var2 = null;

      for(ConcurrentHashMapV8.Node var3 = var0; var3 != null; var3 = var3.next) {
         ConcurrentHashMapV8.Node var4 = new ConcurrentHashMapV8.Node(var3.hash, var3.key, var3.val, (ConcurrentHashMapV8.Node)null);
         if (var2 == null) {
            var1 = var4;
         } else {
            var2.next = var4;
         }

         var2 = var4;
      }

      return var1;
   }

   final int batchFor(long var1) {
      long var3;
      if (var1 != Long.MAX_VALUE && (var3 = this.sumCount()) > 1L && var3 >= var1) {
         int var5 = ForkJoinPool.getCommonPoolParallelism() << 2;
         return var1 > 0L && (var3 /= var1) < (long)var5 ? (int)var3 : var5;
      } else {
         return 0;
      }
   }

   public void forEach(long var1, ConcurrentHashMapV8.BiAction var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         (new ConcurrentHashMapV8.ForEachMappingTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3)).invoke();
      }
   }

   public void forEach(long var1, ConcurrentHashMapV8.BiFun var3, ConcurrentHashMapV8.Action var4) {
      if (var3 != null && var4 != null) {
         (new ConcurrentHashMapV8.ForEachTransformedMappingTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public Object search(long var1, ConcurrentHashMapV8.BiFun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.SearchMappingsTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, new AtomicReference())).invoke();
      }
   }

   public Object reduce(long var1, ConcurrentHashMapV8.BiFun var3, ConcurrentHashMapV8.BiFun var4) {
      if (var3 != null && var4 != null) {
         return (new ConcurrentHashMapV8.MapReduceMappingsTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceMappingsTask)null, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public double reduceToDouble(long var1, ConcurrentHashMapV8.ObjectByObjectToDouble var3, double var4, ConcurrentHashMapV8.DoubleByDoubleToDouble var6) {
      if (var3 != null && var6 != null) {
         return (Double)(new ConcurrentHashMapV8.MapReduceMappingsToDoubleTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceMappingsToDoubleTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public long reduceToLong(long var1, ConcurrentHashMapV8.ObjectByObjectToLong var3, long var4, ConcurrentHashMapV8.LongByLongToLong var6) {
      if (var3 != null && var6 != null) {
         return (Long)(new ConcurrentHashMapV8.MapReduceMappingsToLongTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceMappingsToLongTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public int reduceToInt(long var1, ConcurrentHashMapV8.ObjectByObjectToInt var3, int var4, ConcurrentHashMapV8.IntByIntToInt var5) {
      if (var3 != null && var5 != null) {
         return (Integer)(new ConcurrentHashMapV8.MapReduceMappingsToIntTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceMappingsToIntTask)null, var3, var4, var5)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public void forEachKey(long var1, ConcurrentHashMapV8.Action var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         (new ConcurrentHashMapV8.ForEachKeyTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3)).invoke();
      }
   }

   public void forEachKey(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.Action var4) {
      if (var3 != null && var4 != null) {
         (new ConcurrentHashMapV8.ForEachTransformedKeyTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public Object searchKeys(long var1, ConcurrentHashMapV8.Fun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.SearchKeysTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, new AtomicReference())).invoke();
      }
   }

   public Object reduceKeys(long var1, ConcurrentHashMapV8.BiFun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.ReduceKeysTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.ReduceKeysTask)null, var3)).invoke();
      }
   }

   public Object reduceKeys(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.BiFun var4) {
      if (var3 != null && var4 != null) {
         return (new ConcurrentHashMapV8.MapReduceKeysTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceKeysTask)null, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public double reduceKeysToDouble(long var1, ConcurrentHashMapV8.ObjectToDouble var3, double var4, ConcurrentHashMapV8.DoubleByDoubleToDouble var6) {
      if (var3 != null && var6 != null) {
         return (Double)(new ConcurrentHashMapV8.MapReduceKeysToDoubleTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceKeysToDoubleTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public long reduceKeysToLong(long var1, ConcurrentHashMapV8.ObjectToLong var3, long var4, ConcurrentHashMapV8.LongByLongToLong var6) {
      if (var3 != null && var6 != null) {
         return (Long)(new ConcurrentHashMapV8.MapReduceKeysToLongTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceKeysToLongTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public int reduceKeysToInt(long var1, ConcurrentHashMapV8.ObjectToInt var3, int var4, ConcurrentHashMapV8.IntByIntToInt var5) {
      if (var3 != null && var5 != null) {
         return (Integer)(new ConcurrentHashMapV8.MapReduceKeysToIntTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceKeysToIntTask)null, var3, var4, var5)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public void forEachValue(long var1, ConcurrentHashMapV8.Action var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         (new ConcurrentHashMapV8.ForEachValueTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3)).invoke();
      }
   }

   public void forEachValue(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.Action var4) {
      if (var3 != null && var4 != null) {
         (new ConcurrentHashMapV8.ForEachTransformedValueTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public Object searchValues(long var1, ConcurrentHashMapV8.Fun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.SearchValuesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, new AtomicReference())).invoke();
      }
   }

   public Object reduceValues(long var1, ConcurrentHashMapV8.BiFun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.ReduceValuesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.ReduceValuesTask)null, var3)).invoke();
      }
   }

   public Object reduceValues(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.BiFun var4) {
      if (var3 != null && var4 != null) {
         return (new ConcurrentHashMapV8.MapReduceValuesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceValuesTask)null, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public double reduceValuesToDouble(long var1, ConcurrentHashMapV8.ObjectToDouble var3, double var4, ConcurrentHashMapV8.DoubleByDoubleToDouble var6) {
      if (var3 != null && var6 != null) {
         return (Double)(new ConcurrentHashMapV8.MapReduceValuesToDoubleTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceValuesToDoubleTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public long reduceValuesToLong(long var1, ConcurrentHashMapV8.ObjectToLong var3, long var4, ConcurrentHashMapV8.LongByLongToLong var6) {
      if (var3 != null && var6 != null) {
         return (Long)(new ConcurrentHashMapV8.MapReduceValuesToLongTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceValuesToLongTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public int reduceValuesToInt(long var1, ConcurrentHashMapV8.ObjectToInt var3, int var4, ConcurrentHashMapV8.IntByIntToInt var5) {
      if (var3 != null && var5 != null) {
         return (Integer)(new ConcurrentHashMapV8.MapReduceValuesToIntTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceValuesToIntTask)null, var3, var4, var5)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public void forEachEntry(long var1, ConcurrentHashMapV8.Action var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         (new ConcurrentHashMapV8.ForEachEntryTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3)).invoke();
      }
   }

   public void forEachEntry(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.Action var4) {
      if (var3 != null && var4 != null) {
         (new ConcurrentHashMapV8.ForEachTransformedEntryTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public Object searchEntries(long var1, ConcurrentHashMapV8.Fun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (new ConcurrentHashMapV8.SearchEntriesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, var3, new AtomicReference())).invoke();
      }
   }

   public Entry reduceEntries(long var1, ConcurrentHashMapV8.BiFun var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         return (Entry)(new ConcurrentHashMapV8.ReduceEntriesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.ReduceEntriesTask)null, var3)).invoke();
      }
   }

   public Object reduceEntries(long var1, ConcurrentHashMapV8.Fun var3, ConcurrentHashMapV8.BiFun var4) {
      if (var3 != null && var4 != null) {
         return (new ConcurrentHashMapV8.MapReduceEntriesTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceEntriesTask)null, var3, var4)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public double reduceEntriesToDouble(long var1, ConcurrentHashMapV8.ObjectToDouble var3, double var4, ConcurrentHashMapV8.DoubleByDoubleToDouble var6) {
      if (var3 != null && var6 != null) {
         return (Double)(new ConcurrentHashMapV8.MapReduceEntriesToDoubleTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceEntriesToDoubleTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public long reduceEntriesToLong(long var1, ConcurrentHashMapV8.ObjectToLong var3, long var4, ConcurrentHashMapV8.LongByLongToLong var6) {
      if (var3 != null && var6 != null) {
         return (Long)(new ConcurrentHashMapV8.MapReduceEntriesToLongTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceEntriesToLongTask)null, var3, var4, var6)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   public int reduceEntriesToInt(long var1, ConcurrentHashMapV8.ObjectToInt var3, int var4, ConcurrentHashMapV8.IntByIntToInt var5) {
      if (var3 != null && var5 != null) {
         return (Integer)(new ConcurrentHashMapV8.MapReduceEntriesToIntTask((ConcurrentHashMapV8.BulkTask)null, this.batchFor(var1), 0, 0, this.table, (ConcurrentHashMapV8.MapReduceEntriesToIntTask)null, var3, var4, var5)).invoke();
      } else {
         throw new NullPointerException();
      }
   }

   final long sumCount() {
      ConcurrentHashMapV8.CounterCell[] var1 = this.counterCells;
      long var3 = this.baseCount;
      if (var1 != null) {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            ConcurrentHashMapV8.CounterCell var2;
            if ((var2 = var1[var5]) != null) {
               var3 += var2.value;
            }
         }
      }

      return var3;
   }

   private final void fullAddCount(long var1, ConcurrentHashMapV8.CounterHashCode var3, boolean var4) {
      int var5;
      if (var3 == null) {
         var3 = new ConcurrentHashMapV8.CounterHashCode();
         int var6 = counterHashCodeGenerator.addAndGet(1640531527);
         var5 = var3.code = var6 == 0 ? 1 : var6;
         threadCounterHashCode.set(var3);
      } else {
         var5 = var3.code;
      }

      boolean var20 = false;

      while(true) {
         ConcurrentHashMapV8.CounterCell[] var7;
         int var9;
         long var10;
         if ((var7 = this.counterCells) != null && (var9 = var7.length) > 0) {
            ConcurrentHashMapV8.CounterCell var8;
            if ((var8 = var7[var9 - 1 & var5]) == null) {
               if (this.cellsBusy == 0) {
                  ConcurrentHashMapV8.CounterCell var21 = new ConcurrentHashMapV8.CounterCell(var1);
                  if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                     boolean var22 = false;
                     ConcurrentHashMapV8.CounterCell[] var14;
                     int var15;
                     int var16;
                     if ((var14 = this.counterCells) != null && (var15 = var14.length) > 0 && var14[var16 = var15 - 1 & var5] == null) {
                        var14[var16] = var21;
                        var22 = true;
                     }

                     this.cellsBusy = 0;
                     if (var22) {
                        break;
                     }
                     continue;
                  }
               }

               var20 = false;
            } else if (!var4) {
               var4 = true;
            } else {
               if (U.compareAndSwapLong(var8, CELLVALUE, var10 = var8.value, var10 + var1)) {
                  break;
               }

               if (this.counterCells == var7 && var9 < NCPU) {
                  if (!var20) {
                     var20 = true;
                  } else if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                     if (this.counterCells == var7) {
                        ConcurrentHashMapV8.CounterCell[] var23 = new ConcurrentHashMapV8.CounterCell[var9 << 1];

                        for(int var24 = 0; var24 < var9; ++var24) {
                           var23[var24] = var7[var24];
                        }

                        this.counterCells = var23;
                     }

                     this.cellsBusy = 0;
                     var20 = false;
                     continue;
                  }
               } else {
                  var20 = false;
               }
            }

            var5 ^= var5 << 13;
            var5 ^= var5 >>> 17;
            var5 ^= var5 << 5;
         } else if (this.cellsBusy == 0 && this.counterCells == var7 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
            boolean var12 = false;
            if (this.counterCells == var7) {
               ConcurrentHashMapV8.CounterCell[] var13 = new ConcurrentHashMapV8.CounterCell[2];
               var13[var5 & 1] = new ConcurrentHashMapV8.CounterCell(var1);
               this.counterCells = var13;
               var12 = true;
            }

            this.cellsBusy = 0;
            if (var12) {
               break;
            }
         } else if (U.compareAndSwapLong(this, BASECOUNT, var10 = this.baseCount, var10 + var1)) {
            break;
         }
      }

      var3.code = var5;
   }

   private static Unsafe getUnsafe() {
      try {
         return Unsafe.getUnsafe();
      } catch (SecurityException var2) {
         try {
            return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Unsafe run() throws Exception {
                  Class var1 = Unsafe.class;
                  Field[] var2 = var1.getDeclaredFields();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Field var5 = var2[var4];
                     var5.setAccessible(true);
                     Object var6 = var5.get((Object)null);
                     if (var1.isInstance(var6)) {
                        return (Unsafe)var1.cast(var6);
                     }
                  }

                  throw new NoSuchFieldError("the Unsafe");
               }

               public Object run() throws Exception {
                  return this.run();
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
         }
      }
   }

   public Set keySet() {
      return this.keySet();
   }

   static Unsafe access$000() {
      return getUnsafe();
   }

   static {
      serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("segments", ConcurrentHashMapV8.Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};
      counterHashCodeGenerator = new AtomicInteger();
      threadCounterHashCode = new ThreadLocal();

      try {
         U = getUnsafe();
         Class var0 = ConcurrentHashMapV8.class;
         SIZECTL = U.objectFieldOffset(var0.getDeclaredField("sizeCtl"));
         TRANSFERINDEX = U.objectFieldOffset(var0.getDeclaredField("transferIndex"));
         TRANSFERORIGIN = U.objectFieldOffset(var0.getDeclaredField("transferOrigin"));
         BASECOUNT = U.objectFieldOffset(var0.getDeclaredField("baseCount"));
         CELLSBUSY = U.objectFieldOffset(var0.getDeclaredField("cellsBusy"));
         Class var1 = ConcurrentHashMapV8.CounterCell.class;
         CELLVALUE = U.objectFieldOffset(var1.getDeclaredField("value"));
         Class var2 = ConcurrentHashMapV8.Node[].class;
         ABASE = (long)U.arrayBaseOffset(var2);
         int var3 = U.arrayIndexScale(var2);
         if ((var3 & var3 - 1) != 0) {
            throw new Error("data type scale not a power of two");
         } else {
            ASHIFT = 31 - Integer.numberOfLeadingZeros(var3);
         }
      } catch (Exception var4) {
         throw new Error(var4);
      }
   }

   static final class CounterHashCode {
      int code;
   }

   static final class CounterCell {
      volatile long p0;
      volatile long p1;
      volatile long p2;
      volatile long p3;
      volatile long p4;
      volatile long p5;
      volatile long p6;
      volatile long value;
      volatile long q0;
      volatile long q1;
      volatile long q2;
      volatile long q3;
      volatile long q4;
      volatile long q5;
      volatile long q6;

      CounterCell(long var1) {
         this.value = var1;
      }
   }

   static final class MapReduceMappingsToIntTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectByObjectToInt transformer;
      final ConcurrentHashMapV8.IntByIntToInt reducer;
      final int basis;
      int result;
      ConcurrentHashMapV8.MapReduceMappingsToIntTask rights;
      ConcurrentHashMapV8.MapReduceMappingsToIntTask nextRight;

      MapReduceMappingsToIntTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceMappingsToIntTask var6, ConcurrentHashMapV8.ObjectByObjectToInt var7, int var8, ConcurrentHashMapV8.IntByIntToInt var9) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var9;
      }

      public final Integer getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectByObjectToInt var1;
         ConcurrentHashMapV8.IntByIntToInt var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.basis;
            int var4 = this.baseIndex;

            int var5;
            int var6;
            while(this.batch > 0 && (var6 = (var5 = this.baseLimit) + var4 >>> 1) > var4) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceMappingsToIntTask(this, this.batch >>>= 1, this.baseLimit = var6, var5, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var7;
            while((var7 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var7.key, var7.val));
            }

            this.result = var3;

            for(CountedCompleter var8 = this.firstComplete(); var8 != null; var8 = var8.nextComplete()) {
               ConcurrentHashMapV8.MapReduceMappingsToIntTask var9 = (ConcurrentHashMapV8.MapReduceMappingsToIntTask)var8;

               for(ConcurrentHashMapV8.MapReduceMappingsToIntTask var10 = var9.rights; var10 != null; var10 = var9.rights = var10.nextRight) {
                  var9.result = var2.apply(var9.result, var10.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceEntriesToIntTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToInt transformer;
      final ConcurrentHashMapV8.IntByIntToInt reducer;
      final int basis;
      int result;
      ConcurrentHashMapV8.MapReduceEntriesToIntTask rights;
      ConcurrentHashMapV8.MapReduceEntriesToIntTask nextRight;

      MapReduceEntriesToIntTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceEntriesToIntTask var6, ConcurrentHashMapV8.ObjectToInt var7, int var8, ConcurrentHashMapV8.IntByIntToInt var9) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var9;
      }

      public final Integer getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToInt var1;
         ConcurrentHashMapV8.IntByIntToInt var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.basis;
            int var4 = this.baseIndex;

            int var5;
            int var6;
            while(this.batch > 0 && (var6 = (var5 = this.baseLimit) + var4 >>> 1) > var4) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceEntriesToIntTask(this, this.batch >>>= 1, this.baseLimit = var6, var5, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var7;
            while((var7 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var7));
            }

            this.result = var3;

            for(CountedCompleter var8 = this.firstComplete(); var8 != null; var8 = var8.nextComplete()) {
               ConcurrentHashMapV8.MapReduceEntriesToIntTask var9 = (ConcurrentHashMapV8.MapReduceEntriesToIntTask)var8;

               for(ConcurrentHashMapV8.MapReduceEntriesToIntTask var10 = var9.rights; var10 != null; var10 = var9.rights = var10.nextRight) {
                  var9.result = var2.apply(var9.result, var10.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceValuesToIntTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToInt transformer;
      final ConcurrentHashMapV8.IntByIntToInt reducer;
      final int basis;
      int result;
      ConcurrentHashMapV8.MapReduceValuesToIntTask rights;
      ConcurrentHashMapV8.MapReduceValuesToIntTask nextRight;

      MapReduceValuesToIntTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceValuesToIntTask var6, ConcurrentHashMapV8.ObjectToInt var7, int var8, ConcurrentHashMapV8.IntByIntToInt var9) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var9;
      }

      public final Integer getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToInt var1;
         ConcurrentHashMapV8.IntByIntToInt var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.basis;
            int var4 = this.baseIndex;

            int var5;
            int var6;
            while(this.batch > 0 && (var6 = (var5 = this.baseLimit) + var4 >>> 1) > var4) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceValuesToIntTask(this, this.batch >>>= 1, this.baseLimit = var6, var5, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var7;
            while((var7 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var7.val));
            }

            this.result = var3;

            for(CountedCompleter var8 = this.firstComplete(); var8 != null; var8 = var8.nextComplete()) {
               ConcurrentHashMapV8.MapReduceValuesToIntTask var9 = (ConcurrentHashMapV8.MapReduceValuesToIntTask)var8;

               for(ConcurrentHashMapV8.MapReduceValuesToIntTask var10 = var9.rights; var10 != null; var10 = var9.rights = var10.nextRight) {
                  var9.result = var2.apply(var9.result, var10.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceKeysToIntTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToInt transformer;
      final ConcurrentHashMapV8.IntByIntToInt reducer;
      final int basis;
      int result;
      ConcurrentHashMapV8.MapReduceKeysToIntTask rights;
      ConcurrentHashMapV8.MapReduceKeysToIntTask nextRight;

      MapReduceKeysToIntTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceKeysToIntTask var6, ConcurrentHashMapV8.ObjectToInt var7, int var8, ConcurrentHashMapV8.IntByIntToInt var9) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var9;
      }

      public final Integer getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToInt var1;
         ConcurrentHashMapV8.IntByIntToInt var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.basis;
            int var4 = this.baseIndex;

            int var5;
            int var6;
            while(this.batch > 0 && (var6 = (var5 = this.baseLimit) + var4 >>> 1) > var4) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceKeysToIntTask(this, this.batch >>>= 1, this.baseLimit = var6, var5, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var7;
            while((var7 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var7.key));
            }

            this.result = var3;

            for(CountedCompleter var8 = this.firstComplete(); var8 != null; var8 = var8.nextComplete()) {
               ConcurrentHashMapV8.MapReduceKeysToIntTask var9 = (ConcurrentHashMapV8.MapReduceKeysToIntTask)var8;

               for(ConcurrentHashMapV8.MapReduceKeysToIntTask var10 = var9.rights; var10 != null; var10 = var9.rights = var10.nextRight) {
                  var9.result = var2.apply(var9.result, var10.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceMappingsToLongTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectByObjectToLong transformer;
      final ConcurrentHashMapV8.LongByLongToLong reducer;
      final long basis;
      long result;
      ConcurrentHashMapV8.MapReduceMappingsToLongTask rights;
      ConcurrentHashMapV8.MapReduceMappingsToLongTask nextRight;

      MapReduceMappingsToLongTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceMappingsToLongTask var6, ConcurrentHashMapV8.ObjectByObjectToLong var7, long var8, ConcurrentHashMapV8.LongByLongToLong var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Long getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectByObjectToLong var1;
         ConcurrentHashMapV8.LongByLongToLong var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            long var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceMappingsToLongTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.key, var8.val));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceMappingsToLongTask var10 = (ConcurrentHashMapV8.MapReduceMappingsToLongTask)var9;

               for(ConcurrentHashMapV8.MapReduceMappingsToLongTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceEntriesToLongTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToLong transformer;
      final ConcurrentHashMapV8.LongByLongToLong reducer;
      final long basis;
      long result;
      ConcurrentHashMapV8.MapReduceEntriesToLongTask rights;
      ConcurrentHashMapV8.MapReduceEntriesToLongTask nextRight;

      MapReduceEntriesToLongTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceEntriesToLongTask var6, ConcurrentHashMapV8.ObjectToLong var7, long var8, ConcurrentHashMapV8.LongByLongToLong var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Long getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToLong var1;
         ConcurrentHashMapV8.LongByLongToLong var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            long var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceEntriesToLongTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceEntriesToLongTask var10 = (ConcurrentHashMapV8.MapReduceEntriesToLongTask)var9;

               for(ConcurrentHashMapV8.MapReduceEntriesToLongTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceValuesToLongTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToLong transformer;
      final ConcurrentHashMapV8.LongByLongToLong reducer;
      final long basis;
      long result;
      ConcurrentHashMapV8.MapReduceValuesToLongTask rights;
      ConcurrentHashMapV8.MapReduceValuesToLongTask nextRight;

      MapReduceValuesToLongTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceValuesToLongTask var6, ConcurrentHashMapV8.ObjectToLong var7, long var8, ConcurrentHashMapV8.LongByLongToLong var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Long getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToLong var1;
         ConcurrentHashMapV8.LongByLongToLong var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            long var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceValuesToLongTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.val));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceValuesToLongTask var10 = (ConcurrentHashMapV8.MapReduceValuesToLongTask)var9;

               for(ConcurrentHashMapV8.MapReduceValuesToLongTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceKeysToLongTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToLong transformer;
      final ConcurrentHashMapV8.LongByLongToLong reducer;
      final long basis;
      long result;
      ConcurrentHashMapV8.MapReduceKeysToLongTask rights;
      ConcurrentHashMapV8.MapReduceKeysToLongTask nextRight;

      MapReduceKeysToLongTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceKeysToLongTask var6, ConcurrentHashMapV8.ObjectToLong var7, long var8, ConcurrentHashMapV8.LongByLongToLong var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Long getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToLong var1;
         ConcurrentHashMapV8.LongByLongToLong var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            long var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceKeysToLongTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.key));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceKeysToLongTask var10 = (ConcurrentHashMapV8.MapReduceKeysToLongTask)var9;

               for(ConcurrentHashMapV8.MapReduceKeysToLongTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceMappingsToDoubleTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectByObjectToDouble transformer;
      final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      final double basis;
      double result;
      ConcurrentHashMapV8.MapReduceMappingsToDoubleTask rights;
      ConcurrentHashMapV8.MapReduceMappingsToDoubleTask nextRight;

      MapReduceMappingsToDoubleTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceMappingsToDoubleTask var6, ConcurrentHashMapV8.ObjectByObjectToDouble var7, double var8, ConcurrentHashMapV8.DoubleByDoubleToDouble var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Double getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectByObjectToDouble var1;
         ConcurrentHashMapV8.DoubleByDoubleToDouble var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            double var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceMappingsToDoubleTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.key, var8.val));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceMappingsToDoubleTask var10 = (ConcurrentHashMapV8.MapReduceMappingsToDoubleTask)var9;

               for(ConcurrentHashMapV8.MapReduceMappingsToDoubleTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceEntriesToDoubleTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToDouble transformer;
      final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      final double basis;
      double result;
      ConcurrentHashMapV8.MapReduceEntriesToDoubleTask rights;
      ConcurrentHashMapV8.MapReduceEntriesToDoubleTask nextRight;

      MapReduceEntriesToDoubleTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceEntriesToDoubleTask var6, ConcurrentHashMapV8.ObjectToDouble var7, double var8, ConcurrentHashMapV8.DoubleByDoubleToDouble var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Double getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToDouble var1;
         ConcurrentHashMapV8.DoubleByDoubleToDouble var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            double var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceEntriesToDoubleTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceEntriesToDoubleTask var10 = (ConcurrentHashMapV8.MapReduceEntriesToDoubleTask)var9;

               for(ConcurrentHashMapV8.MapReduceEntriesToDoubleTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceValuesToDoubleTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToDouble transformer;
      final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      final double basis;
      double result;
      ConcurrentHashMapV8.MapReduceValuesToDoubleTask rights;
      ConcurrentHashMapV8.MapReduceValuesToDoubleTask nextRight;

      MapReduceValuesToDoubleTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceValuesToDoubleTask var6, ConcurrentHashMapV8.ObjectToDouble var7, double var8, ConcurrentHashMapV8.DoubleByDoubleToDouble var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Double getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToDouble var1;
         ConcurrentHashMapV8.DoubleByDoubleToDouble var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            double var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceValuesToDoubleTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.val));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceValuesToDoubleTask var10 = (ConcurrentHashMapV8.MapReduceValuesToDoubleTask)var9;

               for(ConcurrentHashMapV8.MapReduceValuesToDoubleTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceKeysToDoubleTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.ObjectToDouble transformer;
      final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      final double basis;
      double result;
      ConcurrentHashMapV8.MapReduceKeysToDoubleTask rights;
      ConcurrentHashMapV8.MapReduceKeysToDoubleTask nextRight;

      MapReduceKeysToDoubleTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceKeysToDoubleTask var6, ConcurrentHashMapV8.ObjectToDouble var7, double var8, ConcurrentHashMapV8.DoubleByDoubleToDouble var10) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.basis = var8;
         this.reducer = var10;
      }

      public final Double getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.ObjectToDouble var1;
         ConcurrentHashMapV8.DoubleByDoubleToDouble var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            double var3 = this.basis;
            int var5 = this.baseIndex;

            int var6;
            int var7;
            while(this.batch > 0 && (var7 = (var6 = this.baseLimit) + var5 >>> 1) > var5) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceKeysToDoubleTask(this, this.batch >>>= 1, this.baseLimit = var7, var6, this.tab, this.rights, var1, var3, var2)).fork();
            }

            ConcurrentHashMapV8.Node var8;
            while((var8 = this.advance()) != null) {
               var3 = var2.apply(var3, var1.apply(var8.key));
            }

            this.result = var3;

            for(CountedCompleter var9 = this.firstComplete(); var9 != null; var9 = var9.nextComplete()) {
               ConcurrentHashMapV8.MapReduceKeysToDoubleTask var10 = (ConcurrentHashMapV8.MapReduceKeysToDoubleTask)var9;

               for(ConcurrentHashMapV8.MapReduceKeysToDoubleTask var11 = var10.rights; var11 != null; var11 = var10.rights = var11.nextRight) {
                  var10.result = var2.apply(var10.result, var11.result);
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class MapReduceMappingsTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun transformer;
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.MapReduceMappingsTask rights;
      ConcurrentHashMapV8.MapReduceMappingsTask nextRight;

      MapReduceMappingsTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceMappingsTask var6, ConcurrentHashMapV8.BiFun var7, ConcurrentHashMapV8.BiFun var8) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.reducer = var8;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         ConcurrentHashMapV8.BiFun var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceMappingsTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, this.rights, var1, var2)).fork();
            }

            Object var9 = null;

            ConcurrentHashMapV8.Node var10;
            while((var10 = this.advance()) != null) {
               Object var12;
               if ((var12 = var1.apply(var10.key, var10.val)) != null) {
                  var9 = var9 == null ? var12 : var2.apply(var9, var12);
               }
            }

            this.result = var9;

            for(CountedCompleter var11 = this.firstComplete(); var11 != null; var11 = var11.nextComplete()) {
               ConcurrentHashMapV8.MapReduceMappingsTask var13 = (ConcurrentHashMapV8.MapReduceMappingsTask)var11;

               for(ConcurrentHashMapV8.MapReduceMappingsTask var6 = var13.rights; var6 != null; var6 = var13.rights = var6.nextRight) {
                  Object var8;
                  if ((var8 = var6.result) != null) {
                     Object var7;
                     var13.result = (var7 = var13.result) == null ? var8 : var2.apply(var7, var8);
                  }
               }
            }
         }

      }
   }

   static final class MapReduceEntriesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.MapReduceEntriesTask rights;
      ConcurrentHashMapV8.MapReduceEntriesTask nextRight;

      MapReduceEntriesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceEntriesTask var6, ConcurrentHashMapV8.Fun var7, ConcurrentHashMapV8.BiFun var8) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.reducer = var8;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.BiFun var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceEntriesTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, this.rights, var1, var2)).fork();
            }

            Object var9 = null;

            ConcurrentHashMapV8.Node var10;
            while((var10 = this.advance()) != null) {
               Object var12;
               if ((var12 = var1.apply(var10)) != null) {
                  var9 = var9 == null ? var12 : var2.apply(var9, var12);
               }
            }

            this.result = var9;

            for(CountedCompleter var11 = this.firstComplete(); var11 != null; var11 = var11.nextComplete()) {
               ConcurrentHashMapV8.MapReduceEntriesTask var13 = (ConcurrentHashMapV8.MapReduceEntriesTask)var11;

               for(ConcurrentHashMapV8.MapReduceEntriesTask var6 = var13.rights; var6 != null; var6 = var13.rights = var6.nextRight) {
                  Object var8;
                  if ((var8 = var6.result) != null) {
                     Object var7;
                     var13.result = (var7 = var13.result) == null ? var8 : var2.apply(var7, var8);
                  }
               }
            }
         }

      }
   }

   static final class MapReduceValuesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.MapReduceValuesTask rights;
      ConcurrentHashMapV8.MapReduceValuesTask nextRight;

      MapReduceValuesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceValuesTask var6, ConcurrentHashMapV8.Fun var7, ConcurrentHashMapV8.BiFun var8) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.reducer = var8;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.BiFun var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceValuesTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, this.rights, var1, var2)).fork();
            }

            Object var9 = null;

            ConcurrentHashMapV8.Node var10;
            while((var10 = this.advance()) != null) {
               Object var12;
               if ((var12 = var1.apply(var10.val)) != null) {
                  var9 = var9 == null ? var12 : var2.apply(var9, var12);
               }
            }

            this.result = var9;

            for(CountedCompleter var11 = this.firstComplete(); var11 != null; var11 = var11.nextComplete()) {
               ConcurrentHashMapV8.MapReduceValuesTask var13 = (ConcurrentHashMapV8.MapReduceValuesTask)var11;

               for(ConcurrentHashMapV8.MapReduceValuesTask var6 = var13.rights; var6 != null; var6 = var13.rights = var6.nextRight) {
                  Object var8;
                  if ((var8 = var6.result) != null) {
                     Object var7;
                     var13.result = (var7 = var13.result) == null ? var8 : var2.apply(var7, var8);
                  }
               }
            }
         }

      }
   }

   static final class MapReduceKeysTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.MapReduceKeysTask rights;
      ConcurrentHashMapV8.MapReduceKeysTask nextRight;

      MapReduceKeysTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.MapReduceKeysTask var6, ConcurrentHashMapV8.Fun var7, ConcurrentHashMapV8.BiFun var8) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.transformer = var7;
         this.reducer = var8;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.BiFun var2;
         if ((var1 = this.transformer) != null && (var2 = this.reducer) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.MapReduceKeysTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, this.rights, var1, var2)).fork();
            }

            Object var9 = null;

            ConcurrentHashMapV8.Node var10;
            while((var10 = this.advance()) != null) {
               Object var12;
               if ((var12 = var1.apply(var10.key)) != null) {
                  var9 = var9 == null ? var12 : var2.apply(var9, var12);
               }
            }

            this.result = var9;

            for(CountedCompleter var11 = this.firstComplete(); var11 != null; var11 = var11.nextComplete()) {
               ConcurrentHashMapV8.MapReduceKeysTask var13 = (ConcurrentHashMapV8.MapReduceKeysTask)var11;

               for(ConcurrentHashMapV8.MapReduceKeysTask var6 = var13.rights; var6 != null; var6 = var13.rights = var6.nextRight) {
                  Object var8;
                  if ((var8 = var6.result) != null) {
                     Object var7;
                     var13.result = (var7 = var13.result) == null ? var8 : var2.apply(var7, var8);
                  }
               }
            }
         }

      }
   }

   static final class ReduceEntriesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun reducer;
      Entry result;
      ConcurrentHashMapV8.ReduceEntriesTask rights;
      ConcurrentHashMapV8.ReduceEntriesTask nextRight;

      ReduceEntriesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.ReduceEntriesTask var6, ConcurrentHashMapV8.BiFun var7) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.reducer = var7;
      }

      public final Entry getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         if ((var1 = this.reducer) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.ReduceEntriesTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, this.rights, var1)).fork();
            }

            Object var8;
            ConcurrentHashMapV8.Node var9;
            for(var8 = null; (var9 = this.advance()) != null; var8 = var8 == null ? var9 : (Entry)var1.apply(var8, var9)) {
            }

            this.result = (Entry)var8;

            for(CountedCompleter var10 = this.firstComplete(); var10 != null; var10 = var10.nextComplete()) {
               ConcurrentHashMapV8.ReduceEntriesTask var11 = (ConcurrentHashMapV8.ReduceEntriesTask)var10;

               for(ConcurrentHashMapV8.ReduceEntriesTask var5 = var11.rights; var5 != null; var5 = var11.rights = var5.nextRight) {
                  Entry var7;
                  if ((var7 = var5.result) != null) {
                     Entry var6;
                     var11.result = (var6 = var11.result) == null ? var7 : (Entry)var1.apply(var6, var7);
                  }
               }
            }
         }

      }

      public Object getRawResult() {
         return this.getRawResult();
      }
   }

   static final class ReduceValuesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.ReduceValuesTask rights;
      ConcurrentHashMapV8.ReduceValuesTask nextRight;

      ReduceValuesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.ReduceValuesTask var6, ConcurrentHashMapV8.BiFun var7) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.reducer = var7;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         if ((var1 = this.reducer) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.ReduceValuesTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, this.rights, var1)).fork();
            }

            Object var8;
            ConcurrentHashMapV8.Node var9;
            Object var11;
            for(var8 = null; (var9 = this.advance()) != null; var8 = var8 == null ? var11 : var1.apply(var8, var11)) {
               var11 = var9.val;
            }

            this.result = var8;

            for(CountedCompleter var10 = this.firstComplete(); var10 != null; var10 = var10.nextComplete()) {
               ConcurrentHashMapV8.ReduceValuesTask var12 = (ConcurrentHashMapV8.ReduceValuesTask)var10;

               for(ConcurrentHashMapV8.ReduceValuesTask var5 = var12.rights; var5 != null; var5 = var12.rights = var5.nextRight) {
                  Object var7;
                  if ((var7 = var5.result) != null) {
                     Object var6;
                     var12.result = (var6 = var12.result) == null ? var7 : var1.apply(var6, var7);
                  }
               }
            }
         }

      }
   }

   static final class ReduceKeysTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun reducer;
      Object result;
      ConcurrentHashMapV8.ReduceKeysTask rights;
      ConcurrentHashMapV8.ReduceKeysTask nextRight;

      ReduceKeysTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.ReduceKeysTask var6, ConcurrentHashMapV8.BiFun var7) {
         super(var1, var2, var3, var4, var5);
         this.nextRight = var6;
         this.reducer = var7;
      }

      public final Object getRawResult() {
         return this.result;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         if ((var1 = this.reducer) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (this.rights = new ConcurrentHashMapV8.ReduceKeysTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, this.rights, var1)).fork();
            }

            Object var8;
            ConcurrentHashMapV8.Node var9;
            Object var11;
            for(var8 = null; (var9 = this.advance()) != null; var8 = var8 == null ? var11 : (var11 == null ? var8 : var1.apply(var8, var11))) {
               var11 = var9.key;
            }

            this.result = var8;

            for(CountedCompleter var10 = this.firstComplete(); var10 != null; var10 = var10.nextComplete()) {
               ConcurrentHashMapV8.ReduceKeysTask var12 = (ConcurrentHashMapV8.ReduceKeysTask)var10;

               for(ConcurrentHashMapV8.ReduceKeysTask var5 = var12.rights; var5 != null; var5 = var12.rights = var5.nextRight) {
                  Object var7;
                  if ((var7 = var5.result) != null) {
                     Object var6;
                     var12.result = (var6 = var12.result) == null ? var7 : var1.apply(var6, var7);
                  }
               }
            }
         }

      }
   }

   static final class SearchMappingsTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun searchFunction;
      final AtomicReference result;

      SearchMappingsTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.BiFun var6, AtomicReference var7) {
         super(var1, var2, var3, var4, var5);
         this.searchFunction = var6;
         this.result = var7;
      }

      public final Object getRawResult() {
         return this.result.get();
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         AtomicReference var2;
         if ((var1 = this.searchFunction) != null && (var2 = this.result) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               if (var2.get() != null) {
                  return;
               }

               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.SearchMappingsTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            while(var2.get() == null) {
               ConcurrentHashMapV8.Node var7;
               if ((var7 = this.advance()) == null) {
                  this.propagateCompletion();
                  break;
               }

               Object var6;
               if ((var6 = var1.apply(var7.key, var7.val)) != null) {
                  if (var2.compareAndSet((Object)null, var6)) {
                     this.quietlyCompleteRoot();
                  }
                  break;
               }
            }
         }

      }
   }

   static final class SearchEntriesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun searchFunction;
      final AtomicReference result;

      SearchEntriesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, AtomicReference var7) {
         super(var1, var2, var3, var4, var5);
         this.searchFunction = var6;
         this.result = var7;
      }

      public final Object getRawResult() {
         return this.result.get();
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         AtomicReference var2;
         if ((var1 = this.searchFunction) != null && (var2 = this.result) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               if (var2.get() != null) {
                  return;
               }

               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.SearchEntriesTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            while(var2.get() == null) {
               ConcurrentHashMapV8.Node var7;
               if ((var7 = this.advance()) == null) {
                  this.propagateCompletion();
                  break;
               }

               Object var6;
               if ((var6 = var1.apply(var7)) != null) {
                  if (var2.compareAndSet((Object)null, var6)) {
                     this.quietlyCompleteRoot();
                  }

                  return;
               }
            }
         }

      }
   }

   static final class SearchValuesTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun searchFunction;
      final AtomicReference result;

      SearchValuesTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, AtomicReference var7) {
         super(var1, var2, var3, var4, var5);
         this.searchFunction = var6;
         this.result = var7;
      }

      public final Object getRawResult() {
         return this.result.get();
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         AtomicReference var2;
         if ((var1 = this.searchFunction) != null && (var2 = this.result) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               if (var2.get() != null) {
                  return;
               }

               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.SearchValuesTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            while(var2.get() == null) {
               ConcurrentHashMapV8.Node var7;
               if ((var7 = this.advance()) == null) {
                  this.propagateCompletion();
                  break;
               }

               Object var6;
               if ((var6 = var1.apply(var7.val)) != null) {
                  if (var2.compareAndSet((Object)null, var6)) {
                     this.quietlyCompleteRoot();
                  }
                  break;
               }
            }
         }

      }
   }

   static final class SearchKeysTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun searchFunction;
      final AtomicReference result;

      SearchKeysTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, AtomicReference var7) {
         super(var1, var2, var3, var4, var5);
         this.searchFunction = var6;
         this.result = var7;
      }

      public final Object getRawResult() {
         return this.result.get();
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         AtomicReference var2;
         if ((var1 = this.searchFunction) != null && (var2 = this.result) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               if (var2.get() != null) {
                  return;
               }

               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.SearchKeysTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            while(var2.get() == null) {
               ConcurrentHashMapV8.Node var7;
               if ((var7 = this.advance()) == null) {
                  this.propagateCompletion();
                  break;
               }

               Object var6;
               if ((var6 = var1.apply(var7.key)) != null) {
                  if (var2.compareAndSet((Object)null, var6)) {
                     this.quietlyCompleteRoot();
                  }
                  break;
               }
            }
         }

      }
   }

   static final class ForEachTransformedMappingTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiFun transformer;
      final ConcurrentHashMapV8.Action action;

      ForEachTransformedMappingTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.BiFun var6, ConcurrentHashMapV8.Action var7) {
         super(var1, var2, var3, var4, var5);
         this.transformer = var6;
         this.action = var7;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiFun var1;
         ConcurrentHashMapV8.Action var2;
         if ((var1 = this.transformer) != null && (var2 = this.action) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachTransformedMappingTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            ConcurrentHashMapV8.Node var6;
            while((var6 = this.advance()) != null) {
               Object var7;
               if ((var7 = var1.apply(var6.key, var6.val)) != null) {
                  var2.apply(var7);
               }
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachTransformedEntryTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.Action action;

      ForEachTransformedEntryTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, ConcurrentHashMapV8.Action var7) {
         super(var1, var2, var3, var4, var5);
         this.transformer = var6;
         this.action = var7;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.Action var2;
         if ((var1 = this.transformer) != null && (var2 = this.action) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachTransformedEntryTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            ConcurrentHashMapV8.Node var6;
            while((var6 = this.advance()) != null) {
               Object var7;
               if ((var7 = var1.apply(var6)) != null) {
                  var2.apply(var7);
               }
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachTransformedValueTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.Action action;

      ForEachTransformedValueTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, ConcurrentHashMapV8.Action var7) {
         super(var1, var2, var3, var4, var5);
         this.transformer = var6;
         this.action = var7;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.Action var2;
         if ((var1 = this.transformer) != null && (var2 = this.action) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachTransformedValueTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            ConcurrentHashMapV8.Node var6;
            while((var6 = this.advance()) != null) {
               Object var7;
               if ((var7 = var1.apply(var6.val)) != null) {
                  var2.apply(var7);
               }
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachTransformedKeyTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Fun transformer;
      final ConcurrentHashMapV8.Action action;

      ForEachTransformedKeyTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Fun var6, ConcurrentHashMapV8.Action var7) {
         super(var1, var2, var3, var4, var5);
         this.transformer = var6;
         this.action = var7;
      }

      public final void compute() {
         ConcurrentHashMapV8.Fun var1;
         ConcurrentHashMapV8.Action var2;
         if ((var1 = this.transformer) != null && (var2 = this.action) != null) {
            int var3 = this.baseIndex;

            int var4;
            int var5;
            while(this.batch > 0 && (var5 = (var4 = this.baseLimit) + var3 >>> 1) > var3) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachTransformedKeyTask(this, this.batch >>>= 1, this.baseLimit = var5, var4, this.tab, var1, var2)).fork();
            }

            ConcurrentHashMapV8.Node var6;
            while((var6 = this.advance()) != null) {
               Object var7;
               if ((var7 = var1.apply(var6.key)) != null) {
                  var2.apply(var7);
               }
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachMappingTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.BiAction action;

      ForEachMappingTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.BiAction var6) {
         super(var1, var2, var3, var4, var5);
         this.action = var6;
      }

      public final void compute() {
         ConcurrentHashMapV8.BiAction var1;
         if ((var1 = this.action) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachMappingTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, var1)).fork();
            }

            ConcurrentHashMapV8.Node var5;
            while((var5 = this.advance()) != null) {
               var1.apply(var5.key, var5.val);
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachEntryTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Action action;

      ForEachEntryTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Action var6) {
         super(var1, var2, var3, var4, var5);
         this.action = var6;
      }

      public final void compute() {
         ConcurrentHashMapV8.Action var1;
         if ((var1 = this.action) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachEntryTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, var1)).fork();
            }

            ConcurrentHashMapV8.Node var5;
            while((var5 = this.advance()) != null) {
               var1.apply(var5);
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachValueTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Action action;

      ForEachValueTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Action var6) {
         super(var1, var2, var3, var4, var5);
         this.action = var6;
      }

      public final void compute() {
         ConcurrentHashMapV8.Action var1;
         if ((var1 = this.action) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachValueTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, var1)).fork();
            }

            ConcurrentHashMapV8.Node var5;
            while((var5 = this.advance()) != null) {
               var1.apply(var5.val);
            }

            this.propagateCompletion();
         }

      }
   }

   static final class ForEachKeyTask extends ConcurrentHashMapV8.BulkTask {
      final ConcurrentHashMapV8.Action action;

      ForEachKeyTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5, ConcurrentHashMapV8.Action var6) {
         super(var1, var2, var3, var4, var5);
         this.action = var6;
      }

      public final void compute() {
         ConcurrentHashMapV8.Action var1;
         if ((var1 = this.action) != null) {
            int var2 = this.baseIndex;

            int var3;
            int var4;
            while(this.batch > 0 && (var4 = (var3 = this.baseLimit) + var2 >>> 1) > var2) {
               this.addToPendingCount(1);
               (new ConcurrentHashMapV8.ForEachKeyTask(this, this.batch >>>= 1, this.baseLimit = var4, var3, this.tab, var1)).fork();
            }

            ConcurrentHashMapV8.Node var5;
            while((var5 = this.advance()) != null) {
               var1.apply(var5.key);
            }

            this.propagateCompletion();
         }

      }
   }

   abstract static class BulkTask extends CountedCompleter {
      ConcurrentHashMapV8.Node[] tab;
      ConcurrentHashMapV8.Node next;
      int index;
      int baseIndex;
      int baseLimit;
      final int baseSize;
      int batch;

      BulkTask(ConcurrentHashMapV8.BulkTask var1, int var2, int var3, int var4, ConcurrentHashMapV8.Node[] var5) {
         super(var1);
         this.batch = var2;
         this.index = this.baseIndex = var3;
         if ((this.tab = var5) == null) {
            this.baseSize = this.baseLimit = 0;
         } else if (var1 == null) {
            this.baseSize = this.baseLimit = var5.length;
         } else {
            this.baseLimit = var4;
            this.baseSize = var1.baseSize;
         }

      }

      final ConcurrentHashMapV8.Node advance() {
         Object var1;
         if ((var1 = this.next) != null) {
            var1 = ((ConcurrentHashMapV8.Node)var1).next;
         }

         while(var1 == null) {
            ConcurrentHashMapV8.Node[] var2;
            int var3;
            int var4;
            if (this.baseIndex >= this.baseLimit || (var2 = this.tab) == null || (var4 = var2.length) <= (var3 = this.index) || var3 < 0) {
               return this.next = null;
            }

            if ((var1 = ConcurrentHashMapV8.tabAt(var2, this.index)) != null && ((ConcurrentHashMapV8.Node)var1).hash < 0) {
               if (var1 instanceof ConcurrentHashMapV8.ForwardingNode) {
                  this.tab = ((ConcurrentHashMapV8.ForwardingNode)var1).nextTable;
                  var1 = null;
                  continue;
               }

               if (var1 instanceof ConcurrentHashMapV8.TreeBin) {
                  var1 = ((ConcurrentHashMapV8.TreeBin)var1).first;
               } else {
                  var1 = null;
               }
            }

            if ((this.index += this.baseSize) >= var4) {
               this.index = ++this.baseIndex;
            }
         }

         return this.next = (ConcurrentHashMapV8.Node)var1;
      }
   }

   static final class EntrySetView extends ConcurrentHashMapV8.CollectionView implements Set, Serializable {
      private static final long serialVersionUID = 2249069246763182397L;

      EntrySetView(ConcurrentHashMapV8 var1) {
         super(var1);
      }

      public boolean contains(Object var1) {
         Object var2;
         Object var3;
         Object var4;
         Entry var5;
         return var1 instanceof Entry && (var2 = (var5 = (Entry)var1).getKey()) != null && (var4 = this.map.get(var2)) != null && (var3 = var5.getValue()) != null && (var3 == var4 || var3.equals(var4));
      }

      public boolean remove(Object var1) {
         Object var2;
         Object var3;
         Entry var4;
         return var1 instanceof Entry && (var2 = (var4 = (Entry)var1).getKey()) != null && (var3 = var4.getValue()) != null && this.map.remove(var2, var3);
      }

      public Iterator iterator() {
         ConcurrentHashMapV8 var1 = this.map;
         ConcurrentHashMapV8.Node[] var2;
         int var3 = (var2 = var1.table) == null ? 0 : var2.length;
         return new ConcurrentHashMapV8.EntryIterator(var2, var3, 0, var3, var1);
      }

      public boolean addAll(Collection var1) {
         boolean var2 = false;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4 == null) {
               var2 = true;
            }
         }

         return var2;
      }

      public final int hashCode() {
         int var1 = 0;
         ConcurrentHashMapV8.Node[] var2;
         ConcurrentHashMapV8.Node var4;
         if ((var2 = this.map.table) != null) {
            for(ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length); (var4 = var3.advance()) != null; var1 += var4.hashCode()) {
            }
         }

         return var1;
      }

      public final boolean equals(Object var1) {
         Set var2;
         return var1 instanceof Set && ((var2 = (Set)var1) == this || this.containsAll(var2) && var2.containsAll(this));
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator spliterator() {
         ConcurrentHashMapV8 var2 = this.map;
         long var3 = var2.sumCount();
         ConcurrentHashMapV8.Node[] var1;
         int var5 = (var1 = var2.table) == null ? 0 : var1.length;
         return new ConcurrentHashMapV8.EntrySpliterator(var1, var5, 0, var5, var3 < 0L ? 0L : var3, var2);
      }

      public void forEach(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node[] var2;
            if ((var2 = this.map.table) != null) {
               ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

               ConcurrentHashMapV8.Node var4;
               while((var4 = var3.advance()) != null) {
                  var1.apply(new ConcurrentHashMapV8.MapEntry(var4.key, var4.val, this.map));
               }
            }

         }
      }

      public boolean add(Object var1) {
         return this.add((Entry)var1);
      }
   }

   static final class ValuesView extends ConcurrentHashMapV8.CollectionView implements Collection, Serializable {
      private static final long serialVersionUID = 2249069246763182397L;

      ValuesView(ConcurrentHashMapV8 var1) {
         super(var1);
      }

      public final boolean contains(Object var1) {
         return this.map.containsValue(var1);
      }

      public final boolean remove(Object var1) {
         if (var1 != null) {
            Iterator var2 = this.iterator();

            while(var2.hasNext()) {
               if (var1.equals(var2.next())) {
                  var2.remove();
                  return true;
               }
            }
         }

         return false;
      }

      public final Iterator iterator() {
         ConcurrentHashMapV8 var1 = this.map;
         ConcurrentHashMapV8.Node[] var2;
         int var3 = (var2 = var1.table) == null ? 0 : var2.length;
         return new ConcurrentHashMapV8.ValueIterator(var2, var3, 0, var3, var1);
      }

      public final boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public final boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator spliterator() {
         ConcurrentHashMapV8 var2 = this.map;
         long var3 = var2.sumCount();
         ConcurrentHashMapV8.Node[] var1;
         int var5 = (var1 = var2.table) == null ? 0 : var1.length;
         return new ConcurrentHashMapV8.ValueSpliterator(var1, var5, 0, var5, var3 < 0L ? 0L : var3);
      }

      public void forEach(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node[] var2;
            if ((var2 = this.map.table) != null) {
               ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

               ConcurrentHashMapV8.Node var4;
               while((var4 = var3.advance()) != null) {
                  var1.apply(var4.val);
               }
            }

         }
      }
   }

   public static class KeySetView extends ConcurrentHashMapV8.CollectionView implements Set, Serializable {
      private static final long serialVersionUID = 7249069246763182397L;
      private final Object value;

      KeySetView(ConcurrentHashMapV8 var1, Object var2) {
         super(var1);
         this.value = var2;
      }

      public Object getMappedValue() {
         return this.value;
      }

      public boolean contains(Object var1) {
         return this.map.containsKey(var1);
      }

      public boolean remove(Object var1) {
         return this.map.remove(var1) != null;
      }

      public Iterator iterator() {
         ConcurrentHashMapV8 var2 = this.map;
         ConcurrentHashMapV8.Node[] var1;
         int var3 = (var1 = var2.table) == null ? 0 : var1.length;
         return new ConcurrentHashMapV8.KeyIterator(var1, var3, 0, var3, var2);
      }

      public boolean add(Object var1) {
         Object var2;
         if ((var2 = this.value) == null) {
            throw new UnsupportedOperationException();
         } else {
            return this.map.putVal(var1, var2, true) == null;
         }
      }

      public boolean addAll(Collection var1) {
         boolean var2 = false;
         Object var3;
         if ((var3 = this.value) == null) {
            throw new UnsupportedOperationException();
         } else {
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (this.map.putVal(var5, var3, true) == null) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public int hashCode() {
         int var1 = 0;

         Object var3;
         for(Iterator var2 = this.iterator(); var2.hasNext(); var1 += var3.hashCode()) {
            var3 = var2.next();
         }

         return var1;
      }

      public boolean equals(Object var1) {
         Set var2;
         return var1 instanceof Set && ((var2 = (Set)var1) == this || this.containsAll(var2) && var2.containsAll(this));
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator spliterator() {
         ConcurrentHashMapV8 var2 = this.map;
         long var3 = var2.sumCount();
         ConcurrentHashMapV8.Node[] var1;
         int var5 = (var1 = var2.table) == null ? 0 : var1.length;
         return new ConcurrentHashMapV8.KeySpliterator(var1, var5, 0, var5, var3 < 0L ? 0L : var3);
      }

      public void forEach(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node[] var2;
            if ((var2 = this.map.table) != null) {
               ConcurrentHashMapV8.Traverser var3 = new ConcurrentHashMapV8.Traverser(var2, var2.length, 0, var2.length);

               ConcurrentHashMapV8.Node var4;
               while((var4 = var3.advance()) != null) {
                  var1.apply(var4.key);
               }
            }

         }
      }

      public ConcurrentHashMapV8 getMap() {
         return super.getMap();
      }
   }

   abstract static class CollectionView implements Collection, Serializable {
      private static final long serialVersionUID = 7249069246763182397L;
      final ConcurrentHashMapV8 map;
      private static final String oomeMsg = "Required array size too large";

      CollectionView(ConcurrentHashMapV8 var1) {
         this.map = var1;
      }

      public ConcurrentHashMapV8 getMap() {
         return this.map;
      }

      public final void clear() {
         this.map.clear();
      }

      public final int size() {
         return this.map.size();
      }

      public final boolean isEmpty() {
         return this.map.isEmpty();
      }

      public abstract Iterator iterator();

      public abstract boolean contains(Object var1);

      public abstract boolean remove(Object var1);

      public final Object[] toArray() {
         long var1 = this.map.mappingCount();
         if (var1 > 2147483639L) {
            throw new OutOfMemoryError("Required array size too large");
         } else {
            int var3 = (int)var1;
            Object[] var4 = new Object[var3];
            int var5 = 0;

            Object var7;
            for(Iterator var6 = this.iterator(); var6.hasNext(); var4[var5++] = var7) {
               var7 = var6.next();
               if (var5 == var3) {
                  if (var3 >= 2147483639) {
                     throw new OutOfMemoryError("Required array size too large");
                  }

                  if (var3 >= 1073741819) {
                     var3 = 2147483639;
                  } else {
                     var3 += (var3 >>> 1) + 1;
                  }

                  var4 = Arrays.copyOf(var4, var3);
               }
            }

            return var5 == var3 ? var4 : Arrays.copyOf(var4, var5);
         }
      }

      public final Object[] toArray(Object[] var1) {
         long var2 = this.map.mappingCount();
         if (var2 > 2147483639L) {
            throw new OutOfMemoryError("Required array size too large");
         } else {
            int var4 = (int)var2;
            Object[] var5 = var1.length >= var4 ? var1 : (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var4));
            int var6 = var5.length;
            int var7 = 0;

            Object var9;
            for(Iterator var8 = this.iterator(); var8.hasNext(); var5[var7++] = var9) {
               var9 = var8.next();
               if (var7 == var6) {
                  if (var6 >= 2147483639) {
                     throw new OutOfMemoryError("Required array size too large");
                  }

                  if (var6 >= 1073741819) {
                     var6 = 2147483639;
                  } else {
                     var6 += (var6 >>> 1) + 1;
                  }

                  var5 = Arrays.copyOf(var5, var6);
               }
            }

            if (var1 == var5 && var7 < var6) {
               var5[var7] = null;
               return var5;
            } else {
               return var7 == var6 ? var5 : Arrays.copyOf(var5, var7);
            }
         }
      }

      public final String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append('[');
         Iterator var2 = this.iterator();
         if (var2.hasNext()) {
            while(true) {
               Object var3 = var2.next();
               var1.append(var3 == this ? "(this Collection)" : var3);
               if (!var2.hasNext()) {
                  break;
               }

               var1.append(',').append(' ');
            }
         }

         return var1.append(']').toString();
      }

      public final boolean containsAll(Collection var1) {
         if (var1 != this) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               if (var3 == null || !this.contains(var3)) {
                  return false;
               }
            }
         }

         return true;
      }

      public final boolean removeAll(Collection var1) {
         boolean var2 = false;
         Iterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public final boolean retainAll(Collection var1) {
         boolean var2 = false;
         Iterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }
   }

   static final class EntrySpliterator extends ConcurrentHashMapV8.Traverser implements ConcurrentHashMapV8.ConcurrentHashMapSpliterator {
      final ConcurrentHashMapV8 map;
      long est;

      EntrySpliterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, long var5, ConcurrentHashMapV8 var7) {
         super(var1, var2, var3, var4);
         this.map = var7;
         this.est = var5;
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator trySplit() {
         int var1;
         int var2;
         int var3;
         return (var3 = (var1 = this.baseIndex) + (var2 = this.baseLimit) >>> 1) <= var1 ? null : new ConcurrentHashMapV8.EntrySpliterator(this.tab, this.baseSize, this.baseLimit = var3, var2, this.est >>>= 1, this.map);
      }

      public void forEachRemaining(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            while((var2 = this.advance()) != null) {
               var1.apply(new ConcurrentHashMapV8.MapEntry(var2.key, var2.val, this.map));
            }

         }
      }

      public boolean tryAdvance(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            if ((var2 = this.advance()) == null) {
               return false;
            } else {
               var1.apply(new ConcurrentHashMapV8.MapEntry(var2.key, var2.val, this.map));
               return true;
            }
         }
      }

      public long estimateSize() {
         return this.est;
      }
   }

   static final class ValueSpliterator extends ConcurrentHashMapV8.Traverser implements ConcurrentHashMapV8.ConcurrentHashMapSpliterator {
      long est;

      ValueSpliterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, long var5) {
         super(var1, var2, var3, var4);
         this.est = var5;
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator trySplit() {
         int var1;
         int var2;
         int var3;
         return (var3 = (var1 = this.baseIndex) + (var2 = this.baseLimit) >>> 1) <= var1 ? null : new ConcurrentHashMapV8.ValueSpliterator(this.tab, this.baseSize, this.baseLimit = var3, var2, this.est >>>= 1);
      }

      public void forEachRemaining(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            while((var2 = this.advance()) != null) {
               var1.apply(var2.val);
            }

         }
      }

      public boolean tryAdvance(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            if ((var2 = this.advance()) == null) {
               return false;
            } else {
               var1.apply(var2.val);
               return true;
            }
         }
      }

      public long estimateSize() {
         return this.est;
      }
   }

   static final class KeySpliterator extends ConcurrentHashMapV8.Traverser implements ConcurrentHashMapV8.ConcurrentHashMapSpliterator {
      long est;

      KeySpliterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, long var5) {
         super(var1, var2, var3, var4);
         this.est = var5;
      }

      public ConcurrentHashMapV8.ConcurrentHashMapSpliterator trySplit() {
         int var1;
         int var2;
         int var3;
         return (var3 = (var1 = this.baseIndex) + (var2 = this.baseLimit) >>> 1) <= var1 ? null : new ConcurrentHashMapV8.KeySpliterator(this.tab, this.baseSize, this.baseLimit = var3, var2, this.est >>>= 1);
      }

      public void forEachRemaining(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            while((var2 = this.advance()) != null) {
               var1.apply(var2.key);
            }

         }
      }

      public boolean tryAdvance(ConcurrentHashMapV8.Action var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            ConcurrentHashMapV8.Node var2;
            if ((var2 = this.advance()) == null) {
               return false;
            } else {
               var1.apply(var2.key);
               return true;
            }
         }
      }

      public long estimateSize() {
         return this.est;
      }
   }

   static final class MapEntry implements Entry {
      final Object key;
      Object val;
      final ConcurrentHashMapV8 map;

      MapEntry(Object var1, Object var2, ConcurrentHashMapV8 var3) {
         this.key = var1;
         this.val = var2;
         this.map = var3;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.val;
      }

      public int hashCode() {
         return this.key.hashCode() ^ this.val.hashCode();
      }

      public String toString() {
         return this.key + "=" + this.val;
      }

      public boolean equals(Object var1) {
         Object var2;
         Object var3;
         Entry var4;
         return var1 instanceof Entry && (var2 = (var4 = (Entry)var1).getKey()) != null && (var3 = var4.getValue()) != null && (var2 == this.key || var2.equals(this.key)) && (var3 == this.val || var3.equals(this.val));
      }

      public Object setValue(Object var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            Object var2 = this.val;
            this.val = var1;
            this.map.put(this.key, var1);
            return var2;
         }
      }
   }

   static final class EntryIterator extends ConcurrentHashMapV8.BaseIterator implements Iterator {
      EntryIterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, ConcurrentHashMapV8 var5) {
         super(var1, var2, var3, var4, var5);
      }

      public final Entry next() {
         ConcurrentHashMapV8.Node var1;
         if ((var1 = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            Object var2 = var1.key;
            Object var3 = var1.val;
            this.lastReturned = var1;
            this.advance();
            return new ConcurrentHashMapV8.MapEntry(var2, var3, this.map);
         }
      }

      public Object next() {
         return this.next();
      }
   }

   static final class ValueIterator extends ConcurrentHashMapV8.BaseIterator implements Iterator, Enumeration {
      ValueIterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, ConcurrentHashMapV8 var5) {
         super(var1, var2, var3, var4, var5);
      }

      public final Object next() {
         ConcurrentHashMapV8.Node var1;
         if ((var1 = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            Object var2 = var1.val;
            this.lastReturned = var1;
            this.advance();
            return var2;
         }
      }

      public final Object nextElement() {
         return this.next();
      }
   }

   static final class KeyIterator extends ConcurrentHashMapV8.BaseIterator implements Iterator, Enumeration {
      KeyIterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, ConcurrentHashMapV8 var5) {
         super(var1, var2, var3, var4, var5);
      }

      public final Object next() {
         ConcurrentHashMapV8.Node var1;
         if ((var1 = this.next) == null) {
            throw new NoSuchElementException();
         } else {
            Object var2 = var1.key;
            this.lastReturned = var1;
            this.advance();
            return var2;
         }
      }

      public final Object nextElement() {
         return this.next();
      }
   }

   static class BaseIterator extends ConcurrentHashMapV8.Traverser {
      final ConcurrentHashMapV8 map;
      ConcurrentHashMapV8.Node lastReturned;

      BaseIterator(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4, ConcurrentHashMapV8 var5) {
         super(var1, var2, var3, var4);
         this.map = var5;
         this.advance();
      }

      public final boolean hasNext() {
         return this.next != null;
      }

      public final boolean hasMoreElements() {
         return this.next != null;
      }

      public final void remove() {
         ConcurrentHashMapV8.Node var1;
         if ((var1 = this.lastReturned) == null) {
            throw new IllegalStateException();
         } else {
            this.lastReturned = null;
            this.map.replaceNode(var1.key, (Object)null, (Object)null);
         }
      }
   }

   static class Traverser {
      ConcurrentHashMapV8.Node[] tab;
      ConcurrentHashMapV8.Node next;
      int index;
      int baseIndex;
      int baseLimit;
      final int baseSize;

      Traverser(ConcurrentHashMapV8.Node[] var1, int var2, int var3, int var4) {
         this.tab = var1;
         this.baseSize = var2;
         this.baseIndex = this.index = var3;
         this.baseLimit = var4;
         this.next = null;
      }

      final ConcurrentHashMapV8.Node advance() {
         Object var1;
         if ((var1 = this.next) != null) {
            var1 = ((ConcurrentHashMapV8.Node)var1).next;
         }

         while(var1 == null) {
            ConcurrentHashMapV8.Node[] var2;
            int var3;
            int var4;
            if (this.baseIndex >= this.baseLimit || (var2 = this.tab) == null || (var4 = var2.length) <= (var3 = this.index) || var3 < 0) {
               return this.next = null;
            }

            if ((var1 = ConcurrentHashMapV8.tabAt(var2, this.index)) != null && ((ConcurrentHashMapV8.Node)var1).hash < 0) {
               if (var1 instanceof ConcurrentHashMapV8.ForwardingNode) {
                  this.tab = ((ConcurrentHashMapV8.ForwardingNode)var1).nextTable;
                  var1 = null;
                  continue;
               }

               if (var1 instanceof ConcurrentHashMapV8.TreeBin) {
                  var1 = ((ConcurrentHashMapV8.TreeBin)var1).first;
               } else {
                  var1 = null;
               }
            }

            if ((this.index += this.baseSize) >= var4) {
               this.index = ++this.baseIndex;
            }
         }

         return this.next = (ConcurrentHashMapV8.Node)var1;
      }
   }

   static final class TreeBin extends ConcurrentHashMapV8.Node {
      ConcurrentHashMapV8.TreeNode root;
      volatile ConcurrentHashMapV8.TreeNode first;
      volatile Thread waiter;
      volatile int lockState;
      static final int WRITER = 1;
      static final int WAITER = 2;
      static final int READER = 4;
      private static final Unsafe U;
      private static final long LOCKSTATE;
      static final boolean $assertionsDisabled = !ConcurrentHashMapV8.class.desiredAssertionStatus();

      TreeBin(ConcurrentHashMapV8.TreeNode var1) {
         super(-2, (Object)null, (Object)null, (ConcurrentHashMapV8.Node)null);
         this.first = var1;
         ConcurrentHashMapV8.TreeNode var2 = null;

         ConcurrentHashMapV8.TreeNode var4;
         for(ConcurrentHashMapV8.TreeNode var3 = var1; var3 != null; var3 = var4) {
            var4 = (ConcurrentHashMapV8.TreeNode)var3.next;
            var3.left = var3.right = null;
            if (var2 == null) {
               var3.parent = null;
               var3.red = false;
               var2 = var3;
            } else {
               Object var5 = var3.key;
               int var6 = var3.hash;
               Class var7 = null;
               ConcurrentHashMapV8.TreeNode var8 = var2;

               int var9;
               ConcurrentHashMapV8.TreeNode var11;
               do {
                  int var10;
                  if ((var10 = var8.hash) > var6) {
                     var9 = -1;
                  } else if (var10 < var6) {
                     var9 = 1;
                  } else if (var7 == null && (var7 = ConcurrentHashMapV8.comparableClassFor(var5)) == null) {
                     var9 = 0;
                  } else {
                     var9 = ConcurrentHashMapV8.compareComparables(var7, var5, var8.key);
                  }

                  var11 = var8;
               } while((var8 = var9 <= 0 ? var8.left : var8.right) != null);

               var3.parent = var11;
               if (var9 <= 0) {
                  var11.left = var3;
               } else {
                  var11.right = var3;
               }

               var2 = balanceInsertion(var2, var3);
            }
         }

         this.root = var2;
      }

      private final void lockRoot() {
         if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
            this.contendedLock();
         }

      }

      private final void unlockRoot() {
         this.lockState = 0;
      }

      private final void contendedLock() {
         boolean var1 = false;

         int var2;
         do {
            while(((var2 = this.lockState) & 1) != 0) {
               if ((var2 | 2) == 0) {
                  if (U.compareAndSwapInt(this, LOCKSTATE, var2, var2 | 2)) {
                     var1 = true;
                     this.waiter = Thread.currentThread();
                  }
               } else if (var1) {
                  LockSupport.park(this);
               }
            }
         } while(!U.compareAndSwapInt(this, LOCKSTATE, var2, 1));

         if (var1) {
            this.waiter = null;
         }

      }

      final ConcurrentHashMapV8.Node find(int var1, Object var2) {
         if (var2 != null) {
            for(Object var3 = this.first; var3 != null; var3 = ((ConcurrentHashMapV8.Node)var3).next) {
               int var4;
               if (((var4 = this.lockState) & 3) != 0) {
                  Object var5;
                  if (((ConcurrentHashMapV8.Node)var3).hash == var1 && ((var5 = ((ConcurrentHashMapV8.Node)var3).key) == var2 || var5 != null && var2.equals(var5))) {
                     return (ConcurrentHashMapV8.Node)var3;
                  }
               } else if (U.compareAndSwapInt(this, LOCKSTATE, var4, var4 + 4)) {
                  ConcurrentHashMapV8.TreeNode var6;
                  ConcurrentHashMapV8.TreeNode var7 = (var6 = this.root) == null ? null : var6.findTreeNode(var1, var2, (Class)null);

                  int var9;
                  while(!U.compareAndSwapInt(this, LOCKSTATE, var9 = this.lockState, var9 - 4)) {
                  }

                  Thread var8;
                  if (var9 == 6 && (var8 = this.waiter) != null) {
                     LockSupport.unpark(var8);
                  }

                  return var7;
               }
            }
         }

         return null;
      }

      final ConcurrentHashMapV8.TreeNode putTreeVal(int var1, Object var2, Object var3) {
         Class var4 = null;
         ConcurrentHashMapV8.TreeNode var5 = this.root;

         while(true) {
            if (var5 == null) {
               this.first = this.root = new ConcurrentHashMapV8.TreeNode(var1, var2, var3, (ConcurrentHashMapV8.Node)null, (ConcurrentHashMapV8.TreeNode)null);
            } else {
               int var6;
               int var7;
               if ((var7 = var5.hash) > var1) {
                  var6 = -1;
               } else if (var7 < var1) {
                  var6 = 1;
               } else {
                  Object var8;
                  if ((var8 = var5.key) == var2 || var8 != null && var2.equals(var8)) {
                     return var5;
                  }

                  if (var4 == null && (var4 = ConcurrentHashMapV8.comparableClassFor(var2)) == null || (var6 = ConcurrentHashMapV8.compareComparables(var4, var2, var8)) == 0) {
                     if (var5.left == null) {
                        var6 = 1;
                     } else {
                        ConcurrentHashMapV8.TreeNode var9;
                        ConcurrentHashMapV8.TreeNode var10;
                        if ((var10 = var5.right) != null && (var9 = var10.findTreeNode(var1, var2, var4)) != null) {
                           return var9;
                        }

                        var6 = -1;
                     }
                  }
               }

               ConcurrentHashMapV8.TreeNode var11 = var5;
               if ((var5 = var6 < 0 ? var5.left : var5.right) != null) {
                  continue;
               }

               ConcurrentHashMapV8.TreeNode var13 = this.first;
               ConcurrentHashMapV8.TreeNode var12;
               this.first = var12 = new ConcurrentHashMapV8.TreeNode(var1, var2, var3, var13, var11);
               if (var13 != null) {
                  var13.prev = var12;
               }

               if (var6 < 0) {
                  var11.left = var12;
               } else {
                  var11.right = var12;
               }

               if (!var11.red) {
                  var12.red = true;
               } else {
                  this.lockRoot();
                  this.root = balanceInsertion(this.root, var12);
                  this.unlockRoot();
               }
            }

            if (!$assertionsDisabled && this.root != null) {
               throw new AssertionError();
            }

            return null;
         }
      }

      final boolean removeTreeNode(ConcurrentHashMapV8.TreeNode var1) {
         ConcurrentHashMapV8.TreeNode var2 = (ConcurrentHashMapV8.TreeNode)var1.next;
         ConcurrentHashMapV8.TreeNode var3 = var1.prev;
         if (var3 == null) {
            this.first = var2;
         } else {
            var3.next = var2;
         }

         if (var2 != null) {
            var2.prev = var3;
         }

         if (this.first == null) {
            this.root = null;
            return true;
         } else {
            ConcurrentHashMapV8.TreeNode var4;
            ConcurrentHashMapV8.TreeNode var5;
            if ((var4 = this.root) != null && var4.right != null && (var5 = var4.left) != null && var5.left != null) {
               this.lockRoot();
               ConcurrentHashMapV8.TreeNode var7 = var1.left;
               ConcurrentHashMapV8.TreeNode var8 = var1.right;
               ConcurrentHashMapV8.TreeNode var6;
               ConcurrentHashMapV8.TreeNode var9;
               if (var7 != null && var8 != null) {
                  ConcurrentHashMapV8.TreeNode var10;
                  for(var9 = var8; (var10 = var9.left) != null; var9 = var10) {
                  }

                  boolean var11 = var9.red;
                  var9.red = var1.red;
                  var1.red = var11;
                  ConcurrentHashMapV8.TreeNode var12 = var9.right;
                  ConcurrentHashMapV8.TreeNode var13 = var1.parent;
                  if (var9 == var8) {
                     var1.parent = var9;
                     var9.right = var1;
                  } else {
                     ConcurrentHashMapV8.TreeNode var14 = var9.parent;
                     if ((var1.parent = var14) != null) {
                        if (var9 == var14.left) {
                           var14.left = var1;
                        } else {
                           var14.right = var1;
                        }
                     }

                     if ((var9.right = var8) != null) {
                        var8.parent = var9;
                     }
                  }

                  var1.left = null;
                  if ((var1.right = var12) != null) {
                     var12.parent = var1;
                  }

                  if ((var9.left = var7) != null) {
                     var7.parent = var9;
                  }

                  if ((var9.parent = var13) == null) {
                     var4 = var9;
                  } else if (var1 == var13.left) {
                     var13.left = var9;
                  } else {
                     var13.right = var9;
                  }

                  if (var12 != null) {
                     var6 = var12;
                  } else {
                     var6 = var1;
                  }
               } else if (var7 != null) {
                  var6 = var7;
               } else if (var8 != null) {
                  var6 = var8;
               } else {
                  var6 = var1;
               }

               if (var6 != var1) {
                  var9 = var6.parent = var1.parent;
                  if (var9 == null) {
                     var4 = var6;
                  } else if (var1 == var9.left) {
                     var9.left = var6;
                  } else {
                     var9.right = var6;
                  }

                  var1.left = var1.right = var1.parent = null;
               }

               this.root = var1.red ? var4 : balanceDeletion(var4, var6);
               if (var1 == var6 && (var9 = var1.parent) != null) {
                  if (var1 == var9.left) {
                     var9.left = null;
                  } else if (var1 == var9.right) {
                     var9.right = null;
                  }

                  var1.parent = null;
               }

               this.unlockRoot();
               if (!$assertionsDisabled && this.root != null) {
                  throw new AssertionError();
               } else {
                  return false;
               }
            } else {
               return true;
            }
         }
      }

      static ConcurrentHashMapV8.TreeNode rotateLeft(ConcurrentHashMapV8.TreeNode var0, ConcurrentHashMapV8.TreeNode var1) {
         ConcurrentHashMapV8.TreeNode var2;
         if (var1 != null && (var2 = var1.right) != null) {
            ConcurrentHashMapV8.TreeNode var4;
            if ((var4 = var1.right = var2.left) != null) {
               var4.parent = var1;
            }

            ConcurrentHashMapV8.TreeNode var3;
            if ((var3 = var2.parent = var1.parent) == null) {
               var0 = var2;
               var2.red = false;
            } else if (var3.left == var1) {
               var3.left = var2;
            } else {
               var3.right = var2;
            }

            var2.left = var1;
            var1.parent = var2;
         }

         return var0;
      }

      static ConcurrentHashMapV8.TreeNode rotateRight(ConcurrentHashMapV8.TreeNode var0, ConcurrentHashMapV8.TreeNode var1) {
         ConcurrentHashMapV8.TreeNode var2;
         if (var1 != null && (var2 = var1.left) != null) {
            ConcurrentHashMapV8.TreeNode var4;
            if ((var4 = var1.left = var2.right) != null) {
               var4.parent = var1;
            }

            ConcurrentHashMapV8.TreeNode var3;
            if ((var3 = var2.parent = var1.parent) == null) {
               var0 = var2;
               var2.red = false;
            } else if (var3.right == var1) {
               var3.right = var2;
            } else {
               var3.left = var2;
            }

            var2.right = var1;
            var1.parent = var2;
         }

         return var0;
      }

      static ConcurrentHashMapV8.TreeNode balanceInsertion(ConcurrentHashMapV8.TreeNode var0, ConcurrentHashMapV8.TreeNode var1) {
         var1.red = true;

         ConcurrentHashMapV8.TreeNode var2;
         while((var2 = var1.parent) != null) {
            ConcurrentHashMapV8.TreeNode var3;
            if (!var2.red || (var3 = var2.parent) == null) {
               return var0;
            }

            ConcurrentHashMapV8.TreeNode var4;
            if (var2 == (var4 = var3.left)) {
               ConcurrentHashMapV8.TreeNode var5;
               if ((var5 = var3.right) != null && var5.red) {
                  var5.red = false;
                  var2.red = false;
                  var3.red = true;
                  var1 = var3;
               } else {
                  if (var1 == var2.right) {
                     var1 = var2;
                     var0 = rotateLeft(var0, var2);
                     var3 = (var2 = var2.parent) == null ? null : var2.parent;
                  }

                  if (var2 != null) {
                     var2.red = false;
                     if (var3 != null) {
                        var3.red = true;
                        var0 = rotateRight(var0, var3);
                     }
                  }
               }
            } else if (var4 != null && var4.red) {
               var4.red = false;
               var2.red = false;
               var3.red = true;
               var1 = var3;
            } else {
               if (var1 == var2.left) {
                  var1 = var2;
                  var0 = rotateRight(var0, var2);
                  var3 = (var2 = var2.parent) == null ? null : var2.parent;
               }

               if (var2 != null) {
                  var2.red = false;
                  if (var3 != null) {
                     var3.red = true;
                     var0 = rotateLeft(var0, var3);
                  }
               }
            }
         }

         var1.red = false;
         return var1;
      }

      static ConcurrentHashMapV8.TreeNode balanceDeletion(ConcurrentHashMapV8.TreeNode var0, ConcurrentHashMapV8.TreeNode var1) {
         while(var1 != null && var1 != var0) {
            ConcurrentHashMapV8.TreeNode var2;
            if ((var2 = var1.parent) == null) {
               var1.red = false;
               return var1;
            }

            if (var1.red) {
               var1.red = false;
               return var0;
            }

            ConcurrentHashMapV8.TreeNode var3;
            ConcurrentHashMapV8.TreeNode var5;
            ConcurrentHashMapV8.TreeNode var6;
            if ((var3 = var2.left) == var1) {
               ConcurrentHashMapV8.TreeNode var4;
               if ((var4 = var2.right) != null && var4.red) {
                  var4.red = false;
                  var2.red = true;
                  var0 = rotateLeft(var0, var2);
                  var4 = (var2 = var1.parent) == null ? null : var2.right;
               }

               if (var4 == null) {
                  var1 = var2;
               } else {
                  var5 = var4.left;
                  var6 = var4.right;
                  if (var6 != null && var6.red || var5 != null && var5.red) {
                     if (var6 == null || !var6.red) {
                        if (var5 != null) {
                           var5.red = false;
                        }

                        var4.red = true;
                        var0 = rotateRight(var0, var4);
                        var4 = (var2 = var1.parent) == null ? null : var2.right;
                     }

                     if (var4 != null) {
                        var4.red = var2 == null ? false : var2.red;
                        if ((var6 = var4.right) != null) {
                           var6.red = false;
                        }
                     }

                     if (var2 != null) {
                        var2.red = false;
                        var0 = rotateLeft(var0, var2);
                     }

                     var1 = var0;
                  } else {
                     var4.red = true;
                     var1 = var2;
                  }
               }
            } else {
               if (var3 != null && var3.red) {
                  var3.red = false;
                  var2.red = true;
                  var0 = rotateRight(var0, var2);
                  var3 = (var2 = var1.parent) == null ? null : var2.left;
               }

               if (var3 == null) {
                  var1 = var2;
               } else {
                  var5 = var3.left;
                  var6 = var3.right;
                  if ((var5 == null || !var5.red) && (var6 == null || !var6.red)) {
                     var3.red = true;
                     var1 = var2;
                  } else {
                     if (var5 == null || !var5.red) {
                        if (var6 != null) {
                           var6.red = false;
                        }

                        var3.red = true;
                        var0 = rotateLeft(var0, var3);
                        var3 = (var2 = var1.parent) == null ? null : var2.left;
                     }

                     if (var3 != null) {
                        var3.red = var2 == null ? false : var2.red;
                        if ((var5 = var3.left) != null) {
                           var5.red = false;
                        }
                     }

                     if (var2 != null) {
                        var2.red = false;
                        var0 = rotateRight(var0, var2);
                     }

                     var1 = var0;
                  }
               }
            }
         }

         return var0;
      }

      static {
         try {
            U = ConcurrentHashMapV8.access$000();
            Class var0 = ConcurrentHashMapV8.TreeBin.class;
            LOCKSTATE = U.objectFieldOffset(var0.getDeclaredField("lockState"));
         } catch (Exception var1) {
            throw new Error(var1);
         }
      }
   }

   static final class TreeNode extends ConcurrentHashMapV8.Node {
      ConcurrentHashMapV8.TreeNode parent;
      ConcurrentHashMapV8.TreeNode left;
      ConcurrentHashMapV8.TreeNode right;
      ConcurrentHashMapV8.TreeNode prev;
      boolean red;

      TreeNode(int var1, Object var2, Object var3, ConcurrentHashMapV8.Node var4, ConcurrentHashMapV8.TreeNode var5) {
         super(var1, var2, var3, var4);
         this.parent = var5;
      }

      ConcurrentHashMapV8.Node find(int var1, Object var2) {
         return this.findTreeNode(var1, var2, (Class)null);
      }

      final ConcurrentHashMapV8.TreeNode findTreeNode(int var1, Object var2, Class var3) {
         if (var2 != null) {
            ConcurrentHashMapV8.TreeNode var4 = this;

            do {
               ConcurrentHashMapV8.TreeNode var9 = var4.left;
               ConcurrentHashMapV8.TreeNode var10 = var4.right;
               int var5;
               if ((var5 = var4.hash) > var1) {
                  var4 = var9;
               } else if (var5 < var1) {
                  var4 = var10;
               } else {
                  Object var7;
                  if ((var7 = var4.key) == var2 || var7 != null && var2.equals(var7)) {
                     return var4;
                  }

                  if (var9 == null && var10 == null) {
                     break;
                  }

                  int var6;
                  if ((var3 != null || (var3 = ConcurrentHashMapV8.comparableClassFor(var2)) != null) && (var6 = ConcurrentHashMapV8.compareComparables(var3, var2, var7)) != 0) {
                     var4 = var6 < 0 ? var9 : var10;
                  } else if (var9 == null) {
                     var4 = var10;
                  } else {
                     ConcurrentHashMapV8.TreeNode var8;
                     if (var10 != null && (var8 = var10.findTreeNode(var1, var2, var3)) != null) {
                        return var8;
                     }

                     var4 = var9;
                  }
               }
            } while(var4 != null);
         }

         return null;
      }
   }

   static final class ReservationNode extends ConcurrentHashMapV8.Node {
      ReservationNode() {
         super(-3, (Object)null, (Object)null, (ConcurrentHashMapV8.Node)null);
      }

      ConcurrentHashMapV8.Node find(int var1, Object var2) {
         return null;
      }
   }

   static final class ForwardingNode extends ConcurrentHashMapV8.Node {
      final ConcurrentHashMapV8.Node[] nextTable;

      ForwardingNode(ConcurrentHashMapV8.Node[] var1) {
         super(-1, (Object)null, (Object)null, (ConcurrentHashMapV8.Node)null);
         this.nextTable = var1;
      }

      ConcurrentHashMapV8.Node find(int var1, Object var2) {
         ConcurrentHashMapV8.Node[] var3 = this.nextTable;

         label41:
         while(true) {
            ConcurrentHashMapV8.Node var4;
            int var5;
            if (var2 != null && var3 != null && (var5 = var3.length) != 0 && (var4 = ConcurrentHashMapV8.tabAt(var3, var5 - 1 & var1)) != null) {
               int var6;
               Object var7;
               while((var6 = var4.hash) != var1 || (var7 = var4.key) != var2 && (var7 == null || !var2.equals(var7))) {
                  if (var6 < 0) {
                     if (!(var4 instanceof ConcurrentHashMapV8.ForwardingNode)) {
                        return var4.find(var1, var2);
                     }

                     var3 = ((ConcurrentHashMapV8.ForwardingNode)var4).nextTable;
                     continue label41;
                  }

                  if ((var4 = var4.next) == null) {
                     return null;
                  }
               }

               return var4;
            }

            return null;
         }
      }
   }

   static class Segment extends ReentrantLock implements Serializable {
      private static final long serialVersionUID = 2249069246763182397L;
      final float loadFactor;

      Segment(float var1) {
         this.loadFactor = var1;
      }
   }

   static class Node implements Entry {
      final int hash;
      final Object key;
      volatile Object val;
      volatile ConcurrentHashMapV8.Node next;

      Node(int var1, Object var2, Object var3, ConcurrentHashMapV8.Node var4) {
         this.hash = var1;
         this.key = var2;
         this.val = var3;
         this.next = var4;
      }

      public final Object getKey() {
         return this.key;
      }

      public final Object getValue() {
         return this.val;
      }

      public final int hashCode() {
         return this.key.hashCode() ^ this.val.hashCode();
      }

      public final String toString() {
         return this.key + "=" + this.val;
      }

      public final Object setValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public final boolean equals(Object var1) {
         Object var2;
         Object var3;
         Object var4;
         Entry var5;
         return var1 instanceof Entry && (var2 = (var5 = (Entry)var1).getKey()) != null && (var3 = var5.getValue()) != null && (var2 == this.key || var2.equals(this.key)) && (var3 == (var4 = this.val) || var3.equals(var4));
      }

      ConcurrentHashMapV8.Node find(int var1, Object var2) {
         ConcurrentHashMapV8.Node var3 = this;
         if (var2 != null) {
            do {
               Object var4;
               if (var3.hash == var1 && ((var4 = var3.key) == var2 || var4 != null && var2.equals(var4))) {
                  return var3;
               }
            } while((var3 = var3.next) != null);
         }

         return null;
      }
   }

   public interface IntByIntToInt {
      int apply(int var1, int var2);
   }

   public interface LongByLongToLong {
      long apply(long var1, long var3);
   }

   public interface DoubleByDoubleToDouble {
      double apply(double var1, double var3);
   }

   public interface ObjectByObjectToInt {
      int apply(Object var1, Object var2);
   }

   public interface ObjectByObjectToLong {
      long apply(Object var1, Object var2);
   }

   public interface ObjectByObjectToDouble {
      double apply(Object var1, Object var2);
   }

   public interface ObjectToInt {
      int apply(Object var1);
   }

   public interface ObjectToLong {
      long apply(Object var1);
   }

   public interface ObjectToDouble {
      double apply(Object var1);
   }

   public interface BiFun {
      Object apply(Object var1, Object var2);
   }

   public interface Fun {
      Object apply(Object var1);
   }

   public interface BiAction {
      void apply(Object var1, Object var2);
   }

   public interface Action {
      void apply(Object var1);
   }

   public interface ConcurrentHashMapSpliterator {
      ConcurrentHashMapV8.ConcurrentHashMapSpliterator trySplit();

      long estimateSize();

      void forEachRemaining(ConcurrentHashMapV8.Action var1);

      boolean tryAdvance(ConcurrentHashMapV8.Action var1);
   }
}
