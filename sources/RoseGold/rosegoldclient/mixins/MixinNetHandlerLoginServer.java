package rosegoldclient.mixins;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.network.NetHandlerLoginServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerLoginServer.class)
public class MixinNetHandlerLoginServer {
    @Shadow
    private EntityPlayerMP player;
}
