package fun.rich.client.command.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.lwjgl.Sys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ParseCommand
        extends CommandAbstract {
    public ParseCommand() {
        super("parser", "parser", ChatFormatting.RED + ".parser" + ChatFormatting.WHITE + " parse | dir", "parser");
    }

    @Override
    public void execute(String... args) {
        if (args.length >= 2) {
            String upperCase = args[1].toUpperCase();

            if (args.length == 2 && upperCase.equalsIgnoreCase("PARSE")) {
                try {
                    List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(ParseCommand.mc.player.connection.getPlayerInfoMap());
                    File fileFolder = new File("C://RichClient/game/", "parser");
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    File file = new File("C://RichClient/game/parser/", ParseCommand.mc.getCurrentServerData().serverIP.split(":")[0] + ".txt");
                    BufferedWriter out = new BufferedWriter(new FileWriter(file));
                    if (file.exists()) {
                        file.delete();
                    } else {
                        file.createNewFile();
                    }
                    for (NetworkPlayerInfo n : players) {
                        if (n.getPlayerTeam().getColorPrefix().length() <= 3) continue;
                        out.write(n.getPlayerTeam().getColorPrefix() + " : " + n.getGameProfile().getName());
                        out.write("\r\n");
                    }
                    out.close();
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully parsed! " + ChatFormatting.WHITE + "please check your game directory");
                    NotificationRenderer.queue(ChatFormatting.GREEN + "Parse Manager", ChatFormatting.GREEN + "Successfully parsed! " + ChatFormatting.WHITE + "please check your game directory", 5, NotificationMode.SUCCESS);
                } catch (Exception exception) {
                }

            } else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                File file = new File("C:\\RichClient\\game\\parser");
                Sys.openURL(file.getAbsolutePath());
            }

        } else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}
