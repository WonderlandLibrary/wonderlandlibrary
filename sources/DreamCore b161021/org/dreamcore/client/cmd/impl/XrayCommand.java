package org.dreamcore.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.cmd.CommandAbstract;
import org.dreamcore.client.feature.impl.visual.XRay;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

import java.util.ArrayList;

public class XrayCommand extends CommandAbstract {

    public static ArrayList<Integer> blockIDS = new ArrayList<>();

    public XrayCommand() {
        super("xray", "xray", "§6.xray" + ChatFormatting.LIGHT_PURPLE + " add " + "§3<blockId> | §6.xray" + ChatFormatting.LIGHT_PURPLE + " remove " + "§3<blockId> | §6.xray" + ChatFormatting.LIGHT_PURPLE + " list | §6.xray" + ChatFormatting.LIGHT_PURPLE + " clear", "xray");
    }

    @Override
    public void execute(String... arguments) {
        if (arguments.length >= 2) {
            if (!dreamcore.instance.featureManager.getFeatureByClass(XRay.class).getState()) {
                ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!");
                NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "please enable XRay module!", 4, NotificationType.SUCCESS);
                return;
            }
            if (arguments[0].equalsIgnoreCase("xray")) {
                if (arguments[1].equalsIgnoreCase("add")) {
                    if (!arguments[2].isEmpty()) {
                        if (!blockIDS.contains(Integer.parseInt(arguments[2]))) {
                            blockIDS.add(Integer.parseInt(arguments[2]));
                            ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"");
                            NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "added block: " + ChatFormatting.RED + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", 4, NotificationType.SUCCESS);
                        } else {
                            ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!");
                            NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block already have in list!", 4, NotificationType.SUCCESS);
                        }
                    }
                } else if (arguments[1].equalsIgnoreCase("remove")) {
                    if (blockIDS.contains(new Integer(arguments[2]))) {
                        blockIDS.remove(new Integer(arguments[2]));
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]));
                        NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "removed block by id " + Integer.parseInt(arguments[2]), 4, NotificationType.SUCCESS);
                    } else {
                        ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!");
                        NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "this block doesn't contains in your list!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("list")) {
                    if (!blockIDS.isEmpty()) {
                        for (Integer integer : blockIDS) {
                            ChatHelper.addChatMessage(ChatFormatting.RED + Block.getBlockById(integer).getLocalizedName() + ChatFormatting.LIGHT_PURPLE + " ID: " + integer);
                        }
                    } else {
                        ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("clear")) {
                    if (blockIDS.isEmpty()) {
                        ChatHelper.addChatMessage(ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", ChatFormatting.RED + "Error " + ChatFormatting.WHITE + "your block list is empty!", 4, NotificationType.SUCCESS);
                    } else {
                        blockIDS.clear();
                        ChatHelper.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!");
                        NotificationManager.publicity("XrayManager", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "clear block list!", 4, NotificationType.SUCCESS);
                    }
                }
            }
        }
    }
}
