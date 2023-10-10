package me.pianopenguin471.command;

import net.minecraft.util.EnumChatFormatting;
import net.weavemc.loader.api.command.Command;
import org.lwjgl.input.Keyboard;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.utils.Utils;

public class GUICommand extends Command {
    private static final String UsageMessage = EnumChatFormatting.RED + "/gui <key>";
    private static final String InvalidKeyMessage = EnumChatFormatting.RED + "Invalid key.";
    private static final String SuccessMessage = EnumChatFormatting.WHITE + "GUI Bind set to " + EnumChatFormatting.GREEN + "%s" + EnumChatFormatting.WHITE + ".";

    public GUICommand() {
        super("clickgui", "gui", "raven");
    }

    public int getStringAsKeycode(String keyString) {
        if (keyString.equalsIgnoreCase("none")) {
            return 0;
        } else {
            return Keyboard.getKeyIndex(keyString.toUpperCase());
        }
    }

    public String getKeycodeAsString(int keyCode) {
        if (keyCode == 0) {
            return "none";
        } else {
            return Keyboard.getKeyName(keyCode);
        }
    }

    @Override
    public void handle(String[] args) {
        Module guiModule = Raven.moduleManager.getModuleByName("Gui");

        if (args.length != 1) {
            Utils.Player.sendMessageToSelf(UsageMessage);
            return;
        }

        String keyString = args[0];
        int keyCode = getStringAsKeycode(keyString);
        keyString = getKeycodeAsString(keyCode);

        if (keyString.equalsIgnoreCase("none")) {
            Utils.Player.sendMessageToSelf(InvalidKeyMessage);
            return;
        }

        guiModule.setBind(keyCode);
        Utils.Player.sendMessageToSelf(String.format(SuccessMessage, keyString));
    }
}
