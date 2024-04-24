package me.crimp.claudius.mixins.mixins;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface ICPacketUseEntityMixin {

    @Accessor("entityId")
    void setEntityIdAccessor(int id);

    @Accessor("action")
    void setActionAccessor(CPacketUseEntity.Action action);

}