// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import exhibition.util.misc.ChatUtil;
import exhibition.management.command.Command;

public class Say extends Command
{
    public Say(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            return;
        }
        if (args.length > 0) {
            final StringBuilder out = new StringBuilder();
            for (final String word : args) {
                out.append(word + " ");
            }
            String message = out.substring(0, out.length() - 1);
            message = message.replaceAll("&", "§");
            ChatUtil.printChat(message);
        }
    }
    
    @Override
    public String getUsage() {
        return "Say <Message>";
    }
}
