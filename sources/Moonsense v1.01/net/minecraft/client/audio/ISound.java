// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound
{
    ResourceLocation getSoundLocation();
    
    boolean canRepeat();
    
    int getRepeatDelay();
    
    float getVolume();
    
    float getPitch();
    
    float getXPosF();
    
    float getYPosF();
    
    float getZPosF();
    
    AttenuationType getAttenuationType();
    
    public enum AttenuationType
    {
        NONE("NONE", 0, "NONE", 0, 0), 
        LINEAR("LINEAR", 1, "LINEAR", 1, 2);
        
        private final int type;
        private static final AttenuationType[] $VALUES;
        private static final String __OBFID = "CL_00001126";
        
        static {
            $VALUES = new AttenuationType[] { AttenuationType.NONE, AttenuationType.LINEAR };
        }
        
        private AttenuationType(final String name, final int ordinal, final String p_i45110_1_, final int p_i45110_2_, final int p_i45110_3_) {
            this.type = p_i45110_3_;
        }
        
        public int getTypeInt() {
            return this.type;
        }
    }
}
