// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.client.Minecraft;
import exhibition.management.command.Command;

public class Clear extends Command
{
    public Clear(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
    }
    
    @Override
    public String getUsage() {
        return "Clear";
    }
}
