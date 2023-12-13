package gnu.trove.decorator;

import gnu.trove.list.TIntList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;

public class TIntListDecorator extends AbstractList implements List, Externalizable, Cloneable {
   static final long serialVersionUID = 1L;
   protected TIntList list;

   public TIntListDecorator() {
   }

   public TIntListDecorator(TIntList var1) {
      this.list = var1;
   }

   public TIntList getList() {
      return this.list;
   }

   public int size() {
      return this.list.size();
   }

   public Integer get(int var1) {
      int var2 = this.list.get(var1);
      return var2 == this.list.getNoEntryValue() ? null : var2;
   }

   public Integer set(int var1, Integer var2) {
      int var3 = this.list.set(var1, var2);
      return var3 == this.list.getNoEntryValue() ? null : var3;
   }

   public void add(int var1, Integer var2) {
      this.list.insert(var1, var2);
   }

   public Integer remove(int var1) {
      int var2 = this.list.removeAt(var1);
      return var2 == this.list.getNoEntryValue() ? null : var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this.list = (TIntList)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this.list);
   }

   public Object remove(int var1) {
      return this.remove(var1);
   }

   public void add(int var1, Object var2) {
      this.add(var1, (Integer)var2);
   }

   public Object set(int var1, Object var2) {
      return this.set(var1, (Integer)var2);
   }

   public Object get(int var1) {
      return this.get(var1);
   }
}
