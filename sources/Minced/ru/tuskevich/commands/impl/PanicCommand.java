// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import java.util.Iterator;
import ru.tuskevich.util.chat.ChatUtility;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "panic", description = "Allows you to hide cheat")
public class PanicCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length == 1) {
            for (final Module feature : Minced.getInstance().manager.getModules()) {
                if (feature.state) {
                    feature.toggle();
                }
            }
            ChatUtility.addChatMessage("Modules " + ChatFormatting.BLUE + "sucessfully" + ChatFormatting.WHITE + " off");
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.RED + ".panic");
    }
}
