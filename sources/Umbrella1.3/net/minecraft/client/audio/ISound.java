/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {
    public ResourceLocation getSoundLocation();

    public boolean canRepeat();

    public int getRepeatDelay();

    public float getVolume();

    public float getPitch();

    public float getXPosF();

    public float getYPosF();

    public float getZPosF();

    public AttenuationType getAttenuationType();

    public static enum AttenuationType {
        NONE("NONE", 0, 0),
        LINEAR("LINEAR", 1, 2);

        private final int type;
        private static final AttenuationType[] $VALUES;
        private static final String __OBFID = "CL_00001126";

        static {
            $VALUES = new AttenuationType[]{NONE, LINEAR};
        }

        private AttenuationType(String p_i45110_1_, int p_i45110_2_, int p_i45110_3_) {
            this.type = p_i45110_3_;
        }

        public int getTypeInt() {
            return this.type;
        }
    }
}

