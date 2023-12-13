// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

public class SoundEventAccessor implements ISoundEventAccessor
{
    private final SoundPoolEntry entry;
    private final int weight;
    private static final String __OBFID = "CL_00001153";
    
    SoundEventAccessor(final SoundPoolEntry entry, final int weight) {
        this.entry = entry;
        this.weight = weight;
    }
    
    @Override
    public int getWeight() {
        return this.weight;
    }
    
    @Override
    public SoundPoolEntry cloneEntry() {
        return new SoundPoolEntry(this.entry);
    }
}
