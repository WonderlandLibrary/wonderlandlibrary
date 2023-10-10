package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagEnd extends NBTBase {
   public String toString() {
      return "END";
   }

   void write(DataOutput output) throws IOException {
   }

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
      sizeTracker.read(64L);
   }

   public byte getId() {
      return (byte)0;
   }

   public NBTBase copy() {
      return new NBTTagEnd();
   }
}
