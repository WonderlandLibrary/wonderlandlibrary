package xyz.northclient.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import xyz.northclient.NorthSingleton;
import xyz.northclient.commands.Command;

import java.util.Objects;

public class CommandBind extends Command {

    public CommandBind() {
        super("bind","Binds module to a key");
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length == 2) {
            if (Objects.nonNull(NorthSingleton.INSTANCE.getModules().get(args[1]))) {
                int key = NorthSingleton.INSTANCE.getModules().get(args[1]).getKeyCode();
                NorthSingleton.logChat(ChatFormatting.GRAY + NorthSingleton.INSTANCE.getModules().get(args[1]).getDisplayName() + ChatFormatting.RESET + " is currently bound to " + ChatFormatting.GRAY + Keyboard.getKeyName(key));
            } else {
                NorthSingleton.logChat("Please provide a valid module to bind a key to!");
            }
        } else if (args.length >= 3) {
            if (Objects.nonNull(NorthSingleton.INSTANCE.getModules().get(args[1]))) {
                if (args[2] != null) {
                    int key = Keyboard.getKeyIndex(args[2].toUpperCase());
                    NorthSingleton.INSTANCE.getModules().get(args[1]).setKeyCode(key);
                    NorthSingleton.logChat(ChatFormatting.GRAY + NorthSingleton.INSTANCE.getModules().get(args[1]).getDisplayName() + ChatFormatting.RESET + " has been bound to " + ChatFormatting.GRAY + args[2].toUpperCase());
                } else {
                    NorthSingleton.logChat("Please provide a key to bind the module to!");
                }
            } else {
                NorthSingleton.logChat("Please provide a valid module to bind a key to!");
            }
        }
    }
}
