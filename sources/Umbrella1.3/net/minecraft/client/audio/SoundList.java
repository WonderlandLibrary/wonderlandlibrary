/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.audio.SoundCategory;

public class SoundList {
    private final List soundList = Lists.newArrayList();
    private boolean replaceExisting;
    private SoundCategory category;
    private static final String __OBFID = "CL_00001121";

    public List getSoundList() {
        return this.soundList;
    }

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    public void setReplaceExisting(boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }

    public SoundCategory getSoundCategory() {
        return this.category;
    }

    public void setSoundCategory(SoundCategory p_148571_1_) {
        this.category = p_148571_1_;
    }

    public static class SoundEntry {
        private String name;
        private float volume = 1.0f;
        private float pitch = 1.0f;
        private int field_148565_d = 1;
        private Type field_148566_e = Type.FILE;
        private boolean field_148564_f = false;
        private static final String __OBFID = "CL_00001122";

        public String getSoundEntryName() {
            return this.name;
        }

        public void setSoundEntryName(String p_148561_1_) {
            this.name = p_148561_1_;
        }

        public float getSoundEntryVolume() {
            return this.volume;
        }

        public void setSoundEntryVolume(float p_148553_1_) {
            this.volume = p_148553_1_;
        }

        public float getSoundEntryPitch() {
            return this.pitch;
        }

        public void setSoundEntryPitch(float p_148559_1_) {
            this.pitch = p_148559_1_;
        }

        public int getSoundEntryWeight() {
            return this.field_148565_d;
        }

        public void setSoundEntryWeight(int p_148554_1_) {
            this.field_148565_d = p_148554_1_;
        }

        public Type getSoundEntryType() {
            return this.field_148566_e;
        }

        public void setSoundEntryType(Type p_148562_1_) {
            this.field_148566_e = p_148562_1_;
        }

        public boolean isStreaming() {
            return this.field_148564_f;
        }

        public void setStreaming(boolean p_148557_1_) {
            this.field_148564_f = p_148557_1_;
        }

        public static enum Type {
            FILE("FILE", 0, "file"),
            SOUND_EVENT("SOUND_EVENT", 1, "event");

            private final String field_148583_c;
            private static final Type[] $VALUES;
            private static final String __OBFID = "CL_00001123";

            static {
                $VALUES = new Type[]{FILE, SOUND_EVENT};
            }

            private Type(String p_i45109_1_, int p_i45109_2_, String p_i45109_3_) {
                this.field_148583_c = p_i45109_3_;
            }

            public static Type getType(String p_148580_0_) {
                for (Type var4 : Type.values()) {
                    if (!var4.field_148583_c.equals(p_148580_0_)) continue;
                    return var4;
                }
                return null;
            }
        }
    }
}

