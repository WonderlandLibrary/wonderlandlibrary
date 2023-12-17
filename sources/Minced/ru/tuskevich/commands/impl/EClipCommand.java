// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import org.apache.commons.lang3.math.NumberUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "eclip", description = "Allows you to clip with elytra")
public class EClipCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        float y = 0.0f;
        if (args[1].equals("bd")) {
            final Minecraft mc = EClipCommand.mc;
            y = -(float)Minecraft.player.posY - 3.0f;
        }
        if (args[1].equals("down")) {
            for (int i = 1; i < 255; ++i) {
                final WorldClient world = EClipCommand.mc.world;
                final Minecraft mc2 = EClipCommand.mc;
                if (world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(-i - 1);
                    break;
                }
                final WorldClient world2 = EClipCommand.mc.world;
                final Minecraft mc3 = EClipCommand.mc;
                if (world2.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                    this.sendMessage(ChatFormatting.BLUE + ".eclip bd");
                    return;
                }
            }
        }
        if (args[1].equals("up")) {
            for (int i = 10; i < 255; ++i) {
                final WorldClient world3 = EClipCommand.mc.world;
                final Minecraft mc4 = EClipCommand.mc;
                if (world3.getBlockState(new BlockPos(Minecraft.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(i + 1);
                    break;
                }
            }
        }
        if (y == 0.0f) {
            if (!NumberUtils.isNumber(args[1])) {
                this.sendMessage("Invalid input");
                return;
            }
            y = Float.parseFloat(args[1]);
        }
        final int elytra = getSlotIDFromItem(Items.ELYTRA);
        if (elytra == -1) {
            this.sendMessage("Elytra not founded");
            return;
        }
        if (elytra != -2) {
            final PlayerControllerMP playerController = EClipCommand.mc.playerController;
            final int windowId = 0;
            final int slotId = elytra;
            final int mouseButton = 1;
            final ClickType pickup = ClickType.PICKUP;
            final Minecraft mc5 = EClipCommand.mc;
            playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
            final PlayerControllerMP playerController2 = EClipCommand.mc.playerController;
            final int windowId2 = 0;
            final int slotId2 = 6;
            final int mouseButton2 = 1;
            final ClickType pickup2 = ClickType.PICKUP;
            final Minecraft mc6 = EClipCommand.mc;
            playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
        }
        final NetHandlerPlayClient connection = EClipCommand.mc.getConnection();
        final Minecraft mc7 = EClipCommand.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc8 = EClipCommand.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc9 = EClipCommand.mc;
        connection.sendPacket(new CPacketPlayer.Position(posX, posY, Minecraft.player.posZ, false));
        final NetHandlerPlayClient connection2 = EClipCommand.mc.getConnection();
        final Minecraft mc10 = EClipCommand.mc;
        final double posX2 = Minecraft.player.posX;
        final Minecraft mc11 = EClipCommand.mc;
        final double posY2 = Minecraft.player.posY;
        final Minecraft mc12 = EClipCommand.mc;
        connection2.sendPacket(new CPacketPlayer.Position(posX2, posY2, Minecraft.player.posZ, false));
        final NetHandlerPlayClient connection3 = EClipCommand.mc.getConnection();
        final Minecraft mc13 = EClipCommand.mc;
        connection3.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
        final NetHandlerPlayClient connection4 = EClipCommand.mc.getConnection();
        final Minecraft mc14 = EClipCommand.mc;
        final double posX3 = Minecraft.player.posX;
        final Minecraft mc15 = EClipCommand.mc;
        final double yIn = Minecraft.player.posY + y;
        final Minecraft mc16 = EClipCommand.mc;
        connection4.sendPacket(new CPacketPlayer.Position(posX3, yIn, Minecraft.player.posZ, false));
        final NetHandlerPlayClient connection5 = EClipCommand.mc.getConnection();
        final Minecraft mc17 = EClipCommand.mc;
        connection5.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
        if (elytra != -2) {
            final PlayerControllerMP playerController3 = EClipCommand.mc.playerController;
            final int windowId3 = 0;
            final int slotId3 = 6;
            final int mouseButton3 = 1;
            final ClickType pickup3 = ClickType.PICKUP;
            final Minecraft mc18 = EClipCommand.mc;
            playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            final PlayerControllerMP playerController4 = EClipCommand.mc.playerController;
            final int windowId4 = 0;
            final int slotId4 = elytra;
            final int mouseButton4 = 1;
            final ClickType pickup4 = ClickType.PICKUP;
            final Minecraft mc19 = EClipCommand.mc;
            playerController4.windowClick(windowId4, slotId4, mouseButton4, pickup4, Minecraft.player);
        }
        final Minecraft mc20 = EClipCommand.mc;
        final EntityPlayerSP player = Minecraft.player;
        final Minecraft mc21 = EClipCommand.mc;
        final double posX4 = Minecraft.player.posX;
        final Minecraft mc22 = EClipCommand.mc;
        final double y2 = Minecraft.player.posY + y;
        final Minecraft mc23 = EClipCommand.mc;
        player.setPosition(posX4, y2, Minecraft.player.posZ);
    }
    
    public static int getSlotIDFromItem(final Item item) {
        final Minecraft mc = EClipCommand.mc;
        for (final ItemStack stack : Minecraft.player.getArmorInventoryList()) {
            if (stack.getItem() == item) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            final Minecraft mc2 = EClipCommand.mc;
            final ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".eclip " + ChatFormatting.BLUE + "<value>/up/down/bd");
    }
}
