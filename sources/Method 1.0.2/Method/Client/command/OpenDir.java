package Method.Client.command;

import Method.Client.managers.FileManager;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

@Command.CommandFeatures(description = "Open Minecraft Directory", Syntax = "OpenDir <Save>|<MC>")
public class OpenDir extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("Save"))
                Desktop.getDesktop().open(FileManager.SaveDir);
            else if (args[0].equalsIgnoreCase("MC"))
                Desktop.getDesktop().open(Minecraft.getMinecraft().gameDir);
        } catch (Exception e) {
            ChatUtils.error("Failed to open Directory. ");
        }
    }

}