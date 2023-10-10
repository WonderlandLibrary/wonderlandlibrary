package gnu.trove.decorator;

import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.map.TObjectLongMap;
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

public class TObjectLongMapDecorator extends AbstractMap implements Map, Externalizable, Cloneable {
   static final long serialVersionUID = 1L;
   protected TObjectLongMap _map;

   public TObjectLongMapDecorator() {
   }

   public TObjectLongMapDecorator(TObjectLongMap var1) {
      this._map = var1;
   }

   public TObjectLongMap getMap() {
      return this._map;
   }

   public Long put(Object var1, Long var2) {
      return var2 == null ? this.wrapValue(this._map.put(var1, this._map.getNoEntryValue())) : this.wrapValue(this._map.put(var1, this.unwrapValue(var2)));
   }

   public Long get(Object var1) {
      long var2 = this._map.get(var1);
      return var2 == this._map.getNoEntryValue() ? null : this.wrapValue(var2);
   }

   public void clear() {
      this._map.clear();
   }

   public Long remove(Object var1) {
      long var2 = this._map.remove(var1);
      return var2 == this._map.getNoEntryValue() ? null : this.wrapValue(var2);
   }

   public Set entrySet() {
      return new AbstractSet(this) {
         final TObjectLongMapDecorator this$0;

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
               private final TObjectLongIterator it;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.it = this.this$1.this$0._map.iterator();
               }

               public Entry next() {
                  this.it.advance();
                  Object var1 = this.it.key();
                  Long var2 = this.this$1.this$0.wrapValue(this.it.value());
                  return new Entry(this, var2, var1) {
                     private Long val;
                     final Long val$v;
                     final Object val$key;
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

                     public Object getKey() {
                        return this.val$key;
                     }

                     public Long getValue() {
                        return this.val;
                     }

                     public int hashCode() {
                        return this.val$key.hashCode() + this.val.hashCode();
                     }

                     public Long setValue(Long var1) {
                        this.val = var1;
                        return this.this$2.this$1.this$0.put(this.val$key, var1);
                     }

                     public Object setValue(Object var1) {
                        return this.setValue((Long)var1);
                     }

                     public Object getValue() {
                        return this.getValue();
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
               Object var3 = ((Entry)var1).getKey();
               this.this$0._map.remove(var3);
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
      return var1 instanceof Long && this._map.containsValue(this.unwrapValue(var1));
   }

   public boolean containsKey(Object var1) {
      return this._map.containsKey(var1);
   }

   public int size() {
      return this._map.size();
   }

   public boolean isEmpty() {
      return this._map.size() == 0;
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();
      int var3 = var1.size();

      while(var3-- > 0) {
         Entry var4 = (Entry)var2.next();
         this.put(var4.getKey(), (Long)var4.getValue());
      }

   }

   protected Long wrapValue(long var1) {
      return var1;
   }

   protected long unwrapValue(Object var1) {
      return (Long)var1;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._map = (TObjectLongMap)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this._map);
   }

   public Object remove(Object var1) {
      return this.remove(var1);
   }

   public Object put(Object var1, Object var2) {
      return this.put(var1, (Long)var2);
   }

   public Object get(Object var1) {
      return this.get(var1);
   }
}
