package me.aquavit.liquidsense.injection.forge.mixins.packets;

import me.aquavit.liquidsense.ui.client.gui.elements.AntiForge;
import net.minecraft.client.Minecraft;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(Side.CLIENT)
@Mixin(C00Handshake.class)
public class MixinC00Handshake {
    @Shadow
    private int protocolVersion;

    @Shadow
    public int port;

    @Shadow
    private EnumConnectionState requestedState;

    @Shadow
    public String ip;

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public void writePacketData(PacketBuffer buf) {
        buf.writeVarIntToBuffer(this.protocolVersion);
        buf.writeString(this.ip + (AntiForge.enabled && AntiForge.blockFML && !Minecraft.getMinecraft().isIntegratedServerRunning() ? "" : "\0FML\0"));
        buf.writeShort(this.port);
        buf.writeVarIntToBuffer(this.requestedState.getId());
    }
}