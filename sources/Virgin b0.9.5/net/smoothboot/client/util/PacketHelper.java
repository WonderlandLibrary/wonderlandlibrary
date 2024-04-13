package net.smoothboot.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class PacketHelper {

    public static MinecraftClient mc = MinecraftClient.getInstance();

    public static void sendPacket(Packet packet) {
        if (mc.player != null) {
            mc.player.networkHandler.sendPacket(packet);
        }
    }

    public static void sendPosition(Vec3d pos) {
        if (mc.player != null) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), mc.player.isOnGround()));
        }
    }

    public static void sendItem(ItemStack item) {
        int slot = 7;
        mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(slot, item));
    }

    public static void sendItem(ItemStack item, int slot) {
        mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(slot, item));
    }

    public void sendPacket2(final Packet<?> packetIn) {
        if (packetIn == null) {
            return;
        }
        Objects.requireNonNull(mc.getNetworkHandler()).getConnection().send(packetIn);
    }
}
