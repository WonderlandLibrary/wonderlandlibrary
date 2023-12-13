package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
   void write(DataOutput var1) throws IOException {
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(64L);
   }

   public NBTBase copy() {
      return new NBTTagEnd();
   }

   public String toString() {
      return "END";
   }

   public byte getId() {
      return 0;
   }
}
