// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.files.impl.MacroConfig;
import ru.fluger.client.cmd.macro.Macro;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;

public class MacroCommand extends CommandAbstract
{
    public MacroCommand() {
        super("macros", "macro", a.h + ".macro" + ChatFormatting.WHITE + " add §3<key> /home_home | §7.macro" + ChatFormatting.WHITE + " remove §3<key> |" + a.h + " .macro" + ChatFormatting.WHITE + " clear §3| §7.macro" + ChatFormatting.WHITE + " list", new String[] { "§7.macro" + ChatFormatting.WHITE + " add §3<key> </home_home> | §7.macro" + ChatFormatting.WHITE + " remove §3<key> | §7.macro" + ChatFormatting.WHITE + " clear | §7.macro" + ChatFormatting.WHITE + " list", "macro" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        final StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        Fluger.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        Fluger.instance.fileManager.getFile(MacroConfig.class).saveFile();
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + (Object)command);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Fluger.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Fluger.instance.macroManager.getMacros().clear();
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
                    }
                    if (arguments[1].equals("remove")) {
                        Fluger.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                    }
                    if (arguments[1].equals("list")) {
                        if (Fluger.instance.macroManager.getMacros().isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Fluger.instance.macroManager.getMacros().forEach(macro -> ChatHelper.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            }
            else {
                ChatHelper.addChatMessage(this.getUsage());
            }
        }
        catch (Exception ex) {}
    }
}
