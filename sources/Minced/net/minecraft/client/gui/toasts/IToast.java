// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.init.SoundEvents;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.ResourceLocation;

public interface IToast
{
    public static final ResourceLocation TEXTURE_TOASTS = new ResourceLocation("textures/gui/toasts.png");
    public static final Object NO_TOKEN = new Object();
    
    Visibility draw(final GuiToast p0, final long p1);
    
    default Object getType() {
        return IToast.NO_TOKEN;
    }
    
    public enum Visibility
    {
        SHOW(SoundEvents.UI_TOAST_IN), 
        HIDE(SoundEvents.UI_TOAST_OUT);
        
        private final SoundEvent sound;
        
        private Visibility(final SoundEvent soundIn) {
            this.sound = soundIn;
        }
        
        public void playSound(final SoundHandler handler) {
            handler.playSound(PositionedSoundRecord.getRecord(this.sound, 1.0f, 1.0f));
        }
    }
}
