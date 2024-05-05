package ru.smertnix.celestial.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.command.CommandAbstract;
import ru.smertnix.celestial.command.macro.Macro;
import ru.smertnix.celestial.files.impl.MacroConfig;
import ru.smertnix.celestial.utils.other.ChatUtils;

import org.lwjgl.input.Keyboard;

public class MacroCommand extends CommandAbstract {

    public MacroCommand() {
        super("macros", "macro", TextFormatting.GRAY + ".macro" + ChatFormatting.WHITE + " add " + "<key> /home_home | .macro" + ChatFormatting.WHITE + " remove " + "<key> |" +  TextFormatting.GRAY + " .macro" + ChatFormatting.WHITE + " clear " + "| .macro" + ChatFormatting.WHITE + " list", ".macro" + ChatFormatting.WHITE + " add " + "<key> </home_home> | .macro" + ChatFormatting.WHITE + " remove " + "<key> | .macro" + ChatFormatting.WHITE + " clear " + "| .macro" + ChatFormatting.WHITE + " list", "macro");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        Celestial.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        Celestial.instance.fileManager.getFile(MacroConfig.class).saveFile();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added" + " macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Celestial.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Celestial.instance.macroManager.getMacros().clear();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
                    }
                    if (arguments[1].equals("remove")) {
                        Celestial.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                    }
                    if (arguments[1].equals("list")) {
                        if (Celestial.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Celestial.instance.macroManager.getMacros().forEach(macro -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
            }
        } catch (Exception ignored) {

        }
    }
}
