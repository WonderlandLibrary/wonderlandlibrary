package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.viaversion.libs.fastutil.objects.ObjectIterator;

public abstract class LegacySoundRewriter<T extends BackwardsProtocol> extends Rewriter<T> {
  protected final Int2ObjectMap<SoundData> soundRewrites = (Int2ObjectMap<SoundData>)new Int2ObjectOpenHashMap(64);
  
  protected LegacySoundRewriter(T protocol) {
    super(protocol);
  }
  
  public SoundData added(int id, int replacement) {
    return added(id, replacement, -1.0F);
  }
  
  public SoundData added(int id, int replacement, float newPitch) {
    SoundData data = new SoundData(replacement, true, newPitch, true);
    this.soundRewrites.put(id, data);
    return data;
  }
  
  public SoundData removed(int id) {
    SoundData data = new SoundData(-1, false, -1.0F, false);
    this.soundRewrites.put(id, data);
    return data;
  }
  
  public int handleSounds(int soundId) {
    int newSoundId = soundId;
    SoundData data = (SoundData)this.soundRewrites.get(soundId);
    if (data != null)
      return data.getReplacementSound(); 
    for (ObjectIterator<Int2ObjectMap.Entry<SoundData>> objectIterator = this.soundRewrites.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
      Int2ObjectMap.Entry<SoundData> entry = objectIterator.next();
      if (soundId > entry.getIntKey()) {
        if (((SoundData)entry.getValue()).isAdded()) {
          newSoundId--;
          continue;
        } 
        newSoundId++;
      } 
    } 
    return newSoundId;
  }
  
  public boolean hasPitch(int soundId) {
    SoundData data = (SoundData)this.soundRewrites.get(soundId);
    return (data != null && data.isChangePitch());
  }
  
  public float handlePitch(int soundId) {
    SoundData data = (SoundData)this.soundRewrites.get(soundId);
    return (data != null) ? data.getNewPitch() : 1.0F;
  }
  
  public static final class SoundData {
    private final int replacementSound;
    
    private final boolean changePitch;
    
    private final float newPitch;
    
    private final boolean added;
    
    public SoundData(int replacementSound, boolean changePitch, float newPitch, boolean added) {
      this.replacementSound = replacementSound;
      this.changePitch = changePitch;
      this.newPitch = newPitch;
      this.added = added;
    }
    
    public int getReplacementSound() {
      return this.replacementSound;
    }
    
    public boolean isChangePitch() {
      return this.changePitch;
    }
    
    public float getNewPitch() {
      return this.newPitch;
    }
    
    public boolean isAdded() {
      return this.added;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\LegacySoundRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */