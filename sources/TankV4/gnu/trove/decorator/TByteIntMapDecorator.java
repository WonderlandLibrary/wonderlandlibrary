package gnu.trove.decorator;

import gnu.trove.iterator.TByteIntIterator;
import gnu.trove.map.TByteIntMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TByteIntMapDecorator extends AbstractMap implements Map, Externalizable, Cloneable {
   static final long serialVersionUID = 1L;
   protected TByteIntMap _map;

   public TByteIntMapDecorator() {
   }

   public TByteIntMapDecorator(TByteIntMap var1) {
      this._map = var1;
   }

   public TByteIntMap getMap() {
      return this._map;
   }

   public Integer put(Byte var1, Integer var2) {
      byte var3;
      if (var1 == null) {
         var3 = this._map.getNoEntryKey();
      } else {
         var3 = this.unwrapKey(var1);
      }

      int var4;
      if (var2 == null) {
         var4 = this._map.getNoEntryValue();
      } else {
         var4 = this.unwrapValue(var2);
      }

      int var5 = this._map.put(var3, var4);
      return var5 == this._map.getNoEntryValue() ? null : this.wrapValue(var5);
   }

   public Integer get(Object var1) {
      byte var2;
      if (var1 != null) {
         if (!(var1 instanceof Byte)) {
            return null;
         }

         var2 = this.unwrapKey(var1);
      } else {
         var2 = this._map.getNoEntryKey();
      }

      int var3 = this._map.get(var2);
      return var3 == this._map.getNoEntryValue() ? null : this.wrapValue(var3);
   }

   public void clear() {
      this._map.clear();
   }

   public Integer remove(Object var1) {
      byte var2;
      if (var1 != null) {
         if (!(var1 instanceof Byte)) {
            return null;
         }

         var2 = this.unwrapKey(var1);
      } else {
         var2 = this._map.getNoEntryKey();
      }

      int var3 = this._map.remove(var2);
      return var3 == this._map.getNoEntryValue() ? null : this.wrapValue(var3);
   }

   public Set entrySet() {
      return new AbstractSet(this) {
         final TByteIntMapDecorator this$0;

         {
            this.this$0 = var1;
         }

         public int size() {
            return this.this$0._map.size();
         }

         public boolean isEmpty() {
            return this.this$0.isEmpty();
         }

         public Iterator iterator() {
            return new Iterator(this) {
               private final TByteIntIterator it;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.it = this.this$1.this$0._map.iterator();
               }

               public Entry next() {
                  this.it.advance();
                  byte var1 = this.it.key();
                  Byte var2 = var1 == this.this$1.this$0._map.getNoEntryKey() ? null : this.this$1.this$0.wrapKey(var1);
                  int var3 = this.it.value();
                  Integer var4 = var3 == this.this$1.this$0._map.getNoEntryValue() ? null : this.this$1.this$0.wrapValue(var3);
                  return new Entry(this, var4, var2) {
                     private Integer val;
                     final Integer val$v;
                     final Byte val$key;
                     final <undefinedtype> this$2;

                     {
                        this.this$2 = var1;
                        this.val$v = var2;
                        this.val$key = var3;
                        this.val = this.val$v;
                     }

                     public boolean equals(Object var1) {
                        return var1 instanceof Entry && ((Entry)var1).getKey().equals(this.val$key) && ((Entry)var1).getValue().equals(this.val);
                     }

                     public Byte getKey() {
                        return this.val$key;
                     }

                     public Integer getValue() {
                        return this.val;
                     }

                     public int hashCode() {
                        return this.val$key.hashCode() + this.val.hashCode();
                     }

                     public Integer setValue(Integer var1) {
                        this.val = var1;
                        return this.this$2.this$1.this$0.put(this.val$key, var1);
                     }

                     public Object setValue(Object var1) {
                        return this.setValue((Integer)var1);
                     }

                     public Object getValue() {
                        return this.getValue();
                     }

                     public Object getKey() {
                        return this.getKey();
                     }
                  };
               }

               public boolean hasNext() {
                  return this.it.hasNext();
               }

               public void remove() {
                  this.it.remove();
               }

               public Object next() {
                  return this.next();
               }
            };
         }

         public boolean add(Entry var1) {
            throw new UnsupportedOperationException();
         }

         public boolean remove(Object var1) {
            boolean var2 = false;
            if (var1 != false) {
               Byte var3 = (Byte)((Entry)var1).getKey();
               this.this$0._map.remove(this.this$0.unwrapKey(var3));
               var2 = true;
            }

            return var2;
         }

         public boolean addAll(Collection var1) {
            throw new UnsupportedOperationException();
         }

         public void clear() {
            this.this$0.clear();
         }

         public boolean add(Object var1) {
            return this.add((Entry)var1);
         }
      };
   }

   public boolean containsValue(Object var1) {
      return var1 instanceof Integer && this._map.containsValue(this.unwrapValue(var1));
   }

   public boolean containsKey(Object var1) {
      if (var1 == null) {
         return this._map.containsKey(this._map.getNoEntryKey());
      } else {
         return var1 instanceof Byte && this._map.containsKey(this.unwrapKey(var1));
      }
   }

   public int size() {
      return this._map.size();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();
      int var3 = var1.size();

      while(var3-- > 0) {
         Entry var4 = (Entry)var2.next();
         this.put((Byte)var4.getKey(), (Integer)var4.getValue());
      }

   }

   protected Byte wrapKey(byte var1) {
      return var1;
   }

   protected byte unwrapKey(Object var1) {
      return (Byte)var1;
   }

   protected Integer wrapValue(int var1) {
      return var1;
   }

   protected int unwrapValue(Object var1) {
      return (Integer)var1;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._map = (TByteIntMap)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this._map);
   }

   public Object remove(Object var1) {
      return this.remove(var1);
   }

   public Object put(Object var1, Object var2) {
      return this.put((Byte)var1, (Integer)var2);
   }

   public Object get(Object var1) {
      return this.get(var1);
   }
}
