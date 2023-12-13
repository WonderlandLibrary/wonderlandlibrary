package gnu.trove.decorator;

import gnu.trove.list.TByteList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;

public class TByteListDecorator extends AbstractList implements List, Externalizable, Cloneable {
   static final long serialVersionUID = 1L;
   protected TByteList list;

   public TByteListDecorator() {
   }

   public TByteListDecorator(TByteList var1) {
      this.list = var1;
   }

   public TByteList getList() {
      return this.list;
   }

   public int size() {
      return this.list.size();
   }

   public Byte get(int var1) {
      byte var2 = this.list.get(var1);
      return var2 == this.list.getNoEntryValue() ? null : var2;
   }

   public Byte set(int var1, Byte var2) {
      byte var3 = this.list.set(var1, var2);
      return var3 == this.list.getNoEntryValue() ? null : var3;
   }

   public void add(int var1, Byte var2) {
      this.list.insert(var1, var2);
   }

   public Byte remove(int var1) {
      byte var2 = this.list.removeAt(var1);
      return var2 == this.list.getNoEntryValue() ? null : var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this.list = (TByteList)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this.list);
   }

   public Object remove(int var1) {
      return this.remove(var1);
   }

   public void add(int var1, Object var2) {
      this.add(var1, (Byte)var2);
   }

   public Object set(int var1, Object var2) {
      return this.set(var1, (Byte)var2);
   }

   public Object get(int var1) {
      return this.get(var1);
   }
}
