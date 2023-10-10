package net.minecraft.client.audio;

public class SoundEventAccessor implements ISoundEventAccessor {
   private final int weight;
   private final SoundPoolEntry entry;

   public SoundPoolEntry cloneEntry() {
      return new SoundPoolEntry(this.entry);
   }

   public int getWeight() {
      return this.weight;
   }

   SoundEventAccessor(SoundPoolEntry var1, int var2) {
      this.entry = var1;
      this.weight = var2;
   }

   public Object cloneEntry() {
      return this.cloneEntry();
   }
}
