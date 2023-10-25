/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.math.NumberUtils;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.feature.impl.player.ClipHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class ClipCommand
extends CommandAbstract {
    Minecraft mc = Minecraft.getMinecraft();
    public static boolean canClip;
    public static double playerPosY;

    public ClipCommand() {
        super("vclip", "vclip | hclip", "\u00a76.vclip | (hclip) + | - \u00a73<value>  | \u00a73name \u00a73(\u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u00a73\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u043d\u0438\u0437 \u043a \u00a73\u043f\u0440\u043e\u0442\u0438\u0432\u043d\u0438\u043a\u0443 \u0441 \u0434\u0430\u043c\u0430\u0433\u043e\u043c | \u00a73up (\u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u0445\u043e\u0434\u0438\u0442 \u00a73\u043b\u0443\u0447\u0448\u0443\u044e \u00a73\u0432\u044b\u0441\u043e\u0442\u0443) | \u00a73down (\u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043d\u0430\u0445\u043e\u0434\u0438\u0442 \u00a73\u0432\u044b\u0441\u043e\u0442\u0443 \u043a \u0431\u0435\u0434\u0440\u043e\u043a\u0443", "vclip", "hclip");
    }

    @Override
    public void execute(String ... args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("vclip")) {
                try {
                    if (!(NumberUtils.isNumber(args[1]) || args[1].equals("up") || args[1].equals("down"))) {
                        if (Celestial.instance.featureManager.getFeatureByClass(ClipHelper.class).getState()) {
                            if (args[1].equals(this.mc.player.getName())) {
                                NotificationManager.publicity("Clip Manager", "You " + (Object)((Object)ChatFormatting.RED) + "cannot " + (Object)((Object)ChatFormatting.WHITE) + "clip to " + (Object)((Object)ChatFormatting.GREEN) + "yourself!", 5, NotificationType.ERROR);
                                ChatHelper.addChatMessage("You " + (Object)((Object)ChatFormatting.RED) + "cannot " + (Object)((Object)ChatFormatting.WHITE) + "clip to " + (Object)((Object)ChatFormatting.GREEN) + "yourself!");
                                return;
                            }
                            for (EntityPlayer entityPlayer : this.mc.world.playerEntities) {
                                if (entityPlayer == null || !args[1].equals(entityPlayer.getName())) continue;
                                canClip = true;
                                playerPosY = entityPlayer.posY;
                            }
                        } else {
                            NotificationManager.publicity("Clip Manager", "Please enable " + (Object)((Object)ChatFormatting.RED) + "ClipHelper " + (Object)((Object)ChatFormatting.WHITE) + "to unlock this " + (Object)((Object)ChatFormatting.GREEN) + "command!", 5, NotificationType.ERROR);
                            ChatHelper.addChatMessage("Please enable " + (Object)((Object)ChatFormatting.RED) + "ClipHelper " + (Object)((Object)ChatFormatting.WHITE) + "to unlock this " + (Object)((Object)ChatFormatting.GREEN) + "command!");
                        }
                    }
                    if (NumberUtils.isNumber(args[1])) {
                        this.mc.player.setPositionAndUpdate(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ);
                    }
                    if (args[1].equals("up")) {
                        int startingYPos;
                        BlockPos feet = new BlockPos(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ);
                        int surfaceLevel = this.mc.world.getSeaLevel();
                        int worldHeight = this.mc.world.getActualHeight();
                        if (feet.getY() > surfaceLevel && this.mc.world.getBlockState(feet.up()).getBlock() instanceof BlockAir) {
                            return;
                        }
                        for (int currentIteratedY = startingYPos = Math.max(feet.getY(), surfaceLevel); currentIteratedY < worldHeight; ++currentIteratedY) {
                            BlockPos newPos = new BlockPos(feet.getX(), currentIteratedY, feet.getZ());
                            if (this.mc.world.getBlockState(newPos).getBlock() instanceof BlockAir || newPos.getY() <= feet.getY()) continue;
                            this.mc.player.setPositionAndUpdate(this.mc.player.posX, newPos.getY() + 2, this.mc.player.posZ);
                        }
                    }
                    if (args[1].equals("down")) {
                        this.mc.player.setPositionAndUpdate(this.mc.player.posX, -3.0, this.mc.player.posZ);
                    }
                }
                catch (Exception feet) {
                    // empty catch block
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                double x = this.mc.player.posX;
                double y = this.mc.player.posY;
                double z = this.mc.player.posZ;
                float yaw = this.mc.player.rotationYaw * ((float)Math.PI / 180);
                try {
                    this.mc.player.setPositionAndUpdate(x - Math.sin(yaw) * Double.parseDouble(args[1]), y, z + Math.cos(yaw) * Double.parseDouble(args[1]));
                }
                catch (Exception exception) {}
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

