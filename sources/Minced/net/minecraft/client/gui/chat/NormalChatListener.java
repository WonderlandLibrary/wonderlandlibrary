// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.chat;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ChatType;
import net.minecraft.client.Minecraft;

public class NormalChatListener implements IChatListener
{
    private final Minecraft mc;
    
    public NormalChatListener(final Minecraft p_i47393_1_) {
        this.mc = p_i47393_1_;
    }
    
    @Override
    public void say(final ChatType chatTypeIn, final ITextComponent message) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(message);
    }
}
