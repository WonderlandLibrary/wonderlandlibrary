// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.commands.commands;

import net.augustus.modules.misc.Spammer;
import net.augustus.commands.Command;

public class SpammerCommand extends Command
{
    public SpammerCommand() {
        super(".spammer");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        try {
            Spammer.spamString = "";
            for (int i = 1; i < message.length; ++i) {
                Spammer.spamString = Spammer.spamString + message[i] + " ";
            }
            this.sendChat("Successfully set the spammer message to " + message[1]);
        }
        catch (final ArrayIndexOutOfBoundsException e) {
            this.sendChat("Do \".spammer *message*\"!");
        }
    }
}
