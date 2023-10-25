/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.toasts;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public interface IToast {
    public static final ResourceLocation field_193654_a = new ResourceLocation("textures/gui/toasts.png");
    public static final Object field_193655_b = new Object();

    public Visibility draw(GuiToast var1, long var2);

    default public Object func_193652_b() {
        return field_193655_b;
    }

    public static enum Visibility {
        SHOW(SoundEvents.field_194226_id),
        HIDE(SoundEvents.field_194227_ie);

        private final SoundEvent field_194170_c;

        private Visibility(SoundEvent p_i47607_3_) {
            this.field_194170_c = p_i47607_3_;
        }

        public void func_194169_a(SoundHandler p_194169_1_) {
            p_194169_1_.playSound(PositionedSoundRecord.func_194007_a(this.field_194170_c, 1.0f, 1.0f));
        }
    }
}

