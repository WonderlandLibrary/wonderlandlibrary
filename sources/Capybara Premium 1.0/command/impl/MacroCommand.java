package fun.expensive.client.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.expensive.client.Expensive;
import fun.expensive.client.command.CommandAbstract;
import fun.expensive.client.command.macro.Macro;
import fun.expensive.client.files.impl.MacroConfig;
import fun.expensive.client.utils.other.ChatUtils;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class MacroCommand extends CommandAbstract {

    public MacroCommand() {
        super("macros", "macro", TextFormatting.GRAY + ".macro" + ChatFormatting.WHITE + " add " + "§3<key> /home_home | §7.macro" + ChatFormatting.WHITE + " remove " + "§3<key> |" +  TextFormatting.GRAY + " .macro" + ChatFormatting.WHITE + " clear " + "§3| §7.macro" + ChatFormatting.WHITE + " list", "§7.macro" + ChatFormatting.WHITE + " add " + "§3<key> </home_home> | §7.macro" + ChatFormatting.WHITE + " remove " + "§3<key> | §7.macro" + ChatFormatting.WHITE + " clear " + "| §7.macro" + ChatFormatting.WHITE + " list", "macro");
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
                        Rich.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        Rich.instance.fileManager.getFile(MacroConfig.class).saveFile();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added" + " macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + command);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Rich.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Rich.instance.macroManager.getMacros().clear();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
                    }
                    if (arguments[1].equals("remove")) {
                        Rich.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                    }
                    if (arguments[1].equals("list")) {
                        if (Rich.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Rich.instance.macroManager.getMacros().forEach(macro -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    }
                }
            } else {
                ChatUtils.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
