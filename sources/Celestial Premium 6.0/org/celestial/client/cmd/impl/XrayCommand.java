/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import net.minecraft.block.Block;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.feature.impl.visuals.XRay;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class XrayCommand
extends CommandAbstract {
    public static ArrayList<Integer> blockIDS = new ArrayList();

    public XrayCommand() {
        super("xray", "xray", "\u00a76.xray" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " add \u00a73<blockId> | \u00a76.xray" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " remove \u00a73<blockId> | \u00a76.xray" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " list | \u00a76.xray" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " clear", "xray");
    }

    @Override
    public void execute(String ... arguments) {
        if (arguments.length >= 2) {
            if (!Celestial.instance.featureManager.getFeatureByClass(XRay.class).getState()) {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "please enable XRay module!");
                NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "please enable XRay module!", 4, NotificationType.SUCCESS);
                return;
            }
            if (arguments[0].equalsIgnoreCase("xray")) {
                if (arguments[1].equalsIgnoreCase("add")) {
                    if (!arguments[2].isEmpty()) {
                        if (!blockIDS.contains(Integer.parseInt(arguments[2]))) {
                            blockIDS.add(Integer.parseInt(arguments[2]));
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "added block: " + (Object)((Object)ChatFormatting.RED) + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"");
                            NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "added block: " + (Object)((Object)ChatFormatting.RED) + "\"" + Block.getBlockById(Integer.parseInt(arguments[2])).getLocalizedName() + "\"", 4, NotificationType.SUCCESS);
                        } else {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "this block already have in list!");
                            NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "this block already have in list!", 4, NotificationType.SUCCESS);
                        }
                    }
                } else if (arguments[1].equalsIgnoreCase("remove")) {
                    if (blockIDS.contains(new Integer(arguments[2]))) {
                        blockIDS.remove(new Integer(arguments[2]));
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "removed block by id " + Integer.parseInt(arguments[2]));
                        NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "removed block by id " + Integer.parseInt(arguments[2]), 4, NotificationType.SUCCESS);
                    } else {
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "this block doesn't contains in your list!");
                        NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "this block doesn't contains in your list!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("list")) {
                    if (!blockIDS.isEmpty()) {
                        for (Integer integer : blockIDS) {
                            ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + Block.getBlockById(integer).getLocalizedName() + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " ID: " + integer);
                        }
                    } else {
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "your block list is empty!", 4, NotificationType.SUCCESS);
                    }
                } else if (arguments[1].equalsIgnoreCase("clear")) {
                    if (blockIDS.isEmpty()) {
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "your block list is empty!");
                        NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.RED) + "Error " + (Object)((Object)ChatFormatting.WHITE) + "your block list is empty!", 4, NotificationType.SUCCESS);
                    } else {
                        blockIDS.clear();
                        ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "clear block list!");
                        NotificationManager.publicity("XrayManager", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "clear block list!", 4, NotificationType.SUCCESS);
                    }
                }
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

