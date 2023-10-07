// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;

public class SoundList
{
    private final List soundList;
    private boolean replaceExisting;
    private SoundCategory category;
    private static final String __OBFID = "CL_00001121";
    
    public SoundList() {
        this.soundList = Lists.newArrayList();
    }
    
    public List getSoundList() {
        return this.soundList;
    }
    
    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }
    
    public void setReplaceExisting(final boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }
    
    public SoundCategory getSoundCategory() {
        return this.category;
    }
    
    public void setSoundCategory(final SoundCategory p_148571_1_) {
        this.category = p_148571_1_;
    }
    
    public static class SoundEntry
    {
        private String name;
        private float volume;
        private float pitch;
        private int field_148565_d;
        private Type field_148566_e;
        private boolean field_148564_f;
        private static final String __OBFID = "CL_00001122";
        
        public SoundEntry() {
            this.volume = 1.0f;
            this.pitch = 1.0f;
            this.field_148565_d = 1;
            this.field_148566_e = Type.FILE;
            this.field_148564_f = false;
        }
        
        public String getSoundEntryName() {
            return this.name;
        }
        
        public void setSoundEntryName(final String p_148561_1_) {
            this.name = p_148561_1_;
        }
        
        public float getSoundEntryVolume() {
            return this.volume;
        }
        
        public void setSoundEntryVolume(final float p_148553_1_) {
            this.volume = p_148553_1_;
        }
        
        public float getSoundEntryPitch() {
            return this.pitch;
        }
        
        public void setSoundEntryPitch(final float p_148559_1_) {
            this.pitch = p_148559_1_;
        }
        
        public int getSoundEntryWeight() {
            return this.field_148565_d;
        }
        
        public void setSoundEntryWeight(final int p_148554_1_) {
            this.field_148565_d = p_148554_1_;
        }
        
        public Type getSoundEntryType() {
            return this.field_148566_e;
        }
        
        public void setSoundEntryType(final Type p_148562_1_) {
            this.field_148566_e = p_148562_1_;
        }
        
        public boolean isStreaming() {
            return this.field_148564_f;
        }
        
        public void setStreaming(final boolean p_148557_1_) {
            this.field_148564_f = p_148557_1_;
        }
        
        public enum Type
        {
            FILE("FILE", 0, "file"), 
            SOUND_EVENT("SOUND_EVENT", 1, "event");
            
            private final String field_148583_c;
            private static final Type[] $VALUES;
            private static final String __OBFID = "CL_00001123";
            
            private Type(final String p_i45109_1_, final int p_i45109_2_, final String p_i45109_3_) {
                this.field_148583_c = p_i45109_3_;
            }
            
            public static Type getType(final String p_148580_0_) {
                for (final Type var4 : values()) {
                    if (var4.field_148583_c.equals(p_148580_0_)) {
                        return var4;
                    }
                }
                return null;
            }
            
            static {
                $VALUES = new Type[] { Type.FILE, Type.SOUND_EVENT };
            }
        }
    }
}
