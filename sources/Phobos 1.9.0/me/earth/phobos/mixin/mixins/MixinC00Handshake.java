package me.earth.phobos.mixin.mixins;

import me.earth.phobos.features.modules.client.ServerModule;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={C00Handshake.class})
public abstract class MixinC00Handshake {
    @Redirect(method={"writePacketData"}, at=@At(value="INVOKE", target="Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
    public PacketBuffer writePacketDataHook(PacketBuffer packetBuffer, String string) {
        if (ServerModule.getInstance().noFML.getValue().booleanValue()) {
            String ipNoFML = string.substring(0, string.length() - "\u0000FML\u0000".length());
            return packetBuffer.writeString(ipNoFML);
        }
        return packetBuffer.writeString(string);
    }
}

