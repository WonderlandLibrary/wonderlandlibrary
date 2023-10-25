package net.minecraft.client.audio;

public class SoundEventAccessor implements ISoundEventAccessor<SoundPoolEntry> {
  private final SoundPoolEntry entry;
  
  private final int weight;
  
  SoundEventAccessor(SoundPoolEntry entry, int weight) {
    this.entry = entry;
    this.weight = weight;
  }
  
  public int getWeight() {
    return this.weight;
  }
  
  public SoundPoolEntry cloneEntry() {
    return new SoundPoolEntry(this.entry);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\audio\SoundEventAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */