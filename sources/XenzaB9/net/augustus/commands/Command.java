// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.commands;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.augustus.Augustus;
import net.augustus.utils.interfaces.MC;

public class Command implements MC
{
    protected String command;
    protected String[] message;
    
    public Command(final String command) {
        this.command = command;
    }
    
    public void commandAction(final String[] message) {
        this.message = message;
    }
    
    public void sendChat(final String msg) {
        final String client = "§6[§9" + Augustus.getInstance().getName() + "§6] ";
        Command.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(client + "§7" + msg));
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public void setCommand(final String command) {
        this.command = command;
    }
    
    public void helpMessage() {
    }
}
