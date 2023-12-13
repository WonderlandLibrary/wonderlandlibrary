// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.widgets;

import net.minecraft.client.audio.SoundHandler;
import java.awt.Color;

public class CustomLoginButton extends CustomButton
{
    public CustomLoginButton(final int id, final int x, final int y, final int width, final int height, final String message, final Color color) {
        super(id, x, y, width, height, message, color);
    }
    
    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {
    }
}
