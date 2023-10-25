/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {
    public boolean canRepeat();

    public float getZPosF();

    public int getRepeatDelay();

    public float getPitch();

    public float getXPosF();

    public float getYPosF();

    public ResourceLocation getSoundLocation();

    public AttenuationType getAttenuationType();

    public float getVolume();

    public static enum AttenuationType {
        NONE(0),
        LINEAR(2);

        private final int type;

        public int getTypeInt() {
            return this.type;
        }

        private AttenuationType(int n2) {
            this.type = n2;
        }
    }
}

