package dev.tenacity.util.network;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public final class PacketUtil {

    private PacketUtil() {
    }

    // This is just temporary, as for some reason packet send event allows server packets as well.
    public static boolean isClientPacket(final Packet<?> packet) {
        return packet instanceof C0APacketAnimation
                || packet instanceof C0BPacketEntityAction
                || packet instanceof C0CPacketInput
                || packet instanceof C0DPacketCloseWindow
                || packet instanceof C0EPacketClickWindow
                || packet instanceof C0FPacketConfirmTransaction
                || packet instanceof C00PacketKeepAlive
                || packet instanceof C01PacketChatMessage
                || packet instanceof C02PacketUseEntity
                || packet instanceof C03PacketPlayer
                || packet instanceof C07PacketPlayerDigging
                || packet instanceof C08PacketPlayerBlockPlacement
                || packet instanceof C09PacketHeldItemChange
                || packet instanceof C10PacketCreativeInventoryAction
                || packet instanceof C11PacketEnchantItem
                || packet instanceof C12PacketUpdateSign
                || packet instanceof C13PacketPlayerAbilities
                || packet instanceof C14PacketTabComplete
                || packet instanceof C15PacketClientSettings
                || packet instanceof C16PacketClientStatus
                || packet instanceof C17PacketCustomPayload
                || packet instanceof C18PacketSpectate
                || packet instanceof C19PacketResourcePackStatus;
    }

}
