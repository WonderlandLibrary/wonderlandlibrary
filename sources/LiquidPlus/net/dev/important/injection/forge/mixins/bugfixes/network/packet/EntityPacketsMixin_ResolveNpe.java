/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S19PacketEntityHeadLook
 *  net.minecraft.network.play.server.S19PacketEntityStatus
 *  net.minecraft.world.World
 */
package net.dev.important.injection.forge.mixins.bugfixes.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={S14PacketEntity.class, S19PacketEntityHeadLook.class, S19PacketEntityStatus.class})
public class EntityPacketsMixin_ResolveNpe {
    @Inject(method={"getEntity", "func_149065_a", "func_149381_a", "func_149161_a"}, at={@At(value="HEAD")}, cancellable=true, remap=false)
    private void patcher$addNullCheck(World worldIn, CallbackInfoReturnable<Entity> cir) {
        if (worldIn == null) {
            cir.setReturnValue(null);
        }
    }
}

