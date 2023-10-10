// 
// Decompiled by Procyon v0.5.36
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
        if (CommandTest.mc.thePlayer != null) {
            CommandTest.mc.thePlayer.sendChatMessage("/server ac-test");
        }
    }
}
