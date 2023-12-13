// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.rise6;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.augustus.events.EventSendPacket;

public final class BadPacket
{
    private static boolean slot;
    private static boolean attack;
    private static boolean swing;
    private static boolean block;
    private static boolean inventory;
    
    @EventTarget
    public void sendPacket(final EventSendPacket event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C09PacketHeldItemChange) {
            BadPacket.slot = true;
        }
        else if (packet instanceof C0APacketAnimation) {
            BadPacket.swing = true;
        }
        else if (packet instanceof C02PacketUseEntity) {
            BadPacket.attack = true;
        }
        else if (packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging) {
            BadPacket.block = true;
        }
        else if (packet instanceof C0EPacketClickWindow || (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) || packet instanceof C0DPacketCloseWindow) {
            BadPacket.inventory = true;
        }
        else if (packet instanceof C03PacketPlayer) {
            reset();
        }
    }
    
    public static boolean bad() {
        return bad(true, true, true, true, true);
    }
    
    public static boolean bad(final boolean slot, final boolean attack, final boolean swing, final boolean block, final boolean inventory) {
        return (BadPacket.slot && slot) || (BadPacket.attack && attack) || (BadPacket.swing && swing) || (BadPacket.block && block) || (BadPacket.inventory && inventory);
    }
    
    public static void reset() {
        BadPacket.slot = false;
        BadPacket.swing = false;
        BadPacket.attack = false;
        BadPacket.block = false;
        BadPacket.inventory = false;
    }
}
