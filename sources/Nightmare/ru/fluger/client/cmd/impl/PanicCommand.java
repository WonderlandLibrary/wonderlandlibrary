// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import java.util.Iterator;
import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.Fluger;
import ru.fluger.client.cmd.CommandAbstract;

public class PanicCommand extends CommandAbstract
{
    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", new String[] { "panic" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (final Feature feature : Fluger.instance.featureManager.getFeatureList()) {
                if (feature.getState()) {
                    feature.toggle();
                }
            }
            ChatHelper.addChatMessage(ChatFormatting.GREEN + "\u0423\u0441\u043f\u0435\u0448\u043d\u043e " + ChatFormatting.RED + "\u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b " + ChatFormatting.WHITE + "\u0432\u0441\u0435 \u043c\u043e\u0434\u0443\u043b\u0438!");
        }
    }
}
