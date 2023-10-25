package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.feature.impl.misc.FakeHack;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class FakeHackCommand extends CommandAbstract {

    public FakeHackCommand() {
        super("fakehack", "fakehack", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | remove | clear" + "§3<name>", "§6.fakehack" + ChatFormatting.LIGHT_PURPLE + " add | del | clear" + "§3<name>", "fakehack");
    }

    @Override
    public void execute(String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("fakehack")) {
                    if (arguments[1].equals("add")) {
                        FakeHack.fakeHackers.add(arguments[2]);
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added" + " player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!");
                        NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Added" + " player " + ChatFormatting.RED + arguments[2] + ChatFormatting.WHITE + " as HACKER!", 4, NotificationType.SUCCESS);
                    }
                    if (arguments[1].equals("remove")) {
                        EntityPlayer player = Minecraft.getInstance().world.getPlayerEntityByName(arguments[2]);
                        if (player == null) {
                            ChatHelper.addChatMessage("§cThat player could not be found!");
                            return;
                        }
                        if (FakeHack.isFakeHacker(player)) {
                            FakeHack.removeHacker(player);
                            ChatHelper.addChatMessage(ChatFormatting.GREEN + "Hacker " + ChatFormatting.RED + player.getName() + " " + ChatFormatting.WHITE + "was removed!");
                            NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Hacker " + ChatFormatting.WHITE + "was removed!", 4, NotificationType.SUCCESS);
                        }
                    }
                    if (arguments[1].equals("clear")) {
                        if (FakeHack.fakeHackers.isEmpty()) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Your FakeHack list is empty!");
                            NotificationManager.publicity("FakeHack Manager", "Your FakeHack list is empty!", 4, NotificationType.ERROR);
                            return;
                        }
                        FakeHack.fakeHackers.clear();
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!");
                        NotificationManager.publicity("FakeHack Manager", ChatFormatting.GREEN + "Your FakeHack list " + ChatFormatting.WHITE + " successfully cleared!", 4, NotificationType.SUCCESS);
                    }
                }
            } else {
                ChatHelper.addChatMessage(getUsage());
            }
        } catch (Exception ignored) {

        }
    }
}
