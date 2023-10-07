// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.commands.commands;

import net.augustus.commands.Command;

public class CommandTest extends Command
{
    public CommandTest() {
        super(".test");
    }
    
    @Override
    public void commandAction(final String[] message) {
        CommandTest.mc.thePlayer.sendChatMessage("Dont mind that. This is a test command");
    }
}
