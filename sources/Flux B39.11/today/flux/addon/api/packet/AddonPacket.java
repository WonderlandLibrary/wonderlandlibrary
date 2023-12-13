package today.flux.addon.api.packet;

import com.soterdev.SoterObfuscator;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import today.flux.addon.api.packet.client.*;
import today.flux.addon.api.packet.server.*;

public class AddonPacket {
    public static AddonPacket INSTANCE = new AddonPacket(null);
    public static Minecraft mc = Minecraft.getMinecraft();
    public Packet nativePacket;

    @Getter
    @Setter
    private boolean cancelled = false;

    public AddonPacket(Packet nativePacket) {
        this.nativePacket = nativePacket;
    }

    public static AddonPacket transformPacket(Packet nativePacket) {
        return INSTANCE.transform(nativePacket);
    }


    private AddonPacket transform(Packet nativePacket) {
        if (nativePacket instanceof C0BPacketEntityAction) {
            return new PacketAction(((C0BPacketEntityAction) nativePacket));
        } else if (nativePacket instanceof C07PacketPlayerDigging) {
            return new PacketDigging(((C07PacketPlayerDigging) nativePacket));
        } else if (nativePacket instanceof C09PacketHeldItemChange) {
            return new PacketSlotChange(((C09PacketHeldItemChange) nativePacket));
        } else if (nativePacket instanceof C0CPacketInput) {
            return new PacketInput(((C0CPacketInput) nativePacket));
        } else if (nativePacket instanceof C00PacketKeepAlive) {
            return new PacketClientKeepAlive(((C00PacketKeepAlive) nativePacket));
        } else if (nativePacket instanceof C03PacketPlayer) {
            if (nativePacket instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                return new PacketPlayer.Position(((C03PacketPlayer.C04PacketPlayerPosition) nativePacket));
            } else if (nativePacket instanceof C03PacketPlayer.C05PacketPlayerLook) {
                return new PacketPlayer.Rotation(((C03PacketPlayer.C05PacketPlayerLook) nativePacket));
            } else if (nativePacket instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                return new PacketPlayer.PositionRotation(((C03PacketPlayer.C06PacketPlayerPosLook) nativePacket));
            } else {
                return new PacketPlayer(((C03PacketPlayer) nativePacket));
            }
        } else if (nativePacket instanceof C01PacketChatMessage) {
            return new PacketSendChat(((C01PacketChatMessage) nativePacket));
        } else if (nativePacket instanceof C0APacketAnimation) {
            return new PacketAnimation(((C0APacketAnimation) nativePacket));
        } else if (nativePacket instanceof C02PacketUseEntity) {
            return new PacketUseEntity(((C02PacketUseEntity) nativePacket));
        } else if (nativePacket instanceof S2EPacketCloseWindow) {
            return new PacketCloseWindow(((S2EPacketCloseWindow) nativePacket));
        } else if (nativePacket instanceof S27PacketExplosion) {
            return new PacketExplosion(((S27PacketExplosion) nativePacket));
        } else if (nativePacket instanceof S02PacketChat) {
            return new PacketReceiveChat(((S02PacketChat) nativePacket));
        } else if (nativePacket instanceof S00PacketKeepAlive) {
            return new PacketServerKeepAlive(((S00PacketKeepAlive) nativePacket));
        } else if (nativePacket instanceof S08PacketPlayerPosLook) {
            return new PacketSetPosition(((S08PacketPlayerPosLook) nativePacket));
        } else if (nativePacket instanceof S03PacketTimeUpdate) {
            return new PacketTime(((S03PacketTimeUpdate) nativePacket));
        } else if (nativePacket instanceof S12PacketEntityVelocity) {
            return new PacketVelocity(((S12PacketEntityVelocity) nativePacket));
        }
        return null;
    }
}
