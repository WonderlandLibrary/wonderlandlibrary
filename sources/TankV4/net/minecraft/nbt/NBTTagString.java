package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
   private String data;

   public NBTTagString() {
      this.data = "";
   }

   public String getString() {
      return this.data;
   }

   public byte getId() {
      return 8;
   }

   public int hashCode() {
      return super.hashCode() ^ this.data.hashCode();
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         NBTTagString var2 = (NBTTagString)var1;
         return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
      }
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(288L);
      this.data = var1.readUTF();
      var3.read((long)(16 * this.data.length()));
   }

   public NBTTagString(String var1) {
      this.data = var1;
      if (var1 == null) {
         throw new IllegalArgumentException("Empty string not allowed");
      }
   }

   public NBTBase copy() {
      return new NBTTagString(this.data);
   }

   void write(DataOutput var1) throws IOException {
      var1.writeUTF(this.data);
   }

   public String toString() {
      return "\"" + this.data.replace("\"", "\\\"") + "\"";
   }

   public boolean hasNoTags() {
      return this.data.isEmpty();
   }
}
