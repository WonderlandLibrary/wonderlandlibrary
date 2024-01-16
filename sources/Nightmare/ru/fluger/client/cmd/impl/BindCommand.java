// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import java.util.Iterator;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.helpers.misc.ChatHelper;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;

public class BindCommand extends CommandAbstract
{
    public BindCommand() {
        super("bind", "bind", "§6.bind" + ChatFormatting.RED + " add §7<name> §7<key> " + a.m + "\n[" + a.p + "Fluger Premium" + a.h + "] §6.bind " + ChatFormatting.RED + "remove §7<name> §7<key> \n[" + a.p + "Fluger Premium" + a.h + "] §6.bind " + ChatFormatting.RED + "list ", new String[] { "bind" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length == 4) {
                final String moduleName = arguments[2];
                final String bind = arguments[3].toUpperCase();
                final Feature feature = Fluger.instance.featureManager.getFeatureByLabel(moduleName);
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("add")) {
                    feature.setBind(Keyboard.getKeyIndex(bind));
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationType.SUCCESS);
                }
                else if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("remove")) {
                    feature.setBind(0);
                    ChatHelper.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationManager.publicity("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationType.SUCCESS);
                }
            }
            else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("list")) {
                    for (final Feature f : Fluger.instance.featureManager.getFeatureList()) {
                        if (f.getBind() != 0) {
                            ChatHelper.addChatMessage(f.getLabel() + " : " + Keyboard.getKeyName(f.getBind()));
                        }
                    }
                }
                else {
                    ChatHelper.addChatMessage(this.getUsage());
                }
            }
            else if (arguments[0].equalsIgnoreCase("bind")) {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception ex) {}
    }
}
