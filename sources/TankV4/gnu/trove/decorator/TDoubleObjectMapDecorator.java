package gnu.trove.decorator;

import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.TDoubleObjectMap;
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

public class TDoubleObjectMapDecorator extends AbstractMap implements Map, Externalizable, Cloneable {
   static final long serialVersionUID = 1L;
   protected TDoubleObjectMap _map;

   public TDoubleObjectMapDecorator() {
   }

   public TDoubleObjectMapDecorator(TDoubleObjectMap var1) {
      this._map = var1;
   }

   public TDoubleObjectMap getMap() {
      return this._map;
   }

   public Object put(Double var1, Object var2) {
      double var3;
      if (var1 == null) {
         var3 = this._map.getNoEntryKey();
      } else {
         var3 = this.unwrapKey(var1);
      }

      return this._map.put(var3, var2);
   }

   public Object get(Object var1) {
      double var2;
      if (var1 != null) {
         if (!(var1 instanceof Double)) {
            return null;
         }

         var2 = this.unwrapKey((Double)var1);
      } else {
         var2 = this._map.getNoEntryKey();
      }

      return this._map.get(var2);
   }

   public void clear() {
      this._map.clear();
   }

   public Object remove(Object var1) {
      double var2;
      if (var1 != null) {
         if (!(var1 instanceof Double)) {
            return null;
         }

         var2 = this.unwrapKey((Double)var1);
      } else {
         var2 = this._map.getNoEntryKey();
      }

      return this._map.remove(var2);
   }

   public Set entrySet() {
      return new AbstractSet(this) {
         final TDoubleObjectMapDecorator this$0;

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
               private final TDoubleObjectIterator it;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.it = this.this$1.this$0._map.iterator();
               }

               public Entry next() {
                  this.it.advance();
                  double var1 = this.it.key();
                  Double var3 = var1 == this.this$1.this$0._map.getNoEntryKey() ? null : this.this$1.this$0.wrapKey(var1);
                  Object var4 = this.it.value();
                  return new Entry(this, var4, var3) {
                     private Object val;
                     final Object val$v;
                     final Double val$key;
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

                     public Double getKey() {
                        return this.val$key;
                     }

                     public Object getValue() {
                        return this.val;
                     }

                     public int hashCode() {
                        return this.val$key.hashCode() + this.val.hashCode();
                     }

                     public Object setValue(Object var1) {
                        this.val = var1;
                        return this.this$2.this$1.this$0.put(this.val$key, var1);
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
               Double var3 = (Double)((Entry)var1).getKey();
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
      return this._map.containsValue(var1);
   }

   public boolean containsKey(Object var1) {
      if (var1 == null) {
         return this._map.containsKey(this._map.getNoEntryKey());
      } else {
         return var1 instanceof Double && this._map.containsKey((Double)var1);
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
         this.put((Double)var4.getKey(), var4.getValue());
      }

   }

   protected Double wrapKey(double var1) {
      return var1;
   }

   protected double unwrapKey(Double var1) {
      return var1;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._map = (TDoubleObjectMap)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this._map);
   }

   public Object put(Object var1, Object var2) {
      return this.put((Double)var1, var2);
   }
}
