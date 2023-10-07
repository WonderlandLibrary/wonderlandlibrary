/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.util.StringConversions;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ClaimFinder
extends Command {
    public ClaimFinder(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            return;
        }
        assert (args.length == 1 && StringConversions.isNumeric(args[0]));
        if (this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityBoat) {
            int i;
            int chunks = Integer.parseInt(args[0]) * 8;
            double topLeftX = this.mc.thePlayer.posX - (double)chunks;
            double topLeftZ = this.mc.thePlayer.posY - (double)chunks;
            for (i = 0; i < Integer.parseInt(args[0]); ++i) {
                int chunk = 16 + i * 16;
                this.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(topLeftX + (double)chunk, this.mc.thePlayer.posY, topLeftZ + (double)chunk, false));
                this.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(topLeftX + (double)chunk, this.mc.thePlayer.posY, topLeftZ + (double)chunk, true));
            }
            for (i = 0; i < 17; ++i) {
                this.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C02PacketUseEntity(this.mc.thePlayer.ridingEntity, C02PacketUseEntity.Action.ATTACK));
            }
            return;
        }
        ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 ERROR: you are not on a boat.");
    }

    @Override
    public String getUsage() {
        return "Invalid Chunk number!";
    }

    public void onEvent(Event event) {
    }
}

