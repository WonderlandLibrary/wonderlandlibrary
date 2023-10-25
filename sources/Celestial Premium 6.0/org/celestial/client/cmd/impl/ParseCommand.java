/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class ParseCommand
extends CommandAbstract {
    public ParseCommand() {
        super("parse", "parse", "parse", "parse");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("parse")) {
                try {
                    List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(ParseCommand.mc.player.connection.getPlayerInfoMap());
                    File fileFolder = new File(ParseCommand.mc.gameDir + "/celestial/", "parse");
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    File file = new File(ParseCommand.mc.gameDir + "/celestial/parse/", ParseCommand.mc.getCurrentServerData().serverIP.split(":")[0] + ".txt");
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
                    ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully parsed! " + (Object)((Object)ChatFormatting.WHITE) + "please check your game directory");
                    NotificationManager.publicity("Parse Manager", (Object)((Object)ChatFormatting.GREEN) + "Successfully parsed! " + (Object)((Object)ChatFormatting.WHITE) + "please check your game directory", 5, NotificationType.SUCCESS);
                }
                catch (Exception exception) {}
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

