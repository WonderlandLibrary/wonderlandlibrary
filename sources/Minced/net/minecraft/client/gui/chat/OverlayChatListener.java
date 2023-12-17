// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.chat;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ChatType;
import net.minecraft.client.Minecraft;

public class OverlayChatListener implements IChatListener
{
    private final Minecraft mc;
    
    public OverlayChatListener(final Minecraft minecraftIn) {
        this.mc = minecraftIn;
    }
    
    @Override
    public void say(final ChatType chatTypeIn, final ITextComponent message) {
        this.mc.ingameGUI.setOverlayMessage(message, false);
    }
}
